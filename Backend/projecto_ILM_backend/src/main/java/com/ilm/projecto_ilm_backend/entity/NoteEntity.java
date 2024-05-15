package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The NoteEntity class represents the "note" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name="note")
public class NoteEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the note. This is the primary key in the "note" table.
     */
    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

    /**
     * The text of the note.
     */
    @Column(name="text", nullable = false, unique = false, updatable = true)
    private String text;

    /**
     * The date of the note.
     */
    @Column(name="date", nullable = false, unique = false, updatable = true)
    private LocalDateTime date;

    /**
     * The done status of the note.
     */
    @Column(name="done", nullable = false, unique = false, updatable = true)
    private boolean done;

    /**
     * The user associated with the note. This is a many-to-one relationship with the UserEntity class.
     */
    @NotNull
    @ManyToOne
    private UserEntity user;

    /**
     * The project associated with the note. This is a many-to-one relationship with the ProjectEntity class.
     */
    @NotNull
    @ManyToOne
    private ProjectEntity project;

    /**
     * Default constructor.
     */
    public NoteEntity() {
    }

    /**
     * Returns the unique ID of this note.
     * @return the ID of this note.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this note.
     * @param id the new ID of this note.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the text of this note.
     * @return the text of this note.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of this note.
     * @param text the new text of this note.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the date of this note.
     * @return the date of this note.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the date of this note.
     * @param date the new date of this note.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Returns the done status of this note.
     * @return true if the note is done, false otherwise.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Sets the done status of this note.
     * @param done the new done status of this note.
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Returns the user associated with this note.
     * @return the user associated with this note.
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Sets the user associated with this note.
     * @param user the new user associated with this note.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Returns the project associated with this note.
     * @return the project associated with this note.
     */
    public ProjectEntity getProject() {
        return project;
    }

    /**
     * Sets the project associated with this note.
     * @param project the new project associated with this note.
     */
    public void setProject(ProjectEntity project) {
        this.project = project;
    }
}