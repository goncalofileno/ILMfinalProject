package com.ilm.projecto_ilm_backend.dto.resource;

import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.ResourceTypeEnumConverter;
import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;

public class ResourceDto {

    private int id;
    private ResourceTypeENUM type;
    private String name;
    private String description;
    private String observation;
    private String brand;
    private String serialNumber;
    private String supplierName;
    private String supplierContact;
    private String oldSupplierName;

    public ResourceDto() {
    }

    public ResourceTypeENUM getType() {
        return type;
    }

    public void setType(ResourceTypeENUM type) {
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getOldSupplierName() {
        return oldSupplierName;
    }

    public void setOldSupplierName(String oldSupplierName) {
        this.oldSupplierName = oldSupplierName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
