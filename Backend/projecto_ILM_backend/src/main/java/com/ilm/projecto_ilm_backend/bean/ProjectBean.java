package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.project.HomeProjectDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectTableDto;
import com.ilm.projecto_ilm_backend.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class ProjectBean {

    @Inject
    ProjectDao projectDao;

    @Inject
    LabDao labDao;

    @Inject
    UserDao userDao;

    @Inject
    SkillDao skillDao;

    @Inject
    UserProjectDao userProjectDao;
    @Inject
    SessionDao sessionDao;

    int numberOfProjectsToCreate=20;

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
//        ProjectEntity project1 = projectDao.findById(1);
//        UserProjectEntity userProjectEntity1 = new UserProjectEntity();
//        userProjectEntity1.setId(1);
//        userProjectEntity1.setProject(project1);
//        userProjectEntity1.setUser(userDao.findById(1));
//        userProjectEntity1.setType(UserInProjectTypeENUM.CREATOR);
//        userProjectDao.merge(userProjectEntity1);
//        UserProjectEntity userProjectEntity2 = new UserProjectEntity();
//        userProjectEntity2.setId(2);
//        userProjectEntity2.setProject(project1);
//        userProjectEntity2.setUser(userDao.findById(2));
//        userProjectEntity2.setType(UserInProjectTypeENUM.MEMBER);
//        userProjectDao.merge(userProjectEntity2);

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

    public ArrayList<ProjectTableDto> getProjectsDtosTable(String sessionId) {
        ArrayList<ProjectTableDto> projectsTableDtos = new ArrayList<>();
        List<Object[]> projectsInfo = projectDao.getProjectTableDtoInfo();
        ProjectTableDto projectTableDto=new ProjectTableDto();

        for (Object[] projectInfo : projectsInfo) {
            projectTableDto.setName((String) projectInfo[1]);
            projectTableDto.setLab(((LabEntity) projectInfo[2]).getLocal());
            projectTableDto.setStatus((StateProjectENUM) projectInfo[3]);
            projectTableDto.setStartDate((LocalDate) projectInfo[4]);
            projectTableDto.setFinalDate((LocalDate) projectInfo[5]);
            projectTableDto.setMaxMembers((int) projectInfo[6]);
            projectTableDto.setMember(userProjectDao.isUserInProject((int) projectInfo[0], sessionDao.findUserIdBySessionId(sessionId)));
            projectsTableDtos.add(projectTableDto);

//          projectsTableDtos.add(new ProjectTableDto((String) projectInfo[1], ((LabEntity) projectInfo[2]).getLocal(), (StateProjectENUM) projectInfo[3], userProjectDao.getNumberOfUsersByProjectId((int) projectInfo[0]), (LocalDate) projectInfo[4], (LocalDate) projectInfo[5], (int) projectInfo[6], userProjectDao.isUserInProject((int) projectInfo[0], userId)));

        }

        return projectsTableDtos;
    }
}
