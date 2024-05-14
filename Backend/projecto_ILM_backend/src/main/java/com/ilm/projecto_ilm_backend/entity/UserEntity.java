package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.UserTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    @Column(name = "username", nullable = false, unique = true, updatable = true)
    private String username;

    @Column(name = "password", nullable = false, unique = false, updatable = true)
    private String password;

    @Column(name = "email", nullable = false, unique = true, updatable = true)
    private String email;

    @Column(name = "firstName", nullable = false, unique = false, updatable = true)
    private String firstName;

    @Column(name = "lastName", nullable = false, unique = false, updatable = true)
    private String lastName;

    @Column(name = "type", nullable = false, unique = false, updatable = true)
    private UserTypeENUM type;

    @Column(name = "registrationDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime registrationDate;

    @Column(name = "mailConfirmed", nullable = false, unique = false, updatable = true)
    private boolean mailConfirmed;

    @Column(name = "profileCreated", nullable = false, unique = false, updatable = true)
    private boolean profileCreated;

    @Column(name = "photo", nullable = false, unique = false, updatable = true)
    private String photo;

    @Column(name = "lab", nullable = false, unique = false, updatable = true)
    private WorkLocalENUM lab;

    @Column(name = "token", nullable = true, unique = true, updatable = true)
    private String token;

    @Column(name = "lastActivity", nullable = true, unique = false, updatable = true)
    private LocalDateTime lastActivity;

    @Column(name = "deleted", nullable = false, unique = false, updatable = true)
    private boolean deleted;

    @Column(name = "tutorial", nullable = false, unique = false, updatable = true)
    private boolean tutorial;

    @OneToMany(mappedBy = "user")
    private List<UserProjectEntity> userProjects;

    @OneToMany(mappedBy = "user")
    private List<UserTaskEntity> userTasks;

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

    public UserTypeENUM getType() {
        return type;
    }

    public void setType(UserTypeENUM type) {
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

    public WorkLocalENUM getLab() {
        return lab;
    }

    public void setLab(WorkLocalENUM lab) {
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

    public List<UserProjectEntity> getUserProjects() {
        return userProjects;
    }

    public void setUserProjects(List<UserProjectEntity> userProjects) {
        this.userProjects = userProjects;
    }

    public List<UserTaskEntity> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(List<UserTaskEntity> userTasks) {
        this.userTasks = userTasks;
    }
}
