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
     * Finds a user by username.
     * @param username the username of the user to find.
     * @return the user with the given username, or null if no such user exists.
     */
    public UserEntity findByUsername(String username) {
        try {
            return em.createNamedQuery("User.findByUsername", UserEntity.class).setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
