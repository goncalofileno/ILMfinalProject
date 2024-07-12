package com.ilm.projecto_ilm_backend.validator;

import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.entity.SessionEntity;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;
import java.util.List;
import com.ilm.projecto_ilm_backend.service.websockets.MailWebSocket;


/**
 * SessionCleanupService is a class that provides a method to clean up expired sessions.
 * It is annotated with @Singleton and @Startup, meaning that it is a singleton and it is eagerly initialized.
 */
@Singleton
@Startup
public class SessionCleanupService {

    /**
     * Logger for this class.
     */
    private static final Logger logger = LogManager.getLogger(SessionCleanupService.class);
    /**
     * SessionDao object, used to interact with the session data in the database.
     */
    @Inject
    private SessionDao sessionDao;
    /**
     * This method cleans up expired sessions.
     * It is scheduled to run every 2 minutes.
     * It retrieves the expired sessions from the database, notifies about the timeout, and removes the session from the database.
     */
    @Schedule(hour = "*", minute = "*/2", persistent = false)
    public void cleanUpExpiredSessions() {
        logger.info("Cleaning up expired sessions...");
        List<SessionEntity> expiredSessions = sessionDao.findExpiredSessions(LocalDateTime.now());
        for (SessionEntity session : expiredSessions) {
            MailWebSocket.notifyTimeout(session.getSessionId());
            sessionDao.remove(session);
            logger.info("Removed expired session: " + session.getSessionId());
        }
    }
}


