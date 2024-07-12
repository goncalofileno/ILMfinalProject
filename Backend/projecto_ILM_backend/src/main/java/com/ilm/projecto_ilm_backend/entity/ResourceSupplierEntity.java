package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "resource_supplier")
// Query to find the session id of a SessionEntity by the id of its associated user.
@NamedQuery(name = "ResourceSupplier.updateIsDeleted", query = "UPDATE ResourceSupplierEntity rs SET rs.isDeleted = :isDeleted WHERE rs.resource.id = :resourceId AND rs.supplier.id = :supplierId")
// Query to check if a relationship exists between a resource and a supplier, identified by their respective ids.
@NamedQuery(name = "ResourceSupplier.doesRelationshipExist", query = "SELECT  COUNT(rs)>0 FROM ResourceSupplierEntity rs  WHERE rs.resource.id = :resourceId AND rs.supplier.id = :supplierId")
// Query to get the 'isDeleted' status of a ResourceSupplierEntity instance identified by its resource id and supplier id.
@NamedQuery(name = "ResourceSupplier.getIsDeletedByIds", query = "SELECT  rs.isDeleted FROM ResourceSupplierEntity rs  WHERE rs.resource.id = :resourceId AND rs.supplier.id = :supplierId")
// Query to count the number of resources per supplier, ordered by the count in descending order.
@NamedQuery(
        name = "ResourceSupplier.countResourcesPerSupplier",
        query = "SELECT rs.supplier.name, COUNT(rs) AS resourceCount " +
                "FROM ResourceSupplierEntity rs " +
                "GROUP BY rs.supplier " +
                "ORDER BY resourceCount DESC"
)

public class ResourceSupplierEntity implements Serializable {
    /**
     * The unique identifier for a ResourceSupplierEntity instance.
     * This field is automatically generated, cannot be updated, and must be unique for each instance.
     * It is mapped to the 'id' column in the 'resource_supplier' table in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The user associated with this user project. This is a many-to-one relationship with the UserEntity class.
     */
    @ManyToOne
    @JoinColumn(name = "resource_id")
    private ResourceEntity resource;

    /**
     * The project associated with this user project. This is a many-to-one relationship with the ProjectEntity class.
     */
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplier;

// The 'isDeleted' field represents whether a ResourceSupplierEntity instance has been deleted.
// It is mapped to the 'is_deleted' column in the 'resource_supplier' table in the database.

    @Column(name="is_deleted", nullable = false, unique = false, updatable = true)
    private boolean isDeleted;

// The 'projectResources' field represents the list of ProjectResourceEntity instances associated with a ResourceSupplierEntity instance.
// This is a one-to-many relationship with the ProjectResourceEntity class.

    @OneToMany(mappedBy = "resource")
    private List<ProjectResourceEntity> projectResources;

// Default constructor for the ResourceSupplierEntity class.

    public ResourceSupplierEntity() {
    }
    // Getter for the 'id' field.
    public int getId() {
        return id;
    }
    // Setter for the 'id' field.
    public void setId(int id) {
        this.id = id;
    }
    // Getter for the 'resource' field.
    public ResourceEntity getResource() {
        return resource;
    }
    // Setter for the 'resource' field.
    public void setResource(ResourceEntity resource) {
        this.resource = resource;
    }
    // Getter for the 'supplier' field.
    public SupplierEntity getSupplier() {
        return supplier;
    }
    // Setter for the 'supplier' field.
    public void setSupplier(SupplierEntity supplier) {
        this.supplier = supplier;
    }
    // Getter for the 'isDeleted' field.
    public boolean isDeleted() {
        return isDeleted;
    }
    // Setter for the 'isDeleted' field.
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    // Getter for the 'projectResources' field.
    public List<ProjectResourceEntity> getProjectResources() {
        return projectResources;
    }
    // Setter for the 'projectResources' field.
    public void setProjectResources(List<ProjectResourceEntity> projectResources) {
        this.projectResources = projectResources;
    }
}
