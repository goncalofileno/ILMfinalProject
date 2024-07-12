package com.ilm.projecto_ilm_backend.dto.project;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Data Transfer Object for managing project creation members.
 * This class encapsulates the list of user IDs that are part of a project and the maximum number of members allowed.
 */
@XmlRootElement
public class ProjectCreationMembersDto {
    /**
     * A list of user IDs representing the users included in the project.
     */
    private ArrayList<Integer> usersInProject;

    /**
     * The maximum number of members allowed in the project.
     */
    private int maxMembers;

    /**
     * Default constructor.
     */
    public ProjectCreationMembersDto() {
    }

    /**
     * Returns the list of user IDs in the project.
     *
     * @return An ArrayList of Integer representing the user IDs in the project.
     */
    public ArrayList<Integer> getUsersInProject() {
        return usersInProject;
    }

    /**
     * Sets the list of user IDs for the project.
     *
     * @param usersInProject An ArrayList of Integer representing the user IDs to be included in the project.
     */
    public void setUsersInProject(ArrayList<Integer> usersInProject) {
        this.usersInProject = usersInProject;
    }

    /**
     * Returns the maximum number of members allowed in the project.
     *
     * @return The maximum number of members.
     */
    public int getMaxMembers() {
        return maxMembers;
    }

    /**
     * Sets the maximum number of members allowed in the project.
     *
     * @param maxMembers The maximum number of members to be set.
     */
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }
}