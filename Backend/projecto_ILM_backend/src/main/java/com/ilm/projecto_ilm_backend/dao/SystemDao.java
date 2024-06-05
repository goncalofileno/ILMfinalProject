package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import com.ilm.projecto_ilm_backend.entity.SessionEntity;
import com.ilm.projecto_ilm_backend.entity.SystemEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

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

    @Transactional
    public void merge(SystemEntity systemConfig) {
        em.merge(systemConfig);
    }


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
