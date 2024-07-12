package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.messages.MessageDto;
import com.ilm.projecto_ilm_backend.dto.messages.MessagesPageDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectMemberDto;
import com.ilm.projecto_ilm_backend.entity.MessageEntity;
import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.entity.UserProjectEntity;
import com.ilm.projecto_ilm_backend.security.exceptions.ProjectNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotInProjectException;
import com.ilm.projecto_ilm_backend.service.websockets.ProjectChatWebSocket;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The MessageBean class is responsible for managing MessageEntity instances.
 * It is a singleton bean, meaning there is a single instance for the entire application.
 */
@Singleton
@Startup
public class MessageBean {

    @Inject
    MessageDao messageDao;

    @Inject
    UserDao userDao;

    @Inject
    ProjectDao projectDao;

    @Inject
    UserProjectDao userProjectDao;

    @Inject
    ProjectBean projectBean;

    @Inject
    NotificationBean notificationBean;

    @Inject
    ProjectChatWebSocket projectChatWebSocket;

    @Inject
    SessionDao sessionDao;

    /**
     * Creates default messages for demonstration or initial setup purposes.
     * This method checks if there are any messages present in the database and,
     * if not, creates a predefined set of messages for a project.
     */
    @Transactional
    public void createDefaultMessagesIfNotExistent() {
        UserEntity user = userDao.findById(1);
        UserEntity user2 = userDao.findById(2);
        ProjectEntity project = projectDao.findById(1);

        if (messageDao.findAll().isEmpty()) {
            messageDao.persist(new MessageEntity("Hello", LocalDateTime.now(), user, project));
            messageDao.persist(new MessageEntity("Hi", LocalDateTime.now(), user2, project));
            messageDao.persist(new MessageEntity("Hey", LocalDateTime.now(), user, project));
            messageDao.persist(new MessageEntity("Hello", LocalDateTime.now(), user2, project));
            messageDao.persist(new MessageEntity("Hi", LocalDateTime.now(), user, project));
            messageDao.persist(new MessageEntity("Hey", LocalDateTime.now(), user2, project));
        }
    }

    /**
     * Retrieves a list of messages for a given project and user.
     * This method checks if the project exists and if the user is a member of the project.
     * If both conditions are met, it returns a list of message DTOs for the project.
     *
     * @param user The user entity for whom the messages are being retrieved.
     * @param projectSystemName The system name of the project.
     * @return A list of {@link MessageDto} objects representing the messages.
     * @throws Exception Throws {@link ProjectNotFoundException} if the project is not found,
     *                   or {@link UserNotInProjectException} if the user is not a member of the project.
     */
    public List<MessageDto> getMessages(UserEntity user, String projectSystemName) throws Exception {

        ProjectEntity project = projectDao.findBySystemName(projectSystemName);

        if (project == null) {
            throw new ProjectNotFoundException("Project not found: " + projectSystemName);
        }
        if (!userProjectDao.isUserInProject(project.getId(), user.getId())) {
            throw new UserNotInProjectException("User not in project: " + projectSystemName);
        }

        List<MessageEntity> messages = messageDao.findByProjectId(project.getId());

        List<MessageDto> messageDtos = new ArrayList<>();

        for (MessageEntity message : messages) {
            messageDtos.add(new MessageDto(message.getSender().getFullName(), message.getText(), message.getDate(), message.getSender().getSystemUsername(), message.getSender().getPhoto()));
        }

        return messageDtos;
    }

    /**
     * Retrieves a page of messages for a given project and user.
     * This method checks if the project exists and if the user is a member of the project.
     * If both conditions are met, it returns a {@link MessagesPageDto} object containing the messages,
     * project name, status, project members, and the user's type in the project.
     *
     * @param user The user entity for whom the messages are being retrieved.
     * @param projectSystemName The system name of the project.
     * @return A {@link MessagesPageDto} object containing the messages, project name, status, project members, and user type.
     * @throws Exception Throws {@link ProjectNotFoundException} if the project is not found,
     *                   or {@link UserNotInProjectException} if the user is not a member of the project.
     */
    public MessagesPageDto getChatPage(UserEntity user, String projectSystemName) throws Exception {

        ProjectEntity project = projectDao.findBySystemName(projectSystemName);

        if (project == null) {
            throw new ProjectNotFoundException("Project not found: " + projectSystemName);
        }
        if (!userProjectDao.isUserInProject(project.getId(), user.getId())) {
            throw new UserNotInProjectException("User not in project: " + projectSystemName);
        }

        List<MessageDto> messageDtos = getMessages(user, projectSystemName);

        List<ProjectMemberDto> projectMembers = projectBean.getProjectMembers(project.getId());

        return new MessagesPageDto(messageDtos, project.getName(), project.getStatus(), projectMembers, projectBean.getUserTypeInProject(user.getId(), project.getId()));

    }

    /**
     * Sends a message to a project.
     * This method checks if the project exists and if the user is a member of the project.
     * If both conditions are met, it creates a new message entity and persists it in the database.
     * It then sends a notification to all project members except the sender and broadcasts the message to all connected WebSocket clients.
     *
     * @param user The user entity sending the message.
     * @param projectSystemName The system name of the project.
     * @param messageDto The message DTO containing the message text.
     * @throws Exception Throws {@link ProjectNotFoundException} if the project is not found,
     *                   or {@link UserNotInProjectException} if the user is not a member of the project.
     */
    public void sendMessage(UserEntity user, String projectSystemName, MessageDto messageDto) throws Exception {

        ProjectEntity project = projectDao.findBySystemName(projectSystemName);

        if (project == null) {
            throw new ProjectNotFoundException("Project not found: " + projectSystemName);
        }
        if (!userProjectDao.isUserInProject(project.getId(), user.getId())) {
            throw new UserNotInProjectException("User not in project: " + projectSystemName);
        }

        MessageEntity message = new MessageEntity(messageDto.getMessage(), LocalDateTime.now(), user, project);

        messageDao.persist(message);

        // Get all project members by UserEntity and send notification to all except the sender
        List<UserProjectEntity> userProjectEntities = userProjectDao.findMembersByProjectId(project.getId());

        List<UserEntity> projectMembers = new ArrayList<>();
        for (UserProjectEntity userProjectEntity : userProjectEntities) {
            projectMembers.add(userProjectEntity.getUser());
        }

        for (UserEntity member : projectMembers) {
            if (!projectChatWebSocket.isUserOnlineInProject(projectSystemName, member.getSystemUsername())) {
                notificationBean.createProjectMessageNotification(project.getSystemName(), user.getSystemUsername(), member);
            }
        }

        // Broadcast the message to all connected WebSocket clients
        MessageDto broadcastMessage = new MessageDto(user.getFullName(), message.getText(), message.getDate(), user.getSystemUsername(), user.getPhoto());
        projectChatWebSocket.broadcastMessage(projectSystemName, broadcastMessage);
    }



}
