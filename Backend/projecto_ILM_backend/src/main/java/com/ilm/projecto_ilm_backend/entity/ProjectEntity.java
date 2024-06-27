package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.StateProjectEnumConverter;

/**
 * The ProjectEntity class represents the "project" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "project")
@NamedQuery(name = "Project.findById", query = "SELECT p FROM ProjectEntity p WHERE p.id = :id")
@NamedQuery(name = "Project.findNameAndDescriptionHome", query = "SELECT p.name, p.description FROM ProjectEntity p WHERE p.status = 1 OR  p.status = 2 OR  p.status = 3 OR  p.status = 4 ")
@NamedQuery(
        name = "Project.getProjectTableDtoInfo",
        query = "SELECT p.id, p.name, p.lab, p.status, FUNCTION('DATE', p.startDate), FUNCTION('DATE', p.endDate), p.maxMembers,p.photo, p.systemName " +
                "FROM ProjectEntity p LEFT JOIN UserProjectEntity up ON p.id = up.project.id " +
                "WHERE (:lab IS NULL OR p.lab = :lab) " +
                "AND (:status IS NULL OR p.status = :status) " +
                "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
                "GROUP BY p.id, p.name, p.lab, p.status, p.startDate, p.endDate, p.maxMembers " +
                "HAVING (:slotsAvailable = FALSE OR p.maxMembers > COUNT(up))")

@NamedQuery(name = "Project.getNumberOfProjectsTableDtoInfo",
        query = "SELECT COUNT(p) " +
        "FROM ProjectEntity p " +
        "WHERE (:lab IS NULL OR p.lab = :lab) " +
        "AND (:status IS NULL OR p.status = :status) " +
        "AND (:keyword IS NULL OR (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))))" +
        "AND (:slotsAvailable = FALSE OR p.maxMembers > " +
        "(SELECT COUNT(up) FROM UserProjectEntity up WHERE up.project.id = p.id))")

@NamedQuery(
        name = "Project.findAllProjectsOrderedByUser",
        query = "SELECT p.id, p.name, p.lab, p.status, FUNCTION('DATE', p.startDate), FUNCTION('DATE', p.endDate), p.maxMembers, p.photo, p.systemName FROM ProjectEntity p " +
                "LEFT JOIN p.userProjects up WITH up.user.id = :userId " +
                "ORDER BY CASE WHEN up.user.id IS NOT NULL THEN 0 ELSE 1 END, p.name ASC"
)

@NamedQuery(
        name = "Project.getMyProjectsInfo",
        query = "SELECT p.id, p.name, p.lab, p.status, FUNCTION('DATE', p.startDate), FUNCTION('DATE', p.endDate), p.maxMembers,p.cardPhoto, up.type, p.systemName " +
                "FROM ProjectEntity p LEFT JOIN UserProjectEntity up ON p.id = up.project.id " +
                "WHERE (:lab IS NULL OR p.lab = :lab) " +
                "AND (:status IS NULL OR p.status = :status) " +
                "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
                "AND (up.user.id = :userId) " +
                "AND (up.type=0 OR up.type=1 OR up.type=2 OR up.type=3 OR up.type=4) ")

@NamedQuery(name = "Project.getNumberOfMyProjectsInfo",
        query = "SELECT COUNT(p) " +
                "FROM ProjectEntity p LEFT JOIN UserProjectEntity up ON p.id = up.project.id " +
                "WHERE (:lab IS NULL OR p.lab = :lab) " +
                "AND (:status IS NULL OR p.status = :status) " +
                "AND (:keyword IS NULL OR (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))))" +
                "AND (up.user.id = :userId) " +
                "AND (up.type = 0 OR up.type = 1 OR up.type = 2 OR up.type = 3 OR up.type = 4) ")

@NamedQuery(name = "Project.countProjects", query = "SELECT COUNT(p) FROM ProjectEntity p")
@NamedQuery(
        name = "Project.getSkillsBySystemName",
        query = "SELECT s.name FROM ProjectEntity p JOIN p.skillInProject s WHERE p.systemName = :projectSystemName"
)

@NamedQuery(
        name = "Project.isSkillInProject",
        query = "SELECT COUNT(s) > 0 FROM ProjectEntity p JOIN p.skillInProject s WHERE p.systemName = :projectSystemName AND s.name = :skillName"
)

public class ProjectEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the project. This is the primary key in the "project" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The title of the project.
     */
    @Column(name = "name", nullable = false, unique = true, updatable = true)
    private String name;

    /**
     * The system name of the project.
     */
    @Column(name = "systemName", nullable = false, unique = true, updatable = true)
    private String systemName;

    /**
     * The description of the project.
     */
    @Column(name = "description", nullable = false, unique = false, updatable = true)
    private String description;

    @Column(name = "createdAt", nullable = false, unique = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * The start date of the project.
     */
    @Column(name = "startDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime startDate;

    /**
     * The end initial date of the project.
     */
    @Column(name = "inProgressDate", nullable = true, unique = false, updatable = true)
    private LocalDateTime initialDate;

    /**
     * The end date of the project.
     */
    @Column(name = "endDate", nullable = false, unique = false, updatable = true)
    private LocalDateTime endDate;

    @Column(name = "finishedDate", nullable = true, unique = false, updatable = true)
    private LocalDateTime finishedDate;

    /**
     * The status of the project. This is an enumerated type.
     */
    @Convert(converter = StateProjectEnumConverter.class)
    @Column(name = "status", nullable = false, unique = false, updatable = true)
    private StateProjectENUM status;

    /**
     * The motivation of the project.
     */
    @Column(name = "motivation", nullable = false, unique = false, updatable = true)
    private String motivation;

    /**
     * The maximum number of members in the project.
     */
    @Column(name = "maxMembers", nullable = false, unique = false, updatable = true)
    private int maxMembers;

    /**
     * The photo of the project.
     */
    @Column(name = "photo", nullable = true, unique = false, updatable = true)
    private String photo;

    @Column(name = "cardPhoto", nullable = true, unique = false, updatable = true)
    private String cardPhoto;


    /**
     * The reason of cancel the project.
     */
    @Column(name = "reason", nullable = true, unique = false, updatable = true)
    private String reason;

    /**
     * The lab associated with the project. This is a many-to-one relationship with the LabEntity class.
     */
    @ManyToOne
    private LabEntity lab;

    /**
     * The deleted status of the project.
     */
    @Column(name = "deleted", nullable = false, unique = false, updatable = true)
    private boolean deleted;

    /**
     * The keywords of the project.
     */
    @Column(name = "keywords", nullable = false, unique = false, updatable = true)
    private String keywords;

    /**
     * The resources associated with the project. This is a one-to-many relationship with the ProjectResourceEntity class.
     */
    @OneToMany(mappedBy = "project")
    private List<ProjectResourceEntity> projectResources;

    /**
     * The users associated with the project. This is a one-to-many relationship with the UserProjectEntity class.
     */
    @OneToMany(mappedBy = "project")
    private List<UserProjectEntity> userProjects;

    /**
     * The skills associated with the project. This is a many-to-many relationship with the SkillEntity class.
     */
    @ManyToMany
    private List<SkillEntity> skillInProject;

    /**
     * Default constructor.
     */
    public ProjectEntity() {
    }

    /**
     * Returns the unique ID of this project.
     *
     * @return the ID of this project.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this project.
     *
     * @param id the new ID of this project.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of this project.
     *
     * @return the name of this project.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this project.
     *
     * @param name the new name of this project.
     */
    public void setName(String name) {
        this.name = name;
    }


    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    /**
     * Returns the description of this project.
     *
     * @return the description of this project.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this project.
     *
     * @param description the new description of this project.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the initial date of this project.
     *
     * @return the initial date of this project.
     */
    public LocalDateTime getInitialDate() {
        return initialDate;
    }

    /**
     * Sets the initial date of this project.
     *
     * @param initialDate the new initial date of this project.
     */
    public void setInitialDate(LocalDateTime initialDate) {
        this.initialDate = initialDate;
    }

    /**
     * Returns the start date of this project.
     *
     * @return the start date of this project.
     */
    public LocalDateTime getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of this project.
     *
     * @param startDate the new start date of this project.
     */
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date of this project.
     *
     * @return the end date of this project.
     */
    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of this project.
     *
     * @param endDate the new end date of this project.
     */
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the status of this project.
     *
     * @return the status of this project.
     */
    public StateProjectENUM getStatus() {
        return status;
    }

    /**
     * Sets the status of this project.
     *
     * @param status the new status of this project.
     */
    public void setStatus(StateProjectENUM status) {
        this.status = status;
    }

    /**
     * Returns the motivation of this project.
     *
     * @return the motivation of this project.
     */
    public String getMotivation() {
        return motivation;
    }

    /**
     * Sets the motivation of this project.
     *
     * @param motivation the new motivation of this project.
     */
    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    /**
     * Returns the maximum number of members in this project.
     *
     * @return the maximum number of members in this project.
     */
    public int getMaxMembers() {
        return maxMembers;
    }

    /**
     * Sets the maximum number of members in this project.
     *
     * @param maxMembers the new maximum number of members in this project.
     */
    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    /**
     * Returns the photo of this project.
     *
     * @return the photo of this project.
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * Sets the photo of this project.
     *
     * @param photo the new photo of this project.
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * Returns the reason of this project.
     *
     * @return the reason of this project.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason of this project.
     *
     * @param reason the new reason of this project.
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Returns the lab associated with this project.
     *
     * @return the lab associated with this project.
     */
    public LabEntity getLab() {
        return lab;
    }

    /**
     * Sets the lab associated with this project.
     *
     * @param lab the new lab associated with this project.
     */
    public void setLab(LabEntity lab) {
        this.lab = lab;
    }

    /**
     * Returns the deleted status of this project.
     *
     * @return true if the project is deleted, false otherwise.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Sets the deleted status of this project.
     *
     * @param deleted the new deleted status of this project.
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Returns the keywords of this project.
     *
     * @return the keywords of this project.
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * Sets the keywords of this project.
     *
     * @param keywords the new keywords of this project.
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * Returns the resources associated with this project.
     *
     * @return the resources associated with this project.
     */
    public List<ProjectResourceEntity> getProjectResources() {
        return projectResources;
    }

    /**
     * Sets the resources associated with this project.
     *
     * @param projectResources the new resources associated with this project.
     */
    public void setProjectResources(List<ProjectResourceEntity> projectResources) {
        this.projectResources = projectResources;
    }

    /**
     * Returns the users associated with this project.
     *
     * @return the users associated with this project.
     */
    public List<UserProjectEntity> getUserProjects() {
        return userProjects;
    }

    /**
     * Sets the users associated with this project.
     *
     * @param userProjects the new users associated with this project.
     */
    public void setUserProjects(List<UserProjectEntity> userProjects) {
        this.userProjects = userProjects;
    }

    /**
     * Returns the skills associated with this project.
     *
     * @return the skills associated with this project.
     */
    public List<SkillEntity> getSkillInProject() {
        return skillInProject;
    }

    /**
     * Sets the skills associated with this project.
     *
     * @param skillInProject the new skills associated with this project.
     */
    public void setSkillInProject(List<SkillEntity> skillInProject) {
        this.skillInProject = skillInProject;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(LocalDateTime finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getCardPhoto() {
        return cardPhoto;
    }

    public void setCardPhoto(String cardPhoto) {
        this.cardPhoto = cardPhoto;
    }
}
