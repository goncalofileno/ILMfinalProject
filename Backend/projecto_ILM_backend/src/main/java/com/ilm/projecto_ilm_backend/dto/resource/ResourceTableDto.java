package com.ilm.projecto_ilm_backend.dto.resource;

public class ResourceTableDto {
    private String name;
    private String brand;
    private String type;
    private String supplier;
    private int id;
    private int resourceSupplierId;

    public ResourceTableDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResourceSupplierId() {
        return resourceSupplierId;
    }

    public void setResourceSupplierId(int resouceSupplierId) {
        this.resourceSupplierId = resouceSupplierId;
    }
}
