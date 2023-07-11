package com.terzo.portal.service;

import com.terzo.portal.dto.DepartmentResponseDTO;
import com.terzo.portal.entity.Department;

import java.util.List;

public interface DepartmentService {

    Department getDepartmentById(int id);

    List<DepartmentResponseDTO> getAllDepartments();
}
