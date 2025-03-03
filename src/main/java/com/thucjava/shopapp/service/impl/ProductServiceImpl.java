package com.thucjava.shopapp.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.thucjava.shopapp.constant.Constant;
import com.thucjava.shopapp.converter.Converter;
import com.thucjava.shopapp.dto.response.PageResponse;
import com.thucjava.shopapp.dto.response.ProductResponse;
import com.thucjava.shopapp.exception.ResourceNotFoundException;
import com.thucjava.shopapp.model.*;
import com.thucjava.shopapp.repository.BrandRepo;
import com.thucjava.shopapp.repository.CategoryRepo;
import com.thucjava.shopapp.repository.OrderItemRepo;
import com.thucjava.shopapp.repository.ProductRepo;
import com.thucjava.shopapp.repository.criteria.FilterCriteria;
import com.thucjava.shopapp.repository.criteria.ProductSearchCriteriaImpl;
import com.thucjava.shopapp.service.ProductService;
import com.thucjava.shopapp.service.RedisProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final BrandRepo brandRepo;
    private final RestClient.Builder builder;
    @PersistenceContext
    private EntityManager entityManager;
    private final OrderItemRepo orderItemRepo;
    private final RedisProductService redisProductService;
    // Bộ lọc sản phẩm
    @Override
    public PageResponse<?> getAllProductsNativeQuery(String slugCategory,List<String> filters,List<String> sorts,int pageNo) {
        // Dùng sql thủ công
        log.info("getAllProducts");
        StringBuilder builder = new StringBuilder("select * from product p where 1=1");
        // Loc theo category
        if(slugCategory != null) {
            Category category=categoryRepo.findBySlug(slugCategory);
            Long categoryId = category.getId();
            builder.append(" and p.category_id ="+categoryId);
        }


        // Loc theo brand,price,bo nho,tan so

        if(filters != null && !filters.isEmpty()) {
            for(String filter:filters){
                String word[]=filter.split(":");
                if(word.length>=2){
                    String key=word[0];
                    String value=word[1];
                    if(key.equals("brand")){
                        Brand brand =brandRepo.findByName(value);
                        builder.append(" and p.brand_id=" +brand.getId());
                    }
                    else if(value.charAt(0)=='>'){
                        builder.append(" and p." + key + " > "+ Long.parseLong(value.substring(1))*1e6);
                    }
                    else if(value.charAt(0)=='<'){
                        builder.append(" and p." + key + " < "+ Long.parseLong(value.substring(1))*1e6);
                    }
                    else {
                        String []valueEqual=value.split("-");
                        if(key.equals("price")){
                            builder.append(" and p." + key + " between "+ Long.parseLong(valueEqual[0])*1e6 + " and "+ Long.parseLong(valueEqual[1])*1e6);
                        }
                        else{
                            String s= String.join(",",valueEqual);
                            builder.append(" and p." + key + " in ("+s+")");
                        }
                    }
                }
            }
        }
        // Loc theo gia  (sorts=rating:asc,sold:asc)
        if(sorts != null && !sorts.isEmpty()) {
            List<String>sortCondition=sorts.stream().map(sort->{
                String word[]=sort.split(":");
                if(word[1].equals("asc")){
                    return "p."+word[0]+" asc";
                }
                else{
                    return "p."+word[0]+" desc";
                }

            }).collect(Collectors.toList());
            builder.append(" order by "+String.join(",", sortCondition));
        }
        Query countQuery = entityManager.createNativeQuery(builder.toString().replace("select *","select count(*)"));
        int total=((Number)countQuery.getSingleResult()).intValue();
        int limit= Constant.pageSize;
        int offset= (pageNo-1)*limit;
        builder.append(" limit "+limit+" offset "+offset);
        Query query = entityManager.createNativeQuery(builder.toString(),Product.class);
        List<Product> products = query.getResultList();
        List<ProductResponse> productResponses = products.stream().map(Converter::toProductResponse).toList();
        return PageResponse.builder().pageNo(1).total(total).pageSize(Constant.pageSize).dataRes(productResponses).build();
    }

    @Override
    public PageResponse<?> getAllProductsCriteria(String slugCategory, List<String> filters, List<String> sorts, int pageNo) {
        List<FilterCriteria> criteriaList=new ArrayList<>();
        String brand="";
        if(filters != null && !filters.isEmpty()) {
            for(String filter:filters){
                String word[]=filter.split(":");
                if(word.length>=2){
                    String key=word[0];
                    String value=word[1];
                    if(key.equals("brand")){
                        brand=value;
                    }
                    else if(value.charAt(0)=='>'){
                        criteriaList.add(new FilterCriteria(key,">",Long.parseLong(value.substring(1))*1e6));
                    }
                    else if(value.charAt(0)=='<'){
                        criteriaList.add(new FilterCriteria(key,"<",Long.parseLong(value.substring(1))*1e6));
                    }
                    else {
                        String []valueEqual=value.split("-");
                        if(key.equals("price")){
                            criteriaList.add(new FilterCriteria(key,">",Long.parseLong(valueEqual[0])*1e6));
                            criteriaList.add(new FilterCriteria(key,"<",Long.parseLong(valueEqual[1])*1e6));
                        }
                        else{
                            String s= String.join(",",valueEqual);
                            criteriaList.add(new FilterCriteria(key,"in",s));
                        }
                    }
                }
            }
        }
        List<Product> products = getProducts(pageNo,criteriaList,slugCategory, brand,sorts);
        List<ProductResponse> productResponses = products.stream().map(Converter::toProductResponse).toList();
        int totalElements=getTotalElements(pageNo,criteriaList,slugCategory, brand,sorts);
        return PageResponse.builder().pageNo(pageNo).total(totalElements).pageSize(Constant.pageSize).dataRes(productResponses).build();
    }
    // getAllProductsJPQL


    private int getTotalElements(int pageNo, List<FilterCriteria> criteriaList, String slugCategory, String brand, List<String> sorts) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> predicateList=new ArrayList<>();
        ProductSearchCriteriaImpl queryConsumer = new ProductSearchCriteriaImpl(criteriaBuilder,predicateList,root);
        for(FilterCriteria criteria:criteriaList){
            queryConsumer.accept(criteria);
        }
        Predicate predicate =criteriaBuilder.or(predicateList.toArray(new Predicate[0]));
        query.where(predicate);
        // join
        if(StringUtils.hasLength(slugCategory)){
            Join<Category,Product>  categoryJoin = root.join("category");
            Predicate categoryPredicate=criteriaBuilder.equal(categoryJoin.get("slug"),slugCategory);
            query.where(predicate,categoryPredicate);
        }
        if(StringUtils.hasLength(brand)){
            Join<Brand, Product> brandJoin = root.join("brand");
            Predicate brandPredicate = criteriaBuilder.equal(brandJoin.get("name"), brand);
            query.where(predicate, brandPredicate);
        }

        // Loc theo gia  (sorts=rating:asc,sold:asc)
        if(sorts != null && !sorts.isEmpty()) {
            for(String sort:sorts){
                String word[]=sort.split(":");
                String key=word[0];
                String value=word[1];
                if(value.equals("desc")){
                    query.orderBy(criteriaBuilder.desc(root.get(key)));
                }
                else{
                    query.orderBy(criteriaBuilder.asc(root.get(key)));
                }
            }
        }

        return entityManager.createQuery(query).getResultList().size();
    }

    private List<Product> getProducts(int pageNo, List<FilterCriteria> criteriaList,String slugCategory,String brand, List<String> sorts) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> predicateList=new ArrayList<>();
        ProductSearchCriteriaImpl queryConsumer = new ProductSearchCriteriaImpl(criteriaBuilder,predicateList,root);
        criteriaList.forEach(queryConsumer);
        Predicate predicate =criteriaBuilder.or(predicateList.toArray(new Predicate[0]));
        query.where(predicate);
        // join
        if(StringUtils.hasLength(slugCategory)){
            Join<Category,Product>  categoryJoin = root.join("category");
            Predicate categoryPredicate=criteriaBuilder.equal(categoryJoin.get("slug"),slugCategory);
            query.where(predicate,categoryPredicate);
        }
        if(StringUtils.hasLength(brand)){
            Join<Brand, Product> brandJoin = root.join("brand");
            Predicate brandPredicate = criteriaBuilder.equal(brandJoin.get("name"), brand);
            query.where(predicate, brandPredicate);
        }

        // Loc theo gia  (sorts=rating:asc,sold:asc)
        if(sorts != null && !sorts.isEmpty()) {
            for(String sort:sorts){
                String word[]=sort.split(":");
                String key=word[0];
                String value=word[1];
                if(value.equals("desc")){
                    query.orderBy(criteriaBuilder.desc(root.get(key)));
                }
                else{
                    query.orderBy(criteriaBuilder.asc(root.get(key)));
                }
            }
        }

        return entityManager.createQuery(query).setFirstResult((pageNo-1)*Constant.pageSize).setMaxResults(Constant.pageSize).getResultList();
    }

    // Lấy sản phẩm theo slug
    @Override
    public ProductResponse getProductBySlug(String slug) {
        Product product=productRepo.findBySlug(slug);
        return Converter.toProductResponse(product);
    }

    // Tra ve tat ca san pham khong theo phan trang
    @Override
    public List<ProductResponse> getAllProductsBySearch(String search) {

        // JPQL
        StringBuilder builder=new StringBuilder("select p from Product p where 1=1 ");
        builder.append(" and (REPLACE(lower(p.name),' ','' ) like :search " +
                "or REPLACE(lower(p.category.name),' ','' )  like :search " +
                "or REPLACE(lower(p.brand.name),' ','' )  like :search)");
        TypedQuery<Product> query =entityManager.createQuery(builder.toString(),Product.class);
        List<Product> products=query.setParameter("search","%"+search.toLowerCase().replace(" ","")+"%").getResultList();

        List<ProductResponse> productResponses=products.stream().map(Converter::toProductResponse).collect(Collectors.toList());
        return productResponses;
    }

    @Override
    public List<ProductResponse> getAllProductsCriteriaBySearch(String search) {

        try{
            List<ProductResponse> productResponses=redisProductService.getAllProductsSearch(search);
            if(productResponses==null || productResponses.isEmpty()){
                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);
                Root<Product> root=query.from(Product.class);
                List<Predicate> predicateList=new ArrayList<>();
                ProductSearchCriteriaImpl queryConsumer = new ProductSearchCriteriaImpl(criteriaBuilder,predicateList,root);
                List<FilterCriteria> criteriaList=Arrays.asList(
                        new FilterCriteria("name","like",search.toLowerCase().replace(" ","")),
                        new FilterCriteria("category","like",search.toLowerCase().replace(" ","")),
                        new FilterCriteria("brand","like",search.toLowerCase().replace(" ",""))
                );
                criteriaList.forEach(queryConsumer);
                Predicate predicate =criteriaBuilder.or(predicateList.toArray(new Predicate[0]));
                query.where(predicate);
                List<Product> products=entityManager.createQuery(query).getResultList();
                productResponses=products.stream().map(Converter::toProductResponse).collect(Collectors.toList());
                redisProductService.saveAllProductsSearch(search,productResponses);
            }
            return productResponses;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found"));
        return Converter.toProductResponse(product);
    }

    @Override
    public void updateProductRate(String slug) {
        Product product=productRepo.findBySlug(slug);
        List<Review> reviews=product.getReviews();
        if(!reviews.isEmpty()){
            Long rateAverage=(long)reviews.stream().mapToLong(Review::getRate).sum()/reviews.size();
            product.setRate(rateAverage);
        }
        else{
            product.setRate(0l);
        }
        productRepo.save(product);

    }

    @Override
    public Long countProducts() {
        return productRepo.count();
    }

    @Override
    public Long countProductsSold() {
        List<OrderItems> orderItemsList=orderItemRepo.findAll();
        Long totalQuantity=orderItemsList.stream().mapToLong(OrderItems::getQuantity).sum();
        return totalQuantity;
    }

}
