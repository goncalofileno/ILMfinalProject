package com.ilm.projecto_ilm_backend.dto.user;

import java.util.List;

/**
 * Data Transfer Object for aggregating project creation information.
 * This class is designed to encapsulate a collection of {@link UserProjectCreationDto} instances,
 * providing a structured way to handle multiple project creation data transfers simultaneously.
 * It also includes pagination support through the {@code maxPageNumber} field, allowing for efficient
 * data handling and UI rendering in scenarios where project data is displayed across multiple pages.
 */
public class UserProjectCreationInfoDto {
    // Collection of UserProjectCreationDto instances
    private List<UserProjectCreationDto> userProjectCreationDtos;
    // Maximum page number for pagination purposes
    private int maxPageNumber;

    /**
     * Default constructor.
     */
    public UserProjectCreationInfoDto() {
    }

    /**
     * Retrieves the list of UserProjectCreationDto instances.
     *
     * @return A list of {@link UserProjectCreationDto} instances.
     */
    public List<UserProjectCreationDto> getUserProjectCreationDtos() {
        return userProjectCreationDtos;
    }

    /**
     * Sets the list of UserProjectCreationDto instances.
     *
     * @param userProjectCreationDtos A list of {@link UserProjectCreationDto} instances to be set.
     */
    public void setUserProjectCreationDtos(List<UserProjectCreationDto> userProjectCreationDtos) {
        this.userProjectCreationDtos = userProjectCreationDtos;
    }

    /**
     * Retrieves the maximum page number for pagination.
     *
     * @return The maximum page number.
     */
    public int getMaxPageNumber() {
        return maxPageNumber;
    }

    /**
     * Sets the maximum page number for pagination.
     *
     * @param maxPageNumber The maximum page number to be set.
     */
    public void setMaxPageNumber(int maxPageNumber) {
        this.maxPageNumber = maxPageNumber;
    }
}