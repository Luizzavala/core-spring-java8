package com.core.api.modules.core.infrastructure.adapters.out.entities;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_roles")
@Getter
@Setter
public class RoleEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Integer idRole;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private Integer status;
}