package com.core.api.modules.core.infrastructure.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.core.api.modules.core.domain.models.Role;
import com.core.api.modules.core.domain.ports.dataAccess.RolePersistence;
import com.core.api.modules.core.application.statuses.CoreStatus;
import com.core.api.modules.core.application.services.RoleService;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RolePersistence rolePersistence;

    @Override
    public Set<Role> getAll(Integer idRole,String name,Integer status) {
        return rolePersistence.getAll(idRole,name,status);
    }

    @Override
    public Role getById(Integer idRole) {
        return rolePersistence.getById(idRole);
    }

    @Override
    public Role save(Role role){ 
        return rolePersistence.save(role);
    }

    @Override
     public Role enable(Role role){
        role.setStatus(CoreStatus.ENABLE);
        return rolePersistence.save(role);
    }

    @Override
     public Role disable(Role role){
        role.setStatus(CoreStatus.DISABLE);
        return rolePersistence.save(role);
    }

    @Override
    public Role delete(Role role){
        role.setStatus(CoreStatus.DELETED);
        return rolePersistence.save(role);
    }

}