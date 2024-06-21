package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import java.io.Serializable;
import java.time.LocalDateTime;

public class MessageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "text", nullable = false, unique = false, updatable = true, columnDefinition = "TEXT")
    private String text;

    @Column(name = "date", nullable = false, unique = false, updatable = true)
    private LocalDateTime date;
    
    @NotNull
    @ManyToOne
    private UserEntity sender;

    @NotNull
    @ManyToOne
    private ProjectEntity receiverProject;

    public MessageEntity() {
    }

    public MessageEntity(String text, LocalDateTime date, UserEntity sender, ProjectEntity receiverProject) {
        this.text = text;
        this.date = date;
        this.sender = sender;
        this.receiverProject = receiverProject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public ProjectEntity getReceiverProject() {
        return receiverProject;
    }

    public void setReceiverProject(ProjectEntity receiverProject) {
        this.receiverProject = receiverProject;
    }
}
