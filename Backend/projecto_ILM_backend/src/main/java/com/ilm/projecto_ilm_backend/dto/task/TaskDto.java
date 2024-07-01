package com.ilm.projecto_ilm_backend.dto.task;

import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;

import java.time.LocalDateTime;
import java.util.List;

public class TaskDto {

    private int id;
    private String title;
    private String systemTitle;
    private String description;
    private TaskStatusENUM status;
    private LocalDateTime initialDate;
    private LocalDateTime finalDate;
    private String outColaboration;
    private List<TaskDto> dependentTasks;
    private List<MemberInTaskDto> membersOfTask;

    public TaskDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSystemTitle() {
        return systemTitle;
    }

    public void setSystemTitle(String systemTitle) {
        this.systemTitle = systemTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatusENUM getStatus() {
        return status;
    }

    public void setStatus(TaskStatusENUM status) {
        this.status = status;
    }

    public LocalDateTime getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDateTime initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDateTime getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDateTime finalDate) {
        this.finalDate = finalDate;
    }

    public String getOutColaboration() {
        return outColaboration;
    }

    public void setOutColaboration(String outColaboration) {
        this.outColaboration = outColaboration;
    }

    public List<TaskDto> getDependentTasks() {
        return dependentTasks;
    }

    public void setDependentTasks(List<TaskDto> dependentTasks) {
        this.dependentTasks = dependentTasks;
    }

    public List<MemberInTaskDto> getMembersOfTask() {
        return membersOfTask;
    }

    public void setMembersOfTask(List<MemberInTaskDto> membersOfTask) {
        this.membersOfTask = membersOfTask;
    }
}
