package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="project")
public class ProjectEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

    @Column(name="title", nullable = false, unique = true, updatable = true)
    private String title;

    @Column(name="description", nullable = false, unique = false, updatable = true)
    private String description;

    @Column(name="startDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime startDate;

    @Column(name="endDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime endDate;

    @Column(name="status", nullable = false, unique = false, updatable = true)
    private StateProjectENUM status;

    @Column(name="motivation", nullable = false, unique = false, updatable = true)
    private String motivation;

    @Column(name="maxMembers", nullable = false, unique = false, updatable = true)
    private int maxMembers;

    @Column(name="photo", nullable = false, unique = false, updatable = true)
    private String photo;

    @Column(name="lab", nullable = false, unique = false, updatable = true)
    private int lab;

    @Column(name="deleted", nullable = false, unique = false, updatable = true)
    private boolean deleted;

    @OneToMany(mappedBy = "project")
    private List<ProjectResourceEntity> projectResources;

    @OneToMany(mappedBy = "project")
    private List<UserProjectEntity> userProjects;

    @ManyToMany
    private List<SkillEntity> skillInProject;

    public ProjectEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public StateProjectENUM getStatus() {
        return status;
    }

    public void setStatus(StateProjectENUM status) {
        this.status = status;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getLab() {
        return lab;
    }

    public void setLab(int lab) {
        this.lab = lab;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<ProjectResourceEntity> getProjectResources() {
        return projectResources;
    }

    public void setProjectResources(List<ProjectResourceEntity> projectResources) {
        this.projectResources = projectResources;
    }

    public List<UserProjectEntity> getUserProjects() {
        return userProjects;
    }

    public void setUserProjects(List<UserProjectEntity> userProjects) {
        this.userProjects = userProjects;
    }

    public List<SkillEntity> getSkillInProject() {
        return skillInProject;
    }

    public void setSkillInProject(List<SkillEntity> skillInProject) {
        this.skillInProject = skillInProject;
    }
}
