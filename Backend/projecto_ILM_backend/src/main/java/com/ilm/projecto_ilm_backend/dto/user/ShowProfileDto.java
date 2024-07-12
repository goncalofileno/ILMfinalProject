package com.ilm.projecto_ilm_backend.dto.user;

import com.ilm.projecto_ilm_backend.ENUMS.UserTypeENUM;
import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectProfileDto;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;

import java.util.List;

/**
 * Data Transfer Object for displaying user profile information.
 * This class encapsulates user profile details including personal information,
 * projects, skills, interests, and privacy settings.
 */
public class ShowProfileDto {
    private String username; // Username of the user
    private String firstName; // First name of the user
    private String lastName; // Last name of the user
    private String location; // Location of the user
    private String profileImage; // URL to the user's profile image
    private String bio; // Biography of the user
    private String email; // Email address of the user
    private List<ProjectProfileDto> projects; // List of projects associated with the user
    private List<SkillDto> skills; // List of skills the user has
    private List<InterestDto> interests; // List of interests of the user
    private boolean publicProfile; // Flag indicating if the profile is public
    private UserTypeENUM userType; // Type of user (e.g., ADMIN, USER)

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isPublicProfile() {
        return publicProfile;
    }

    public void setPublicProfile(boolean publicProfile) {
        this.publicProfile = publicProfile;
    }

    public List<ProjectProfileDto> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectProfileDto> projects) {
        this.projects = projects;
    }

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }

    public List<InterestDto> getInterests() {
        return interests;
    }

    public void setInterests(List<InterestDto> interests) {
        this.interests = interests;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserTypeENUM getUserType() {
        return userType;
    }

    public void setUserType(UserTypeENUM userType) {
        this.userType = userType;
    }
}
