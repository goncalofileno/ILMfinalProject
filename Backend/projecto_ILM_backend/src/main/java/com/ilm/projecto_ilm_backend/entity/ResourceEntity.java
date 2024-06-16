package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.ResourceTypeEnumConverter;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

/**
 * The ResourceEntity class represents the "resources" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "resources")
@NamedQuery(name = "Resource.findById", query = "SELECT r FROM ResourceEntity r WHERE r.id = :id")
@NamedQuery(
        name = "Resource.hasSupplier",
        query = "SELECT COUNT(r) > 0 FROM ResourceEntity r " +
                "JOIN r.supplier s " +
                "WHERE r.id = :resourceId " +
                "AND s.id = :supplierId"
)
@NamedQuery(
        name = "Resource.findResourceByDetails",
        query = "SELECT r FROM ResourceEntity r " +
                "JOIN r.supplier s " +
                "WHERE (r.name=:name)" +
                "AND (r.brand = :brand) "+
                "AND (r.type = :type) "+
                "AND (s.name=:supplierName) "
)

@NamedQuery(
        name = "Resource.getResourcesDetails",
        query = "SELECT r.name, r.brand, r.type, s.id FROM ResourceEntity r " +
                "JOIN r.supplier s " +
                "WHERE (:brand IS NULL OR r.brand = :brand) "+
                "AND (:type IS NULL OR r.type = :type) "+
                "AND (:supplierId IS NULL OR s.id = :supplierId) "+
                "AND (:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
)

@NamedQuery(
        name = "Resource.getAllBrands",
        query = "SELECT DISTINCT r.brand FROM ResourceEntity r ")

@NamedQuery(
        name = "Resource.getNumberOfResourcesDetails",
        query = "SELECT COUNT(r) FROM ResourceEntity r " +
                "JOIN r.supplier s "+
                "WHERE (:brand IS NULL OR r.brand = :brand) "+
                "AND (:type IS NULL OR r.type = :type) "+
                "AND (:supplierId IS NULL OR s.id = :supplierId) "+
                "AND (:name IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
)

public class ResourceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the resource. This is the primary key in the "resources" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The type of the resource. This is an enumerated type.
     */
    @Convert(converter = ResourceTypeEnumConverter.class)
    @Column(name = "type", nullable = false, unique = false, updatable = true)
    private ResourceTypeENUM type;

    /**
     * The name of the resource.
     */
    @Column(name = "name", nullable = false, unique = false, updatable = true)
    private String name;

    /**
     * The description of the resource.
     */
    @Column(name = "description", nullable = false, unique = false, updatable = true)
    private String description;

    /**
     * The observation of the resource.
     */
    @Column(name = "observation", nullable = false, unique = false, updatable = true)
    private String observation;


    /**
     * The brand of the resource.
     */
    @Column(name = "brand", nullable = false, unique = false, updatable = true)
    private String brand;

    /**
     * The serial number of the resource.
     */
    @Column(name = "serialNumber", nullable = false, unique = false, updatable = true)
    private String serialNumber;

    /**
     * The suppliers of the resource. This is a many-to-many relationship with the SupplierEntity class.
     */
    @ManyToMany
    private List<SupplierEntity> supplier;

    /**
     * The project resources associated with the resource. This is a one-to-many relationship with the ProjectResourceEntity class.
     */
    @OneToMany(mappedBy = "resources")
    private List<ProjectResourceEntity> projectResources;

    /**
     * Default constructor.
     */
    public ResourceEntity() {
    }

    /**
     * Returns the unique ID of this resource.
     *
     * @return the ID of this resource.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this resource.
     *
     * @param id the new ID of this resource.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the type of this resource.
     *
     * @return the type of this resource.
     */
    public ResourceTypeENUM getType() {
        return type;
    }

    /**
     * Sets the type of this resource.
     *
     * @param type the new type of this resource.
     */
    public void setType(ResourceTypeENUM type) {
        this.type = type;
    }

    /**
     * Returns the project resources associated with this resource.
     *
     * @return the project resources associated with this resource.
     */
    public List<ProjectResourceEntity> getProjectResources() {
        return projectResources;
    }

    /**
     * Sets the project resources associated with this resource.
     *
     * @param projectResources the new project resources associated with this resource.
     */
    public void setProjectResources(List<ProjectResourceEntity> projectResources) {
        this.projectResources = projectResources;
    }

    /**
     * Returns the name of this resource.
     *
     * @return the name of this resource.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this resource.
     *
     * @param name the new name of this resource.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of this resource.
     *
     * @return the description of this resource.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this resource.
     *
     * @param description the new description of this resource.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the observation of this resource.
     *
     * @return the observation of this resource.
     */
    public String getObservation() {
        return observation;
    }

    /**
     * Sets the observation of this resource.
     *
     * @param observation the new observation of this resource.
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }


    /**
     * Returns the brand of this resource.
     *
     * @return the brand of this resource.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of this resource.
     *
     * @param brand the new brand of this resource.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the serial number of this resource.
     *
     * @return the serial number of this resource.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the serial number of this resource.
     *
     * @param serialNumber the new serial number of this resource.
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Returns the suppliers of this resource.
     *
     * @return the suppliers of this resource.
     */
    public List<SupplierEntity> getSupplier() {
        return supplier;
    }

    /**
     * Sets the suppliers of this resource.
     *
     * @param supplier the new suppliers of this resource.
     */
    public void setSupplier(List<SupplierEntity> supplier) {
        this.supplier = supplier;
    }

    public void addSupplier(SupplierEntity supplier) {
        this.supplier.add(supplier);
    }

}
