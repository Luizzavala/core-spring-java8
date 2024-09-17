package com.core.api.modules.core.domain.ports.gateway;

import com.core.api.domain.models.Response;
import com.core.api.modules.core.domain.models.Person;

public interface PersonRest {
    Response<?> getAll(Integer idPerson,Integer status);
    Response<?> getById(Integer idPerson);
    Response<?> save(Person person);
    Response<?> enable(Person person);
    Response<?> disable(Person person);
    Response<?> delete(Person person);
}