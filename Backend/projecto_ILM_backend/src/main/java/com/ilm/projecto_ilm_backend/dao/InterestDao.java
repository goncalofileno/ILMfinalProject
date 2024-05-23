package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.InterestEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The InterestDao class provides methods for interacting with the "interest" table in the database.
 * It extends the AbstractDao class, inheriting common database operations.
 */
@Stateless
public class InterestDao extends AbstractDao<InterestEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * The EntityManager instance used for interacting with the database.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor.
     * Calls the superclass constructor with the InterestEntity class as the parameter.
     */
    public InterestDao() {
        super(InterestEntity.class);
    }

    /**
     * Merges the state of the given InterestEntity with the current persistent context.
     *
     * @param interest the InterestEntity to be merged.
     */
    @Transactional
    public void merge(InterestEntity interest) {
        em.merge(interest);
    }

    /**
     * Finds an InterestEntity in the database with the given id.
     *
     * @param id the id of the InterestEntity to find.
     * @return the InterestEntity with the given id, or null if no such InterestEntity exists.
     */
    public InterestEntity findById(int id) {
        try {
            return em.createNamedQuery("Interest.findById", InterestEntity.class).setParameter("id", id)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Counts the number of interests in the database and returns the value int.
     *
     * @return the number of interests in the database.
     */
    public int countAll() {
        try {
            return em.createNamedQuery("Interest.countAll", Long.class).getSingleResult().intValue();

        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Finds all InterestEntities in the database.
     *
     * @return a list of all InterestEntities in the database.
     */
    public List<InterestEntity> findAll() {
        try {
            return em.createNamedQuery("Interest.findAll", InterestEntity.class).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds an interest entity by its name.
     *
     * @param name the name of the interest entity to find.
     * @return an Optional containing the interest entity with the given name if it exists.
     */
    public Optional<InterestEntity> findByName(String name) {
        try {
            InterestEntity interest = em.createNamedQuery("Interest.findByName", InterestEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(interest);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Creates a new interest entity in the database.
     *
     * @param interest the interest entity to create.
     */
    @Transactional
    public void create(InterestEntity interest) {
        em.persist(interest);
    }
}

