package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="resources")
public class ResourceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

   @Column(name="type", nullable = false, unique = false, updatable = true)
    private boolean type;

    @Column(name="name", nullable = false, unique = false, updatable = true)
    private String name;

    @Column(name="description", nullable = false, unique = false, updatable = true)
    private String description;

    @Column(name="observation", nullable = false, unique = false, updatable = true)
    private String observation;

    @Column(name="stock", nullable = false, unique = false, updatable = true)
    private int stock;

    @Column(name="brand", nullable = false, unique = false, updatable = true)
    private String brand;

    @Column(name="serialNumber", nullable = false, unique = false, updatable = true)
    private String serialNumber;

    @ManyToMany
    private List<SupplierEntity> supplier;

    @OneToMany(mappedBy = "resources")
    private List<ProjectResourceEntity> projectResources;


    public ResourceEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public List<SupplierEntity> getSupplier() {
        return supplier;
    }

    public void setSupplier(List<SupplierEntity> supplier) {
        this.supplier = supplier;
    }

}
