package com.ilm.projecto_ilm_backend.dto.messages;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dto.project.ProjectMemberDto;

import java.util.List;


/**
 * Data Transfer Object for the messages page.
 * This class encapsulates the data required to display messages related to a specific project,
 * including the messages themselves, project name, project state, project members, and the type of user viewing the project.
 */
public class MessagesPageDto {
    /**
     * A list of MessageDto objects representing the messages associated with the project.
     */
    private List<MessageDto> messages;

    /**
     * The name of the project to which the messages are related.
     */
    private String projectName;

    /**
     * The current state of the project, as defined in StateProjectENUM.
     */
    private StateProjectENUM stateProject;

    /**
     * A list of ProjectMemberDto objects representing the members of the project.
     */
    private List<ProjectMemberDto> projectMembers;

    /**
     * The type of user viewing the project, determining their level of access or interaction,
     * as defined in UserInProjectTypeENUM.
     */
    private UserInProjectTypeENUM typeOfUserSeingTheProject;

    /**
     * Constructs a new MessagesPageDto with specified messages, project name, project state, project members, and user type.
     *
     * @param messages A list of MessageDto objects.
     * @param projectName The name of the project.
     * @param stateProject The current state of the project.
     * @param projectMembers A list of ProjectMemberDto objects.
     * @param typeOfUserSeingTheProject The type of user viewing the project.
     */
    public MessagesPageDto(List<MessageDto> messages, String projectName, StateProjectENUM stateProject, List<ProjectMemberDto> projectMembers, UserInProjectTypeENUM typeOfUserSeingTheProject) {
        this.messages = messages;
        this.projectName = projectName;
        this.stateProject = stateProject;
        this.projectMembers = projectMembers;
        this.typeOfUserSeingTheProject = typeOfUserSeingTheProject;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDto> messages) {
        this.messages = messages;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public StateProjectENUM getStateProject() {
        return stateProject;
    }

    public void setStateProject(StateProjectENUM stateProject) {
        this.stateProject = stateProject;
    }

    public List<ProjectMemberDto> getProjectMembers() {
        return projectMembers;
    }

    public void setProjectMembers(List<ProjectMemberDto> projectMembers) {
        this.projectMembers = projectMembers;
    }

    public UserInProjectTypeENUM getTypeOfUserSeingTheProject() {
        return typeOfUserSeingTheProject;
    }

    public void setTypeOfUserSeingTheProject(UserInProjectTypeENUM typeOfUserSeingTheProject) {
        this.typeOfUserSeingTheProject = typeOfUserSeingTheProject;
    }
}
