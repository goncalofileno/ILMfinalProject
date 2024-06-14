package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.mail.MailDto;
import com.ilm.projecto_ilm_backend.dto.project.HomeProjectDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectProfileDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectTableDto;

import com.ilm.projecto_ilm_backend.dto.project.ProjectTableInfoDto;
import com.ilm.projecto_ilm_backend.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private NotificationBean notificationBean;

    @Inject
    SessionDao sessionDao;
    @Inject
    MailBean mailBean;

    private int numberOfProjectsToCreate=20;

    private static final int NUMBER_OF_PROJECTS_PER_PAGE=15;

    public void createDefaultProjectsIfNotExistent() {

        if (projectDao.countProjects() < numberOfProjectsToCreate) {
            for (int i = 0; i < numberOfProjectsToCreate; i++) {

                ProjectEntity project = new ProjectEntity();

                project.setName("project name" + i);
                project.setSystemName(projectSystemNameGenerator(project.getName()));
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

    private String projectSystemNameGenerator(String originalName) {
        // Convert to lower case
        String lowerCaseName = originalName.toLowerCase();

        // Normalize and remove accents
        String normalized = Normalizer.normalize(lowerCaseName, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String withoutAccents = pattern.matcher(normalized).replaceAll("");

        // Replace spaces with underscores
        String withUnderscores = withoutAccents.replaceAll("\\s+", "_");

        // Remove all non-alphanumeric characters except underscores
        String cleanSystemName = withUnderscores.replaceAll("[^a-z0-9_]", "");

        return cleanSystemName;
    }

    public void createDefaultUsersInProjectIfNotExistent() {
        if(userProjectDao.countUserProjects()<20) {

            for (int i = 1; i < numberOfProjectsToCreate + 1; i++) {

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
        List<Object[]>  projectsInfo;
        if(labName == null || labName.equals("")) lab=null;
        else lab=labDao.findbyLocal(WorkLocalENUM.valueOf(labName));
        if(status.equals("")) state=null;
        else state=StateProjectENUM.valueOf(status);
        if( keyword.equals("")) keyword=null;

        if(lab==null && (status==null || status.equals("")) && !slotsAvailable && (nameAsc==null || nameAsc.equals("")) && (statusAsc==null || statusAsc.equals("")) && (labAsc==null || labAsc.equals("")) && (startDateAsc==null || startDateAsc.equals("")) && (endDateAsc==null || endDateAsc.equals("")) && (keyword==null || keyword.equals(""))){
            int userId=sessionDao.findUserIdBySessionId(sessionId);
            projectsInfo=projectDao.findAllProjectsOrderedByUser(page, NUMBER_OF_PROJECTS_PER_PAGE,userId);
       }else {
             projectsInfo = projectDao.getProjectTableDtoInfo(page,NUMBER_OF_PROJECTS_PER_PAGE,lab, state, slotsAvailable,nameAsc,
                    statusAsc, labAsc,startDateAsc, endDateAsc, keyword);
       }

        ArrayList<ProjectTableDto> projectsTableDtos = new ArrayList<>();

        for (Object[] projectInfo : projectsInfo) {
            ProjectTableDto projectTableDto = new ProjectTableDto();
            projectTableDto.setName((String) projectInfo[1]);
            projectTableDto.setLab(((LabEntity) projectInfo[2]).getLocal());
            projectTableDto.setStatus((StateProjectENUM) projectInfo[3]);
            projectTableDto.setStartDate((Date) projectInfo[4]);
            projectTableDto.setFinalDate((Date) projectInfo[5]);
            projectTableDto.setMaxMembers((int) projectInfo[6]);
            projectTableDto.setNumberOfMembers(userProjectDao.getNumberOfUsersByProjectId((int) projectInfo[0]));
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

    public List<ProjectProfileDto> getProjectsByUserRoleToInvite(int userId, UserInProjectTypeENUM... roles) {
        List<UserProjectEntity> userProjects = userProjectDao.findByUserId(userId);
        return userProjects.stream()
                .filter(up -> List.of(roles).contains(up.getType()) &&
                        up.getProject().getStatus() != StateProjectENUM.CANCELED &&
                        up.getProject().getStatus() != StateProjectENUM.FINISHED)
                .map(up -> {
                    ProjectProfileDto dto = new ProjectProfileDto();
                    dto.setName(up.getProject().getName());
                    dto.setTypeMember(up.getType().name());
                    dto.setStatus(up.getProject().getStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public boolean isUserCreatorOrManager(int userId, String projectName) {
        ProjectEntity project = projectDao.findByName(projectName);
        return userProjectDao.isUserCreatorOrManager(userId, project.getId());
    }

    public String inviteUserToProject(String sessionId, String systemUsername, String projectName) {
        UserEntity userToInvite = userDao.findBySystemUsername(systemUsername);
        ProjectEntity project = projectDao.findByName(projectName);
        UserEntity sender = sessionDao.findBySessionId(sessionId).getUser();

        if (userProjectDao.isUserAlreadyInvited(userToInvite.getId(), project.getId())) {
            return "User is already invited to this project";
        }

        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setUser(userToInvite);
        userProjectEntity.setProject(project);
        userProjectEntity.setType(UserInProjectTypeENUM.PENDING_BY_INVITATION);

        userProjectDao.persist(userProjectEntity);

        // Send invitation email
        String subject = "Invite to Project " + project.getName();
        String text = "<p>User " + "<strong>" + sender.getFirstName() + " " + sender.getLastName() +"</strong>" + " has invited you to join the project <strong>" + project.getName() + "</strong>.</p>" +
                "<p>Click the button below to accept the invitation:</p>" +
                "<a href=\"http://localhost:3000/project/" + project.getId() + "\" style=\"display:inline-block; padding:10px 20px; font-size:16px; color:#fff; background-color:#f39c12; text-align:center; text-decoration:none; border-radius:5px;\">Accept Invitation</a>" +
                "<p>If the button does not work, you can also copy and paste the following link into your browser:</p>" +
                "<p>http://localhost:3000/project/" + project.getId() + "</p>" +
                "<p></p>" + 
                "<p>Best regards,<br>ILM Management Team</p>";
        MailDto mailDto = new MailDto(subject, text, (sender.getFirstName() + " " + sender.getLastName()), sender.getEmail(), userToInvite.getFirstName() + " " + userToInvite.getLastName(), userToInvite.getEmail());

        mailBean.sendMail(sessionId, mailDto);

        notificationBean.createInviteNotification(project.getSystemName(), userDao.getFullNameBySystemUsername(sender.getSystemUsername()), userToInvite, sender.getSystemUsername());

        return "User invited successfully";
    }



}
