package com.ilm.projecto_ilm_backend.dto.resource;

import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;

/**
 * Data Transfer Object for creating a new resource.
 * This class encapsulates all necessary information required to create a new resource,
 * including its name, description, observations, brand, serial number, type, supplier name, and supplier contact.
 */
public class ResourceCreationDto {
    private String name; // Name of the resource
    private String description; // Description of the resource
    private String observations; // Observations related to the resource
    private String brand; // Brand of the resource
    private String serialNumber; // Serial number of the resource
    private String type; // Type of the resource
    private String supplierName; // Name of the resource's supplier
    private String supplierContact; // Contact information of the resource's supplier

    /**
     * Default constructor.
     */
    public ResourceCreationDto() {
    }

    // Getters and Setters

    /**
     * Gets the name of the resource.
     *
     * @return The name of the resource.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the resource.
     *
     * @param name The name to set for the resource.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the resource.
     *
     * @return The description of the resource.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the resource.
     *
     * @param description The description to set for the resource.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the observations related to the resource.
     *
     * @return The observations related to the resource.
     */
    public String getObservations() {
        return observations;
    }

    /**
     * Sets the observations related to the resource.
     *
     * @param observation The observations to set for the resource.
     */
    public void setObservations(String observation) {
        this.observations = observation;
    }

    /**
     * Gets the brand of the resource.
     *
     * @return The brand of the resource.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand of the resource.
     *
     * @param brand The brand to set for the resource.
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Gets the serial number of the resource.
     *
     * @return The serial number of the resource.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the serial number of the resource.
     *
     * @param serialNumber The serial number to set for the resource.
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Gets the type of the resource.
     *
     * @return The type of the resource.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the resource.
     *
     * @param type The type to set for the resource.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the name of the resource's supplier.
     *
     * @return The name of the resource's supplier.
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * Sets the name of the resource's supplier.
     *
     * @param supplierName The name to set for the resource's supplier.
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * Gets the contact information of the resource's supplier.
     *
     * @return The contact information of the resource's supplier.
     */
    public String getSupplierContact() {
        return supplierContact;
    }

    /**
     * Sets the contact information of the resource's supplier.
     *
     * @param supplierContact The contact information to set for the resource's supplier.
     */
    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }
}