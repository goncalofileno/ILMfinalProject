package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.dao.NotificationDao;
import com.ilm.projecto_ilm_backend.dao.ProjectDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dto.notification.NotificationDto;
import com.ilm.projecto_ilm_backend.entity.NotificationEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
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

    public void createDefaultNotificationsIfNotExistent() {
        createNotification(NotificationTypeENUM.PROJECT, 1, 1, StateProjectENUM.IN_PROGRESS, null);
        createNotification(NotificationTypeENUM.INVITE, 2, 1, null, 2);
        createNotification(NotificationTypeENUM.INVITE_ACCEPTED, 3, 1, null, 2);
        createNotification(NotificationTypeENUM.INVITE_REJECTED, 4, 1, null, 2);
        createNotification(NotificationTypeENUM.TASK, 5, 1, null, 2);
        createNotification(NotificationTypeENUM.APPLIANCE, 2, 1, null, 2);
        createNotification(NotificationTypeENUM.APPLIANCE_ACCEPTED, 3, 1, null, 2);
        createNotification(NotificationTypeENUM.APPLIANCE_REJECTED, 4, 1, null, 2);
    }

    private void createNotification(NotificationTypeENUM type, int projectId, int receptorId,
                                    StateProjectENUM projectStatus, Integer userId) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(type);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectDao.findById(projectId).getSystemName());
        notification.setReceptor(userDao.findById(receptorId));

        if (projectStatus != null) {
            notification.setProjectStatus(projectStatus);
        }

        if (userId != null) {
            notification.setUserName(userDao.findById(userId).getSystemUsername());
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
        }

        if (entity.getProjectStatus() != null) {
            dto.setProjectStatus(entity.getProjectStatus().name());
        }

        if (entity.getUserName() != null) {
            dto.setUserName(userDao.getFullNameBySystemUsername(entity.getUserName()));
            dto.setUserPhoto(userDao.findBySystemUsername(entity.getUserName()).getAvatarPhoto());
        }
        return dto;
    }

    public void createProjectNotification(String projectSystemName, StateProjectENUM projectStatus, String userName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.PROJECT);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setProjectStatus(projectStatus);
        notification.setUserName(userName);
        notification.setReceptor(receptor);
        notificationDao.persist(notification);
    }

    public void createInviteNotification(String projectSystemName, String userName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.INVITE);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setReceptor(receptor);
        notificationDao.persist(notification);
    }

    public void createInviteAcceptedNotification(String projectSystemName, String userName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.INVITE_ACCEPTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setReceptor(receptor);
        notificationDao.persist(notification);
    }

    public void createInviteRejectedNotification(String projectSystemName, String userName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.INVITE_REJECTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setReceptor(receptor);
        notificationDao.persist(notification);
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
        notificationDao.persist(notification);
    }

    public void createApplianceNotification(String projectSystemName, String userName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.APPLIANCE);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setReceptor(receptor);
        notificationDao.persist(notification);
    }

    public void createApplianceAcceptedNotification(String projectSystemName, String userName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.APPLIANCE_ACCEPTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setReceptor(receptor);
        notificationDao.persist(notification);
    }

    public void createApplianceRejectedNotification(String projectSystemName, String userName, UserEntity receptor) {
        NotificationEntity notification = new NotificationEntity();
        notification.setType(NotificationTypeENUM.APPLIANCE_REJECTED);
        notification.setReadStatus(false);
        notification.setSendDate(LocalDateTime.now());
        notification.setProjectSystemName(projectSystemName);
        notification.setUserName(userName);
        notification.setReceptor(receptor);
        notificationDao.persist(notification);
    }
}
