package com.ilm.projecto_ilm_backend.dto.notification;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for notifications.
 * This class encapsulates the details of a notification, including its unique identifier, type,
 * read status, send date, associated project information, and user details.
 */
public class NotificationDto {
    private int id; // Unique identifier of the notification
    private String type; // Type of the notification
    private boolean readStatus; // Read status of the notification
    private LocalDateTime sendDate; // Date and time the notification was sent
    private String projectName; // Name of the associated project
    private String projectSystemName; // System name of the associated project
    private String projectStatus; // Status of the associated project
    private String userName; // Name of the user associated with the notification
    private String systemUserName; // System username of the associated user
    private String userPhoto; // Photo URL of the associated user
    private String newUserType;

    public NotificationDto() {
    }

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

    public String getNewUserType() {
        return newUserType;
    }

    public void setNewUserType(String newUserType) {
        this.newUserType = newUserType;
    }
}

