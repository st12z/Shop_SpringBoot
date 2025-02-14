package com.thucjava.shopapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {
    private String name;
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
