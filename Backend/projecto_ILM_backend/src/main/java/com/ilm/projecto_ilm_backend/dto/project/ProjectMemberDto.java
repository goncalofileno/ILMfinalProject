package com.ilm.projecto_ilm_backend.dto.project;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;

import java.util.List;

/**
 * Data Transfer Object for project member details.
 * This class encapsulates the information of a member associated with a project,
 * including their ID, name, system username, type (role in the project), and profile picture URL.
 */
public class ProjectMemberDto {
    /**
     * The unique identifier of the project member.
     */
    private int id;

    /**
     * The name of the project member.
     */
    private String name;

    /**
     * The system username of the project member.
     */
    private String systemUsername;

    /**
     * The type (role) of the project member within the project, as defined by {@link UserInProjectTypeENUM}.
     */
    private UserInProjectTypeENUM type;

    /**
     * The URL or path to the profile picture of the project member.
     */
    private String profilePicture;

    /**
     * Constructs a new ProjectMemberDto with specified details.
     *
     * @param id The unique identifier of the project member.
     * @param name The name of the project member.
     * @param systemUsername The system username of the project member.
     * @param type The type (role) of the project member within the project.
     * @param profilePicture The URL or path to the profile picture of the project member.
     */
    public ProjectMemberDto(int id, String name, String systemUsername, UserInProjectTypeENUM type, String profilePicture) {
        this.id = id;
        this.name = name;
        this.systemUsername = systemUsername;
        this.type = type;
        this.profilePicture = profilePicture;
    }

    /**
     * Default constructor.
     */
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
