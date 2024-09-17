package com.core.api.modules.core.domain.ports.dataAccess;

import java.util.Set;
import com.core.api.modules.core.domain.models.Role;

public interface RolePersistence {
    Set<Role> getAll(Integer idRole,String name,Integer status);
    Role getById(Integer idRole);
    Role save(Role role);
}