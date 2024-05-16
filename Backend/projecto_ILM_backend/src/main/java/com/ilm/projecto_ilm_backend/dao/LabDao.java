package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.entity.LabEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * The LabDao class provides methods for interacting with the "lab" table in the database.
 * It extends the AbstractDao class, inheriting common database operations.
 */
@Stateless
public class LabDao extends AbstractDao<LabEntity>{

    private static final long serialVersionUID = 1L;

    /**
     * The EntityManager instance used for interacting with the database.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Merges the state of the given LabEntity with the current persistent context.
     *
     * @param lab the LabEntity to be merged.
     */
    @Transactional
    public void merge(LabEntity lab) {
        em.merge(lab);
    }

    /**
     * Default constructor.
     * Calls the superclass constructor with the LabEntity class as the parameter.
     */
    public LabDao() {
        super(LabEntity.class);
    }

    /**
     * Finds a LabEntity in the database with the given local.
     *
     * @param local the local of the LabEntity to find.
     * @return the LabEntity with the given local, or null if no such LabEntity exists.
     */
    public LabEntity findbyLocal(WorkLocalENUM local) {
        try {
            return em.createNamedQuery("Lab.findByLocal", LabEntity.class).setParameter("local", local).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}