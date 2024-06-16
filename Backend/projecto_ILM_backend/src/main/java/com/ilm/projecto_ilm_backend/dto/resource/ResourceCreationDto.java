package com.ilm.projecto_ilm_backend.dto.resource;

import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;

public class ResourceCreationDto {
    private String name;
    private String description;
    private String observation;
    private String brand;
    private String serialNumber;
    private ResourceTypeENUM type;
    private String supplierName;
    private String supplierContact;

    public ResourceCreationDto() {
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

    public ResourceTypeENUM getType() {
        return type;
    }

    public void setType(ResourceTypeENUM type) {
        this.type = type;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }


    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }
}
