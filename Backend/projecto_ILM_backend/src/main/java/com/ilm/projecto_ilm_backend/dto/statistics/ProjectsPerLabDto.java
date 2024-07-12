package com.ilm.projecto_ilm_backend.dto.statistics;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;

/**
 * Data Transfer Object for representing the number of projects per laboratory.
 * This class encapsulates the work location of a laboratory and the count of projects associated with it.
 */
public class ProjectsPerLabDto {
    /**
     * The work location of the laboratory, represented as an enum.
     */
    private WorkLocalENUM workLocal;

    /**
     * The number of projects associated with the laboratory.
     */
    private long projects;

    /**
     * Default constructor.
     */
    public ProjectsPerLabDto() {
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
     * Gets the number of projects associated with the laboratory.
     *
     * @return The number of projects.
     */
    public long getProjects() {
        return projects;
    }

    /**
     * Sets the number of projects associated with the laboratory.
     *
     * @param projects The number of projects to set.
     */
    public void setProjects(long projects) {
        this.projects = projects;
    }
}