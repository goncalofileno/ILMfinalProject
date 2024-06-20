package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.LogTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import com.ilm.projecto_ilm_backend.dao.LogDao;
import com.ilm.projecto_ilm_backend.dao.ProjectDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dao.UserProjectDao;
import com.ilm.projecto_ilm_backend.dto.logs.LogDto;
import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.entity.LogEntity;
import com.ilm.projecto_ilm_backend.security.exceptions.ProjectNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotInProjectException;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Startup
public class LogBean {

    @Inject
    ProjectDao projectDao;

    @Inject
    UserDao userDao;

    @Inject
    LogDao logDao;

    @Inject
    UserBean userBean;

    @Inject
    ProjectBean projectBean;

    @Inject
    UserProjectDao userProjectDao;

    @Transactional
    public void createDefaultLogsIfNotExistent() {
        ProjectEntity project = projectDao.findById(1);
        UserEntity author = userDao.findById(1);

        if (!logDao.existsByType(LogTypeENUM.MEMBER_ADDED)) {
            createMemberAddedLog(project, author, "Default Receiver");
        }
        if (!logDao.existsByType(LogTypeENUM.MEMBER_REMOVED)) {
            createMemberRemovedLog(project, author, "Default Receiver");
        }
        if (!logDao.existsByType(LogTypeENUM.TASKS_CREATED)) {
            createTasksCreatedLog(project, author, "Default Task");
        }
        if (!logDao.existsByType(LogTypeENUM.TASKS_COMPLETED)) {
            createTasksCompletedLog(project, author, "Default Task", TaskStatusENUM.PLANNED, TaskStatusENUM.DONE);
        }
        if (!logDao.existsByType(LogTypeENUM.TASKS_IN_PROGRESS)) {
            createTasksInProgressLog(project, author, "Default Task", TaskStatusENUM.PLANNED, TaskStatusENUM.IN_PROGRESS);
        }
        if (!logDao.existsByType(LogTypeENUM.TASKS_DELETED)) {
            createTasksDeletedLog(project, author, "Default Task");
        }
        if (!logDao.existsByType(LogTypeENUM.TASKS_UPDATED)) {
            createTasksUpdatedLog(project, author, "Default Task");
        }
        if (!logDao.existsByType(LogTypeENUM.PROJECT_INFO_UPDATED)) {
            createProjectInfoUpdatedLog(project, author);
        }
        if (!logDao.existsByType(LogTypeENUM.PROJECT_STATUS_UPDATED)) {
            createProjectStatusUpdatedLog(project, author, StateProjectENUM.PLANNING, StateProjectENUM.APPROVED);
        }
        if (!logDao.existsByType(LogTypeENUM.RESOURCES_ADDED)) {
            createResourcesAddedLog(project, author, "Default Resource", 100);
        }
    }

    private void createMemberAddedLog(ProjectEntity project, UserEntity author, String receiver) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.MEMBER_ADDED);
        log.setAuthor(author);
        log.setProject(project);
        log.setReceiver(receiver);
        logDao.persist(log);
    }

    private void createMemberRemovedLog(ProjectEntity project, UserEntity author, String receiver) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.MEMBER_REMOVED);
        log.setAuthor(author);
        log.setProject(project);
        log.setReceiver(receiver);
        logDao.persist(log);
    }

    private void createTasksCreatedLog(ProjectEntity project, UserEntity author, String taskTitle) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.TASKS_CREATED);
        log.setAuthor(author);
        log.setProject(project);
        log.setTaskTitle(taskTitle);
        logDao.persist(log);
    }

    private void createTasksCompletedLog(ProjectEntity project, UserEntity author, String taskTitle, TaskStatusENUM oldStatus, TaskStatusENUM newStatus) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.TASKS_COMPLETED);
        log.setAuthor(author);
        log.setProject(project);
        log.setTaskTitle(taskTitle);
        log.setTaskOldStatus(oldStatus);
        log.setTaskNewStatus(newStatus);
        logDao.persist(log);
    }

    private void createTasksInProgressLog(ProjectEntity project, UserEntity author, String taskTitle, TaskStatusENUM oldStatus, TaskStatusENUM newStatus) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.TASKS_IN_PROGRESS);
        log.setAuthor(author);
        log.setProject(project);
        log.setTaskTitle(taskTitle);
        log.setTaskOldStatus(oldStatus);
        log.setTaskNewStatus(newStatus);
        logDao.persist(log);
    }

    private void createTasksDeletedLog(ProjectEntity project, UserEntity author, String taskTitle) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.TASKS_DELETED);
        log.setAuthor(author);
        log.setProject(project);
        log.setTaskTitle(taskTitle);
        logDao.persist(log);
    }

    private void createTasksUpdatedLog(ProjectEntity project, UserEntity author, String taskTitle) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.TASKS_UPDATED);
        log.setAuthor(author);
        log.setProject(project);
        log.setTaskTitle(taskTitle);
        logDao.persist(log);
    }

    private void createProjectInfoUpdatedLog(ProjectEntity project, UserEntity author) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.PROJECT_INFO_UPDATED);
        log.setAuthor(author);
        log.setProject(project);
        logDao.persist(log);
    }

    private void createProjectStatusUpdatedLog(ProjectEntity project, UserEntity author, StateProjectENUM oldState, StateProjectENUM newState) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.PROJECT_STATUS_UPDATED);
        log.setAuthor(author);
        log.setProject(project);
        log.setProjectOldState(oldState);
        log.setProjectNewState(newState);
        logDao.persist(log);
    }

    private void createResourcesAddedLog(ProjectEntity project, UserEntity author, String resourceName, int resourceStock) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.RESOURCES_ADDED);
        log.setAuthor(author);
        log.setProject(project);
        log.setResourceName(resourceName);
        log.setResourceStock(resourceStock);
        logDao.persist(log);
    }

    public List<LogDto> getLogsByProjectName(String sessionId, String systemProjectName) throws Exception {
        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        UserEntity user = userBean.getUserBySessionId(sessionId);

        if (project == null) {
            throw new ProjectNotFoundException("Project not found");
        }
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        if (!userProjectDao.isUserInProject(project.getId(), user.getId())) {
            throw new UserNotInProjectException("User not part of project");
        }

        List<LogEntity> logs = logDao.findByProject(project);
        List<LogDto> logDtos = new ArrayList<>();

        for (LogEntity log : logs) {
            logDtos.add(convertToDto(log));
        }
        return logDtos;
    }


    private LogDto convertToDto(LogEntity logEntity) {
        LogDto logDto = new LogDto();
        logDto.setId(logEntity.getId());
        logDto.setDate(logEntity.getDate());
        if(logEntity.getReceiver() != null) {
            logDto.setReceiver(logEntity.getReceiver());
        }
        if(logEntity.getType() != null) {
            logDto.setType(logEntity.getType());
        }
        if(logEntity.getTaskTitle() != null) {
            logDto.setTaskTitle(logEntity.getTaskTitle());
        }
        if(logEntity.getTaskOldStatus() != null) {
            logDto.setTaskOldStatus(logEntity.getTaskOldStatus());
        }
        if(logEntity.getTaskNewStatus() != null) {
            logDto.setTaskNewStatus(logEntity.getTaskNewStatus());
        }
        if(logEntity.getProjectOldState() != null) {
            logDto.setProjectOldState(logEntity.getProjectOldState());
        }
        if(logEntity.getProjectNewState() != null) {
            logDto.setProjectNewState(logEntity.getProjectNewState());
        }
        if(logEntity.getResourceName() != null) {
            logDto.setResourceName(logEntity.getResourceName());
        }
        if(logEntity.getResourceStock() != 0) {
            logDto.setResourceStock(logEntity.getResourceStock());
        }
        return logDto;
    }

}
