package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "resource_supplier")

@NamedQuery(name = "ResourceSupplier.updateIsDeleted", query = "UPDATE ResourceSupplierEntity rs SET rs.isDeleted = :isDeleted WHERE rs.resource.id = :resourceId AND rs.supplier.id = :supplierId")
@NamedQuery(name = "ResourceSupplier.doesRelationshipExist", query = "SELECT  COUNT(rs)>0 FROM ResourceSupplierEntity rs  WHERE rs.resource.id = :resourceId AND rs.supplier.id = :supplierId")
@NamedQuery(name = "ResourceSupplier.getIsDeletedByIds", query = "SELECT  rs.isDeleted FROM ResourceSupplierEntity rs  WHERE rs.resource.id = :resourceId AND rs.supplier.id = :supplierId")


public class ResourceSupplierEntity implements Serializable {
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

    @Column(name="is_deleted", nullable = false, unique = false, updatable = true)
    private boolean isDeleted;

    @OneToMany(mappedBy = "resource")
    private List<ProjectResourceEntity> projectResources;

    public ResourceSupplierEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ResourceEntity getResource() {
        return resource;
    }

    public void setResource(ResourceEntity resource) {
        this.resource = resource;
    }

    public SupplierEntity getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierEntity supplier) {
        this.supplier = supplier;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<ProjectResourceEntity> getProjectResources() {
        return projectResources;
    }

    public void setProjectResources(List<ProjectResourceEntity> projectResources) {
        this.projectResources = projectResources;
    }
}
