package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.entity.TaskEntity;
import com.ilm.projecto_ilm_backend.entity.UserTaskEntity;
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
            userTask1.setType(UserInTaskTypeENUM.CREATOR);
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
            userTask3.setType(UserInTaskTypeENUM.CREATOR);
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
            userTask5.setType(UserInTaskTypeENUM.CREATOR);
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
}
