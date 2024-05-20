package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.UserTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.UserTypeEnumConverter;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The UserEntity class represents the "user" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "user")
@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = :email")
@NamedQuery(name = "User.findById", query = "SELECT u FROM UserEntity u WHERE u.id = :id")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the user. This is the primary key in the "user" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The username of the user. This is a unique field in the "user" table.
     */
    @Column(name = "username", nullable = true, unique = true, updatable = true)
    private String username;

    /**
     * The password of the user.
     */
    @Column(name = "password", nullable = false, unique = false, updatable = true)
    private String password;

    /**
     * The email of the user. This is a unique field in the "user" table.
     */
    @Column(name = "email", nullable = false, unique = true, updatable = true)
    private String email;

    /**
     * The first name of the user.
     */
    @Column(name = "firstName", nullable = true, unique = false, updatable = true)
    private String firstName;

    /**
     * The last name of the user.
     */
    @Column(name = "lastName", nullable = true, unique = false, updatable = true)
    private String lastName;

    /**
     * The type of the user. This is an enum field in the "user" table.
     */
    @Convert(converter = UserTypeEnumConverter.class)
    @Column(name = "type", nullable = false, unique = false, updatable = true)
    private UserTypeENUM type;

    /**
     * The registration date of the user.
     */
    @Column(name = "registrationDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime registrationDate;

    /**
     * The mail confirmation status of the user.
     */
    @Column(name = "mailConfirmed", nullable = false, unique = false, updatable = true)
    private boolean mailConfirmed;

    /**
     * The profile creation status of the user.
     */
    @Column(name = "profileCreated", nullable = false, unique = false, updatable = true)
    private boolean profileCreated;

    /**
     * The photo URL of the user.
     */
    @Column(name = "photo", nullable = false, unique = false, updatable = true)
    private String photo;

    /**
     * The token of the user. This is a unique field in the "user" table.
     */
    @Column(name = "token", nullable = true, unique = true, updatable = true)
    private String token;

    /**
     * The last activity date of the user.
     */
    @Column(name = "lastActivity", nullable = true, unique = false, updatable = true)
    private LocalDateTime lastActivity;

    /**
     * The deletion status of the user.
     */
    @Column(name = "deleted", nullable = false, unique = false, updatable = true)
    private boolean deleted;

    /**
     * The tutorial status of the user.
     */
    @Column(name = "tutorial", nullable = false, unique = false, updatable = true)
    private boolean tutorial;

    /**
     * The interests of the user. This is a many-to-many relationship.
     */
    @ManyToMany
    private List<InterestEntity> interests;

    /**
     * The skills of the user. This is a many-to-many relationship.
     */
    @ManyToMany
    private List<SkillEntity> skills;

    /**
     * The lab associated with the user. This is a many-to-one relationship.
     */
    @ManyToOne
    private LabEntity lab;

    /**
     * The list of user projects associated with this user. This is a one-to-many relationship.
     */
    @OneToMany(mappedBy = "user")
    private List<UserProjectEntity> userProjects;

    /**
     * The list of user tasks associated with this user. This is a one-to-many relationship.
     */
    @OneToMany(mappedBy = "user")
    private List<UserTaskEntity> userTasks;

    /**
     * Default constructor.
     */
    public UserEntity() {
    }

    /**
     * Returns the ID of this user.
     *
     * @return the ID of this user.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of this user.
     *
     * @param id the new ID of this user.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the username of this user.
     *
     * @return the username of this user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of this user.
     *
     * @param username the new username of this user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of this user.
     *
     * @return the password of this user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of this user.
     *
     * @param password the new password of this user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the email of this user.
     *
     * @return the email of this user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of this user.
     *
     * @param email the new email of this user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the first name of this user.
     *
     * @return the first name of this user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of this user.
     *
     * @param firstName the new first name of this user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of this user.
     *
     * @return the last name of this user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of this user.
     *
     * @param lastName the new last name of this user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the type of this user.
     *
     * @return the type of this user.
     */
    public UserTypeENUM getType() {
        return type;
    }

    /**
     * Sets the type of this user.
     *
     * @param type the new type of this user.
     */
    public void setType(UserTypeENUM type) {
        this.type = type;
    }

    /**
     * Returns the registration date of this user.
     *
     * @return the registration date of this user.
     */
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Sets the registration date of this user.
     *
     * @param registrationDate the new registration date of this user.
     */
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Returns the mail confirmation status of this user.
     *
     * @return the mail confirmation status of this user.
     */
    public boolean isMailConfirmed() {
        return mailConfirmed;
    }

    /**
     * Sets the mail confirmation status of this user.
     *
     * @param mailConfirmed the new mail confirmation status of this user.
     */
    public void setMailConfirmed(boolean mailConfirmed) {
        this.mailConfirmed = mailConfirmed;
    }

    /**
     * Returns the profile creation status of this user.
     *
     * @return the profile creation status of this user.
     */
    public boolean isProfileCreated() {
        return profileCreated;
    }

    /**
     * Sets the profile creation status of this user.
     *
     * @param profileCreated the new profile creation status of this user.
     */
    public void setProfileCreated(boolean profileCreated) {
        this.profileCreated = profileCreated;
    }

    /**
     * Returns the photo URL of this user.
     *
     * @return the photo URL of this user.
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the photo URL of this user.
     *
     * @param photo the new photo URL of this user.
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Returns the lab associated with this user.
     *
     * @return the lab associated with this user.
     */
    public LabEntity getLab() {
        return lab;
    }

    /**
     * Sets the lab associated with this user.
     *
     * @param lab the new lab associated with this user.
     */
    public void setLab(LabEntity lab) {
        this.lab = lab;
    }

    /**
     * Returns the token of this user.
     *
     * @return the token of this user.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token of this user.
     *
     * @param token the new token of this user.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Returns the last activity date of this user.
     *
     * @return the last activity date of this user.
     */
    public LocalDateTime getLastActivity() {
        return lastActivity;
    }

    /**
     * Sets the last activity date of this user.
     *
     * @param lastActivity the new last activity date of this user.
     */
    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }

    /**
     * Returns the deletion status of this user.
     *
     * @return the deletion status of this user.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets the deletion status of this user.
     *
     * @param deleted the new deletion status of this user.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Returns the tutorial status of this user.
     *
     * @return the tutorial status of this user.
     */
    public boolean isTutorial() {
        return tutorial;
    }

    /**
     * Sets the tutorial status of this user.
     *
     * @param tutorial the new tutorial status of this user.
     */
    public void setTutorial(boolean tutorial) {
        this.tutorial = tutorial;
    }

    /**
     * Returns the list of user projects associated with this user.
     *
     * @return the list of user projects associated with this user.
     */
    public List<UserProjectEntity> getUserProjects() {
        return userProjects;
    }

    /**
     * Sets the list of user projects associated with this user.
     *
     * @param userProjects the new list of user projects associated with this user.
     */
    public void setUserProjects(List<UserProjectEntity> userProjects) {
        this.userProjects = userProjects;
    }

    /**
     * Returns the list of user tasks associated with this user.
     *
     * @return the list of user tasks associated with this user.
     */
    public List<UserTaskEntity> getUserTasks() {
        return userTasks;
    }

    /**
     * Sets the list of user tasks associated with this user.
     *
     * @param userTasks the new list of user tasks associated with this user.
     */
    public void setUserTasks(List<UserTaskEntity> userTasks) {
        this.userTasks = userTasks;
    }

    /**
     * Returns the interests of this user.
     *
     * @return the interests of this user.
     */
    public List<InterestEntity> getInterests() {
        return interests;
    }

    /**
     * Sets the interests of this user.
     *
     * @param interests the new interests of this user.
     */
    public void setInterests(List<InterestEntity> interests) {
        this.interests = interests;
    }

    /**
     * Returns the skills of this user.
     *
     * @return the skills of this user.
     */
    public List<SkillEntity> getSkills() {
        return skills;
    }

    /**
     * Sets the skills of this user.
     *
     * @param skills the new skills of this user.
     */
    public void setSkills(List<SkillEntity> skills) {
        this.skills = skills;
    }
}
