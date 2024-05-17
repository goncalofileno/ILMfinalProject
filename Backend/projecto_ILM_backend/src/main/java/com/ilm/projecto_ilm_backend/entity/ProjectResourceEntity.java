package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * The ProjectResourceEntity class represents the "project_resource" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "project_resource")
public class ProjectResourceEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the project resource. This is the primary key in the "project_resource" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The project associated with the resource. This is a many-to-one relationship with the ProjectEntity class.
     */
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    /**
     * The resource associated with the project. This is a many-to-one relationship with the ResourceEntity class.
     */
    @ManyToOne
    @JoinColumn(name = "resource_id")
    private ResourceEntity resources;

    /**
     * The stock of the resource in the project.
     */
    @Column(name = "stock", nullable = false, unique = false, updatable = true)
    private int stock;

    /**
     * Default constructor.
     */
    public ProjectResourceEntity() {
    }

}
