package com.core.api.infrastructure.adapters.out.persistence.sql;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;

public class CriteriaAdapter<E> {

    private final SessionFactory sessionFactory;
    private Session session;
    private CriteriaBuilder builder;
    private CriteriaQuery<E> criteriaQuery;
    private Root<E> rootQuery;
    private String fetchGraph;
    private Integer limit;

    public CriteriaAdapter(EntityManagerFactory entityManagerFactory) {
        if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
            throw new IllegalArgumentException("Factory is not a Hibernate factory");
        }
        this.sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    }

    @Transactional
    public void init(Class<E> clazz) {
        session = sessionFactory.openSession();
        builder = session.getCriteriaBuilder();
        criteriaQuery = builder.createQuery(clazz);
        rootQuery = criteriaQuery.from(clazz);
    }

    public CriteriaQuery<E> getCriteriaQuery() {
        return criteriaQuery;
    }

    public Root<E> getRootQuery() {
        return rootQuery;
    }

    public void distinct() {
        criteriaQuery.select(rootQuery).distinct(true);
    }

    public void select() {
        criteriaQuery.select(rootQuery);
    }

    public Fetch<E, ?> fetch(String entity) {
        return rootQuery.fetch(entity);
    }

    public Fetch<E, ?> fetch(String entity, JoinType joinType) {
        return rootQuery.fetch(entity, joinType);
    }

    public CriteriaBuilder getBuilder() {
        return builder;
    }

    public void where(Expression<Boolean> expression) {
        criteriaQuery.where(expression);
    }

    public void setFetchGraph(String graph) {
        this.fetchGraph = graph;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Set<E> getResultList() {
        try (Session session = sessionFactory.openSession()) {
            Query<E> query = createQuery(session);
            return new LinkedHashSet<>(query.getResultList());
        }
    }

    public Set<E> getResultList(Integer limit) {
        setLimit(limit);
        return getResultList();
    }

    public Optional<E> getSingleResult() {
        try (Session session = sessionFactory.openSession()) {
            Query<E> result = createQuery(session);
            return Optional.ofNullable(result.uniqueResult());
        }
    }

    public Optional<E> getSingleResult(Integer limit, Map<String, Boolean> orders) {
        applyOrders(orders);
        return getSingleResult();
    }

    public Predicate like(String attr, String pattern) {
        return builder.like(rootQuery.get(attr), pattern);
    }

    public Predicate and(Predicate... predicates) {
        return builder.and(predicates);
    }

    public Predicate or(Predicate... predicates) {
        return builder.or(predicates);
    }

    public Predicate equal(String attribute, Object value) {
        return builder.equal(rootQuery.get(attribute), value);
    }

    public Predicate equal(String attribute, Object... values) {
        return combinePredicates(attribute, values, this::equal);
    }

    public Predicate notEqual(String attribute, Object... values) {
        return combinePredicates(attribute, values, this::notEqual);
    }

    public Predicate lessThanDate(String attribute, Date value) {
        return builder.lessThan(rootQuery.get(attribute), value);
    }

    public Predicate lessOrEqualDate(String attribute, LocalDate value) {
        return builder.lessThanOrEqualTo(rootQuery.get(attribute), value);
    }

    public Predicate greaterThanDate(String attribute, LocalDate value) {
        return builder.greaterThan(rootQuery.get(attribute), value);
    }

    public Predicate greaterOrEqualDate(String attribute, LocalDate value) {
        return builder.greaterThanOrEqualTo(rootQuery.get(attribute), value);
    }

    public Predicate isNull(String attribute) {
        return builder.isNull(rootQuery.get(attribute));
    }

    public Predicate isNotNull(String attribute) {
        return builder.isNotNull(rootQuery.get(attribute));
    }

    public Predicate between(String attribute, Date startDate, Date endDate) {
        return builder.between(rootQuery.get(attribute), startDate, endDate);
    }

    public Predicate in(String attribute, List<?> values) {
        return rootQuery.get(attribute).in(values);
    }

    public void orderBy(String attribute) {
        criteriaQuery.orderBy(builder.asc(rootQuery.get(attribute)));
    }

    public void orderByDesc(String attribute) {
        criteriaQuery.orderBy(builder.desc(rootQuery.get(attribute)));
    }

    public void groupBy(String attribute) {
        criteriaQuery.groupBy(rootQuery.get(attribute));
    }

    public void groupBy(List<String> attributes) {
        List<Expression<?>> groupByExpressions = new ArrayList<>();
        for (String attribute : attributes) {
            groupByExpressions.add(rootQuery.get(attribute));
        }
        criteriaQuery.groupBy(groupByExpressions);
    }

    public void close() {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    private Query<E> createQuery(Session session) {
        Query<E> query = session.createQuery(criteriaQuery);
        if (fetchGraph != null) {
            EntityGraph<?> entityGraph = session.getEntityGraph(fetchGraph);
            query.setHint("javax.persistence.fetchgraph", entityGraph);
        }
        if (limit != null) {
            query.setMaxResults(limit);
        }
        return query;
    }

    private void applyOrders(Map<String, Boolean> orders) {
        if (orders != null && !orders.isEmpty()) {
            List<Order> orderList = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : orders.entrySet()) {
                Order order = entry.getValue() ? builder.asc(rootQuery.get(entry.getKey())) : builder.desc(rootQuery.get(entry.getKey()));
                orderList.add(order);
            }
            criteriaQuery.orderBy(orderList);
        }
    }

    private Predicate combinePredicates(String attribute, Object[] values, BiFunction<String, Object, Predicate> predicateFunction) {
        return builder.and(Arrays.stream(values).map(value -> predicateFunction.apply(attribute, value)).toArray(Predicate[]::new));
    }
}
