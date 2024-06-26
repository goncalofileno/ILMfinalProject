package com.ilm.projecto_ilm_backend.service.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ilm.projecto_ilm_backend.dto.messages.MessageDto;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import com.ilm.projecto_ilm_backend.dto.project.ProjectMemberDto;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ServerEndpoint("/ws/chat/{projectId}/{username}")
public class ProjectChatWebSocket {

    private static final Logger logger = Logger.getLogger(ProjectChatWebSocket.class.getName());
    private static final ObjectMapper objectMapper;
    private static final Map<String, Set<Session>> sessionMap = new ConcurrentHashMap<>();
    private static final Map<String, String> userSessionMap = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final Map<String, ProjectMemberDto> onlineMembers = new ConcurrentHashMap<>();

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("projectId") String projectId, @PathParam("username") String username) {
        closeExistingSessionForUser(username, projectId);
        sessionMap.computeIfAbsent(projectId, k -> ConcurrentHashMap.newKeySet()).add(session);
        userSessionMap.put(username, session.getId());
        onlineMembers.put(session.getId(), new ProjectMemberDto(username, username, UserInProjectTypeENUM.MEMBER, "defaultProfilePic.png"));
        sendOnlineMembers(projectId);
        logger.info("Connected to chat... Project ID: " + projectId + ", Username: " + username);
        startPeriodicUpdate(projectId);
    }

    private void closeExistingSessionForUser(String username, String projectId) {
        String existingSessionId = userSessionMap.get(username);
        if (existingSessionId != null) {
            Set<Session> sessions = sessionMap.get(projectId);
            if (sessions != null) {
                sessions.removeIf(session -> session.getId().equals(existingSessionId));
                onlineMembers.remove(existingSessionId);
                logger.info("Closed existing session for user: " + username);
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("Received message: " + message);
        // Custom logic to handle chat messages
        try {
            // Ensure message is JSON formatted and process it
            MessageDto messageDto = objectMapper.readValue(message, MessageDto.class);
            // Broadcast the message to all sessions
            String broadcastMessage = objectMapper.writeValueAsString(new WebSocketMessage("new_message", messageDto));
            broadcastMessageToProject(session, broadcastMessage);
        } catch (IOException e) {
            logger.severe("Error processing message: " + e.getMessage());
        }
    }

    private void broadcastMessageToProject(Session session, String message) {
        String projectId = sessionMap.entrySet().stream()
                .filter(entry -> entry.getValue().contains(session))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (projectId != null) {
            broadcastMessage(projectId, message);
        }
    }

    private static class WebSocketMessage {
        public String type;
        public Object message;

        public WebSocketMessage(String type, Object message) {
            this.type = type;
            this.message = message;
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("projectId") String projectId) {
        Set<Session> sessions = sessionMap.get(projectId);
        if (sessions != null) {
            sessions.remove(session);
            onlineMembers.remove(session.getId());
            if (sessions.isEmpty()) {
                sessionMap.remove(projectId);
            }
            sendOnlineMembers(projectId);
        }
        userSessionMap.values().remove(session.getId());
        logger.info("Disconnected from chat... Project ID: " + projectId);
    }

    public void broadcastMessage(String projectId, MessageDto messageDto) {
        Set<Session> sessions = sessionMap.get(projectId);
        if (sessions != null) {
            try {
                String broadcastMessage = objectMapper.writeValueAsString(new WebSocketMessage("new_message", messageDto));
                for (Session s : sessions) {
                    if (s.isOpen()) {
                        s.getAsyncRemote().sendText(broadcastMessage);
                    }
                }
            } catch (JsonProcessingException e) {
                logger.severe("Error serializing message: " + e.getMessage());
            }
        }
    }

    public void broadcastMessage(String projectId, String message) {
        Set<Session> sessions = sessionMap.get(projectId);
        if (sessions != null) {
            for (Session s : sessions) {
                if (s.isOpen()) {
                    s.getAsyncRemote().sendText(message);
                }
            }
        }
    }

    private void sendOnlineMembers(String projectId) {
        Set<ProjectMemberDto> members = sessionMap.get(projectId).stream()
                .map(session -> onlineMembers.get(session.getId()))
                .collect(Collectors.toSet());
        try {
            String message = objectMapper.writeValueAsString(new WebSocketMessage("online_members", members));
            broadcastMessage(projectId, message);
        } catch (JsonProcessingException e) {
            logger.severe("Error serializing online member list: " + e.getMessage());
        }
    }

    private void startPeriodicUpdate(String projectId) {
        scheduler.scheduleAtFixedRate(() -> sendOnlineMembers(projectId), 10, 10, TimeUnit.MINUTES);
    }
}
