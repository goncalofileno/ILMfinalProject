package com.ilm.projecto_ilm_backend.dto.mail;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for contact information.
 * This class is used to encapsulate the contact details such as name and email.
 * It is annotated with @XmlRootElement to indicate that this class can be converted to XML.
 */
@XmlRootElement
public class ContactDto {

    private String name;
    private String email;

    /**
     * Default constructor.
     */
    public ContactDto() {
    }

    /**
     * Constructs a new ContactDto with specified name and email.
     *
     * @param name  The name of the contact.
     * @param email The email address of the contact.
     */
    public ContactDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Gets the name of the contact.
     *
     * @return The name of the contact.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the contact.
     *
     * @param name The name to set for the contact.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the contact.
     *
     * @return The email address of the contact.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the contact.
     *
     * @param email The email address to set for the contact.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}