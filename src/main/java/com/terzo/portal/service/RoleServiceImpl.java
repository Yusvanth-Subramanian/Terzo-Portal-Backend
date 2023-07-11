package com.terzo.portal.service;

import com.terzo.portal.entity.Role;
import com.terzo.portal.repository.RoleRepo;
import org.springframework.stereotype.Service;

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
}
