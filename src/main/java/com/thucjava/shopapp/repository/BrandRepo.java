package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepo extends JpaRepository<Brand, Long> {
    Brand findByName(String name);
}
