package com.core.api.modules.core.infrastructure.adapters.out.mappers;

import com.core.api.domain.ports.dataAccess.IMapper;
import com.core.api.infrastructure.adapters.out.mappers.Mapper;
import com.core.api.modules.core.domain.models.Person;
import com.core.api.modules.core.infrastructure.adapters.out.entities.PersonEntity;
import org.springframework.stereotype.Service;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
 public class PersonMapper extends Mapper<Person, PersonEntity> implements IMapper<Person, PersonEntity> { 
    @Override
    public Person toModel(PersonEntity personEntity) {
        return mapperEntityToModel(personEntity, new Person());
    }

    @Override
    public Set<Person> toModelList(Set<PersonEntity> personEntities) {
        Set<Person> persons = new LinkedHashSet<>();
        for( PersonEntity personEntity : personEntities) {
            persons.add(toModel(personEntity));
        }
        return persons;
    }

    @Override
    public PersonEntity toEntity(Person person) {
        return mapperModelToEntity(person, new PersonEntity());
    }
}