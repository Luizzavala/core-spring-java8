package com.core.api.modules.core.domain.ports.dataAccess;

import java.util.Set;
import com.core.api.modules.core.domain.models.User;

public interface UserPersistence {
    Set<User> getAll(Integer idUser,String userName,String email,Integer status, Boolean onlyGraph);
    User getById(Integer idUser);
    User save(User user);
    User login(String userName);
}