package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * The ProjectResourceEntity class represents the "project_resource" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "project_resource")
// Query to delete a ProjectResourceEntity by its id.
@NamedQuery(
        name = "ProjectResourceEntity.deleteById",
        query = "DELETE FROM ProjectResourceEntity pr WHERE pr.id = :id"
)
// Query to find a ProjectResourceEntity by its id.
@NamedQuery(
        name = "ProjectResourceEntity.findById",
        query = "SELECT pr FROM ProjectResourceEntity pr WHERE pr.id = :id"
)
// Query to get a ProjectResourceEntity by the id of its associated project.
@NamedQuery(
        name = "ProjectResourceEntity.getByProjectId",
        query = "SELECT pr FROM ProjectResourceEntity pr WHERE pr.project.id = :id "
)

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
    @JoinColumn(name = "resourceSupplier_id")
    private ResourceSupplierEntity resource;

    /**
     * The stock of the resource in the project.
     */
    @Column(name = "stockRequested", nullable = false, unique = false, updatable = true)
    private int stockRequested;


    /**
     * The stock accepted of the resource in the project.
     */
    @Column(name = "stockAccepted", nullable = false, unique = false, updatable = true)
    private boolean stockAccepted;

    /**
     * Default constructor.
     */
    public ProjectResourceEntity() {
    }

    /**
     * Returns the unique ID of this project resource.
     *
     * @return the ID of this project resource.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this project resource.
     *
     * @param id the new ID of this project resource.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the project associated with this project resource.
     *
     * @return the project associated with this project resource.
     */
    public ProjectEntity getProject() {
        return project;
    }

    /**
     * Sets the project associated with this project resource.
     *
     * @param project the new project associated with this project resource.
     */
    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    /**
     * Returns the resource associated with this project resource.
     *
     * @return the resource associated with this project resource.
     */
    public ResourceSupplierEntity getResources() {
        return resource;
    }
    /**
     * Sets the resource associated with this project resource.
     *
     * @param resource the new resource associated with this project resource.
     */
    public void setResources(ResourceSupplierEntity resource) {
        this.resource = resource;
    }

    /**
     * Returns the stock of the resource in the project.
     *
     * @return the stock of the resource in the project.
     */
    public int getStockRequested() {
        return stockRequested;
    }

    /**
     * Sets the stock of the resource in the project.
     *
     * @param stockRequested the new stock of the resource in the project.
     */
    public void setStockRequested(int stockRequested) {
        this.stockRequested = stockRequested;
    }

}