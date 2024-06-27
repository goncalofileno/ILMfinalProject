package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.entity.LabEntity;
import com.ilm.projecto_ilm_backend.entity.SkillEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * The UserDao class provides database operations for UserEntity instances.
 * It extends the AbstractDao class to inherit common database operations.
 */
@Stateless
public class UserDao extends AbstractDao<UserEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * The EntityManager instance used for performing database operations.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Merges the state of the given user into the current persistence context.
     *
     * @param user the user to be merged.
     */
    @Transactional
    public void merge(UserEntity user) {
        em.merge(user);
    }

    /**
     * Constructs a new UserDao instance.
     */
    public UserDao() {
        super(UserEntity.class);
    }

    /**
     * Finds a user by the given email.
     *
     * @param email the email of the user to be found.
     *              The email is unique for each user.
     *              It is used to identify the user.
     *              It is a string.
     */
    public UserEntity findByEmail(String email) {
        try {
            return em.createNamedQuery("User.findByEmail", UserEntity.class).setParameter("email", email)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a user by the given id.
     *
     * @param id the id of the user to be found.
     *           The id is unique for each user.
     *           It is used to identify the user.
     *           It is an integer.
     */
    public UserEntity findById(int id) {
        try {
            return em.createNamedQuery("User.findById", UserEntity.class).setParameter("id", id)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a user by the given username.
     *
     * @param username the username of the user to be found.
     *              The username is unique for each user.
     *              It is used to identify the user.
     *              It is a string.
     */
    public UserEntity findByUsername(String username) {
        try {
            return em.createNamedQuery("User.findByUsername", UserEntity.class).setParameter("username", username)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Finds a user by the given auxiliarToken.
     *
     * @param auxiliarToken the auxiliarToken of the user to be found.
     *              The auxiliarToken is unique for each user.
     *              It is used to identify the user.
     *              It is a string.
     */

    public UserEntity findByAuxiliarToken(String auxiliarToken) {
        try {
            return em.createNamedQuery("User.findByAuxiliarToken", UserEntity.class).setParameter("auxiliarToken", auxiliarToken)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    public boolean checkPassFromEmail(String email, String password) {
        try {
            String pass = (String) em.createNamedQuery("User.checkPassFromEmail").setParameter("email", email).getSingleResult();
            return pass.equals(password);

        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkSystemUsername(String systemUsername) {
        try {
            return em.createNamedQuery("User.checkSystemUsername").setParameter("systemUsername", systemUsername).getSingleResult() != null;

        } catch (Exception e) {
            return false;
        }
    }

    public UserEntity findBySystemUsername(String systemUsername) {
        try {
            return em.createNamedQuery("User.findBySystemUsername", UserEntity.class).setParameter("systemUsername", systemUsername)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    public UserEntity getUserBySystemUsernameWithAssociations(String systemUsername) {
        // Carregar o usuário
        UserEntity user = em.createQuery("SELECT u FROM UserEntity u WHERE u.systemUsername = :systemUsername", UserEntity.class)
                .setParameter("systemUsername", systemUsername)
                .getSingleResult();

        // Inicializar as coleções
        user.getUserProjects().size(); // Inicializar a coleção de userProjects
        user.getSkills().size(); // Inicializar a coleção de skills
        user.getInterests().size(); // Inicializar a coleção de interests

        return user;
    }

    //recebe o systemUsername e retorna o nome completo do utilizador, firstName + " " + lastName
    public String getFullNameBySystemUsername(String systemUsername) {
        try {
            return (String) em.createQuery("SELECT u.firstName || ' ' || u.lastName FROM UserEntity u WHERE u.systemUsername = :systemUsername")
                    .setParameter("systemUsername", systemUsername)
                    .getSingleResult();
        }
        catch (Exception e) {
            return null;
        }
    }

    //recebe o systemUsername e retorna o nome completo do utilizador, firstName + " " + lastName
    public List<Object[]> getUserProjectCreationDto(int userId, List<Long> rejectedUsersId, int usersPerPage, int page, LabEntity lab, String keyword){
        try {
                return em.createNamedQuery("User.getUserProjectCreationDto", Object[].class).setParameter("id",userId).setParameter("excludedIds",rejectedUsersId).setParameter("lab",lab).setParameter("keyword",keyword).setFirstResult(usersPerPage * (page - 1)).setMaxResults(usersPerPage).getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    public int getNumberUserProjectCreationDto(int userId, List<Long> rejectedUsersId,LabEntity lab,String keyword) {
        try {
            return  em.createNamedQuery("User.countUserProjectCreationDto", Long.class).setParameter("id",userId).setParameter("excludedIds",rejectedUsersId).setParameter("lab",lab).setParameter("keyword",keyword).getSingleResult().intValue();

        } catch (Exception e) {
            return -1;
        }
    }

    public List<SkillEntity> getUserSkills(int userId, List<String> skillNames) {
        try {
            return em.createNamedQuery("User.getUserSkills", SkillEntity.class).setParameter("id",userId).setParameter("skillNames",skillNames).getResultList();

        } catch (Exception e) {
            return null;
        }
    }
    
}
