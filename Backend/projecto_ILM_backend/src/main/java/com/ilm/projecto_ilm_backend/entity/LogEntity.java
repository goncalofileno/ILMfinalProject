package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.LogTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The LogEntity class represents the "log" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "log")
public class LogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the log. This is the primary key in the "log" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The date of the log entry.
     */
    @Column(name = "date", nullable = false, unique = false, updatable = true)
    private LocalDateTime date;

    /**
     * The receiver of the log entry.
     */
    @Column(name = "receiver", nullable = true, unique = false, updatable = false)
    private String receiver;

    /**
     * The type of the log entry. This is an enumerated type.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, unique = false, updatable = false)
    private LogTypeENUM type;

    /**
     * The title of the task associated with the log entry.
     */
    @Column(name = "taskTitle", nullable = true, unique = false, updatable = false)
    private String taskTitle;

    /**
     * The old status of the task associated with the log entry. This is an enumerated type.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "taskOldStatus", nullable = true, unique = false, updatable = false)
    private TaskStatusENUM taskOldStatus;

    /**
     * The new status of the task associated with the log entry. This is an enumerated type.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "taskNewStatus", nullable = true, unique = false, updatable = false)
    private TaskStatusENUM taskNewStatus;

    /**
     * The name of the resource associated with the log entry.
     */
    @Column(name = "resourceName", nullable = true, unique = false, updatable = false)
    private String resourceName;

    /**
     * The stock of the resource associated with the log entry.
     */
    @Column(name = "resourceStock", nullable = true, unique = false, updatable = false)
    private int resourceStock;

    /**
     * The author of the log entry. This is a many-to-one relationship with the UserEntity class.
     */
    @NotNull
    @ManyToOne
    private UserEntity author;

    /**
     * The project associated with the log entry. This is a many-to-one relationship with the ProjectEntity class.
     */
    @NotNull
    @ManyToOne
    private ProjectEntity project;

    /**
     * Default constructor.
     */
    public LogEntity() {
    }

    /**
     * Returns the unique ID of this log entry.
     *
     * @return the ID of this log entry.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this log entry.
     *
     * @param id the new ID of this log entry.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the date of this log entry.
     *
     * @return the date of this log entry.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the date of this log entry.
     *
     * @param date the new date of this log entry.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Returns the receiver of this log entry.
     *
     * @return the receiver of this log entry.
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Sets the receiver of this log entry.
     *
     * @param receiver the new receiver of this log entry.
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * Returns the type of this log entry.
     *
     * @return the type of this log entry.
     */
    public LogTypeENUM getType() {
        return type;
    }

    /**
     * Sets the type of this log entry.
     *
     * @param type the new type of this log entry.
     */
    public void setType(LogTypeENUM type) {
        this.type = type;
    }

    /**
     * Returns the title of the task associated with this log entry.
     *
     * @return the title of the task.
     */
    public String getTaskTitle() {
        return taskTitle;
    }

    /**
     * Sets the title of the task associated with this log entry.
     *
     * @param taskTitle the new title of the task.
     */
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    /**
     * Returns the old status of the task associated with this log entry.
     *
     * @return the old status of the task.
     */
    public TaskStatusENUM getTaskOldStatus() {
        return taskOldStatus;
    }

    /**
     * Sets the old status of the task associated with this log entry.
     *
     * @param taskOldStatus the new old status of the task.
     */
    public void setTaskOldStatus(TaskStatusENUM taskOldStatus) {
        this.taskOldStatus = taskOldStatus;
    }

    /**
     * Returns the new status of the task associated with this log entry.
     *
     * @return the new status of the task.
     */
    public TaskStatusENUM getTaskNewStatus() {
        return taskNewStatus;
    }

    /**
     * Sets the new status of the task associated with this log entry.
     *
     * @param taskNewStatus the new status of the task.
     */
    public void setTaskNewStatus(TaskStatusENUM taskNewStatus) {
        this.taskNewStatus = taskNewStatus;
    }

    /**
     * Returns the name of the resource associated with this log entry.
     *
     * @return the name of the resource.
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Sets the name of the resource associated with this log entry.
     *
     * @param resourceName the new name of the resource.
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * Returns the stock of the resource associated with this log entry.
     *
     * @return the stock of the resource.
     */
    public int getResourceStock() {
        return resourceStock;
    }

    /**
     * Sets the stock of the resource associated with this log entry.
     *
     * @param resourceStock the new stock of the resource.
     */
    public void setResourceStock(int resourceStock) {
        this.resourceStock = resourceStock;
    }

    /**
     * Returns the author of this log entry.
     *
     * @return the author of this log entry.
     */
    public UserEntity getAuthor() {
        return author;
    }

    /**
     * Sets the author of this log entry.
     *
     * @param author the new author of this log entry.
     */
    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    /**
     * Returns the project associated with this log entry.
     *
     * @return the project associated with this log entry.
     */
    public ProjectEntity getProject() {
        return project;
    }

    /**
     * Sets the project associated with this log entry.
     *
     * @param project the new project associated with this log entry.
     */
    public void setProject(ProjectEntity project) {
        this.project = project;
    }
}
