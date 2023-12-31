package com.terzo.portal.repository;

import com.terzo.portal.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    User findByEmail(String email);

    User findById(int id);

    void deleteById(int id);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(concat('%', :name, '%'))")
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);



}
