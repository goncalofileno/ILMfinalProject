package com.ilm.projecto_ilm_backend.dao;

import java.io.Serializable;
import java.util.List;

import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;

/**
 * AbstractDao is a generic class that provides common database operations.
 * This class is annotated with @TransactionAttribute to specify that all methods require a transaction.
 *
 * @param <T> the type of the entity
 */
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public abstract class AbstractDao<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The class of the entity.
     */
    private final Class<T> clazz;

    /**
     * The EntityManager that is automatically injected by the container.
     */
    @PersistenceContext(unitName = "PersistenceUnit")
    protected EntityManager em;

    /**
     * Constructor that sets the class of the entity.
     *
     * @param clazz the class of the entity
     */
    public AbstractDao(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    /**
     * Finds an entity by its primary key.
     *
     * @param id the primary key
     * @return the entity
     */
    public T find(Object id)
    {
        return em.find(clazz, id);
    }

    /**
     * Persists an entity to the database.
     *
     * @param entity the entity to persist
     */
    public void persist(final T entity)
    {
        em.persist(entity);
    }

    /**
     * Merges the state of the given entity into the current persistence context.
     *
     * @param entity the entity to merge
     */
    public void merge(final T entity)
    {
        em.merge(entity);
    }

    /**
     * Removes the entity from the database.
     *
     * @param entity the entity to remove
     */
    public void remove(final T entity)
    {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    /**
     * Finds all entities of the given type.
     *
     * @return a list of entities
     */
    public List<T> findAll()
    {
        final CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(clazz);
        criteriaQuery.select(criteriaQuery.from(clazz));
        return em.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Deletes all entities of the given type.
     */
    public void deleteAll()
    {
        final CriteriaDelete<T> criteriaDelete = em.getCriteriaBuilder().createCriteriaDelete(clazz);
        criteriaDelete.from(clazz);
        em.createQuery(criteriaDelete).executeUpdate();
    }

    /**
     * Synchronizes the persistence context to the underlying database.
     */
    public void flush() {
        em.flush();
    }
}

