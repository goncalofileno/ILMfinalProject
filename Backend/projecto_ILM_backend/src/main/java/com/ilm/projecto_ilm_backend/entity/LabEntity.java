package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import jakarta.persistence.*;
import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.WorkLocalEnumConverter;
import java.io.Serializable;

/**
 * The LabEntity class represents the "lab" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "lab")
@NamedQuery(name = "Lab.findByLocal", query = "SELECT l FROM LabEntity l WHERE l.local = :local")
public class LabEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the lab. This is the primary key in the "lab" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The local of the lab. This is an enumerated type.
     */
    @Convert(converter = WorkLocalEnumConverter.class)
    @Column(name = "local", nullable = false, unique = true, updatable = true)
    private WorkLocalENUM local;

    /**
     * The contact information for the lab.
     */
    @Column(name = "contact", nullable = false, unique = true, updatable = true)
    private String contact;

    /**
     * Default constructor.
     */
    public LabEntity() {
    }

    /**
     * Returns the ID of this lab.
     * @return the ID of this lab.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of this lab.
     * @param id the new ID of this lab.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the local of this lab.
     * @return the local of this lab.
     */
    public WorkLocalENUM getLocal() {
        return local;
    }

    /**
     * Sets the local of this lab.
     * @param local the new local of this lab.
     */
    public void setLocal(WorkLocalENUM local) {
        this.local = local;
    }

    /**
     * Returns the contact information of this lab.
     * @return the contact information of this lab.
     */
    public String getContact() {
        return contact;
    }

    /**
     * Sets the contact information of this lab.
     * @param contact the new contact information of this lab.
     */
    public void setContact(String contact) {
        this.contact = contact;
    }
}
