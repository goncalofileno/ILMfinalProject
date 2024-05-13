package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="interest")
public class Interest {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="name", nullable = false, unique = true, updatable = true)
    private String name;

    @Column(name="description", nullable = false, unique = false, updatable = true)
    private String description;

    public Interest() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
