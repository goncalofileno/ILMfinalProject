package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.LogTypeENUM;
import com.ilm.projecto_ilm_backend.entity.LogEntity;
import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
public class LogDao extends AbstractDao<LogEntity> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    EntityManager em;

    public LogDao() {
        super(LogEntity.class);
    }

    @Transactional
    public void merge(LogEntity log) {
        em.merge(log);
    }

    public LogEntity findLogById(int id) {
        return em.find(LogEntity.class, id);
    }

    public boolean existsByType(LogTypeENUM type) {
        return !em.createQuery("SELECT l FROM LogEntity l WHERE l.type = :type")
                .setParameter("type", type)
                .getResultList()
                .isEmpty();
    }

    public List<LogEntity> findByProject(ProjectEntity project) {
        return em.createQuery("SELECT l FROM LogEntity l WHERE l.project = :project")
                .setParameter("project", project)
                .getResultList();
    }


}
