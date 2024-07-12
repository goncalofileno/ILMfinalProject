package com.ilm.projecto_ilm_backend.dto.task;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;

/**
 * Data Transfer Object for representing a member involved in a task.
 * This class encapsulates details about the member, including their ID, name, role in the task,
 * role in the project, system name, and a link to their photo.
 */
public class MemberInTaskDto {

    /**
     * The unique identifier of the member.
     */
    private int id;

    /**
     * The name of the member.
     */
    private String name;

    /**
     * The role of the member in the task, represented as an enum.
     */
    private UserInTaskTypeENUM type;

    /**
     * The role of the member in the project, represented as an enum.
     */
    private UserInProjectTypeENUM typeInProject;

    /**
     * The system name associated with the member.
     */
    private String systemName;

    /**
     * The URL or path to the member's photo.
     */
    private String photo;

    /**
     * Constructs a new MemberInTaskDto with specified details.
     *
     * @param id            The unique identifier of the member.
     * @param name          The name of the member.
     * @param type          The role of the member in the task.
     * @param typeInProject The role of the member in the project.
     * @param systemName    The system name associated with the member.
     * @param photo         The URL or path to the member's photo.
     */
    public MemberInTaskDto(int id, String name, UserInTaskTypeENUM type, UserInProjectTypeENUM typeInProject, String systemName, String photo) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.typeInProject = typeInProject;
        this.systemName = systemName;
        this.photo = photo;
    }

    /**
     * Default constructor.
     */
    public MemberInTaskDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserInTaskTypeENUM getType() {
        return type;
    }

    public void setType(UserInTaskTypeENUM type) {
        this.type = type;
    }

    public UserInProjectTypeENUM getTypeInProject() {
        return typeInProject;
    }

    public void setTypeInProject(UserInProjectTypeENUM typeInProject) {
        this.typeInProject = typeInProject;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
