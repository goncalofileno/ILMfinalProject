package com.ilm.projecto_ilm_backend.dto.resource;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;

import java.util.List;

public class ResourcesProjectProfileDto {
    private List<ResourceTableDto> resources;
    private UserInProjectTypeENUM userInProjectTypeENUM;

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
}
