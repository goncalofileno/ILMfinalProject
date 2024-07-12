package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
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


    /**
     * Returns the number of users associated with the given project ID.
     *
     * @param projectId the ID of the project.
     * @return the number of users associated with the given project ID.
     */
    public int getNumberOfUsersByProjectId(int projectId) {
        try {
            return  ((Number)em.createNamedQuery("UserProject.findNumberOfUsers").setParameter("projectId",projectId).getSingleResult()).intValue();

        } catch (NoResultException e) {
            return 0;
        }
    }

    /**
     * Checks if a user is part of a specific project.
     *
     * @param projectId The ID of the project to check.
     * @param userId The ID of the user to check.
     * @return true if the user is part of the project, false otherwise.
     */
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

    /**
     * Counts the total number of projects associated with a user.
     *
     * @return The total number of projects.
     */
    public long countUserProjects() {
        try {
            return  em.createNamedQuery("UserProject.countUserProjects", Long.class)
                    .getSingleResult();

        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Determines if a user is either the creator or a manager of a project.
     *
     * @param userId The ID of the user.
     * @param projectId The ID of the project.
     * @return true if the user is the creator or a manager, false otherwise.
     */
    public boolean isUserCreatorOrManager(int userId, int projectId) {
        return em.createNamedQuery("UserProject.isUserCreatorOrManager", Long.class)
                .setParameter("userId", userId)
                .setParameter("projectId", projectId)
                .getSingleResult() > 0;
    }

    /**
     * Checks if a user has already been invited to a project.
     *
     * @param userId The ID of the user.
     * @param projectId The ID of the project.
     * @return true if the user has been invited, false otherwise.
     */
    public boolean isUserAlreadyInvited(int userId, int projectId) {
        return em.createNamedQuery("UserProject.isUserAlreadyInvited", Long.class)
                .setParameter("userId", userId)
                .setParameter("projectId", projectId)
                .setParameter("invited", UserInProjectTypeENUM.PENDING_BY_INVITATION)
                .getSingleResult() > 0;
    }

    /**
     * Finds a UserProjectEntity by user ID, project ID, and type.
     *
     * @param userId The ID of the user.
     * @param projectId The ID of the project.
     * @param type The type of association (e.g., MEMBER, MANAGER).
     * @return The found UserProjectEntity or null if not found.
     */
    public  UserProjectEntity findByUserIdAndProjectIdAndType(int userId, int projectId, UserInProjectTypeENUM type) {
        try {
            return em.createNamedQuery("UserProject.findByUserIdAndProjectIdAndType", UserProjectEntity.class)
                    .setParameter("userId", userId)
                    .setParameter("projectId", projectId)
                    .setParameter("type", type)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Finds all members of a project by project ID.
     *
     * @param projectId The ID of the project.
     * @return A list of UserProjectEntity instances representing the members.
     */
    public List<UserProjectEntity> findMembersByProjectId(int projectId) {
        try {
            return em.createNamedQuery("UserProject.findMembersByProjectId", UserProjectEntity.class)
                    .setParameter("projectId", projectId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Finds all types of members (including pending and rejected) by project ID.
     *
     * @param projectId The ID of the project.
     * @return A list of UserProjectEntity instances representing all types of members.
     */
    public List<UserProjectEntity> findAllTypeOfMembersByProjectId(int projectId) {
        try {
            return em.createNamedQuery("UserProject.findAllTypeOfMembersByProjectId", UserProjectEntity.class)
                    .setParameter("projectId", projectId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Finds UserEntity instances representing members of a project by project ID.
     *
     * @param projectId The ID of the project.
     * @return A list of UserEntity instances.
     */
    public List<UserEntity> findMembersEntityByProjectId(int projectId) {
        try {
            return em.createNamedQuery("UserProject.findMembersByProjectId", UserEntity.class)
                    .setParameter("projectId", projectId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Finds UserEntity instances representing creators and managers of a project by project ID.
     *
     * @param projectId The ID of the project.
     * @return A list of UserEntity instances.
     */
    public List<UserEntity> findCreatorsAndManagersByProjectId(int projectId) {
        try {
            return em.createNamedQuery("UserProject.findCreatorsAndManagersByProjectId", UserEntity.class)
                    .setParameter("projectId", projectId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Finds the UserEntity instance representing the creator of a project by project ID.
     *
     * @param projectId The ID of the project.
     * @return The UserEntity instance or null if not found.
     */
    public UserEntity findCreatorByProjectId(int projectId) {
        try {
            return em.createNamedQuery("UserProject.findCreatorByProjectId", UserEntity.class)
                    .setParameter("projectId", projectId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Finds a UserProjectEntity by user ID and project ID.
     *
     * @param userId The ID of the user.
     * @param projectId The ID of the project.
     * @return The UserProjectEntity or null if not found.
     */
    public UserProjectEntity findByUserIdAndProjectId(int userId, int projectId) {
        try {
            return em.createNamedQuery("UserProject.findByUserIdAndProjectId", UserProjectEntity.class)
                    .setParameter("userId", userId)
                    .setParameter("projectId", projectId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Finds the type of a user in a project by user ID and project ID.
     *
     * @param userId The ID of the user.
     * @param projectId The ID of the project.
     * @return The UserInProjectTypeENUM representing the user's type.
     */
    public UserInProjectTypeENUM findUserTypeByUserIdAndProjectId(int userId, int projectId) {
        try {
            return em.createNamedQuery("UserProject.findUserTypeByUserIdAndProjectId", UserInProjectTypeENUM.class)
                    .setParameter("userId", userId)
                    .setParameter("projectId", projectId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Checks if a user is the creator of a project.
     *
     * @param userId The ID of the user.
     * @param projectId The ID of the project.
     * @return true if the user is the creator, false otherwise.
     */
    public boolean isUserCreator(int userId, int projectId) {
        return em.createNamedQuery("UserProject.isUserCreator", Long.class)
                .setParameter("userId", userId)
                .setParameter("projectId", projectId)
                .getSingleResult() > 0;
    }

    /**
     * Counts the number of users in a project by project ID.
     *
     * @param projectId The ID of the project.
     * @return The count of users in the project.
     */
    public int countUsersInProject(int projectId) {
        return em.createNamedQuery("UserProject.countMembersByProjectId", Long.class)
                .setParameter("projectId", projectId)
                .getSingleResult().intValue();
    }

    /**
     * Checks if a user has projects.
     *
     * @param userId The ID of the user.
     * @return true if the user has projects, false otherwise.
     */
    public boolean userHasProjects(int userId) {
        return em.createNamedQuery("UserProject.userHasProjects", Long.class)
                .setParameter("userId", userId)
                .getSingleResult() > 0;
    }
}
