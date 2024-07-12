package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.LogTypeENUM;
import com.ilm.projecto_ilm_backend.entity.LogEntity;
import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Provides data access functionality for log entities within the application.
 * This class allows for the creation, update, and retrieval of log entries associated with projects.
 * It extends the AbstractDao class to inherit common CRUD operations.
 */
@Stateless
public class LogDao extends AbstractDao<LogEntity> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    EntityManager em;

    /**
     * Default constructor initializing the class with LogEntity.class to utilize in the AbstractDao.
     */
    public LogDao() {
        super(LogEntity.class);
    }

    /**
     * Updates an existing log entity in the database.
     * If the log entity does not exist, it will be created.
     *
     * @param log The log entity to be updated or created.
     */
    @Transactional
    public void merge(LogEntity log) {
        em.merge(log);
    }

    /**
     * Retrieves a log entity by its unique identifier.
     *
     * @param id The unique identifier of the log entity.
     * @return The found LogEntity or null if no entity is found with the provided id.
     */
    public LogEntity findLogById(int id) {
        return em.find(LogEntity.class, id);
    }

    /**
     * Checks if a log entry of a specific type exists in the database.
     *
     * @param type The type of log entry to check for.
     * @return true if at least one log entry of the specified type exists, false otherwise.
     */
    public boolean existsByType(LogTypeENUM type) {
        return !em.createQuery("SELECT l FROM LogEntity l WHERE l.type = :type", LogEntity.class)
                .setParameter("type", type)
                .getResultList()
                .isEmpty();
    }

    /**
     * Retrieves all log entries associated with a specific project.
     *
     * @param project The project entity whose log entries are to be retrieved.
     * @return A list of LogEntity objects associated with the specified project.
     */
    public List<LogEntity> findByProject(ProjectEntity project) {
        return em.createQuery("SELECT l FROM LogEntity l WHERE l.project = :project", LogEntity.class)
                .setParameter("project", project)
                .getResultList();
    }
}