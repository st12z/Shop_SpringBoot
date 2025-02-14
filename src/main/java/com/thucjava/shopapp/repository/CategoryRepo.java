package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    Category findBySlug(String slug);

    void deleteBySlug(String slug);
}
