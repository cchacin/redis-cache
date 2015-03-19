package com.groupon.getaways.persistence;


import com.groupon.getaways.annotations.Find;
import com.groupon.getaways.annotations.Merge;
import com.groupon.getaways.annotations.NamedQuery;
import com.groupon.getaways.annotations.Persist;
import com.groupon.getaways.annotations.QueryParam;
import com.groupon.getaways.annotations.Remove;
import com.groupon.getaways.util.Generics;
import com.groupon.getaways.util.Parameter;
import com.groupon.getaways.util.Reflection;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.lang.reflect.Method;
import java.util.Collection;

public class PersistenceHandler {

    public static Object invoke(EntityManager em, Method method, Object[] args) throws Throwable {

        if (method.isAnnotationPresent(NamedQuery.class)) {

            return invokeNamedQuery(em, method, args);

        }

        if (method.isAnnotationPresent(Find.class)) {

            return findByPrimaryKey(em, method, args);

        }

        if (method.isAnnotationPresent(Merge.class)) {

            return merge(em, method, args);

        }

        if (method.isAnnotationPresent(Remove.class)) {

            return remove(em, method, args);

        }

        if (method.isAnnotationPresent(Persist.class)) {

            return persist(em, method, args);

        }

        throw new AbstractMethodError("No handler logic for method: " + method.toString());
    }

    public static Object persist(EntityManager em, Method method, Object[] args) throws Throwable {
        final Iterable<Parameter> params = Reflection.params(method, args);
        final Parameter parameter = params.iterator().next();

        if (parameter.getValue() == null)
            throw new Exception(parameter.getType().getSimpleName() + " object is null");

        em.persist(parameter.getValue());

        return parameter.getValue();
    }

    public static Object findByPrimaryKey(EntityManager em, Method method, Object[] args) throws Throwable {
        final Class<?> entityClass = method.getReturnType();
        final Object primaryKey = args[0];

        if (primaryKey == null) {
            throw new Exception("Invalid id");
        }
        return em.find(entityClass, primaryKey);
    }


    public static Object invokeNamedQuery(EntityManager em, Method method, Object[] args) throws Throwable {
        final NamedQuery namedQuery = method.getAnnotation(NamedQuery.class);

        final TypedQuery<?> typedQuery = em.createNamedQuery(namedQuery.value(), getEntityType(method));

        for (Parameter parameter : Reflection.params(method, args)) {
            final QueryParam queryParam = parameter.getAnnotation(QueryParam.class);

            if (parameter.getValue() == null) {
                throw new Exception(queryParam.value() + " is null");
            }

            typedQuery.setParameter(queryParam.value(), parameter.getValue());
        }

        return (isList(method)) ? typedQuery.getResultList() : typedQuery.getSingleResult();
    }

    public static Object merge(EntityManager em, Method method, Object[] args) throws Throwable {
        final Iterable<Parameter> params = Reflection.params(method, args);
        final Parameter parameter = params.iterator().next();

        if (parameter.getValue() == null)
            throw new Exception(parameter.getType().getSimpleName() + " object is null");

        return em.merge(parameter.getValue());
    }

    public static Object remove(EntityManager em, Method method, Object[] args) throws Throwable {
        final Iterable<Parameter> params = Reflection.params(method, args);
        final Parameter parameter = params.iterator().next();

        if (parameter.getValue() == null)
            throw new Exception(parameter.getType().getSimpleName() + " object is null");

        em.remove(em.merge(parameter.getValue()));

        return null;
    }

    private static boolean isList(Method method) {
        return Collection.class.isAssignableFrom(method.getReturnType());
    }

    private static Class<?> getEntityType(Method method) {
        if (isList(method)) {
            return Generics.getCollectionType(method.getGenericReturnType());
        } else {
            return method.getReturnType();
        }
    }

}