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

    public boolean isUserCreatorOrManager(int userId, int projectId) {
        return em.createNamedQuery("UserProject.isUserInProject", Long.class)
                .setParameter("userId", userId)
                .setParameter("projectId", projectId)
                .getSingleResult() > 0;
    }

    public boolean isUserAlreadyInvited(int userId, int projectId) {
        return em.createNamedQuery("UserProject.isUserAlreadyInvited", Long.class)
                .setParameter("userId", userId)
                .setParameter("projectId", projectId)
                .setParameter("invited", UserInProjectTypeENUM.PENDING_BY_INVITATION)
                .getSingleResult() > 0;
    }

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

    //retorna todos os membros do projecto pelo id do projecto, membros do projecto sao  CREATOR(0),
    //
    //    MANAGER(1),
    //
    //    MEMBER(2),
    //
    //    MEMBER_BY_INVITATION(3),
    //
    //    MEMBER_BY_APPLIANCE(4),

    public List<UserProjectEntity> findMembersByProjectId(int projectId) {
        try {
            return em.createNamedQuery("UserProject.findMembersByProjectId", UserProjectEntity.class)
                    .setParameter("projectId", projectId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<UserEntity> findMembersEntityByProjectId(int projectId) {
        try {
            return em.createNamedQuery("UserProject.findMembersByProjectId", UserEntity.class)
                    .setParameter("projectId", projectId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<UserEntity> findCreatorsAndManagersByProjectId(int projectId) {
        try {
            return em.createNamedQuery("UserProject.findCreatorsAndManagersByProjectId", UserEntity.class)
                    .setParameter("projectId", projectId)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public UserEntity findCreatorByProjectId(int projectId) {
        try {
            return em.createNamedQuery("UserProject.findCreatorByProjectId", UserEntity.class)
                    .setParameter("projectId", projectId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

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
}
