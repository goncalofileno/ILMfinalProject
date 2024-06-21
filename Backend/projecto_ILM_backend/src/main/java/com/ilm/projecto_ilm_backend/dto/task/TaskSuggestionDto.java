package com.ilm.projecto_ilm_backend.dto.task;

public class TaskSuggestionDto {

    private String title;
    private String systemTitle;

    public TaskSuggestionDto() {
    }

    public TaskSuggestionDto(String title, String systemTitle) {
        this.title = title;
        this.systemTitle = systemTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSystemTitle() {
        return systemTitle;
    }

    public void setSystemTitle(String systemTitle) {
        this.systemTitle = systemTitle;
    }
}
