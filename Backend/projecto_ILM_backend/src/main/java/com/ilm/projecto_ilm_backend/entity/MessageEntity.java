package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.time.LocalDateTime;


@Entity
@Table(name="message")
public class MessageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="text", nullable = false, unique = false, updatable = true)
    private String text;

    @Column(name="date", nullable = false, unique = false, updatable = true)
    private LocalDateTime date;

    @Column(name="seen", nullable = false, unique = false, updatable = true)
    private boolean seen;

    @Column(name="receiver", nullable = false, unique = false, updatable = true)
    private String receiver;

    @ManyToOne
    private UserEntity sender;

    public MessageEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }
}
