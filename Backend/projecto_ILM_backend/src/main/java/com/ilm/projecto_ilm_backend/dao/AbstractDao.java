package com.ilm.projecto_ilm_backend.dao;

import java.io.Serializable;
import java.util.List;

import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.logging.Logger;

/**
 * AbstractDao is a generic class that provides common database operations.
 * This class is annotated with @TransactionAttribute to specify that all methods require a transaction.
 *
 * @param <T> the type of the entity
 */
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public abstract class AbstractDao<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(AbstractDao.class.getName());

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
    public AbstractDao(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class type cannot be null");
        }
        this.clazz = clazz;
    }

    /**
     * Finds an entity by its primary key.
     *
     * @param id the primary key
     * @return the entity
     */
    public T find(Object id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return em.find(clazz, id);
    }

    /**
     * Persists an entity to the database.
     *
     * @param entity the entity to persist
     */
    public void persist(final T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        em.persist(entity);
        logger.info("Entity persisted: " + entity);
    }

    /**
     * Merges the state of the given entity into the current persistence context.
     *
     * @param entity the entity to merge
     */
    public void merge(final T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        em.merge(entity);
        logger.info("Entity merged: " + entity);
    }

    /**
     * Removes the entity from the database.
     *
     * @param entity the entity to remove
     */
    public void remove(final T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        em.remove(em.contains(entity) ? entity : em.merge(entity));
        logger.info("Entity removed: " + entity);
    }

    /**
     * Finds all entities of the given type.
     *
     * @return a list of entities
     */
    public List<T> findAll() {
        final CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(clazz);
        criteriaQuery.select(criteriaQuery.from(clazz));
        return em.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Deletes all entities of the given type.
     */
    public void deleteAll() {
        final CriteriaDelete<T> criteriaDelete = em.getCriteriaBuilder().createCriteriaDelete(clazz);
        criteriaDelete.from(clazz);
        em.createQuery(criteriaDelete).executeUpdate();
        logger.info("All entities deleted for class: " + clazz.getName());
    }

    /**
     * Synchronizes the persistence context to the underlying database.
     */
    public void flush() {
        em.flush();
        logger.info("Persistence context flushed for class: " + clazz.getName());
    }
}
