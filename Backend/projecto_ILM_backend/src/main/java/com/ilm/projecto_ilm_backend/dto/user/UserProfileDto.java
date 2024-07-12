package com.ilm.projecto_ilm_backend.dto.user;

import com.ilm.projecto_ilm_backend.ENUMS.UserTypeENUM;
import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Represents the user profile data transfer object.
 * This class is used to encapsulate all relevant information about a user's profile,
 * including personal details, skills, and interests. It is annotated with {@link XmlRootElement}
 * to indicate that it can be converted to and from XML.
 */
@XmlRootElement
public class UserProfileDto {
    private String photo; // URL to the user's profile photo
    private String firstName; // User's first name
    private String lastName; // User's last name
    private String username; // User's username
    private String lab; // Laboratory or research group the user is associated with
    private boolean publicProfile; // Flag indicating if the profile is public
    private String bio; // User's biography
    private List<SkillDto> skills; // List of user's skills
    private List<InterestDto> interests; // List of user's interests

    // Getters and Setters

    /**
     * Gets the URL to the user's profile photo.
     * @return The URL to the profile photo.
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the URL to the user's profile photo.
     * @param photo The URL to set for the profile photo.
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Gets the first name of the user.
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     * @param firstName The first name to set for the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     * @param lastName The last name to set for the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the username of the user.
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     * @param username The username to set for the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the laboratory or research group the user is associated with.
     * @return The laboratory or research group.
     */
    public String getLab() {
        return lab;
    }

    /**
     * Sets the laboratory or research group the user is associated with.
     * @param lab The laboratory or research group to set.
     */
    public void setLab(String lab) {
        this.lab = lab;
    }

    /**
     * Checks if the user's profile is public.
     * @return True if the profile is public, false otherwise.
     */
    public boolean isPublicProfile() {
        return publicProfile;
    }

    /**
     * Sets the user's profile to public or private.
     * @param publicProfile True to make the profile public, false to make it private.
     */
    public void setPublicProfile(boolean publicProfile) {
        this.publicProfile = publicProfile;
    }

    /**
     * Gets the biography of the user.
     * @return The biography of the user.
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the biography of the user.
     * @param bio The biography to set for the user.
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Gets the list of skills the user has.
     * @return A list of skills.
     */
    public List<SkillDto> getSkills() {
        return skills;
    }

    /**
     * Sets the list of skills the user has.
     * @param skills The list of skills to set.
     */
    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }

    /**
     * Gets the list of interests of the user.
     * @return A list of interests.
     */
    public List<InterestDto> getInterests() {
        return interests;
    }

    /**
     * Sets the list of interests of the user.
     * @param interests The list of interests to set.
     */
    public void setInterests(List<InterestDto> interests) {
        this.interests = interests;
    }
}