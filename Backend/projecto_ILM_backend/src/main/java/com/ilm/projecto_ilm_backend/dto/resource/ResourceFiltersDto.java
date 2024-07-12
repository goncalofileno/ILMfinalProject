package com.ilm.projecto_ilm_backend.dto.resource;

import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for filtering resources.
 * This class encapsulates the criteria used to filter resources, including types, brands, suppliers, and names.
 */
public class ResourceFiltersDto {
    // List of resource types to filter by
    List<ResourceTypeENUM> types;
    // List of brands to filter by
    List<String> brands;
    // List of suppliers to filter by
    List<String> suppliers;
    // List of resource names to filter by
    List<String> names;

    /**
     * Default constructor.
     */
    public ResourceFiltersDto() {
    }

    /**
     * Gets the list of resource types to filter by.
     *
     * @return A list of {@link ResourceTypeENUM}.
     */
    public List<ResourceTypeENUM> getTypes() {
        return types;
    }

    /**
     * Sets the list of resource types to filter by.
     *
     * @param types A list of {@link ResourceTypeENUM} to set.
     */
    public void setTypes(List<ResourceTypeENUM> types) {
        this.types = types;
    }

    /**
     * Gets the list of brands to filter by.
     *
     * @return A list of brand names.
     */
    public List<String> getBrands() {
        return brands;
    }

    /**
     * Sets the list of brands to filter by.
     *
     * @param brands A list of brand names to set.
     */
    public void setBrands(List<String> brands) {
        this.brands = brands;
    }

    /**
     * Gets the list of suppliers to filter by.
     *
     * @return A list of supplier names.
     */
    public List<String> getSuppliers() {
        return suppliers;
    }

    /**
     * Sets the list of suppliers to filter by.
     *
     * @param suppliers A list of supplier names to set.
     */
    public void setSuppliers(List<String> suppliers) {
        this.suppliers = suppliers;
    }

    /**
     * Gets the list of resource names to filter by.
     *
     * @return A list of resource names.
     */
    public List<String> getNames() {
        return names;
    }

    /**
     * Sets the list of resource names to filter by.
     *
     * @param names A list of resource names to set.
     */
    public void setNames(List<String> names) {
        this.names = names;
    }
}