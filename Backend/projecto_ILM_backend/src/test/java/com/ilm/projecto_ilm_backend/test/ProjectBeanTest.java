package com.ilm.projecto_ilm_backend.test;

import com.ilm.projecto_ilm_backend.ENUMS.*;
import com.ilm.projecto_ilm_backend.bean.*;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.mail.MailDto;
import com.ilm.projecto_ilm_backend.entity.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectBeanTest {

    @InjectMocks
    private ProjectBean projectBean;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private LabDao labDao;

    @Mock
    private UserDao userDao;

    @Mock
    private SkillDao skillDao;

    @Mock
    private TaskDao taskDao;

    @Mock
    private UserTaskDao userTaskDao;

    @Mock
    private NotificationDao notificationDao;

    @Mock
    private UserProjectDao userProjectDao;

    @Mock
    private NotificationBean notificationBean;

    @Mock
    private ResourceBean resourceBean;

    @Mock
    private SessionDao sessionDao;

    @Mock
    private MailBean mailBean;

    @Mock
    private UserBean userBean;

    @Mock
    private LogBean logBean;

    @Mock
    private TaskBean taskBean;

    @Mock
    private ResourceSupplierDao resourceSupplierDao;

    @Mock
    private ProjectResourceDao projectResourceDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFinalPresentation() {
        UserEntity user = new UserEntity();
        user.setId(1);
        String projectSystemName = "test_project";

        ProjectEntity project = new ProjectEntity();
        project.setEndDate(LocalDateTime.now().plusDays(1));

        when(projectDao.findBySystemName(projectSystemName)).thenReturn(project);

        projectBean.createFinalPresentation(user, projectSystemName);

        verify(taskDao, times(1)).persist(any(TaskEntity.class));
        verify(userTaskDao, times(1)).merge(any(UserTaskEntity.class));
    }
    @Test
    public void testCreateFinalPresentation_Failure() {
        UserEntity user = new UserEntity();
        user.setId(1);
        String projectSystemName = "test_project";

        when(projectDao.findBySystemName(projectSystemName)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            try {
                projectBean.createFinalPresentation(user, projectSystemName);
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Project not found", e);
            }
        });

        String expectedMessage = "Project not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(taskDao, never()).persist(any(TaskEntity.class));
        verify(userTaskDao, never()).merge(any(UserTaskEntity.class));
    }
    @Test
    public void testProjectSystemNameGenerator() {
        String originalName = "Test Project!";
        String expectedSystemName = "test_project";

        String actualSystemName = projectBean.projectSystemNameGenerator(originalName);

        assertEquals(expectedSystemName, actualSystemName);
    }



    @Test
    public void testInviteUserToProject() {
        String sessionId = "session123";
        String systemUsername = "testUser";
        String projectName = "testProject";

        UserEntity userToInvite = new UserEntity();
        userToInvite.setId(2);
        userToInvite.setLanguage(LanguageENUM.ENGLISH);
        userToInvite.setEmail("invitee@example.com");
        userToInvite.setFirstName("John");
        userToInvite.setLastName("Doe");

        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setName(projectName);
        project.setStatus(StateProjectENUM.PLANNING);
        project.setMaxMembers(5);
        project.setSystemName("test_project");

        UserEntity sender = new UserEntity();
        sender.setId(3);
        sender.setFirstName("Alice");
        sender.setLastName("Smith");
        sender.setEmail("sender@example.com");
        sender.setSystemUsername("alice");

        SessionEntity session = new SessionEntity();
        session.setUser(sender);

        UserEntity administration = new UserEntity();
        administration.setSystemUsername("administration");
        administration.setEmail("admin@example.com");
        administration.setFirstName("Admin");
        administration.setLastName("User");

        when(userDao.findBySystemUsername(systemUsername)).thenReturn(userToInvite);
        when(projectDao.findByName(projectName)).thenReturn(project);
        when(sessionDao.findBySessionId(sessionId)).thenReturn(session);
        when(userProjectDao.getNumberOfUsersByProjectId(project.getId())).thenReturn(3);
        when(userDao.findBySystemUsername("administration")).thenReturn(administration);

        doAnswer(invocation -> null).when(notificationBean).createInviteNotification(anyString(), anyString(), any(UserEntity.class), anyString());
        doAnswer(invocation -> null).when(mailBean).sendMail(any(UserEntity.class), any(MailDto.class));

        String result = projectBean.inviteUserToProject(sessionId, systemUsername, projectName);

        assertEquals("User invited successfully", result);
        verify(userProjectDao, times(1)).persist(any(UserProjectEntity.class));
        verify(mailBean, times(1)).sendMail(eq(administration), any(MailDto.class));

    }

    @Test
    public void testInviteUserToProject_Failure() {
        String sessionId = "session123";
        String systemUsername = "testUser";
        String projectName = "testProject";

        when(userDao.findBySystemUsername(systemUsername)).thenReturn(null);
        when(projectDao.findByName(projectName)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            try {
                projectBean.inviteUserToProject(sessionId, systemUsername, projectName);
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("User or project not found", e);
            }
        });

        String expectedMessage = "User or project not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(userProjectDao, never()).persist(any(UserProjectEntity.class));
        verify(mailBean, never()).sendMail(any(UserEntity.class), any(MailDto.class));
    }

    @Test
    public void testGetProgressPlanning() {
        ProjectEntity project = new ProjectEntity();
        project.setStatus(StateProjectENUM.PLANNING);
        when(projectDao.findById(1)).thenReturn(project);

        int progress = projectBean.getProgress(1);
        assertEquals(10, progress);
    }

    @Test
    public void testGetProgressReady() {
        ProjectEntity project = new ProjectEntity();
        project.setStatus(StateProjectENUM.READY);
        when(projectDao.findById(1)).thenReturn(project);

        int progress = projectBean.getProgress(1);
        assertEquals(20, progress);
    }

    @Test
    public void testGetProgressFinished() {
        ProjectEntity project = new ProjectEntity();
        project.setStatus(StateProjectENUM.FINISHED);
        when(projectDao.findById(1)).thenReturn(project);

        int progress = projectBean.getProgress(1);
        assertEquals(100, progress);
    }

    @Test
    public void testGetProgressCanceled() {
        ProjectEntity project = new ProjectEntity();
        project.setStatus(StateProjectENUM.CANCELED);
        when(projectDao.findById(1)).thenReturn(project);

        int progress = projectBean.getProgress(1);
        assertEquals(0, progress);
    }

    @Test
    public void testGetProgressWithTasks() {
        ProjectEntity project = new ProjectEntity();
        project.setStatus(StateProjectENUM.IN_PROGRESS);
        when(projectDao.findById(1)).thenReturn(project);

        TaskEntity doneTask = new TaskEntity();
        doneTask.setStatus(TaskStatusENUM.DONE);

        TaskEntity inProgressTask = new TaskEntity();
        inProgressTask.setStatus(TaskStatusENUM.IN_PROGRESS);

        List<TaskEntity> tasks = List.of(doneTask, inProgressTask);

        when(taskDao.findByProject(1)).thenReturn(tasks);

        int progress = projectBean.getProgress(1);
        assertEquals(55, progress);  // 20% base + 35% (half of the 70% range)
    }

    @Test
    public void testGetProgressNoTasks() {
        ProjectEntity project = new ProjectEntity();
        project.setStatus(StateProjectENUM.IN_PROGRESS);
        when(projectDao.findById(1)).thenReturn(project);

        List<TaskEntity> tasks = Collections.emptyList();

        when(taskDao.findByProject(1)).thenReturn(tasks);

        int progress = projectBean.getProgress(1);
        assertEquals(20, progress);  // No tasks, return 20%
    }

    @Test
    public void testGetUserTypeInProjectMember() {
        UserProjectEntity userProject = new UserProjectEntity();
        userProject.setType(UserInProjectTypeENUM.MEMBER);
        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(userProject);

        UserInProjectTypeENUM userType = projectBean.getUserTypeInProject(1, 1);
        assertEquals(UserInProjectTypeENUM.MEMBER, userType);
    }
    @Test
    public void testGetUserTypeInProject_Failure() {
        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(null);
        when(userDao.findById(1)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            try {
                projectBean.getUserTypeInProject(1, 1);
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("User not found", e);
            }
        });

        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void testGetUserTypeInProjectAdmin() {
        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(null);

        UserEntity user = new UserEntity();
        user.setType(UserTypeENUM.ADMIN);
        when(userDao.findById(1)).thenReturn(user);

        UserInProjectTypeENUM userType = projectBean.getUserTypeInProject(1, 1);
        assertEquals(UserInProjectTypeENUM.ADMIN, userType);
    }

    @Test
    public void testGetUserTypeInProjectGuest() {
        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(null);

        UserEntity user = new UserEntity();
        user.setType(UserTypeENUM.STANDARD_USER);
        when(userDao.findById(1)).thenReturn(user);

        UserInProjectTypeENUM userType = projectBean.getUserTypeInProject(1, 1);
        assertEquals(UserInProjectTypeENUM.GUEST, userType);
    }

    @Test
    public void testGetProgress_Planning() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setStatus(StateProjectENUM.PLANNING);

        when(projectDao.findById(1)).thenReturn(project);

        int progress = projectBean.getProgress(1);
        assertEquals(10, progress);
    }

    @Test
    public void testGetProgress_Ready() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setStatus(StateProjectENUM.READY);

        when(projectDao.findById(1)).thenReturn(project);

        int progress = projectBean.getProgress(1);
        assertEquals(20, progress);
    }

    @Test
    public void testGetProgress_Finished() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setStatus(StateProjectENUM.FINISHED);

        when(projectDao.findById(1)).thenReturn(project);

        int progress = projectBean.getProgress(1);
        assertEquals(100, progress);
    }

    @Test
    public void testGetProgress_Canceled() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setStatus(StateProjectENUM.CANCELED);

        when(projectDao.findById(1)).thenReturn(project);

        int progress = projectBean.getProgress(1);
        assertEquals(0, progress);
    }

    @Test
    public void testGetProgress_Tasks() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setStatus(StateProjectENUM.IN_PROGRESS);

        TaskEntity task1 = new TaskEntity();
        task1.setStatus(TaskStatusENUM.DONE);
        TaskEntity task2 = new TaskEntity();
        task2.setStatus(TaskStatusENUM.IN_PROGRESS);

        when(projectDao.findById(1)).thenReturn(project);
        when(taskDao.findByProject(1)).thenReturn(Arrays.asList(task1, task2));

        int progress = projectBean.getProgress(1);
        assertEquals(55, progress); // 20% + 70% * (1/2) = 55%
    }

    @Test
    public void testGetUserTypeInProject_UserIsAdmin() {
        UserEntity user = new UserEntity();
        user.setType(UserTypeENUM.ADMIN);

        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(null);
        when(userDao.findById(1)).thenReturn(user);

        UserInProjectTypeENUM userType = projectBean.getUserTypeInProject(1, 1);
        assertEquals(UserInProjectTypeENUM.ADMIN, userType);
    }

    @Test
    public void testGetUserTypeInProject_UserIsGuest() {
        UserEntity user = new UserEntity();
        user.setType(UserTypeENUM.STANDARD_USER);

        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(null);
        when(userDao.findById(1)).thenReturn(user);

        UserInProjectTypeENUM userType = projectBean.getUserTypeInProject(1, 1);
        assertEquals(UserInProjectTypeENUM.GUEST, userType);
    }

    @Test
    public void testJoinProject_Success() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("test_project");
        project.setStatus(StateProjectENUM.IN_PROGRESS);

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("john");

        when(projectDao.findBySystemName("test_project")).thenReturn(project);
        when(userDao.findById(1)).thenReturn(user);
        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(null);

        List<UserEntity> teamLeaders = Arrays.asList(user);
        when(userProjectDao.findCreatorsAndManagersByProjectId(1)).thenReturn(teamLeaders);

        doNothing().when(userProjectDao).persist(any(UserProjectEntity.class));
        doNothing().when(notificationBean).createApplianceNotification(anyString(), anyString(), any(UserEntity.class), anyString());

        String result = projectBean.joinProject(1, "test_project");
        assertEquals("User applied to join project successfully", result);
    }

    @Test
    public void testJoinProject_Failure() {
        when(projectDao.findBySystemName("test_project")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            projectBean.joinProject(1, "test_project");
        });

        verify(userProjectDao, never()).persist(any(UserProjectEntity.class));
        verify(notificationBean, never()).createApplianceNotification(anyString(), anyString(), any(UserEntity.class), anyString());
    }
    @Test
    public void testCancelProject() {
        // Create and set up the entities
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("test_project");
        project.setName("Test Project");
        project.setStatus(StateProjectENUM.PLANNING);

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("john");
        user.setType(UserTypeENUM.ADMIN);

        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setUser(user);
        userProjectEntity.setProject(project);

        List<UserProjectEntity> userProjectList = new ArrayList<>();
        userProjectList.add(userProjectEntity);

        // Set up the mocks
        when(projectDao.findBySystemName("test_project")).thenReturn(project);
        when(userDao.findById(1)).thenReturn(user);
        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(userProjectEntity);
        when(userProjectDao.isUserCreatorOrManager(1, 1)).thenReturn(true);
        when(userProjectDao.findMembersByProjectId(1)).thenReturn(userProjectList);

        doAnswer(invocation -> null).when(projectDao).merge(any(ProjectEntity.class));
        doAnswer(invocation -> null).when(logBean).createProjectStatusUpdatedLog(any(), any(), any(), any());
        doAnswer(invocation -> null).when(notificationBean).createProjectNotification(anyString(), any(), anyString(), any(), anyString());
        doAnswer(invocation -> null).when(mailBean).sendMail(any(UserEntity.class), any(MailDto.class));

        // Call the method under test
        String result = projectBean.cancelProject(1, "test_project", "Reason", "sessionId");

        // Verify the results
        assertEquals("Project canceled successfully", result);
        assertEquals(StateProjectENUM.CANCELED, project.getStatus());

        // Verify the interactions
        verify(projectDao).merge(project);
        verify(logBean).createProjectStatusUpdatedLog(any(), any(), any(), any());
    }

    @Test
    public void testMarkReasonAsRead() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("test_project");
        project.setName("Test Project");
        project.setReason("Some reason");

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("john");

        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setUser(user);
        userProjectEntity.setProject(project);
        userProjectEntity.setType(UserInProjectTypeENUM.CREATOR);

        when(projectDao.findBySystemName("test_project")).thenReturn(project);
        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(userProjectEntity);

        doNothing().when(projectDao).merge(any(ProjectEntity.class));

        String result = projectBean.markReasonAsRead(1, "test_project");
        assertEquals("Reason marked as read successfully", result);
        assertNull(project.getReason());
    }
    @Test
    public void testMarkReasonAsRead_Failure() {
        when(projectDao.findBySystemName("test_project")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            projectBean.markReasonAsRead(1, "test_project");
        });

        verify(projectDao, never()).merge(any(ProjectEntity.class));
    }
    @Test
    public void testGetUserTypeInProject() {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setType(UserTypeENUM.STANDARD_USER);

        ProjectEntity project = new ProjectEntity();
        project.setId(1);

        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setUser(user);
        userProjectEntity.setProject(project);
        userProjectEntity.setType(UserInProjectTypeENUM.MEMBER);

        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(userProjectEntity);
        when(userDao.findById(1)).thenReturn(user);

        UserInProjectTypeENUM userType = projectBean.getUserTypeInProject(1, 1);
        assertEquals(UserInProjectTypeENUM.MEMBER, userType);

        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(null);
        user.setType(UserTypeENUM.ADMIN);

        userType = projectBean.getUserTypeInProject(1, 1);
        assertEquals(UserInProjectTypeENUM.ADMIN, userType);

        user.setType(UserTypeENUM.STANDARD_USER);

        userType = projectBean.getUserTypeInProject(1, 1);
        assertEquals(UserInProjectTypeENUM.GUEST, userType);
    }
    @Test
    public void testRemoveInvitation() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("test_project");
        project.setName("Test Project");

        UserEntity userToRemove = new UserEntity();
        userToRemove.setId(2);
        userToRemove.setSystemUsername("userToRemove");
        userToRemove.setLanguage(LanguageENUM.ENGLISH);

        UserEntity currentUser = new UserEntity();
        currentUser.setId(3);
        currentUser.setSystemUsername("currentUser");

        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setUser(userToRemove);
        userProjectEntity.setProject(project);
        userProjectEntity.setType(UserInProjectTypeENUM.PENDING_BY_INVITATION);

        when(projectDao.findBySystemName("test_project")).thenReturn(project);
        when(userDao.findById(2)).thenReturn(userToRemove);
        when(userDao.findById(3)).thenReturn(currentUser);
        when(userProjectDao.findByUserIdAndProjectId(2, 1)).thenReturn(userProjectEntity);
        when(userDao.findBySystemUsername("administration")).thenReturn(currentUser);

        doNothing().when(userProjectDao).remove(any(UserProjectEntity.class));
        doNothing().when(notificationDao).removeByProjectIdAndReceptorAndType(anyString(), anyInt(), any(NotificationTypeENUM.class));
        doAnswer(invocation -> null).when(mailBean).sendMail(any(UserEntity.class), any(MailDto.class));

        String result = projectBean.removeInvitation("test_project", 2, 3, "sessionId");
        assertEquals("Invitation removed successfully", result);
    }
    @Test
    public void testRemoveInvitation_Failure() {
        when(projectDao.findBySystemName("test_project")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            try {
                projectBean.removeInvitation("test_project", 2, 3, "sessionId");
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Project not found", e);
            }
        });

        String expectedMessage = "Project not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(userProjectDao, never()).remove(any(UserProjectEntity.class));
        verify(notificationDao, never()).removeByProjectIdAndReceptorAndType(anyString(), anyInt(), any(NotificationTypeENUM.class));
        verify(mailBean, never()).sendMail(any(UserEntity.class), any(MailDto.class));
    }
    @Test
    public void testLeaveProject() {
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("test_project");

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("user");

        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setUser(user);
        userProjectEntity.setProject(project);
        userProjectEntity.setType(UserInProjectTypeENUM.MEMBER);

        List<UserEntity> teamManagers = new ArrayList<>();
        teamManagers.add(user);

        when(projectDao.findBySystemName("test_project")).thenReturn(project);
        when(userDao.findById(1)).thenReturn(user);
        when(userProjectDao.findByUserIdAndProjectId(1, 1)).thenReturn(userProjectEntity);
        when(userProjectDao.findCreatorsAndManagersByProjectId(1)).thenReturn(teamManagers);
        when(userDao.findBySystemUsername("administration")).thenReturn(user);

        doNothing().when(userProjectDao).remove(any(UserProjectEntity.class));
        doNothing().when(taskBean).removeUserFromProjectTasks(anyInt(), anyInt());
        doNothing().when(notificationBean).createLeftProjectNotification(anyString(), anyString(), any());
        doAnswer(invocation -> null).when(mailBean).sendMail(any(UserEntity.class), any(MailDto.class));
        doNothing().when(logBean).createMemberLeftLog(any(ProjectEntity.class), any(UserEntity.class));

        String result = projectBean.leaveProject(1, "test_project", "Reason");
        assertEquals("User left project successfully", result);
    }
    @Test
    public void testLeaveProject_Failure() {
        when(projectDao.findBySystemName("test_project")).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            try {
                projectBean.leaveProject(1, "test_project", "Reason");
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("Project not found", e);
            }
        });

        String expectedMessage = "Project not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        verify(userProjectDao, never()).remove(any(UserProjectEntity.class));
        verify(taskBean, never()).removeUserFromProjectTasks(anyInt(), anyInt());
        verify(notificationBean, never()).createLeftProjectNotification(anyString(), anyString(), any());
        verify(mailBean, never()).sendMail(any(UserEntity.class), any(MailDto.class));
        verify(logBean, never()).createMemberLeftLog(any(ProjectEntity.class), any(UserEntity.class));
    }
}