package com.core.api.modules.core.application.services;

import java.util.Set;
import com.core.api.modules.core.domain.models.Person;

public interface PersonService {
    Set<Person> getAll(Integer idPerson,Integer status);
    Person getById(Integer idPerson);
    Person save(Person person);
    Person enable(Person person);
    Person disable(Person person);
    Person delete(Person person);
}