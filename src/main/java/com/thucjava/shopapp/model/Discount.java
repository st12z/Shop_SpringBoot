package com.thucjava.shopapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Discount extends BaseEntity {
    private String name;
    private String description;
    private Long value;
    private String image;
    private Long quantity;
    @ManyToMany(mappedBy = "discounts")
    private List<User> users;
    private Boolean active;
}
