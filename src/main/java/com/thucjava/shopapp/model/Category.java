package com.thucjava.shopapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thucjava.shopapp.utils.ConverterToSlug;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    @Column(name="name",columnDefinition = "NVARCHAR(255)",nullable=false)
    private String name;

    @Column(name="slug",unique=true,nullable=false)
    private String slug;


    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY, cascade={CascadeType.PERSIST,CascadeType.MERGE})
    private List<Product> products;


    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY,cascade={CascadeType.PERSIST,CascadeType.MERGE})
    private List<Brand> brands;

    @PrePersist
    @PreUpdate
    public void generateSlug(){
        if(name!=null){
            this.slug= ConverterToSlug.toSlug(name);
        }
    }
}
