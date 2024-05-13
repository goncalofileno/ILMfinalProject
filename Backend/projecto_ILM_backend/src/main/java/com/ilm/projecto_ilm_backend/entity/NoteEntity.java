package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="note")
public class NoteEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="text", nullable = false, unique = false, updatable = true)
    private String text;

    @Column(name="date", nullable = false, unique = false, updatable = true)
    private LocalDateTime date;

    @Column(name="done", nullable = false, unique = false, updatable = true)
    private boolean done;

    @NotNull
    @ManyToOne
    private UserEntity user;

    @NotNull
    @ManyToOne
    private ProjectEntity project;

    public NoteEntity() {
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }
}
