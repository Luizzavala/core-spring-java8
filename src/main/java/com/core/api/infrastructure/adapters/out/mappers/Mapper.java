package com.core.api.infrastructure.adapters.out.mappers;

import org.hibernate.Hibernate;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Mapper<M, E> {

    public M mapperEntityToModel(E entity, M model) {
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    public Set<M> mapperEntityToModelList(Set<E> entityList, Class<M> modelClass) {
        return entityList.stream().map(entity -> {
            try {
                Constructor<M> constructor = modelClass.getDeclaredConstructor();
                M modelInstance = constructor.newInstance();
                BeanUtils.copyProperties(entity, modelInstance);
                return modelInstance;
            } catch (Exception e) {
                throw new RuntimeException("Error al mapear de entidad a modelo", e);
            }
        }).collect(Collectors.toSet());
    }

    public E mapperModelToEntity(M model, E entity) {
        BeanUtils.copyProperties(model, entity);
        return entity;
    }

    public Set<E> mapperModelToEntityList(Set<M> modelList, Class<E> entityClass) {
        return modelList.stream().map(model -> {
            try {
                Constructor<E> constructor = entityClass.getDeclaredConstructor();
                E entityInstance = constructor.newInstance();
                BeanUtils.copyProperties(model, entityInstance);
                return entityInstance;
            } catch (Exception e) {
                throw new RuntimeException("Error al mapear de modelo a entidad", e);
            }
        }).collect(Collectors.toSet());
    }

    public boolean isInitialized(Object object) {
        if (object == null) {
            return false;
        } else if (object instanceof HibernateProxy) {
            try {
                Hibernate.initialize(object);
                return true;
            } catch (org.hibernate.LazyInitializationException e) {
                return false;
            }
        } else if (object instanceof PersistentSet persistentSet) {
            return persistentSet.wasInitialized();
        }
        return true;
    }
}
