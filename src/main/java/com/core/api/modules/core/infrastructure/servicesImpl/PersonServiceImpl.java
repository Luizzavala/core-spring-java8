package com.core.api.modules.core.infrastructure.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.core.api.modules.core.domain.models.Person;
import com.core.api.modules.core.domain.ports.dataAccess.PersonPersistence;
import com.core.api.modules.core.application.statuses.CoreStatus;
import com.core.api.modules.core.application.services.PersonService;
import java.util.Set;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonPersistence personPersistence;

    @Override
    public Set<Person> getAll(Integer idPerson,Integer status) {
        return personPersistence.getAll(idPerson,status);
    }

    @Override
    public Person getById(Integer idPerson) {
        return personPersistence.getById(idPerson);
    }

    @Override
    public Person save(Person person){ 
        return personPersistence.save(person);
    }

    @Override
     public Person enable(Person person){
        person.setStatus(CoreStatus.ENABLE);
        return personPersistence.save(person);
    }

    @Override
     public Person disable(Person person){
        person.setStatus(CoreStatus.DISABLE);
        return personPersistence.save(person);
    }

    @Override
    public Person delete(Person person){
        person.setStatus(CoreStatus.DELETED);
        return personPersistence.save(person);
    }

}