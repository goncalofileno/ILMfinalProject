package com.ilm.projecto_ilm_backend.dto.project;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HomeProjectDto {
    private String name;
    private String description;


    public HomeProjectDto(String name, String description) {

        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
