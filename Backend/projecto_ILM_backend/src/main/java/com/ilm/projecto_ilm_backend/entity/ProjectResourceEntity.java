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
    @Column(name = "stockRequested", nullable = false, unique = false, updatable = true)
    private int stockRequested;

    /**
     * The real stock of the resource in the project.
     */
    @Column(name = "realStock", nullable = false, unique = false, updatable = true)
    private int realStock;

    /**
     * The stock accepted of the resource in the project.
     */
    @Column(name = "stockAcepeted", nullable = false, unique = false, updatable = true)
    private boolean stockAcepeted;

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
    public ResourceEntity getResources() {
        return resources;
    }

    /**
     * Sets the resource associated with this project resource.
     *
     * @param resources the new resource associated with this project resource.
     */
    public void setResources(ResourceEntity resources) {
        this.resources = resources;
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

    /**
     * Returns the real stock of the resource in the project.
     *
     * @return the real stock of the resource in the project.
     */
    public int getRealStock() {
        return realStock;
    }

    /**
     * Sets the real stock of the resource in the project.
     *
     * @param realStock the new real stock of the resource in the project.
     */
    public void setRealStock(int realStock) {
        this.realStock = realStock;
    }

    /**
     * Returns the stock accepted of the resource in the project.
     *
     * @return the stock accepted of the resource in the project.
     */
    public boolean isStockAcepeted() {
        return stockAcepeted;
    }

    /**
     * Sets the stock accepted of the resource in the project.
     *
     * @param stockAcepeted the new stock accepted of the resource in the project.
     */
    public void setStockAcepeted(boolean stockAcepeted) {
        this.stockAcepeted = stockAcepeted;
    }
}
