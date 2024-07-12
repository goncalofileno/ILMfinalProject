package com.ilm.projecto_ilm_backend.dto.project;

import java.util.ArrayList;

/**
 * Data Transfer Object for encapsulating pagination information of project tables.
 * This class holds the maximum page number and a list of {@link ProjectTableDto} objects
 * representing the projects to be displayed on a specific page of a table view.
 */
public class ProjectTableInfoDto {
    /**
     * The maximum number of pages available based on the total number of projects and the page size.
     */
    private int maxPageNumber;

    /**
     * A list of {@link ProjectTableDto} objects, each representing a project's information
     * to be displayed in a table format.
     */
    private ArrayList<ProjectTableDto> tableProjects;

    /**
     * Default constructor.
     */
    public ProjectTableInfoDto() {
    }

    /**
     * Gets the maximum page number available for pagination.
     *
     * @return The maximum page number.
     */
    public int getMaxPageNumber() {
        return maxPageNumber;
    }

    /**
     * Sets the maximum page number for pagination.
     *
     * @param maxPageNumber The maximum page number to set.
     */
    public void setMaxPageNumber(int maxPageNumber) {
        this.maxPageNumber = maxPageNumber;
    }

    /**
     * Gets the list of {@link ProjectTableDto} objects representing the projects.
     *
     * @return The list of projects.
     */
    public ArrayList<ProjectTableDto> getTableProjects() {
        return tableProjects;
    }

    /**
     * Sets the list of {@link ProjectTableDto} objects representing the projects.
     *
     * @param tableProjects The list of projects to set.
     */
    public void setTableProjects(ArrayList<ProjectTableDto> tableProjects) {
        this.tableProjects = tableProjects;
    }
}