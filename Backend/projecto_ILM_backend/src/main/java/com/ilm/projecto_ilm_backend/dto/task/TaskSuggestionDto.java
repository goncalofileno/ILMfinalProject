package com.ilm.projecto_ilm_backend.dto.task;

/**
 * Data Transfer Object for suggesting tasks.
 * This class encapsulates the minimal information required for suggesting a task,
 * including its title and system title.
 */
public class TaskSuggestionDto {

    /**
     * The title of the suggested task.
     */
    private String title;

    /**
     * The system title of the suggested task.
     * This may be used for system-level identification or categorization.
     */
    private String systemTitle;

    /**
     * Default constructor.
     */
    public TaskSuggestionDto() {
    }

    /**
     * Constructs a new TaskSuggestionDto with specified title and system title.
     *
     * @param title       The title of the suggested task.
     * @param systemTitle The system title of the suggested task.
     */
    public TaskSuggestionDto(String title, String systemTitle) {
        this.title = title;
        this.systemTitle = systemTitle;
    }

    /**
     * Gets the title of the suggested task.
     *
     * @return The title of the suggested task.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the suggested task.
     *
     * @param title The title to set for the suggested task.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the system title of the suggested task.
     *
     * @return The system title of the suggested task.
     */
    public String getSystemTitle() {
        return systemTitle;
    }

    /**
     * Sets the system title of the suggested task.
     *
     * @param systemTitle The system title to set for the suggested task.
     */
    public void setSystemTitle(String systemTitle) {
        this.systemTitle = systemTitle;
    }
}