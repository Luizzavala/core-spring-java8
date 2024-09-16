package com.core.api.domain.ports.dataAccess;

import java.util.Set;

public interface IMapper<M, E> {
    M toModel(E entity);

    Set<M> toModelList(Set<E> eList);

    E toEntity(M model);
}