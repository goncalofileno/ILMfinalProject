package com.ilm.projecto_ilm_backend.dto.interest;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object (DTO) for an interest.
 * This class is used to encapsulate the data required for an interest,
 * specifically the interest's id and name.
 * <p>
 * It is annotated with {@link XmlRootElement} to indicate that it can be
 * converted to and from XML.
 * </p>
 */
@XmlRootElement
public class InterestDto {

    /**
     * The id of the interest.
     */
    private int id;
    /**
     * The name of the interest.
     */
    private String name;

    /**
     * Default no-argument constructor.
     */
    public InterestDto() {
    }

    /**
     * Parameterized constructor to initialize the InterestDto with the given id and name.
     *
     * @param id   the id of the interest
     * @param name the name of the interest
     */
    public InterestDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the id of the interest.
     *
     * @return the id of the interest
     */
    @XmlElement
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the interest.
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the interest.
     *
     * @return the name of the interest
     */
    @XmlElement
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the interest.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
