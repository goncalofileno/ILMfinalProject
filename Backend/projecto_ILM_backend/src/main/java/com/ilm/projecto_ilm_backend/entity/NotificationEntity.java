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
        @NamedQuery(name = "NotificationEntity.findByUserId",
                query = "SELECT n FROM NotificationEntity n WHERE n.receptor.id = :userId"),
        @NamedQuery(name = "NotificationEntity.markAsRead",
                query = "UPDATE NotificationEntity n SET n.readStatus = true WHERE n.receptor.id = :userId AND n.id IN :notificationIds"),
        @NamedQuery(name = "NotificationEntity.findUnreadByUserId",
                query = "SELECT n FROM NotificationEntity n WHERE n.receptor.id = :userId AND n.readStatus = false AND n.messageNotificationClicked = false ORDER BY n.sendDate DESC"),
        @NamedQuery(name = "NotificationEntity.findReadByUserId",
                query = "SELECT n FROM NotificationEntity n WHERE n.receptor.id = :userId AND n.readStatus = true AND n.messageNotificationClicked = false ORDER BY n.sendDate DESC"),
        @NamedQuery(name = "NotificationEntity.countNonProjectMessageUnreadByUserId",
                query = "SELECT COUNT(n) FROM NotificationEntity n WHERE n.receptor.id = :userId AND n.readStatus = false AND n.messageNotificationClicked = false AND n.type <> com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM.PROJECT_MESSAGE"),
        @NamedQuery(name = "NotificationEntity.countDistinctProjectMessageUnreadByUserId",
                query = "SELECT COUNT(DISTINCT n.projectSystemName) FROM NotificationEntity n WHERE n.receptor.id = :userId AND n.readStatus = false AND n.messageNotificationClicked = false AND n.type = com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM.PROJECT_MESSAGE"),
        @NamedQuery(name = "NotificationEntity.countAllByUserId",
                query = "SELECT COUNT(n) FROM NotificationEntity n WHERE n.receptor.id = :userId"),
        @NamedQuery(name = "NotificationEntity.findSystemUsernameOfCreatorByReceptorAndType",
        query = "SELECT n.systemUserName FROM NotificationEntity n WHERE n.receptor.id = :receptorId AND n.type = :type"),
        @NamedQuery(name = "NotificationEntity.findUnreadByUserIdAndProjectId",
                query = "SELECT n FROM NotificationEntity n WHERE n.receptor.id = :userId AND n.projectSystemName = :projectSystemName AND n.readStatus = false AND n.messageNotificationClicked = false AND n.type = com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM.PROJECT_MESSAGE ORDER BY n.sendDate DESC"),
        @NamedQuery(name = "NotificationEntity.removeByProjectIdAndReceptorAndType",
                query = "DELETE FROM NotificationEntity n WHERE n.receptor.id = :receptorId AND n.projectSystemName = :projectSystemName AND n.type = :type"),
        @NamedQuery(name = "NotificationEntity.findByProjectSystemName",
                query = "SELECT n FROM NotificationEntity n WHERE n.projectSystemName = :projectSystemName"),
        @NamedQuery(name = "NotificationEntity.findDoubleNotificationTask",
                query = "SELECT n FROM NotificationEntity n WHERE n.receptor.id = :receptor AND n.taskName = :taskTitle AND n.systemUserName = :systemUsername AND n.projectSystemName = :projectSystemName AND n.sendDate > :dateMinus AND n.sendDate < :datePlus"),
})
public class NotificationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Convert(converter = NotificationTypeEnumConverter.class)
    @Column(name = "type", nullable = false, unique = false, updatable = false)
    private NotificationTypeENUM type;

    @Column(name = "readStatus", nullable = false, unique = false, updatable = true)
    private boolean readStatus;

    @Column(name = "sendDate", nullable = false, unique = false, updatable = false)
    private LocalDateTime sendDate;

    @Column(name = "projectSystemName", nullable = true, unique = false, updatable = false)
    private String projectSystemName;

    @Column(name = "projectStatus", nullable = true, unique = false, updatable = false)
    private StateProjectENUM projectStatus;

    @Column(name = "userName", nullable = true, unique = false, updatable = false)
    private String userName;

    @Column(name = "systemUserName", nullable = true, unique = false, updatable = false)
    private String systemUserName;

    @Column(name = "taskName", nullable = true, unique = false, updatable = false)
    private String taskName;

    @Column(name = "messageNotificationClicked", nullable = true, unique = false, updatable = true)
    private Boolean messageNotificationClicked;

    @Column(name = "newUserType", nullable = true, unique = false, updatable = false)
    private UserInProjectTypeENUM newUserType;

    @NotNull
    @ManyToOne
    private UserEntity receptor;

    public NotificationEntity() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NotificationTypeENUM getType() {
        return type;
    }

    public void setType(NotificationTypeENUM type) {
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

    public String getProjectSystemName() {
        return projectSystemName;
    }

    public void setProjectSystemName(String projectSystemName) {
        this.projectSystemName = projectSystemName;
    }

    public StateProjectENUM getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(StateProjectENUM projectStatus) {
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

    public UserEntity getReceptor() {
        return receptor;
    }

    public void setReceptor(UserEntity receptor) {
        this.receptor = receptor;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Boolean getMessageNotificationClicked() {
        return messageNotificationClicked;
    }

    public void setMessageNotificationClicked(Boolean messageNotificationClicked) {
        this.messageNotificationClicked = messageNotificationClicked;
    }

    public UserInProjectTypeENUM getNewUserType() {
        return newUserType;
    }

    public void setNewUserType(UserInProjectTypeENUM newUserType) {
        this.newUserType = newUserType;
    }
}
