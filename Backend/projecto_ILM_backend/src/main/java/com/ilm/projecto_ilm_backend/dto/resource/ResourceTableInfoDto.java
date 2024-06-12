package com.ilm.projecto_ilm_backend.dto.resource;

import com.ilm.projecto_ilm_backend.dto.project.ProjectTableDto;

import java.util.ArrayList;

public class ResourceTableInfoDto {

    private int maxPageNumber;
    private ArrayList<ResourceTableDto> tableResources;

    public ResourceTableInfoDto() {
    }

    public int getMaxPageNumber() {
        return maxPageNumber;
    }

    public void setMaxPageNumber(int maxPageNumber) {
        this.maxPageNumber = maxPageNumber;
    }

    public ArrayList<ResourceTableDto> getTableResources() {
        return tableResources;
    }

    public void setTableResources(ArrayList<ResourceTableDto> tableResources) {
        this.tableResources = tableResources;
    }
}
