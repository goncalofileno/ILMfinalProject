package com.ilm.projecto_ilm_backend.dto.user;

import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectProfileDto;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;

import java.util.List;

public class ShowProfileDto {
    private String username;
    private String firstName;
    private String lastName;
    private String location;
    private String profileImage;
    private String bio;
    private List<ProjectProfileDto> projects;
    private List<SkillDto> skills;
    private List<InterestDto> interests;
    private boolean publicProfile;

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

}
