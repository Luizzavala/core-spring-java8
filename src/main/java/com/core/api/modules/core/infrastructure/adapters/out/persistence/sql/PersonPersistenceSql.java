package com.core.api.modules.core.infrastructure.adapters.out.persistence.sql;

import com.core.api.infrastructure.adapters.out.persistence.sql.CriteriaAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.Predicate;
import java.util.LinkedHashSet;
import java.util.Set;

import com.core.api.modules.core.domain.models.Person;
import com.core.api.modules.core.domain.ports.dataAccess.PersonPersistence;
import com.core.api.modules.core.infrastructure.adapters.out.entities.PersonEntity;
import com.core.api.modules.core.infrastructure.adapters.out.mappers.PersonMapper;
import com.core.api.modules.core.infrastructure.adapters.out.repositories.PersonRepository;

@Service
public class PersonPersistenceSql implements PersonPersistence {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public Set<Person> getAll(Integer idPerson, Integer status) {
        CriteriaAdapter<PersonEntity> criteriaAdapter = new CriteriaAdapter<>(entityManagerFactory);
        criteriaAdapter.init(PersonEntity.class);
        Set<Predicate> predicates = new LinkedHashSet<>();
        if (idPerson != null) {
            predicates.add(criteriaAdapter.equal("idPerson", idPerson));
        }
        if (status != null) {
            predicates.add(criteriaAdapter.equal("status", status));
        }
        criteriaAdapter.where(criteriaAdapter.and(predicates.toArray(new Predicate[0])));
        Set<PersonEntity> personEntity = criteriaAdapter.getResultList();
        criteriaAdapter.close();
        return personMapper.toModelList(personEntity);
    }

    @Override
    public Person getById(Integer idPerson) {
        CriteriaAdapter<PersonEntity> criteriaAdapter = new CriteriaAdapter<>(entityManagerFactory);
        criteriaAdapter.init(PersonEntity.class);
        criteriaAdapter.where(criteriaAdapter.equal("idPerson", idPerson));
        PersonEntity personEntity = criteriaAdapter.getResultList().stream().findFirst().orElse(new PersonEntity());
        criteriaAdapter.close();
        if (personEntity.getIdPerson() != null) {
            return personMapper.toModel(personEntity);
        } else {
            return null;
        }
    }

    @Override
    public Person save(Person person) {
        PersonEntity personEntity = personMapper.toEntity(person);
        personEntity = personRepository.save(personEntity);
        return personMapper.toModel(personEntity);
    }

}