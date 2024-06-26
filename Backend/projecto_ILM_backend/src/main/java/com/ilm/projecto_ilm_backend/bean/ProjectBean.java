package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.*;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.dto.mail.MailDto;
import com.ilm.projecto_ilm_backend.dto.project.*;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import com.ilm.projecto_ilm_backend.entity.*;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsForInviteeException;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsToInviteException;
import com.ilm.projecto_ilm_backend.security.exceptions.UnauthorizedAccessException;
import com.ilm.projecto_ilm_backend.utilities.imgsPath;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Singleton
@Startup
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
    private TaskDao taskDao;

    @Inject
    private UserTaskDao userTaskDao;

    @Inject
    private NotificationDao notificationDao;

    @Inject
    private UserProjectDao userProjectDao;

    @Inject
    private NotificationBean notificationBean;

    @Inject
    SessionDao sessionDao;

    @Inject
    MailBean mailBean;

    @Inject
    UserBean userBean;

    @Inject
    LogBean logBean;


    private static final int NUMBER_OF_MYPROJECTS_PER_PAGE = 6;
    private static final int NUMBER_OF_PROJECTS_PER_PAGE = 8;
    private int numberOfProjectsToCreate = 20;
    private static final int MAX_PROJECT_MEMBERS_DEFAULT = 4;
    private static final int MAX_PROJECT_MEMBERS_MAX = 30;

    @Transactional
    public void createDefaultProjectsIfNotExistent() {
        if (projectDao.countProjects() < numberOfProjectsToCreate) {
            for (int i = 0; i < numberOfProjectsToCreate; i++) {
                ProjectEntity project = new ProjectEntity();
                project.setName("project name" + i);
                project.setSystemName(projectSystemNameGenerator(project.getName()));
                project.setDescription("This project aims to develop an innovative software solution for managing large-scale data in real-time. The system will leverage cutting-edge technologies to handle vast amounts of information efficiently.");
                project.setCreatedAt(LocalDateTime.now());
                project.setStartDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
                project.setStartDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
                project.setEndDate(LocalDateTime.now().plus(1, ChronoUnit.YEARS));
                project.setStatus(StateProjectENUM.IN_PROGRESS);
                project.setPhoto("https://cdn.pixabay.com/photo/2016/03/29/08/48/project-1287781_1280.jpg");
                project.setMotivation("This project aims to develop an innovative software solution for managing large-scale data in real-time. The system will leverage cutting-edge technologies to handle vast amounts of information efficiently.");
                project.setMaxMembers(15);
                project.setKeywords("Keyword1, Keyword2, Keyword3, Keyword4, Keyword5, Keyword6");
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

        if (projectDao.findById(21) == null) {
            ProjectEntity project = new ProjectEntity();
            project.setName("project name" + 21);
            project.setSystemName(projectSystemNameGenerator(project.getName()));
            project.setDescription("This project aims to develop an innovative software solution for managing large-scale data in real-time. The system will leverage cutting-edge technologies to handle vast amounts of information efficiently.");
            project.setStartDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project.setCreatedAt(LocalDateTime.now());
            project.setStartDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
            project.setEndDate(LocalDateTime.now().plus(1, ChronoUnit.YEARS));
            project.setStatus(StateProjectENUM.IN_PROGRESS);
            project.setMotivation("This project aims to develop an innovative software solution for managing large-scale data in real-time. The system will leverage cutting-edge technologies to handle vast amounts of information efficiently.");
            project.setMaxMembers(15);
            project.setKeywords("Keyword1, Keyword2, Keyword3, Keyword4, Keyword5, Keyword6");
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

    public String projectSystemNameGenerator(String originalName) {
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
        if (userProjectDao.countUserProjects() < numberOfProjectsToCreate) {

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
            ProjectEntity project = projectDao.findById(21);

            UserProjectEntity userProjectEntity = new UserProjectEntity();

            userProjectEntity.setProject(project);
            userProjectEntity.setUser(userDao.findById(1));
            userProjectEntity.setType(UserInProjectTypeENUM.CREATOR);
            userProjectDao.merge(userProjectEntity);
        }

    }

    public ArrayList<HomeProjectDto> getProjectsDtosHome() {
        return projectDao.findAllNamesAndDescriptionsHome();
    }

    public ProjectTableInfoDto getProjectTableInfo(String sessionId, int page, String labName, String status, boolean slotsAvailable, String nameAsc,
                                                   String statusAsc, String labAsc, String startDateAsc, String endDateAsc, String keyword) {

        LabEntity lab;
        StateProjectENUM state;
        List<Object[]> projectsInfo;
        if (labName == null || labName.equals("")) lab = null;
        else lab = labDao.findbyLocal(WorkLocalENUM.valueOf(labName));
        if (status.equals("")) state = null;
        else state = StateProjectENUM.valueOf(status);
        if (keyword.equals("")) keyword = null;

        if (lab == null && (status == null || status.equals("")) && !slotsAvailable && (nameAsc == null || nameAsc.equals("")) && (statusAsc == null || statusAsc.equals("")) && (labAsc == null || labAsc.equals("")) && (startDateAsc == null || startDateAsc.equals("")) && (endDateAsc == null || endDateAsc.equals("")) && (keyword == null || keyword.equals(""))) {
            int userId = sessionDao.findUserIdBySessionId(sessionId);
            projectsInfo = projectDao.findAllProjectsOrderedByUser(page, NUMBER_OF_PROJECTS_PER_PAGE, userId);
        } else {
            projectsInfo = projectDao.getProjectTableDtoInfo(page, NUMBER_OF_PROJECTS_PER_PAGE, lab, state, slotsAvailable, nameAsc,
                    statusAsc, labAsc, startDateAsc, endDateAsc, keyword);
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
            projectTableDto.setPhoto((String) projectInfo[7]);
            projectTableDto.setNumberOfMembers(userProjectDao.getNumberOfUsersByProjectId((int) projectInfo[0]));
            projectTableDto.setMember(userProjectDao.isUserInProject((int) projectInfo[0], sessionDao.findUserIdBySessionId(sessionId)));
            projectTableDto.setSystemProjectName((String) projectInfo[8]);
            projectsTableDtos.add(projectTableDto);

        }

        ProjectTableInfoDto projectTableInfoDto = new ProjectTableInfoDto();

        projectTableInfoDto.setTableProjects(projectsTableDtos);
        System.out.println("number of projects: " + projectDao.getNumberOfProjectsTableDtoInfo(lab, state, slotsAvailable, keyword));
        projectTableInfoDto.setMaxPageNumber(calculateMaximumPageTableProjects(projectDao.getNumberOfProjectsTableDtoInfo(lab, state, slotsAvailable, keyword), NUMBER_OF_PROJECTS_PER_PAGE));

        return projectTableInfoDto;
    }

    public ProjectTableInfoDto getMyProjectsPageInfo(String sessionId, int page, String labName, String status, String keyword, String type) {
        int userId = sessionDao.findUserIdBySessionId(sessionId);

        LabEntity lab;
        StateProjectENUM state;
        UserInProjectTypeENUM typeEnum;

        if (labName == null || labName.equals("")) lab = null;
        else lab = labDao.findbyLocal(WorkLocalENUM.valueOf(labName));
        if (status.equals("")) state = null;
        else state = StateProjectENUM.valueOf(status);
        if (keyword.equals("")) keyword = null;
        if (type == null || type.equals("")) typeEnum = null;
        else typeEnum = UserInProjectTypeENUM.valueOf(type);

        List<Object[]> projectsInfo = projectDao.getMyProjectsDtoInfo(page, NUMBER_OF_MYPROJECTS_PER_PAGE, lab, state, keyword, userId, typeEnum);

        ArrayList<ProjectTableDto> projectsTableDtos = new ArrayList<>();

        for (Object[] projectInfo : projectsInfo) {
            ProjectTableDto projectTableDto = new ProjectTableDto();
            projectTableDto.setName((String) projectInfo[1]);
            projectTableDto.setLab(((LabEntity) projectInfo[2]).getLocal());
            projectTableDto.setStatus((StateProjectENUM) projectInfo[3]);
            projectTableDto.setStartDate((Date) projectInfo[4]);
            projectTableDto.setFinalDate((Date) projectInfo[5]);
            projectTableDto.setMaxMembers((int) projectInfo[6]);
            projectTableDto.setPhoto((String) projectInfo[7]);
            projectTableDto.setNumberOfMembers(userProjectDao.getNumberOfUsersByProjectId((int) projectInfo[0]));
            projectTableDto.setMember(userProjectDao.isUserInProject((int) projectInfo[0], userId));
            projectTableDto.setPercentageDone(getProgress((int) projectInfo[0]));
            projectTableDto.setSystemProjectName((String) projectInfo[9]);
            UserInProjectTypeENUM userInProjectType = (UserInProjectTypeENUM) projectInfo[8];
            if (userInProjectType == userInProjectType.MEMBER_BY_INVITATION || userInProjectType == userInProjectType.MEMBER_BY_APPLIANCE) {
                projectTableDto.setUserInProjectType(UserInProjectTypeENUM.MEMBER);
            } else {
                projectTableDto.setUserInProjectType(userInProjectType);
            }
            projectsTableDtos.add(projectTableDto);

        }

        ProjectTableInfoDto projectTableInfoDto = new ProjectTableInfoDto();

        projectTableInfoDto.setTableProjects(projectsTableDtos);
        projectTableInfoDto.setMaxPageNumber(calculateMaximumPageTableProjects(projectDao.getNumberOfMyProjectsDtoInfo(lab, state, keyword, userId, typeEnum), NUMBER_OF_MYPROJECTS_PER_PAGE));

        return projectTableInfoDto;
    }

    public ArrayList<StateProjectENUM> getAllStatus() {
        ArrayList<StateProjectENUM> status = new ArrayList<>();
        for (StateProjectENUM state : StateProjectENUM.values()) {
            status.add(state);
        }
        return status;
    }

    public ProjectFiltersDto getProjectsFilters() {
        ArrayList<StateProjectENUM> status = new ArrayList<>();
        ArrayList<UserInProjectTypeENUM> userInProjectType = new ArrayList<>();

        for (StateProjectENUM state : StateProjectENUM.values()) {
            status.add(state);
        }
        for (UserInProjectTypeENUM type : UserInProjectTypeENUM.values()) {
            if (type != UserInProjectTypeENUM.PENDING_BY_APPLIANCE && type != UserInProjectTypeENUM.PENDING_BY_INVITATION && type != UserInProjectTypeENUM.MEMBER_BY_APPLIANCE && type != UserInProjectTypeENUM.MEMBER_BY_INVITATION && type != UserInProjectTypeENUM.EXMEMBER && type != UserInProjectTypeENUM.GUEST && type != UserInProjectTypeENUM.ADMIN) {

                userInProjectType.add(type);

            }
        }

        List<LabEntity> labs = labDao.findAll();
        ArrayList<WorkLocalENUM> labNames = new ArrayList<>();
        for (LabEntity lab : labs) {
            labNames.add(lab.getLocal());
        }

        ProjectFiltersDto projectFiltersDto = new ProjectFiltersDto();
        projectFiltersDto.setLabs(labNames);
        projectFiltersDto.setStates(status);
        projectFiltersDto.setUserTypesInProject(userInProjectType);

        return projectFiltersDto;
    }

    public int calculateMaximumPageTableProjects(int numberOfProjects, int numberOfProjectPerPage) {
        return (int) Math.ceil((double) numberOfProjects / numberOfProjectPerPage);
    }

    public List<ProjectProfileDto> getProjectsToInvite(int userId, String inviteeUsername) {
        UserEntity invitee = userBean.getUserBySystemUsername(inviteeUsername);

        if (invitee == null) {
            throw new IllegalArgumentException("User to invite not found");
        }

        List<UserProjectEntity> userProjects = userProjectDao.findByUserId(userId).stream()
                .filter(up -> List.of(UserInProjectTypeENUM.CREATOR, UserInProjectTypeENUM.MANAGER).contains(up.getType()))
                .collect(Collectors.toList());

        if (userProjects.isEmpty()) {
            throw new NoProjectsToInviteException("You don't have any projects to invite the user to.");
        }

        List<UserProjectEntity> inviteeProjects = userProjectDao.findByUserId(invitee.getId());
        Set<Integer> inviteeProjectIds = inviteeProjects.stream()
                .filter(up -> List.of(
                        UserInProjectTypeENUM.CREATOR,
                        UserInProjectTypeENUM.MANAGER,
                        UserInProjectTypeENUM.MEMBER,
                        UserInProjectTypeENUM.MEMBER_BY_INVITATION,
                        UserInProjectTypeENUM.MEMBER_BY_APPLIANCE,
                        UserInProjectTypeENUM.PENDING_BY_APPLIANCE,
                        UserInProjectTypeENUM.PENDING_BY_INVITATION).contains(up.getType()))
                .map(up -> up.getProject().getId())
                .collect(Collectors.toSet());

        List<ProjectProfileDto> projects = userProjects.stream()
                .filter(up -> !inviteeProjectIds.contains(up.getProject().getId()) &&
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

        if (projects.isEmpty()) {
            throw new NoProjectsForInviteeException("The user you want to invite has no projects to be invited to.");
        }

        return projects;
    }


    public boolean isUserCreatorOrManagerByProjectName(int userId, String projectName) {
        ProjectEntity project = projectDao.findByName(projectName);
        return userProjectDao.isUserCreatorOrManager(userId, project.getId());
    }

    public boolean isUserCreatorOrManager(int userId, String projectSystemName) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
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
        String text = "<p>User " + "<strong>" + sender.getFirstName() + " " + sender.getLastName() + "</strong>" + " has invited you to join the project <strong>" + project.getName() + "</strong>.</p>" +
                "<p>Click the button below to accept the invitation:</p>" +
                "<a href=\"http://localhost:3000/project/" + project.getSystemName() + "\" style=\"display:inline-block; padding:10px 20px; font-size:16px; color:#fff; background-color:#f39c12; text-align:center; text-decoration:none; border-radius:5px;\">Accept Invitation</a>" +
                "<p>If the button does not work, you can also copy and paste the following link into your browser:</p>" +
                "<p>http://localhost:3000/project/" + project.getSystemName() + "</p>" +
                "<p></p>" +
                "<p>Best regards,<br>ILM Management Team</p>";

        MailDto mailDto = new MailDto(subject, text, (sender.getFirstName() + " " + sender.getLastName()), sender.getEmail(), userToInvite.getFirstName() + " " + userToInvite.getLastName(), userToInvite.getEmail());

        mailBean.sendMail(sessionId, mailDto);

        notificationBean.createInviteNotification(project.getSystemName(), userDao.getFullNameBySystemUsername(sender.getSystemUsername()), userToInvite, sender.getSystemUsername());

        return "User invited successfully";
    }

    public String acceptInvite(int userId, String projectName) {
        ProjectEntity project = projectDao.findByName(projectName);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectIdAndType(userId, project.getId(), UserInProjectTypeENUM.PENDING_BY_INVITATION);
        userProject.setType(UserInProjectTypeENUM.MEMBER_BY_INVITATION);
        userProjectDao.merge(userProject);
        UserEntity invitator = userBean.getUserBySystemUsername(notificationBean.getSystemUsernameOfCreatorOfNotificationByReceptorAndType(userId, NotificationTypeENUM.INVITE));
        System.out.println("INVITATOR: " + invitator.getSystemUsername());
        UserEntity acceptor = userDao.findById(userId);
        System.out.println("ACCEPTOR: " + acceptor.getSystemUsername());
        notificationBean.createInviteAcceptedNotification(project.getSystemName(), userDao.getFullNameBySystemUsername(acceptor.getSystemUsername()), invitator, acceptor.getSystemUsername());
        logBean.createMemberAddedLog(project, invitator, acceptor.getFullName());
        return "Invite accepted successfully";
    }

    public String rejectInvite(int userId, String projectName) {
        ProjectEntity project = projectDao.findByName(projectName);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectIdAndType(userId, project.getId(), UserInProjectTypeENUM.PENDING_BY_INVITATION);
        UserEntity invitator = userBean.getUserBySystemUsername(notificationBean.getSystemUsernameOfCreatorOfNotificationByReceptorAndType(userId, NotificationTypeENUM.INVITE));
        UserEntity rejector = userDao.findById(userId);
        userProjectDao.remove(userProject);
        notificationBean.createInviteRejectedNotification(project.getSystemName(), userDao.getFullNameBySystemUsername(rejector.getSystemUsername()), invitator, rejector.getSystemUsername());

        return "Invite rejected successfully";
    }

    public ProjectProfilePageDto getProjectInfoPage(int userId, String systemProjectName) {
        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        List<ProjectMemberDto> members = getProjectMembers(project.getId());
        List<SkillDto> skills = getProjectSkills(project);
        List<StateProjectENUM> statesToChange = getPossibleStatesToChange(userId, project);
        int progress = getProgress(project.getId());

        ProjectProfilePageDto projectProfilePageDto = new ProjectProfilePageDto();

        UserEntity creator = userProjectDao.findCreatorByProjectId(project.getId());
        ProjectMemberDto creatorDto = createProjectMemberDto(creator, UserInProjectTypeENUM.CREATOR);

        projectProfilePageDto.setTitle(project.getName());
        projectProfilePageDto.setState(project.getStatus());
        projectProfilePageDto.setDescription(project.getDescription());
        projectProfilePageDto.setStartDate(project.getStartDate());
        projectProfilePageDto.setEndDate(project.getEndDate());
        projectProfilePageDto.setPhoto(project.getPhoto());
        projectProfilePageDto.setLab(project.getLab().getLocal().toString());
        projectProfilePageDto.setMaxMembers(project.getMaxMembers());
        projectProfilePageDto.setCreator(creatorDto);
        projectProfilePageDto.setMembers(members);
        projectProfilePageDto.setKeywords(List.of(project.getKeywords().split(", ")));
        projectProfilePageDto.setSkills(skills);
        projectProfilePageDto.setStatesToChange(statesToChange);
        projectProfilePageDto.setProgress(progress);
        projectProfilePageDto.setTypeOfUserSeingProject(getUserTypeInProject(userId, project.getId()));
        projectProfilePageDto.setTypeOfUser(userDao.findById(userId).getType());
        projectProfilePageDto.setReason(project.getReason());

        return projectProfilePageDto;
    }

    public List<ProjectMemberDto> getProjectMembers(int projectId) {
        List<UserProjectEntity> membersUserProjects = userProjectDao.findMembersByProjectId(projectId);
        return membersUserProjects.stream().map(userProject -> {
            UserEntity user = userProject.getUser();
            return createProjectMemberDto(user, userProject.getType());
        }).collect(Collectors.toList());
    }

    public List<ProjectMemberDto> getAllProjectMembers(int projectId) {
        List<UserProjectEntity> membersUserProjects = userProjectDao.findAllTypeOfMembersByProjectId(projectId);
        return membersUserProjects.stream().map(userProject -> {
            UserEntity user = userProject.getUser();
            return createProjectMemberDto(user, userProject.getType());
        }).collect(Collectors.toList());
    }

    private ProjectMemberDto createProjectMemberDto(UserEntity user, UserInProjectTypeENUM type) {
        ProjectMemberDto member = new ProjectMemberDto();
        member.setId(user.getId());
        member.setName(user.getFirstName() + " " + user.getLastName());
        member.setSystemUsername(user.getSystemUsername());
        member.setType(type);
        member.setProfilePicture(user.getPhoto());
        return member;
    }

    private List<SkillDto> getProjectSkills(ProjectEntity project) {
        return project.getSkillInProject().stream().map(skill -> {
            SkillDto skillDto = new SkillDto();
            skillDto.setName(skill.getName());
            skillDto.setType(skill.getType().toString());
            return skillDto;
        }).collect(Collectors.toList());
    }

    private List<StateProjectENUM> getPossibleStatesToChange(int userId, ProjectEntity project) {

        UserTypeENUM userType = userDao.findById(userId).getType();

        if (userType == UserTypeENUM.ADMIN) {
            if (project.getStatus() == StateProjectENUM.CANCELED) {
                return List.of(StateProjectENUM.PLANNING, StateProjectENUM.CANCELED);
            }
        }

        if (userProjectDao.isUserCreatorOrManager(userId, project.getId())) {
            if (project.getStatus() == StateProjectENUM.CANCELED) {
                return List.of(StateProjectENUM.CANCELED);
            } else if (project.getStatus() == StateProjectENUM.PLANNING || project.getStatus() == StateProjectENUM.READY) {
                return List.of(StateProjectENUM.PLANNING, StateProjectENUM.READY);
            } else {
                return List.of(StateProjectENUM.APPROVED, StateProjectENUM.IN_PROGRESS, StateProjectENUM.CANCELED, StateProjectENUM.FINISHED);
            }
        }
        return new ArrayList<>();
    }


    public int getProgress(int projectId) {
        ProjectEntity project = projectDao.findById(projectId);
        switch (project.getStatus()) {
            case PLANNING:
                return 10;
            case READY:
                return 20;
            case FINISHED:
                return 100;
            case CANCELED:
                return 0;
            default:
                List<TaskEntity> tasks = taskDao.findByProject(projectId);
                long doneTasks = tasks.stream()
                        .filter(task -> task.getStatus() == TaskStatusENUM.DONE)
                        .count();
                if (tasks.isEmpty()) {
                    return 20; // Sem tarefas, retorno 20%
                }
                double progress = 20 + (70.0 * doneTasks / tasks.size()); // Progresso entre 20% e 90%
                return (int) Math.round(progress);
        }
    }

    public UserInProjectTypeENUM getUserTypeInProject(int userId, int projectId) {
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectId(userId, projectId);
        if (userProject != null) {
            return userProject.getType();
        } else {
            UserEntity user = userDao.findById(userId);
            if (user.getType() == UserTypeENUM.ADMIN) {
                return UserInProjectTypeENUM.ADMIN;
            } else {
                return UserInProjectTypeENUM.GUEST;
            }
        }
    }

    @Transactional
    public String approveOrRejectProject(String projectSystemName, boolean approve, String reason, int userId, String sessionId) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        UserEntity userResponsable = userDao.findById(userId);

        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        if (project.getStatus() == StateProjectENUM.CANCELED) {
            throw new IllegalStateException("Project cannot be approved or rejected as it is canceled");
        }

        if (project.getStatus() != StateProjectENUM.READY) {
            throw new IllegalStateException("Project must be in READY state to be approved or rejected");
        }

        if (approve) {
            project.setStatus(StateProjectENUM.APPROVED);
            project.setReason(null);
            logBean.createProjectStatusUpdatedLog(project, userResponsable, project.getStatus(), StateProjectENUM.APPROVED);

        } else {
            project.setStatus(StateProjectENUM.PLANNING);
            project.setReason(reason);
            logBean.createProjectStatusUpdatedLog(project, userResponsable, project.getStatus(), StateProjectENUM.PLANNING);
        }

        List<UserEntity> teamMembers = getProjectMembersByProjectId(project.getId());
        projectDao.merge(project);


        for (UserEntity user : teamMembers) {
            if (approve) {
                notificationBean.createProjectNotification(project.getSystemName(), StateProjectENUM.APPROVED, userResponsable.getFirstName() + " " + userResponsable.getLastName(), user, userResponsable.getSystemUsername());

                // Send approval email
                String subject = "Project " + project.getName() + " Approved";
                String text = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                        "<p>We are pleased to inform you that the project <strong><a href=\"http://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been approved. You can now start working on the project.</p>" +
                        "<p>Best regards,<br>ILM Management Team</p>";

                MailDto mailDto = new MailDto(
                        subject,
                        text,
                        userResponsable.getFirstName() + " " + userResponsable.getLastName(),
                        userResponsable.getEmail(),
                        user.getFirstName() + " " + user.getLastName(),
                        user.getEmail()
                );

                mailBean.sendMail(sessionId, mailDto);
            } else {
                notificationBean.createRejectProjectNotification(project.getSystemName(), StateProjectENUM.PLANNING, userResponsable.getFirstName() + " " + userResponsable.getLastName(), user, userResponsable.getSystemUsername());

                // Send rejection email
                String subject = "Project " + project.getName() + " Rejected";
                String text = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                        "<p>We regret to inform you that the project <strong><a href=\"http://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been rejected.</p>" +
                        "<p>Reason: " + reason + "</p>" +
                        "<p>We apologize for any inconvenience this may cause.</p>" +
                        "<p>Best regards,<br>ILM Management Team</p>";

                MailDto mailDto = new MailDto(
                        subject,
                        text,
                        userResponsable.getFirstName() + " " + userResponsable.getLastName(),
                        userResponsable.getEmail(),
                        user.getFirstName() + " " + user.getLastName(),
                        user.getEmail()
                );

                mailBean.sendMail(sessionId, mailDto);
            }
        }

        return approve ? "Project approved successfully" : "Project rejected successfully";
    }


    @Transactional
    public String joinProject(int userId, String projectSystemName) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);

        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        if (project.getStatus() == StateProjectENUM.CANCELED) {
            throw new IllegalStateException("Cannot join a canceled project");
        }

        UserProjectEntity existingUserProject = userProjectDao.findByUserIdAndProjectId(userId, project.getId());

        if (existingUserProject != null) {
            throw new IllegalArgumentException("User already has a record in this project");
        }

        UserEntity user = userDao.findById(userId);
        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setUser(user);
        userProjectEntity.setProject(project);
        userProjectEntity.setType(UserInProjectTypeENUM.PENDING_BY_APPLIANCE);

        List<UserEntity> teamLeaders = getProjectCreatorsAndManagersByProjectId(project.getId());

        userProjectDao.persist(userProjectEntity);

        for (UserEntity teamLeader : teamLeaders) {
            notificationBean.createApplianceNotification(project.getSystemName(), userDao.getFullNameBySystemUsername(user.getSystemUsername()), teamLeader, user.getSystemUsername());
        }

        return "User applied to join project successfully";
    }


    @Transactional
    public String cancelProject(int userId, String projectSystemName, String reason, String sessionId) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        UserEntity sender = userDao.findById(userId);

        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        project.setStatus(StateProjectENUM.CANCELED);
        project.setReason(reason);
        logBean.createProjectStatusUpdatedLog(project, sender, project.getStatus(), StateProjectENUM.CANCELED);

        projectDao.merge(project);

        // Get the list of team members
        List<UserEntity> teamMembers = getProjectMembersByProjectId(project.getId());

        // Loop through each team member and send email
        for (UserEntity user : teamMembers) {
            // Create a notification for each team member, except the sender
            if (user.getId() != sender.getId()) {
                notificationBean.createProjectNotification(project.getSystemName(), StateProjectENUM.CANCELED, userDao.getFullNameBySystemUsername(sender.getSystemUsername()), user, sender.getSystemUsername());
            }

            // Send cancellation email
            String subject = "Project " + project.getName() + " Canceled";
            String text = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                    "<p>We regret to inform you that the project <strong><a href=\"http://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been canceled.</p>" +
                    "<p>Reason: " + reason + "</p>" +
                    "<p>We apologize for any inconvenience this may cause.</p>" +
                    "<p>Best regards,<br>ILM Management Team</p>";

            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    sender.getFirstName() + " " + sender.getLastName(),
                    sender.getEmail(),
                    user.getFirstName() + " " + user.getLastName(),
                    user.getEmail()
            );

            mailBean.sendMail(sessionId, mailDto);
        }

        return "Project canceled successfully";
    }


    @Transactional
    public String markReasonAsRead(int userId, String projectSystemName) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);

        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectId(userId, project.getId());
        if (userProject == null || !(userProject.getType().equals(UserInProjectTypeENUM.CREATOR) || userProject.getType().equals(UserInProjectTypeENUM.MANAGER))) {
            throw new UnauthorizedAccessException("User does not have permission to mark reason as read for this project.");
        }

        project.setReason(null);
        projectDao.merge(project);

        return "Reason marked as read successfully";
    }

    public String changeProjectState(int userId, String projectSystemName, StateProjectENUM newState, String reason) throws Exception {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        UserEntity sender = userDao.findById(userId);
        if (project == null) {
            throw new Exception("Project not found");
        }
        logBean.createProjectStatusUpdatedLog(project, sender, project.getStatus(), newState);

        UserTypeENUM userType = userDao.findById(userId).getType();
        boolean isAdmin = userType == UserTypeENUM.ADMIN;

        UserInProjectTypeENUM userInProjectType = userProjectDao.findByUserIdAndProjectId(userId, project.getId()).getType();

        if (project.getStatus() == StateProjectENUM.CANCELED && !isAdmin) {
            throw new UnauthorizedAccessException("Only admins can change the state of a canceled project.");
        }

        if (userInProjectType != UserInProjectTypeENUM.MANAGER && userInProjectType != UserInProjectTypeENUM.CREATOR && !isAdmin) {
            throw new UnauthorizedAccessException("User does not have permission to change project state.");
        }

        if (newState == StateProjectENUM.CANCELED && reason == null) {
            throw new Exception("Cancellation reason is required.");
        }

        if (newState == StateProjectENUM.PLANNING && project.getStatus() == StateProjectENUM.CANCELED) {
            project.setReason(null);
        }

        project.setStatus(newState);
        if (newState == StateProjectENUM.CANCELED) {
            project.setReason(reason);
        }

        List<UserEntity> teamMembers = getProjectMembersByProjectId(project.getId());
        for (UserEntity user : teamMembers) {
            if (user.getId() != sender.getId()) {
                notificationBean.createProjectNotification(project.getSystemName(), newState, userDao.getFullNameBySystemUsername(sender.getSystemUsername()), user, sender.getSystemUsername());
            }
        }

        if(newState == StateProjectENUM.IN_PROGRESS){
            project.setinProgressDate(LocalDateTime.now());
        }

        if(newState == StateProjectENUM.FINISHED){
            project.setFinishedDate(LocalDateTime.now());
        }

        projectDao.merge(project);
        return "Project state updated successfully";
    }


    public List<UserEntity> getProjectMembersByProjectId(int projectId) {
        return userProjectDao.findMembersByProjectId(projectId).stream().map(UserProjectEntity::getUser).collect(Collectors.toList());
    }

    public List<UserEntity> getProjectCreatorsAndManagersByProjectId(int projectId) {
        return userProjectDao.findCreatorsAndManagersByProjectId(projectId);
    }

    public String removeUserFromProject(String systemProjectName, int userToRemoveId, int currentUserId, String reason, String sessionId) {
        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        UserEntity userToRemove = userDao.findById(userToRemoveId);
        UserEntity currentUser = userDao.findById(currentUserId);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectId(userToRemoveId, project.getId());
        if (userProject == null) {
            throw new IllegalArgumentException("User is not in the project");
        }
        if (userProject.getType() == UserInProjectTypeENUM.CREATOR) {
            throw new IllegalArgumentException("Cannot remove the creator of the project");
        }
        if (userProject.getType() == UserInProjectTypeENUM.MANAGER && !userProjectDao.isUserCreatorOrManager(currentUserId, project.getId())) {
            throw new UnauthorizedAccessException("Only the creator can remove a manager");
        }
        if (userProject.getType() == UserInProjectTypeENUM.MEMBER && !userProjectDao.isUserCreatorOrManager(currentUserId, project.getId())) {
            throw new UnauthorizedAccessException("Only the creator or a manager can remove a member");
        }
        userProjectDao.remove(userProject);

        notificationBean.createRemovedNotification(systemProjectName, currentUser.getSystemUsername(), userToRemove);

        String subject = "You have been removed from project " + project.getName();
        String text = "<p>Dear " + userToRemove.getFirstName() + " " + userToRemove.getLastName() + ",</p>" +
                "<p>We regret to inform you that you have been removed from the project <strong><a href=\"http://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong>.</p>" +
                "<p>Reason: " + reason + "</p>" +
                "<p>We apologize for any inconvenience this may cause.</p>" +
                "<p>Best regards,<br>ILM Management Team</p>";

        MailDto mailDto = new MailDto(
                subject,
                text,
                userDao.getFullNameBySystemUsername(currentUser.getSystemUsername()),
                currentUser.getEmail(),
                userToRemove.getFirstName() + " " + userToRemove.getLastName(),
                userToRemove.getEmail()
        );

        logBean.createMemberRemovedLog(project, currentUser, userToRemove.getFullName());

        mailBean.sendMail(sessionId, mailDto);

        return "User removed successfully";
    }

    public String acceptApplication(String systemProjectName, int userToAccept, int currentUserId, String sessionId) {
        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        UserEntity user = userDao.findById(userToAccept);
        UserEntity currentUser = userDao.findById(currentUserId);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectId(userToAccept, project.getId());
        if (userProject == null || userProject.getType() != UserInProjectTypeENUM.PENDING_BY_APPLIANCE) {
            throw new IllegalArgumentException("User is not pending to join the project");
        }
        userProject.setType(UserInProjectTypeENUM.MEMBER_BY_APPLIANCE);
        userProjectDao.merge(userProject);

        notificationBean.createApplianceAcceptedNotification(systemProjectName, currentUser.getSystemUsername(), user);

        String subject = "Your application to project " + project.getName() + " has been accepted";
        String text = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                "<p>We are pleased to inform you that your application to join the project <strong><a href=\"http://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been accepted.</p>" +
                "<p>You can now start working on the project.</p>" +
                "<p>Best regards,<br>ILM Management Team</p>";

        MailDto mailDto = new MailDto(
                subject,
                text,
                userDao.getFullNameBySystemUsername(currentUser.getSystemUsername()),
                currentUser.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail()
        );

        logBean.createMemberAddedLog(project, currentUser, user.getFullName());


        mailBean.sendMail(sessionId, mailDto);

        return "Application accepted successfully";
    }

    public String rejectApplication(String systemProjectName, int userToReject, int currentUserId, String reason, String sessionId) {
        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        UserEntity user = userDao.findById(userToReject);
        UserEntity currentUser = userDao.findById(currentUserId);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectId(userToReject, project.getId());
        if (userProject == null || userProject.getType() != UserInProjectTypeENUM.PENDING_BY_APPLIANCE) {
            throw new IllegalArgumentException("User is not pending to join the project");
        }
        userProjectDao.remove(userProject);

        notificationBean.createApplianceRejectedNotification(systemProjectName, currentUser.getSystemUsername(), user);

        String subject = "Your application to project " + project.getName() + " has been rejected";
        String text = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                "<p>We regret to inform you that your application to join the project <strong><a href=\"http://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been rejected.</p>" +
                "<p>Reason: " + reason + "</p>" +
                "<p>We apologize for any inconvenience this may cause.</p>" +
                "<p>Best regards,<br>ILM Management Team</p>";

        MailDto mailDto = new MailDto(
                subject,
                text,
                userDao.getFullNameBySystemUsername(currentUser.getSystemUsername()),
                currentUser.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail()
        );

        mailBean.sendMail(sessionId, mailDto);

        return "Application rejected successfully";
    }

    public ProjectMembersPageDto getProjectMembersPage(int currentUserId, String systemProjectName) {
        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        UserEntity currentUser = userDao.findById(currentUserId);
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }
        if (!userProjectDao.isUserCreatorOrManager(currentUserId, project.getId())) {
            throw new UnauthorizedAccessException("Only the creator or a manager can view project members");
        }
        List<ProjectMemberDto> members = getAllProjectMembers(project.getId());

        StateProjectENUM projectState = project.getStatus();

        String projectName = project.getName();

        List<SkillDto> skills = getProjectSkills(project);

        UserInProjectTypeENUM userSeinfProject = getUserTypeInProject(currentUserId, project.getId());

        ProjectMembersPageDto projectMembersPageDto = new ProjectMembersPageDto();
        projectMembersPageDto.setProjectMembers(members);
        projectMembersPageDto.setProjectState(projectState);
        projectMembersPageDto.setProjectName(projectName);
        projectMembersPageDto.setProjectSkills(skills);
        projectMembersPageDto.setUserSeingProject(userSeinfProject);

        return projectMembersPageDto;
    }

    public boolean isUserCreator(int userId, String projectSystemName) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        return userProjectDao.isUserCreator(userId, project.getId());
    }

    public String changeUserInProjectType(String projectSystemName, int userId, UserInProjectTypeENUM newType, int currentUserId) {

        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        UserEntity currentUser = userDao.findById(currentUserId);
        UserEntity user = userDao.findById(userId);

        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }
        if (!userProjectDao.isUserCreatorOrManager(currentUserId, project.getId())) {
            throw new UnauthorizedAccessException("Only the creator or a manager can change user type");
        }
        if (newType != UserInProjectTypeENUM.MEMBER && newType != UserInProjectTypeENUM.MANAGER) {
            throw new IllegalArgumentException("New type must be MEMBER or MANAGER");
        }

        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectId(userId, project.getId());

        if (userProject == null) {
            throw new IllegalArgumentException("User is not in the project");
        }

        if (userProject.getType() == UserInProjectTypeENUM.CREATOR) {
            throw new IllegalArgumentException("Cannot change the type of the creator of the project");
        }

        logBean.createMemberTypeChangedLog(project, currentUser, userDao.getFullNameBySystemUsername(user.getSystemUsername()), userProject.getType(), newType);

        userProject.setType(newType);

        userProjectDao.merge(userProject);

        notificationBean.createTypeChangedNotification(projectSystemName, currentUser.getSystemUsername(), user, newType);



        return "User type changed successfully";

    }


    public boolean createProject(ProjectCreationDto projectCreationInfoDto, String sessionId) {

        if (projectDao.doesProjectExists(projectCreationInfoDto.getName())) return false;
        LocalDateTime localStartDateTime = convertStringToLocalDateTime(projectCreationInfoDto.getStartDate());
        LocalDateTime localEndDateTime = convertStringToLocalDateTime(projectCreationInfoDto.getEndDate());

        if (localStartDateTime.isAfter(localEndDateTime)) return false;
        if (localStartDateTime.isBefore(LocalDateTime.now())) return false;

        ProjectEntity project = new ProjectEntity();
        project.setStartDate(localStartDateTime);
        project.setEndDate(localEndDateTime);
        project.setName(projectCreationInfoDto.getName());
        project.setSystemName(projectSystemNameGenerator(project.getName()));
        project.setDescription(projectCreationInfoDto.getDescription());
        project.setStatus(StateProjectENUM.PLANNING);
        project.setMotivation(projectCreationInfoDto.getMotivation());
        project.setCreatedAt(LocalDateTime.now());
        project.setMaxMembers(MAX_PROJECT_MEMBERS_DEFAULT);

        project.setLab(labDao.findbyLocal(WorkLocalENUM.valueOf(projectCreationInfoDto.getLab())));

        String keywordsList = null;

        for (InterestDto interestDto : projectCreationInfoDto.getKeywords()) {
            if (keywordsList == null) {
                keywordsList = interestDto.getName();
            } else keywordsList += ", " + interestDto.getName();
        }

        project.setKeywords(keywordsList);

        List<SkillEntity> skills = new ArrayList<>();
        for (SkillDto skillName : projectCreationInfoDto.getSkills()) {
            SkillEntity skill = skillDao.findSkillByName(skillName.getName());
            if (skill == null) {
                skill = new SkillEntity();
                skill.setName(skillName.getName());
                skill.setType(SkillTypeENUM.valueOf(skillName.getType()));
                skillDao.persist(skill);
            }
            skills.add(skill);
        }
        project.setSkillInProject(skills);

        projectDao.persist(project);

        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setProject(project);
        userProjectEntity.setUser(userDao.findById(sessionDao.findUserIdBySessionId(sessionId)));
        userProjectEntity.setType(UserInProjectTypeENUM.CREATOR);
        userProjectDao.persist(userProjectEntity);

        return true;
    }

    public ProjectCreationDto getProjectDetails(String systemProjectName, String sessionId) {

        UserEntity user = userDao.findById(sessionDao.findUserIdBySessionId(sessionId));

        if (!userProjectDao.isUserCreatorOrManager(user.getId(), projectDao.findBySystemName(systemProjectName).getId())) {
            throw new UnauthorizedAccessException("Only the creator or a manager can view project details");
        }

        // Fetch the project entity by systemProjectName
        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        if (project == null) {
            throw new IllegalArgumentException("Project not found with system name: " + systemProjectName);
        }

        // Map the project entity to ProjectCreationDto
        ProjectCreationDto projectCreationDto = new ProjectCreationDto();
        projectCreationDto.setName(project.getName());
        projectCreationDto.setDescription(project.getDescription());
        projectCreationDto.setLab(project.getLab().getLocal().name());
        projectCreationDto.setMotivation(project.getMotivation());
        projectCreationDto.setStartDate(project.getStartDate().toString());
        projectCreationDto.setEndDate(project.getEndDate().toString());
        projectCreationDto.setPhoto(project.getPhoto()); // Assuming project entity has a getPhoto method

        // Map skills
        List<SkillDto> skillDtos = new ArrayList<>();
        for (SkillEntity skillEntity : project.getSkillInProject()) {
            SkillDto skillDto = new SkillDto();
            skillDto.setName(skillEntity.getName());
            skillDto.setType(skillEntity.getType().name());
            skillDtos.add(skillDto);
        }
        projectCreationDto.setSkills(skillDtos);

        // Map keywords
        String[] keywordsArray = project.getKeywords().split(", ");
        List<InterestDto> interestDtos = new ArrayList<>();
        for (String keyword : keywordsArray) {
            InterestDto interestDto = new InterestDto();
            interestDto.setName(keyword);
            interestDtos.add(interestDto);
        }
        projectCreationDto.setKeywords(interestDtos);

        return projectCreationDto;
    }

    public boolean updateProject(ProjectCreationDto projectUpdateDto, String sessionId, String systemProjectName) {

        UserEntity user = userDao.findById(sessionDao.findUserIdBySessionId(sessionId));

        try {
            ProjectEntity project = projectDao.findBySystemName(systemProjectName);
            if (project == null) return false;

            project.setName(projectUpdateDto.getName());

            LocalDateTime localStartDateTime = convertStringToLocalDateTime(projectUpdateDto.getStartDate());
            LocalDateTime localEndDateTime = convertStringToLocalDateTime(projectUpdateDto.getEndDate());

            if (localStartDateTime.isAfter(localEndDateTime)) return false;

            project.setStartDate(localStartDateTime);
            project.setEndDate(localEndDateTime);
            project.setDescription(projectUpdateDto.getDescription());
            project.setMotivation(projectUpdateDto.getMotivation());
            project.setLab(labDao.findbyLocal(WorkLocalENUM.valueOf(projectUpdateDto.getLab())));

            String keywordsList = null;
            for (InterestDto interestDto : projectUpdateDto.getKeywords()) {
                if (keywordsList == null) {
                    keywordsList = interestDto.getName();
                } else {
                    keywordsList += ", " + interestDto.getName();
                }
            }
            project.setKeywords(keywordsList);

            List<SkillEntity> skills = new ArrayList<>();
            for (SkillDto skillName : projectUpdateDto.getSkills()) {
                SkillEntity skill = skillDao.findSkillByName(skillName.getName());
                if (skill == null) {
                    skill = new SkillEntity();
                    skill.setName(skillName.getName());
                    skill.setType(SkillTypeENUM.valueOf(skillName.getType()));
                    skillDao.persist(skill);
                }
                skills.add(skill);
            }
            project.setSkillInProject(skills);

            projectDao.merge(project);

            List <UserEntity> teamMembers = getProjectMembersByProjectId(project.getId());

            for (UserEntity member : teamMembers) {
                if(member.getId() != user.getId()) {
                    notificationBean.createProjectUpdatedNotification(project.getSystemName(), user.getSystemUsername(), member);
                }
            }

            logBean.createProjectInfoUpdatedLog(project, user);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating project: " + e.getMessage());
            return false;
        }
    }



    public boolean uploadProjectPicture(Map<String, String> request, String projectName) {
        try {
            String base64Image = request.get("file");
            if (base64Image == null || base64Image.isEmpty()) {
                return false;
            }
            // Remove "data:image/png;base64," or similar prefix from the string
            if (base64Image.contains(",")) {
                base64Image = base64Image.split(",")[1];
            }

            if (!saveProjectPicture(projectDao.findByName(projectName), base64Image)) return false;

            return true;
            // Delegate to UserBean to handle the file saving and updating the user's photo path

        } catch (Exception e) {
            return false;
        }
    }

    public boolean saveProjectPicture(ProjectEntity projectEntity, String base64Image) {
        try {
            // Decode the Base64 string back to an image
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Save the original image to the user's specific directory
            String directoryPath = imgsPath.IMAGES_PROJECTS_PATH + "/" + projectEntity.getSystemName();

            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Determine the image format
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage originalImage = ImageIO.read(bis);
            String format = getImageFormat(originalImage);

            // Save the original image
            String originalFilePath = directoryPath + "/project_profile_picture." + format;
            ImageIO.write(originalImage, format, new File(originalFilePath));

            // Save resized images
            BufferedImage avatarImage = resizeImage(originalImage, 280, 80); // Example size for avatar
            String avatarFilePath = directoryPath + "/project_card_picture." + format;
            ImageIO.write(avatarImage, format, new File(avatarFilePath));


            // Construct the URLs
            long timestamp = System.currentTimeMillis();
            String baseUrl = "http://localhost:8080/images/projects/" + projectEntity.getSystemName();
            String originalUrl = baseUrl + "/project_profile_picture." + format + "?t=" + timestamp;
            String cardUrl = baseUrl + "/project_card_picture." + format + "?t=" + timestamp;

            projectEntity.setPhoto(originalUrl);// Set URL for original image
            projectEntity.setCardPhoto(cardUrl); // Set URL for avatar

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getImageFormat(BufferedImage image) throws IOException {
        if (ImageIO.getImageWritersBySuffix("png").hasNext()) {
            return "png";
        } else if (ImageIO.getImageWritersBySuffix("jpg").hasNext()) {
            return "jpg";
        } else {
            throw new IOException("Unsupported image format");
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    private LocalDateTime convertStringToLocalDateTime(String date) {

        LocalDate localDate = LocalDate.parse(date);

        LocalDateTime localDateTime = localDate.atTime(0, 0);

        return localDateTime;
    }

    public boolean addInitialMembers(ProjectCreationMembersDto projectCreationMembersDto, String projectSystemName, String sessionId) {
        ArrayList<Integer> usersInProject = projectCreationMembersDto.getUsersInProject();
        if (projectCreationMembersDto.getMaxMembers() > MAX_PROJECT_MEMBERS_MAX || usersInProject.size() > projectCreationMembersDto.getMaxMembers()) {
            return false;
        }

        UserEntity sender = userBean.getUserBySessionId(sessionId);
        System.out.println("SENDER: " + sender.getSystemUsername());
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);

        int numberOfMembersInProject = userProjectDao.getNumberOfUsersByProjectId(project.getId());
        if (numberOfMembersInProject != 1) {
            return false;
        }
        for (Integer userInProject : usersInProject) {
            UserEntity userReceiver = userDao.findById(userInProject.intValue());
            UserProjectEntity userProjectEntity = new UserProjectEntity();
            userProjectEntity.setProject(project);
            userProjectEntity.setUser(userReceiver);
            userProjectEntity.setType(UserInProjectTypeENUM.MEMBER);
            userProjectDao.persist(userProjectEntity);
            notificationBean.createProjectInsertedNotification(projectSystemName, userDao.getFullNameBySystemUsername(sender.getSystemUsername()), userReceiver, sender.getSystemUsername());
            String subject = "You have been added to project " + project.getName();
            String text = "<p>Dear " + userReceiver.getFirstName() + " " + userReceiver.getLastName() + ",</p>" +
                    "<p>You have been added to the project <strong><a href=\"http://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong>.</p>" +
                    "<p>Best regards,<br>ILM Management Team</p>";

            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    userDao.getFullNameBySystemUsername(sender.getSystemUsername()),
                    sender.getEmail(),
                    userReceiver.getFirstName() + " " + userReceiver.getLastName(),
                    userReceiver.getEmail()
            );
            mailBean.sendMail(sessionDao.findSessionIdByUserId(sender.getId()), mailDto);
        }

        project.setMaxMembers(projectCreationMembersDto.getMaxMembers());
        projectDao.merge(project);
        return true;
    }

    public String removeInvitation(String projectSystemName, int userToRemoveId, int currentUserId, String sessionId) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        UserEntity userToRemove = userDao.findById(userToRemoveId);
        UserEntity currentUser = userDao.findById(currentUserId);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectId(userToRemoveId, project.getId());

        if (userProject == null || userProject.getType() != UserInProjectTypeENUM.PENDING_BY_INVITATION) {
            throw new IllegalArgumentException("User is not invited to the project");
        }

        userProjectDao.remove(userProject);

        notificationDao.removeByProjectIdAndReceptorAndType(project.getSystemName(), userToRemove.getId(), NotificationTypeENUM.INVITE);

        String subject = "Invitation to project " + project.getName() + " removed";
        String text = "<p>Dear " + userToRemove.getFirstName() + " " + userToRemove.getLastName() + ",</p>" +
                "<p>We regret to inform you that the invitation to join the project <strong><a href=\"http://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been removed.</p>" +
                "<p>Best regards,<br>ILM Management Team</p>";

        MailDto mailDto = new MailDto(
                subject,
                text,
                userDao.getFullNameBySystemUsername(currentUser.getSystemUsername()),
                currentUser.getEmail(),
                userToRemove.getFirstName() + " " + userToRemove.getLastName(),
                userToRemove.getEmail()
        );

        mailBean.sendMail(sessionId, mailDto);

        return "Invitation removed successfully";
    }


}
