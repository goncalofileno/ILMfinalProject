package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.SkillTypeENUM;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * The SkillEntity class represents the "skill" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name="skill")
public class SkillEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the skill. This is the primary key in the "skill" table.
     */
    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

    /**
     * The name of the skill.
     */
    @Column(name="name", nullable = false, unique = true, updatable = true)
    private String name;

    /**
     * The type of the skill. This is an enumerated type.
     */
    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable = false, unique = false, updatable = true)
    private SkillTypeENUM type;

    /**
     * The users associated with the skill. This is a many-to-many relationship with the UserEntity class.
     */
    @ManyToMany
    private List<UserEntity> userWithSkill;

    /**
     * Default constructor.
     */
    public SkillEntity() {
    }

    /**
     * Returns the unique ID of this skill.
     *
     * @return the ID of this skill.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this skill.
     *
     * @param id the new ID of this skill.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of this skill.
     *
     * @return the name of this skill.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this skill.
     *
     * @param name the new name of this skill.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of this skill.
     *
     * @return the type of this skill.
     */
    public SkillTypeENUM getType() {
        return type;
    }

    /**
     * Sets the type of this skill.
     *
     * @param type the new type of this skill.
     */
    public void setType(SkillTypeENUM type) {
        this.type = type;
    }

    /**
     * Returns the users associated with this skill.
     *
     * @return the users associated with this skill.
     */
    public List<UserEntity> getUserWithSkill() {
        return userWithSkill;
    }

    /**
     * Sets the users associated with this skill.
     *
     * @param userWithSkill the new users associated with this skill.
     */
    public void setUserWithSkill(List<UserEntity> userWithSkill) {
        this.userWithSkill = userWithSkill;
    }
}
