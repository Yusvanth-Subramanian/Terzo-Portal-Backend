package com.terzo.portal.repository;

import com.terzo.portal.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Integer> {

    Department findById(int id);
}
