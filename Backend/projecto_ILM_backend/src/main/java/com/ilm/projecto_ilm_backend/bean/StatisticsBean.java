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

    private SupplierWithMostResourcesDto getSupplierWithMostResources() {
        List<Object[]> suppliersWithMostResources = resourceSupplierDao.countResourcesPerSupplier();
        SupplierWithMostResourcesDto supplierWithMostResourcesDto = new SupplierWithMostResourcesDto();
        supplierWithMostResourcesDto.setSupplier((String) suppliersWithMostResources.get(0)[0]);
        supplierWithMostResourcesDto.setResources((long) suppliersWithMostResources.get(0)[1]);
        return supplierWithMostResourcesDto;
    }

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

    private ArrayList<ProjectsStatusNumberPerLab> getProjectsStatusNumberPerLabs() {
        List<Object[]> projects = projectDao.countProjectsByStatusAndLab();
        ArrayList<ProjectsStatusNumberPerLab> projectsStatusNumberPerLabs = new ArrayList<>();

        for (WorkLocalENUM location : WorkLocalENUM.values()) {
            ProjectsStatusNumberPerLab projectStatusNumberPerLab = new ProjectsStatusNumberPerLab(convertToTitleCase(location.toString()));
            projectsStatusNumberPerLabs.add(projectStatusNumberPerLab);
            for(StateProjectENUM state : StateProjectENUM.values()){
                projectStatusNumberPerLab.addStatusNumber(new StatusNumberDto(state, 0L));
            }
        }
        for (Object[] project : projects) {
            boolean found = false;
            for (int i=0; i<projectsStatusNumberPerLabs.size() && !found; i++) {
                if (projectsStatusNumberPerLabs.get(i).getLab().equals((WorkLocalENUM) project[0])) {
                    projectsStatusNumberPerLabs.get(i).setStatusNumber((long) project[2], (StateProjectENUM) project[1]);
                    found = true;
                }
            }

        }
        return projectsStatusNumberPerLabs;
    }

    private String convertToTitleCase(String location) {
        // Convert the first character to uppercase and the rest to lowercase
        if(location.equals("VILA_REAL")){return "Vila Real";}
        return location.substring(0, 1).toUpperCase() + location.substring(1).toLowerCase();
    }
}
