package com.ilm.projecto_ilm_backend.dto.project;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;

import java.util.ArrayList;

public class ProjectProfilePageDto {

    private String title;
    private StateProjectENUM state;
    private String description;
    private String startDate;
    private String endDate;
    private ProjectMemberDto creator;
    private ArrayList<ProjectMemberDto> members;
    private ArrayList<String> keywords;
    private ArrayList<SkillDto> skills;
    private ArrayList<StateProjectENUM> statesToChange;
    private int progress;

    public ProjectProfilePageDto(String title, StateProjectENUM state, String description, String startDate, String endDate, ProjectMemberDto creator, ArrayList<ProjectMemberDto> members, ArrayList<String> keywords, ArrayList<SkillDto> skills, ArrayList<StateProjectENUM> statesToChange) {
        this.title = title;
        this.state = state;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.creator = creator;
        this.members = members;
        this.keywords = keywords;
        this.skills = skills;
        this.statesToChange = statesToChange;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StateProjectENUM getState() {
        return state;
    }

    public void setState(StateProjectENUM state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ProjectMemberDto getCreator() {
        return creator;
    }

    public void setCreator(ProjectMemberDto creator) {
        this.creator = creator;
    }

    public ArrayList<ProjectMemberDto> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<ProjectMemberDto> members) {
        this.members = members;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<SkillDto> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<SkillDto> skills) {
        this.skills = skills;
    }

    public ArrayList<StateProjectENUM> getStatesToChange() {
        return statesToChange;
    }

    public void setStatesToChange(ArrayList<StateProjectENUM> statesToChange) {
        this.statesToChange = statesToChange;
    }
}
