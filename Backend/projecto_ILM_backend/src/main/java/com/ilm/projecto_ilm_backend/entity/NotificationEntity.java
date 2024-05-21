package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.NotificationTypeEnumConverter;
import com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The NotificationEntity class represents the "notification" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "notification")
public class NotificationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the notification. This is the primary key in the "notification" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The type of the notification. This is an enumerated type.
     */
    @Convert(converter = NotificationTypeEnumConverter.class)
    @Column(name = "type", nullable = false, unique = false, updatable = false)
    private NotificationTypeENUM type;

    /**
     * The read status of the notification.
     */
    @Column(name = "readStatus", nullable = false, unique = false, updatable = true)
    private boolean readStatus;

    /**
     * The send date of the notification.
     */
    @Column(name = "sendDate", nullable = false, unique = false, updatable = false)
    private LocalDateTime sendDate;

    /**
     * The text of the notification.
     */
    @Column(name = "text", nullable = false, unique = false, updatable = false)
    private String text;

    /**
     * The receptor of the notification. This is a many-to-one relationship with the UserEntity class.
     */
    @NotNull
    @ManyToOne
    private UserEntity receptor;

    /**
     * Default constructor.
     */
    public NotificationEntity() {
    }

    /**
     * Returns the unique ID of this notification.
     * @return the ID of this notification.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this notification.
     * @param id the new ID of this notification.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the type of this notification.
     * @param type the new type of this notification.
     */
    public void setType(NotificationTypeENUM type) {
        this.type = type;
    }

    /**
     * Returns the type of this notification.
     * @return the type of this notification.
     */
    public NotificationTypeENUM getType() {
        return type;
    }

    /**
     * Returns the read status of this notification.
     * @return true if the notification has been read, false otherwise.
     */
    public boolean isReadStatus() {
        return readStatus;
    }

    /**
     * Sets the read status of this notification.
     * @param readStatus the new read status of this notification.
     */
    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    /**
     * Returns the send date of this notification.
     * @return the send date of this notification.
     */
    public LocalDateTime getSendDate() {
        return sendDate;
    }

    /**
     * Sets the send date of this notification.
     * @param sendDate the new send date of this notification.
     */
    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    /**
     * Returns the text of this notification.
     * @return the text of this notification.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of this notification.
     * @param text the new text of this notification.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the receptor of this notification.
     * @return the receptor of this notification.
     */
    public UserEntity getReceptor() {
        return receptor;
    }

    /**
     * Sets the receptor of this notification.
     * @param receptor the new receptor of this notification.
     */
    public void setReceptor(UserEntity receptor) {
        this.receptor = receptor;
    }
}
