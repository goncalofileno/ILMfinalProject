package com.ilm.projecto_ilm_backend.dto.task;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;

public class MemberInTaskDto {

    private int id;
    private String name;
    private UserInTaskTypeENUM type;
    private UserInProjectTypeENUM typeInProject;
    private String systemName;
    private String photo;

    public MemberInTaskDto(int id, String name, UserInTaskTypeENUM type, UserInProjectTypeENUM typeInProject, String systemName, String photo) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.typeInProject = typeInProject;
        this.systemName = systemName;
        this.photo = photo;
    }

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
