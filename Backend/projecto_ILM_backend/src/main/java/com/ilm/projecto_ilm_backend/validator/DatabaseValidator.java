package com.ilm.projecto_ilm_backend.validator;

import com.ilm.projecto_ilm_backend.dao.ProjectDao;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class DatabaseValidator {

    @Inject
    private UserDao userDao;
    @Inject
    private SessionDao sessionDao;
    @Inject
    private ProjectDao projectDao;

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


    /**
     * Checks if the provided auxiliarToken is already present in the database.
     *
     * @param auxiliarToken the auxiliarToken to be checked
     * @return {@code true} if the auxiliarToken is found in the database, {@code false} otherwise
     */
    public boolean checkAuxiliarToken(String auxiliarToken) {
        return userDao.findByAuxiliarToken(auxiliarToken) != null;
    }

    public boolean checkSessionId(String sessionId) {
        return sessionDao.isUserLogged(sessionId);
    }

    public boolean checkProjectName(String projectName) {
        return projectDao.doesProjectExists(projectName);
    }

}
