package com.terzo.portal.service;

import com.terzo.portal.dto.GetRolesDTO;
import com.terzo.portal.entity.Role;

import java.util.List;

public interface RoleService {

    Role getRoleById(int id);

    List<GetRolesDTO> getRoles();
}
