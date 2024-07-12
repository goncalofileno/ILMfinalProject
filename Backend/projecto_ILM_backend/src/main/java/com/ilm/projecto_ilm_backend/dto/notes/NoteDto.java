package com.ilm.projecto_ilm_backend.dto.notes;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for notes.
 * This class encapsulates the details of a note, including its unique identifier, text content,
 * creation date, completion status, author's name, author's photo URL, and the associated task system name.
 */
public class NoteDto {
    private int id; // Unique identifier of the note
    private String text; // Text content of the note
    private LocalDateTime date; // Date and time the note was created
    private boolean done; // Completion status of the note
    private String authorName; // Name of the note's author
    private String authorPhoto; // URL to the author's photo
    private String taskSystemName; // Name of the task system associated with the note

    /**
     * Default constructor.
     */
    public NoteDto() {
    }

    /**
     * Constructs a new NoteDto with specified details.
     *
     * @param id The unique identifier of the note.
     * @param text The text content of the note.
     * @param date The date and time the note was created.
     * @param done The completion status of the note.
     * @param authorName The name of the note's author.
     * @param authorPhoto The URL to the author's photo.
     * @param taskSystemName The name of the task system associated with the note.
     */
    public NoteDto(int id, String text, LocalDateTime date, boolean done, String authorName, String authorPhoto, String taskSystemName) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.done = done;
        this.authorName = authorName;
        this.authorPhoto = authorPhoto;
        this.taskSystemName = taskSystemName;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
    }

    public String getTaskSystemName() {
        return taskSystemName;
    }

    public void setTaskSystemName(String taskSystemName) {
        this.taskSystemName = taskSystemName;
    }
}
