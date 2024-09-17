package com.core.api.modules.core.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class User {
    Integer idUser;
    Person person;
    String userName;
    String password;
    String email;
    LocalDateTime createAt;
    LocalDateTime lastLogin;
    LocalDateTime disabledAt;
    Integer isCorporate;
    Integer status;
}
