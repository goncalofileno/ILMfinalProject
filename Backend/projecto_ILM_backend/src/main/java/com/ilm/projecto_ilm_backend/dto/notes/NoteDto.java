package com.ilm.projecto_ilm_backend.dto.notes;

import java.time.LocalDateTime;

public class NoteDto {
    private int id;
    private String text;
    private LocalDateTime date;
    private boolean done;
    private String authorName;
    private String authorPhoto;
    private String taskSystemName;

    public NoteDto() {
    }

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
