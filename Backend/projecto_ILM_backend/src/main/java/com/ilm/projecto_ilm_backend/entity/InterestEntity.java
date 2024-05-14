package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The InterestEntity class represents the "interest" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name="interest")
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
     * The list of users associated with this interest. This is a many-to-many relationship.
     */
    @ManyToMany
    private List<UserEntity> users;

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
}
