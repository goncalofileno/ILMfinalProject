package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.NotificationTypeEnumConverter;
import com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The NotificationEntity class represents the "notification" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "notification")
@NamedQueries({
        // Query to find a NotificationEntity by the user id of the receptor.
        @NamedQuery(name = "NotificationEntity.findByUserId",
                query = "SELECT n FROM NotificationEntity n WHERE n.receptor.id = :userId"),
        // Query to mark a NotificationEntity as read by the user id of the receptor and the notification ids.
        @NamedQuery(name = "NotificationEntity.markAsRead",
                query = "UPDATE NotificationEntity n SET n.readStatus = true WHERE n.receptor.id = :userId AND n.id IN :notificationIds"),
        // Query to find unread NotificationEntity instances by the user id of the receptor.
        @NamedQuery(name = "NotificationEntity.findUnreadByUserId",
                query = "SELECT n FROM NotificationEntity n WHERE n.receptor.id = :userId AND n.readStatus = false AND n.messageNotificationClicked = false ORDER BY n.sendDate DESC"),
        // Query to find read NotificationEntity instances by the user id of the receptor.
        @NamedQuery(name = "NotificationEntity.findReadByUserId",
                query = "SELECT n FROM NotificationEntity n WHERE n.receptor.id = :userId AND n.readStatus = true AND n.messageNotificationClicked = false ORDER BY n.sendDate DESC"),
        // Query to count non-project message unread NotificationEntity instances by the user id of the receptor.
        @NamedQuery(name = "NotificationEntity.countNonProjectMessageUnreadByUserId",
                query = "SELECT COUNT(n) FROM NotificationEntity n WHERE n.receptor.id = :userId AND n.readStatus = false AND n.messageNotificationClicked = false AND n.type <> com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM.PROJECT_MESSAGE"),
        // Query to count distinct project message unread NotificationEntity instances by the user id of the receptor.
        @NamedQuery(name = "NotificationEntity.countDistinctProjectMessageUnreadByUserId",
                query = "SELECT COUNT(DISTINCT n.projectSystemName) FROM NotificationEntity n WHERE n.receptor.id = :userId AND n.readStatus = false AND n.messageNotificationClicked = false AND n.type = com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM.PROJECT_MESSAGE"),
        // Query to count all NotificationEntity instances by the user id of the receptor.
        @NamedQuery(name = "NotificationEntity.countAllByUserId",
                query = "SELECT COUNT(n) FROM NotificationEntity n WHERE n.receptor.id = :userId"),
        // Query to find the system username of the creator of a NotificationEntity by the user id of the receptor and the notification type.
        @NamedQuery(name = "NotificationEntity.findSystemUsernameOfCreatorByReceptorAndType",
        query = "SELECT n.systemUserName FROM NotificationEntity n WHERE n.receptor.id = :receptorId AND n.type = :type"),
        // Query to find unread NotificationEntity instances by the user id of the receptor and the system name of the project.
        @NamedQuery(name = "NotificationEntity.findUnreadByUserIdAndProjectId",
                query = "SELECT n FROM NotificationEntity n WHERE n.receptor.id = :userId AND n.projectSystemName = :projectSystemName AND n.readStatus = false AND n.messageNotificationClicked = false AND n.type = com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM.PROJECT_MESSAGE ORDER BY n.sendDate DESC"),
        // Query to remove a NotificationEntity by the system name of the project, the user id of the receptor, and the notification type.
        @NamedQuery(name = "NotificationEntity.removeByProjectIdAndReceptorAndType",
                query = "DELETE FROM NotificationEntity n WHERE n.receptor.id = :receptorId AND n.projectSystemName = :projectSystemName AND n.type = :type"),
        // Query to find a NotificationEntity by the system name of the project.
        @NamedQuery(name = "NotificationEntity.findByProjectSystemName",
                query = "SELECT n FROM NotificationEntity n WHERE n.projectSystemName = :projectSystemName"),
        // Query to find a NotificationEntity that matches a specific task, user, project, and date range.
        @NamedQuery(name = "NotificationEntity.findDoubleNotificationTask",
                query = "SELECT n FROM NotificationEntity n WHERE n.receptor.id = :receptor AND n.taskName = :taskTitle AND n.systemUserName = :systemUsername AND n.projectSystemName = :projectSystemName AND n.sendDate > :dateMinus AND n.sendDate < :datePlus"),
})
public class NotificationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    // Unique identifier for the notification.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;
    // Type of the notification.
    @Convert(converter = NotificationTypeEnumConverter.class)
    @Column(name = "type", nullable = false, unique = false, updatable = false)
    private NotificationTypeENUM type;
    // Read status of the notification.
    @Column(name = "readStatus", nullable = false, unique = false, updatable = true)
    private boolean readStatus;
    // Date and time when the notification was sent.
    @Column(name = "sendDate", nullable = false, unique = false, updatable = false)
    private LocalDateTime sendDate;
    // System name of the project associated with the notification.
    @Column(name = "projectSystemName", nullable = true, unique = false, updatable = false)
    private String projectSystemName;
    // Status of the project associated with the notification.
    @Column(name = "projectStatus", nullable = true, unique = false, updatable = false)
    private StateProjectENUM projectStatus;
    // Name of the user associated with the notification.
    @Column(name = "userName", nullable = true, unique = false, updatable = false)
    private String userName;
    // System username of the user associated with the notification.
    @Column(name = "systemUserName", nullable = true, unique = false, updatable = false)
    private String systemUserName;
    // Name of the task associated with the notification.
    @Column(name = "taskName", nullable = true, unique = false, updatable = false)
    private String taskName;
    // Status of the notification message click.
    @Column(name = "messageNotificationClicked", nullable = true, unique = false, updatable = true)
    private Boolean messageNotificationClicked;
    // Type of the new user associated with the notification.
    @Column(name = "newUserType", nullable = true, unique = false, updatable = false)
    private UserInProjectTypeENUM newUserType;
    // User entity who is the receptor of the notification.
    @NotNull
    @ManyToOne
    private UserEntity receptor;
    // Default constructor for the NotificationEntity class.
    public NotificationEntity() {}
    // Getter for the id field.
    public int getId() {
        return id;
    }
    // Setter for the id field.
    public void setId(int id) {
        this.id = id;
    }
    // Getter for the type field.
    public NotificationTypeENUM getType() {
        return type;
    }
    // Setter for the type field.
    public void setType(NotificationTypeENUM type) {
        this.type = type;
    }
    // Getter for the readStatus field.
    public boolean isReadStatus() {
        return readStatus;
    }
    // Setter for the readStatus field.
    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }
    // Getter for the sendDate field.
    public LocalDateTime getSendDate() {
        return sendDate;
    }
    // Setter for the sendDate field.
    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }
    // Getter for the projectSystemName field.
    public String getProjectSystemName() {
        return projectSystemName;
    }
    // Setter for the projectSystemName field.
    public void setProjectSystemName(String projectSystemName) {
        this.projectSystemName = projectSystemName;
    }
    // Getter for the projectStatus field.
    public StateProjectENUM getProjectStatus() {
        return projectStatus;
    }
    // Setter for the projectStatus field.
    public void setProjectStatus(StateProjectENUM projectStatus) {
        this.projectStatus = projectStatus;
    }
    // Getter for the userName field.
    public String getUserName() {
        return userName;
    }
    // Setter for the userName field.
    public void setUserName(String userName) {
        this.userName = userName;
    }
    // Getter for the systemUserName field.
    public String getSystemUserName() {
        return systemUserName;
    }
    // Setter for the systemUserName field.
    public void setSystemUserName(String systemUserName) {
        this.systemUserName = systemUserName;
    }
    // Getter for the receptor field.
    public UserEntity getReceptor() {
        return receptor;
    }
    // Setter for the receptor field.
    public void setReceptor(UserEntity receptor) {
        this.receptor = receptor;
    }
    // Getter for the taskName field.
    public String getTaskName() {
        return taskName;
    }
    // Setter for the taskName field.
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    // Getter for the messageNotificationClicked field.
    public Boolean getMessageNotificationClicked() {
        return messageNotificationClicked;
    }
    // Setter for the messageNotificationClicked field.
    public void setMessageNotificationClicked(Boolean messageNotificationClicked) {
        this.messageNotificationClicked = messageNotificationClicked;
    }
    // Getter for the newUserType field.
    public UserInProjectTypeENUM getNewUserType() {
        return newUserType;
    }
    // Setter for the newUserType field.
    public void setNewUserType(UserInProjectTypeENUM newUserType) {
        this.newUserType = newUserType;
    }
}
