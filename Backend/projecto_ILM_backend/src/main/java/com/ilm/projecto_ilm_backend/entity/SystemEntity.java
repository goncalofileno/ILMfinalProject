package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * The SystemEntity class represents the "systemConfigs" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "systemConfigs")
@NamedQuery(name = "System.getValueByName", query = "SELECT sy.value FROM SystemEntity sy WHERE sy.name= :name")
public class SystemEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the system configuration. This is the primary key in the "systemConfigs" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The name of the system configuration.
     */
    @Column(name = "name", nullable = false, unique = false, updatable = true)
    private String name;

    /**
     * The value of the system configuration.
     */
    @Column(name = "value", nullable = false, unique = false, updatable = true)
    private int value;

    /**
     * Default constructor.
     */
    public SystemEntity() {
    }

    /**
     * Returns the unique ID of this system configuration.
     *
     * @return the ID of this system configuration.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this system configuration.
     *
     * @param id the new ID of this system configuration.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of this system configuration.
     *
     * @return the name of this system configuration.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this system configuration.
     *
     * @param name the new name of this system configuration.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the value of this system configuration.
     *
     * @return the value of this system configuration.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of this system configuration.
     *
     * @param value the new value of this system configuration.
     */
    public void setValue(int value) {
        this.value = value;
    }
}