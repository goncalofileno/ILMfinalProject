package com.ilm.projecto_ilm_backend.dto.project;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for home project information.
 * This class encapsulates the basic details of a project, including its name and description.
 */
@XmlRootElement
public class HomeProjectDto {
    private String name; // The name of the project
    private String description; // A brief description of the project

    /**
     * Constructs a new HomeProjectDto with specified name and description.
     *
     * @param name The name of the project.
     * @param description A brief description of the project.
     */
    public HomeProjectDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Returns the name of the project.
     *
     * @return The name of the project.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project.
     *
     * @param name The new name of the project.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the project.
     *
     * @return The description of the project.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the project.
     *
     * @param description The new description of the project.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}