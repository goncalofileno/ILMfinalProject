package com.ilm.projecto_ilm_backend.dto.project;


import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import jakarta.ejb.Local;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@XmlRootElement
public class ProjectTableDto {

    private String name;
    private WorkLocalENUM lab;
    private StateProjectENUM status;
    private Date startDate;
    private Date finalDate;
    private int numberOfMembers;
    private int maxMembers;
    private boolean isMember;


    public ProjectTableDto() {
    }

    public ProjectTableDto(String name, WorkLocalENUM lab, StateProjectENUM status, int numberOfMembers, Date startDate, Date finalDate, int maxMembers, boolean isMember) {
        this.name = name;
        this.lab = lab;
        this.status = status;
        this.numberOfMembers = numberOfMembers;
        this.maxMembers = maxMembers;
        this.startDate=startDate;
        this.finalDate=finalDate;
        this.isMember = isMember;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkLocalENUM getLab() {
        return lab;
    }

    public void setLab(WorkLocalENUM lab) {
        this.lab = lab;
    }

    public StateProjectENUM getStatus() {
        return status;
    }

    public void setStatus(StateProjectENUM status) {
        this.status = status;
    }

    public int getNumberOfMembers() {
        return numberOfMembers;
    }

    public void setNumberOfMembers(int numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }
}
