package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;
import java.time.LocalDateTime;


@Entity
@Table(name="message")
public class MessageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

    @Column(name="text", nullable = false, unique = false, updatable = true)
    private String text;

    @Column(name="date", nullable = false, unique = false, updatable = true)
    private LocalDateTime date;

    @Column(name="seen", nullable = false, unique = false, updatable = true)
    private boolean seen;

    @NotNull
    @ManyToOne
    private UserEntity sender;

    @ManyToOne
    private UserEntity receiverUser;

    @ManyToOne
    private ProjectEntity receiverProject;

    public MessageEntity() {
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

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }


    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(UserEntity receiverUser) {
        this.receiverUser = receiverUser;
    }

    public ProjectEntity getReceiverProject() {
        return receiverProject;
    }

    public void setReceiverProject(ProjectEntity receiverProject) {
        this.receiverProject = receiverProject;
    }
}
