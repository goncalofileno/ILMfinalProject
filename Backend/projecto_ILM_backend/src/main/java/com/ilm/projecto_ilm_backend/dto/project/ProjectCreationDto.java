package com.ilm.projecto_ilm_backend.dto.project;

import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;

import java.util.List;

/**
 * Data Transfer Object for creating a new project.
 * This class encapsulates all necessary information required to create a new project,
 * including its name, description, associated lab, required skills, interests (keywords),
 * motivation for the project, start and end dates, project photo, and the maximum number of members.
 */
public class ProjectCreationDto {

    private String name; // Name of the project
    private String description; // Description of the project
    private String lab; // Lab associated with the project
    private List<SkillDto> skills; // List of required skills for the project
    private List<InterestDto> keywords; // List of interests or keywords associated with the project
    private String motivation; // Motivation behind the project
    private String startDate; // Start date of the project
    private String endDate; // End date of the project
    private String photo; // URL or path to the project's photo
    private int maxMembers; // Maximum number of members allowed in the project

    /**
     * Default constructor.
     */
    public ProjectCreationDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }

    public List<InterestDto> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<InterestDto> keywords) {
        this.keywords = keywords;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }
}
