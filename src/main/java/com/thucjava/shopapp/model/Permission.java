package com.thucjava.shopapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Permission extends BaseEntity {
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Role> roles;
}
