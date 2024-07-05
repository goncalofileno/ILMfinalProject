package com.ilm.projecto_ilm_backend.dto.statistics;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;

public class ProjectsPerLabDto {
    private WorkLocalENUM workLocal;
    private long projects;

    public ProjectsPerLabDto() {
    }

    public WorkLocalENUM getWorkLocal() {
        return workLocal;
    }

    public void setWorkLocal(WorkLocalENUM workLocal) {
        this.workLocal = workLocal;
    }

    public long getProjects() {
        return projects;
    }

    public void setProjects(long projects) {
        this.projects = projects;
    }
}
