package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.TaskEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
public class TaskDao extends AbstractDao<TaskEntity> {

    private static final long serialVersionUID = 1L;

    public TaskDao() {
        super(TaskEntity.class);
    }

    @PersistenceContext
    private EntityManager em;


    @Transactional
    public void persist(TaskEntity task) {
        em.persist(task);
    }

    @Transactional
    public void merge(TaskEntity task) {
        em.merge(task);
    }

    public TaskEntity findById(int id) {
        try {
            return em.createNamedQuery("Task.findById", TaskEntity.class).setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TaskEntity> findByProject(int id) {
        try {
            return em.createNamedQuery("Task.findByProject", TaskEntity.class).setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
