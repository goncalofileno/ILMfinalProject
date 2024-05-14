package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.LogTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="log")
public class LogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

    @Column(name = "date", nullable = false, unique = false, updatable = true)
    private LocalDateTime date;

    @Column(name = "receiver", nullable = true, unique = false, updatable = false)
    private String receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, unique = false, updatable = false)
    private LogTypeENUM type;

    @Column(name = "taskTitle", nullable = true, unique = false, updatable = false)
    private String taskTitle;

    @Column(name = "taskOldStatus", nullable = true, unique = false, updatable = false)
    private TaskStatusENUM taskOldStatus;

    @Column(name = "taskNewStatus", nullable = true, unique = false, updatable = false)
    private TaskStatusENUM taskNewStatus;

    @Column(name = "resourceName", nullable = true, unique = false, updatable = false)
    private String resourceName;

    @Column(name = "resourceStock", nullable = true, unique = false, updatable = false)
    private int resourceStock;

    @NotNull
    @ManyToOne
    private UserEntity author;

    @NotNull
    @ManyToOne
    private ProjectEntity project;

    public LogEntity() {
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

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }
}
