package com.ilm.projecto_ilm_backend.dto.messages;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for messages.
 * This class encapsulates the details of a message, including the sender's name, the message content,
 * the date and time the message was sent, the system username of the sender, and a photo URL associated with the message.
 */
public class MessageDto {

    private String name; // The name of the sender
    private String message; // The message content
    private LocalDateTime date; // The date and time the message was sent
    private String systemUsername; // The system username of the sender
    private String photo; // A URL to a photo associated with the message

    /**
     * Default constructor.
     */
    public MessageDto() {
    }

    /**
     * Constructs a new MessageDto with specified details.
     *
     * @param name The name of the sender.
     * @param message The message content.
     * @param date The date and time the message was sent.
     * @param systemUsername The system username of the sender.
     * @param photo A URL to a photo associated with the message.
     */
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
