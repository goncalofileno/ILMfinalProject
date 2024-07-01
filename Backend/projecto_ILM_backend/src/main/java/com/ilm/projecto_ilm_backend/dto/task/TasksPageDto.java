package com.ilm.projecto_ilm_backend.dto.task;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dto.project.ProjectMemberDto;

import java.util.List;

public class TasksPageDto {

    private String projectName;
    private TaskDto projectTask;
    private List<TaskDto> tasks;
    private UserInProjectTypeENUM userSeingTasksType;
    private List<ProjectMemberDto> projectMembers;

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
}
