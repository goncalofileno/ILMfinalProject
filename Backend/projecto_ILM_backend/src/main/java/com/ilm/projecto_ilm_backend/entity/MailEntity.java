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
@Table(name = "mail")
@NamedQuery(name = "Mail.getMailsReceivedByUserId", query = "SELECT m FROM MailEntity m WHERE m.receiver.id = :userId")
@NamedQuery(name = "Mail.getMailsSentByUserId", query = "SELECT m FROM MailEntity m WHERE m.sender.id = :userId")
@NamedQuery(name = "Mail.findById", query = "SELECT m FROM MailEntity m WHERE m.id = :id")
@NamedQuery(name = "Mail.getUnseenMailsCount", query = "SELECT COUNT(m) FROM MailEntity m WHERE m.receiver.id = :userId AND m.seen = false")
public class MailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the message. This is the primary key in the "message" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The subject of the message.
     */
    @Column(name = "subject", nullable = false, unique = false, updatable = true)
    private String subject;

    /**
     * The text of the message.
     */
    @Column(name = "text", nullable = false, unique = false, updatable = true)
    private String text;

    /**
     * The deleted status of the message.
     */
    @Column(name = "deleted", nullable = false, unique = false, updatable = true)
    private boolean deleted;

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
    private UserEntity receiver;

    /**
     * Default constructor.
     */
    public MailEntity() {
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
     * Returns the subject of this message.
     *
     * @return the subject of this message.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject of this message.
     *
     * @param subject the new subject of this message.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns the deleted status of this message.
     *
     * @return true if the message has been deleted, false otherwise.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted status of this message.
     *
     * @param deleted the new deleted status of this message.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
    public UserEntity getReceiver() {
        return receiver;
    }

    /**
     * Sets the user receiver of this message.
     *
     * @param receiver the new user receiver of this message.
     */
    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }
}
