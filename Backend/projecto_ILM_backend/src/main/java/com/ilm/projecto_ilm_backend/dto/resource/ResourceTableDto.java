package com.ilm.projecto_ilm_backend.dto.resource;

/**
 * Represents a simplified view of a resource for table display.
 * This DTO is used to encapsulate the basic information of a resource,
 * such as its name, brand, type, and supplier, along with its unique identifier
 * and the identifier of its supplier, for use in table or list views within the UI.
 */
public class ResourceTableDto {
    private String name; // The name of the resource
    private String brand; // The brand of the resource
    private String type; // The type/category of the resource
    private String supplier; // The name of the supplier of the resource
    private int id; // The unique identifier of the resource
    private int resourceSupplierId; // The unique identifier of the resource's supplier

    /**
     * Default constructor.
     */
    public ResourceTableDto() {
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
     * Gets the supplier of the resource.
     *
     * @return The supplier of the resource.
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * Sets the supplier of the resource.
     *
     * @param supplier The supplier to set for the resource.
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    /**
     * Gets the unique identifier of the resource.
     *
     * @return The unique identifier of the resource.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the resource.
     *
     * @param id The unique identifier to set for the resource.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the unique identifier of the resource's supplier.
     *
     * @return The unique identifier of the resource's supplier.
     */
    public int getResourceSupplierId() {
        return resourceSupplierId;
    }

    /**
     * Sets the unique identifier of the resource's supplier.
     *
     * @param resourceSupplierId The unique identifier to set for the resource's supplier.
     */
    public void setResourceSupplierId(int resourceSupplierId) {
        this.resourceSupplierId = resourceSupplierId;
    }
}