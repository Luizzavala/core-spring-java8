package com.core.api.modules.core.infrastructure.adapters.out.entities;

import com.core.api.modules.core.domain.models.Person;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "core_users")
@Getter
@Setter
@NamedEntityGraph(
        name = "user-graph",
        attributeNodes = {
                @NamedAttributeNode( value = "person" )
        }
)
@NamedEntityGraph(
        name= "onlyUser.graph",
        attributeNodes = @NamedAttributeNode( value = "person")
)
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer idUser;
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_person", nullable = false )
    private PersonEntity person;
    @Column(name = "username")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "create_at")
    private LocalDateTime createAt;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @Column(name = "disabled_at")
    private LocalDateTime disabledAt;
    @Column(name = "is_corporate")
    private Integer isCorporate;
    @Column(name = "status")
    private Integer status;
}