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

    public void createDefaultProjectsIfNotExistent() {
        if (projectDao.findById(1) == null) {
            ProjectEntity project1 = new ProjectEntity();
            project1.setId(1);
            project1.setName("Project 1");
            project1.setDescription("Description of Project 1 Description of Project 1 Description of Project 1 Description of Project 1 Description of Project 1");
            project1.setStartDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project1.setInitialDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project1.setEndDate(LocalDateTime.now().plus(1, ChronoUnit.YEARS));
            project1.setStatus(StateProjectENUM.PLANNING);
            project1.setMotivation("Motivation of Project 1 Motivation of Project 1 Motivation of Project 1 Motivation of Project 1 Motivation of Project 1");
            project1.setMaxMembers(15);
            LabEntity lab1 = new LabEntity();
            lab1 = labDao.findbyLocal(WorkLocalENUM.COIMBRA);
            project1.setLab(lab1);
            project1.setKeywords("Keyword1, Keyword2, Keyword3, Keyword4, Keyword5, Keyword6");
            List<SkillEntity> skillEntities = new ArrayList<>();
            SkillEntity skill1 = skillDao.findById(1);
            skillEntities.add(skill1);
            SkillEntity skill2 = skillDao.findById(2);
            skillEntities.add(skill2);
            project1.setSkillInProject(skillEntities);
            projectDao.merge(project1);
        }

        if (projectDao.findById(2) == null) {
            ProjectEntity project2 = new ProjectEntity();
            project2.setId(2);
            project2.setName("Project 2");
            project2.setDescription("Description of Project 2 Description of Project 2 Description of Project 2 Description of Project 2 Description of Project 2");
            project2.setStartDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project2.setInitialDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project2.setEndDate(LocalDateTime.now().plus(1, ChronoUnit.YEARS));
            project2.setStatus(StateProjectENUM.PLANNING);
            project2.setMotivation("Motivation of Project 2 Motivation of Project 2 Motivation of Project 2 Motivation of Project 2 Motivation of Project 2");
            project2.setMaxMembers(15);
            LabEntity lab2 = new LabEntity();
            lab2 = labDao.findbyLocal(WorkLocalENUM.COIMBRA);
            project2.setLab(lab2);
            project2.setKeywords("Keyword1, Keyword2, Keyword3, Keyword4, Keyword5, Keyword6");
            List<SkillEntity> skillEntities2 = new ArrayList<>();
            SkillEntity skill3 = skillDao.findById(3);
            skillEntities2.add(skill3);
            SkillEntity skill4 = skillDao.findById(4);
            skillEntities2.add(skill4);
            project2.setSkillInProject(skillEntities2);
            projectDao.merge(project2);
        }
        if (projectDao.findById(3) == null) {
            ProjectEntity project3 = new ProjectEntity();
            project3.setId(3);
            project3.setName("Project 3");
            project3.setDescription("Description of Project 3 Description of Project 3 Description of Project 3 Description of Project 3 Description of Project 3");
            project3.setStartDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project3.setInitialDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project3.setEndDate(LocalDateTime.now().plus(1, ChronoUnit.YEARS));
            project3.setStatus(StateProjectENUM.PLANNING);
            project3.setMotivation("Motivation of Project 3 Motivation of Project 3 Motivation of Project 3 Motivation of Project 3 Motivation of Project 3");
            project3.setMaxMembers(15);
            LabEntity lab3 = new LabEntity();
            lab3 = labDao.findbyLocal(WorkLocalENUM.COIMBRA);
            project3.setLab(lab3);
            project3.setKeywords("Keyword1, Keyword2, Keyword3, Keyword4, Keyword5, Keyword6");
            List<SkillEntity> skillEntities3 = new ArrayList<>();
            SkillEntity skill3 = skillDao.findById(3);
            skillEntities3.add(skill3);
            SkillEntity skill4 = skillDao.findById(4);
            skillEntities3.add(skill4);
            project3.setSkillInProject(skillEntities3);
            projectDao.merge(project3);
        }
        if (projectDao.findById(4) == null) {
            ProjectEntity project4 = new ProjectEntity();
            project4.setId(4);
            project4.setName("Project 4");
            project4.setDescription("Description of Project 4 Description of Project 4 Description of Project 4 Description of Project 4 Description of Project 4");
            project4.setStartDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project4.setInitialDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project4.setEndDate(LocalDateTime.now().plus(1, ChronoUnit.YEARS));
            project4.setStatus(StateProjectENUM.PLANNING);
            project4.setMotivation("Motivation of Project 4 Motivation of Project 4 Motivation of Project 4 Motivation of Project 4 Motivation of Project 4");
            project4.setMaxMembers(15);
            LabEntity lab4 = new LabEntity();
            lab4 = labDao.findbyLocal(WorkLocalENUM.COIMBRA);
            project4.setLab(lab4);
            project4.setKeywords("Keyword1, Keyword2, Keyword3, Keyword4, Keyword5, Keyword6");
            List<SkillEntity> skillEntities4 = new ArrayList<>();
            SkillEntity skill3 = skillDao.findById(3);
            skillEntities4.add(skill3);
            SkillEntity skill4 = skillDao.findById(4);
            skillEntities4.add(skill4);
            project4.setSkillInProject(skillEntities4);
            projectDao.merge(project4);
        }
    }

    public void createDefaultUsersInProjectIfNotExistent() {
        ProjectEntity project1 = projectDao.findById(1);
        UserProjectEntity userProjectEntity1 = new UserProjectEntity();
        userProjectEntity1.setId(1);
        userProjectEntity1.setProject(project1);
        userProjectEntity1.setUser(userDao.findById(1));
        userProjectEntity1.setType(UserInProjectTypeENUM.CREATOR);
        userProjectDao.merge(userProjectEntity1);
        UserProjectEntity userProjectEntity2 = new UserProjectEntity();
        userProjectEntity2.setId(2);
        userProjectEntity2.setProject(project1);
        userProjectEntity2.setUser(userDao.findById(2));
        userProjectEntity2.setType(UserInProjectTypeENUM.MEMBER);
        userProjectDao.merge(userProjectEntity2);

        ProjectEntity project2 = projectDao.findById(2);
        UserProjectEntity userProjectEntity3 = new UserProjectEntity();
        userProjectEntity3.setId(3);
        userProjectEntity3.setProject(project2);
        userProjectEntity3.setUser(userDao.findById(2));
        userProjectEntity3.setType(UserInProjectTypeENUM.CREATOR);
        userProjectDao.merge(userProjectEntity3);
        UserProjectEntity userProjectEntity4 = new UserProjectEntity();
        userProjectEntity4.setId(4);
        userProjectEntity4.setProject(project2);
        userProjectEntity4.setUser(userDao.findById(1));
        userProjectEntity4.setType(UserInProjectTypeENUM.MEMBER);
        userProjectDao.merge(userProjectEntity4);

        ProjectEntity project3 = projectDao.findById(3);
        UserProjectEntity userProjectEntity5 = new UserProjectEntity();
        userProjectEntity5.setId(5);
        userProjectEntity5.setProject(project3);
        userProjectEntity5.setUser(userDao.findById(2));
        userProjectEntity5.setType(UserInProjectTypeENUM.CREATOR);
        userProjectDao.merge(userProjectEntity5);
        UserProjectEntity userProjectEntity6 = new UserProjectEntity();
        userProjectEntity6.setId(6);
        userProjectEntity6.setProject(project3);
        userProjectEntity6.setUser(userDao.findById(1));
        userProjectEntity6.setType(UserInProjectTypeENUM.MEMBER);
        userProjectDao.merge(userProjectEntity6);

        ProjectEntity project4 = projectDao.findById(4);
        UserProjectEntity userProjectEntity7 = new UserProjectEntity();
        userProjectEntity7.setId(7);
        userProjectEntity7.setProject(project4);
        userProjectEntity7.setUser(userDao.findById(2));
        userProjectEntity7.setType(UserInProjectTypeENUM.CREATOR);
        userProjectDao.merge(userProjectEntity7);
        UserProjectEntity userProjectEntity8 = new UserProjectEntity();
        userProjectEntity8.setId(8);
        userProjectEntity8.setProject(project4);
        userProjectEntity8.setUser(userDao.findById(1));
        userProjectEntity8.setType(UserInProjectTypeENUM.MEMBER);
        userProjectDao.merge(userProjectEntity8);

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
