package com.core.api.modules.core.infrastructure.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.core.api.modules.core.domain.models.User;
import com.core.api.modules.core.domain.ports.dataAccess.UserPersistence;
import com.core.api.modules.core.application.statuses.CoreStatus;
import com.core.api.modules.core.application.services.UserService;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserPersistence userPersistence;

    @Override
    public Set<User> getAll(Integer idUser,String userName,String email,Integer status, Boolean onlyGraph) {
        return userPersistence.getAll(idUser,userName,email,status, onlyGraph);
    }

    @Override
    public User getById(Integer idUser) {
        return userPersistence.getById(idUser);
    }

    @Override
    public User save(User user){
        return userPersistence.save(user);
    }

    @Override
     public User enable(User user){
        user.setStatus(CoreStatus.ENABLE);
        user.getPerson().setStatus(CoreStatus.ENABLE);
        return userPersistence.save(user);
    }

    @Override
     public User disable(User user){
        user.setStatus(CoreStatus.DISABLE);
        user.getPerson().setStatus(CoreStatus.DISABLE);
        return userPersistence.save(user);
    }

    @Override
    public User delete(User user){
        user.setStatus(CoreStatus.DELETED);
        user.getPerson().setStatus(CoreStatus.DELETED);
        return userPersistence.save(user);
    }

}