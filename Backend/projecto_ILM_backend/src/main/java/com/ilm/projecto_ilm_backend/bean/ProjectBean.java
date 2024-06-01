package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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

    public void createDefaultProjectsIfNotExistent() {
        if (projectDao.findById(1) == null) {
            ProjectEntity project1 = new ProjectEntity();
            project1.setId(1);
            project1.setName("Project 1");
            project1.setDescription("Description of Project 1 Description of Project 1 Description of Project 1 Description of Project 1 Description of Project 1");
            project1.setStartDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project1.setInitialDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project1.setEndDate(LocalDateTime.now().plus(1, ChronoUnit.YEARS));
            project1.setStatus(StateProjectENUM.PLANING);
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
            project2.setStatus(StateProjectENUM.PLANING);
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

    }
}
