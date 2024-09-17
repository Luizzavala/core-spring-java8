package com.core.api.modules.core.domain.ports.gateway;

import com.core.api.domain.models.Response;
import com.core.api.modules.core.domain.models.Role;

public interface RoleRest {
    Response<?> getAll(Integer idRole,String name,Integer status);
    Response<?> getById(Integer idRole);
    Response<?> save(Role role);
    Response<?> enable(Role role);
    Response<?> disable(Role role);
    Response<?> delete(Role role);
}