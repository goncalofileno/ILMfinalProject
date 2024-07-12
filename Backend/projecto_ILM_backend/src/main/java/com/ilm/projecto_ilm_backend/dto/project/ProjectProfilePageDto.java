package com.ilm.projecto_ilm_backend.dto.project;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserTypeENUM;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for the profile page of a project.
 * This class encapsulates comprehensive details about a project for display on its profile page,
 * including metadata, state, members, and associated skills.
 */
public class ProjectProfilePageDto {

    private String title; // Title of the project
    private StateProjectENUM state; // Current state of the project
    private String description; // Description of the project
    private LocalDateTime startDate; // Start date of the project
    private LocalDateTime endDate; // End date of the project
    private String photo; // URL or path to the project's photo
    private String lab; // Lab associated with the project
    private ProjectMemberDto creator; // Creator of the project
    private List<ProjectMemberDto> members; // List of project members
    private List<String> keywords; // Keywords associated with the project
    private List<SkillDto> skills; // Skills required for the project
    private List<StateProjectENUM> statesToChange; // Possible next states of the project
    private int progress; // Current progress of the project
    private int maxMembers; // Maximum number of members allowed in the project
    private UserInProjectTypeENUM typeOfUserSeingProject; // Type of user viewing the project
    private UserTypeENUM typeOfUser; // Type of user associated with the project
    private String reason; // Reason for the current state or action

    /**
     * Constructs a new ProjectProfilePageDto with specified details.
     *
     * @param title Title of the project.
     * @param state Current state of the project.
     * @param description Description of the project.
     * @param startDate Start date of the project.
     * @param endDate End date of the project.
     * @param photo URL or path to the project's photo.
     * @param lab Lab associated with the project.
     * @param creator Creator of the project.
     * @param members List of project members.
     * @param keywords Keywords associated with the project.
     * @param skills Skills required for the project.
     * @param statesToChange Possible next states of the project.
     * @param progress Current progress of the project.
     * @param maxMembers Maximum number of members allowed in the project.
     * @param typeOfUserSeingProject Type of user viewing the project.
     * @param typeOfUser Type of user associated with the project.
     * @param reason Reason for the current state or action.
     */
    public ProjectProfilePageDto(String title, StateProjectENUM state, String description, LocalDateTime startDate, LocalDateTime endDate, String photo, String lab, ProjectMemberDto creator, List<ProjectMemberDto> members, List<String> keywords, List<SkillDto> skills, List<StateProjectENUM> statesToChange, int progress, int maxMembers,UserTypeENUM typeOfUser, UserInProjectTypeENUM typeOfUserSeingProject, String reason) {
        this.title = title;
        this.state = state;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.photo = photo;
        this.lab = lab;
        this.creator = creator;
        this.members = members;
        this.keywords = keywords;
        this.skills = skills;
        this.statesToChange = statesToChange;
        this.progress = progress;
        this.maxMembers = maxMembers;
        this.typeOfUserSeingProject = typeOfUserSeingProject;
        this.typeOfUser = typeOfUser;
        this.reason = reason;
    }

    /**
     * Default constructor.
     */
    public ProjectProfilePageDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StateProjectENUM getState() {
        return state;
    }

    public void setState(StateProjectENUM state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public ProjectMemberDto getCreator() {
        return creator;
    }

    public void setCreator(ProjectMemberDto creator) {
        this.creator = creator;
    }

    public List<ProjectMemberDto> getMembers() {
        return members;
    }

    public void setMembers(List<ProjectMemberDto> members) {
        this.members = members;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }

    public List<StateProjectENUM> getStatesToChange() {
        return statesToChange;
    }

    public void setStatesToChange(List<StateProjectENUM> statesToChange) {
        this.statesToChange = statesToChange;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public UserInProjectTypeENUM getTypeOfUserSeingProject() {
        return typeOfUserSeingProject;
    }

    public void setTypeOfUserSeingProject(UserInProjectTypeENUM typeOfUserSeingProject) {
        this.typeOfUserSeingProject = typeOfUserSeingProject;
    }

    public UserTypeENUM getTypeOfUser() {
        return typeOfUser;
    }

    public void setTypeOfUser(UserTypeENUM typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
