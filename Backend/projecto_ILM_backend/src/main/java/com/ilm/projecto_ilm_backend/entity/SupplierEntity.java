package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "supplier")
public class SupplierEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "name", nullable = false, unique = true, updatable = true)
    private String name;

    @Column(name = "contact", nullable = false, unique = false, updatable = true)
    private String contact;

    public SupplierEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
