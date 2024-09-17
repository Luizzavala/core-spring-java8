package com.core.api.modules.core.application.services;

import java.util.Set;

import com.core.api.modules.core.domain.models.User;

public interface UserService {
    Set<User> getAll(Integer idUser, String userName, String email, Integer status, Boolean onlyGraph);

    User getById(Integer idUser);

    User save(User user);

    User enable(User user);

    User disable(User user);

    User delete(User user);
}