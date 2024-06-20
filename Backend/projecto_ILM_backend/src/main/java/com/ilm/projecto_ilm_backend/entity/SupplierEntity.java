package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The SupplierEntity class represents the "supplier" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "supplier")
@NamedQuery(name = "Supplier.findByName", query = "SELECT s FROM SupplierEntity s WHERE s.name = :name")
@NamedQuery(name = "Supplier.findById", query = "SELECT s FROM SupplierEntity s WHERE s.id = :id")
@NamedQuery(name = "Supplier.findNameById", query = "SELECT s.name FROM SupplierEntity s WHERE s.id = :id")
@NamedQuery(name = "Supplier.findIdByName", query = "SELECT s.id FROM SupplierEntity s WHERE s.name = :name")
@NamedQuery(name = "Supplier.findAllNames", query = "SELECT s.name FROM SupplierEntity s ")
@NamedQuery(name = "Supplier.findSupplierContactByName", query = "SELECT s.contact FROM SupplierEntity s WHERE s.name = :name")


public class SupplierEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the supplier. This is the primary key in the "supplier" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The name of the supplier.
     */
    @Column(name = "name", nullable = false, unique = true, updatable = true)
    private String name;

    /**
     * The contact information of the supplier.
     */
    @Column(name = "contact", nullable = false, unique = false, updatable = true)
    private String contact;


    @OneToMany(mappedBy = "supplier")
    private List<ResourceSupplierEntity> resource;
    /**
     * Default constructor.
     */
    public SupplierEntity() {
    }

    /**
     * Returns the unique ID of this supplier.
     *
     * @return the ID of this supplier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this supplier.
     *
     * @param id the new ID of this supplier.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of this supplier.
     *
     * @return the name of this supplier.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this supplier.
     *
     * @param name the new name of this supplier.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the contact information of this supplier.
     *
     * @return the contact information of this supplier.
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the contact information of this supplier.
     *
     * @param contact the new contact information of this supplier.
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<ResourceSupplierEntity> getResource() {
        return resource;
    }

    public void setResource(List<ResourceSupplierEntity> resource) {
        this.resource = resource;
    }
}
