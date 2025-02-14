package com.thucjava.shopapp.model;

import com.github.slugify.Slugify;
import com.thucjava.shopapp.utils.ConverterToSlug;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Column(name="name",columnDefinition = "NVARCHAR(255)",nullable=false)
    private String name;

    @Column(name="description",columnDefinition = "TEXT")
    private String description;

    @Column(name="status")
    private Boolean status;

    @Column(name="slug",unique=true)
    private String slug;

    @Column(name="price")
    private Long price;

    @Column(name="discount")
    private Long discount;

    @Column(name="image",columnDefinition = "TEXT")
    private String image;

    @Column(name="stock")
    private Long stock;

    @Column(name="rate")
    private Long rate;

    @Column(name="sold")
    private Long sold;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="category_id",nullable=false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private List<OrderItems> orderItems;

    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    private List<Review> reviews;

    private int memory;

    private int frequency;

    private int pin;

    private int monitorSize;

    @PrePersist
    @PreUpdate
    public void generateSlug(){
        if(name!=null){
            this.slug= ConverterToSlug.toSlug(name);
        }
    }

}
