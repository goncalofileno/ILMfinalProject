package com.ilm.projecto_ilm_backend.service.websockets;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@ServerEndpoint("/ws/mail/{sessionId}/{inbox}")
public class MailWebSocket {
    private static final Map<String, Set<Session>> sessionMap = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(MailWebSocket.class.getName());

    @OnOpen
    public void onOpen(Session session, @PathParam("sessionId") String sessionId, @PathParam("inbox") String inbox) {
        String key = sessionId + (inbox.equals("inbox") ? "-inbox" : "");
        sessionMap.computeIfAbsent(key, k -> ConcurrentHashMap.newKeySet()).add(session);
        logger.info("Connected ... Session ID: " + sessionId + " Inbox: " + inbox);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("Received ...." + message);
        // No message handling needed for this example
    }

    @OnClose
    public void onClose(Session session, @PathParam("sessionId") String sessionId, @PathParam("inbox") String inbox) {
        String key = sessionId + (inbox.equals("inbox") ? "-inbox" : "");
        Set<Session> sessions = sessionMap.get(key);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                sessionMap.remove(key);
            }
        }
        logger.info("Disconnected ... Session ID: " + sessionId + " Inbox: " + inbox);
    }

    public static void notifyNewMail(String sessionId, String firstName, String lastName) {
        String keyInbox = sessionId + "-inbox";
        String keyDefault = sessionId;

        boolean hasInboxSession = false;

        Set<Session> inboxSessions = sessionMap.get(keyInbox);

        if (inboxSessions != null) {
            for (Session session : inboxSessions) {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText("real_time_mail:" + firstName + " " + lastName);
                    hasInboxSession = true;
                    logger.info("Sent real_time_mail to session: " + session.getId());
                }
            }
        }

        if (!hasInboxSession) {
            Set<Session> defaultSessions = sessionMap.get(keyDefault);
            if (defaultSessions != null) {
                for (Session session : defaultSessions) {
                    if (session.isOpen()) {
                        session.getAsyncRemote().sendText("new_mail:" + firstName + " " + lastName);
                        logger.info("Sent new_mail to session: " + session.getId());
                    }
                }
            } else {
                logger.warning("No sessions found for key: " + keyDefault);
            }
        }
    }


    public static void notifyTimeout(String sessionId) {
        String keyDefault = sessionId;
        Set<Session> defaultSessions = sessionMap.get(keyDefault);
        if (defaultSessions != null) {
            for (Session session : defaultSessions) {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText("timeout");
                    logger.info("Sent timeout to session: " + session.getId());
                }
            }
        } else {
            logger.warning("No sessions found for key: " + keyDefault);
        }
    }
}
