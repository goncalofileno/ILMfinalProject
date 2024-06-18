package com.ilm.projecto_ilm_backend.dto.resource;

import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;

import java.util.ArrayList;
import java.util.List;

public class ResourceFiltersDto {
    List<ResourceTypeENUM> types;
    List<String> brands;
    List<String> suppliers;
    List<String> names;


    public ResourceFiltersDto() {
    }

    public List<ResourceTypeENUM> getTypes() {
        return types;
    }

    public void setTypes(List<ResourceTypeENUM> types) {
        this.types = types;
    }

    public List<String> getBrands() {
        return brands;
    }

    public void setBrands(List<String> brands) {
        this.brands = brands;
    }

    public List<String> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<String> suppliers) {
        this.suppliers = suppliers;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
