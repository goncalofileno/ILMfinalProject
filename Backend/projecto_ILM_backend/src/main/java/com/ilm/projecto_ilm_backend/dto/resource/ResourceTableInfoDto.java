package com.ilm.projecto_ilm_backend.dto.resource;

import com.ilm.projecto_ilm_backend.dto.project.ProjectTableDto;

import java.util.ArrayList;

/**
 * Data Transfer Object for providing paginated resource table information.
 * This class encapsulates the data necessary for displaying resources in a paginated table view,
 * including the maximum number of pages available and a list of resources for the current page.
 */
public class ResourceTableInfoDto {

    private int maxPageNumber; // The maximum number of pages available for pagination
    private ArrayList<ResourceTableDto> tableResources; // List of resources for the current page

    /**
     * Default constructor.
     */
    public ResourceTableInfoDto() {
    }

    /**
     * Gets the maximum number of pages available.
     *
     * @return The maximum number of pages.
     */
    public int getMaxPageNumber() {
        return maxPageNumber;
    }

    /**
     * Sets the maximum number of pages available.
     *
     * @param maxPageNumber The maximum number of pages to set.
     */
    public void setMaxPageNumber(int maxPageNumber) {
        this.maxPageNumber = maxPageNumber;
    }

    /**
     * Gets the list of resources for the current page.
     *
     * @return A list of {@link ResourceTableDto} objects.
     */
    public ArrayList<ResourceTableDto> getTableResources() {
        return tableResources;
    }

    /**
     * Sets the list of resources for the current page.
     *
     * @param tableResources A list of {@link ResourceTableDto} objects to set.
     */
    public void setTableResources(ArrayList<ResourceTableDto> tableResources) {
        this.tableResources = tableResources;
    }
}