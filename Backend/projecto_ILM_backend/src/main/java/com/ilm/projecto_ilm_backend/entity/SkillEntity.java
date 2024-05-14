package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.SkillTypeENUM;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="skill")
public class SkillEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

    @Column(name="name", nullable = false, unique = true, updatable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable = false, unique = false, updatable = true)
    private SkillTypeENUM type;

    @ManyToMany
    private List<UserEntity> userWithSkill;

    public SkillEntity() {
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

    public SkillTypeENUM getType() {
        return type;
    }

    public void setType(SkillTypeENUM type) {
        this.type = type;
    }

    public List<UserEntity> getUserWithSkill() {
        return userWithSkill;
    }

    public void setUserWithSkill(List<UserEntity> userWithSkill) {
        this.userWithSkill = userWithSkill;
    }
}
