package com.ilm.projecto_ilm_backend.dto.project;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for the profile of a project.
 * This class encapsulates detailed information about a project, including its name, system name,
 * member type, status, and creation date.
 */
@XmlRootElement
public class ProjectProfileDto {

    private String name; // The name of the project
    private String systemName; // The system name of the project
    private String typeMember; // The type of member associated with the project
    private String status; // The current status of the project
    private LocalDateTime createdDate; // The date and time when the project was created

    /**
     * Default constructor.
     */
    public ProjectProfileDto() {
    }

    // Getters and Setters

    /**
     * Gets the name of the project.
     *
     * @return The name of the project.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project.
     *
     * @param name The name to set for the project.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the system name of the project.
     *
     * @return The system name of the project.
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * Sets the system name of the project.
     *
     * @param systemName The system name to set for the project.
     */
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    /**
     * Gets the type of member associated with the project.
     *
     * @return The type of member.
     */
    public String getTypeMember() {
        return typeMember;
    }

    /**
     * Sets the type of member associated with the project.
     *
     * @param typeMember The type of member to set.
     */
    public void setTypeMember(String typeMember) {
        this.typeMember = typeMember;
    }

    /**
     * Gets the current status of the project.
     *
     * @return The current status of the project.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the project.
     *
     * @param status The status to set for the project.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the creation date and time of the project.
     *
     * @return The creation date and time of the project.
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the creation date and time of the project.
     *
     * @param createdDate The creation date and time to set for the project.
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

}