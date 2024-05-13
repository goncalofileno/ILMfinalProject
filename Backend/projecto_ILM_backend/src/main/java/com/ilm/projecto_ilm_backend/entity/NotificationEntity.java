package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="notification")
public class NotificationEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    @Column(name="id", nullable = false,unique = true,updatable = false)
    private int id;

    @Column(name="type", nullable = false, unique = false, updatable = false)
    private int type;

    @Column(name="readStatus", nullable = false, unique = false, updatable = true)
    private boolean readStatus;

    @Column(name="sendDate", nullable = false, unique = false, updatable = false)
    private LocalDateTime sendDate;

    @Column(name="text", nullable = false, unique = false, updatable = false)
    private String text;

    @NotNull
    @ManyToOne
    private UserEntity receptor;

    public NotificationEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserEntity getReceptor() {
        return receptor;
    }

    public void setReceptor(UserEntity receptor) {
        this.receptor = receptor;
    }
}
