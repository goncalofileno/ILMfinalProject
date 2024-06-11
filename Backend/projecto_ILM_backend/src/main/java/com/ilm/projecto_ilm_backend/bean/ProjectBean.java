package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.project.HomeProjectDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectTableDto;

import com.ilm.projecto_ilm_backend.dto.project.ProjectTableInfoDto;
import com.ilm.projecto_ilm_backend.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class ProjectBean {

    @Inject
    private ProjectDao projectDao;

    @Inject
    private LabDao labDao;

    @Inject
    private UserDao userDao;

    @Inject
    private SkillDao skillDao;

    @Inject
    private UserProjectDao userProjectDao;
    @Inject
    SessionDao sessionDao;

    private int numberOfProjectsToCreate=20;

    private static final int NUMBER_OF_PROJECTS_PER_PAGE=15;

    public void createDefaultProjectsIfNotExistent() {

        if(projectDao.countProjects()<numberOfProjectsToCreate) {
            for (int i = 0; i < numberOfProjectsToCreate; i++) {

                ProjectEntity project = new ProjectEntity();

                project.setName("project name" + i);
                project.setDescription("This project aims to develop an innovative software solution for managing large-scale data in real-time. The system will leverage cutting-edge technologies to handle vast amounts of information efficiently.");
                project.setStartDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
                project.setInitialDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
                project.setEndDate(LocalDateTime.now().plus(1, ChronoUnit.YEARS));
                project.setStatus(StateProjectENUM.IN_PROGRESS);
                project.setMotivation("This project aims to develop an innovative software solution for managing large-scale data in real-time. The system will leverage cutting-edge technologies to handle vast amounts of information efficiently.");
                project.setMaxMembers(15);
                LabEntity lab = labDao.findbyLocal(WorkLocalENUM.COIMBRA);
                project.setLab(lab);
                project.setKeywords("Keyword1, Keyword2, Keyword3, Keyword4, Keyword5, Keyword6");

                List<SkillEntity> skillEntities = new ArrayList<>();
                SkillEntity skill1 = skillDao.findById(1);
                skillEntities.add(skill1);
                SkillEntity skill2 = skillDao.findById(2);
                skillEntities.add(skill2);
                project.setSkillInProject(skillEntities);

                projectDao.merge(project);
            }
        }

    }

    public void createDefaultUsersInProjectIfNotExistent() {
        if(userProjectDao.countUserProjects()<20) {

            for (int i = 1; i < numberOfProjectsToCreate+1; i++) {

                ProjectEntity project = projectDao.findById(i);

                UserProjectEntity userProjectEntity = new UserProjectEntity();

                userProjectEntity.setProject(project);
                userProjectEntity.setUser(userDao.findById(1));
                userProjectEntity.setType(UserInProjectTypeENUM.CREATOR);
                userProjectDao.merge(userProjectEntity);

                UserProjectEntity userProjectEntity2 = new UserProjectEntity();

                userProjectEntity2.setProject(project);
                userProjectEntity2.setUser(userDao.findById(2));
                userProjectEntity2.setType(UserInProjectTypeENUM.MEMBER);
                userProjectDao.merge(userProjectEntity2);

            }
        }

    }

    public ArrayList<HomeProjectDto> getProjectsDtosHome() {
        return projectDao.findAllNamesAndDescriptionsHome();
    }

    public ProjectTableInfoDto getProjectTableInfo(String sessionId, int page, String labName, String status, boolean slotsAvailable, String nameAsc,
                                                   String statusAsc,String labAsc,String startDateAsc,String endDateAsc, String keyword) {

        LabEntity lab;
        StateProjectENUM state;
        if(labName == null || labName.equals("")) lab=null;
        else lab=labDao.findbyLocal(WorkLocalENUM.valueOf(labName));
        if(status == null || status.equals("")) state=null;
        else state=StateProjectENUM.valueOf(status);
        if(keyword == null || keyword.equals("")) keyword=null;
        List<Object[]> projectsInfo = projectDao.getProjectTableDtoInfo(page,NUMBER_OF_PROJECTS_PER_PAGE,lab, state, slotsAvailable,nameAsc,
                statusAsc, labAsc,startDateAsc, endDateAsc, keyword);
        ArrayList<ProjectTableDto> projectsTableDtos = new ArrayList<>();

        for (Object[] projectInfo : projectsInfo) {
            ProjectTableDto projectTableDto=new ProjectTableDto();
            projectTableDto.setName((String) projectInfo[1]);
            projectTableDto.setLab(((LabEntity) projectInfo[2]).getLocal());
            projectTableDto.setStatus((StateProjectENUM) projectInfo[3]);
            projectTableDto.setStartDate( (Date) projectInfo[4]);
            projectTableDto.setFinalDate((Date) projectInfo[5]);
            projectTableDto.setMaxMembers((int) projectInfo[6]);
            projectTableDto.setNumberOfMembers(userProjectDao.getNumberOfUsersByProjectId((int)projectInfo[0]));
            projectTableDto.setMember(userProjectDao.isUserInProject((int) projectInfo[0], sessionDao.findUserIdBySessionId(sessionId)));
            projectsTableDtos.add(projectTableDto);

        }

        ProjectTableInfoDto projectTableInfoDto=new ProjectTableInfoDto();

        projectTableInfoDto.setTableProjects(projectsTableDtos);
        System.out.println("number of projects: "+projectDao.getNumberOfProjectsTableDtoInfo(lab,state, slotsAvailable,keyword));
        projectTableInfoDto.setMaxPageNumber(calculateMaximumPageTableProjects( projectDao.getNumberOfProjectsTableDtoInfo(lab,state, slotsAvailable,keyword)));

        return projectTableInfoDto;
    }

    public ArrayList<StateProjectENUM> getAllStatus() {
        ArrayList<StateProjectENUM> status = new ArrayList<>();
        for (StateProjectENUM state : StateProjectENUM.values()) {
            status.add(state);
        }
        return status;
    }


    public int calculateMaximumPageTableProjects(int numberOfProjects){
        return (int) Math.ceil((double) numberOfProjects/NUMBER_OF_PROJECTS_PER_PAGE);
    }

}
