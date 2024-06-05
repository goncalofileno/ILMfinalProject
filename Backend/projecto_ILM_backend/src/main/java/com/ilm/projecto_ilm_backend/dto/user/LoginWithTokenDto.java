package com.ilm.projecto_ilm_backend.dto.user;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginWithTokenDto {
    private String token;
    private String userAgent;

    public LoginWithTokenDto() {
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}

