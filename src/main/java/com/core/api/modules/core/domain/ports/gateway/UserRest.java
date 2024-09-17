package com.core.api.modules.core.domain.ports.gateway;

import com.core.api.domain.models.Response;
import com.core.api.modules.core.domain.models.User;

public interface UserRest {
    Response<?> getAll(Integer idUser,String userName,String email,Integer status, Boolean onlyGraph);
    Response<?> getById(Integer idUser);
    Response<?> save(User user);
    Response<?> enable(User user);
    Response<?> disable(User user);
    Response<?> delete(User user);
}