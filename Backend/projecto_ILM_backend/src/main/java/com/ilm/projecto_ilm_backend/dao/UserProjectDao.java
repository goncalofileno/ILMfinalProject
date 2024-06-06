package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.UserProjectEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * The UserProjectDao class provides database operations for UserProjectEntity instances.
 * It extends the AbstractDao class to inherit common database operations.
 */
@Stateless
public class UserProjectDao extends AbstractDao<UserProjectEntity>{

    /**
     * The EntityManager instance used to interact with the persistence context.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The EntityManager instance used to interact with the persistence context.
     */
    @PersistenceContext
    EntityManager em;

    /**
     * Default constructor.
     */
    public UserProjectDao() {
        super(UserProjectEntity.class);
    }

    /**
     * Persists the given UserProjectEntity instance in the database.
     *
     * @param userProject the UserProjectEntity instance to persist.
     */
    @Transactional
    public void merge(UserProjectEntity userProject) {
        em.merge(userProject);
    }


    /**
     * Finds and returns the UserProjectEntity instance with the given ID.
     */
    public UserProjectEntity findById(int id) {
        try {
            return em.createNamedQuery("UserProject.findById", UserProjectEntity.class).setParameter("id", id)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a list of UserProjectEntity instances associated with the given project ID.
     *
     * @param projectId the ID of the project.
     * @return a list of UserProjectEntity instances associated with the given project ID.
     */
    public List<UserProjectEntity> findByProjectId(int projectId) {
        try {
            return em.createNamedQuery("UserProject.findByProjectId", UserProjectEntity.class).setParameter("projectId", projectId)
                    .getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a list of UserProjectEntity instances associated with the given user ID.
     *
     * @param userId the ID of the user.
     * @return a list of UserProjectEntity instances associated with the given user ID.
     */
    public List<UserProjectEntity> findByUserId(int userId) {
        try {
            return em.createNamedQuery("UserProject.findByUserId", UserProjectEntity.class).setParameter("userId", userId)
                    .getResultList();

        } catch (Exception e) {
            return null;
        }
    }



    public int getNumberOfUsersByProjectId(int projectId) {
        try {
            return  ((Number)em.createNamedQuery("UserProject.findNumberOfUsers").setParameter("projectId",projectId).getSingleResult()).intValue();

        } catch (NoResultException e) {
            return 0;
        }
    }

    public boolean isUserInProject(int projectId, int userId) {
        try {
            if  (((Number)em.createNamedQuery("UserProject.isUserInProject").setParameter("projectId",projectId).setParameter("userId",userId).getSingleResult()).intValue()>0){
                return true;
            }
            else return false;

        } catch (NoResultException e) {
            return false;
        }
    }

    public long countUserProjects() {
        try {
            return  em.createNamedQuery("UserProject.countUserProjects", Long.class)
                    .getSingleResult();

        } catch (Exception e) {
            return 0;
        }
    }
}
