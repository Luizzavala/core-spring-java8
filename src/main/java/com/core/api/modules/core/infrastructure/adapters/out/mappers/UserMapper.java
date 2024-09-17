package com.core.api.modules.core.infrastructure.adapters.out.mappers;

import com.core.api.domain.ports.dataAccess.IMapper;
import com.core.api.infrastructure.adapters.out.mappers.Mapper;
import com.core.api.modules.core.domain.models.User;
import com.core.api.modules.core.infrastructure.adapters.out.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class UserMapper extends Mapper<User, UserEntity> implements IMapper<User, UserEntity> {
    @Autowired
    private PersonMapper personMapper;

    @Override
    public User toModel(UserEntity userEntity) {
        User user = mapperEntityToModel(userEntity, new User());
        if (isInitialized(userEntity.getPerson())) {
            user.setPerson(personMapper.toModel(userEntity.getPerson()));
        }
        return user;
    }

    @Override
    public Set<User> toModelList(Set<UserEntity> userEntities) {
        Set<User> users = new LinkedHashSet<>();
        for (UserEntity userEntity : userEntities) {
            users.add(toModel(userEntity));
        }
        return users;
    }

    @Override
    public UserEntity toEntity(User user) {
        UserEntity userEntity = mapperModelToEntity(user, new UserEntity());
        if (isInitialized(user.getPerson())) {
            userEntity.setPerson(personMapper.toEntity(user.getPerson()));
        }
        return userEntity;
    }
}