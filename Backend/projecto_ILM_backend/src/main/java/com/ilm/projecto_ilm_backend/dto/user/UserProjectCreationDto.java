package com.ilm.projecto_ilm_backend.dto.user;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Data Transfer Object for creating a user project.
 * This class encapsulates all necessary information to create a new project associated with a user,
 * including project details, skills required, and privacy settings.
 */
@XmlRootElement
public class UserProjectCreationDto {
    private WorkLocalENUM lab; // The work location or lab associated with the project
    private String name; // The name of the project
    private String photo; // URL to the project's photo or image
    private List<SkillDto> skills; // List of skills required for the project
    private int id; // The unique identifier for the project
    private String systemUsername; // The system username of the user creating the project
    private boolean publicProfile; // Flag indicating if the project is public
    private String email; // The email address of the user creating the project

    /**
     * Default constructor.
     */
    public UserProjectCreationDto() {
    }

    // Getters and Setters

    /**
     * Gets the work location or lab associated with the project.
     * @return The work location or lab.
     */
    public WorkLocalENUM getLab() {
        return lab;
    }

    /**
     * Sets the work location or lab associated with the project.
     * @param lab The work location or lab to set.
     */
    public void setLab(WorkLocalENUM lab) {
        this.lab = lab;
    }

    /**
     * Gets the name of the project.
     * @return The name of the project.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project.
     * @param name The name to set for the project.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the URL to the project's photo or image.
     * @return The URL to the photo or image.
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the URL to the project's photo or image.
     * @param photo The URL to set for the photo or image.
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Gets the list of skills required for the project.
     * @return A list of skills.
     */
    public List<SkillDto> getSkills() {
        return skills;
    }

    /**
     * Sets the list of skills required for the project.
     * @param skills The list of skills to set.
     */
    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }

    /**
     * Gets the unique identifier for the project.
     * @return The unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the project.
     * @param id The unique identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the system username of the user creating the project.
     * @return The system username.
     */
    public String getSystemUsername() {
        return systemUsername;
    }

    /**
     * Sets the system username of the user creating the project.
     * @param systemUsername The system username to set.
     */
    public void setSystemUsername(String systemUsername) {
        this.systemUsername = systemUsername;
    }

    /**
     * Checks if the project is public.
     * @return True if the project is public, false otherwise.
     */
    public boolean isPublicProfile() {
        return publicProfile;
    }

    /**
     * Sets the project's privacy setting.
     * @param publicProfile True to make the project public, false to keep it private.
     */
    public void setPublicProfile(boolean publicProfile) {
        this.publicProfile = publicProfile;
    }

    /**
     * Gets the email address of the user creating the project.
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user creating the project.
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}