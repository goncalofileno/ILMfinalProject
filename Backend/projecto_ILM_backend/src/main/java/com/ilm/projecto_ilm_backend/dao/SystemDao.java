package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import com.ilm.projecto_ilm_backend.entity.SessionEntity;
import com.ilm.projecto_ilm_backend.entity.SystemEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * SystemDao provides database operations for SystemEntity instances.
 * It extends AbstractDao to inherit common database operations.
 * This DAO is responsible for managing system configurations, including retrieving and updating configuration values.
 */
@Stateless
public class SystemDao extends AbstractDao<SystemEntity>  {
    private static final long serialVersionUID = 1L;

    @PersistenceContext
    EntityManager em;

    /**
     * Constructs a new SessionDao instance.
     */
    public SystemDao() {
        super(SystemEntity.class);
    }

    /**
     * Merges the state of the given SystemEntity into the current persistence context.
     *
     * @param systemConfig The SystemEntity instance to merge.
     */
    @Transactional
    public void merge(SystemEntity systemConfig) {
        em.merge(systemConfig);
    }


    /**
     * Retrieves the configuration value for a given configuration name.
     *
     * @param name The name of the configuration to retrieve.
     * @return The configuration value as an integer. Returns -1 if the configuration cannot be found or on error.
     */
    public int findConfigValueByName(String name) {
        try {
            return (int) em.createNamedQuery("System.getValueByName")
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return -1;
        }
    }
}
