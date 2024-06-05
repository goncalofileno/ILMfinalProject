package com.ilm.projecto_ilm_backend.dto.project;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProjectProfileDto {
    private String name;
    private String typeMember;
    private String status;

// Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeMember() {
        return typeMember;
    }

    public void setTypeMember(String typeMember) {
        this.typeMember = typeMember;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
