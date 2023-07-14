package com.terzo.portal.service;

import com.terzo.portal.dto.GetRolesDTO;
import com.terzo.portal.entity.Role;
import com.terzo.portal.repository.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService{

    RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Role getRoleById(int id) {
        return roleRepo.findById(id);
    }

    @Override
    public List<GetRolesDTO> getRoles() {
        List<Role> roles = roleRepo.findAll();
        return roles.stream()
                .map(this::mapToGrtRolesDTO).collect(Collectors.toList());
    }

    private GetRolesDTO  mapToGrtRolesDTO(Role role) {
        return GetRolesDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
