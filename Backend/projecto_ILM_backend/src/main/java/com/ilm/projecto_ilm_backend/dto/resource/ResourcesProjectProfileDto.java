package com.ilm.projecto_ilm_backend.dto.resource;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;

import java.util.List;

public class ResourcesProjectProfileDto {
    private List<ResourceTableDto> resources;
    private UserInProjectTypeENUM userInProjectTypeENUM;
    private String projectName;
    private String projectStatus;

    public ResourcesProjectProfileDto() {
    }

    public List<ResourceTableDto> getResources() {
        return resources;
    }

    public void setResources(List<ResourceTableDto> resources) {
        this.resources = resources;
    }

    public UserInProjectTypeENUM getUserInProjectTypeENUM() {
        return userInProjectTypeENUM;
    }

    public void setUserInProjectTypeENUM(UserInProjectTypeENUM userInProjectTypeENUM) {
        this.userInProjectTypeENUM = userInProjectTypeENUM;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }
}
