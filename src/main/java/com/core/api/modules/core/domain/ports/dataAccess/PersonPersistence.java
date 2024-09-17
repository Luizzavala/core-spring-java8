package com.core.api.modules.core.domain.ports.dataAccess;

import java.util.Set;
import com.core.api.modules.core.domain.models.Person;

public interface PersonPersistence {
    Set<Person> getAll(Integer idPerson,Integer status);
    Person getById(Integer idPerson);
    Person save(Person person);
}