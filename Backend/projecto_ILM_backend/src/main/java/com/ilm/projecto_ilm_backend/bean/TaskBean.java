package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.project.ProjectMemberDto;
import com.ilm.projecto_ilm_backend.dto.task.MemberInTaskDto;
import com.ilm.projecto_ilm_backend.dto.task.TaskDto;
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
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Startup
public class TaskBean {

    @Inject
    private ProjectDao projectDao;

    @Inject
    private LabDao labDao;

    @Inject
    private UserDao userDao;

    @Inject
    private SkillDao skillDao;

    @Inject
    private TaskDao taskDao;

    @Inject
    private UserTaskDao userTaskDao;

    @Inject
    private UserBean userBean;

    @Inject
    private UserProjectDao userProjectDao;

    @Inject
    private ProjectBean projectBean;

    @Inject
    private NotificationBean notificationBean;

    @Inject LogBean logBean;

    private int numberOfProjectsToCreate = 20;

    @Transactional
    public void createDefaultTasksIfNotExistent() {
        for (int i = 1; i < numberOfProjectsToCreate + 1; i++) {

            TaskEntity task1 = new TaskEntity();
            task1.setTitle("Task 1");
            task1.setSystemTitle(taskSystemNameGenerator(task1.getTitle()));
            task1.setDescription("This task aims to develop the front-end of the system.");
            task1.setInitialDate(LocalDateTime.now().minus(3, ChronoUnit.MONTHS));
            task1.setFinalDate(LocalDateTime.now().plus(3, ChronoUnit.MONTHS));
            task1.setStatus(TaskStatusENUM.PLANNED);
            task1.setProject(projectDao.findById(i));
            task1.setDeleted(false);
            taskDao.persist(task1);

            UserTaskEntity userTask1 = new UserTaskEntity();
            userTask1.setTask(task1);
            userTask1.setUser(userDao.findById(1));
            userTask1.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
            userTaskDao.persist(userTask1);

            UserTaskEntity userTask2 = new UserTaskEntity();
            userTask2.setTask(task1);
            userTask2.setUser(userDao.findById(2));
            userTask2.setType(UserInTaskTypeENUM.MEMBER);
            userTaskDao.persist(userTask2);

            TaskEntity task2 = new TaskEntity();
            task2.setTitle("Task 2");
            task2.setSystemTitle(taskSystemNameGenerator(task2.getTitle()));
            task2.setDescription("This task aims to develop the back-end of the system.");
            task2.setInitialDate(LocalDateTime.now().plus(3, ChronoUnit.MONTHS));
            task2.setFinalDate(LocalDateTime.now().plus(6, ChronoUnit.MONTHS));
            task2.setStatus(TaskStatusENUM.IN_PROGRESS);
            task2.setProject(projectDao.findById(i));
            task2.setDeleted(false);
            List<TaskEntity> tasks = new ArrayList<>();
            tasks.add(task1);
            task2.setDependentTasks(tasks);
            taskDao.persist(task2);

            UserTaskEntity userTask3 = new UserTaskEntity();
            userTask3.setTask(task2);
            userTask3.setUser(userDao.findById(1));
            userTask3.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
            userTaskDao.persist(userTask3);

            UserTaskEntity userTask4 = new UserTaskEntity();
            userTask4.setTask(task2);
            userTask4.setUser(userDao.findById(2));
            userTask4.setType(UserInTaskTypeENUM.MEMBER);
            userTaskDao.persist(userTask4);

            TaskEntity task3 = new TaskEntity();
            task3.setTitle("Task 3");
            task3.setSystemTitle(taskSystemNameGenerator(task3.getTitle()));
            task3.setDescription("This task aims to develop the database of the system.");
            task3.setInitialDate(LocalDateTime.now().plus(6, ChronoUnit.MONTHS));
            task3.setFinalDate(LocalDateTime.now().plus(9, ChronoUnit.MONTHS));
            task3.setStatus(TaskStatusENUM.DONE);
            task3.setProject(projectDao.findById(i));
            task3.setDeleted(false);
            List<TaskEntity> tasks2 = new ArrayList<>();
            tasks2.add(task1);
            tasks2.add(task2);
            task3.setDependentTasks(tasks2);
            taskDao.persist(task3);

            UserTaskEntity userTask5 = new UserTaskEntity();
            userTask5.setTask(task3);
            userTask5.setUser(userDao.findById(1));
            userTask5.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
            userTaskDao.persist(userTask5);

            UserTaskEntity userTask6 = new UserTaskEntity();
            userTask6.setTask(task3);
            userTask6.setUser(userDao.findById(2));
            userTask6.setType(UserInTaskTypeENUM.MEMBER);
            userTaskDao.persist(userTask6);
        }
    }

    private String taskSystemNameGenerator(String originalName) {

        String systemTitle = originalName.replaceAll("\\s+", "").toLowerCase();

        if (taskDao.findTaskBySystemTitle(systemTitle) == null) {
            return systemTitle;
        } else {
            int i = 1;
            while (taskDao.findTaskBySystemTitle(systemTitle + i) != null) {
                i++;
            }
            return systemTitle + i;
        }
    }

    public List<TaskSuggestionDto> getTasksSuggestions(String sessionId, String systemProjectName) throws Exception {
        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        UserEntity user = userBean.getUserBySessionId(sessionId);

        if (project == null) {
            throw new ProjectNotFoundException("Project not found: " + systemProjectName);
        }

        if (user == null) {
            throw new UserNotFoundException("User not found for session id: " + sessionId);
        }

        if (!userProjectDao.isUserInProject(project.getId(), user.getId())) {
            throw new UserNotInProjectException("User not part of project");
        }

        List<TaskEntity> tasks = taskDao.findByProject(project.getId());
        List<TaskSuggestionDto> tasksSuggestions = new ArrayList<>();

        for (TaskEntity task : tasks) {
            tasksSuggestions.add(new TaskSuggestionDto(task.getTitle(), task.getSystemTitle()));
        }


        return tasksSuggestions;

    }

    public TasksPageDto getTasksPageDto(String sessionId, String systemProjectName) throws Exception {
        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        UserEntity user = userBean.getUserBySessionId(sessionId);

        if (project == null) {
            throw new ProjectNotFoundException("Project not found: " + systemProjectName);
        }

        if (user == null) {
            throw new UserNotFoundException("User not found for session id: " + sessionId);
        }

        if (!userProjectDao.isUserInProject(project.getId(), user.getId())) {
            throw new UserNotInProjectException("User not part of project");
        }

        String projectName = project.getName();

        TaskDto projectTask = new TaskDto();
        projectTask.setId(0);
        projectTask.setTitle(project.getName());
        projectTask.setSystemTitle(project.getSystemName());
        projectTask.setDescription(project.getDescription());
        projectTask.setProjectState(project.getStatus());
        projectTask.setInitialDate(project.getStartDate());
        projectTask.setFinalDate(project.getEndDate());

        List<TaskEntity> tasks = taskDao.findByProject(project.getId());
        List<TaskDto> tasksDto = new ArrayList<>();
        for (TaskEntity task : tasks) {
            TaskDto taskDto = new TaskDto();
            taskDto.setId(task.getId());
            taskDto.setTitle(task.getTitle());
            taskDto.setSystemTitle(task.getSystemTitle());
            taskDto.setDescription(task.getDescription());
            taskDto.setStatus(task.getStatus());
            taskDto.setInitialDate(task.getInitialDate());
            taskDto.setFinalDate(task.getFinalDate());
            taskDto.setOutColaboration(task.getOutColaboration());
            List<TaskDto> dependentTasks = new ArrayList<>();
            for (TaskEntity dependentTask : task.getDependentTasks()) {
                dependentTasks.add(taskToDto(dependentTask));
            }
            List<MemberInTaskDto> membersOfTask = new ArrayList<>();
            List<UserEntity> usersInTask = userTaskDao.findUsersByTaskId(task.getId());
            for (UserEntity userInTask : usersInTask) {
                membersOfTask.add(new MemberInTaskDto(userInTask.getId(), userInTask.getFullName(), userTaskDao.findUserTypeByTaskIdAndUserId(task.getId(), userInTask.getId()), userProjectDao.findUserTypeByUserIdAndProjectId(userInTask.getId(), project.getId()), userInTask.getSystemUsername(), userInTask.getPhoto()));
            }
            taskDto.setMembersOfTask(membersOfTask);
            taskDto.setDependentTasks(dependentTasks);
            tasksDto.add(taskDto);
        }

        UserInProjectTypeENUM userSeingTasksType = projectBean.getUserTypeInProject(user.getId(), project.getId());

        List<ProjectMemberDto> projectMembers = projectBean.getProjectMembers(project.getId());

        int projectProgress = projectBean.getProgress(project.getId());

        StateProjectENUM projectState = project.getStatus();

        TasksPageDto tasksPageDto = new TasksPageDto();
        tasksPageDto.setProjectName(projectName);
        tasksPageDto.setProjectTask(projectTask);
        tasksPageDto.setTasks(tasksDto);
        tasksPageDto.setUserSeingTasksType(userSeingTasksType);
        tasksPageDto.setProjectMembers(projectMembers);
        tasksPageDto.setProjectProgress(projectProgress);
        tasksPageDto.setProjectState(projectState);

        return tasksPageDto;
    }

    private TaskDto taskToDto(TaskEntity task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setSystemTitle(task.getSystemTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setStatus(task.getStatus());
        taskDto.setInitialDate(task.getInitialDate());
        taskDto.setFinalDate(task.getFinalDate());
        taskDto.setOutColaboration(task.getOutColaboration());
        return taskDto;
    }

    @Transactional
    public void addTask(String sessionId, UpdateTaskDto updateTaskDto) throws ProjectNotFoundException, UserNotFoundException, UserNotInProjectException {
        System.out.println("Received request to add new task: " + updateTaskDto.toString());

        UserEntity user = userBean.getUserBySessionId(sessionId);
        ProjectEntity project = projectDao.findBySystemName(updateTaskDto.getSystemProjectName());

        if (project == null) {
            throw new ProjectNotFoundException("Project not found");
        }

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        if (!userProjectDao.isUserInProject(project.getId(), user.getId())) {
            throw new UserNotInProjectException("User not part of project");
        }

        TaskEntity task = new TaskEntity();
        task.setTitle(updateTaskDto.getTitle());
        task.setSystemTitle(taskSystemNameGenerator(task.getTitle()));
        task.setDescription(updateTaskDto.getDescription());
        task.setStatus(TaskStatusENUM.PLANNED);
        task.setInitialDate(updateTaskDto.getInitialDate());
        task.setFinalDate(updateTaskDto.getFinalDate());
        task.setOutColaboration(updateTaskDto.getOutColaboration());
        task.setProject(project);
        task.setDeleted(false);

        List<TaskEntity> dependentTasks = updateTaskDto.getDependentTaskIds().stream()
                .map(taskId -> taskDao.findById(taskId))
                .collect(Collectors.toList());
        task.setDependentTasks(dependentTasks);

        taskDao.persist(task);

        UserTaskEntity userTask = new UserTaskEntity();
        userTask.setTask(task);
        userTask.setUser(user);
        userTask.setType(UserInTaskTypeENUM.CREATOR);
        userTaskDao.persist(userTask);

        UserEntity inCharge = userDao.findById(updateTaskDto.getInChargeId());
        if (inCharge != null) {
            if (inCharge.getId() != user.getId()) {
                UserTaskEntity userTaskInCharge = new UserTaskEntity();
                userTaskInCharge.setTask(task);
                userTaskInCharge.setUser(inCharge);
                userTaskInCharge.setType(UserInTaskTypeENUM.INCHARGE);
                userTaskDao.persist(userTaskInCharge);
            } else {
                userTask.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
                userTaskDao.merge(userTask);
            }
        }

        if (user.getId() != inCharge.getId()) {
            notificationBean.createTaskAssignedNotification(updateTaskDto.getTitle(), updateTaskDto.getSystemProjectName(), user.getSystemUsername(), inCharge);
        }

        List<UserEntity> membersOfTask = updateTaskDto.getMemberIds().stream()
                .map(memberId -> userDao.findById(memberId))
                .collect(Collectors.toList());

        for (UserEntity member : membersOfTask) {
            if (member.getId() != user.getId()) {
                if (member.getId() != inCharge.getId()) {
                    UserTaskEntity userTaskMember = new UserTaskEntity();
                    userTaskMember.setTask(task);
                    userTaskMember.setUser(member);
                    userTaskMember.setType(UserInTaskTypeENUM.MEMBER);
                    userTaskDao.persist(userTaskMember);
                }
            }
        }

        organizeTaskDates(project, task);

        taskDao.merge(task);

        logBean.createTasksCreatedLog(project, user, updateTaskDto.getTitle());
    }

    @Transactional
    public void updateTask(String sessionId, UpdateTaskDto updateTaskDto) throws ProjectNotFoundException, UserNotFoundException, UserNotInProjectException {
        TaskEntity task = taskDao.findById(updateTaskDto.getId());
        ProjectEntity project = projectDao.findBySystemName(updateTaskDto.getSystemProjectName());
        UserEntity user = userBean.getUserBySessionId(sessionId);
        if (task == null) {
            throw new ProjectNotFoundException("Task not found");
        }

        if(updateTaskDto.getStatus() == TaskStatusENUM.IN_PROGRESS && task.getStatus() == TaskStatusENUM.PLANNED){
            logBean.createTasksInProgressLog(project, user, updateTaskDto.getTitle(), task.getStatus(), updateTaskDto.getStatus());
        } else if (updateTaskDto.getStatus() == TaskStatusENUM.DONE && task.getStatus() == TaskStatusENUM.IN_PROGRESS){
            logBean.createTasksCompletedLog(project, user, updateTaskDto.getTitle(), task.getStatus(), updateTaskDto.getStatus());
        } else {
            logBean.createTasksUpdatedLog(project, user, updateTaskDto.getTitle());
        }

        task.setTitle(updateTaskDto.getTitle());
        task.setDescription(updateTaskDto.getDescription());
        task.setStatus(updateTaskDto.getStatus());

        if(updateTaskDto.getInitialDate().isBefore(project.getStartDate()) && updateTaskDto.getFinalDate().isBefore(project.getStartDate())){
            task.setInitialDate(project.getStartDate());
            task.setFinalDate(project.getStartDate().plusDays(task.getDurationDays()));
        }

        if(updateTaskDto.getInitialDate().isBefore(project.getStartDate())) {
            task.setInitialDate(project.getStartDate());
        } else {
            task.setInitialDate(updateTaskDto.getInitialDate());
        }

        if(updateTaskDto.getFinalDate().isAfter(project.getEndDate())) {
            project.setEndDate(updateTaskDto.getFinalDate());
            task.setFinalDate(updateTaskDto.getFinalDate());
        } else {
            task.setFinalDate(updateTaskDto.getFinalDate());
        }

        task.setOutColaboration(updateTaskDto.getOutColaboration());

        List<UserEntity> membersOfTask = updateTaskDto.getMemberIds().stream()
                .map(memberId -> userDao.findById(memberId))
                .collect(Collectors.toList());

        userTaskDao.deleteAllByTaskId(task.getId());

        for (UserEntity member : membersOfTask) {
            UserTaskEntity userTask = new UserTaskEntity();
            userTask.setTask(task);
            userTask.setUser(member);
            userTask.setType(UserInTaskTypeENUM.MEMBER);
            userTaskDao.persist(userTask);
        }

        UserEntity creator = userDao.findById(updateTaskDto.getCreatorId());
        if (creator != null) {
            UserTaskEntity userTask = userTaskDao.findByTaskIdAndUserId(task.getId(), creator.getId());
            userTask.setType(UserInTaskTypeENUM.CREATOR);
            userTaskDao.merge(userTask);
        }

        UserEntity inCharge = userDao.findById(updateTaskDto.getInChargeId());
        if (inCharge != null) {
            UserTaskEntity userTask = userTaskDao.findByTaskIdAndUserId(task.getId(), inCharge.getId());
            if (userTask.getType() == UserInTaskTypeENUM.CREATOR || userTask.getType() == UserInTaskTypeENUM.CREATOR_INCHARGE) {
                userTask.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
            } else {
                userTask.setType(UserInTaskTypeENUM.INCHARGE);
            }
        }

        if(user.getId() != inCharge.getId()) {
            notificationBean.createTaskAssignedNotification(updateTaskDto.getTitle(), updateTaskDto.getSystemProjectName(), user.getSystemUsername(), inCharge);
        }

        List<TaskEntity> dependentTasks = updateTaskDto.getDependentTaskIds().stream()
                .map(taskId -> taskDao.findById(taskId))
                .collect(Collectors.toList());
        task.setDependentTasks(dependentTasks);

        // Organizar as datas
        organizeTaskDates(task.getProject(), task);

        taskDao.merge(task);
    }

    private void organizeTaskDates(ProjectEntity project, TaskEntity task) {
        LocalDateTime projectStartDate = project.getStartDate();
        List<TaskEntity> tasks = taskDao.findByProject(project.getId());

        // Ajustar as datas das tarefas dependentes
        adjustChildTasks(task, projectStartDate, project);

        // Ajustar as datas das tarefas das quais esta tarefa depende
        adjustParentTasks(task, projectStartDate, project);

        // Atualizar a data final do projeto, se necessário
        LocalDateTime projectEndDate = tasks.stream()
                .map(TaskEntity::getFinalDate)
                .max(LocalDateTime::compareTo)
                .orElse(projectStartDate);

        project.setFinishedDate(projectEndDate);
        projectDao.merge(project);
    }

    private void adjustChildTasks(TaskEntity task, LocalDateTime projectStartDate, ProjectEntity project) {
        List<TaskEntity> childTasks = taskDao.findByDependentTask(task);

        for (TaskEntity childTask : childTasks) {
            if (task.getFinalDate().isAfter(childTask.getInitialDate().minusDays(1))) {
                long duration = java.time.Duration.between(childTask.getInitialDate(), childTask.getFinalDate()).toDays();
                childTask.setInitialDate(task.getFinalDate().plusDays(1));
                childTask.setFinalDate(childTask.getInitialDate().plusDays(duration));

                // Verificar se a nova data inicial não é antes da data inicial do projeto
                if (childTask.getInitialDate().isBefore(projectStartDate)) {
                    childTask.setInitialDate(projectStartDate);
                    childTask.setFinalDate(projectStartDate.plusDays(duration));
                }

                // Verificar se a nova data final ultrapassa a data final do projeto
                if (childTask.getFinalDate().isAfter(project.getFinishedDate())) {
                    project.setEndDate(childTask.getFinalDate());
                }

                taskDao.merge(childTask);

                adjustChildTasks(childTask, projectStartDate, project);
            }
        }
    }

    private void adjustParentTasks(TaskEntity task, LocalDateTime projectStartDate, ProjectEntity project) {
        List<TaskEntity> parentTasks = task.getDependentTasks();

        for (TaskEntity parentTask : parentTasks) {
            if (task.getInitialDate().isBefore(parentTask.getFinalDate().plusDays(1))) {
                long duration = java.time.Duration.between(parentTask.getInitialDate(), parentTask.getFinalDate()).toDays();
                parentTask.setFinalDate(task.getInitialDate().minusDays(1));
                parentTask.setInitialDate(parentTask.getFinalDate().minusDays(duration));

                // Verificar se a nova data inicial não é antes da data inicial do projeto
                if (parentTask.getInitialDate().isBefore(projectStartDate)) {
                    parentTask.setInitialDate(projectStartDate);
                    parentTask.setFinalDate(projectStartDate.plusDays(duration));
                }

                taskDao.merge(parentTask);

                adjustParentTasks(parentTask, projectStartDate, project);
            }
        }
    }



    public void deleteTask(String sessionId, UpdateTaskDto updateTaskDto) throws ProjectNotFoundException, UserNotFoundException, UserNotInProjectException {
        TaskEntity task = taskDao.findById(updateTaskDto.getId());
        UserEntity user = userBean.getUserBySessionId(sessionId);
        ProjectEntity project = projectDao.findBySystemName(updateTaskDto.getSystemProjectName());

        if (task == null) {
            throw new ProjectNotFoundException("Task not found");
        }

        if (project == null) {
            throw new ProjectNotFoundException("Project not found");
        }

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        if (!userProjectDao.isUserInProject(project.getId(), user.getId())) {
            throw new UserNotInProjectException("User not part of project");
        }

        task.setDeleted(true);

        logBean.createTasksDeletedLog(project, user, updateTaskDto.getTitle());

        List<TaskEntity> tasks = taskDao.findByProject(project.getId());

        for(TaskEntity taskWithDependent : tasks) {
            List<TaskEntity> dependentTasks = taskWithDependent.getDependentTasks();
            if(dependentTasks.contains(task)) {
                dependentTasks.remove(task);
                taskWithDependent.setDependentTasks(dependentTasks);
                taskDao.merge(taskWithDependent);
            }
        }

        taskDao.merge(task);
    }

    @Transactional
    public void removeUserFromProjectTasks(int userId, int projectId) {
        List<TaskEntity> tasks = taskDao.findByProject(projectId);
        UserEntity user = userDao.findById(userId);
        UserEntity creator = userProjectDao.findCreatorByProjectId(projectId);
        for (TaskEntity task : tasks) {
            UserTaskEntity userTask = userTaskDao.findByTaskIdAndUserId(task.getId(), userId);
            if (userTask != null) {
                if (userTask.getType() == UserInTaskTypeENUM.INCHARGE) {
                    if (userTask.getUser().getId() == user.getId()) {
                        userTaskDao.remove(userTask);
                        if (userTaskDao.findByTaskIdAndUserId(task.getId(), creator.getId()) == null) {
                            UserTaskEntity userTaskCreator = new UserTaskEntity();
                            userTaskCreator.setTask(task);
                            userTaskCreator.setUser(creator);
                            userTaskCreator.setType(UserInTaskTypeENUM.INCHARGE);
                            userTaskDao.persist(userTaskCreator);
                        } else {
                            UserTaskEntity userTaskCreator = userTaskDao.findByTaskIdAndUserId(task.getId(), creator.getId());
                            userTaskCreator.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
                            userTaskDao.merge(userTaskCreator);
                        }
                    } else {
                        userTaskDao.remove(userTask);
                    }
                } else if (userTask.getType() == UserInTaskTypeENUM.CREATOR_INCHARGE) {
                    if (userTask.getUser().getId() == user.getId()) {
                        userTaskDao.remove(userTask);
                        if (userTaskDao.findByTaskIdAndUserId(task.getId(), creator.getId()) == null) {
                            UserTaskEntity userTaskCreator = new UserTaskEntity();
                            userTaskCreator.setTask(task);
                            userTaskCreator.setUser(creator);
                            userTaskCreator.setType(UserInTaskTypeENUM.INCHARGE);
                            userTaskDao.persist(userTaskCreator);
                        } else {
                            UserTaskEntity userTaskCreator = userTaskDao.findByTaskIdAndUserId(task.getId(), creator.getId());
                            userTaskCreator.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
                            userTaskDao.merge(userTaskCreator);
                        }
                    } else {
                        userTaskDao.remove(userTask);
                    }
                } else if (userTask.getType() == UserInTaskTypeENUM.CREATOR) {
                    if (userTask.getUser().getId() == user.getId()) {
                        userTaskDao.remove(userTask);
                        if (userTaskDao.findByTaskIdAndUserId(task.getId(), creator.getId()) == null) {
                            UserTaskEntity userTaskCreator = new UserTaskEntity();
                            userTaskCreator.setTask(task);
                            userTaskCreator.setUser(creator);
                            userTaskCreator.setType(UserInTaskTypeENUM.INCHARGE);
                            userTaskDao.persist(userTaskCreator);
                        } else {
                            UserTaskEntity userTaskCreator = userTaskDao.findByTaskIdAndUserId(task.getId(), creator.getId());
                            userTaskCreator.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
                            userTaskDao.merge(userTaskCreator);
                        }
                    } else {
                        userTaskDao.remove(userTask);
                    }
                } else if (userTask.getType() == UserInTaskTypeENUM.MEMBER) {
                    userTaskDao.remove(userTask);
                }
            }
        }
    }



}
