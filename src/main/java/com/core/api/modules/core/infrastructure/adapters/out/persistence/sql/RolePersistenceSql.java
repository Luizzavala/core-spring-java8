package com.core.api.modules.core.infrastructure.adapters.out.persistence.sql;

import com.core.api.infrastructure.adapters.out.persistence.sql.CriteriaAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;
import java.util.LinkedHashSet;
import java.util.Set;
import com.core.api.modules.core.domain.models.Role;
import com.core.api.modules.core.domain.ports.dataAccess.RolePersistence;
import com.core.api.modules.core.infrastructure.adapters.out.entities.RoleEntity;
import com.core.api.modules.core.infrastructure.adapters.out.mappers.RoleMapper;
import com.core.api.modules.core.infrastructure.adapters.out.repositories.RoleRepository;

@Service
public class RolePersistenceSql implements RolePersistence {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public Set<Role> getAll(Integer idRole,String name,Integer status) {
        CriteriaAdapter<RoleEntity> criteriaAdapter = new CriteriaAdapter<>(entityManagerFactory);
        criteriaAdapter.init(RoleEntity.class);
        Set<Predicate> predicates = new LinkedHashSet<>();
        if (idRole != null) {
            predicates.add(criteriaAdapter.equal("idRole", idRole));
        }
        if (name != null && !name.isEmpty()) {
            predicates.add(criteriaAdapter.equal("name", name));
        }
        if (status != null) {
            predicates.add(criteriaAdapter.equal("status", status));
        }
        criteriaAdapter.where(criteriaAdapter.and(predicates.toArray(new Predicate[0])));
        Set<RoleEntity> roleEntity = criteriaAdapter.getResultList();
        criteriaAdapter.close();
        return roleMapper.toModelList(roleEntity);
    }

    @Override
    public Role getById(Integer idRole){
        CriteriaAdapter<RoleEntity> criteriaAdapter = new CriteriaAdapter<>(entityManagerFactory);
        criteriaAdapter.init(RoleEntity.class);
        criteriaAdapter.where(criteriaAdapter.equal("idRole", idRole));
        RoleEntity roleEntity = criteriaAdapter.getResultList().stream().findFirst().orElse(new RoleEntity());
        criteriaAdapter.close();
        if( roleEntity.getIdRole() != null ) {
            return roleMapper.toModel(roleEntity);
        } else {
            return null;
        }    }

    @Override
    public Role save(Role role){
        RoleEntity roleEntity = roleMapper.toEntity(role);
        roleEntity = roleRepository.save(roleEntity);
        return roleMapper.toModel(roleEntity);
    }

}