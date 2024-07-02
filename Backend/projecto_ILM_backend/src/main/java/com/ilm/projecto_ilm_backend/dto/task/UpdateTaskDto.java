package com.ilm.projecto_ilm_backend.dto.task;

import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;

import java.time.LocalDateTime;
import java.util.List;

public class UpdateTaskDto {
    private int id;
    private String title;
    private String systemTitle;
    private String description;
    private TaskStatusENUM status;
    private LocalDateTime initialDate;
    private LocalDateTime finalDate;
    private String outColaboration;
    private List<Integer> dependentTaskIds;
    private List<Integer> memberIds;
    private int creatorId;
    private int inChargeId;
    private String systemProjectName;

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
