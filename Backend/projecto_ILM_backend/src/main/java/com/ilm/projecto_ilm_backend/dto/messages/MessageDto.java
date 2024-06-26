package com.ilm.projecto_ilm_backend.dto.messages;

import java.time.LocalDateTime;

public class MessageDto {

    private String name;
    private String message;
    private LocalDateTime date;
    private String systemUsername;
    private String photo;

    // Construtor padrão
    public MessageDto() {
    }

    // Construtor com parâmetros
    public MessageDto(String name, String message, LocalDateTime date, String systemUsername, String photo) {
        this.name = name;
        this.message = message;
        this.date = date;
        this.systemUsername = systemUsername;
        this.photo = photo;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getSystemUsername() {
        return systemUsername;
    }

    public void setSystemUsername(String systemUsername) {
        this.systemUsername = systemUsername;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
