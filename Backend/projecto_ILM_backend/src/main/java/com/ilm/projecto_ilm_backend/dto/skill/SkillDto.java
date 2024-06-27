package com.ilm.projecto_ilm_backend.dto.skill;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * SkillDto is a Data Transfer Object (DTO) class.
 * It is used to transfer data between processes.
 * In this case, it is used to send Skill data in the response.
 */
@XmlRootElement
public class SkillDto {
    /**
     * The id of the skill.
     */
    private int id;
    /**
     * The name of the skill.
     */
    private String name;
    /**
     * The type of the skill.
     */
    private String type;

    private boolean isInProject;

    /**
     * Default constructor.
     */
    public SkillDto() {
    }

    /**
     * Overloaded constructor.
     *
     * @param id   the id of the skill
     * @param name the name of the skill
     * @param type the type of the skill
     */
    public SkillDto(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the id of the skill.
     *
     * @return the id of the skill
     */
    @XmlElement
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the skill.
     *
     * @param id the new id of the skill
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the skill.
     *
     * @return the name of the skill
     */
    @XmlElement
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the skill.
     *
     * @param name the new name of the skill
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of the skill.
     *
     * @return the type of the skill
     */
    @XmlElement
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the skill.
     *
     * @param type the new type of the skill
     */
    public void setType(String type) {
        this.type = type;
    }

    public boolean isInProject() {
        return isInProject;
    }

    public void setInProject(boolean inProject) {
        isInProject = inProject;
    }
}
