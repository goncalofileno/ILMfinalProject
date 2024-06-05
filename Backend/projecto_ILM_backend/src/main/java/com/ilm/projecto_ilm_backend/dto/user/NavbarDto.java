package com.ilm.projecto_ilm_backend.dto.user;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for the Navbar.
 * This class is used to transfer data related to the Navbar between different parts of the application.
 * It includes the user's photo, first name, and last name.
 */
@XmlRootElement
public class NavbarDto {

    private String photo;
    private String firstName;
    private String lastName;

    /**
     * Default constructor.
     */
    public NavbarDto() {
    }

    /**
     * Constructor with parameters.
     *
     * @param photo The photo of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     */
    public NavbarDto(String photo, String firstName, String lastName) {
        this.photo = photo;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Returns the photo of the user.
     *
     * @return A string representing the user's photo.
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the photo of the user.
     *
     * @param photo A string representing the user's photo.
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Returns the first name of the user.
     *
     * @return A string representing the user's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName A string representing the user's first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return A string representing the user's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName A string representing the user's last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
