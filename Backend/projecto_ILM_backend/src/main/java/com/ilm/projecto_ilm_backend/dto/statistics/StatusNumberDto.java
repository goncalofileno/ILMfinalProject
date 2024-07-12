package com.ilm.projecto_ilm_backend.dto.statistics;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;

/**
 * Represents a status and its associated number for a project.
 * This DTO is used to encapsulate the state of a project along with a numerical value,
 * typically representing the count of projects in that state.
 */
public class StatusNumberDto {
    /**
     * The status of the project, represented as an enum.
     */
    private StateProjectENUM status;

    /**
     * The number associated with the status, typically representing a count.
     */
    private long number;

    /**
     * Constructs a new StatusNumberDto with specified status and number.
     *
     * @param status The status of the project.
     * @param number The number associated with the status.
     */
    public StatusNumberDto(StateProjectENUM status, long number) {
        this.status = status;
        this.number = number;
    }

    /**
     * Gets the status of the project.
     *
     * @return The status of the project.
     */
    public StateProjectENUM getStatus() {
        return status;
    }

    /**
     * Sets the status of the project.
     *
     * @param status The status to set.
     */
    public void setStatus(StateProjectENUM status) {
        this.status = status;
    }

    /**
     * Gets the number associated with the status.
     *
     * @return The number associated with the status.
     */
    public long getNumber() {
        return number;
    }

    /**
     * Sets the number associated with the status.
     *
     * @param number The number to set.
     */
    public void setNumber(long number) {
        this.number = number;
    }
}