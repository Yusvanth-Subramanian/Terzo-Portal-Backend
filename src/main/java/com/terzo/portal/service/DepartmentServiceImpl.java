package com.terzo.portal.service;

import com.terzo.portal.dto.DepartmentResponseDTO;
import com.terzo.portal.entity.Department;
import com.terzo.portal.repository.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    DepartmentRepo departmentRepo;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepo departmentRepo) {
        this.departmentRepo = departmentRepo;
    }

    @Override
    public Department getDepartmentById(int id) {
        return departmentRepo.findById(id);
    }

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        return departmentRepo.findAll().stream().map(this::mapToDepartmentResponseDTO).toList();
    }

    private DepartmentResponseDTO mapToDepartmentResponseDTO(Department department) {

        return DepartmentResponseDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .build();

    }
}
