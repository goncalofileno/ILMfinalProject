package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.SessionEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The SessionDao class provides database operations for SessionEntity instances.
 */
@Stateless
public class SessionDao extends AbstractDao<SessionEntity> {

    private static final long serialVersionUID = 1L;

    @Inject
    SystemDao systemDao;

    /**
     * Constructs a new SessionDao instance.
     */
    public SessionDao() {
        super(SessionEntity.class);
    }

    /**
     * Merges the state of the given session into the current persistence context.
     *
     * @param session the session to be merged.
     */
    @Transactional
    public void merge(SessionEntity session) {
        super.merge(session);
    }

    /**
     * Saves a new session to the database.
     *
     * @param session the session to be saved.
     */
    @Transactional
    public void save(SessionEntity session) {
        super.persist(session);
    }

    /**
     * Finds a session by the given session ID.
     *
     * @param sessionId the ID of the session to be found.
     *                  The session ID is unique for each session.
     *                  It is used to identify the session.
     *                  It is a string.
     * @return the session entity, or null if not found.
     */
    public SessionEntity findBySessionId(String sessionId) {
        try {
            return em.createNamedQuery("Session.findBySessionId", SessionEntity.class)
                    .setParameter("sessionId", sessionId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a session by the given user ID.
     *
     * @param userId the ID of the user to be found.
     *               The user ID is unique for each user.
     *               It is used to identify the user.
     *               It is an integer.
     * @return the session entity, or null if not found.
     */
    public SessionEntity findByUserId(int userId) {
        try {
            return em.createNamedQuery("Session.findByUserId", SessionEntity.class)
                    .setParameter("user_id", userId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Removes a session from the database by its ID.
     *
     * @param sessionId the ID of the session to be removed.
     *                  The session ID is unique for each session.
     *                  It is used to identify the session.
     *                  It is a string.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean removeSessionById(String sessionId) {
        try {
            SessionEntity session = findBySessionId(sessionId);
            if (session != null) {
                em.remove(session);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    /**
     * Finds the user ID associated with a given session ID.
     *
     * @param sessionId The session ID for which the user ID is to be found.
     * @return The user ID associated with the given session ID, or -1 if no such session exists.
     */
    public int findUserIdBySessionId(String sessionId) {
        try {
            return (int) em.createNamedQuery("Session.findUserIdBySessionId")
                    .setParameter("sessionId", sessionId)
                    .getSingleResult();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Checks if a user associated with the given session ID is logged in by updating the session's expiry time.
     *
     * @param sessionId The session ID of the user to check.
     * @return true if the user is logged in (session exists and is updated), false otherwise.
     */
    public boolean isUserLogged(String sessionId) {
        try {
            SessionEntity session= (SessionEntity) em.createNamedQuery("Session.findBySessionId")
                    .setParameter("sessionId", sessionId)
                    .getSingleResult();

            session.setExpiresAt(LocalDateTime.now().plusHours(systemDao.findConfigValueByName("timeout")));
            merge(session);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Finds all sessions that have expired relative to the given current time.
     *
     * @param currentTime The current time to compare session expiry times against.
     * @return A list of SessionEntity objects representing all sessions that have expired.
     */
    public List<SessionEntity> findExpiredSessions(LocalDateTime currentTime) {
        return em.createQuery("SELECT s FROM SessionEntity s WHERE s.expiresAt < :currentTime", SessionEntity.class)
                .setParameter("currentTime", currentTime)
                .getResultList();
    }

    /**
     * Finds the session ID associated with a given user ID.
     *
     * @param userId The user ID for which the session ID is to be found.
     * @return The session ID associated with the given user ID, or null if no such session exists.
     */
    public String findSessionIdByUserId(int userId) {
        try {
            return em.createNamedQuery("Session.findSessionIdByUserId", String.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


}
