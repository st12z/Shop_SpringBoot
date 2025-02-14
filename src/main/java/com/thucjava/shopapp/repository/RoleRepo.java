package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    List<Role> findByIdNot(Long id);

    List<Role> findByIdIn(List<Long> roleIds);
}
