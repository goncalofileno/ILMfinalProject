package com.ilm.projecto_ilm_backend.dto.user;

import java.util.List;

/**
 * Data Transfer Object for holding a list of rejected IDs.
 * This class is used to encapsulate a list of integer IDs that have been rejected for some operation,
 * allowing for easy transfer of this data between different layers of the application.
 */
public class RejectedIdsDto {
    // List of integer IDs that have been rejected
    List<Integer> rejectedIds;

    /**
     * Default constructor.
     */
    public RejectedIdsDto() {
    }

    /**
     * Gets the list of rejected IDs.
     *
     * @return A list of integer IDs that have been rejected.
     */
    public List<Integer> getRejectedIds() {
        return rejectedIds;
    }

    /**
     * Sets the list of rejected IDs.
     *
     * @param rejectedIds A list of integer IDs to be marked as rejected.
     */
    public void setRejectedIds(List<Integer> rejectedIds) {
        this.rejectedIds = rejectedIds;
    }
}