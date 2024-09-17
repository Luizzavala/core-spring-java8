package com.core.api.modules.core.application.services;

import java.util.Set;
import com.core.api.modules.core.domain.models.Role;

public interface RoleService {
    Set<Role> getAll(Integer idRole,String name,Integer status);
    Role getById(Integer idRole);
    Role save(Role role);
    Role enable(Role role);
    Role disable(Role role);
    Role delete(Role role);
}