package com.thucjava.shopapp.converter;

import com.thucjava.shopapp.dto.response.*;
import com.thucjava.shopapp.model.*;
import org.modelmapper.ModelMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Converter {
    public static ProductResponse toProductResponse(Product product) {
        ProductResponse productResponse = new ModelMapper().map(product, ProductResponse.class);
        productResponse.setCategoryId(product.getCategory().getId());
        productResponse.setBrandId(product.getBrand().getId());
        List<Review> reviewList = product.getReviews();
        List<ReviewResponse> reviewResponses = reviewList.stream().map(Converter::toReviewResponse).toList();
        productResponse.setReview(reviewResponses);
        return productResponse;
    }
    public static CategoryResponse toCategoryResponse(Category category) {
        CategoryResponse categoryResponse = new ModelMapper().map(category, CategoryResponse.class);
        List<ProductResponse> productResponses=category.getProducts().stream().map(Converter::toProductResponse).collect(Collectors.toList());
        categoryResponse.setProducts(productResponses);
        categoryResponse.setCountBrands((long)category.getBrands().size());
        return categoryResponse;
    }
    public static BrandResponse toBrandResponse(Brand brand) {
        BrandResponse brandResponse = new ModelMapper().map(brand, BrandResponse.class);
        List<ProductResponse> productResponseList=new ArrayList<>();
        if(brand.getProducts()!=null){
            productResponseList= brand.getProducts().stream().map(Converter::toProductResponse).collect(Collectors.toList());
        }
        brandResponse.setProductResponseList(productResponseList);
        return brandResponse;
    }
    public static OrderResponse toOrderResponse(Orders order) {
        UserResponse userReponse = UserResponse.builder()
                .email(order.getEmail())
                .firstName(order.getFirstName())
                .lastName(order.getLastName())
                .phone(order.getPhone())
                .city(order.getCity())
                .district(order.getDistrict())
                .build();
        List< OrderItems> orderItems = order.getOrderItems();
        List<ItemResponse> itemResponses =orderItems.stream().map(item->{
            ProductResponse productResponse = toProductResponse(item.getProduct());
            Long quantity=item.getQuantity();
            return ItemResponse.builder().product(productResponse).quantity(quantity).build();
        }).toList();
        return OrderResponse.builder().items(itemResponses).user(userReponse).build();
    }
    public static OrderResponseBasic toOrderResponseBasic(Orders order) {
        Long totalPrice=order.getOrderItems().stream().reduce(0L,(sum,item)->{
            Long price_new =(long)(item.getProduct().getPrice()*(1-1.0*item.getProduct().getDiscount()/100));
            return sum+= price_new*item.getQuantity();
        },Long::sum);
        return OrderResponseBasic.builder()
                .totalPrice(totalPrice)
                .createDate(order.getCreateDate())
                .email(order.getEmail())
                .firstName(order.getFirstName())
                .lastName(order.getLastName())
                .phone(order.getPhone())
                .city(order.getCity())
                .district(order.getDistrict())
                .orderCode(order.getOrderCode())
                .orderStatus(order.getOrderStatus())
                .build();
    }
    public static UserResponse toUserResponse(User user) {
        UserResponse userResponse = new ModelMapper().map(user, UserResponse.class);
        List<RoleResponse> roles = user.getRoles().stream().map(item->{
            return RoleResponse.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .build();
        }).toList();
        userResponse.setRoles(roles);
        List<RoomChat> roomChats = user.getRoomChats() == null ? new ArrayList<>() : user.getRoomChats();
        List<RoomChatResponse> roomChatResponses = new ArrayList<>();
        if(!roomChats.isEmpty()){
            roomChatResponses = roomChats.stream().map(item->RoomChatResponse.builder().roomId(item.getId()).build()).toList();
        }
        userResponse.setRoomChats(roomChatResponses);
        return userResponse;
    }
    public static ReviewResponse toReviewResponse(Review review) {
        ReviewResponse reviewResponse = ReviewResponse.builder()
                .imageReview(review.getImagePath())
                .user(toUserResponse(review.getUser()))
                .content(review.getContent())
                .fullName(review.getFullName())
                .rate(review.getRate())
                .id(review.getId())
                .productId(review.getProduct().getId())
                .build();
        return reviewResponse;
    }
    public static MessageResponse toMessageResponse(Message message) {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        return  MessageResponse.builder()
                .content(message.getContent())
                .email(message.getUser().getEmail())
                .name(message.getUser().getLastName())
                .avatar(message.getUser().getAvatar())
                .timestamp(sf.format(message.getTimestamp()))
                .dateSend(message.getTimestamp())
                .build();
    }

    public static DiscountResponse toDiscountResponse(Discount discount) {
        return DiscountResponse.builder()
                .id(discount.getId())
                .image(discount.getImage())
                .value(discount.getValue())
                .description(discount.getDescription())
                .quantity(discount.getQuantity())
                .name(discount.getName())
                .active(discount.getActive())
                .build();
    }
}
