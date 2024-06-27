package com.ilm.projecto_ilm_backend.dto.project;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

@XmlRootElement
public class ProjectCreationMembersDto {
    private ArrayList<Integer> usersInProject;
    private int maxMembers;

    public ProjectCreationMembersDto() {
    }

    public ArrayList<Integer> getUsersInProject() {
        return usersInProject;
    }

    public void setUsersInProject(ArrayList<Integer> usersInProject) {
        this.usersInProject = usersInProject;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }
}
