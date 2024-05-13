package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="project_resource")
public class ProjectResourceEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name="project_id")
    private ProjectEntity project;

    @ManyToOne
    @JoinColumn(name="resource_id")
    private ResourceEntity resources;

    @Column(name="stock", nullable = false, unique = false, updatable = true)
    private int stock;

}
