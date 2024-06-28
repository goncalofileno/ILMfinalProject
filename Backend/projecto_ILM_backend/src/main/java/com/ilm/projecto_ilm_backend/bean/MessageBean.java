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
