package com.ilm.projecto_ilm_backend.dto.statistics;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;

import java.util.ArrayList;

/**
 * Represents the number of projects in various states for a specific laboratory.
 * This class encapsulates the laboratory's name and a list of status and number pairs,
 * indicating the count of projects in each state.
 */
public class ProjectsStatusNumberPerLab {
    private String lab; // The name of the laboratory
    private ArrayList<StatusNumberDto> statusNumber; // List of status and number pairs for the laboratory

    /**
     * Constructs a new instance with a specified laboratory name.
     * Initializes the list of status and number pairs as empty.
     *
     * @param lab The name of the laboratory.
     */
    public ProjectsStatusNumberPerLab(String lab) {
        this.lab = lab;
        this.statusNumber = new ArrayList<>();
    }

    /**
     * Gets the name of the laboratory.
     *
     * @return The laboratory name.
     */
    public String getLab() {
        return lab;
    }

    /**
     * Sets the name of the laboratory.
     *
     * @param lab The laboratory name to set.
     */
    public void setLab(String lab) {
        this.lab = lab;
    }

    /**
     * Gets the list of status and number pairs for the laboratory.
     *
     * @return The list of {@link StatusNumberDto} objects.
     */
    public ArrayList<StatusNumberDto> getStatusNumber() {
        return statusNumber;
    }

    /**
     * Sets the list of status and number pairs for the laboratory.
     *
     * @param statusNumber The list of {@link StatusNumberDto} objects to set.
     */
    public void setStatusNumber(ArrayList<StatusNumberDto> statusNumber) {
        this.statusNumber = statusNumber;
    }

    /**
     * Adds a status and number pair to the list for the laboratory.
     *
     * @param statusNumberDto The {@link StatusNumberDto} object to add.
     */
    public void addStatusNumber(StatusNumberDto statusNumberDto) {
        statusNumber.add(statusNumberDto);
    }

    /**
     * Updates the number of projects for a specific project state.
     * If the state exists in the list, its count is updated; otherwise, no action is taken.
     *
     * @param numberOfProjects The number of projects to set.
     * @param projectState The state of the projects.
     */
    public void setStatusNumber(long numberOfProjects, StateProjectENUM projectState) {
        for (StatusNumberDto statusNumberDto : statusNumber) {
            if (statusNumberDto.getStatus().equals(projectState)) {
                statusNumberDto.setNumber(numberOfProjects);
                return;
            }
        }
    }
}