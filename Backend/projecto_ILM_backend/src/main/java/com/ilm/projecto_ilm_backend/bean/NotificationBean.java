package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.dao.NotificationDao;
import com.ilm.projecto_ilm_backend.dao.ProjectDao;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dto.notification.NotificationDto;
import com.ilm.projecto_ilm_backend.entity.NotificationEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.service.websockets.MailWebSocket;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public int getTotalUserNotifications(int userId) {
        return notificationDao.countAllByUserId(userId);
    }

    public int getUnreadNotificationCount(int userId) {
        return notificationDao.countUnreadByUserId(userId);
    }

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
        return dto;
    }

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
        }else {
            System.out.println("SESSION NOT FOUND TO SEND INVITE NOTIFICATION");
        }
    }

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

    public void createTaskNotification(String taskName, String projectSystemName, String userName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.TASK);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setTaskName(taskName);
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendTaskNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

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

    public void createApplianceAcceptedNotification(String projectSystemName, String userName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.APPLIANCE_ACCEPTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendApplianceAcceptedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    public void createApplianceRejectedNotification(String projectSystemName, String userName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.APPLIANCE_REJECTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendApplianceRejectedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

    public void createRemovedNotification(String projectSystemName, String userName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.REMOVED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setReceptor(receptor);
        notification.setMessageNotificationClicked(false);
        notificationDao.persist(notification);
        if(sessionDao.findSessionIdByUserId(receptor.getId()) != null) {
            MailWebSocket.sendRemovedNotification(sessionDao.findSessionIdByUserId(receptor.getId()), toDto(notification));
        }
    }

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

    public String getSystemUsernameOfCreatorOfNotificationByReceptorAndType(int receptorId, NotificationTypeENUM type) {
        return notificationDao.findSystemUsernameOfCreatorByReceptorAndType(receptorId, type);
    }

    public void markMessageNotificationClicked(int userId, List<Integer> notificationIds) {
        notificationDao.markMessageNotificationClicked(userId, notificationIds);
    }
}
