package com.terzo.portal.repository;

import com.terzo.portal.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role,Integer> {

    Role findById(int id);
}
