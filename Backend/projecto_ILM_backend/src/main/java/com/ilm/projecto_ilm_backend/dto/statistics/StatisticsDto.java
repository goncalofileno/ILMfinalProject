package com.ilm.projecto_ilm_backend.dto.statistics;

import java.util.ArrayList;

public class StatisticsDto {
    private int totalUsers;
    private double averageUsersInProject;
    private double averageExecutionTimePerProjectInMinutes;
    private SupplierWithMostResourcesDto supplierWithMostResources;
    private ArrayList<MembersPerLabDto> membersPerLab;
    private ArrayList<ProjectsPerLabDto> projectsPerLab;
    private ArrayList<ProjectsStatusNumberPerLab> projectsStatusNumberPerLab;

    public StatisticsDto() {
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public double getAverageUsersInProject() {
        return averageUsersInProject;
    }

    public void setAverageUsersInProject(double averageUsersInProject) {
        this.averageUsersInProject = averageUsersInProject;
    }

    public double getAverageExecutionTimePerProject() {
        return averageExecutionTimePerProjectInMinutes;
    }

    public void setAverageExecutionTimePerProject(double averageExecutionTimePerProject) {
        this.averageExecutionTimePerProjectInMinutes = averageExecutionTimePerProject;
    }

    public SupplierWithMostResourcesDto getSupplierWithMostResources() {
        return supplierWithMostResources;
    }

    public void setSupplierWithMostResources(SupplierWithMostResourcesDto supplierWithMostResources) {
        this.supplierWithMostResources = supplierWithMostResources;
    }

    public ArrayList<MembersPerLabDto> getMembersPerLab() {
        return membersPerLab;
    }

    public void setMembersPerLab(ArrayList<MembersPerLabDto> membersPerLab) {
        this.membersPerLab = membersPerLab;
    }

    public ArrayList<ProjectsPerLabDto> getProjectsPerLab() {
        return projectsPerLab;
    }

    public void setProjectsPerLab(ArrayList<ProjectsPerLabDto> projectsPerLab) {
        this.projectsPerLab = projectsPerLab;
    }

    public ArrayList<ProjectsStatusNumberPerLab> getProjectsStatusNumberPerLab() {
        return projectsStatusNumberPerLab;
    }

    public void setProjectsStatusNumberPerLab(ArrayList<ProjectsStatusNumberPerLab> projectsStatusNumberPerLab) {
        this.projectsStatusNumberPerLab = projectsStatusNumberPerLab;
    }
}
