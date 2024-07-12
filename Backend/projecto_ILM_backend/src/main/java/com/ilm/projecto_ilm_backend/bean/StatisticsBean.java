package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.dao.ProjectDao;
import com.ilm.projecto_ilm_backend.dao.ResourceSupplierDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dao.UserProjectDao;
import com.ilm.projecto_ilm_backend.dto.statistics.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean responsible for aggregating and calculating various statistics related to users, projects, and resources
 * within the application. It provides methods to calculate averages, totals, and distributions of different entities
 * and their attributes across the application.
 */
@ApplicationScoped
public class StatisticsBean {
    @Inject
    UserDao userDao;
    @Inject
    ProjectDao projectDao;
    @Inject
    UserProjectDao userProjectDao;
    @Inject
    ResourceSupplierDao resourceSupplierDao;

    /**
     * Compiles a comprehensive set of statistics from various aspects of the application, including user counts,
     * project execution times, and resource allocations. This method serves as a central point for gathering
     * statistical data to be used for reporting and analysis.
     *
     * @return A {@link StatisticsDto} object containing aggregated statistical data.
     */
    public StatisticsDto getStatistics(){
        StatisticsDto statisticsDto = new StatisticsDto();
        statisticsDto.setTotalUsers(userDao.getNumberOfUsersInApp());
        statisticsDto.setAverageUsersInProject(getAverageUsersInProject());
        statisticsDto.setAverageExecutionTimePerProject(getProjectsAverageExecution());
        statisticsDto.setSupplierWithMostResources(getSupplierWithMostResources());
        statisticsDto.setMembersPerLab(getMembersPerLabDto());
        statisticsDto.setProjectsPerLab(getProjectsPerLabDto());
        statisticsDto.setProjectsStatusNumberPerLab(getProjectsStatusNumberPerLabs());
        return statisticsDto;
    }

    /**
     * Calculates the average number of users per project across all projects in the application.
     *
     * @return The average number of users per project as a double.
     */
    private double getAverageUsersInProject() {
        List<Integer> projectsIds = projectDao.getProjectIds();
        double totalUsers = 0.0;
        for (Integer projectId : projectsIds) {
            totalUsers += userProjectDao.countUsersInProject(projectId.intValue());
        }
        double averageUsers = totalUsers / projectsIds.size();

        final int DECIMAL_PLACES=1000;
        int averageUsersInt= (int) (averageUsers*DECIMAL_PLACES);

        double decimals= (averageUsers*DECIMAL_PLACES)-averageUsersInt;

        averageUsers=((averageUsers*DECIMAL_PLACES)-decimals)/1000;

        return averageUsers;
    }


    /**
     * Calculates the average execution time for projects, based on their start and end dates.
     *
     * @return The average execution time per project in minutes as a double.
     */
    private double getProjectsAverageExecution() {
        List<Object[]> projectsExecutionTime = projectDao.getProjectsExecutionDates();

        double totalExecutionTime = 0.0;
        int countProjects = 0;

        for (Object[] projectExecutionTime : projectsExecutionTime) {
            if((projectExecutionTime[0] != null && projectExecutionTime[1] != null) && (((LocalDateTime) projectExecutionTime[0]).isBefore((LocalDateTime) projectExecutionTime[1])) ){
                Duration duration = Duration.between((LocalDateTime) projectExecutionTime[0], (LocalDateTime) projectExecutionTime[1]);
                totalExecutionTime += duration.toMinutes();
                countProjects++;
            }
        }

        double averageExecutionTime = totalExecutionTime / countProjects;

        return averageExecutionTime;

    }

    /**
     * Identifies the suppliers with the most resources allocated and compiles a list of the top suppliers.
     *
     * @return An ArrayList of {@link SupplierWithMostResourcesDto} objects representing the suppliers with the most resources.
     */
    private ArrayList<SupplierWithMostResourcesDto> getSupplierWithMostResources() {
        List<Object[]> suppliersWithMostResources = resourceSupplierDao.countResourcesPerSupplier();
        ArrayList<SupplierWithMostResourcesDto> supplierWithMostResourcesDtos = new ArrayList<>();
        for (int i =0; i < 5 && i < suppliersWithMostResources.size() ; i++) {
            SupplierWithMostResourcesDto supplierWithMostResourcesDto = new SupplierWithMostResourcesDto();
            supplierWithMostResourcesDto.setSupplier((String) suppliersWithMostResources.get(i)[0]);
            supplierWithMostResourcesDto.setResources((long) suppliersWithMostResources.get(i)[1]);
            supplierWithMostResourcesDtos.add(supplierWithMostResourcesDto);
        }

        return supplierWithMostResourcesDtos;
    }

    /**
     * Compiles a list of labs and the number of members associated with each, providing insight into the distribution
     * of users across different work locations.
     *
     * @return An ArrayList of {@link MembersPerLabDto} objects representing the number of members per lab.
     */
    private ArrayList<MembersPerLabDto> getMembersPerLabDto(){
        List<Object[]> membersPerLab = userDao.getUsersPerLab();
        ArrayList<MembersPerLabDto> membersPerLabDtos = new ArrayList<>();
        for (Object[] memberPerLab : membersPerLab) {
            MembersPerLabDto membersPerLabDto = new MembersPerLabDto();
            membersPerLabDto.setWorkLocal((WorkLocalENUM) memberPerLab[0]);
            membersPerLabDto.setMembers((long) memberPerLab[1]);
            membersPerLabDtos.add(membersPerLabDto);
        }
        return membersPerLabDtos;
    }

    /**
     * Compiles a list of labs and the number of projects associated with each, offering a view into the distribution
     * of projects across different work locations.
     *
     * @return An ArrayList of {@link ProjectsPerLabDto} objects representing the number of projects per lab.
     */
    private ArrayList<ProjectsPerLabDto> getProjectsPerLabDto(){
        List<Object[]> projectsPerLab = projectDao.getProjectsPerLab();
        ArrayList<ProjectsPerLabDto> projectsPerLabDtos = new ArrayList<>();

        for(Object[] project : projectsPerLab){
            ProjectsPerLabDto projectsPerLabDto = new ProjectsPerLabDto();
            projectsPerLabDto.setWorkLocal((WorkLocalENUM) project[0]);
            projectsPerLabDto.setProjects((long) project[1]);
            projectsPerLabDtos.add(projectsPerLabDto);
        }
        return projectsPerLabDtos;
    }

    /**
     * Aggregates the number of projects by their status for each lab, providing a detailed view of project progress
     * across different locations.
     *
     * @return An ArrayList of {@link ProjectsStatusNumberPerLab} objects representing the number of projects by status per lab.
     */
    private ArrayList<ProjectsStatusNumberPerLab> getProjectsStatusNumberPerLabs() {
        List<Object[]> projects = projectDao.countProjectsByStatusAndLab();
        ArrayList<ProjectsStatusNumberPerLab> projectsStatusNumberPerLabs = new ArrayList<>();

        for (WorkLocalENUM location : WorkLocalENUM.values()) {
            ProjectsStatusNumberPerLab projectStatusNumberPerLab = new ProjectsStatusNumberPerLab(location.toString());
            projectsStatusNumberPerLabs.add(projectStatusNumberPerLab);
            for(StateProjectENUM state : StateProjectENUM.values()){
                projectStatusNumberPerLab.addStatusNumber(new StatusNumberDto(state, 0L));
            }
        }
        for (Object[] project : projects) {
            boolean found = false;
            for (int i=0; i<projectsStatusNumberPerLabs.size() && !found; i++) {
                if (projectsStatusNumberPerLabs.get(i).getLab().equals( project[0].toString())) {
                    projectsStatusNumberPerLabs.get(i).setStatusNumber((long) project[2], (StateProjectENUM) project[1]);
                    found = true;
                }
            }
        }

        for (int i=0; i<projectsStatusNumberPerLabs.size(); i++) {
            projectsStatusNumberPerLabs.get(i).setLab(convertToTitleCase(projectsStatusNumberPerLabs.get(i).getLab()));
        }

        return projectsStatusNumberPerLabs;
    }

    /**
     * Converts a string representing a location into title case, improving readability for presentation purposes.
     *
     * @param location The location string to be converted.
     * @return A string representing the location in title case.
     */
    private String convertToTitleCase(String location) {
        // Convert the first character to uppercase and the rest to lowercase
        if(location.equals("VILA_REAL")){return "Vila Real";}
        return location.substring(0, 1).toUpperCase() + location.substring(1).toLowerCase();
    }
}
