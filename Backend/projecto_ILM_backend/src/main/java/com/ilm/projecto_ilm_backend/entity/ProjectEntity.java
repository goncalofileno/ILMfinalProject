package com.ilm.projecto_ilm_backend.entity;

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
    private int status;

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
}
