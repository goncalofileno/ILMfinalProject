package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.entity.UserTaskEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * DAO class for UserTaskEntity operations.
 * This class handles database operations related to user tasks, such as persisting, merging, and querying user tasks.
 */
@Stateless
public class UserTaskDao extends AbstractDao<UserTaskEntity> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor initializing with UserTaskEntity class type.
     */
    public UserTaskDao() {
        super(UserTaskEntity.class);
    }

    /**
     * Persists a new UserTaskEntity in the database.
     *
     * @param userTask The UserTaskEntity to be persisted.
     */
    @Transactional
    public void persist(UserTaskEntity userTask) {
        em.persist(userTask);
    }

    /**
     * Merges the state of the given UserTaskEntity into the current persistence context.
     *
     * @param userTask The UserTaskEntity to merge.
     */
    @Transactional
    public void merge(UserTaskEntity userTask) {
        em.merge(userTask);
    }

    /**
     * Finds a UserTaskEntity by its ID.
     *
     * @param id The ID of the UserTaskEntity to find.
     * @return The found UserTaskEntity or null if no entity with the given ID exists.
     */
    public UserTaskEntity findById(int id) {
        try {
            return em.createNamedQuery("UserTask.findById", UserTaskEntity.class).setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a UserTaskEntity by task ID and user ID.
     *
     * @param taskId The ID of the task.
     * @param userId The ID of the user.
     * @return The found UserTaskEntity or null if no entity matches the given task ID and user ID.
     */
    public UserTaskEntity findByTaskIdAndUserId(int taskId, int userId) {
        try {
            return em.createNamedQuery("UserTask.findByTaskIdAndUserId", UserTaskEntity.class).setParameter("taskId", taskId)
                    .setParameter("userId", userId).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds users associated with a task by task ID.
     *
     * @param taskId The ID of the task.
     * @return A list of UserEntity instances associated with the task.
     */
    public List<UserEntity> findUsersByTaskId(int taskId) {
        try {
            return em.createNamedQuery("UserTask.findUsersByTaskId", UserEntity.class).setParameter("taskId", taskId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds the type of a user in a task by task ID and user ID.
     *
     * @param taskId The ID of the task.
     * @param userId The ID of the user.
     * @return The UserInTaskTypeENUM representing the user's type in the task.
     */
    public UserInTaskTypeENUM findUserTypeByTaskIdAndUserId(int taskId, int userId) {
        try {
            return em.createNamedQuery("UserTask.findUserTypeByTaskIdAndUserId", UserInTaskTypeENUM.class)
                    .setParameter("taskId", taskId).setParameter("userId", userId).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Deletes all UserTaskEntity instances associated with a task except for the creator or creator in charge.
     *
     * @param taskId The ID of the task.
     */
    public void deleteAllExceptCreatorOrCreatorInCharge(int taskId) {
        em.createNamedQuery("UserTask.deleteAllExceptCreatorOrCreatorInCharge").setParameter("taskId", taskId).executeUpdate();
    }

    /**
     * Deletes all UserTaskEntity instances associated with a task by task ID.
     *
     * @param taskId The ID of the task to delete UserTaskEntities for.
     */
    public void deleteAllByTaskId(int taskId) {
        em.createQuery("DELETE FROM UserTaskEntity ut WHERE ut.task.id = :taskId").setParameter("taskId", taskId).executeUpdate();
    }

    /**
     * Finds the user in charge of a task by task ID.
     *
     * @param taskId The ID of the task.
     * @return The UserEntity instance representing the user in charge or null if not found.
     */
    public UserEntity findInChargeByTaskId(int taskId) {
        try {
            return em.createNamedQuery("UserTask.findInChargeByTaskId", UserEntity.class)
                    .setParameter("taskId", taskId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}