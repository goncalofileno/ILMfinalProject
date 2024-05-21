package com.ilm.projecto_ilm_backend.validator;

import com.ilm.projecto_ilm_backend.dao.UserDao;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DatabaseValidator {

    @Inject
    private UserDao userDao;

    /**
     * Checks if the provided email is already present in the database.
     *
     * @param email the email to be checked
     * @return {@code true} if the email is found in the database, {@code false} otherwise
     */
    public boolean checkEmail(String email) {
        return userDao.findByEmail(email) != null;
    }

    /**
     * Checks if the provided username is already present in the database.
     *
     * @param username the username to be checked
     * @return {@code true} if the username is found in the database, {@code false} otherwise
     */
    public boolean checkUsername(String username) {
      return userDao.findByUsername(username) != null;
    }
}
