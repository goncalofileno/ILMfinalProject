package com.ilm.projecto_ilm_backend.dto.task;

import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for updating a task.
 * This class encapsulates all necessary information required to update an existing task,
 * including its unique identifier, title, system title, description, status, dates, collaboration status,
 * dependent task IDs, member IDs, creator ID, in charge ID, and the system project name.
 */
public class UpdateTaskDto {
    private int id; // Unique identifier of the task
    private String title; // Title of the task
    private String systemTitle; // System title of the task for internal use
    private String description; // Description of the task
    private TaskStatusENUM status; // Current status of the task
    private LocalDateTime initialDate; // The start date of the task
    private LocalDateTime finalDate; // The end date of the task
    private String outColaboration; // Status of external collaboration on the task
    private List<Integer> dependentTaskIds; // IDs of tasks that depend on this task
    private List<Integer> memberIds; // IDs of members associated with this task
    private int creatorId; // ID of the user who created the task
    private int inChargeId; // ID of the user in charge of the task
    private String systemProjectName; // System name of the project this task belongs to

    /**
     * Default constructor.
     */
    public UpdateTaskDto() {
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

    public List<Integer> getDependentTaskIds() {
        return dependentTaskIds;
    }

    public void setDependentTaskIds(List<Integer> dependentTaskIds) {
        this.dependentTaskIds = dependentTaskIds;
    }

    public List<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getInChargeId() {
        return inChargeId;
    }

    public void setInChargeId(int inChargeId) {
        this.inChargeId = inChargeId;
    }

    public String getSystemProjectName() {
        return systemProjectName;
    }

    public void setSystemProjectName(String systemProjectName) {
        this.systemProjectName = systemProjectName;
    }

    @Override
    public String toString() {
        return "UpdateTaskDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", systemTitle='" + systemTitle + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", initialDate=" + initialDate +
                ", finalDate=" + finalDate +
                ", outColaboration='" + outColaboration + '\'' +
                ", dependentTaskIds=" + dependentTaskIds +
                ", memberIds=" + memberIds +
                ", creatorId=" + creatorId +
                ", inChargeId=" + inChargeId +
                ", systemProjectName='" + systemProjectName + '\'' +
                '}';
    }
}
