package com.ilm.projecto_ilm_backend.dto.statistics;

public class SupplierWithMostResourcesDto {
    private String supplier;
    private long resources;

    public SupplierWithMostResourcesDto() {
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public long getResources() {
        return resources;
    }

    public void setResources(long resources) {
        this.resources = resources;
    }
}
