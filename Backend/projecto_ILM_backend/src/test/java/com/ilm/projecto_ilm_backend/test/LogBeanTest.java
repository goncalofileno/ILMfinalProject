package com.ilm.projecto_ilm_backend.test;

import com.ilm.projecto_ilm_backend.ENUMS.LogTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.bean.LogBean;
import com.ilm.projecto_ilm_backend.bean.NoteBean;
import com.ilm.projecto_ilm_backend.bean.ProjectBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.entity.LogEntity;
import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class LogBeanTest {
    @InjectMocks
    private LogBean logBean;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserDao userDao;

    @Mock
    private LogDao logDao;

    @Mock
    private NoteDao noteDao;

    @Mock
    private UserBean userBean;

    @Mock
    private ProjectBean projectBean;

    @Mock
    private UserProjectDao userProjectDao;

    @Mock
    private NoteBean noteBean;

    private ProjectEntity mockProject;
    private UserEntity mockAuthor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockProject = new ProjectEntity();
        mockProject.setId(1);

        mockAuthor = new UserEntity();
        mockAuthor.setId(1);
    }

    private void verifyLog(LogEntity log, LogTypeENUM expectedType, UserEntity expectedAuthor, ProjectEntity expectedProject) {
        assertEquals(expectedType, log.getType());
        assertEquals(expectedAuthor, log.getAuthor());
        assertEquals(expectedProject, log.getProject());
        assertNotNull(log.getDate());
    }

    @Test
    public void testCreateMemberAddedLog() {
        // Arrange
        String receiver = "Default Receiver";

        // Act
        logBean.createMemberAddedLog(mockProject, mockAuthor, receiver);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.MEMBER_ADDED, mockAuthor, mockProject);
        assertEquals(receiver, capturedLog.getReceiver());
    }

    @Test
    public void testCreateMemberRemovedLog() {
        // Arrange
        String receiver = "Default Receiver";

        // Act
        logBean.createMemberRemovedLog(mockProject, mockAuthor, receiver);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.MEMBER_REMOVED, mockAuthor, mockProject);
        assertEquals(receiver, capturedLog.getReceiver());
    }

    @Test
    public void testCreateTasksCreatedLog() {
        // Arrange
        String taskTitle = "Default Task";

        // Act
        logBean.createTasksCreatedLog(mockProject, mockAuthor, taskTitle);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.TASKS_CREATED, mockAuthor, mockProject);
        assertEquals(taskTitle, capturedLog.getTaskTitle());
    }

    @Test
    public void testCreateTasksCompletedLog() {
        // Arrange
        String taskTitle = "Default Task";
        TaskStatusENUM oldStatus = TaskStatusENUM.PLANNED;
        TaskStatusENUM newStatus = TaskStatusENUM.DONE;

        // Act
        logBean.createTasksCompletedLog(mockProject, mockAuthor, taskTitle, oldStatus, newStatus);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.TASKS_COMPLETED, mockAuthor, mockProject);
        assertEquals(taskTitle, capturedLog.getTaskTitle());
        assertEquals(oldStatus, capturedLog.getTaskOldStatus());
        assertEquals(newStatus, capturedLog.getTaskNewStatus());
    }

    @Test
    public void testCreateTasksInProgressLog() {
        // Arrange
        String taskTitle = "Default Task";
        TaskStatusENUM oldStatus = TaskStatusENUM.PLANNED;
        TaskStatusENUM newStatus = TaskStatusENUM.IN_PROGRESS;

        // Act
        logBean.createTasksInProgressLog(mockProject, mockAuthor, taskTitle, oldStatus, newStatus);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.TASKS_IN_PROGRESS, mockAuthor, mockProject);
        assertEquals(taskTitle, capturedLog.getTaskTitle());
        assertEquals(oldStatus, capturedLog.getTaskOldStatus());
        assertEquals(newStatus, capturedLog.getTaskNewStatus());
    }

    @Test
    public void testCreateTasksDeletedLog() {
        // Arrange
        String taskTitle = "Default Task";

        // Act
        logBean.createTasksDeletedLog(mockProject, mockAuthor, taskTitle);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.TASKS_DELETED, mockAuthor, mockProject);
        assertEquals(taskTitle, capturedLog.getTaskTitle());
    }

    @Test
    public void testCreateTasksUpdatedLog() {
        // Arrange
        String taskTitle = "Default Task";

        // Act
        logBean.createTasksUpdatedLog(mockProject, mockAuthor, taskTitle);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.TASKS_UPDATED, mockAuthor, mockProject);
        assertEquals(taskTitle, capturedLog.getTaskTitle());
    }

    @Test
    public void testCreateProjectInfoUpdatedLog() {
        // Act
        logBean.createProjectInfoUpdatedLog(mockProject, mockAuthor);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.PROJECT_INFO_UPDATED, mockAuthor, mockProject);
    }

    @Test
    public void testCreateProjectStatusUpdatedLog() {
        // Arrange
        StateProjectENUM oldState = StateProjectENUM.PLANNING;
        StateProjectENUM newState = StateProjectENUM.APPROVED;

        // Act
        logBean.createProjectStatusUpdatedLog(mockProject, mockAuthor, oldState, newState);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.PROJECT_STATUS_UPDATED, mockAuthor, mockProject);
        assertEquals(oldState, capturedLog.getProjectOldState());
        assertEquals(newState, capturedLog.getProjectNewState());
    }

    @Test
    public void testCreateResourcesAddedLog() {
        // Act
        logBean.createResourcesAddedLog(mockProject, mockAuthor);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.RESOURCES_UPDATED, mockAuthor, mockProject);
    }

    @Test
    public void testCreateMemberTypeChangedLog() {
        // Arrange
        String receiver = "Default Receiver";
        UserInProjectTypeENUM oldType = UserInProjectTypeENUM.MEMBER;
        UserInProjectTypeENUM newType = UserInProjectTypeENUM.MANAGER;

        // Act
        logBean.createMemberTypeChangedLog(mockProject, mockAuthor, receiver, oldType, newType);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.MEMBER_TYPE_CHANGED, mockAuthor, mockProject);
        assertEquals(receiver, capturedLog.getReceiver());
        assertEquals(oldType, capturedLog.getOldUserType());
        assertEquals(newType, capturedLog.getNewUserType());
    }

    @Test
    public void testCreateMemberLeftLog() {
        // Act
        logBean.createMemberLeftLog(mockProject, mockAuthor);

        // Assert
        ArgumentCaptor<LogEntity> captor = ArgumentCaptor.forClass(LogEntity.class);
        verify(logDao).persist(captor.capture());
        LogEntity capturedLog = captor.getValue();

        verifyLog(capturedLog, LogTypeENUM.MEMBER_LEFT, mockAuthor, mockProject);
    }

    @Test
    public void testCreateMemberAddedLogWithFailure() {
        // Arrange
        String receiver = "Default Receiver";
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createMemberAddedLog(mockProject, mockAuthor, receiver);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testCreateMemberRemovedLogWithFailure() {
        // Arrange
        String receiver = "Default Receiver";
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createMemberRemovedLog(mockProject, mockAuthor, receiver);
        });

        assertEquals("Database error", exception.getMessage());
    }
    @Test
    public void testCreateTasksCreatedLogWithFailure() {
        // Arrange
        String taskTitle = "Default Task";
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createTasksCreatedLog(mockProject, mockAuthor, taskTitle);
        });

        assertEquals("Database error", exception.getMessage());
    }
    @Test
    public void testCreateTasksCompletedLogWithFailure() {
        // Arrange
        String taskTitle = "Default Task";
        TaskStatusENUM oldStatus = TaskStatusENUM.PLANNED;
        TaskStatusENUM newStatus = TaskStatusENUM.DONE;
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createTasksCompletedLog(mockProject, mockAuthor, taskTitle, oldStatus, newStatus);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testCreateTasksInProgressLogWithFailure() {
        // Arrange
        String taskTitle = "Default Task";
        TaskStatusENUM oldStatus = TaskStatusENUM.PLANNED;
        TaskStatusENUM newStatus = TaskStatusENUM.IN_PROGRESS;
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createTasksInProgressLog(mockProject, mockAuthor, taskTitle, oldStatus, newStatus);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testCreateTasksDeletedLogWithFailure() {
        // Arrange
        String taskTitle = "Default Task";
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createTasksDeletedLog(mockProject, mockAuthor, taskTitle);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testCreateTasksUpdatedLogWithFailure() {
        // Arrange
        String taskTitle = "Default Task";
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createTasksUpdatedLog(mockProject, mockAuthor, taskTitle);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testCreateProjectInfoUpdatedLogWithFailure() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createProjectInfoUpdatedLog(mockProject, mockAuthor);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testCreateProjectStatusUpdatedLogWithFailure() {
        // Arrange
        StateProjectENUM oldState = StateProjectENUM.PLANNING;
        StateProjectENUM newState = StateProjectENUM.APPROVED;
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createProjectStatusUpdatedLog(mockProject, mockAuthor, oldState, newState);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testCreateResourcesAddedLogWithFailure() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createResourcesAddedLog(mockProject, mockAuthor);
        });

        assertEquals("Database error", exception.getMessage());
    }
    @Test
    public void testCreateMemberTypeChangedLogWithFailure() {
        // Arrange
        String receiver = "Default Receiver";
        UserInProjectTypeENUM oldType = UserInProjectTypeENUM.MEMBER;
        UserInProjectTypeENUM newType = UserInProjectTypeENUM.MANAGER;
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createMemberTypeChangedLog(mockProject, mockAuthor, receiver, oldType, newType);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    public void testCreateMemberLeftLogWithFailure() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(logDao).persist(any(LogEntity.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            logBean.createMemberLeftLog(mockProject, mockAuthor);
        });

        assertEquals("Database error", exception.getMessage());
    }
}