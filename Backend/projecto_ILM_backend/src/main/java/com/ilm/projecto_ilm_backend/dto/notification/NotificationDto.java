package com.ilm.projecto_ilm_backend.dto.notification;

import java.time.LocalDateTime;

public class NotificationDto {
    private int id;
    private String type;
    private boolean readStatus;
    private LocalDateTime sendDate;
    private String projectName;
    private String projectSystemName;
    private String projectStatus;
    private String userName;
    private String systemUserName;
    private String userPhoto;

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public String getprojectName() {
        return projectName;
    }

    public void setprojectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSystemName() {
        return projectSystemName;
    }

    public void setProjectSystemName(String projectSystemName) {
        this.projectSystemName = projectSystemName;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSystemUserName() {
        return systemUserName;
    }

    public void setSystemUserName(String systemUserName) {
        this.systemUserName = systemUserName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}

