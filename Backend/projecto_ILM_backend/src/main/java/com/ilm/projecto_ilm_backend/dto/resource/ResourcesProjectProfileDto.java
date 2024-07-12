package com.ilm.projecto_ilm_backend.dto.resource;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;

import java.util.List;

/**
 * Data Transfer Object for representing resources associated with a project profile.
 * This class encapsulates the list of resources, the user's role in the project, the project's name, and its status.
 */
public class ResourcesProjectProfileDto {
    /**
     * List of resources associated with the project.
     */
    private List<ResourceTableDto> resources;

    /**
     * The role of the user within the project.
     */
    private UserInProjectTypeENUM userInProjectTypeENUM;

    /**
     * The name of the project.
     */
    private String projectName;

    /**
     * The current status of the project.
     */
    private String projectStatus;

    /**
     * Default constructor.
     */
    public ResourcesProjectProfileDto() {
    }

    /**
     * Gets the list of resources associated with the project.
     *
     * @return A list of {@link ResourceTableDto} objects.
     */
    public List<ResourceTableDto> getResources() {
        return resources;
    }

    /**
     * Sets the list of resources associated with the project.
     *
     * @param resources A list of {@link ResourceTableDto} objects to be associated with the project.
     */
    public void setResources(List<ResourceTableDto> resources) {
        this.resources = resources;
    }

    /**
     * Gets the role of the user within the project.
     *
     * @return The user's role within the project as a {@link UserInProjectTypeENUM}.
     */
    public UserInProjectTypeENUM getUserInProjectTypeENUM() {
        return userInProjectTypeENUM;
    }

    /**
     * Sets the role of the user within the project.
     *
     * @param userInProjectTypeENUM The user's role within the project to set.
     */
    public void setUserInProjectTypeENUM(UserInProjectTypeENUM userInProjectTypeENUM) {
        this.userInProjectTypeENUM = userInProjectTypeENUM;
    }

    /**
     * Gets the name of the project.
     *
     * @return The name of the project.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the name of the project.
     *
     * @param projectName The name of the project to set.
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Gets the current status of the project.
     *
     * @return The current status of the project.
     */
    public String getProjectStatus() {
        return projectStatus;
    }

    /**
     * Sets the current status of the project.
     *
     * @param projectStatus The current status of the project to set.
     */
    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }
}