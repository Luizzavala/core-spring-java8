package com.core.api.modules.core.infrastructure.adapters.out.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.core.api.modules.core.infrastructure.adapters.out.entities.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer>{
}