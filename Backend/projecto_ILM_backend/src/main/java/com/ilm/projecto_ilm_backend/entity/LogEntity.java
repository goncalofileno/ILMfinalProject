package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.StateProjectEnumConverter;
import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.TaskStatusEnumConverter;
import com.ilm.projecto_ilm_backend.ENUMS.LogTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.LogTypeEnumConverter;
import com.ilm.projecto_ilm_backend.ENUMS.TaskStatusENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
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
    @Convert(converter = LogTypeEnumConverter.class)
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
    @Convert(converter = TaskStatusEnumConverter.class)
    @Column(name = "taskOldStatus", nullable = true, unique = false, updatable = false)
    private TaskStatusENUM taskOldStatus;

    /**
     * The new status of the task associated with the log entry. This is an enumerated type.
     */
    @Convert(converter = TaskStatusEnumConverter.class)
    @Column(name = "taskNewStatus", nullable = true, unique = false, updatable = false)
    private TaskStatusENUM taskNewStatus;

    /**
     * The old state of the project associated with the log entry. This is an enumerated type.
     */
    @Convert(converter = StateProjectEnumConverter.class)
    @Column(name = "projectOldState", nullable = true, unique = false, updatable = false)
    private StateProjectENUM projectOldState;

    /**
     * The new state of the project associated with the log entry. This is an enumerated type.
     */
    @Convert(converter = StateProjectEnumConverter.class)
    @Column(name = "projectNewState", nullable = true, unique = false, updatable = false)
    private StateProjectENUM projectNewState;

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
     * Sets the deleted status of the message by the sender.
     *
     * @param deletedBySender the new deleted status of the message by the sender.
     */
    @Column(name = "oldUserType", nullable = true, unique = false, updatable = false)
    private UserInProjectTypeENUM oldUserType;
    /**
     * The new type of the user in the project associated with the log entry.
     * This is an enumerated type.
     */
    @Column(name = "newUserType", nullable = true, unique = false, updatable = false)
    private UserInProjectTypeENUM newUserType;

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
     * Method to check the constraints before persisting or updating the entity.
     */
//    @PrePersist
//    @PreUpdate
//    private void validateLogEntry() {
//        switch (this.type) {
//            case MEMBER_REMOVED:
//            case MEMBER_ADDED:
//                if (receiver == null || taskTitle != null || taskOldStatus != null || taskNewStatus != null || resourceName != null || resourceStock != 0) {
//                    throw new IllegalStateException("For MEMBERS log type: receiver must not be null and all task/resource fields must be null.");
//                }
//                break;
//            case TASKS_CREATED:
//                if (taskTitle == null || receiver != null || taskOldStatus != null || taskNewStatus != null || resourceName != null || resourceStock != 0) {
//                    throw new IllegalStateException("For TASKS_CREATED log type: taskTitle must not be null and all other fields must be null.");
//                }
//                break;
//            case TASKS_COMPLETED:
//            case TASKS_IN_PROGRESS:
//                if (taskTitle == null || taskOldStatus == null || taskNewStatus == null || receiver != null || resourceName != null || resourceStock != 0) {
//                    throw new IllegalStateException("For TASKS_COMPLETED or TASKS_IN_PROGRESS log types: taskTitle, taskOldStatus, and taskNewStatus must not be null, and all other fields must be null.");
//                }
//                break;
//            case TASKS_DELETED:
//            case TASKS_UPDATED:
//                if (taskTitle == null || receiver != null || taskOldStatus != null || taskNewStatus != null || resourceName != null || resourceStock != 0) {
//                    throw new IllegalStateException("For TASKS_DELETED or TASKS_UPDATED log types: taskTitle must not be null and all other fields must be null.");
//                }
//                break;
//            case PROJECT_INFO_UPDATED:
//                if (receiver != null || taskTitle != null || taskOldStatus != null || taskNewStatus != null || resourceName != null || resourceStock != 0 || projectOldState != null || projectNewState != null) {
//                    throw new IllegalStateException("For PROJECT_INFO_UPDATED log type: all fields must be null.");
//                }
//                break;
//            case PROJECT_STATUS_UPDATED:
//                if (projectOldState == null || projectNewState == null || receiver != null || taskTitle != null || taskOldStatus != null || taskNewStatus != null || resourceName != null || resourceStock != 0) {
//                    throw new IllegalStateException("For PROJECT_STATUS_UPDATED log type: projectOldState and projectNewState must not be null and all other fields must be null.");
//                }
//                break;
//            case RESOURCES_UPDATED:
//                if (receiver != null || taskTitle != null || taskOldStatus != null || taskNewStatus != null || projectOldState != null || projectNewState != null) {
//                    throw new IllegalStateException("For RESOURCES_ADDED log type: resourceName and resourceStock must not be null and all other fields must be null.");
//                }
//                break;
//            case MEMBER_TYPE_CHANGED:
//                if (oldUserType == null || newUserType == null || receiver == null || taskTitle != null || taskOldStatus != null || taskNewStatus != null || resourceName != null || resourceStock != 0 || projectOldState != null || projectNewState != null) {
//                    throw new IllegalStateException("For MEMBER_TYPE_CHANGED log type: oldUserType and newUserType must not be null and all other fields must be null.");
//                }
//                break;
//            case MEMBER_LEFT:
//                if ( receiver != null || taskTitle != null || taskOldStatus != null || taskNewStatus != null || resourceName != null || resourceStock != 0 || projectOldState != null || projectNewState != null || oldUserType != null || newUserType != null) {
//                    throw new IllegalStateException("For MEMBER_LEFT log type: author and project must not be null and all other fields must be null.");
//                }
//            default:
//                throw new IllegalStateException("Unknown log type: " + this.type);
//        }
//    }

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
     * Returns the old state of the project associated with this log entry.
     *
     * @return the old state of the project.
     */
    public StateProjectENUM getProjectOldState() {
        return projectOldState;
    }

    /**
     * Sets the old state of the project associated with this log entry.
     *
     * @param projectOldState the new old state of the project.
     */
    public void setProjectOldState(StateProjectENUM projectOldState) {
        this.projectOldState = projectOldState;
    }

    /**
     * Returns the new state of the project associated with this log entry.
     *
     * @return the new state of the project.
     */
    public StateProjectENUM getProjectNewState() {
        return projectNewState;
    }

    /**
     * Sets the new state of the project associated with this log entry.
     *
     * @param projectNewState the new state of the project.
     */
    public void setProjectNewState(StateProjectENUM projectNewState) {
        this.projectNewState = projectNewState;
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
    /**
     * Returns the old user type in the project associated with the log entry.
     *
     * @return the old user type in the project.
     */
    public UserInProjectTypeENUM getOldUserType() {
        return oldUserType;
    }
    /**
     * Sets the old user type in the project associated with the log entry.
     *
     * @param oldUserType the new old user type in the project.
     */
    public void setOldUserType(UserInProjectTypeENUM oldUserType) {
        this.oldUserType = oldUserType;
    }
    /**
     * Returns the new user type in the project associated with the log entry.
     *
     * @return the new user type in the project.
     */
    public UserInProjectTypeENUM getNewUserType() {
        return newUserType;
    }
    /**
     * Sets the new user type in the project associated with the log entry.
     *
     * @param newUserType the new user type in the project.
     */
    public void setNewUserType(UserInProjectTypeENUM newUserType) {
        this.newUserType = newUserType;
    }
}
