package com.thucjava.shopapp.repository;

import com.thucjava.shopapp.model.User;
import com.thucjava.shopapp.utils.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByEmail(String email);

    @Query("select u  from User u join u.roles r where r.name =:roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
}
