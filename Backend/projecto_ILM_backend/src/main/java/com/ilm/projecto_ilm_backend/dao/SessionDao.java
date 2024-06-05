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

    public SessionEntity findByUserId(int userId) {
        try {
            return em.createNamedQuery("Session.findByUserId", SessionEntity.class)
                    .setParameter("user_id", userId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

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

    public int findUserIdBySessionId(String sessionId) {
        try {
            return (int) em.createNamedQuery("Session.findUserIdBySessionId")
                    .setParameter("sessionId", sessionId)
                    .getSingleResult();
        } catch (Exception e) {
            return -1;
        }
    }

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
}
