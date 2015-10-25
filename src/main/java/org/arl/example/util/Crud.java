package org.arl.example.util;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.arl.example.entity.AbstractEntity;

public class Crud {

    public static Long salvar(AbstractEntity object) {

        final EntityManager em = EntityManagerWrapper.getEntityManager();

        em.getTransaction().begin();
        final AbstractEntity persisted = em.merge(object);
        em.getTransaction().commit();

        return persisted.getId();

    }

    public static <T> void remover(Long id, Class<T> clazz) {

        final EntityManager em = EntityManagerWrapper.getEntityManager();

        em.getTransaction().begin();
        em.remove(em.find(clazz, id));
        em.getTransaction().commit();

    }

    public static <T> T find(Long id, Class<T> clazz) {

        final EntityManager em = EntityManagerWrapper.getEntityManager();

        return em.find(clazz, id);

    }

    public static <T> List<T> findAllByQuery(String queryString, Map<String, Object> params, Class<T> clazz) {

        final TypedQuery<T> query = createTypedQuery(queryString, params, clazz);
        return query.getResultList();

    }

    public static <T> Optional<T> findFirstByQuery(String queryString, Map<String, Object> params, Class<T> clazz) {

        final TypedQuery<T> query = createTypedQuery(queryString, params, clazz);
        query.setMaxResults(1);
        final List<T> resultList = query.getResultList();

        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));

    }

    public static int countNative(String nativeQueryString, List<Object> params) {
        final EntityManager em = EntityManagerWrapper.getEntityManager();
        final Query nativeQuery = em.createNativeQuery(nativeQueryString);
        IntStream.range(1, params.size() + 1).forEachOrdered(i -> nativeQuery.setParameter(i, params.get(i - 1)));
        final BigInteger count = (BigInteger) nativeQuery.getSingleResult();
        return count.intValue();
    }

    public static int executeNative(String nativeQueryString) {

        final EntityManager em = EntityManagerWrapper.getEntityManager();
        final Query nativeQuery = em.createNativeQuery(nativeQueryString);

        em.getTransaction().begin();
        final int retorno = nativeQuery.executeUpdate();
        em.getTransaction().commit();

        return retorno;
    }

    private static <T> TypedQuery<T> createTypedQuery(String queryString, Map<String, Object> params, Class<T> clazz) {

        final EntityManager em = EntityManagerWrapper.getEntityManager();
        final TypedQuery<T> query = em.createQuery(queryString, clazz);
        params.forEach((k, v) -> query.setParameter(k, v));
        return query;

    }

}
