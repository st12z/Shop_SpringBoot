package com.thucjava.shopapp.repository.criteria;

import com.thucjava.shopapp.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchCriteriaImpl implements Consumer<FilterCriteria> {
    private CriteriaBuilder builder;
    private List<Predicate> predicates;
    private Root<Product> root;
    @Override
    public void accept(FilterCriteria param) {
        if(param.getOperation().equalsIgnoreCase(">")) {
            predicates.add(builder.greaterThan(root.get(param.getKey()),(Double)param.getValue()));
        }
        else if(param.getOperation().equalsIgnoreCase("<")) {
            predicates.add(builder.lessThan(root.get(param.getKey()),(Double)param.getValue()));
        }
        else if(param.getOperation().equalsIgnoreCase("=")) {
            if(root.get(param.getKey()).getJavaType()==String.class) {
                predicates.add(builder.like(root.get(param.getKey()),"%"+param.getValue().toString()+"%"));
            }
            else{
                predicates.add(builder.equal(root.get(param.getKey()),param.getValue().toString()));
            }
        }
        else if(param.getOperation().equalsIgnoreCase("in")) {
            List<String> values= Arrays.asList(param.getValue().toString().split(","));
            predicates.add(root.get(param.getKey()).in(values));
        }
        else if(param.getOperation().equalsIgnoreCase("like")) {
            if(param.getKey().equals("name")){
                predicates.add(builder.like(builder.function("REPLACE",String.class,root.get(param.getKey()),builder.literal(" "),builder.literal("")),"%"+param.getValue().toString()+"%"));
            }
            else
            {
                predicates.add(builder.like(builder.function("REPLACE",String.class,root.get(param.getKey()).get("name"),builder.literal(" "),builder.literal("")),"%"+param.getValue().toString()+"%"));
            }
        }
    }
}
