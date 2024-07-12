package com.ilm.projecto_ilm_backend.dto.task;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dto.project.ProjectMemberDto;

import java.util.List;

/**
 * Data Transfer Object for representing a page of tasks within a project.
 * This class encapsulates details about the project's name, a specific project task, a list of tasks,
 * the type of user viewing the tasks, project members, project progress, and the project's state.
 */
public class TasksPageDto {

    private String projectName; // Name of the project
    private TaskDto projectTask; // A specific task associated with the project
    private List<TaskDto> tasks; // List of tasks within the project
    private UserInProjectTypeENUM userSeingTasksType; // Type of user viewing the tasks
    private List<ProjectMemberDto> projectMembers; // List of members involved in the project
    private int projectProgress; // Progress of the project as a percentage
    private StateProjectENUM projectState; // Current state of the project

    /**
     * Default constructor.
     */
    public TasksPageDto() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public TaskDto getProjectTask() {
        return projectTask;
    }

    public void setProjectTask(TaskDto projectTask) {
        this.projectTask = projectTask;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }

    public UserInProjectTypeENUM getUserSeingTasksType() {
        return userSeingTasksType;
    }

    public void setUserSeingTasksType(UserInProjectTypeENUM userSeingTasksType) {
        this.userSeingTasksType = userSeingTasksType;
    }

    public List<ProjectMemberDto> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(List<ProjectMemberDto> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public int getProjectProgress() {
        return projectProgress;
    }

    public void setProjectProgress(int projectProgress) {
        this.projectProgress = projectProgress;
    }

    public StateProjectENUM getProjectState() {
        return projectState;
    }

    public void setProjectState(StateProjectENUM projectState) {
        this.projectState = projectState;
    }
}
