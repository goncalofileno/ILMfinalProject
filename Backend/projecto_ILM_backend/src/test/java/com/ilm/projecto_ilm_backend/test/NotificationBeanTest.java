package com.ilm.projecto_ilm_backend.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ilm.projecto_ilm_backend.bean.NotificationBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.notification.NotificationDto;
import com.ilm.projecto_ilm_backend.entity.*;
import com.ilm.projecto_ilm_backend.service.websockets.MailWebSocket;
import com.ilm.projecto_ilm_backend.ENUMS.*;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

class NotificationBeanTest {

    @InjectMocks
    private NotificationBean notificationBean;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserDao userDao;

    @Mock
    private NotificationDao notificationDao;

    @Mock
    private SessionDao sessionDao;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        UserEntity mockUser = new UserEntity();
        mockUser.setId(1);
        mockUser.setSystemUsername("user1");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setAvatarPhoto("avatar.png");

        // Create a mock project
        ProjectEntity mockProject = new ProjectEntity();
        mockProject.setId(1);
        mockProject.setSystemName("project1");

        // Mock the returns
        when(userDao.findById(1)).thenReturn(mockUser);
        when(userDao.findBySystemUsername("user1")).thenReturn(mockUser);
        when(projectDao.findById(1)).thenReturn(mockProject);
    }


    @Test
    public void testGetTotalUserNotificationsSuccess() {
        int userId = 1;
        when(notificationDao.countAllByUserId(userId)).thenReturn(10);

        int totalNotifications = notificationBean.getTotalUserNotifications(userId);

        assertEquals(10, totalNotifications);
    }

    @Test
    public void testGetUnreadNotificationCountSuccess() {
        int userId = 1;
        when(notificationDao.countUnreadByUserId(userId)).thenReturn(5);

        int unreadCount = notificationBean.getUnreadNotificationCount(userId);

        assertEquals(5, unreadCount);
    }

    @Test
    public void testCreateProjectNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createProjectNotification("project1", StateProjectENUM.APPROVED, "user1", receptor, "user1");

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendProjectNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateRejectProjectNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createRejectProjectNotification("project1", StateProjectENUM.CANCELED, "user1", receptor, "user1");

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendProjectNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateInviteNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createInviteNotification("project1", "user1", receptor, "user1");

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendInviteNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateProjectInsertedNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createProjectInsertedNotification("project1", "user1", receptor, "user1");

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendProjectInsertedNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateInviteAcceptedNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createInviteAcceptedNotification("project1", "user1", receptor, "user1");

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendInviteAcceptedNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateInviteRejectedNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createInviteRejectedNotification("project1", "user1", receptor, "user1");

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendInviteRejectedNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateTaskNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");
        when(notificationDao.findSimilarTaskNotification(anyString(), anyString(), any(UserEntity.class))).thenReturn(false);

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createTaskNotification("task1", "project1", "user1", receptor);

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendTaskNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateTaskAssignedNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");
        when(notificationDao.findSimilarTaskNotification(anyString(), anyString(), any(UserEntity.class))).thenReturn(false);

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createTaskAssignedNotification("task1", "project1", "user1", receptor);

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendTaskAssignedNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateApplianceNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createApplianceNotification("project1", "user1", receptor, "user1");

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendApplianceNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateApplianceAcceptedNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createApplianceAcceptedNotification("project1", "user1", receptor);

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendApplianceAcceptedNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateApplianceRejectedNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createApplianceRejectedNotification("project1", "user1", receptor);

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendApplianceRejectedNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateRemovedNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createRemovedNotification("project1", "user1", receptor);

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendRemovedNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateProjectMessageNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createProjectMessageNotification("project1", "user1", receptor);

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendProjectMessageNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateTypeChangedNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createTypeChangedNotification("project1", "user1", receptor, UserInProjectTypeENUM.MEMBER);

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendTypeChangedNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateProjectUpdatedNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createProjectUpdatedNotification("project1", "user1", receptor);

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendProjectUpdatedNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testCreateLeftProjectNotificationSuccess() {
        UserEntity receptor = new UserEntity();
        receptor.setId(1);

        when(sessionDao.findSessionIdByUserId(receptor.getId())).thenReturn("session123");

        try (MockedStatic<MailWebSocket> mockedStatic = mockStatic(MailWebSocket.class)) {
            notificationBean.createLeftProjectNotification("project1", "user1", receptor);

            verify(notificationDao, times(1)).persist(any(NotificationEntity.class));
            mockedStatic.verify(() -> MailWebSocket.sendLeftProjectNotification(eq("session123"), any(NotificationDto.class)), times(1));
        }
    }

    @Test
    public void testGetSystemUsernameOfCreatorOfNotificationByReceptorAndTypeSuccess() {
        int receptorId = 1;
        NotificationTypeENUM type = NotificationTypeENUM.PROJECT;
        when(notificationDao.findSystemUsernameOfCreatorByReceptorAndType(receptorId, type)).thenReturn("user1");

        String username = notificationBean.getSystemUsernameOfCreatorOfNotificationByReceptorAndType(receptorId, type);

        assertEquals("user1", username);
    }

    @Test
    public void testMarkMessageNotificationClickedSuccess() {
        int userId = 1;
        List<Integer> notificationIds = Arrays.asList(1, 2, 3);

        doNothing().when(notificationDao).markMessageNotificationClicked(userId, notificationIds);

        notificationBean.markMessageNotificationClicked(userId, notificationIds);

        verify(notificationDao, times(1)).markMessageNotificationClicked(userId, notificationIds);
    }

    @Test
    public void testMarkAllNotificationsClickedSuccess() {
        int userId = 1;
        String projectSystemName = "project1";

        doNothing().when(notificationDao).markAllNotificationsClicked(userId, projectSystemName);

        notificationBean.markAllNotificationsClicked(userId, projectSystemName);

        verify(notificationDao, times(1)).markAllNotificationsClicked(userId, projectSystemName);
    }
}