package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.*;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.dto.mail.MailDto;
import com.ilm.projecto_ilm_backend.dto.project.*;
import com.ilm.projecto_ilm_backend.dto.resource.ResourceTableDto;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import com.ilm.projecto_ilm_backend.dto.user.RejectedIdsDto;
import com.ilm.projecto_ilm_backend.entity.*;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsForInviteeException;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsToInviteException;
import com.ilm.projecto_ilm_backend.security.exceptions.UnauthorizedAccessException;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsForInviteeException;
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

/**
 * The ProjectBean class is responsible for managing ProjectEntity instances.
 * It is a singleton bean, meaning there is a single instance for the entire application.
 */
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
    private ResourceBean resourceBean;

    @Inject
    SessionDao sessionDao;

    @Inject
    MailBean mailBean;

    @Inject
    UserBean userBean;

    @Inject
    LogBean logBean;

    @Inject
    TaskBean taskBean;

    @Inject
    ResourceSupplierDao resourceSupplierDao;

    @Inject
    ProjectResourceDao projectResourceDao;

    @Inject
    SystemDao systemDao;

    private static final int NUMBER_OF_MYPROJECTS_PER_PAGE = 6;
    private static final int NUMBER_OF_PROJECTS_PER_PAGE = 8;
    private int numberOfProjectsToCreate = 20;

    /**
     * Creates default projects if they do not already exist.
     * This method initializes a predefined number of projects with default values.
     */
    @Transactional
    public void createDefaultProjectsIfNotExistent() {
        if (projectDao.countProjects() < numberOfProjectsToCreate) {
            for (int i = 0; i < numberOfProjectsToCreate; i++) {
                ProjectEntity project = new ProjectEntity();
                project.setName("project name" + i);
                project.setSystemName(projectSystemNameGenerator(project.getName()));
                project.setDescription("This project aims to develop an innovative software solution for managing large-scale data in real-time. The system will leverage cutting-edge technologies to handle vast amounts of information efficiently.");
                project.setCreatedAt(LocalDateTime.now());
                project.setStartDate(LocalDateTime.now().minus(5, ChronoUnit.YEARS));
                project.setEndDate(LocalDateTime.now().plus(5, ChronoUnit.YEARS));
                project.setStatus(StateProjectENUM.IN_PROGRESS);
                project.setMotivation("This project aims to develop an innovative software solution for managing large-scale data in real-time. The system will leverage cutting-edge technologies to handle vast amounts of information efficiently.");
                project.setMaxMembers(15);
                project.setKeywords("Keyword1, Keyword2, Keyword3, Keyword4, Keyword5, Keyword6");
                LabEntity lab = labDao.findbyLocal(WorkLocalENUM.COIMBRA);
                project.setLab(lab);
                project.setKeywords("Keyword1, Keyword2, Keyword3, Keyword4, Keyword5, Keyword6");
                project.setPhoto("http://localhost:8080/images/projects/default/project_profile_picture.jpg");
                project.setCardPhoto("http://localhost:8080/images/projects/default/project_profile_picture.jpg");
                List<SkillEntity> skillEntities = new ArrayList<>();
                SkillEntity skill1 = skillDao.findById(1);
                skillEntities.add(skill1);
                SkillEntity skill2 = skillDao.findById(2);
                skillEntities.add(skill2);
                project.setSkillInProject(skillEntities);

                projectDao.merge(project);

                createFinalPresentation(userDao.findById(2), project.getSystemName());

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
            project.setPhoto("http://localhost:8080/images/projects/default/project_profile_picture.jpg");
            project.setCardPhoto("http://localhost:8080/images/projects/default/project_profile_picture.jpg");
            List<SkillEntity> skillEntities = new ArrayList<>();
            SkillEntity skill1 = skillDao.findById(1);
            skillEntities.add(skill1);
            SkillEntity skill2 = skillDao.findById(2);
            skillEntities.add(skill2);
            project.setSkillInProject(skillEntities);

            projectDao.merge(project);

            createFinalPresentation(userDao.findById(2), project.getSystemName());
        }
    }

    /**
     * Creates a final presentation task for a project.
     *
     * @param userResponsable The user responsible for the final presentation task.
     * @param projectSystemName The system name of the project.
     */
    public void createFinalPresentation(UserEntity userResponsable, String projectSystemName) {

        TaskEntity finalPresentation = new TaskEntity();
        finalPresentation.setTitle("Final Presentation");
        finalPresentation.setDescription("Final presentation of the project");
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        finalPresentation.setInitialDate(project.getEndDate().minus(1, ChronoUnit.DAYS));
        finalPresentation.setFinalDate(project.getEndDate());
        finalPresentation.setProject(project);
        finalPresentation.setStatus(TaskStatusENUM.IN_PROGRESS);
        finalPresentation.setDeleted(false);
        String finalPresentationSystemName = projectSystemName + "_final_presentation";
        finalPresentation.setSystemTitle(finalPresentationSystemName);
        taskDao.persist(finalPresentation);

        createUserTaskEntityPresentator(userResponsable, finalPresentationSystemName);

    }

    /**
     * Creates a UserTaskEntity for a user responsible for a task.
     *
     * @param userResponsable The user responsible for the task.
     * @param systemTitle The system title of the task.
     */
    public void createUserTaskEntityPresentator(UserEntity userResponsable, String systemTitle) {

        TaskEntity finalPresentation2 = taskDao.findTaskBySystemTitle(systemTitle);

        UserTaskEntity userTaskEntity = new UserTaskEntity();
        userTaskEntity.setTask(finalPresentation2);
        userTaskEntity.setUser(userResponsable);
        userTaskEntity.setType(UserInTaskTypeENUM.CREATOR_INCHARGE);
        userTaskDao.merge(userTaskEntity);
    }


    /**
     * Generates a system name for a project based on its original name.
     *
     * @param originalName The original name of the project.
     * @return The generated system name.
     */
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

    /**
     * Creates default users in projects if they do not already exist.
     * This method initializes a predefined number of users in projects with default values.
     */
    public void createDefaultUsersInProjectIfNotExistent() {
        if (userProjectDao.countUserProjects() < numberOfProjectsToCreate) {

            for (int i = 1; i < numberOfProjectsToCreate + 1; i++) {

                ProjectEntity project = projectDao.findById(i);

                UserProjectEntity userProjectEntity = new UserProjectEntity();

                userProjectEntity.setProject(project);
                userProjectEntity.setUser(userDao.findById(2));
                userProjectEntity.setType(UserInProjectTypeENUM.CREATOR);
                userProjectDao.merge(userProjectEntity);

                UserProjectEntity userProjectEntity2 = new UserProjectEntity();

                userProjectEntity2.setProject(project);
                userProjectEntity2.setUser(userDao.findById(3));
                userProjectEntity2.setType(UserInProjectTypeENUM.MEMBER);
                userProjectDao.merge(userProjectEntity2);

            }
            ProjectEntity project = projectDao.findById(21);

            UserProjectEntity userProjectEntity = new UserProjectEntity();

            userProjectEntity.setProject(project);
            userProjectEntity.setUser(userDao.findById(2));
            userProjectEntity.setType(UserInProjectTypeENUM.CREATOR);
            userProjectDao.merge(userProjectEntity);
        }

    }

    /**
     * Retrieves a list of project DTOs for the home page.
     *
     * @return A list of HomeProjectDto instances.
     */
    public ArrayList<HomeProjectDto> getProjectsDtosHome() {
        return projectDao.findAllNamesAndDescriptionsHome();
    }

    /**
     * Retrieves information for the project table.
     *
     * @param sessionId The session ID of the user.
     * @param page The page number to retrieve.
     * @param labName The name of the lab.
     * @param status The status of the project.
     * @param slotsAvailable Whether the project has available slots.
     * @param nameAsc Sort by name ascending.
     * @param statusAsc Sort by status ascending.
     * @param labAsc Sort by lab ascending.
     * @param startDateAsc Sort by start date ascending.
     * @param endDateAsc Sort by end date ascending.
     * @param keyword The keyword to search for.
     * @return A ProjectTableInfoDto instance containing the project table information.
     */
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
        projectTableInfoDto.setMaxPageNumber(calculateMaximumPageTableProjects(projectDao.getNumberOfProjectsTableDtoInfo(lab, state, slotsAvailable, keyword), NUMBER_OF_PROJECTS_PER_PAGE));

        return projectTableInfoDto;
    }

    /**
     * Retrieves information for the user's projects.
     *
     * @param sessionId The session ID of the user.
     * @param page The page number to retrieve.
     * @param labName The name of the lab.
     * @param status The status of the project.
     * @param keyword The keyword to search for.
     * @param type The type of user in the project.
     * @return A ProjectTableInfoDto instance containing the user's projects information.
     */
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

    /**
     * Retrieves all project statuses.
     *
     * @return A list of StateProjectENUM values representing all project statuses.
     */
    public ArrayList<StateProjectENUM> getAllStatus() {
        ArrayList<StateProjectENUM> status = new ArrayList<>();
        for (StateProjectENUM state : StateProjectENUM.values()) {
            status.add(state);
        }
        return status;
    }

    /**
     * Retrieves project filters for the project table.
     *
     * @return A ProjectFiltersDto instance containing the project filters.
     */
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

    /**
     * Calculates the maximum page number for the project table.
     *
     * @param numberOfProjects The total number of projects.
     * @param numberOfProjectPerPage The number of projects per page.
     * @return The maximum page number.
     */
    public int calculateMaximumPageTableProjects(int numberOfProjects, int numberOfProjectPerPage) {
        return (int) Math.ceil((double) numberOfProjects / numberOfProjectPerPage);
    }

    /**
     * Retrieves a list of project profiles to invite a user to.
     *
     * @param userId The ID of the user sending the invitation.
     * @param inviteeUsername The username of the user to be invited.
     * @return A list of ProjectProfileDto instances representing the projects to invite the user to.
     * @throws NoProjectsToInviteException If there are no projects to invite the user to.
     * @throws NoProjectsForInviteeException If the invitee does not exist.
     */
    public List<ProjectProfileDto> getProjectsToInvite(int userId, String inviteeUsername) throws NoProjectsToInviteException, NoProjectsForInviteeException{
        UserEntity invitee = userBean.getUserBySystemUsername(inviteeUsername);

        LanguageENUM languageOfUserInviting = userDao.findById(userId).getLanguage();

        if (invitee == null) {
            if(languageOfUserInviting == LanguageENUM.ENGLISH){
                throw new NoProjectsForInviteeException("The user you want to invite does not exist.");
            } else {
                throw new NoProjectsForInviteeException("O utilizador que pretende convidar não existe.");
            }
        }

        List<UserProjectEntity> userProjects = userProjectDao.findByUserId(userId).stream()
                .filter(up -> List.of(UserInProjectTypeENUM.CREATOR, UserInProjectTypeENUM.MANAGER).contains(up.getType()))
                .collect(Collectors.toList());

        if (userProjects.isEmpty()) {
            if (languageOfUserInviting == LanguageENUM.ENGLISH) {
                throw new NoProjectsToInviteException("You have no projects to invite the user to.");
            } else {
                throw new NoProjectsToInviteException("Não tem projetos para convidar o utilizador.");
            }
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

        projects = projects.stream()
                .filter(project -> userProjectDao.getNumberOfUsersByProjectId(projectDao.findByName(project.getName()).getId()) < projectDao.findByName(project.getName()).getMaxMembers())
                .collect(Collectors.toList());

        if (projects.isEmpty()) {
            if (languageOfUserInviting == LanguageENUM.ENGLISH) {
                throw new NoProjectsToInviteException("There are no projects available to invite the user to.");
            } else {
                throw new NoProjectsToInviteException("Não existem projetos disponíveis para convidar o utilizador.");
            }
        }

        return projects;
    }


    /**
     * Checks if a user is either a creator or a manager of a project by project name.
     *
     * @param userId The ID of the user to check.
     * @param projectName The name of the project.
     * @return true if the user is a creator or manager of the project, false otherwise.
     */
    public boolean isUserCreatorOrManagerByProjectName(int userId, String projectName) {
        ProjectEntity project = projectDao.findByName(projectName);
        return userProjectDao.isUserCreatorOrManager(userId, project.getId());
    }

    /**
     * Checks if a user is either a creator or a manager of a project by project system name.
     *
     * @param userId The ID of the user to check.
     * @param projectSystemName The system name of the project.
     * @return true if the user is a creator or manager of the project, false otherwise.
     */
    public boolean isUserCreatorOrManager(int userId, String projectSystemName) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        return userProjectDao.isUserCreatorOrManager(userId, project.getId());
    }

    /**
     * Invites a user to a project. This method checks if the project is not canceled or full,
     * and if the user is not already invited, then sends an invitation.
     *
     * @param sessionId The session ID of the user sending the invitation.
     * @param systemUsername The system username of the user to invite.
     * @param projectName The name of the project to which the user is being invited.
     * @return A string message indicating the result of the invitation process.
     */
    public String inviteUserToProject(String sessionId, String systemUsername, String projectName) {
        UserEntity userToInvite = userDao.findBySystemUsername(systemUsername);
        ProjectEntity project = projectDao.findByName(projectName);
        UserEntity sender = sessionDao.findBySessionId(sessionId).getUser();

        if (userProjectDao.isUserAlreadyInvited(userToInvite.getId(), project.getId())) {
            return "User is already invited to this project";
        }

        //verifica se o projecto esta cancelado ou tem as vagas preenchidas
        if (project.getStatus() == StateProjectENUM.CANCELED) {
            return "Project is canceled";
        }

        if (userProjectDao.getNumberOfUsersByProjectId(project.getId()) >= project.getMaxMembers()) {
            return "Project is full";
        }

        UserProjectEntity userProjectEntity = new UserProjectEntity();
        userProjectEntity.setUser(userToInvite);
        userProjectEntity.setProject(project);
        userProjectEntity.setType(UserInProjectTypeENUM.PENDING_BY_INVITATION);

        userProjectDao.persist(userProjectEntity);

        UserEntity admnistration = userDao.findBySystemUsername("administration");

        if (userToInvite.getLanguage() == LanguageENUM.ENGLISH) {
            String subject = "Invite to Project " + project.getName();
            String text = "<p>User " + "<strong>" + sender.getFirstName() + " " + sender.getLastName() + "</strong>" + " has invited you to join the project <strong>" + project.getName() + "</strong>.</p>" +
                    "<p>Click the button below to accept the invitation:</p>" +
                    "<a href=\"https://localhost:3000/project/" + project.getSystemName() + "\" style=\"display:inline-block; padding:10px 20px; font-size:16px; color:#fff; background-color:#f39c12; text-align:center; text-decoration:none; border-radius:5px;\">Accept Invitation</a>" +
                    "<p>If the button does not work, you can also copy and paste the following link into your browser:</p>" +
                    "<p>https://localhost:3000/project/" + project.getSystemName() + "</p>" +
                    "<p></p>" +
                    "<p>Best regards,<br>ILM Management Team</p>";
            MailDto mailDto = new MailDto(subject, text, (sender.getFirstName() + " " + sender.getLastName()), sender.getEmail(), userToInvite.getFirstName() + " " + userToInvite.getLastName(), userToInvite.getEmail());
            mailBean.sendMail(admnistration, mailDto);
        } else if (userToInvite.getLanguage() == LanguageENUM.PORTUGUESE) {
            String subject = "Convite para o Projeto " + project.getName();
            String text = "<p>O utilizador " + "<strong>" + sender.getFirstName() + " " + sender.getLastName() + "</strong>" + " convidou-o a juntar-se ao projeto <strong>" + project.getName() + "</strong>.</p>" +
                    "<p>Clique no botão abaixo para aceitar o convite:</p>" +
                    "<a href=\"https://localhost:3000/project/" + project.getSystemName() + "\" style=\"display:inline-block; padding:10px 20px; font-size:16px; color:#fff; background-color:#f39c12; text-align:center; text-decoration:none; border-radius:5px;\">Aceitar Convite</a>" +
                    "<p>Se o botão não funcionar, pode também copiar e colar o seguinte link no seu navegador:</p>" +
                    "<p>https://localhost:3000/project/" + project.getSystemName() + "</p>" +
                    "<p></p>" +
                    "<p>Com os melhores cumprimentos,<br>Equipa de Gestão ILM</p>";
            MailDto mailDto = new MailDto(subject, text, (admnistration.getFirstName() + " " + admnistration.getLastName()), admnistration.getEmail(), userToInvite.getFirstName() + " " + userToInvite.getLastName(), userToInvite.getEmail());
            mailBean.sendMail(admnistration, mailDto);
        }

        notificationBean.createInviteNotification(project.getSystemName(), userDao.getFullNameBySystemUsername(sender.getSystemUsername()), userToInvite, sender.getSystemUsername());

        return "User invited successfully";
    }

    /**
     * Accepts an invitation to a project. This method checks if the project is not canceled or full,
     * and if so, changes the user's project status to MEMBER_BY_INVITATION.
     *
     * @param userId The ID of the user accepting the invitation.
     * @param projectName The name of the project to which the user was invited.
     * @return A string message indicating the result of the acceptance process.
     */
    public String acceptInvite(int userId, String projectName) {
        ProjectEntity project = projectDao.findByName(projectName);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectIdAndType(userId, project.getId(), UserInProjectTypeENUM.PENDING_BY_INVITATION);

        //verifica se o projecto esta cancelado ou tem as vagas preenchidas
        if (project.getStatus() == StateProjectENUM.CANCELED) {
            return "Project is canceled";
        }

        if (userProjectDao.getNumberOfUsersByProjectId(project.getId()) >= project.getMaxMembers()) {
            return "Project is full";
        }

        userProject.setType(UserInProjectTypeENUM.MEMBER_BY_INVITATION);
        userProjectDao.merge(userProject);
        UserEntity invitator = userBean.getUserBySystemUsername(notificationBean.getSystemUsernameOfCreatorOfNotificationByReceptorAndType(userId, NotificationTypeENUM.INVITE));
        UserEntity acceptor = userDao.findById(userId);
        notificationBean.createInviteAcceptedNotification(project.getSystemName(), userDao.getFullNameBySystemUsername(acceptor.getSystemUsername()), invitator, acceptor.getSystemUsername());
        logBean.createMemberAddedLog(project, invitator, acceptor.getFullName());

        //verifica se o numero de membros do projeto é igual ao maximo de membros e se for elimina todos os convites pendentes que ainda possam existir para esse projeto
        if (userProjectDao.getNumberOfUsersByProjectId(project.getId()) == project.getMaxMembers()) {
            List<UserProjectEntity> userProjects = userProjectDao.findByProjectId(project.getId());
            for (UserProjectEntity userProjectEntity : userProjects) {
                if (userProjectEntity.getType() == UserInProjectTypeENUM.PENDING_BY_INVITATION) {
                    userProjectDao.remove(userProjectEntity);
                }
            }
            List <NotificationEntity> notifications = notificationDao.findByProjectSystemName(project.getSystemName());
            for (NotificationEntity notification : notifications) {
                if (notification.getType() == NotificationTypeENUM.INVITE) {
                    notificationDao.remove(notification);
                }
            }
        }

        return "Invite accepted successfully";
    }

    /**
     * Rejects an invitation to a project. This method removes the user's pending invitation to the project
     * and sends a notification to the inviter indicating the rejection.
     *
     * @param userId The ID of the user rejecting the invitation.
     * @param projectName The name of the project to which the user was invited.
     * @return A string message indicating the result of the rejection process.
     */
    public String rejectInvite(int userId, String projectName) {
        ProjectEntity project = projectDao.findByName(projectName);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectIdAndType(userId, project.getId(), UserInProjectTypeENUM.PENDING_BY_INVITATION);
        UserEntity invitator = userBean.getUserBySystemUsername(notificationBean.getSystemUsernameOfCreatorOfNotificationByReceptorAndType(userId, NotificationTypeENUM.INVITE));
        UserEntity rejector = userDao.findById(userId);
        userProjectDao.remove(userProject);
        notificationBean.createInviteRejectedNotification(project.getSystemName(), userDao.getFullNameBySystemUsername(rejector.getSystemUsername()), invitator, rejector.getSystemUsername());

        return "Invite rejected successfully";
    }

    /**
     * Retrieves detailed information about a project for display on the project's profile page.
     * This includes project members, skills required for the project, possible states the project can transition to,
     * and the project's progress.
     *
     * @param userId The ID of the user requesting the project information.
     * @param systemProjectName The system name of the project.
     * @return An instance of ProjectProfilePageDto containing detailed information about the project.
     */
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

    /**
     * Retrieves a list of project members for a given project.
     *
     * @param projectId The ID of the project.
     * @return A list of ProjectMemberDto objects representing the members of the project.
     */
    public List<ProjectMemberDto> getProjectMembers(int projectId) {
        List<UserProjectEntity> membersUserProjects = userProjectDao.findMembersByProjectId(projectId);
        return membersUserProjects.stream().map(userProject -> {
            UserEntity user = userProject.getUser();
            return createProjectMemberDto(user, userProject.getType());
        }).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all project members, regardless of their role, for a given project.
     *
     * @param projectId The ID of the project.
     * @return A list of ProjectMemberDto objects representing all members associated with the project.
     */
    public List<ProjectMemberDto> getAllProjectMembers(int projectId) {
        List<UserProjectEntity> membersUserProjects = userProjectDao.findAllTypeOfMembersByProjectId(projectId);
        return membersUserProjects.stream().map(userProject -> {
            UserEntity user = userProject.getUser();
            return createProjectMemberDto(user, userProject.getType());
        }).collect(Collectors.toList());
    }

    /**
     * Creates a ProjectMemberDto object from a UserEntity and a UserInProjectTypeENUM.
     * This method is used internally to construct project member DTOs for API responses.
     *
     * @param user The UserEntity object representing the user.
     * @param type The type of the user within the project, as defined by UserInProjectTypeENUM.
     * @return A ProjectMemberDto object representing the project member.
     */
    private ProjectMemberDto createProjectMemberDto(UserEntity user, UserInProjectTypeENUM type) {
        ProjectMemberDto member = new ProjectMemberDto();
        member.setId(user.getId());
        member.setName(user.getFirstName() + " " + user.getLastName());
        member.setSystemUsername(user.getSystemUsername());
        member.setType(type);
        member.setProfilePicture(user.getPhoto());
        return member;
    }

    /**
     * Retrieves a list of skills associated with a project.
     *
     * @param project The ProjectEntity object representing the project.
     * @return A list of SkillDto objects representing the skills required for the project.
     */
    private List<SkillDto> getProjectSkills(ProjectEntity project) {
        return project.getSkillInProject().stream().map(skill -> {
            SkillDto skillDto = new SkillDto();
            skillDto.setName(skill.getName());
            skillDto.setType(skill.getType().toString());
            return skillDto;
        }).collect(Collectors.toList());
    }

    /**
     * Determines the possible states to which a project can transition, based on the current state of the project
     * and the role of the user making the request.
     *
     * @param userId The ID of the user.
     * @param project The ProjectEntity object representing the project.
     * @return A list of StateProjectENUM values representing the possible states the project can transition to.
     */
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
                return List.of(StateProjectENUM.APPROVED, StateProjectENUM.IN_PROGRESS, StateProjectENUM.FINISHED);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Calculates the progress of a project based on the status of its tasks.
     * The progress is a percentage value representing the completion level of the project.
     *
     * @param projectId The ID of the project.
     * @return An integer value representing the project's progress percentage.
     */
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

    /**
     * Determines the type of a user within a project. This can be used to tailor the user interface
     * based on the user's role in the project.
     *
     * @param userId The ID of the user.
     * @param projectId The ID of the project.
     * @return A UserInProjectTypeENUM value representing the user's type within the project.
     */
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

    /**
     * Approves or rejects a project based on the provided boolean flag. This method also sends notifications
     * to project members and updates the project's status accordingly.
     *
     * @param projectSystemName The system name of the project.
     * @param approve A boolean flag indicating whether the project is to be approved (true) or rejected (false).
     * @param reason The reason for rejection, if applicable.
     * @param userId The ID of the user performing the approval or rejection.
     * @param sessionId The session ID of the user performing the action.
     * @return A string message indicating the result of the operation.
     */
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

                UserEntity admnistration = userDao.findBySystemUsername("administration");
                if (user.getLanguage() == LanguageENUM.ENGLISH) {
                    String subject = "Project " + project.getName() + " Approved";
                    String text = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                            "<p>We are pleased to inform you that the project <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been approved. You can now start working on the project.</p>" +
                            "<p>Best regards,<br>ILM Management Team</p>";

                    MailDto mailDto = new MailDto(
                            subject,
                            text,
                            admnistration.getFirstName() + " " + admnistration.getLastName(),
                            admnistration.getEmail(),
                            user.getFirstName() + " " + user.getLastName(),
                            user.getEmail()
                    );

                    mailBean.sendMail(admnistration, mailDto);
                } else if (user.getLanguage() == LanguageENUM.PORTUGUESE) {
                    String subject = "Projeto " + project.getName() + " Aprovado";
                    String text = "<p>Caro(a) " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                            "<p>É com prazer que informamos que o projeto <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> foi aprovado. Pode agora começar a trabalhar no projeto.</p>" +
                            "<p>Com os melhores cumprimentos,<br>Equipa de Gestão ILM</p>";

                    MailDto mailDto = new MailDto(
                            subject,
                            text,
                            admnistration.getFirstName() + " " + admnistration.getLastName(),
                            admnistration.getEmail(),
                            user.getFirstName() + " " + user.getLastName(),
                            user.getEmail()
                    );

                    mailBean.sendMail(admnistration, mailDto);
                }
            } else {
                notificationBean.createRejectProjectNotification(project.getSystemName(), StateProjectENUM.PLANNING, userResponsable.getFirstName() + " " + userResponsable.getLastName(), user, userResponsable.getSystemUsername());

                UserEntity admnistration = userDao.findBySystemUsername("administration");
                if (user.getLanguage() == LanguageENUM.ENGLISH) {
                    String subject = "Project " + project.getName() + " Rejected";
                    String text = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                            "<p>We regret to inform you that the project <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been rejected.</p>" +
                            "<p>Reason: " + reason + "</p>" +
                            "<p>We apologize for any inconvenience this may cause.</p>" +
                            "<p>Best regards,<br>ILM Management Team</p>";

                    MailDto mailDto = new MailDto(
                            subject,
                            text,
                            admnistration.getFirstName() + " " + admnistration.getLastName(),
                            admnistration.getEmail(),
                            user.getFirstName() + " " + user.getLastName(),
                            user.getEmail()
                    );

                    mailBean.sendMail(admnistration, mailDto);
                } else if (user.getLanguage() == LanguageENUM.PORTUGUESE) {
                    String subject = "Projeto " + project.getName() + " Rejeitado";
                    String text = "<p>Caro(a) " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                            "<p>Lamentamos informar que o projeto <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> foi rejeitado.</p>" +
                            "<p>Razão: " + reason + "</p>" +
                            "<p>Pedimos desculpa por qualquer inconveniente que isto possa causar.</p>" +
                            "<p>Com os melhores cumprimentos,<br>Equipa de Gestão ILM</p>";

                    MailDto mailDto = new MailDto(
                            subject,
                            text,
                            admnistration.getFirstName() + " " + admnistration.getLastName(),
                            admnistration.getEmail(),
                            user.getFirstName() + " " + user.getLastName(),
                            user.getEmail()
                    );

                    mailBean.sendMail(admnistration, mailDto);
                }
            }
        }
        return approve ? "Project approved successfully" : "Project rejected successfully";
    }

    /**
     * Allows a user to apply to join a project. This method checks if the project exists and is not canceled,
     * and if the user does not already have a record in this project. If all conditions are met, the user's application
     * is recorded, and notifications are sent to the project's team leaders.
     *
     * @param userId The ID of the user applying to join the project.
     * @param projectSystemName The system name of the project the user wants to join.
     * @return A string message indicating the outcome of the application process.
     * @throws IllegalArgumentException if the project does not exist or the user already has a record in this project.
     * @throws IllegalStateException if the project is canceled.
     */
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


    /**
     * Cancels a project. This method checks if the project exists and if the user requesting the cancellation
     * has the necessary permissions. If the conditions are met, the project's status is set to canceled, a reason
     * for the cancellation is recorded, and notifications are sent to all team members.
     *
     * @param userId The ID of the user requesting the cancellation.
     * @param projectSystemName The system name of the project to be canceled.
     * @param reason The reason for canceling the project.
     * @param sessionId The session ID of the user requesting the cancellation.
     * @return A string message indicating the outcome of the cancellation process.
     * @throws IllegalArgumentException if the project does not exist or the user is not found.
     * @throws UnauthorizedAccessException if the user does not have permission to cancel the project.
     */
    @Transactional
    public String cancelProject(int userId, String projectSystemName, String reason, String sessionId) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }

        UserEntity sender = userDao.findById(userId);
        if (sender == null) {
            throw new IllegalArgumentException("User not found");
        }

        UserTypeENUM senderType = sender.getType();

        if(userProjectDao.findByUserIdAndProjectId(userId, project.getId()) == null){
            if (senderType != UserTypeENUM.ADMIN) {
                throw new UnauthorizedAccessException("User is not in the project");
            }
        } else {
            if (senderType != UserTypeENUM.ADMIN && !userProjectDao.isUserCreatorOrManager(userId, project.getId())) {
                throw new UnauthorizedAccessException("User is not a creator or manager of the project");
            }
        }

        project.setStatus(StateProjectENUM.CANCELED);
        project.setReason(reason);
        logBean.createProjectStatusUpdatedLog(project, sender, project.getStatus(), StateProjectENUM.CANCELED);

        projectDao.merge(project);

        List<UserEntity> teamMembers = getProjectMembersByProjectId(project.getId());

        // Loop through each team member and send email
        for (UserEntity user : teamMembers) {
            // Create a notification for each team member, except the sender
            if (user.getId() != sender.getId()) {
                notificationBean.createProjectNotification(project.getSystemName(), StateProjectENUM.CANCELED, userDao.getFullNameBySystemUsername(sender.getSystemUsername()), user, sender.getSystemUsername());
            }

            UserEntity administration = userDao.findBySystemUsername("administration");
            if (user.getLanguage() == LanguageENUM.ENGLISH) {
                String subject = "Project " + project.getName() + " Canceled";
                String text = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                        "<p>We regret to inform you that the project <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been canceled.</p>" +
                        "<p>Reason: " + reason + "</p>" +
                        "<p>We apologize for any inconvenience this may cause.</p>" +
                        "<p>Best regards,<br>ILM Management Team</p>";

                MailDto mailDto = new MailDto(
                        subject,
                        text,
                        administration.getFirstName() + " " + administration.getLastName(),
                        administration.getEmail(),
                        user.getFirstName() + " " + user.getLastName(),
                        user.getEmail()
                );

                mailBean.sendMail(administration, mailDto);
            } else if (user.getLanguage() == LanguageENUM.PORTUGUESE) {
                String subject = "Projeto " + project.getName() + " Cancelado";
                String text = "<p>Caro(a) " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                        "<p>Lamentamos informar que o projeto <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> foi cancelado.</p>" +
                        "<p>Razão: " + reason + "</p>" +
                        "<p>Pedimos desculpa por qualquer inconveniente que isto possa causar.</p>" +
                        "<p>Com os melhores cumprimentos,<br>Equipa de Gestão ILM</p>";

                MailDto mailDto = new MailDto(
                        subject,
                        text,
                        administration.getFirstName() + " " + administration.getLastName(),
                        administration.getEmail(),
                        user.getFirstName() + " " + user.getLastName(),
                        user.getEmail()
                );

                mailBean.sendMail(administration, mailDto);
            }
        }

        return "Project canceled successfully";
    }


    /**
     * Marks the cancellation reason of a project as read by a user. This method checks if the project exists
     * and if the user has the necessary permissions to mark the reason as read. If the conditions are met,
     * the cancellation reason is cleared.
     *
     * @param userId The ID of the user marking the reason as read.
     * @param projectSystemName The system name of the project.
     * @return A string message indicating the outcome of the operation.
     * @throws IllegalArgumentException if the project does not exist.
     * @throws UnauthorizedAccessException if the user does not have permission to mark the reason as read.
     */
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

    /**
     * Changes the state of a project. This method checks if the project exists and if the user requesting the change
     * has the necessary permissions. It also validates the new state and, if applicable, records a reason for the change.
     * Notifications are sent to all team members about the state change.
     *
     * @param userId The ID of the user requesting the state change.
     * @param projectSystemName The system name of the project.
     * @param newState The new state to which the project is to be changed.
     * @param reason The reason for changing the state, required if the new state is CANCELED.
     * @return A string message indicating the outcome of the state change.
     * @throws Exception if the project does not exist, the user does not have permission, or a cancellation reason is required but not provided.
     */
    public String changeProjectState(int userId, String projectSystemName, StateProjectENUM newState, String reason) throws Exception {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        UserEntity sender = userDao.findById(userId);
        if (project == null) {
            throw new Exception("Project not found");
        }
        logBean.createProjectStatusUpdatedLog(project, sender, project.getStatus(), newState);

        UserTypeENUM userType = userDao.findById(userId).getType();
        boolean isAdmin = userType == UserTypeENUM.ADMIN;

        if(userProjectDao.findByUserIdAndProjectId(userId, project.getId()) == null){
            if (!isAdmin) {
                throw new UnauthorizedAccessException("User is not in the project");
            }
        } else {
            if (!isAdmin && !userProjectDao.isUserCreatorOrManager(userId, project.getId())) {
                throw new UnauthorizedAccessException("User is not a creator or manager of the project");
            }
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

        if (newState == StateProjectENUM.IN_PROGRESS) {
            project.setinProgressDate(LocalDateTime.now());
        }

        if (newState == StateProjectENUM.FINISHED) {
            project.setFinishedDate(LocalDateTime.now());
        }

        projectDao.merge(project);
        return "Project state updated successfully";
    }

    /**
     * Retrieves a list of project members by project ID. This method is used internally to fetch the list of users
     * who are members of a specific project.
     *
     * @param projectId The ID of the project.
     * @return A list of UserEntity objects representing the members of the project.
     */
    public List<UserEntity> getProjectMembersByProjectId(int projectId) {
        return userProjectDao.findMembersByProjectId(projectId).stream().map(UserProjectEntity::getUser).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of project creators and managers by project ID. This method is used internally to fetch the list
     * of users who are either creators or managers of a specific project.
     *
     * @param projectId The ID of the project.
     * @return A list of UserEntity objects representing the creators and managers of the project.
     */
    public List<UserEntity> getProjectCreatorsAndManagersByProjectId(int projectId) {
        return userProjectDao.findCreatorsAndManagersByProjectId(projectId);
    }

    /**
     * Removes a user from a project. This method checks if the project exists, if the user to be removed exists,
     * and if the user requesting the removal has the necessary permissions. If the conditions are met, the user is
     * removed from the project, and notifications are sent to the removed user.
     *
     * @param systemProjectName The system name of the project.
     * @param userToRemoveId The ID of the user to be removed from the project.
     * @param currentUserId The ID of the user requesting the removal.
     * @param reason The reason for removing the user from the project.
     * @param sessionId The session ID of the user requesting the removal.
     * @return A string message indicating the outcome of the removal process.
     * @throws IllegalArgumentException if the project does not exist, the user to be removed is not in the project,
     *                                  or the user to be removed is the creator of the project.
     * @throws UnauthorizedAccessException if the user requesting the removal does not have permission to remove the user.
     * @throws IllegalStateException if the project is in a state that does not allow removal of users.
     */
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

        if (project.getStatus() == StateProjectENUM.CANCELED || project.getStatus() == StateProjectENUM.READY) {
            throw new IllegalStateException("Project is canceled or ready");
        }

        userProjectDao.remove(userProject);

        taskBean.removeUserFromProjectTasks(userToRemove.getId(), project.getId());

        notificationBean.createRemovedNotification(systemProjectName, currentUser.getSystemUsername(), userToRemove);

        UserEntity admnistration = userDao.findBySystemUsername("administration");
        if (userToRemove.getLanguage() == LanguageENUM.ENGLISH) {
            String subject = "You have been removed from project " + project.getName();
            String text = "<p>Dear " + userToRemove.getFirstName() + " " + userToRemove.getLastName() + ",</p>" +
                    "<p>We regret to inform you that you have been removed from the project <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong>.</p>" +
                    "<p>Reason: " + reason + "</p>" +
                    "<p>We apologize for any inconvenience this may cause.</p>" +
                    "<p>Best regards,<br>ILM Management Team</p>";

            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                    admnistration.getEmail(),
                    userToRemove.getFirstName() + " " + userToRemove.getLastName(),
                    userToRemove.getEmail()
            );
            mailBean.sendMail(admnistration, mailDto);

        } else if (userToRemove.getLanguage() == LanguageENUM.PORTUGUESE) {
            String subject = "Foi removido do projeto " + project.getName();
            String text = "<p>Caro(a) " + userToRemove.getFirstName() + " " + userToRemove.getLastName() + ",</p>" +
                    "<p>Lamentamos informar que foi removido do projeto <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong>.</p>" +
                    "<p>Razão: " + reason + "</p>" +
                    "<p>Pedimos desculpa por qualquer inconveniente que isto possa causar.</p>" +
                    "<p>Com os melhores cumprimentos,<br>Equipa de Gestão ILM</p>";

            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                    admnistration.getEmail(),
                    userToRemove.getFirstName() + " " + userToRemove.getLastName(),
                    userToRemove.getEmail()
            );

            mailBean.sendMail(admnistration, mailDto);
        }
        logBean.createMemberRemovedLog(project, currentUser, userToRemove.getFullName());
        return "User removed successfully";
    }

    /**
     * Accepts a user's application to join a project. This method verifies the project's existence, the user's pending status,
     * and the project's capacity before changing the user's status to a member. It also sends a notification and an email
     * to the user about the acceptance.
     *
     * @param systemProjectName The system name of the project to which the user applied.
     * @param userToAccept The ID of the user whose application is to be accepted.
     * @param currentUserId The ID of the current user performing the operation.
     * @param sessionId The session ID of the current user.
     * @return A string message indicating the outcome of the operation.
     * @throws IllegalArgumentException If the user is not pending to join the project.
     * @throws IllegalStateException If the project is full, canceled, or ready.
     */
    public String acceptApplication(String systemProjectName, int userToAccept, int currentUserId, String sessionId) {
        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        UserEntity user = userDao.findById(userToAccept);
        UserEntity currentUser = userDao.findById(currentUserId);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectId(userToAccept, project.getId());

        if (userProject == null || userProject.getType() != UserInProjectTypeENUM.PENDING_BY_APPLIANCE) {
            throw new IllegalArgumentException("User is not pending to join the project");
        }

        if (userProjectDao.getNumberOfUsersByProjectId(project.getId()) >= project.getMaxMembers()) {
            throw new IllegalStateException("Project is full");
        }

        if (project.getStatus() == StateProjectENUM.CANCELED || project.getStatus() == StateProjectENUM.READY) {
            throw new IllegalStateException("Project is canceled or ready");
        }

        userProject.setType(UserInProjectTypeENUM.MEMBER_BY_APPLIANCE);
        userProjectDao.merge(userProject);

        notificationBean.createApplianceAcceptedNotification(systemProjectName, currentUser.getSystemUsername(), user);

        UserEntity admnistration = userDao.findBySystemUsername("administration");
        if(user.getLanguage() == LanguageENUM.ENGLISH) {
            String subject = "Your application to project " + project.getName() + " has been accepted";
            String text = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                    "<p>We are pleased to inform you that your application to join the project <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been accepted.</p>" +
                    "<p>You can now start working on the project.</p>" +
                    "<p>Best regards,<br>ILM Management Team</p>";

            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                    admnistration.getEmail(),
                    user.getFirstName() + " " + user.getLastName(),
                    user.getEmail()
            );

            mailBean.sendMail(admnistration, mailDto);
        } else if (user.getLanguage() == LanguageENUM.PORTUGUESE) {
            String subject = "A sua candidatura ao projeto " + project.getName() + " foi aceite";
            String text = "<p>Caro(a) " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                    "<p>É com prazer que informamos que a sua candidatura ao projeto <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> foi aceite.</p>" +
                    "<p>Pode agora começar a trabalhar no projeto.</p>" +
                    "<p>Com os melhores cumprimentos,<br>Equipa de Gestão ILM</p>";

            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                    admnistration.getEmail(),
                    user.getFirstName() + " " + user.getLastName(),
                    user.getEmail()
            );

            mailBean.sendMail(admnistration, mailDto);

        }
        logBean.createMemberAddedLog(project, currentUser, user.getFullName());
        return "Application accepted successfully";
    }

    /**
     * Rejects a user's application to join a project. This method verifies the project's existence and the user's pending status
     * before removing the user's application. It also sends a notification and an email to the user about the rejection.
     *
     * @param systemProjectName The system name of the project to which the user applied.
     * @param userToReject The ID of the user whose application is to be rejected.
     * @param currentUserId The ID of the current user performing the operation.
     * @param reason The reason for rejecting the application.
     * @param sessionId The session ID of the current user.
     * @return A string message indicating the outcome of the operation.
     * @throws IllegalArgumentException If the user is not pending to join the project.
     * @throws IllegalStateException If the project is canceled or ready.
     */
    public String rejectApplication(String systemProjectName, int userToReject, int currentUserId, String reason, String sessionId) {
        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        UserEntity user = userDao.findById(userToReject);
        UserEntity currentUser = userDao.findById(currentUserId);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectId(userToReject, project.getId());

        if (userProject == null || userProject.getType() != UserInProjectTypeENUM.PENDING_BY_APPLIANCE) {
            throw new IllegalArgumentException("User is not pending to join the project");
        }

        if (project.getStatus() == StateProjectENUM.CANCELED || project.getStatus() == StateProjectENUM.READY) {
            throw new IllegalStateException("Project is canceled or ready");
        }

        userProjectDao.remove(userProject);

        notificationBean.createApplianceRejectedNotification(systemProjectName, currentUser.getSystemUsername(), user);

        UserEntity admnistration = userDao.findBySystemUsername("administration");
        if(user.getLanguage() == LanguageENUM.ENGLISH) {
            String subject = "Your application to project " + project.getName() + " has been rejected";
            String text = "<p>Dear " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                    "<p>We regret to inform you that your application to join the project <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been rejected.</p>" +
                    "<p>Reason: " + reason + "</p>" +
                    "<p>We apologize for any inconvenience this may cause.</p>" +
                    "<p>Best regards,<br>ILM Management Team</p>";

            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                    admnistration.getEmail(),
                    user.getFirstName() + " " + user.getLastName(),
                    user.getEmail()
            );

            mailBean.sendMail(admnistration, mailDto);
        } else if(user.getLanguage() == LanguageENUM.PORTUGUESE){
            String subject = "A sua candidatura ao projeto " + project.getName() + " foi rejeitada";
            String text = "<p>Caro(a) " + user.getFirstName() + " " + user.getLastName() + ",</p>" +
                    "<p>Lamentamos informar que a sua candidatura ao projeto <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> foi rejeitada.</p>" +
                    "<p>Razão: " + reason + "</p>" +
                    "<p>Pedimos desculpa por qualquer inconveniente que isto possa causar.</p>" +
                    "<p>Com os melhores cumprimentos,<br>Equipa de Gestão ILM</p>";

            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                    admnistration.getEmail(),
                    user.getFirstName() + " " + user.getLastName(),
                    user.getEmail()
            );

            mailBean.sendMail(admnistration, mailDto);
        }

        return "Application rejected successfully";
    }

    /**
     * Retrieves the page information of project members for a specific project. This method checks if the project exists
     * and if the current user has the necessary permissions to view the project members.
     *
     * @param currentUserId The ID of the current user requesting the information.
     * @param systemProjectName The system name of the project.
     * @return An instance of ProjectMembersPageDto containing information about the project members.
     * @throws IllegalArgumentException If the project is not found.
     * @throws UnauthorizedAccessException If the current user is not a creator or manager of the project.
     */
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
        projectMembersPageDto.setMaxMembers(project.getMaxMembers());
        projectMembersPageDto.setProjectState(projectState);
        projectMembersPageDto.setProjectName(projectName);
        projectMembersPageDto.setProjectSkills(skills);
        projectMembersPageDto.setUserSeingProject(userSeinfProject);

        return projectMembersPageDto;
    }

    /**
     * Checks if a user is the creator of a project.
     *
     * @param userId The ID of the user to check.
     * @param projectSystemName The system name of the project.
     * @return true if the user is the creator of the project, false otherwise.
     */
    public boolean isUserCreator(int userId, String projectSystemName) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        return userProjectDao.isUserCreator(userId, project.getId());
    }

    /**
     * Changes the type of a user in a project. This method verifies the project's existence, the current user's permissions,
     * and the validity of the new type before updating the user's type in the project.
     *
     * @param projectSystemName The system name of the project.
     * @param userId The ID of the user whose type is to be changed.
     * @param newType The new type to assign to the user.
     * @param currentUserId The ID of the current user performing the operation.
     * @return A string message indicating the outcome of the operation.
     * @throws IllegalArgumentException If the project is not found, the new type is invalid, or the user is not in the project.
     * @throws UnauthorizedAccessException If the current user does not have permission to change the user type.
     */
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


    /**
     * Creates a new project based on the provided information. This method checks if the project already exists,
     * validates the start and end dates, and then creates a new project entity along with its associated user project entity.
     *
     * @param projectCreationInfoDto The DTO containing information for project creation.
     * @param sessionId The session ID of the user creating the project.
     * @return true if the project is successfully created, false otherwise.
     * @throws IllegalStateException If the start date is after the end date or before the current date.
     */
    @Transactional
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
        project.setMaxMembers(systemDao.findConfigValueByName("maxMembersDefault"));
        project.setPhoto("http://localhost:8080/images/projects/default/project_profile_picture.jpg");
        project.setCardPhoto("http://localhost:8080/images/projects/default/project_profile_picture.jpg");

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

        createFinalPresentation(userBean.getUserBySessionId(sessionId), project.getSystemName());

        return true;
    }

    /**
     * Retrieves detailed information about a project based on its system name and the session ID of the requesting user.
     * This method ensures that only the creator or a manager of the project can view its details. It fetches the project
     * from the database and maps its properties to a {@link ProjectCreationDto} object, including name, description,
     * lab location, motivation, start and end dates, photo URL, maximum members, skills required, and keywords associated
     * with the project.
     *
     * @param systemProjectName The system name of the project for which details are requested.
     * @param sessionId The session ID of the user requesting the project details.
     * @return A {@link ProjectCreationDto} object containing the detailed information of the project.
     * @throws UnauthorizedAccessException If the user is not the creator or a manager of the project.
     * @throws IllegalArgumentException If the project with the given system name does not exist.
     */
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
        projectCreationDto.setMaxMembers(project.getMaxMembers());

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

    /**
     * Updates the details of an existing project based on the provided {@link ProjectCreationDto} object, session ID,
     * and system project name. This method checks if the project exists and if the user making the request is the creator
     * or a manager of the project. It then updates the project details in the database if the project is not canceled
     * or ready. The method also validates the start and end dates, maximum members, and updates the project's skills
     * and keywords.
     *
     * @param projectUpdateDto The DTO containing the updated project details.
     * @param sessionId The session ID of the user requesting the update.
     * @param systemProjectName The system name of the project to update.
     * @return true if the project was successfully updated, false otherwise.
     * @throws UnauthorizedAccessException If the user is not the creator or a manager of the project.
     * @throws IllegalArgumentException If the project does not exist or the input data is invalid.
     */
    public boolean updateProject(ProjectCreationDto projectUpdateDto, String sessionId, String systemProjectName) {

        UserEntity user = userDao.findById(sessionDao.findUserIdBySessionId(sessionId));
        ProjectEntity projectCheck = projectDao.findBySystemName(systemProjectName);

        if(projectCheck == null) return false;

        if (!userProjectDao.isUserCreatorOrManager(user.getId(), projectCheck.getId())) {
            throw new UnauthorizedAccessException("Only the creator or a manager can update project details");
        }

        if(projectCheck.getStatus() == StateProjectENUM.CANCELED || projectCheck.getStatus() == StateProjectENUM.READY) return false;

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

            if(projectUpdateDto.getMaxMembers() > systemDao.findConfigValueByName("maxMaxMembers") || projectUpdateDto.getMaxMembers() < 1 || projectUpdateDto.getMaxMembers() < project.getUserProjects().size()) return false;
            project.setMaxMembers(projectUpdateDto.getMaxMembers());

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

            List<UserEntity> teamMembers = getProjectMembersByProjectId(project.getId());

            for (UserEntity member : teamMembers) {
                if (member.getId() != user.getId()) {
                    notificationBean.createProjectUpdatedNotification(project.getSystemName(), user.getSystemUsername(), member);
                }
            }

            logBean.createProjectInfoUpdatedLog(project, user);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Handles the upload of a project picture, decoding it from a Base64 string and saving it to the project's directory.
     * The method updates the project entity with URLs to the original and resized images. It checks if the provided
     * Base64 string is not empty and contains the image data, then delegates the saving of the image to the
     * {@code saveProjectPicture} method.
     *
     * @param request A map containing the Base64 string of the image under the key "file".
     * @param projectName The name of the project to which the picture will be uploaded.
     * @return true if the picture was successfully uploaded and saved, false otherwise.
     */
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

    /**
     * Saves the project picture to the filesystem and updates the project entity with URLs to the original and resized
     * images. This method decodes the Base64 string to an image, determines the image format, and saves the image
     * to the project's specific directory. It also creates resized versions of the image for different purposes.
     *
     * @param projectEntity The project entity to which the picture belongs.
     * @param base64Image The Base64 string of the image.
     * @return true if the image was successfully saved, false otherwise.
     */
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

    /**
     * Determines the image format based on the available ImageIO image writers for "png" and "jpg" suffixes.
     * This method checks if there are available image writers for "png" and "jpg" formats and returns the format
     * if available. Throws an exception if neither format is supported.
     *
     * @param image The BufferedImage object for which the format needs to be determined.
     * @return The image format as a String ("png" or "jpg").
     * @throws IOException If neither "png" nor "jpg" formats are supported by the available ImageIO image writers.
     */
    private String getImageFormat(BufferedImage image) throws IOException {
        if (ImageIO.getImageWritersBySuffix("png").hasNext()) {
            return "png";
        } else if (ImageIO.getImageWritersBySuffix("jpg").hasNext()) {
            return "jpg";
        } else {
            throw new IOException("Unsupported image format");
        }
    }

    /**
     * Resizes a given BufferedImage to the specified width and height. This method creates a new BufferedImage
     * of the specified dimensions and draws the original image onto the new image at the specified size,
     * effectively resizing it.
     *
     * @param originalImage The original BufferedImage to resize.
     * @param width The desired width of the resized image.
     * @param height The desired height of the resized image.
     * @return A new BufferedImage object containing the original image resized to the specified dimensions.
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    /**
     * Converts a date string in ISO_LOCAL_DATE format to a LocalDateTime object at the start of the day (00:00).
     * This method parses the input string as a LocalDate and then converts it to a LocalDateTime at the start
     * of the day.
     *
     * @param date The date string in ISO_LOCAL_DATE format (e.g., "2023-01-01").
     * @return A LocalDateTime object representing the start of the specified date.
     */
    private LocalDateTime convertStringToLocalDateTime(String date) {

        LocalDate localDate = LocalDate.parse(date);

        LocalDateTime localDateTime = localDate.atTime(0, 0);

        return localDateTime;
    }

    /**
     * Adds initial members to a project based on a list of user IDs. This method checks if the number of initial members
     * does not exceed the project's maximum allowed members and the system's configuration. It then adds each user as a
     * member of the project and sends notifications and emails to the added members.
     *
     * @param projectCreationMembersDto The DTO containing the list of user IDs to add as initial members and the maximum
     *                                  members allowed for the project.
     * @param projectSystemName The system name of the project to which members will be added.
     * @param sessionId The session ID of the user adding the members.
     * @return true if the initial members were successfully added, false otherwise.
     */
    public boolean addInitialMembers(ProjectCreationMembersDto projectCreationMembersDto, String projectSystemName, String sessionId) {
        ArrayList<Integer> usersInProject = projectCreationMembersDto.getUsersInProject();
        if (projectCreationMembersDto.getMaxMembers() > systemDao.findConfigValueByName("maxMaxMembers") || usersInProject.size() > projectCreationMembersDto.getMaxMembers()) {
            return false;
        }

        UserEntity sender = userBean.getUserBySessionId(sessionId);
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
            UserEntity admnistration = userDao.findBySystemUsername("administration");
            if(userReceiver.getLanguage() == LanguageENUM.ENGLISH) {
                String subject = "You have been added to project " + project.getName();
                String text = "<p>Dear " + userReceiver.getFirstName() + " " + userReceiver.getLastName() + ",</p>" +
                        "<p>You have been added to the project <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong>.</p>" +
                        "<p>Best regards,<br>ILM Management Team</p>";

                MailDto mailDto = new MailDto(
                        subject,
                        text,
                        userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                        admnistration.getEmail(),
                        userReceiver.getFirstName() + " " + userReceiver.getLastName(),
                        userReceiver.getEmail()
                );
                mailBean.sendMail(admnistration, mailDto);
            } else if (userReceiver.getLanguage() == LanguageENUM.PORTUGUESE) {
                String subject = "Foi adicionado ao projeto " + project.getName();
                String text = "<p>Caro(a) " + userReceiver.getFirstName() + " " + userReceiver.getLastName() + ",</p>" +
                        "<p>Foi adicionado ao projeto <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong>.</p>" +
                        "<p>Com os melhores cumprimentos,<br>Equipa de Gestão ILM</p>";

                MailDto mailDto = new MailDto(
                        subject,
                        text,
                        userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                        admnistration.getEmail(),
                        userReceiver.getFirstName() + " " + userReceiver.getLastName(),
                        userReceiver.getEmail()
                );
                mailBean.sendMail(admnistration, mailDto);
            }
        }

        project.setMaxMembers(projectCreationMembersDto.getMaxMembers());
        projectDao.merge(project);
        return true;
    }

    /**
     * Removes an invitation for a user to join a project. This method checks if the user has a pending invitation to the
     * project and removes it. It also sends a notification and an email to the user informing them that the invitation
     * has been removed.
     *
     * @param projectSystemName The system name of the project from which the invitation is to be removed.
     * @param userToRemoveId The ID of the user whose invitation is to be removed.
     * @param currentUserId The ID of the current user performing the operation.
     * @param sessionId The session ID of the current user.
     * @return A string message indicating the outcome of the operation.
     * @throws IllegalArgumentException If the user does not have a pending invitation to the project.
     */
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

        UserEntity admnistration = userDao.findBySystemUsername("administration");
        if(userToRemove.getLanguage() == LanguageENUM.ENGLISH) {
            String subject = "Invitation to project " + project.getName() + " removed";
            String text = "<p>Dear " + userToRemove.getFirstName() + " " + userToRemove.getLastName() + ",</p>" +
                    "<p>We regret to inform you that the invitation to join the project <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> has been removed.</p>" +
                    "<p>Best regards,<br>ILM Management Team</p>";

            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                    admnistration.getEmail(),
                    userToRemove.getFirstName() + " " + userToRemove.getLastName(),
                    userToRemove.getEmail()
            );

            mailBean.sendMail(admnistration, mailDto);
        } else if (userToRemove.getLanguage() == LanguageENUM.PORTUGUESE) {
            String subject = "Convite para o projeto " + project.getName() + " removido";
            String text = "<p>Caro(a) " + userToRemove.getFirstName() + " " + userToRemove.getLastName() + ",</p>" +
                    "<p>Lamentamos informar que o convite para se juntar ao projeto <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong> foi removido.</p>" +
                    "<p>Com os melhores cumprimentos,<br>Equipa de Gestão ILM</p>";

            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                    admnistration.getEmail(),
                    userToRemove.getFirstName() + " " + userToRemove.getLastName(),
                    userToRemove.getEmail()
            );

            mailBean.sendMail(admnistration, mailDto);
        }

        return "Invitation removed successfully";
    }


    /**
     * Adds resources to a project based on a list of resource supplier IDs. This method updates the project's resources,
     * removing any that are not in the provided list and adding new ones. It ensures that each added resource is valid
     * and updates the project entity accordingly.
     *
     * @param projectSystemName The system name of the project to which resources are to be added.
     * @param resourcesSuppliersIds The DTO containing the list of resource supplier IDs to add to the project.
     * @param sessionId The session ID of the user performing the operation.
     * @return true if the resources were successfully added to the project, false otherwise.
     */
    public boolean addResourcesToProject(String projectSystemName, RejectedIdsDto resourcesSuppliersIds, String sessionId) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        List<ProjectResourceEntity> atualProjectResources = projectResourceDao.findResourcesById(project.getId());

        ArrayList<Integer> atualProjectResourcesIds = new ArrayList<>();
        for (ProjectResourceEntity atualProjectResource : atualProjectResources) {
            atualProjectResourcesIds.add(atualProjectResource.getId());
        }

        for (Integer atualProjectResource : atualProjectResourcesIds) {
            if (!resourcesSuppliersIds.getRejectedIds().contains(atualProjectResource)) {
                projectResourceDao.remove(projectResourceDao.findById(atualProjectResource.intValue()));
            }
        }

        if (project == null) return false;
        for (Integer rejectedId : resourcesSuppliersIds.getRejectedIds()) {
            if (!atualProjectResourcesIds.contains(rejectedId)) {
                ResourceSupplierEntity resourceSupplierEntity = resourceSupplierDao.find(rejectedId.intValue());
                if (resourceSupplierEntity == null) return false;
                ProjectResourceEntity projectResourceEntity = new ProjectResourceEntity();
                projectResourceEntity.setProject(project);
                projectResourceEntity.setResources(resourceSupplierEntity);
                projectResourceDao.persist(projectResourceEntity);
            }
        }

        logBean.createResourcesAddedLog(project, userBean.getUserBySessionId(sessionId));
        return true;
    }

    /**
     * Allows a user to leave a project. This method checks if the user is part of the project and not the creator. It
     * then removes the user from the project and sends notifications and emails to the project's managers informing them
     * of the user's departure.
     *
     * @param userId The ID of the user leaving the project.
     * @param projectSystemName The system name of the project the user is leaving.
     * @param reason The reason provided by the user for leaving the project.
     * @return A string message indicating the outcome of the operation.
     * @throws IllegalArgumentException If the user is not in the project or is the project's creator.
     */
    public String leaveProject(int userId, String projectSystemName, String reason) {
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);
        UserEntity user = userDao.findById(userId);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectId(userId, project.getId());
        if (userProject == null) {
            throw new IllegalArgumentException("User is not in the project");
        }
        if (userProject.getType() == UserInProjectTypeENUM.CREATOR) {
            throw new IllegalArgumentException("Cannot leave the project if you are the creator");
        }
        userProjectDao.remove(userProject);

        taskBean.removeUserFromProjectTasks(userId, project.getId());

        List<UserEntity> teamManagers = userProjectDao.findCreatorsAndManagersByProjectId(project.getId());
        UserEntity admnistration = userDao.findBySystemUsername("administration");
        for (UserEntity teamManager : teamManagers) {
            notificationBean.createLeftProjectNotification(projectSystemName, user.getSystemUsername(), teamManager);
            if(teamManager.getLanguage() == LanguageENUM.ENGLISH){
            String subject = "User " + user.getFullName() + " left project " + project.getName();
            String text = "<p>Dear " + teamManager.getFullName() + ",</p>" +
                    "<p>We regret to inform you that user <strong>" + user.getFullName() + "</strong> has left the project <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong>.</p>" +
                    "<p>Reason: " + reason + "</p>" +
                    "<p>We apologize for any inconvenience this may cause.</p>" +
                    "<p>Best regards,<br>ILM Management Team</p>";
            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                    admnistration.getEmail(),
                    teamManager.getFullName(),
                    teamManager.getEmail()
            );
            mailBean.sendMail(admnistration, mailDto);
        } else if(teamManager.getLanguage() == LanguageENUM.PORTUGUESE){
            String subject = "Utilizador " + user.getFullName() + " saiu do projeto " + project.getName();
            String text = "<p>Caro(a) " + teamManager.getFullName() + ",</p>" +
                    "<p>Lamentamos informar que o utilizador <strong>" + user.getFullName() + "</strong> saiu do projeto <strong><a href=\"https://localhost:3000/project/" + project.getSystemName() + "\">" + project.getName() + "</a></strong>.</p>" +
                    "<p>Razão: " + reason + "</p>" +
                    "<p>Pedimos desculpa por qualquer inconveniente que isto possa causar.</p>" +
                    "<p>Com os melhores cumprimentos,<br>Equipa de Gestão ILM</p>";
            MailDto mailDto = new MailDto(
                    subject,
                    text,
                    userDao.getFullNameBySystemUsername(admnistration.getSystemUsername()),
                    admnistration.getEmail(),
                    teamManager.getFullName(),
                    teamManager.getEmail()
            );
            mailBean.sendMail(admnistration, mailDto);
        }
        }

        logBean.createMemberLeftLog(project, user);

        return "User left project successfully";

    }

}


