package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.TaskEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Retrieves the configuration value for a given configuration name.
 *
 * @param name The name of the configuration to retrieve.
 * @return The configuration value as an integer. Returns -1 if the configuration cannot be found or on error.
 */
@Stateless
public class TaskDao extends AbstractDao<TaskEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor initializing with TaskEntity class type.
     */
    public TaskDao() {
        super(TaskEntity.class);
    }

    @PersistenceContext
    private EntityManager em;


    /**
     * Persists a new task entity in the database.
     *
     * @param task The TaskEntity to be persisted.
     */
    @Transactional
    public void persist(TaskEntity task) {
        em.persist(task);
    }

    /**
     * Merges the state of the given TaskEntity into the current persistence context.
     *
     * @param task The TaskEntity to merge.
     */
    @Transactional
    public void merge(TaskEntity task) {
        em.merge(task);
    }

    /**
     * Finds a task by its ID.
     *
     * @param id The ID of the task to find.
     * @return The found TaskEntity or null if no task with the given ID exists.
     */
    public TaskEntity findById(int id) {
        try {
            return em.createNamedQuery("Task.findById", TaskEntity.class).setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds tasks associated with a specific project ID.
     *
     * @param id The ID of the project.
     * @return A list of TaskEntity objects associated with the project.
     */
    public List<TaskEntity> findByProject(int id) {
        try {
            return em.createNamedQuery("Task.findByProject", TaskEntity.class).setParameter("id", id)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a task by its system title.
     *
     * @param systemTitle The system title of the task.
     * @return The found TaskEntity or null if no task with the given system title exists.
     */
    public TaskEntity findTaskBySystemTitle(String systemTitle) {
        try {
            return em.createNamedQuery("Task.findBySystemTitle", TaskEntity.class).setParameter("systemTitle", systemTitle)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds tasks that are dependent on a specific task.
     *
     * @param task The task entity whose dependents are to be found.
     * @return A list of TaskEntity objects that are dependent on the specified task.
     */
    public List<TaskEntity> findByDependentTask(TaskEntity task) {
        String query = "SELECT t FROM TaskEntity t JOIN t.dependentTasks dt WHERE dt.id = :taskId";
        try {
            return em.createQuery(query, TaskEntity.class).setParameter("taskId", task.getId())
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a task by its system title and project ID.
     *
     * @param systemTitle The system title of the task.
     * @param projectId The ID of the project.
     * @return The found TaskEntity or null if no task matches the given criteria.
     */
    public TaskEntity findTaskBySystemTitleAndProject(String systemTitle, int projectId) {
        try {
            return em.createNamedQuery("Task.findBySystemTitleAndProject", TaskEntity.class)
                    .setParameter("systemTitle", systemTitle)
                    .setParameter("projectId", projectId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a task by its system title.
     *
     * @param systemTitle The system title of the task.
     * @return The found TaskEntity or null if no task with the given system title exists.
     */
    public TaskEntity findBySystemTiltle(String systemTitle) {
        try {
            return em.createNamedQuery("Task.findBySystemTitle", TaskEntity.class).setParameter("systemTitle", systemTitle)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds tasks that start at the beginning of a project.
     *
     * @param projectId The ID of the project.
     * @param projectStartDate The start date of the project.
     * @return A list of TaskEntity objects that start at the beginning of the specified project.
     */
    public List<TaskEntity> findTasksAtProjectStart(int projectId, LocalDateTime projectStartDate) {
        String query = "SELECT t FROM TaskEntity t WHERE t.project.id = :projectId AND t.initialDate = :projectStartDate";
        return em.createQuery(query, TaskEntity.class)
                .setParameter("projectId", projectId)
                .setParameter("projectStartDate", projectStartDate)
                .getResultList();
    }


}
