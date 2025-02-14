package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Product findBySlug(String slug);

    void deleteBySlug(String slug);
}
