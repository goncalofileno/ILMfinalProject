package com.ilm.projecto_ilm_backend.dto.project;


import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import jakarta.ejb.Local;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Data Transfer Object for displaying project information in a table format.
 * This class encapsulates the essential details of a project necessary for listing in a tabular view,
 * including project photo, name, lab location, status, start and end dates, member count, and completion percentage.
 */
@XmlRootElement
public class ProjectTableDto {

    private String photo; // URL or path to the project's photo
    private String name; // Name of the project
    private WorkLocalENUM lab; // Lab location associated with the project
    private StateProjectENUM status; // Current status of the project
    private Date startDate; // Start date of the project
    private Date finalDate; // End date (expected or actual) of the project
    private int numberOfMembers; // Current number of members in the project
    private int maxMembers; // Maximum allowed number of members in the project
    private boolean isMember; // Flag indicating if the current user is a member of the project
    private int percentageDone; // Percentage of the project completed
    private UserInProjectTypeENUM userInProjectType; // Role of the user in the project
    private String systemProjectName; // System name of the project for internal use

    /**
     * Default constructor.
     */
    public ProjectTableDto() {
    }

    /**
     * Constructs a new ProjectTableDto with specified details.
     *
     * @param photo URL or path to the project's photo.
     * @param name Name of the project.
     * @param lab Lab location associated with the project.
     * @param status Current status of the project.
     * @param numberOfMembers Current number of members in the project.
     * @param startDate Start date of the project.
     * @param finalDate End date (expected or actual) of the project.
     * @param maxMembers Maximum allowed number of members in the project.
     * @param isMember Flag indicating if the current user is a member of the project.
     * @param percentageDone Percentage of the project completed.
     */
    public ProjectTableDto(String photo, String name, WorkLocalENUM lab, StateProjectENUM status, int numberOfMembers, Date startDate, Date finalDate, int maxMembers, boolean isMember, int percentageDone) {
        this.photo = photo;
        this.name = name;
        this.lab = lab;
        this.status = status;
        this.numberOfMembers = numberOfMembers;
        this.maxMembers = maxMembers;
        this.startDate = startDate;
        this.finalDate = finalDate;
        this.isMember = isMember;
        this.percentageDone = percentageDone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public int getPercentageDone() {
        return percentageDone;
    }

    public void setPercentageDone(int percentageDone) {
        this.percentageDone = percentageDone;
    }

    public UserInProjectTypeENUM getUserInProjectType() {
        return userInProjectType;
    }

    public void setUserInProjectType(UserInProjectTypeENUM userInProjectType) {
        this.userInProjectType = userInProjectType;
    }

    public String getSystemProjectName() {
        return systemProjectName;
    }

    public void setSystemProjectName(String systemProjectName) {
        this.systemProjectName = systemProjectName;
    }
}
