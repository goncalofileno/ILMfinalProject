package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.*;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.lab.LabDto;
import com.ilm.projecto_ilm_backend.dto.mail.MailDto;
import com.ilm.projecto_ilm_backend.dto.project.*;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import com.ilm.projecto_ilm_backend.entity.*;
import com.ilm.projecto_ilm_backend.mapper.LabMapper;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsForInviteeException;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsToInviteException;
import com.ilm.projecto_ilm_backend.security.exceptions.UnauthorizedAccessException;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
    private UserProjectDao userProjectDao;

    @Inject
    private NotificationBean notificationBean;

    @Inject
    SessionDao sessionDao;

    @Inject
    MailBean mailBean;

    @Inject
    UserBean userBean;


    private static final int NUMBER_OF_MYPROJECTS_PER_PAGE=6;
    private static final int NUMBER_OF_PROJECTS_PER_PAGE=8;
    private int numberOfProjectsToCreate = 20;

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
                project.setInitialDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
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
            project.setInitialDate(LocalDateTime.now().minus(1, ChronoUnit.YEARS));
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
            projectsTableDtos.add(projectTableDto);

        }

        ProjectTableInfoDto projectTableInfoDto = new ProjectTableInfoDto();

        projectTableInfoDto.setTableProjects(projectsTableDtos);
        System.out.println("number of projects: " + projectDao.getNumberOfProjectsTableDtoInfo(lab, state, slotsAvailable, keyword));
        projectTableInfoDto.setMaxPageNumber(calculateMaximumPageTableProjects(projectDao.getNumberOfProjectsTableDtoInfo(lab, state, slotsAvailable, keyword), NUMBER_OF_PROJECTS_PER_PAGE));

        return projectTableInfoDto;
    }

    public ProjectTableInfoDto getMyProjectsPageInfo(String sessionId, int page, String labName, String status, String keyword, String type){
        int userId = sessionDao.findUserIdBySessionId(sessionId);

        LabEntity lab;
        StateProjectENUM state;
        UserInProjectTypeENUM typeEnum;

        if (labName == null || labName.equals("")) lab = null;
        else lab = labDao.findbyLocal(WorkLocalENUM.valueOf(labName));
        if (status.equals("")) state = null;
        else state = StateProjectENUM.valueOf(status);
        if (keyword.equals("")) keyword = null;
        if(type == null || type.equals("")) typeEnum= null;
        else typeEnum = UserInProjectTypeENUM.valueOf(type);

        List<Object[]> projectsInfo = projectDao.getMyProjectsDtoInfo(page, NUMBER_OF_MYPROJECTS_PER_PAGE, lab, state,  keyword,userId, typeEnum) ;

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
            UserInProjectTypeENUM userInProjectType = (UserInProjectTypeENUM) projectInfo[8];
            if(userInProjectType==userInProjectType.MEMBER_BY_APPLIANCE || userInProjectType== userInProjectType.MEMBER_BY_APPLIANCE) {
                projectTableDto.setUserInProjectType(UserInProjectTypeENUM.MEMBER);
            }else {
                projectTableDto.setUserInProjectType(userInProjectType);
            }
            projectsTableDtos.add(projectTableDto);

        }

        ProjectTableInfoDto projectTableInfoDto = new ProjectTableInfoDto();

        projectTableInfoDto.setTableProjects(projectsTableDtos);
        projectTableInfoDto.setMaxPageNumber(calculateMaximumPageTableProjects(projectDao.getNumberOfMyProjectsDtoInfo(lab, state, keyword, userId,typeEnum),NUMBER_OF_MYPROJECTS_PER_PAGE));

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
            if(type!=UserInProjectTypeENUM.PENDING_BY_APPLIANCE && type!=UserInProjectTypeENUM.PENDING_BY_INVITATION && type!=UserInProjectTypeENUM.MEMBER_BY_APPLIANCE && type!=UserInProjectTypeENUM.MEMBER_BY_INVITATION && type!=UserInProjectTypeENUM.EXMEMBER) {

                    userInProjectType.add(type);

            }
        }

        List<LabEntity> labs = labDao.findAll();
        ArrayList<WorkLocalENUM> labNames = new ArrayList<>();
        for (LabEntity lab : labs) {
            labNames.add(lab.getLocal());
        }

        ProjectFiltersDto projectFiltersDto= new ProjectFiltersDto();
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

    private List<ProjectMemberDto> getProjectMembers(int projectId) {
        List<UserProjectEntity> membersUserProjects = userProjectDao.findMembersByProjectId(projectId);
        return membersUserProjects.stream().map(userProject -> {
            UserEntity user = userProject.getUser();
            return createProjectMemberDto(user, userProject.getType());
        }).collect(Collectors.toList());
    }

    private ProjectMemberDto createProjectMemberDto(UserEntity user, UserInProjectTypeENUM type) {
        ProjectMemberDto member = new ProjectMemberDto();
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
        } else {
            project.setStatus(StateProjectENUM.PLANNING);
            project.setReason(reason);
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

        if(newState == StateProjectENUM.PLANNING && project.getStatus() == StateProjectENUM.CANCELED){
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

        projectDao.merge(project);
        return "Project state updated successfully";
    }


    public List<UserEntity> getProjectMembersByProjectId(int projectId) {
        return userProjectDao.findMembersByProjectId(projectId).stream().map(UserProjectEntity::getUser).collect(Collectors.toList());
    }

    public List<UserEntity> getProjectCreatorsAndManagersByProjectId(int projectId) {
        return userProjectDao.findCreatorsAndManagersByProjectId(projectId);
    }


}
