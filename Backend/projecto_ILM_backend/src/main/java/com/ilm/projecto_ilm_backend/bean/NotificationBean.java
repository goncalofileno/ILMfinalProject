package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dao.NotificationDao;
import com.ilm.projecto_ilm_backend.dao.ProjectDao;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dto.messages.MessageDto;
import com.ilm.projecto_ilm_backend.dto.notification.NotificationDto;
import com.ilm.projecto_ilm_backend.entity.NotificationEntity;
import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.security.exceptions.ProjectNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotInProjectException;
import com.ilm.projecto_ilm_backend.service.websockets.MailWebSocket;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The NotificationBean class is responsible for managing NotificationEntity instances.
 * It is an application scoped bean, meaning there is a single instance for the entire application.
 */
@ApplicationScoped
public class NotificationBean {

    @Inject
    ProjectDao projectDao;

    @Inject
    UserDao userDao;

    @Inject
    NotificationDao notificationDao;

    @Inject
    SessionDao sessionDao;

    /**
     * Creates default notifications for demonstration or initial setup purposes.
     * This method checks if there are any notifications present in the database and,
     * if not, creates a predefined set of notifications for a project.
     */
    public void createDefaultNotificationsIfNotExistent() {
        createNotification(NotificationTypeENUM.PROJECT, 1, 1, StateProjectENUM.IN_PROGRESS, null);
        createNotification(NotificationTypeENUM.INVITE, 2, 1, null, 2);
        createNotification(NotificationTypeENUM.INVITE_ACCEPTED, 3, 1, null, 2);
        createNotification(NotificationTypeENUM.INVITE_REJECTED, 4, 1, null, 2);
        createNotification(NotificationTypeENUM.TASK, 5, 1, null, 2);
        createNotification(NotificationTypeENUM.APPLIANCE, 2, 1, null, 2);
        createNotification(NotificationTypeENUM.APPLIANCE_ACCEPTED, 3, 1, null, 2);
        createNotification(NotificationTypeENUM.APPLIANCE_REJECTED, 4, 1, null, 2);
        createNotification(NotificationTypeENUM.REMOVED, 4, 1, null, 2);
    }


    /**
     * Creates a notification entity and persists it to the database.
     * This method is a utility for creating notifications with various types and associated data.
     *
     * @param type The type of the notification.
     * @param projectId The ID of the project associated with the notification.
     * @param receptorId The ID of the user who will receive the notification.
     * @param projectStatus The status of the project, if applicable.
     * @param userId The ID of the user related to the notification event, if applicable.
     */
    private void createNotification(NotificationTypeENUM type, int projectId, int receptorId,
                                    StateProjectENUM projectStatus, Integer userId) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(type);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectDao.findById(projectId).getSystemName());
        notification.setReceptor(userDao.findById(receptorId));
        notification.setMessageNotificationClicked(false);

        if (projectStatus != null) {
            notification.setProjectStatus(projectStatus);
        }

        if (userId != null) {
            notification.setUserName(userDao.getFullNameBySystemUsername(userDao.findById(userId).getSystemUsername()));
            notification.setSystemUserName(userDao.findById(userId).getSystemUsername());
        }

        notificationDao.persist(notification);
    }

    /**
     * Retrieves notifications for a user, combining unread and read notifications up to a limit.
     * This method prioritizes unread notifications and fills the remaining slots with the most recent read notifications.
     *
     * @param userId The ID of the user whose notifications are being retrieved.
     * @param page The pagination page number for read notifications.
     * @return A list of {@link NotificationDto} representing the user's notifications.
     */
    public List<NotificationDto> getUserNotifications(int userId, int page) {

        List<NotificationEntity> unreadNotifications = notificationDao.findUnreadByUserId(userId);
        List<NotificationEntity> readNotifications = notificationDao.findReadByUserId(userId, page);

        int unreadCount = unreadNotifications.size();
        int neededNotifications = 5 - unreadCount;

        List<NotificationEntity> resultNotifications = new ArrayList<>(unreadNotifications);
        if (unreadCount < 5) {
            resultNotifications.addAll(readNotifications.subList(0, Math.min(neededNotifications, readNotifications.size())));
        }

        List<NotificationDto> dtos = resultNotifications.stream().map(this::toDto).collect(Collectors.toList());

        // Mark unread notifications as read after creating the DTOs
        unreadNotifications.forEach(notification -> notification.setReadStatus(true));
        notificationDao.markNotificationsAsRead(unreadNotifications);

        return dtos;
    }

    /**
     * Counts the total number of notifications for a user.
     *
     * @param userId The ID of the user.
     * @return The total count of notifications for the specified user.
     */
    public int getTotalUserNotifications(int userId) {
        return notificationDao.countAllByUserId(userId);
    }

    /**
     * Counts the number of unread notifications for a user.
     *
     * @param userId The ID of the user.
     * @return The count of unread notifications for the specified user.
     */
    public int getUnreadNotificationCount(int userId) {
        return notificationDao.countUnreadByUserId(userId);
    }

    /**
     * Converts a NotificationEntity instance to a NotificationDto instance.
     *
     * @param entity The NotificationEntity instance to convert.
     * @return The converted NotificationDto instance.
     */
    private NotificationDto toDto(NotificationEntity entity) {
        NotificationDto dto = new NotificationDto();
        dto.setId(entity.getId());
        dto.setType(entity.getType().name());
        dto.setReadStatus(entity.isReadStatus());
        dto.setSendDate(entity.getSendDate());

        if (entity.getProjectSystemName() != null) {
            dto.setprojectName(projectDao.findNameBySystemName(entity.getProjectSystemName()));
            dto.setProjectSystemName(entity.getProjectSystemName());
        }

        if (entity.getProjectStatus() != null) {
            dto.setProjectStatus(entity.getProjectStatus().name());
        }

        if (entity.getUserName() != null) {
            dto.setUserName(entity.getUserName());
            dto.setUserPhoto(userDao.findBySystemUsername(entity.getSystemUserName()).getAvatarPhoto());
            dto.setSystemUserName(entity.getSystemUserName());
        }

        if (entity.getNewUserType() != null) {
            dto.setNewUserType(entity.getNewUserType().name());
        }
        return dto;
    }

    /**
     * Creates and sends a project-related notification to a user.
     * This method is used for notifications related to project events such as creation, updates, and status changes.
     *
     * @param projectSystemName The system name of the project.
     * @param projectStatus The current status of the project.
     * @param userName The name of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     * @param systemUserName The system username of the user associated with the event.
     */
    public void createProjectNotification(String projectSystemName, StateProjectENUM projectStatus, String userName, UserEntity receptor, String systemUserName) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.PROJECT);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setProjectStatus(projectStatus);
        notification.setUserName(userName);
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendProjectNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends a project rejection-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param projectStatus The current status of the project.
     * @param userName The name of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     * @param systemUserName The system username of the user associated with the event.
     */
    public void createRejectProjectNotification(String projectSystemName, StateProjectENUM projectStatus, String userName, UserEntity receptor, String systemUserName) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.PROJECT_REJECTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setProjectStatus(projectStatus);
        notification.setUserName(userName);
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendProjectNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends an invitation-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param userName The name of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     * @param systemUserName The system username of the user associated with the event.
     */
    public void createInviteNotification(String projectSystemName, String userName, UserEntity receptor, String systemUserName) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.INVITE);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendInviteNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends a project insertion-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param userName The name of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     * @param systemUserName The system username of the user associated with the event.
     */
    public void createProjectInsertedNotification(String projectSystemName, String userName, UserEntity receptor, String systemUserName) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.PROJECT_INSERTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendProjectInsertedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends an invitation acceptance-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param userName The name of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     * @param systemUserName The system username of the user associated with the event.
     */
    public void createInviteAcceptedNotification(String projectSystemName, String userName, UserEntity receptor, String systemUserName) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.INVITE_ACCEPTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendInviteAcceptedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends an invitation rejection-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param userName The name of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     * @param systemUserName The system username of the user associated with the event.
     */
    public void createInviteRejectedNotification(String projectSystemName, String userName, UserEntity receptor, String systemUserName) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.INVITE_REJECTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendInviteRejectedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends a task-related notification to a user.
     *
     * @param taskName The name of the task.
     * @param projectSystemName The system name of the project.
     * @param systemeUsername The system username of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     */
    public void createTaskNotification(String taskName, String projectSystemName, String systemeUsername, UserEntity receptor) {

        boolean notificationExists = notificationDao.findSimilarTaskNotification(taskName, projectSystemName, receptor);

        if (notificationExists) {
            return;
        }

        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.TASK);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setTaskName(taskName);
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userDao.getFullNameBySystemUsername(systemeUsername));
        notification.setSystemUserName(systemeUsername);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendTaskNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends a task assignment-related notification to a user.
     *
     * @param taskName The name of the task.
     * @param projectSystemName The system name of the project.
     * @param systemUsername The system username of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     */
    public void createTaskAssignedNotification(String taskName, String projectSystemName, String systemUsername, UserEntity receptor) {
        // Verificar se já existe uma notificação similar
        boolean notificationExists = notificationDao.findSimilarTaskNotification(taskName, projectSystemName, receptor);

        if (notificationExists) {
            // Se uma notificação similar já existir, não crie uma nova
            return;
        }
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.TASK_ASSIGNED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setTaskName(taskName);
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userDao.getFullNameBySystemUsername(systemUsername));
        notification.setSystemUserName(systemUsername);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendTaskAssignedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends an appliance-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param userName The name of the user associated with the appliance.
     * @param receptor The user entity who will receive the notification.
     * @param systemUserName The system username of the user associated with the appliance.
     */
    public void createApplianceNotification(String projectSystemName, String userName, UserEntity receptor, String systemUserName) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.APPLIANCE);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendApplianceNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends an appliance acceptance-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param systemUsername The system username of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     */
    public void createApplianceAcceptedNotification(String projectSystemName, String systemUsername, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.APPLIANCE_ACCEPTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userDao.getFullNameBySystemUsername(systemUsername));
        notification.setSystemUserName(systemUsername);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendApplianceAcceptedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends an appliance rejection-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param systemUsername The system username of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     */
    public void createApplianceRejectedNotification(String projectSystemName, String systemUsername, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.APPLIANCE_REJECTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userDao.getFullNameBySystemUsername(systemUsername));
        notification.setSystemUserName(systemUsername);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendApplianceRejectedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends a removed-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param systemUsername The system username of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     */
    public void createRemovedNotification(String projectSystemName, String systemUsername, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.REMOVED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userDao.getFullNameBySystemUsername(systemUsername));
        notification.setSystemUserName(systemUsername);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendRemovedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends a message-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param systemUserName The system username of the user associated with the event.
     * @param receptor The user entity who will receive the notification.
     */
    public void createProjectMessageNotification(String projectSystemName, String systemUserName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.PROJECT_MESSAGE);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userDao.findBySystemUsername(systemUserName).getFullName());
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendProjectMessageNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends a user type change-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param systemUserName The system username of the user associated with the type change.
     * @param receptor The user entity who will receive the notification.
     * @param newUserType The new user type.
     */
    public void createTypeChangedNotification(String projectSystemName, String systemUserName, UserEntity receptor, UserInProjectTypeENUM newUserType){
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.USER_TYPE_CHANGED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userDao.getFullNameBySystemUsername(systemUserName));
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notification.setNewUserType(newUserType);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendTypeChangedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends a project update-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param systemUserName The system username of the user associated with the update.
     * @param receptor The user entity who will receive the notification.
     */
    public void createProjectUpdatedNotification(String projectSystemName, String systemUserName, UserEntity receptor){
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.PROJECT_UPDATED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userDao.getFullNameBySystemUsername(systemUserName));
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendProjectUpdatedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends a project leaving-related notification to a user.
     *
     * @param projectSystemName The system name of the project.
     * @param systemUserName The system username of the user associated with the leaving.
     * @param receptor The user entity who will receive the notification.
     */
    public void createLeftProjectNotification(String projectSystemName, String systemUserName, UserEntity receptor){
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.LEFT_PROJECT);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userDao.getFullNameBySystemUsername(systemUserName));
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendLeftProjectNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Creates and sends a promotion to admin-related notification to a user.
     *
     * @param systemUserName The system username of the user associated with the promotion.
     * @param receptor The user entity who will receive the notification.
     */
    public void createPromoteToAdminNotification(String systemUserName, UserEntity receptor){
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.PROMOTE_TO_ADMIN);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setUserName(userDao.getFullNameBySystemUsername(systemUserName));
        notification.setSystemUserName(systemUserName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendPromoteToAdminNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    /**
     * Retrieves the system username of the creator of a notification by the receptor ID and notification type.
     *
     * @param receptorId The ID of the receptor.
     * @param type The type of the notification.
     * @return The system username of the creator of the notification.
     */
    public String getSystemUsernameOfCreatorOfNotificationByReceptorAndType(int receptorId, NotificationTypeENUM type) {
        return notificationDao.findSystemUsernameOfCreatorByReceptorAndType(receptorId, type);
    }

    /**
     * Marks message notifications as clicked.
     *
     * @param userId The ID of the user.
     * @param notificationIds The list of notification IDs to be marked as clicked.
     */
    public void markMessageNotificationClicked(int userId, List<Integer> notificationIds) {
        notificationDao.markMessageNotificationClicked(userId, notificationIds);
    }

    /**
     * Marks all notifications as clicked for a user and project.
     *
     * @param userId The ID of the user.
     * @param projectSystemName The system name of the project.
     */
    public void markAllNotificationsClicked(int userId, String projectSystemName) {
        notificationDao.markAllNotificationsClicked(userId, projectSystemName);
    }
}
