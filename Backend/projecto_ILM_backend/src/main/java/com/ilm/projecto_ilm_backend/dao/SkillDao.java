package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.SkillEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The SkillDao class is a Data Access Object (DAO) for the SkillEntity class.
 * It provides methods to interact with the database.
 */
@Stateless
public class SkillDao extends AbstractDao<SkillEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * The entity manager used to interact with the database.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor.
     */
    public SkillDao() {
        super(SkillEntity.class);
    }

    /**
     * Merges the given skill entity with the database.
     *
     * @param entity the skill entity to merge.
     */
    @Transactional
    public void merge(final SkillEntity entity) {
        em.merge(entity);
    }

    /**
     * Finds a skill entity by its unique ID.
     *
     * @param id the ID of the skill entity to find.
     * @return the skill entity with the given ID.
     */
    public SkillEntity findById(int id) {
        try {
            return em.createNamedQuery("Skill.findById", SkillEntity.class).setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds all skill entities in the database.
     *
     * @return a list of all skill entities in the database.
     */
    public List<SkillEntity> findAll() {
        try {
            return em.createNamedQuery("Skill.findAll", SkillEntity.class).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a skill entity by its name.
     *
     * @param name the name of the skill entity to find.
     * @return an Optional containing the skill entity with the given name.
     */
    public Optional<SkillEntity> findByName(String name) {
        try {
            return Optional.of(em.createNamedQuery("Skill.findByName", SkillEntity.class).setParameter("name", name).getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public SkillEntity findSkillByName(String name) {
        try {
            return em.createNamedQuery("Skill.findByName", SkillEntity.class).setParameter("name", name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates a new skill entity in the database.
     *
     * @param skillEntity the skill entity to create.
     */
    @Transactional
    public void create(SkillEntity skillEntity) {
        em.persist(skillEntity);
    }
}
