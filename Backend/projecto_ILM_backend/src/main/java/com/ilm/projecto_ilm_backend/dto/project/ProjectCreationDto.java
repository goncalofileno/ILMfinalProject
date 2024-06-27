package com.ilm.projecto_ilm_backend.dto.project;

import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import java.util.List;

public class ProjectCreationDto {

    private String name;
    private String description;
    private String lab;
    private List<SkillDto> skills;
    private List<InterestDto> keywords;
    private String motivation;
    private String startDate;
    private String endDate;

    public ProjectCreationDto() {
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

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public List<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillDto> skills) {
        this.skills = skills;
    }

    public List<InterestDto> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<InterestDto> keywords) {
        this.keywords = keywords;
    }

    public String getMotivation() {
        return motivation;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
