package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.NotificationTypeEnumConverter;
import com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
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
                query = "UPDATE NotificationEntity n SET n.readStatus = true WHERE n.receptor.id = :userId AND n.id IN :notificationIds")
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

    @Column(name = "taskName", nullable = true, unique = false, updatable = false)
    private String taskName;

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
}
