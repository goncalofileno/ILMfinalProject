package com.ilm.projecto_ilm_backend.dto.user;



import jakarta.xml.bind.annotation.XmlRootElement;
import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;

import java.io.File;
import java.util.List;

/**
 * Data Transfer Object (DTO) for a user's profile.
 * This class is used to encapsulate the data required for a user's profile,
 * specifically the user's first name, last name, username, lab, public profile status, bio, skills, interests, and profile picture.
 * <p>
 * It is annotated with {@link XmlRootElement} to indicate that it can be
 * converted to and from XML.
 * </p>
 */
@XmlRootElement
public class UserProfileDto {

    /**
     * The first name of the user.
     */
    private String firstName;
    /**
     * The last name of the user.
     */
    private String lastName;
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The lab of the user.
     */
    private String lab;
    /**
     * The public profile status of the user.
     */
    private boolean publicProfile;
    /**
     * The bio of the user.
     */
    private String bio;
    /**
     * The skills of the user.
     */
    private List<SkillDto> skills;
    /**
     * The interests of the user.
     */
    private List<InterestDto> interests;
    /**
     * The profile picture of the user.
     */
    private File profilePicture;

    /**
     * Default no-argument constructor.
     */
    public UserProfileDto() {
    }

    /**
     * Overloaded constructor.
     *
     * @param firstName        the first name of the user
     * @param lastName         the last name of the user
     * @param username         the username of the user
     * @param lab              the lab of the user
     * @param publicProfile  the public profile status of the user
     * @param bio              the bio of the user
     * @param skills           the skills of the user
     * @param interests        the interests of the user
     * @param profilePicture   the profile picture of the user
     */
    public UserProfileDto(String firstName, String lastName, String username, String lab, boolean publicProfile, String bio, List<SkillDto> skills, List<InterestDto> interests, File profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.lab = lab;
        this.publicProfile = publicProfile;
        this.bio = bio;
        this.skills = skills;
        this.interests = interests;
        this.profilePicture = profilePicture;
    }

    /**
     * Returns the first name of the user.
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the lab of the user.
     *
     * @return the lab of the user
     */
    public String getLab() {
        return lab;
    }

    /**
     * Sets the lab of the user.
     *
     * @param lab the lab to set
     */
    public void setLab(String lab) {
        this.lab = lab;
    }

    /**
     * Returns the public profile status of the user.
     *
     * @return the public profile status of the user
     */
    public boolean publicProfile() {
        return publicProfile;
    }

    /**
     * Sets the public profile status of the user.
     *
     * @param publicProfile the public profile status to set
     */
    public void setPublicProfile(boolean publicProfile) {
        publicProfile = publicProfile;
    }

    /**
     * Returns the bio of the user.
     *
     * @return the bio of the user
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the bio of the user.
     *
     * @param bio the bio to set
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Returns the skills of the user.
     *
     * @return the skills of the user
     */
    public List<SkillDto> getSkills() {
        return skills;
    }

    /**
     * Sets the skills of the user.
     *
     * @param skills the skills to set
     */
    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }

    /**
     * Returns the interests of the user.
     *
     * @return the interests of the user
     */
    public List<InterestDto> getInterests() {
        return interests;
    }

    /**
     * Sets the interests of the user.
     *
     * @param interests the interests to set
     */
    public void setInterests(List<InterestDto> interests) {
        this.interests = interests;
    }

    /**
     * Returns the profile picture of the user.
     *
     * @return the profile picture of the user
     */
    public File getProfilePicture() {
        return profilePicture;
    }

    /**
     * Sets the profile picture of the user.
     *
     * @param profilePicture the profile picture to set
     */
    public void setProfilePicture(File profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public String toString() {
        return "UserProfileDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", lab='" + lab + '\'' +
                ", publicProfile=" + publicProfile +
                ", bio='" + bio + '\'' +
                ", skills=" + skills +
                ", interests=" + interests +
                ", profilePicture=" + profilePicture +
                '}';
    }
}
