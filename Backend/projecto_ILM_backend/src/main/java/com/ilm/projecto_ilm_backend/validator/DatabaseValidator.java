package com.ilm.projecto_ilm_backend.validator;

import com.ilm.projecto_ilm_backend.dao.ProjectDao;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

/**
 * DatabaseValidator is a class that provides methods to validate various database entities.
 * It is annotated with @ApplicationScoped, meaning that it is a singleton and the same instance is used throughout the application.
 */
@ApplicationScoped
public class DatabaseValidator {
    /**
     * UserDao object, used to interact with the user data in the database.
     */
    @Inject
    private UserDao userDao;
    /**
     * SessionDao object, used to interact with the session data in the database.
     */
    @Inject
    private SessionDao sessionDao;
    /**
     * ProjectDao object, used to interact with the project data in the database.
     */
    @Inject
    private ProjectDao projectDao;

    /**
     * This method checks if the provided email is already present in the database.
     *
     * @param email the email to be checked
     * @return true if the email is found in the database, false otherwise
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
    /**
     * This method checks if the provided sessionId is already present in the database.
     *
     * @param sessionId the sessionId to be checked
     * @return true if the sessionId is found in the database, false otherwise
     */
    public boolean checkSessionId(String sessionId) {
        return sessionDao.isUserLogged(sessionId);
    }
    /**
     * This method checks if the provided projectName is already present in the database.
     *
     * @param projectName the projectName to be checked
     * @return true if the projectName is found in the database, false otherwise
     */
    public boolean checkProjectName(String projectName) {
        return projectDao.doesProjectExists(projectName);
    }

}
