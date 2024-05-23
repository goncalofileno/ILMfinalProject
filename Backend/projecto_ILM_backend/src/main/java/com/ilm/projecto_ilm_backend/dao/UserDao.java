package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

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

}
