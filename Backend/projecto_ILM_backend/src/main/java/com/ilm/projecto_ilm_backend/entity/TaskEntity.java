package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="task")
public class TaskEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

    @Column(name="title", nullable = false, unique = false, updatable = true)
    private String title;

    @Column(name="description", nullable = false, unique = false, updatable = true)
    private String description;

    @Column(name="status", nullable = false, unique = false, updatable = true)
    private int status;

    @Column(name="initialDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime initialDate;

    @Column(name="finalDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime finalDate;

    @Column(name="outColaboration", nullable = false, unique = false, updatable = true)
    private String outColaboration;

    @ManyToMany
    private List<TaskEntity> dependentTasks;

    @OneToMany(mappedBy = "task")
    private List<UserTaskEntity> userTasks;

    @ManyToOne
    private ProjectEntity project;

    public TaskEntity() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(LocalDateTime initialDate) {
        this.initialDate = initialDate;
    }

    public LocalDateTime getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDateTime finalDate) {
        this.finalDate = finalDate;
    }

    public String getOutColaboration() {
        return outColaboration;
    }

    public void setOutColaboration(String outColaboration) {
        this.outColaboration = outColaboration;
    }

    public List<TaskEntity> getDependentTasks() {
        return dependentTasks;
    }

    public void setDependentTasks(List<TaskEntity> dependentTasks) {
        this.dependentTasks = dependentTasks;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }
}
