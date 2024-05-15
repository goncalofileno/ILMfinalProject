package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The ProjectEntity class represents the "project" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name="project")
public class ProjectEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the project. This is the primary key in the "project" table.
     */
    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

    /**
     * The title of the project.
     */
    @Column(name="title", nullable = false, unique = true, updatable = true)
    private String title;

    /**
     * The description of the project.
     */
    @Column(name="description", nullable = false, unique = false, updatable = true)
    private String description;

    /**
     * The start date of the project.
     */
    @Column(name="startDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime startDate;

    /**
     * The end date of the project.
     */
    @Column(name="endDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime endDate;

    /**
     * The status of the project. This is an enumerated type.
     */
    @Column(name="status", nullable = false, unique = false, updatable = true)
    private StateProjectENUM status;

    /**
     * The motivation of the project.
     */
    @Column(name="motivation", nullable = false, unique = false, updatable = true)
    private String motivation;

    /**
     * The maximum number of members in the project.
     */
    @Column(name="maxMembers", nullable = false, unique = false, updatable = true)
    private int maxMembers;

    /**
     * The photo of the project.
     */
    @Column(name="photo", nullable = false, unique = false, updatable = true)
    private String photo;

    /**
     * The lab associated with the project. This is a many-to-one relationship with the LabEntity class.
     */
    @ManyToOne
    private LabEntity lab;

    /**
     * The deleted status of the project.
     */
    @Column(name="deleted", nullable = false, unique = false, updatable = true)
    private boolean deleted;

    /**
     * The resources associated with the project. This is a one-to-many relationship with the ProjectResourceEntity class.
     */
    @OneToMany(mappedBy = "project")
    private List<ProjectResourceEntity> projectResources;

    /**
     * The users associated with the project. This is a one-to-many relationship with the UserProjectEntity class.
     */
    @OneToMany(mappedBy = "project")
    private List<UserProjectEntity> userProjects;

    /**
     * The skills associated with the project. This is a many-to-many relationship with the SkillEntity class.
     */
    @ManyToMany
    private List<SkillEntity> skillInProject;

    /**
     * Default constructor.
     */
    public ProjectEntity() {
    }

    /**
     * Returns the unique ID of this project.
     * @return the ID of this project.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this project.
     * @param id the new ID of this project.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the title of this project.
     * @return the title of this project.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this project.
     * @param title the new title of this project.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description of this project.
     * @return the description of this project.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this project.
     * @param description the new description of this project.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the start date of this project.
     * @return the start date of this project.
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of this project.
     * @param startDate the new start date of this project.
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date of this project.
     * @return the end date of this project.
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of this project.
     * @param endDate the new end date of this project.
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the status of this project.
     * @return the status of this project.
     */
    public StateProjectENUM getStatus() {
        return status;
    }

    /**
     * Sets the status of this project.
     * @param status the new status of this project.
     */
    public void setStatus(StateProjectENUM status) {
        this.status = status;
    }

    /**
     * Returns the motivation of this project.
     * @return the motivation of this project.
     */
    public String getMotivation() {
        return motivation;
    }

    /**
     * Sets the motivation of this project.
     * @param motivation the new motivation of this project.
     */
    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    /**
     * Returns the maximum number of members in this project.
     * @return the maximum number of members in this project.
     */
    public int getMaxMembers() {
        return maxMembers;
    }

    /**
     * Sets the maximum number of members in this project.
     * @param maxMembers the new maximum number of members in this project.
     */
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    /**
     * Returns the photo of this project.
     * @return the photo of this project.
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the photo of this project.
     * @param photo the new photo of this project.
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Returns the lab associated with this project.
     * @return the lab associated with this project.
     */
    public LabEntity getLab() {
        return lab;
    }

    /**
     * Sets the lab associated with this project.
     * @param lab the new lab associated with this project.
     */
    public void setLab(LabEntity lab) {
        this.lab = lab;
    }

    /**
     * Returns the deleted status of this project.
     * @return true if the project is deleted, false otherwise.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted status of this project.
     * @param deleted the new deleted status of this project.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Returns the resources associated with this project.
     * @return the resources associated with this project.
     */
    public List<ProjectResourceEntity> getProjectResources() {
        return projectResources;
    }

    /**
     * Sets the resources associated with this project.
     * @param projectResources the new resources associated with this project.
     */
    public void setProjectResources(List<ProjectResourceEntity> projectResources) {
        this.projectResources = projectResources;
    }

    /**
     * Returns the users associated with this project.
     * @return the users associated with this project.
     */
    public List<UserProjectEntity> getUserProjects() {
        return userProjects;
    }

    /**
     * Sets the users associated with this project.
     * @param userProjects the new users associated with this project.
     */
    public void setUserProjects(List<UserProjectEntity> userProjects) {
        this.userProjects = userProjects;
    }

    /**
     * Returns the skills associated with this project.
     * @return the skills associated with this project.
     */
    public List<SkillEntity> getSkillInProject() {
        return skillInProject;
    }

    /**
     * Sets the skills associated with this project.
     * @param skillInProject the new skills associated with this project.
     */
    public void setSkillInProject(List<SkillEntity> skillInProject) {
        this.skillInProject = skillInProject;
    }
}
