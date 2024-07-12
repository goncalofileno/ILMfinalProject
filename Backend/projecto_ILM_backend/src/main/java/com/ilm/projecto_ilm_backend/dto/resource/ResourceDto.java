package com.ilm.projecto_ilm_backend.dto.resource;

import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.ResourceTypeEnumConverter;
import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;

/**
 * Data Transfer Object for resource information.
 * This class encapsulates all relevant details about a resource, including its identifier, type,
 * name, description, observations, brand, serial number, supplier name, supplier contact, and old supplier name.
 */
public class ResourceDto {

    private int id; // Unique identifier for the resource
    private ResourceTypeENUM type; // Enum representing the type of the resource
    private String name; // Name of the resource
    private String description; // Description of the resource
    private String observation; // Observations related to the resource
    private String brand; // Brand of the resource
    private String serialNumber; // Serial number of the resource
    private String supplierName; // Name of the current supplier of the resource
    private String supplierContact; // Contact information of the current supplier
    private String oldSupplierName; // Name of the previous supplier of the resource

    /**
     * Default constructor.
     */
    public ResourceDto() {
    }

    // Getters and Setters

    /**
     * Gets the resource type.
     *
     * @return The type of the resource.
     */
    public ResourceTypeENUM getType() {
        return type;
    }

    /**
     * Sets the resource type.
     *
     * @param type The type to set for the resource.
     */
    public void setType(ResourceTypeENUM type) {
        this.type = type;
    }

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
     * Gets the observation related to the resource.
     *
     * @return The observation related to the resource.
     */
    public String getObservation() {
        return observation;
    }

    /**
     * Sets the observation related to the resource.
     *
     * @param observation The observation to set for the resource.
     */
    public void setObservation(String observation) {
        this.observation = observation;
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
     * Gets the name of the current supplier of the resource.
     *
     * @return The name of the current supplier.
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * Sets the name of the current supplier of the resource.
     *
     * @param supplierName The name to set for the current supplier.
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * Gets the contact information of the current supplier.
     *
     * @return The contact information of the current supplier.
     */
    public String getSupplierContact() {
        return supplierContact;
    }

    /**
     * Sets the contact information of the current supplier.
     *
     * @param supplierContact The contact information to set for the current supplier.
     */
    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    /**
     * Gets the name of the previous supplier of the resource.
     *
     * @return The name of the previous supplier.
     */
    public String getOldSupplierName() {
        return oldSupplierName;
    }

    /**
     * Sets the name of the previous supplier of the resource.
     *
     * @param oldSupplierName The name to set for the previous supplier.
     */
    public void setOldSupplierName(String oldSupplierName) {
        this.oldSupplierName = oldSupplierName;
    }

    /**
     * Gets the unique identifier for the resource.
     *
     * @return The unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the resource.
     *
     * @param id The identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }
}