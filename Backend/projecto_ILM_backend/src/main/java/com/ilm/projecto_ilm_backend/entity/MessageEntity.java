package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The MessageEntity class represents the "message" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "message")
public class MessageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the message. This is the primary key in the "message" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The text of the message.
     */
    @Column(name = "text", nullable = false, unique = false, updatable = true)
    private String text;

    /**
     * The date of the message.
     */
    @Column(name = "date", nullable = false, unique = false, updatable = true)
    private LocalDateTime date;

    /**
     * The seen status of the message.
     */
    @Column(name = "seen", nullable = false, unique = false, updatable = true)
    private boolean seen;

    /**
     * The sender of the message. This is a many-to-one relationship with the UserEntity class.
     */
    @NotNull
    @ManyToOne
    private UserEntity sender;

    /**
     * The user receiver of the message. This is a many-to-one relationship with the UserEntity class.
     */
    @ManyToOne
    private UserEntity receiverUser;

    /**
     * The project receiver of the message. This is a many-to-one relationship with the ProjectEntity class.
     */
    @ManyToOne
    private ProjectEntity receiverProject;

    /**
     * Default constructor.
     */
    public MessageEntity() {
    }

    /**
     * Method to check the constraints before persisting or updating the entity.
     */
    @PrePersist
    @PreUpdate
    private void validateReceivers() {
        if ((receiverUser == null && receiverProject == null) || (receiverUser != null && receiverProject != null)) {
            throw new IllegalStateException("Message must have either a receiverUser or a receiverProject, but not both.");
        }
    }

    /**
     * Returns the unique ID of this message.
     *
     * @return the ID of this message.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this message.
     *
     * @param id the new ID of this message.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the text of this message.
     *
     * @return the text of this message.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of this message.
     *
     * @param text the new text of this message.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the date of this message.
     *
     * @return the date of this message.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the date of this message.
     *
     * @param date the new date of this message.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Returns the seen status of this message.
     *
     * @return true if the message has been seen, false otherwise.
     */
    public boolean isSeen() {
        return seen;
    }

    /**
     * Sets the seen status of this message.
     *
     * @param seen the new seen status of this message.
     */
    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    /**
     * Returns the sender of this message.
     *
     * @return the sender of this message.
     */
    public UserEntity getSender() {
        return sender;
    }

    /**
     * Sets the sender of this message.
     *
     * @param sender the new sender of this message.
     */
    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    /**
     * Returns the user receiver of this message.
     *
     * @return the user receiver of this message.
     */
    public UserEntity getReceiverUser() {
        return receiverUser;
    }

    /**
     * Sets the user receiver of this message.
     *
     * @param receiverUser the new user receiver of this message.
     */
    public void setReceiverUser(UserEntity receiverUser) {
        this.receiverUser = receiverUser;
    }

    /**
     * Returns the project receiver of this message.
     *
     * @return the project receiver of this message.
     */
    public ProjectEntity getReceiverProject() {
        return receiverProject;
    }

    /**
     * Sets the project receiver of this message.
     *
     * @param receiverProject the new project receiver of this message.
     */
    public void setReceiverProject(ProjectEntity receiverProject) {
        this.receiverProject = receiverProject;
    }
}
