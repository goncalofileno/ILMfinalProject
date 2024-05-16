package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
/**
 * The InterestEntity class represents the "interest" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name="interest")
@NamedQuery(name = "Interest.findById", query = "SELECT i FROM InterestEntity i WHERE i.id = :id")
@NamedQuery(name = "Interest.findAll", query = "SELECT i FROM InterestEntity i")
@NamedQuery(name = "Interest.countAll", query = "SELECT COUNT(i) FROM InterestEntity i")
public class InterestEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the interest. This is the primary key in the "interest" table.
     */
    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

    /**
     * The name of the interest. This is a unique field in the "interest" table.
     */
    @Column(name="name", nullable = false, unique = true, updatable = false)
    private String name;

    /**
     * The deleted field of the interest. This is a boolean field in the "interest" table.
     */
    @Column(name="isDeleted", nullable = false, unique = false, updatable = true)
    private boolean deleted;

    /**
     * Default constructor.
     */
    public InterestEntity() {
    }

    /**
     * Returns the ID of this interest.
     * @return the ID of this interest.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of this interest.
     * @param id the new ID of this interest.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of this interest.
     * @return the name of this interest.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this interest.
     * @param name the new name of this interest.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the deleted status of this interest.
     * @return the deleted status of this interest.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted status of this interest.
     * @param deleted the new deleted status of this interest.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
