package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name="username", nullable = false, unique = true, updatable = true)
    private String username;

    @Column(name="password", nullable = false, unique = false, updatable = true)
    private String password;

    @Column(name="email", nullable = false, unique = true, updatable = true)
    private String email;

    @Column(name="firstName", nullable = false, unique = false, updatable = true)
    private String firstName;

    @Column(name="lastName", nullable = false, unique = false, updatable = true)
    private String lastName;

    @Column(name="type", nullable = false, unique = false, updatable = true)
    private int type;

    @Column(name="registrationDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime registrationDate;

    @Column(name="mailConfirmed", nullable = false, unique = false, updatable = true)
    private boolean mailConfirmed;

    @Column(name="profileCreated", nullable = false, unique = false, updatable = true)
    private boolean profileCreated;

    @Column(name="photo", nullable = false, unique = false, updatable = true)
    private String photo;

    @Column(name="lab", nullable = false, unique = false, updatable = true)
    private int lab;

    @Column(name="token", nullable = true, unique = true, updatable = true)
    private String token;

    @Column(name="lastActivity", nullable = true, unique = false, updatable = true)
    private LocalDateTime lastActivity;

    @Column(name="deleted", nullable = false, unique = false, updatable = true)
    private boolean deleted;

    @Column(name="tutorial", nullable = false, unique = false, updatable = true)
    private boolean tutorial;

    public UserEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isMailConfirmed() {
        return mailConfirmed;
    }

    public void setMailConfirmed(boolean mailConfirmed) {
        this.mailConfirmed = mailConfirmed;
    }

    public boolean isProfileCreated() {
        return profileCreated;
    }

    public void setProfileCreated(boolean profileCreated) {
        this.profileCreated = profileCreated;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getLab() {
        return lab;
    }

    public void setLab(int lab) {
        this.lab = lab;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isTutorial() {
        return tutorial;
    }

    public void setTutorial(boolean tutorial) {
        this.tutorial = tutorial;
    }
}
