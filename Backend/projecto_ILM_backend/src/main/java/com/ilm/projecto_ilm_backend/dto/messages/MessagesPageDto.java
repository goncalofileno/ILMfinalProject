package com.ilm.projecto_ilm_backend.dto.messages;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dto.project.ProjectMemberDto;

import java.util.List;


public class MessagesPageDto {
    private List<MessageDto> messages;
    private String projectName;
    private StateProjectENUM stateProject;
    private List<ProjectMemberDto> projectMembers;
    private UserInProjectTypeENUM typeOfUserSeingTheProject;

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
