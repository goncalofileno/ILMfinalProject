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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Singleton
@Startup
/**
 * The TaskBean class is responsible for managing tasks within the project.
 * It handles operations such as creating default tasks, generating task suggestions,
 * retrieving task details for pages, adding new tasks, and updating existing tasks.
 */
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

    @Inject
    NotificationDao notificationDao;

    @Inject
    LogBean logBean;

    private int numberOfProjectsToCreate = 20;

    /**
     * Automatically generates a predefined set of tasks for each project if they do not already exist.
     * This method is intended to be run at application startup to ensure that there are default tasks
     * available for demonstration or initial use.
     */
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
            userTask1.setUser(userDao.findById(2));
            userTask1.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
            userTaskDao.persist(userTask1);

            UserTaskEntity userTask2 = new UserTaskEntity();
            userTask2.setTask(task1);
            userTask2.setUser(userDao.findById(3));
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
            userTask3.setUser(userDao.findById(3));
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
            userTask5.setUser(userDao.findById(2));
            userTask5.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
            userTaskDao.persist(userTask5);

            UserTaskEntity userTask6 = new UserTaskEntity();
            userTask6.setTask(task3);
            userTask6.setUser(userDao.findById(3));
            userTask6.setType(UserInTaskTypeENUM.MEMBER);
            userTaskDao.persist(userTask6);
        }
    }

    /**
     * Generates a system-friendly name for a task by removing spaces and converting to lowercase.
     * If the generated name already exists, it appends a number to ensure uniqueness.
     *
     * @param originalName The original name of the task.
     * @return A unique system-friendly name for the task.
     */
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

    /**
     * Provides a list of task suggestions for a given project and user session.
     * This can be used to suggest tasks to users based on their current project context.
     *
     * @param sessionId The session ID of the user requesting suggestions.
     * @param systemProjectName The system name of the project for which suggestions are being requested.
     * @return A list of TaskSuggestionDto objects representing the suggested tasks.
     * @throws Exception if the project or user cannot be found, or if the user is not part of the project.
     */
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

    /**
     * Retrieves a detailed view of tasks for a given project, formatted for display on a page.
     * This includes tasks details, project members, and the current user's role within the project.
     *
     * @param sessionId The session ID of the user.
     * @param systemProjectName The system name of the project for which the task page is being requested.
     * @return A TasksPageDto object containing detailed information about the project's tasks.
     * @throws Exception if the project or user cannot be found, or if the user is not part of the project.
     */
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

    /**
     * Converts a TaskEntity object into a TaskDto object.
     * This method is used internally to prepare task data for transfer or display.
     *
     * @param task The TaskEntity object to be converted.
     * @return A TaskDto object containing the task's details.
     */
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

    /**
     * Adds a new task to the system based on provided details.
     * This method handles task creation, including setting up relationships with users and projects.
     *
     * @param sessionId The session ID of the user creating the task.
     * @param updateTaskDto An UpdateTaskDto object containing the details of the task to be added.
     * @throws ProjectNotFoundException if the specified project cannot be found.
     * @throws UserNotFoundException if the specified user cannot be found.
     * @throws UserNotInProjectException if the user is not part of the specified project.
     */
    @Transactional
    public void addTask(String sessionId, UpdateTaskDto updateTaskDto) throws ProjectNotFoundException, UserNotFoundException, UserNotInProjectException {

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

    /**
     * Updates an existing task with new details.
     * This method allows for modifying various aspects of a task, including its status, description, and associated users.
     *
     * @param sessionId The session ID of the user updating the task.
     * @param updateTaskDto An UpdateTaskDto object containing the updated details of the task.
     * @throws ProjectNotFoundException if the specified project or task cannot be found.
     * @throws UserNotFoundException if the specified user cannot be found.
     * @throws UserNotInProjectException if the user is not part of the specified project.
     */
    @Transactional
    public void updateTask(String sessionId, UpdateTaskDto updateTaskDto) throws ProjectNotFoundException, UserNotFoundException, UserNotInProjectException {
        TaskEntity task = taskDao.findById(updateTaskDto.getId());
        ProjectEntity project = projectDao.findBySystemName(updateTaskDto.getSystemProjectName());
        UserEntity user = userBean.getUserBySessionId(sessionId);
        if (task == null) {
            throw new ProjectNotFoundException("Task not found");
        }

        if (updateTaskDto.getStatus() == TaskStatusENUM.IN_PROGRESS && task.getStatus() == TaskStatusENUM.PLANNED) {
            logBean.createTasksInProgressLog(project, user, updateTaskDto.getTitle(), task.getStatus(), updateTaskDto.getStatus());
        } else if (updateTaskDto.getStatus() == TaskStatusENUM.DONE && task.getStatus() == TaskStatusENUM.IN_PROGRESS) {
            logBean.createTasksCompletedLog(project, user, updateTaskDto.getTitle(), task.getStatus(), updateTaskDto.getStatus());
        } else {
            logBean.createTasksUpdatedLog(project, user, updateTaskDto.getTitle());
        }

        task.setTitle(updateTaskDto.getTitle());
        task.setDescription(updateTaskDto.getDescription());
        task.setStatus(updateTaskDto.getStatus());

        if (updateTaskDto.getInitialDate().isBefore(project.getStartDate()) && updateTaskDto.getFinalDate().isBefore(project.getStartDate())) {
            task.setInitialDate(project.getStartDate());
            task.setFinalDate(project.getStartDate().plusDays(task.getDurationDays()));
        }

        if (updateTaskDto.getInitialDate().isBefore(project.getStartDate())) {
            task.setInitialDate(project.getStartDate());
        } else {
            task.setInitialDate(updateTaskDto.getInitialDate());
        }

        if (updateTaskDto.getFinalDate().isAfter(project.getEndDate())) {
            project.setEndDate(updateTaskDto.getFinalDate());
            task.setFinalDate(updateTaskDto.getFinalDate());
        } else {
            task.setFinalDate(updateTaskDto.getFinalDate());
        }

        task.setOutColaboration(updateTaskDto.getOutColaboration());

        UserEntity inCharge = userDao.findById(updateTaskDto.getInChargeId());
        UserEntity userInCharge = userTaskDao.findInChargeByTaskId(task.getId());

        if (updateTaskDto.getInChargeId() != userInCharge.getId()) {
            notificationBean.createTaskAssignedNotification(updateTaskDto.getTitle(), updateTaskDto.getSystemProjectName(), user.getSystemUsername(), inCharge);
        }

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

        if (inCharge != null) {
            UserTaskEntity userTask = userTaskDao.findByTaskIdAndUserId(task.getId(), inCharge.getId());
            if (userTask.getType() == UserInTaskTypeENUM.CREATOR || userTask.getType() == UserInTaskTypeENUM.CREATOR_INCHARGE) {
                userTask.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
            } else {
                userTask.setType(UserInTaskTypeENUM.INCHARGE);
            }
        }

        List<TaskEntity> dependentTasks = updateTaskDto.getDependentTaskIds().stream()
                .map(taskId -> taskDao.findById(taskId))
                .collect(Collectors.toList());
        task.setDependentTasks(dependentTasks);

        // Organizar as datas
        if (!organizeTaskDates(task.getProject(), task)) {
            throw new IllegalStateException("Task update failed due to overlapping dates with tasks already at the project start date.");
        }

        taskDao.merge(task);

        List<UserEntity> teamMembers = userTaskDao.findUsersByTaskId(task.getId());
        for (UserEntity teamMember : teamMembers) {
            if (teamMember.getId() != user.getId()) {
                if (teamMember.getId() != user.getId()) {
                    notificationBean.createTaskNotification(task.getTitle(), project.getSystemName(), user.getSystemUsername(), teamMember);
                }
            }
        }
    }

    /**
     * Checks for duplicate notifications for a given task, project, and user within a short timeframe.
     * This method is used to prevent spamming users with multiple notifications of the same type in quick succession.
     *
     * @param taskTitle The title of the task for which the notification is being checked.
     * @param projectSystemName The system name of the project related to the task.
     * @param systemUsername The username of the user in the system.
     * @param user The {@link UserEntity} object representing the user to check notifications for.
     * @return true if a duplicate notification exists, false otherwise.
     */
    public boolean checkDoubleNotification(String taskTitle, String projectSystemName, String systemUsername, UserEntity user) {
        if (notificationDao.findDoubleNotificationTask(taskTitle, projectSystemName, systemUsername, user, LocalDateTime.now())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Organizes and adjusts the dates of tasks within a project to ensure logical progression and dependencies.
     * This includes adjusting the start and end dates of tasks based on their dependencies to prevent overlaps
     * and ensure that tasks follow a logical order. It also sets the project's end date based on the final task.
     *
     * @param project The {@link ProjectEntity} object representing the project whose tasks are being organized.
     * @param task The {@link TaskEntity} object representing the task that triggered the organization process.
     *              This can be null if the organization is triggered by a project-level event.
     * @return true if the organization was successful, false if it was not possible to adjust dates without overlap.
     */
    private boolean organizeTaskDates(ProjectEntity project, TaskEntity task) {
        LocalDateTime projectStartDate = project.getStartDate();
        List<TaskEntity> tasks = taskDao.findByProject(project.getId());

        // Ajustar datas de todas as tarefas como antes
        if (task != null) {
            if (!adjustParentTasks(task, projectStartDate, project) || !adjustChildTasks(task, projectStartDate, project)) {
                return false;
            }
        }

        // Encontrar a tarefa de apresentação final
        TaskEntity finalPresentationTask = getFinalPresentationTask(project);
        if (finalPresentationTask != null) {
            // Identificar a penúltima tarefa
            TaskEntity penultimateTask = tasks.stream()
                    .filter(t -> !t.getSystemTitle().equals(finalPresentationTask.getSystemTitle()))
                    .max(Comparator.comparing(TaskEntity::getFinalDate))
                    .orElse(null);

            if (penultimateTask != null) {
                // Configurar a tarefa de apresentação final para ser dependente da penúltima tarefa
                finalPresentationTask.setInitialDate(penultimateTask.getFinalDate().plusDays(1));
                finalPresentationTask.setFinalDate(finalPresentationTask.getInitialDate().plusDays(1)); // Supondo duração de 1 dia

                List<TaskEntity> dependencies = new ArrayList<>();
                dependencies.add(penultimateTask);
                finalPresentationTask.setDependentTasks(dependencies);

                taskDao.merge(finalPresentationTask);
            }

            // Atualizar a data final do projeto para a data final da apresentação final
            project.setEndDate(finalPresentationTask.getFinalDate());
        } else {
            // Se nenhuma tarefa de apresentação final for encontrada, seguir a lógica original
            LocalDateTime projectEndDate = tasks.stream()
                    .map(TaskEntity::getFinalDate)
                    .max(LocalDateTime::compareTo)
                    .orElse(projectStartDate);

            project.setEndDate(projectEndDate);
        }
        projectDao.merge(project);
        return true;
    }

    /**
     * Adjusts the start and end dates of parent tasks based on the start date of a given task.
     * This method ensures that parent tasks do not overlap with their child tasks by adjusting
     * their dates backwards if necessary.
     *
     * @param task The {@link TaskEntity} object representing the child task that may cause date adjustments.
     * @param projectStartDate The start date of the project, used to ensure tasks do not start before the project.
     * @param project The {@link ProjectEntity} object representing the project to which the tasks belong.
     * @return true if the adjustment was successful, false if it was not possible to adjust dates without overlap.
     */
    private boolean adjustParentTasks(TaskEntity task, LocalDateTime projectStartDate, ProjectEntity project) {
        List<TaskEntity> parentTasks = task.getDependentTasks();

        for (TaskEntity parentTask : parentTasks) {
            if (parentTask != null && task.getInitialDate() != null && task.getInitialDate().isBefore(parentTask.getFinalDate().plusDays(1))) {
                long duration = java.time.Duration.between(parentTask.getInitialDate(), parentTask.getFinalDate()).toDays();
                LocalDateTime newFinalDate = task.getInitialDate().minusDays(1);
                LocalDateTime newInitialDate = newFinalDate.minusDays(duration);

                // Verificar se a nova data inicial não é antes da data inicial do projeto
                if (newInitialDate.isBefore(projectStartDate)) {
                    // Verificar se a nova data inicial sobrepõe a data de alguma task que já está encostada ao início do projeto
                    List<TaskEntity> tasksAtStart = taskDao.findTasksAtProjectStart(project.getId(), projectStartDate);
                    boolean overlaps = false;
                    for (TaskEntity t : tasksAtStart) {
                        if (!t.equals(parentTask) && newInitialDate.isBefore(t.getFinalDate().plusDays(1))) {
                            overlaps = true;
                            break;
                        }
                    }

                    if (overlaps) {
                        // Ajustar a tarefa atual para começar após o término da tarefa encostada
                        TaskEntity earliestTask = tasksAtStart.stream()
                                .filter(t -> !t.equals(parentTask))
                                .min(Comparator.comparing(TaskEntity::getFinalDate))
                                .orElse(null);

                        if (earliestTask != null) {
                            newInitialDate = earliestTask.getFinalDate().plusDays(1);
                            newFinalDate = newInitialDate.plusDays(duration);
                        } else {
                            newInitialDate = projectStartDate;
                            newFinalDate = projectStartDate.plusDays(duration);
                        }
                    } else {
                        newInitialDate = projectStartDate;
                        newFinalDate = projectStartDate.plusDays(duration);
                    }
                }

                // Verificar novamente a sobreposição após o ajuste da data inicial
                List<TaskEntity> tasksAtStart = taskDao.findTasksAtProjectStart(project.getId(), projectStartDate);
                boolean overlaps = false;
                for (TaskEntity t : tasksAtStart) {
                    if (!t.equals(parentTask) && newInitialDate.isBefore(t.getFinalDate().plusDays(1))) {
                        overlaps = true;
                        break;
                    }
                }

                if (overlaps) {
                    // Ajustar a tarefa atual para começar após o término da tarefa encostada
                    TaskEntity earliestTask = tasksAtStart.stream()
                            .filter(t -> !t.equals(parentTask))
                            .min(Comparator.comparing(TaskEntity::getFinalDate))
                            .orElse(null);

                    if (earliestTask != null) {
                        newInitialDate = earliestTask.getFinalDate().plusDays(1);
                        newFinalDate = newInitialDate.plusDays(duration);
                    } else {
                        return false; // Não é possível ajustar sem sobreposição
                    }
                }

                parentTask.setFinalDate(newFinalDate);
                parentTask.setInitialDate(newInitialDate);

                taskDao.merge(parentTask);

                if (!adjustParentTasks(parentTask, projectStartDate, project)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Adjusts the start and end dates of child tasks based on the end date of a given task.
     * This method ensures that child tasks do not start before their parent tasks by adjusting
     * their dates forward if necessary.
     *
     * @param task The {@link TaskEntity} object representing the parent task that may cause date adjustments.
     * @param projectStartDate The start date of the project, used to ensure tasks do not start before the project.
     * @param project The {@link ProjectEntity} object representing the project to which the tasks belong.
     * @return true if the adjustment was successful, false if it was not possible to adjust dates without overlap.
     */
    private boolean adjustChildTasks(TaskEntity task, LocalDateTime projectStartDate, ProjectEntity project) {
        List<TaskEntity> childTasks = taskDao.findByDependentTask(task);

        for (TaskEntity childTask : childTasks) {
            if (childTask != null && task.getFinalDate() != null && task.getFinalDate().isAfter(childTask.getInitialDate().minusDays(1))) {
                long duration = java.time.Duration.between(childTask.getInitialDate(), childTask.getFinalDate()).toDays();
                LocalDateTime newInitialDate = task.getFinalDate().plusDays(1);
                LocalDateTime newFinalDate = newInitialDate.plusDays(duration);

                // Verificar se a nova data inicial não é antes da data inicial do projeto
                if (newInitialDate.isBefore(projectStartDate)) {
                    newInitialDate = projectStartDate;
                    newFinalDate = projectStartDate.plusDays(duration);
                }

                // Verificar se a nova data final ultrapassa a data final do projeto
                if (newFinalDate.isAfter(project.getEndDate())) {
                    project.setEndDate(newFinalDate);
                }

                childTask.setInitialDate(newInitialDate);
                childTask.setFinalDate(newFinalDate);

                taskDao.merge(childTask);

                if (!adjustChildTasks(childTask, projectStartDate, project)) {
                    return false;
                }
            }
        }
        return true;
    }



    /**
     * Marks a task as deleted and adjusts project and task dependencies accordingly.
     * This method is used to logically delete a task from the project, ensuring that
     * any dependent tasks are updated to remove the deleted task from their dependencies.
     *
     * @param sessionId The session ID of the user performing the deletion.
     * @param updateTaskDto An {@link UpdateTaskDto} object containing the details of the task to be deleted.
     * @throws ProjectNotFoundException if the specified project cannot be found.
     * @throws UserNotFoundException if the specified user cannot be found.
     * @throws UserNotInProjectException if the user is not part of the specified project.
     */
    @Transactional
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

        for (TaskEntity taskWithDependent : tasks) {
            List<TaskEntity> dependentTasks = taskWithDependent.getDependentTasks();
            if (dependentTasks != null && dependentTasks.contains(task)) {
                dependentTasks.remove(task);
                taskWithDependent.setDependentTasks(dependentTasks);
                taskDao.merge(taskWithDependent);
            }
        }

        taskDao.merge(task);

        organizeTaskDates(project, null);
    }


    /**
     * Retrieves the final presentation task for a given project.
     * This method is used to identify the task designated as the final presentation within a project,
     * which is a special task that typically marks the end of the project.
     *
     * @param project The {@link ProjectEntity} object representing the project.
     * @return The {@link TaskEntity} object representing the final presentation task, or null if not found.
     */
    private TaskEntity getFinalPresentationTask(ProjectEntity project) {
        String finalPresentationSystemName = project.getSystemName() + "_final_presentation";
        return taskDao.findBySystemTiltle(finalPresentationSystemName);
    }

    /**
     * Removes a user from all tasks within a project.
     * This method is used when a user is removed from a project to ensure that they are also removed
     * from any tasks within that project. If the user is in charge of a task, the project creator is set as the new in charge.
     *
     * @param userId The ID of the user being removed.
     * @param projectId The ID of the project from which the user is being removed.
     */
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
