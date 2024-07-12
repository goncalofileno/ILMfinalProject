package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@NamedQueries({
        // Query to find all NoteEntity instances associated with a specific project.
        @NamedQuery(name = "Message.findById", query = "SELECT m FROM MessageEntity m WHERE m.id = :id"),
        // Query to find all MessageEntity instances associated with a specific project.
        @NamedQuery(name = "Message.findByProjectId", query = "SELECT m FROM MessageEntity m WHERE m.receiverProject.id = :projectId")
})
/**
 * The MessageEntity class represents the "message" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
public class MessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    // Unique identifier for the message.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;
    // Text content of the message.
    @Column(name = "text", nullable = false, unique = false, updatable = true, columnDefinition = "TEXT")
    private String text;
    // Date and time when the message was sent.
    @Column(name = "date", nullable = false, unique = false, updatable = true)
    private LocalDateTime date;
    // User entity who is the sender of the message.
    @NotNull
    @ManyToOne
    private UserEntity sender;
    // Project entity which is the receiver of the message.
    @NotNull
    @ManyToOne
    private ProjectEntity receiverProject;
    // Default constructor for the MessageEntity class.
    public MessageEntity() {
    }
    // Constructor for the MessageEntity class with parameters.
    public MessageEntity(String text, LocalDateTime date, UserEntity sender, ProjectEntity receiverProject) {
        this.text = text;
        this.date = date;
        this.sender = sender;
        this.receiverProject = receiverProject;
    }
    // Getter for the id field.
    public int getId() {
        return id;
    }
    // Setter for the id field.
    public void setId(int id) {
        this.id = id;
    }
    // Getter for the text field.
    public String getText() {
        return text;
    }
    // Setter for the text field.
    public void setText(String text) {
        this.text = text;
    }
    // Getter for the date field.
    public LocalDateTime getDate() {
        return date;
    }
    // Setter for the date field.
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    // Getter for the sender field.
    public UserEntity getSender() {
        return sender;
    }
    // Setter for the sender field.
    public void setSender(UserEntity sender) {
        this.sender = sender;
    }
    // Getter for the receiverProject field.
    public ProjectEntity getReceiverProject() {
        return receiverProject;
    }
    // Setter for the receiverProject field.
    public void setReceiverProject(ProjectEntity receiverProject) {
        this.receiverProject = receiverProject;
    }
}
