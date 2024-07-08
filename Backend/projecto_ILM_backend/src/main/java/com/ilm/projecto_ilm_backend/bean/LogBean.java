package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.LogTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.logs.LogDto;
import com.ilm.projecto_ilm_backend.dto.logs.LogsAndNotesPageDto;
import com.ilm.projecto_ilm_backend.dto.notes.NoteDto;
import com.ilm.projecto_ilm_backend.entity.NoteEntity;
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

/**
 * Bean responsável por gerenciar logs e notas de projetos.
 */
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
    NoteDao noteDao;

    @Inject
    UserBean userBean;

    @Inject
    ProjectBean projectBean;

    @Inject
    UserProjectDao userProjectDao;

    @Inject
    NoteBean noteBean;

    /**
     * Cria logs padrão se não existirem.
     */
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
        if (!logDao.existsByType(LogTypeENUM.RESOURCES_UPDATED)) {
            createResourcesAddedLog(project, author);
        }
    }

    /**
     * Cria um log de membro adicionado.
     *
     * @param project   O projeto ao qual o log pertence.
     * @param author    O autor do log.
     * @param receiver  O nome do membro adicionado.
     */
    public void createMemberAddedLog(ProjectEntity project, UserEntity author, String receiver) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.MEMBER_ADDED);
        log.setAuthor(author);
        log.setProject(project);
        log.setReceiver(receiver);
        logDao.persist(log);
    }

    /**
     * Cria um log de membro removido.
     *
     * @param project   O projeto ao qual o log pertence.
     * @param author    O autor do log.
     * @param receiver  O nome do membro removido.
     */
    public void createMemberRemovedLog(ProjectEntity project, UserEntity author, String receiver) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.MEMBER_REMOVED);
        log.setAuthor(author);
        log.setProject(project);
        log.setReceiver(receiver);
        logDao.persist(log);
    }

    /**
     * Cria um log de tarefa criada.
     *
     * @param project   O projeto ao qual o log pertence.
     * @param author    O autor do log.
     * @param taskTitle O título da tarefa criada.
     */
    public void createTasksCreatedLog(ProjectEntity project, UserEntity author, String taskTitle) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.TASKS_CREATED);
        log.setAuthor(author);
        log.setProject(project);
        log.setTaskTitle(taskTitle);
        logDao.persist(log);
    }

    /**
     * Cria um log de tarefa concluída.
     *
     * @param project   O projeto ao qual o log pertence.
     * @param author    O autor do log.
     * @param taskTitle O título da tarefa concluída.
     * @param oldStatus O status antigo da tarefa.
     * @param newStatus O novo status da tarefa.
     */
    public void createTasksCompletedLog(ProjectEntity project, UserEntity author, String taskTitle, TaskStatusENUM oldStatus, TaskStatusENUM newStatus) {
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

    /**
     * Cria um log de tarefa em progresso.
     *
     * @param project   O projeto ao qual o log pertence.
     * @param author    O autor do log.
     * @param taskTitle O título da tarefa.
     * @param oldStatus O status antigo da tarefa.
     * @param newStatus O novo status da tarefa.
     */
    public void createTasksInProgressLog(ProjectEntity project, UserEntity author, String taskTitle, TaskStatusENUM oldStatus, TaskStatusENUM newStatus) {
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

    /**
     * Cria um log de tarefa deletada.
     *
     * @param project   O projeto ao qual o log pertence.
     * @param author    O autor do log.
     * @param taskTitle O título da tarefa deletada.
     */
    public void createTasksDeletedLog(ProjectEntity project, UserEntity author, String taskTitle) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.TASKS_DELETED);
        log.setAuthor(author);
        log.setProject(project);
        log.setTaskTitle(taskTitle);
        logDao.persist(log);
    }

    /**
     * Cria um log de tarefa atualizada.
     *
     * @param project   O projeto ao qual o log pertence.
     * @param author    O autor do log.
     * @param taskTitle O título da tarefa atualizada.
     */
    public void createTasksUpdatedLog(ProjectEntity project, UserEntity author, String taskTitle) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.TASKS_UPDATED);
        log.setAuthor(author);
        log.setProject(project);
        log.setTaskTitle(taskTitle);
        logDao.persist(log);
    }

    /**
     * Cria um log de informações do projeto atualizadas.
     *
     * @param project O projeto ao qual o log pertence.
     * @param author  O autor do log.
     */
    public void createProjectInfoUpdatedLog(ProjectEntity project, UserEntity author) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.PROJECT_INFO_UPDATED);
        log.setAuthor(author);
        log.setProject(project);
        logDao.persist(log);
    }

    /**
     * Cria um log de status do projeto atualizado.
     *
     * @param project   O projeto ao qual o log pertence.
     * @param author    O autor do log.
     * @param oldState  O estado antigo do projeto.
     * @param newState  O novo estado do projeto.
     */
    public void createProjectStatusUpdatedLog(ProjectEntity project, UserEntity author, StateProjectENUM oldState, StateProjectENUM newState) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.PROJECT_STATUS_UPDATED);
        log.setAuthor(author);
        log.setProject(project);
        log.setProjectOldState(oldState);
        log.setProjectNewState(newState);
        logDao.persist(log);
    }

    /**
     * Cria um log de recursos atualizados.
     *
     * @param project O projeto ao qual o log pertence.
     * @param author  O autor do log.
     */
    public void createResourcesAddedLog(ProjectEntity project, UserEntity author) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.RESOURCES_UPDATED);
        log.setAuthor(author);
        log.setProject(project);
        logDao.persist(log);
    }

    /**
     * Cria um log de mudança de tipo de membro.
     *
     * @param project   O projeto ao qual o log pertence.
     * @param author    O autor do log.
     * @param receiver  O nome do membro.
     * @param oldType   O tipo antigo do membro.
     * @param newType   O novo tipo do membro.
     */
    public void createMemberTypeChangedLog(ProjectEntity project, UserEntity author, String receiver, UserInProjectTypeENUM oldType, UserInProjectTypeENUM newType) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.MEMBER_TYPE_CHANGED);
        log.setAuthor(author);
        log.setProject(project);
        log.setReceiver(receiver);
        log.setOldUserType(oldType);
        log.setNewUserType(newType);
        logDao.persist(log);
    }

    /**
     * Cria um log de membro que deixou o projeto.
     *
     * @param project O projeto ao qual o log pertence.
     * @param leaver  O membro que deixou o projeto.
     */
    public void createMemberLeftLog(ProjectEntity project, UserEntity leaver) {
        LogEntity log = new LogEntity();
        log.setDate(LocalDateTime.now());
        log.setType(LogTypeENUM.MEMBER_LEFT);
        log.setAuthor(leaver);
        log.setProject(project);
        logDao.persist(log);
    }

    /**
     * Obtém logs de um projeto pelo nome do projeto.
     *
     * @param sessionId           O ID da sessão do usuário.
     * @param systemProjectName   O nome do projeto.
     * @return Lista de DTOs de logs.
     * @throws Exception Caso ocorra algum erro ao obter os logs.
     */
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

    /**
     * Converte uma entidade LogEntity para um DTO LogDto.
     *
     * @param logEntity A entidade LogEntity a ser convertida.
     * @return O DTO LogDto correspondente.
     */
    private LogDto convertToDto(LogEntity logEntity) {
        LogDto logDto = new LogDto();
        logDto.setId(logEntity.getId());
        logDto.setDate(logEntity.getDate());
        if (logEntity.getReceiver() != null) {
            logDto.setReceiver(logEntity.getReceiver());
        }
        if (logEntity.getType() != null) {
            logDto.setType(logEntity.getType());
        }
        if (logEntity.getTaskTitle() != null) {
            logDto.setTaskTitle(logEntity.getTaskTitle());
        }
        if (logEntity.getTaskOldStatus() != null) {
            logDto.setTaskOldStatus(logEntity.getTaskOldStatus());
        }
        if (logEntity.getTaskNewStatus() != null) {
            logDto.setTaskNewStatus(logEntity.getTaskNewStatus());
        }
        if (logEntity.getProjectOldState() != null) {
            logDto.setProjectOldState(logEntity.getProjectOldState());
        }
        if (logEntity.getProjectNewState() != null) {
            logDto.setProjectNewState(logEntity.getProjectNewState());
        }
        if (logEntity.getResourceName() != null) {
            logDto.setResourceName(logEntity.getResourceName());
        }
        if (logEntity.getResourceStock() != 0) {
            logDto.setResourceStock(logEntity.getResourceStock());
        }
        if (logEntity.getAuthor() != null) {
            logDto.setAuthorName(logEntity.getAuthor().getFirstName() + " " + logEntity.getAuthor().getLastName());
            logDto.setAuthorPhoto(logEntity.getAuthor().getPhoto());
        }
        if (logEntity.getOldUserType() != null) {
            logDto.setMemberOldType(logEntity.getOldUserType().name());
            logDto.setMemberNewType(logEntity.getNewUserType().name());
        }

        return logDto;
    }

    /**
     * Obtém logs e notas de um projeto pelo nome do projeto.
     *
     * @param sessionId           O ID da sessão do usuário.
     * @param systemProjectName   O nome do projeto.
     * @return DTO contendo logs e notas do projeto.
     * @throws Exception Caso ocorra algum erro ao obter os logs e notas.
     */
    public LogsAndNotesPageDto getLogsAndNotesByProjectName(String sessionId, String systemProjectName) throws Exception {
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

        List<NoteEntity> notes = noteDao.findAllByProject(project);
        List<NoteDto> noteDtos = new ArrayList<>();

        for (NoteEntity note : notes) {
            noteDtos.add(noteBean.convertToDto(note));
        }

        UserInProjectTypeENUM userSeingTheProject = userProjectDao.findUserTypeByUserIdAndProjectId(user.getId(), project.getId());

        LogsAndNotesPageDto logsAndNotesPageDto = new LogsAndNotesPageDto();
        logsAndNotesPageDto.setLogs(logDtos);
        logsAndNotesPageDto.setNotes(noteDtos);
        logsAndNotesPageDto.setTypeOfUserSeingPage(userSeingTheProject);
        logsAndNotesPageDto.setProjectName(project.getName());
        logsAndNotesPageDto.setProjectState(project.getStatus());

        return logsAndNotesPageDto;
    }
}
