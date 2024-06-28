package com.ilm.projecto_ilm_backend.dto.logs;

import com.ilm.projecto_ilm_backend.ENUMS.LogTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;

import java.time.LocalDateTime;

public class LogDto {
    private int id;
    private LocalDateTime date;
    private String receiver;
    private LogTypeENUM type;
    private String taskTitle;
    private TaskStatusENUM taskOldStatus;
    private TaskStatusENUM taskNewStatus;
    private StateProjectENUM projectOldState;
    private StateProjectENUM projectNewState;
    private String resourceName;
    private int resourceStock;
    private String authorName;
    private String authorPhoto;
    private String memberOldType;
    private String memberNewType;

    public LogDto() {
    }

    public LogDto(int id, LocalDateTime date, String receiver, LogTypeENUM type, String taskTitle, TaskStatusENUM taskOldStatus, TaskStatusENUM taskNewStatus, StateProjectENUM projectOldState, StateProjectENUM projectNewState, String resourceName, int resourceStock, String authorName, String authorPhoto, String memberOldType, String memberNewType) {
        this.id = id;
        this.date = date;
        this.receiver = receiver;
        this.type = type;
        this.taskTitle = taskTitle;
        this.taskOldStatus = taskOldStatus;
        this.taskNewStatus = taskNewStatus;
        this.projectOldState = projectOldState;
        this.projectNewState = projectNewState;
        this.resourceName = resourceName;
        this.resourceStock = resourceStock;
        this.authorName = authorName;
        this.authorPhoto = authorPhoto;
        this.memberOldType = memberOldType;
        this.memberNewType = memberNewType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public LogTypeENUM getType() {
        return type;
    }

    public void setType(LogTypeENUM type) {
        this.type = type;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public TaskStatusENUM getTaskOldStatus() {
        return taskOldStatus;
    }

    public void setTaskOldStatus(TaskStatusENUM taskOldStatus) {
        this.taskOldStatus = taskOldStatus;
    }

    public TaskStatusENUM getTaskNewStatus() {
        return taskNewStatus;
    }

    public void setTaskNewStatus(TaskStatusENUM taskNewStatus) {
        this.taskNewStatus = taskNewStatus;
    }

    public StateProjectENUM getProjectOldState() {
        return projectOldState;
    }

    public void setProjectOldState(StateProjectENUM projectOldState) {
        this.projectOldState = projectOldState;
    }

    public StateProjectENUM getProjectNewState() {
        return projectNewState;
    }

    public void setProjectNewState(StateProjectENUM projectNewState) {
        this.projectNewState = projectNewState;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getResourceStock() {
        return resourceStock;
    }

    public void setResourceStock(int resourceStock) {
        this.resourceStock = resourceStock;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
    }

    public String getMemberOldType() {
        return memberOldType;
    }

    public void setMemberOldType(String memberOldType) {
        this.memberOldType = memberOldType;
    }

    public String getMemberNewType() {
        return memberNewType;
    }

    public void setMemberNewType(String memberNewType) {
        this.memberNewType = memberNewType;
    }
}
