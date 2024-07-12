package com.ilm.projecto_ilm_backend.dto.statistics;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;

/**
 * Data Transfer Object for representing the number of members per laboratory.
 * This class encapsulates the work location of a laboratory and the count of members associated with it.
 */
public class MembersPerLabDto {
    /**
     * The work location of the laboratory, represented as an enum.
     */
    private WorkLocalENUM workLocal;

    /**
     * The number of members associated with the laboratory.
     */
    private long members;

    /**
     * Default constructor.
     */
    public MembersPerLabDto() {
    }

    /**
     * Gets the work location of the laboratory.
     *
     * @return The work location as a {@link WorkLocalENUM}.
     */
    public WorkLocalENUM getWorkLocal() {
        return workLocal;
    }

    /**
     * Sets the work location of the laboratory.
     *
     * @param workLocal The work location to set, as a {@link WorkLocalENUM}.
     */
    public void setWorkLocal(WorkLocalENUM workLocal) {
        this.workLocal = workLocal;
    }

    /**
     * Gets the number of members associated with the laboratory.
     *
     * @return The number of members.
     */
    public long getMembers() {
        return members;
    }

    /**
     * Sets the number of members associated with the laboratory.
     *
     * @param members The number of members to set.
     */
    public void setMembers(long members) {
        this.members = members;
    }
}