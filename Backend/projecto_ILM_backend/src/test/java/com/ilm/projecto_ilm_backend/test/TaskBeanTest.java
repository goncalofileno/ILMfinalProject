package com.ilm.projecto_ilm_backend.test;

import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;
import com.ilm.projecto_ilm_backend.bean.*;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.task.TaskSuggestionDto;
import com.ilm.projecto_ilm_backend.dto.task.TasksPageDto;
import com.ilm.projecto_ilm_backend.dto.task.UpdateTaskDto;
import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import com.ilm.projecto_ilm_backend.entity.TaskEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.entity.UserTaskEntity;
import com.ilm.projecto_ilm_backend.security.exceptions.ProjectNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotInProjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TaskBeanTest {
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
    private UserBean userBean;

    @Mock
    private UserProjectDao userProjectDao;

    @Mock
    private ProjectBean projectBean;

    @Mock
    private NotificationBean notificationBean;

    @Mock
    private NotificationDao notificationDao;

    @Mock
    private LogBean logBean;

    @InjectMocks
    private TaskBean taskBean;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testGetTasksSuggestions_Success() throws Exception {
        // Given
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("testProject");
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("testUser");

        TaskEntity task1 = new TaskEntity();
        task1.setTitle("Task 1");
        task1.setSystemTitle("task1");
        TaskEntity task2 = new TaskEntity();
        task2.setTitle("Task 2");
        task2.setSystemTitle("task2");

        when(projectDao.findBySystemName(anyString())).thenReturn(project);
        when(userBean.getUserBySessionId(anyString())).thenReturn(user);
        when(userProjectDao.isUserInProject(anyInt(), anyInt())).thenReturn(true);
        when(taskDao.findByProject(anyInt())).thenReturn(Arrays.asList(task1, task2));

        // When
        List<TaskSuggestionDto> result = taskBean.getTasksSuggestions("testSessionId", "testProject");

        // Then
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("task1", result.get(0).getSystemTitle());
        assertEquals("Task 2", result.get(1).getTitle());
        assertEquals("task2", result.get(1).getSystemTitle());
    }
    @Test
    public void testGetTasksPageDto_Success() throws Exception {
        // Given
        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("testProject");
        project.setName("Test Project");
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("testUser");

        TaskEntity task1 = new TaskEntity();
        task1.setId(1);
        task1.setTitle("Task 1");
        task1.setSystemTitle("task1");
        task1.setDependentTasks(new ArrayList<>()); // Ensure dependent tasks are initialized

        when(projectDao.findBySystemName(anyString())).thenReturn(project);
        when(userBean.getUserBySessionId(anyString())).thenReturn(user);
        when(userProjectDao.isUserInProject(anyInt(), anyInt())).thenReturn(true);
        when(taskDao.findByProject(anyInt())).thenReturn(Collections.singletonList(task1));

        // When
        TasksPageDto result = taskBean.getTasksPageDto("testSessionId", "testProject");

        // Then
        assertEquals("Test Project", result.getProjectName());
        assertEquals(1, result.getTasks().size());
        assertEquals("Task 1", result.getTasks().get(0).getTitle());
        assertEquals("task1", result.getTasks().get(0).getSystemTitle());
    }@Test
    public void testAddTask_Success() throws Exception {
        // Given
        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setTitle("New Task");
        updateTaskDto.setSystemProjectName("testProject");
        updateTaskDto.setDescription("Description");
        updateTaskDto.setInitialDate(LocalDateTime.now());
        updateTaskDto.setFinalDate(LocalDateTime.now().plusDays(10));
        updateTaskDto.setInChargeId(2);
        updateTaskDto.setMemberIds(Collections.singletonList(3));
        updateTaskDto.setDependentTaskIds(Collections.singletonList(1));

        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("testProject");
        project.setStartDate(LocalDateTime.now().minusDays(5));
        project.setEndDate(LocalDateTime.now().plusDays(30));

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("testUser");

        UserEntity inChargeUser = new UserEntity();
        inChargeUser.setId(2);

        UserEntity memberUser = new UserEntity();
        memberUser.setId(3);

        TaskEntity dependentTask = new TaskEntity();
        dependentTask.setId(1);
        dependentTask.setInitialDate(LocalDateTime.now().minusDays(10));
        dependentTask.setFinalDate(LocalDateTime.now().minusDays(5));

        when(projectDao.findBySystemName(anyString())).thenReturn(project);
        when(userBean.getUserBySessionId(anyString())).thenReturn(user);
        when(userProjectDao.isUserInProject(anyInt(), anyInt())).thenReturn(true);
        when(userDao.findById(2)).thenReturn(inChargeUser);
        when(userDao.findById(3)).thenReturn(memberUser);
        when(taskDao.findById(1)).thenReturn(dependentTask);

        // When
        taskBean.addTask("testSessionId", updateTaskDto);

        // Then
        verify(taskDao, times(1)).persist(any(TaskEntity.class));
        verify(userTaskDao, times(3)).persist(any(UserTaskEntity.class)); // Expecting 3 persist calls
        verify(notificationBean, times(1)).createTaskAssignedNotification(anyString(), anyString(), anyString(), any(UserEntity.class));
    }

    @Test
    public void testDeleteTask_Success() throws Exception {
        // Given
        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setId(1);
        updateTaskDto.setSystemProjectName("testProject");
        updateTaskDto.setTitle("Task to be deleted");

        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("testProject");
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("testUser");
        TaskEntity task = new TaskEntity();

        when(taskDao.findById(anyInt())).thenReturn(task);
        when(projectDao.findBySystemName(anyString())).thenReturn(project);
        when(userBean.getUserBySessionId(anyString())).thenReturn(user);
        when(userProjectDao.isUserInProject(anyInt(), anyInt())).thenReturn(true);

        // When
        taskBean.deleteTask("testSessionId", updateTaskDto);

        // Then
        assertTrue(task.isDeleted());
        verify(taskDao, times(1)).merge(any(TaskEntity.class));
        verify(logBean, times(1)).createTasksDeletedLog(any(ProjectEntity.class), any(UserEntity.class), anyString());
    }
    @Test
    public void testUpdateTask_ProjectNotFound() throws Exception {
        // Given
        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setId(1);
        updateTaskDto.setTitle("Updated Task");
        updateTaskDto.setSystemProjectName("nonexistentProject");
        updateTaskDto.setDescription("Updated Description");
        updateTaskDto.setStatus(TaskStatusENUM.IN_PROGRESS);
        updateTaskDto.setInitialDate(LocalDateTime.now());
        updateTaskDto.setFinalDate(LocalDateTime.now().plusDays(10));
        updateTaskDto.setInChargeId(2);
        updateTaskDto.setMemberIds(Collections.singletonList(3));
        updateTaskDto.setDependentTaskIds(Collections.singletonList(1));

        when(projectDao.findBySystemName(anyString())).thenReturn(null);

        // When & Then
        assertThrows(ProjectNotFoundException.class, () -> {
            taskBean.updateTask("testSessionId", updateTaskDto);
        });
    }
    @Test
    public void testUpdateTask_TaskNotFound() throws Exception {
        // Given
        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setId(1);
        updateTaskDto.setTitle("Updated Task");
        updateTaskDto.setSystemProjectName("testProject");
        updateTaskDto.setDescription("Updated Description");
        updateTaskDto.setStatus(TaskStatusENUM.IN_PROGRESS);
        updateTaskDto.setInitialDate(LocalDateTime.now());
        updateTaskDto.setFinalDate(LocalDateTime.now().plusDays(10));
        updateTaskDto.setInChargeId(2);
        updateTaskDto.setMemberIds(Collections.singletonList(3));
        updateTaskDto.setDependentTaskIds(Collections.singletonList(1));

        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName("testProject");

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setSystemUsername("testUser");

        when(projectDao.findBySystemName(anyString())).thenReturn(project);
        when(userBean.getUserBySessionId(anyString())).thenReturn(user);
        when(taskDao.findById(anyInt())).thenReturn(null);

        // When & Then
        assertThrows(ProjectNotFoundException.class, () -> {
            taskBean.updateTask("testSessionId", updateTaskDto);
        });
    }
}