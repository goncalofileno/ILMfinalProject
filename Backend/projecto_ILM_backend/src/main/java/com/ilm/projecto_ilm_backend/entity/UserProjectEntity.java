package com.ilm.projecto_ilm_backend.entity;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.ConvertersENUM.UserInProjectTypeEnumConverter;
import jakarta.persistence.*;

import java.io.Serializable;

/**
 * The UserProjectEntity class represents the "user_project" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "user_project")
@NamedQueries({
        @NamedQuery(name = "UserProject.findById", query = "SELECT up FROM UserProjectEntity up WHERE up.id = :id"),
        @NamedQuery(name = "UserProject.findByProjectId", query = "SELECT up FROM UserProjectEntity up WHERE up.project.id = :projectId"),
        @NamedQuery(name = "UserProject.findByUserId", query = "SELECT up FROM UserProjectEntity up WHERE up.user.id = :userId"),
        @NamedQuery(name = "UserProject.findNumberOfUsers", query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.project.id = :projectId AND (up.type=0 OR up.type=1 OR up.type=2 OR up.type=3 OR up.type=4)"),
        @NamedQuery(name = "UserProject.isUserInProject", query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.project.id = :projectId AND up.user.id = :userId"),
        @NamedQuery(name = "UserProject.countUserProjects", query = "SELECT COUNT(up) FROM UserProjectEntity up"),
        @NamedQuery(name = "UserProject.isUserAlreadyInvited", query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.user.id = :userId AND up.project.id = :projectId AND up.type = :invited"),
        @NamedQuery(name = "UserProject.findByUserIdAndProjectId", query = "SELECT up FROM UserProjectEntity up WHERE up.user.id = :userId AND up.project.id = :projectId"),
        @NamedQuery(name = "UserProject.findByUserIdAndProjectIdAndType", query = "SELECT up FROM UserProjectEntity up WHERE up.user.id = :userId AND up.project.id = :projectId AND up.type = :type"),
        @NamedQuery(name = "UserProject.findMembersByProjectId", query = "SELECT up FROM UserProjectEntity up WHERE up.project.id = :projectId AND (up.type=0 OR up.type=1 OR up.type=2 OR up.type=3 OR up.type=4)"),
        @NamedQuery(name = "UserProject.findCreatorByProjectId", query = "SELECT up.user FROM UserProjectEntity up WHERE up.project.id = :projectId AND up.type = 0"),
        @NamedQuery(name = "UserProject.findCreatorsAndManagersByProjectId", query = "SELECT up.user FROM UserProjectEntity up WHERE up.project.id = :projectId AND (up.type=0 OR up.type=1)"),
        @NamedQuery(name = "UserProject.findUserTypeByUserIdAndProjectId", query = "SELECT up.type FROM UserProjectEntity up WHERE up.user.id = :userId AND up.project.id = :projectId")
})
public class UserProjectEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the user project. This is the primary key in the "user_project" table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private int id;

    /**
     * The user associated with this user project. This is a many-to-one relationship with the UserEntity class.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * The project associated with this user project. This is a many-to-one relationship with the ProjectEntity class.
     */
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    /**
     * The type of the user in the project. This is an enumerated type.
     */
    @Convert(converter = UserInProjectTypeEnumConverter.class)
    @Column(name = "type", nullable = false, unique = false, updatable = true)
    private UserInProjectTypeENUM type;

    /**
     * Default constructor.
     */
    public UserProjectEntity() {
    }

    /**
     * Returns the unique ID of this user project.
     *
     * @return the ID of this user project.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique ID of this user project.
     *
     * @param id the new ID of this user project.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the user associated with this user project.
     *
     * @return the user associated with this user project.
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Sets the user associated with this user project.
     *
     * @param user the new user associated with this user project.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Returns the project associated with this user project.
     *
     * @return the project associated with this user project.
     */
    public ProjectEntity getProject() {
        return project;
    }

    /**
     * Sets the project associated with this user project.
     *
     * @param project the new project associated with this user project.
     */
    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    /**
     * Returns the type of the user in the project.
     *
     * @return the type of the user in the project.
     */
    public UserInProjectTypeENUM getType() {
        return type;
    }

    /**
     * Sets the type of the user in the project.
     *
     * @param type the new type of the user in the project.
     */
    public void setType(UserInProjectTypeENUM type) {
        this.type = type;
    }
}
