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

    public TaskEntity findTaskBySystemTitle(String systemTitle) {
        try {
            return em.createNamedQuery("Task.findBySystemTitle", TaskEntity.class).setParameter("systemTitle", systemTitle)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<TaskEntity> findByDependentTask(TaskEntity task) {
        String query = "SELECT t FROM TaskEntity t JOIN t.dependentTasks dt WHERE dt.id = :taskId";
        try {
            return em.createQuery(query, TaskEntity.class).setParameter("taskId", task.getId())
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
