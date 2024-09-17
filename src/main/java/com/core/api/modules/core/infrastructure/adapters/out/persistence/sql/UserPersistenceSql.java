package com.core.api.modules.core.infrastructure.adapters.out.persistence.sql;

import com.core.api.infrastructure.adapters.out.persistence.sql.CriteriaAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;
import java.util.LinkedHashSet;
import java.util.Set;

import com.core.api.modules.core.domain.models.User;
import com.core.api.modules.core.domain.ports.dataAccess.UserPersistence;
import com.core.api.modules.core.infrastructure.adapters.out.entities.UserEntity;
import com.core.api.modules.core.infrastructure.adapters.out.mappers.UserMapper;
import com.core.api.modules.core.infrastructure.adapters.out.repositories.UserRepository;

@Service
public class UserPersistenceSql implements UserPersistence {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public Set<User> getAll(Integer idUser, String userName, String email, Integer status, Boolean onlyGraph) {
        CriteriaAdapter<UserEntity> criteriaAdapter = new CriteriaAdapter<>(entityManagerFactory);
        criteriaAdapter.init(UserEntity.class);
        Set<Predicate> predicates = new LinkedHashSet<>();
        if (onlyGraph) {
            criteriaAdapter.setFetchGraph("onlyUser.graph");
        }else {
            criteriaAdapter.setFetchGraph("user-graph");
        }
        if (idUser != null) {
            predicates.add(criteriaAdapter.equal("idUser", idUser));
        }
        if (userName != null && !userName.isEmpty()) {
            predicates.add(criteriaAdapter.equal("userName", userName));
        }
        if (email != null && !email.isEmpty()) {
            predicates.add(criteriaAdapter.equal("email", email));
        }
        if (status != null) {
            predicates.add(criteriaAdapter.equal("status", status));
        }
        criteriaAdapter.where(criteriaAdapter.and(predicates.toArray(new Predicate[0])));
        Set<UserEntity> userEntity = criteriaAdapter.getResultList();
        criteriaAdapter.close();
        return userMapper.toModelList(userEntity);
    }

    @Override
    public User getById(Integer idUser) {
        CriteriaAdapter<UserEntity> criteriaAdapter = new CriteriaAdapter<>(entityManagerFactory);
        criteriaAdapter.init(UserEntity.class);
        criteriaAdapter.setFetchGraph("user-graph");
        criteriaAdapter.where(criteriaAdapter.equal("idUser", idUser));
        UserEntity userEntity = criteriaAdapter.getResultList().stream().findFirst().orElse(new UserEntity());
        criteriaAdapter.close();
        if (userEntity.getIdUser() != null) {
            return userMapper.toModel(userEntity);
        } else {
            return null;
        }
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        userEntity = userRepository.save(userEntity);
        return userMapper.toModel(userEntity);
    }

}