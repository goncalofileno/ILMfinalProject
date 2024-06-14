package com.ilm.projecto_ilm_backend.service.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.dto.notification.NotificationDto;
import jakarta.inject.Inject;
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

    @Inject
    SessionDao sessionDao;

    private static final Map<String, Set<Session>> sessionMap = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(MailWebSocket.class.getName());
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("sessionId") String sessionId, @PathParam("inbox") String inbox) {
        String key = sessionId;
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
        String key = sessionId;
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
        String keyDefault = sessionId;
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

    public static void sendProjectNotification(String sessionId, NotificationDto notificationDto) {
        sendNotification(sessionId, notificationDto);
    }

    public static void sendInviteNotification(String sessionId, NotificationDto notificationDto) {
        logger.info("Sending invite notification to session: " + sessionId);
        sendNotification(sessionId, notificationDto);
    }

    public static void sendInviteAcceptedNotification(String sessionId, NotificationDto notificationDto) {
        sendNotification(sessionId, notificationDto);
    }

    public static void sendInviteRejectedNotification(String sessionId, NotificationDto notificationDto) {
        sendNotification(sessionId, notificationDto);
    }

    public static void sendTaskNotification(String sessionId, NotificationDto notificationDto) {
        sendNotification(sessionId, notificationDto);
    }

    public static void sendApplianceNotification(String sessionId, NotificationDto notificationDto) {
        sendNotification(sessionId, notificationDto);
    }

    public static void sendApplianceAcceptedNotification(String sessionId, NotificationDto notificationDto) {
        sendNotification(sessionId, notificationDto);
    }

    public static void sendApplianceRejectedNotification(String sessionId, NotificationDto notificationDto) {
        sendNotification(sessionId, notificationDto);
    }

    public static void sendRemovedNotification(String sessionId, NotificationDto notificationDto) {
        sendNotification(sessionId, notificationDto);
    }

    private static void sendNotification(String sessionId, NotificationDto notificationDto) {
        String keyDefault = sessionId;
        Set<Session> defaultSessions = sessionMap.get(keyDefault);
        if (defaultSessions != null) {
            for (Session session : defaultSessions) {
                if (session.isOpen()) {
                    try {
                        String message = objectMapper.writeValueAsString(notificationDto);
                        session.getAsyncRemote().sendText(message);
                        logger.info("Sent notification to session: " + session.getId());
                    } catch (JsonProcessingException e) {
                        logger.severe("Error serializing notification DTO: " + e.getMessage());
                    }
                }
            }
        } else {
            logger.warning("No sessions found for key: " + keyDefault);
        }
    }
}
