package com.ilm.projecto_ilm_backend.dto.statistics;

/**
 * Represents a supplier with the most resources.
 * This DTO is used to encapsulate the supplier's name and the total number of resources they have.
 */
public class SupplierWithMostResourcesDto {
    /**
     * The name of the supplier.
     */
    private String supplier;

    /**
     * The total number of resources associated with the supplier.
     */
    private long resources;

    /**
     * Default constructor.
     */
    public SupplierWithMostResourcesDto() {
    }

    /**
     * Gets the name of the supplier.
     *
     * @return The name of the supplier.
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * Sets the name of the supplier.
     *
     * @param supplier The name of the supplier to set.
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    /**
     * Gets the total number of resources associated with the supplier.
     *
     * @return The total number of resources.
     */
    public long getResources() {
        return resources;
    }

    /**
     * Sets the total number of resources associated with the supplier.
     *
     * @param resources The total number of resources to set.
     */
    public void setResources(long resources) {
        this.resources = resources;
    }
}