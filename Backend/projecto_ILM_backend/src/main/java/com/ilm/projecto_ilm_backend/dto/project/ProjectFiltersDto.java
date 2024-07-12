package com.ilm.projecto_ilm_backend.dto.project;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for filtering projects.
 * This class encapsulates the criteria used to filter projects, including the lab locations,
 * project states, and types of users in the project.
 */
public class ProjectFiltersDto {
    /**
     * A list of lab locations to filter projects by.
     */
    private ArrayList<WorkLocalENUM> labs;
    /**
     * A list of project states to filter projects by.
     */
    private ArrayList<StateProjectENUM> states;
    /**
     * A list of user types in the project to filter projects by.
     */
    private ArrayList<UserInProjectTypeENUM> userTypesInProject;

    /**
     * Default constructor.
     */
    public ProjectFiltersDto() {
    }

    /**
     * Gets the list of lab locations used for filtering.
     *
     * @return A list of {@link WorkLocalENUM} representing the lab locations.
     */
    public ArrayList<WorkLocalENUM> getLabs() {
        return labs;
    }

    /**
     * Sets the list of lab locations used for filtering.
     *
     * @param labs A list of {@link WorkLocalENUM} representing the lab locations.
     */
    public void setLabs(ArrayList<WorkLocalENUM> labs) {
        this.labs = labs;
    }

    /**
     * Gets the list of project states used for filtering.
     *
     * @return A list of {@link StateProjectENUM} representing the project states.
     */
    public ArrayList<StateProjectENUM> getStates() {
        return states;
    }

    /**
     * Sets the list of project states used for filtering.
     *
     * @param states A list of {@link StateProjectENUM} representing the project states.
     */
    public void setStates(ArrayList<StateProjectENUM> states) {
        this.states = states;
    }

    /**
     * Gets the list of user types in the project used for filtering.
     *
     * @return A list of {@link UserInProjectTypeENUM} representing the user types in the project.
     */
    public ArrayList<UserInProjectTypeENUM> getUserTypesInProject() {
        return userTypesInProject;
    }

    /**
     * Sets the list of user types in the project used for filtering.
     *
     * @param userTypesInProject A list of {@link UserInProjectTypeENUM} representing the user types in the project.
     */
    public void setUserTypesInProject(ArrayList<UserInProjectTypeENUM> userTypesInProject) {
        this.userTypesInProject = userTypesInProject;
    }
}