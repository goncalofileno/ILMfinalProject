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
        // Query to find a UserProjectEntity by its id.
        @NamedQuery(name = "UserProject.findById", query = "SELECT up FROM UserProjectEntity up WHERE up.id = :id"),
        // Query to find a UserProjectEntity by project id.
        @NamedQuery(name = "UserProject.findByProjectId", query = "SELECT up FROM UserProjectEntity up WHERE up.project.id = :projectId"),
        // Query to find a UserProjectEntity by user id.
        @NamedQuery(name = "UserProject.findByUserId", query = "SELECT up FROM UserProjectEntity up WHERE up.user.id = :userId"),
        // Query to find the number of users in a project.
        @NamedQuery(name = "UserProject.findNumberOfUsers", query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.project.id = :projectId AND (up.type=0 OR up.type=1 OR up.type=2 OR up.type=3 OR up.type=4)"),
        // Query to check if a user is in a project.
        @NamedQuery(name = "UserProject.isUserInProject", query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.project.id = :projectId AND up.user.id = :userId AND (up.type=0 OR up.type=1 OR up.type=2 OR up.type=3 OR up.type=4)"),
        // Query to count the number of user projects.
        @NamedQuery(name = "UserProject.countUserProjects", query = "SELECT COUNT(up) FROM UserProjectEntity up"),
        // Query to check if a user is already invited to a project.
        @NamedQuery(name = "UserProject.isUserAlreadyInvited", query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.user.id = :userId AND up.project.id = :projectId AND up.type = :invited"),
        // Query to find a UserProjectEntity by user id and project id.
        @NamedQuery(name = "UserProject.findByUserIdAndProjectId", query = "SELECT up FROM UserProjectEntity up WHERE up.user.id = :userId AND up.project.id = :projectId"),
        // Query to find a UserProjectEntity by user id, project id and type.
        @NamedQuery(name = "UserProject.findByUserIdAndProjectIdAndType", query = "SELECT up FROM UserProjectEntity up WHERE up.user.id = :userId AND up.project.id = :projectId AND up.type = :type"),
        // Query to find members of a project by project id.
        @NamedQuery(name = "UserProject.findMembersByProjectId", query = "SELECT up FROM UserProjectEntity up WHERE up.project.id = :projectId AND (up.type=0 OR up.type=1 OR up.type=2 OR up.type=3 OR up.type=4)"),
        // Query to find all types of members of a project by project id.
        @NamedQuery(name = "UserProject.findAllTypeOfMembersByProjectId", query = "SELECT up FROM UserProjectEntity up WHERE up.project.id = :projectId AND (up.type=0 OR up.type=1 OR up.type=2 OR up.type=3 OR up.type=4 OR up.type=5 OR up.type=6)"),
        // Query to find the creator of a project by project id.
        @NamedQuery(name = "UserProject.findCreatorByProjectId", query = "SELECT up.user FROM UserProjectEntity up WHERE up.project.id = :projectId AND up.type = 0"),
        // Query to find the creators and managers of a project by project id.
        @NamedQuery(name = "UserProject.findCreatorsAndManagersByProjectId", query = "SELECT up.user FROM UserProjectEntity up WHERE up.project.id = :projectId AND (up.type=0 OR up.type=1)"),
        // Query to find the type of user in a project by user id and project id.
        @NamedQuery(name = "UserProject.findUserTypeByUserIdAndProjectId", query = "SELECT up.type FROM UserProjectEntity up WHERE up.user.id = :userId AND up.project.id = :projectId"),
        // Query to find members of a project by project id.
        @NamedQuery(name = "UserProject.findMembersEntityByProjectId", query = "SELECT up.user FROM UserProjectEntity up WHERE up.project.id = :projectId AND (up.type=0 OR up.type=1 OR up.type=2 OR up.type=3 OR up.type=4)"),
        // Query to check if a user is the creator of a project.
        @NamedQuery(name = "UserProject.isUserCreator", query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.project.id = :projectId AND up.user.id = :userId AND up.type = 0"),
        // Query to check if a user is the creator or manager of a project.
        @NamedQuery(name = "UserProject.isUserCreatorOrManager", query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.project.id = :projectId AND up.user.id = :userId AND (up.type = 0 OR up.type = 1)"),
        // Query to count the members of a project by project id.
        @NamedQuery(name = "UserProject.countMembersByProjectId", query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.project.id = :projectId"),
        // Query to check if a user has projects.
        @NamedQuery(name = "UserProject.userHasProjects" , query = "SELECT COUNT(up) FROM UserProjectEntity up WHERE up.user.id = :userId AND (up.type=0 OR up.type=1 OR up.type=2 OR up.type=3 OR up.type=4)" ),

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * The project associated with this user project. This is a many-to-one relationship with the ProjectEntity class.
     */
    @ManyToOne(fetch = FetchType.EAGER)
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
