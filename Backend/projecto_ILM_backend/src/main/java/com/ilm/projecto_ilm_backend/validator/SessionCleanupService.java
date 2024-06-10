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

@Singleton
@Startup
public class SessionCleanupService {

    private static final Logger logger = LogManager.getLogger(SessionCleanupService.class);

    @Inject
    private SessionDao sessionDao;

    @Schedule(hour = "*", minute = "*/5", persistent = false)
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


