package com.ilm.projecto_ilm_backend.dto.project;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;

public class ProjectMemberDto {
    private int id;
    private String name;
    private String systemUsername;
    private UserInProjectTypeENUM type;
    private String profilePicture;

    public ProjectMemberDto(int id, String name, String systemUsername, UserInProjectTypeENUM type, String profilePicture) {
        this.id = id;
        this.name = name;
        this.systemUsername = systemUsername;
        this.type = type;
        this.profilePicture = profilePicture;
    }

    public ProjectMemberDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
