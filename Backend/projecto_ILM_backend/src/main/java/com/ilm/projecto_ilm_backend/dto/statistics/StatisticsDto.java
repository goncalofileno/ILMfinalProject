package com.ilm.projecto_ilm_backend.dto.statistics;

import java.util.ArrayList;

/**
 * Represents statistical data for the system.
 * This class encapsulates various statistics, including total users, average users per project,
 * average execution time per project, and lists of suppliers, members per lab, projects per lab,
 * and projects status numbers per lab.
 */
public class StatisticsDto {
    private int totalUsers; // Total number of users in the system
    private double averageUsersInProject; // Average number of users involved in a project
    private double averageExecutionTimePerProjectInMinutes; // Average execution time per project, in minutes
    private ArrayList<SupplierWithMostResourcesDto> supplierWithMostResources; // List of suppliers with the most resources
    private ArrayList<MembersPerLabDto> membersPerLab; // List of members per laboratory
    private ArrayList<ProjectsPerLabDto> projectsPerLab; // List of projects per laboratory
    private ArrayList<ProjectsStatusNumberPerLab> projectsStatusNumberPerLab; // List of projects status numbers per laboratory

    /**
     * Default constructor.
     */
    public StatisticsDto() {
    }

    /**
     * Gets the total number of users.
     *
     * @return The total number of users.
     */
    public int getTotalUsers() {
        return totalUsers;
    }

    /**
     * Sets the total number of users.
     *
     * @param totalUsers The total number of users to set.
     */
    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    /**
     * Gets the average number of users in a project.
     *
     * @return The average number of users in a project.
     */
    public double getAverageUsersInProject() {
        return averageUsersInProject;
    }

    /**
     * Sets the average number of users in a project.
     *
     * @param averageUsersInProject The average number of users in a project to set.
     */
    public void setAverageUsersInProject(double averageUsersInProject) {
        this.averageUsersInProject = averageUsersInProject;
    }

    /**
     * Gets the average execution time per project in minutes.
     *
     * @return The average execution time per project in minutes.
     */
    public double getAverageExecutionTimePerProject() {
        return averageExecutionTimePerProjectInMinutes;
    }

    /**
     * Sets the average execution time per project in minutes.
     *
     * @param averageExecutionTimePerProject The average execution time per project in minutes to set.
     */
    public void setAverageExecutionTimePerProject(double averageExecutionTimePerProject) {
        this.averageExecutionTimePerProjectInMinutes = averageExecutionTimePerProject;
    }

    /**
     * Gets the list of suppliers with the most resources.
     *
     * @return The list of suppliers with the most resources.
     */
    public ArrayList<SupplierWithMostResourcesDto> getSupplierWithMostResources() {
        return supplierWithMostResources;
    }

    /**
     * Sets the list of suppliers with the most resources.
     *
     * @param supplierWithMostResources The list of suppliers with the most resources to set.
     */
    public void setSupplierWithMostResources(ArrayList<SupplierWithMostResourcesDto> supplierWithMostResources) {
        this.supplierWithMostResources = supplierWithMostResources;
    }

    /**
     * Gets the list of members per laboratory.
     *
     * @return The list of members per laboratory.
     */
    public ArrayList<MembersPerLabDto> getMembersPerLab() {
        return membersPerLab;
    }

    /**
     * Sets the list of members per laboratory.
     *
     * @param membersPerLab The list of members per laboratory to set.
     */
    public void setMembersPerLab(ArrayList<MembersPerLabDto> membersPerLab) {
        this.membersPerLab = membersPerLab;
    }

    /**
     * Gets the list of projects per laboratory.
     *
     * @return The list of projects per laboratory.
     */
    public ArrayList<ProjectsPerLabDto> getProjectsPerLab() {
        return projectsPerLab;
    }

    /**
     * Sets the list of projects per laboratory.
     *
     * @param projectsPerLab The list of projects per laboratory to set.
     */
    public void setProjectsPerLab(ArrayList<ProjectsPerLabDto> projectsPerLab) {
        this.projectsPerLab = projectsPerLab;
    }

    /**
     * Gets the list of projects status numbers per laboratory.
     *
     * @return The list of projects status numbers per laboratory.
     */
    public ArrayList<ProjectsStatusNumberPerLab> getProjectsStatusNumberPerLab() {
        return projectsStatusNumberPerLab;
    }

    /**
     * Sets the list of projects status numbers per laboratory.
     *
     * @param projectsStatusNumberPerLab The list of projects status numbers per laboratory to set.
     */
    public void setProjectsStatusNumberPerLab(ArrayList<ProjectsStatusNumberPerLab> projectsStatusNumberPerLab) {
        this.projectsStatusNumberPerLab = projectsStatusNumberPerLab;
    }
}