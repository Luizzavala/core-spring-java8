package com.core.api.modules.core.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Person {
    Integer idPerson;
    String firstName;
    String secondName;
    String lastName;
    String secondLastName;
    String curp;
    Integer status;
}
