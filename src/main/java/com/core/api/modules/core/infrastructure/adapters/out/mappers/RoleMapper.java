package com.core.api.modules.core.infrastructure.adapters.out.mappers;

import com.core.api.domain.ports.dataAccess.IMapper;
import com.core.api.infrastructure.adapters.out.mappers.Mapper;
import com.core.api.modules.core.domain.models.Role;
import com.core.api.modules.core.infrastructure.adapters.out.entities.RoleEntity;
import org.springframework.stereotype.Service;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
 public class RoleMapper extends Mapper<Role, RoleEntity> implements IMapper<Role, RoleEntity> { 
    @Override
    public Role toModel(RoleEntity roleEntity) {
        return mapperEntityToModel(roleEntity, new Role());
    }

    @Override
    public Set<Role> toModelList(Set<RoleEntity> roleEntities) {
        Set<Role> roles = new LinkedHashSet<>();
        for( RoleEntity roleEntity : roleEntities) {
            roles.add(toModel(roleEntity));
        }
        return roles;
    }

    @Override
    public RoleEntity toEntity(Role role) {
        return mapperModelToEntity(role, new RoleEntity());
    }
}