package com.ilm.projecto_ilm_backend.test;

import static org.junit.jupiter.api.Assertions.*;

import com.ilm.projecto_ilm_backend.bean.MessageBean;
import com.ilm.projecto_ilm_backend.bean.NotificationBean;
import com.ilm.projecto_ilm_backend.bean.ProjectBean;
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
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class MessageBeanTest {
    @Mock
    private MessageDao messageDao;

    @Mock
    private UserDao userDao;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserProjectDao userProjectDao;

    @Mock
    private ProjectBean projectBean;

    @Mock
    private NotificationBean notificationBean;

    @Spy
    private ProjectChatWebSocket projectChatWebSocket = new ProjectChatWebSocket();

    @Mock
    private SessionDao sessionDao;

    @InjectMocks
    private MessageBean messageBean;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMessages() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("user1");

        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("project1");

        when(projectDao.findBySystemName("project1")).thenReturn(project);
        when(userProjectDao.isUserInProject(1, 1)).thenReturn(true);

        List<MessageEntity> messages = new ArrayList<>();
        MessageEntity message = new MessageEntity("Hello", LocalDateTime.now(), user, project);
        messages.add(message);

        when(messageDao.findByProjectId(1)).thenReturn(messages);

        List<MessageDto> messageDtos = messageBean.getMessages(user, "project1");

        assertEquals(1, messageDtos.size());
        assertEquals("Hello", messageDtos.get(0).getMessage());
    }

    @Test
    public void testGetChatPage() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("user1");

        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("project1");
        project.setName("Project 1");

        when(projectDao.findBySystemName("project1")).thenReturn(project);
        when(userProjectDao.isUserInProject(1, 1)).thenReturn(true);

        List<MessageEntity> messages = new ArrayList<>();
        MessageEntity message = new MessageEntity("Hello", LocalDateTime.now(), user, project);
        messages.add(message);

        when(messageDao.findByProjectId(1)).thenReturn(messages);

        List<ProjectMemberDto> projectMembers = new ArrayList<>();
        when(projectBean.getProjectMembers(1)).thenReturn(projectMembers);

        MessagesPageDto chatPage = messageBean.getChatPage(user, "project1");

        assertEquals(1, chatPage.getMessages().size());
        assertEquals("Project 1", chatPage.getProjectName());
    }

    @Test
    public void testSendMessage() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("user1");

        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("project1");

        when(projectDao.findBySystemName("project1")).thenReturn(project);
        when(userProjectDao.isUserInProject(1, 1)).thenReturn(true);

        MessageDto messageDto = new MessageDto("User1", "Hello", LocalDateTime.now(), "user1", null);
        MessageEntity messageEntity = new MessageEntity("Hello", LocalDateTime.now(), user, project);

        List<UserProjectEntity> userProjectEntities = new ArrayList<>();
        UserProjectEntity userProjectEntity = new UserProjectEntity();
        UserEntity member = new UserEntity();
        member.setSystemUsername("user2"); // Setting a valid username
        userProjectEntity.setUser(member);
        userProjectEntities.add(userProjectEntity);

        when(userProjectDao.findMembersByProjectId(1)).thenReturn(userProjectEntities);

        doNothing().when(notificationBean).createProjectMessageNotification(anyString(), anyString(), any(UserEntity.class));
        doNothing().when(projectChatWebSocket).broadcastMessage(anyString(), any(MessageDto.class));
        doReturn(false).when(projectChatWebSocket).isUserOnlineInProject(anyString(), anyString());

        messageBean.sendMessage(user, "project1", messageDto);

        verify(messageDao, times(1)).persist(any(MessageEntity.class));
        verify(projectChatWebSocket, times(1)).broadcastMessage(eq("project1"), any(MessageDto.class));
        verify(notificationBean, times(1)).createProjectMessageNotification(anyString(), anyString(), any(UserEntity.class));
    }

    @Test
    public void testGetChatPageProjectNotFound() {
        UserEntity user = new UserEntity();
        user.setId(1);

        when(projectDao.findBySystemName("invalidProject")).thenReturn(null);

        assertThrows(ProjectNotFoundException.class, () -> {
            messageBean.getChatPage(user, "invalidProject");
        });
    }

    @Test
    public void testGetChatPageUserNotInProject() {
        UserEntity user = new UserEntity();
        user.setId(1);

        ProjectEntity project = new ProjectEntity();
        project.setId(1);

        when(projectDao.findBySystemName("project1")).thenReturn(project);
        when(userProjectDao.isUserInProject(1, 1)).thenReturn(false);

        assertThrows(UserNotInProjectException.class, () -> {
            messageBean.getChatPage(user, "project1");
        });
    }

    @Test
    public void testSendMessageProjectNotFound() {
        UserEntity user = new UserEntity();
        user.setId(1);

        MessageDto messageDto = new MessageDto("User1", "Hello", LocalDateTime.now(), "user1", null);

        when(projectDao.findBySystemName("invalidProject")).thenReturn(null);

        assertThrows(ProjectNotFoundException.class, () -> {
            messageBean.sendMessage(user, "invalidProject", messageDto);
        });
    }

    @Test
    public void testSendMessageUserNotInProject() {
        UserEntity user = new UserEntity();
        user.setId(1);

        ProjectEntity project = new ProjectEntity();
        project.setId(1);

        MessageDto messageDto = new MessageDto("User1", "Hello", LocalDateTime.now(), "user1", null);

        when(projectDao.findBySystemName("project1")).thenReturn(project);
        when(userProjectDao.isUserInProject(1, 1)).thenReturn(false);

        assertThrows(UserNotInProjectException.class, () -> {
            messageBean.sendMessage(user, "project1", messageDto);
        });
    }

}