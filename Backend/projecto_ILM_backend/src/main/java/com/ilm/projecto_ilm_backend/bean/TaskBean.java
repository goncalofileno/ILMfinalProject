package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.project.ProjectMemberDto;
import com.ilm.projecto_ilm_backend.dto.task.MemberInTaskDto;
import com.ilm.projecto_ilm_backend.dto.task.TaskDto;
import com.ilm.projecto_ilm_backend.dto.task.TaskSuggestionDto;
import com.ilm.projecto_ilm_backend.dto.task.TasksPageDto;
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

    private int numberOfProjectsToCreate = 20;

    @Transactional
    public void createDefaultTasksIfNotExistent() {
        for (int i = 1; i < numberOfProjectsToCreate + 1; i++) {

            TaskEntity task1 = new TaskEntity();
            task1.setTitle("Task 1");
            task1.setSystemTitle(taskSystemNameGenerator(task1.getTitle()));
            task1.setDescription("This task aims to develop the front-end of the system.");
            task1.setInitialDate(LocalDateTime.now().minus(4, ChronoUnit.YEARS));
            task1.setFinalDate(LocalDateTime.now().plus(3, ChronoUnit.YEARS));
            task1.setStatus(TaskStatusENUM.PLANNED);
            task1.setProject(projectDao.findById(i));
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
            task2.setInitialDate(LocalDateTime.now().minus(3, ChronoUnit.YEARS));
            task2.setFinalDate(LocalDateTime.now().plus(2, ChronoUnit.YEARS));
            task2.setStatus(TaskStatusENUM.IN_PROGRESS);
            task2.setProject(projectDao.findById(i));
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
            task3.setInitialDate(LocalDateTime.now().minus(2, ChronoUnit.YEARS));
            task3.setFinalDate(LocalDateTime.now().plus(1, ChronoUnit.YEARS));
            task3.setStatus(TaskStatusENUM.DONE);
            task3.setProject(projectDao.findById(i));
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

        if(taskDao.findTaskBySystemTitle(systemTitle) == null) {
            return systemTitle;
        }else {
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
        projectTask.setStatus(TaskStatusENUM.PLANNED);
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

        TasksPageDto tasksPageDto = new TasksPageDto();
        tasksPageDto.setProjectName(projectName);
        tasksPageDto.setProjectTask(projectTask);
        tasksPageDto.setTasks(tasksDto);
        tasksPageDto.setUserSeingTasksType(userSeingTasksType);
        tasksPageDto.setProjectMembers(projectMembers);

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
}
