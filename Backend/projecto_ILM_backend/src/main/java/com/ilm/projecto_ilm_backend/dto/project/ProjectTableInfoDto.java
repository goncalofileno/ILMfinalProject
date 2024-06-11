package com.ilm.projecto_ilm_backend.dto.project;

import java.util.ArrayList;

public class ProjectTableInfoDto {
    private int maxPageNumber;
    private ArrayList<ProjectTableDto> tableProjects;

    public ProjectTableInfoDto() {
    }

    public int getMaxPageNumber() {
        return maxPageNumber;
    }

    public void setMaxPageNumber(int maxPageNumber) {
        this.maxPageNumber = maxPageNumber;
    }

    public ArrayList<ProjectTableDto> getTableProjects() {
        return tableProjects;
    }

    public void setTableProjects(ArrayList<ProjectTableDto> tableProjects) {
        this.tableProjects = tableProjects;
    }
}
