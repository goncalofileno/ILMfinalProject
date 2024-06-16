package com.ilm.projecto_ilm_backend.dto.project;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;

public class ProjectMemberDto {
    private String name;
    private String systemUsername;
    private UserInProjectTypeENUM type;

    public ProjectMemberDto(String name, String systemUsername, UserInProjectTypeENUM type) {
        this.name = name;
        this.systemUsername = systemUsername;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystemUsername() {
        return systemUsername;
    }

    public void setSystemUsername(String systemUsername) {
        this.systemUsername = systemUsername;
    }

    public UserInProjectTypeENUM getType() {
        return type;
    }

    public void setType(UserInProjectTypeENUM type) {
        this.type = type;
    }
}
