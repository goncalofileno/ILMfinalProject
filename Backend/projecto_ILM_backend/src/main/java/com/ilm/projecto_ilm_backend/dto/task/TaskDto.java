package com.ilm.projecto_ilm_backend.dto.task;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for representing a task.
 * This class encapsulates details about a task, including its ID, title, system title, description,
 * status, project state, initial and final dates, out collaboration status, dependent tasks, and members of the task.
 */
public class TaskDto {

    private int id; // Unique identifier of the task
    private String title; // Title of the task
    private String systemTitle; // System title of the task
    private String description; // Description of the task
    private TaskStatusENUM status; // Status of the task
    private StateProjectENUM projectState; // State of the project the task belongs to
    private LocalDateTime initialDate; // Initial date of the task
    private LocalDateTime finalDate; // Final date of the task
    private String outColaboration; // Out collaboration status of the task
    private List<TaskDto> dependentTasks; // List of tasks that depend on this task
    private List<MemberInTaskDto> membersOfTask; // List of members involved in the task

    /**
     * Default constructor.
     */
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

    public StateProjectENUM getProjectState() {
        return projectState;
    }

    public void setProjectState(StateProjectENUM projectState) {
        this.projectState = projectState;
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
