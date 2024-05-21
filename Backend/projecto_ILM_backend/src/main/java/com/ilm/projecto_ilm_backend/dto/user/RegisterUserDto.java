package com.ilm.projecto_ilm_backend.dto.user;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object (DTO) for registering a user.
 * This class is used to encapsulate the data required for user registration,
 * specifically the user's email and password.
 * <p>
 * It is annotated with {@link XmlRootElement} to indicate that it can be
 * converted to and from XML.
 * </p>
 */
@XmlRootElement
public class RegisterUserDto {

    /**
     * The email of the user.
     */
    private String mail;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * Default no-argument constructor.
     */
    public RegisterUserDto() {
    }

    /**
     * Parameterized constructor to initialize the RegisterUserDto with the given email and password.
     *
     * @param mail     the email of the user
     * @param password the password of the user
     */
    public RegisterUserDto(String mail, String password) {
        this.mail = mail;
        this.password = password;
    }

    /**
     * Returns the email of the user.
     *
     * @return the email of the user
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets the email of the user.
     *
     * @param mail the email to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
