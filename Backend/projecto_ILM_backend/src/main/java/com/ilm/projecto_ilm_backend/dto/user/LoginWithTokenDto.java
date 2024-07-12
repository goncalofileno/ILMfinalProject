package com.ilm.projecto_ilm_backend.dto.user;

import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for user login with a token.
 * This class encapsulates the token used for authentication and the user agent from which the login request originates.
 */
@XmlRootElement
public class LoginWithTokenDto {
    private String token; // The authentication token
    private String userAgent; // The user agent of the client making the login request

    /**
     * Default constructor.
     */
    public LoginWithTokenDto() {
    }

    // Getters and setters

    /**
     * Gets the authentication token.
     *
     * @return The authentication token.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the authentication token.
     *
     * @param token The authentication token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the user agent of the client making the login request.
     *
     * @return The user agent.
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Sets the user agent of the client making the login request.
     *
     * @param userAgent The user agent to set.
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}