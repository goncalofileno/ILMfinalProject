package com.ilm.projecto_ilm_backend.dto.user;


import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement
public class UserProjectCreationDto {
 private WorkLocalENUM lab;
 private String name;
 private String photo;
 private List<SkillDto> skills;
 private int id;
 private String systemUsername;

    public UserProjectCreationDto() {
    }


    public WorkLocalENUM getLab() {
        return lab;
    }

    public void setLab(WorkLocalENUM lab) {
        this.lab = lab;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSystemUsername() {
        return systemUsername;
    }

    public void setSystemUsername(String systemUsername) {
        this.systemUsername = systemUsername;
    }
}
