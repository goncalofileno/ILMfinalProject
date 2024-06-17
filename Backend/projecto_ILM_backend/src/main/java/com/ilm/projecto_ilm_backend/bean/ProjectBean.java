package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.*;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.mail.MailDto;
import com.ilm.projecto_ilm_backend.dto.project.*;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import com.ilm.projecto_ilm_backend.entity.*;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsForInviteeException;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsToInviteException;
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

    @Transactional
    public void createDefaultTasksIfNotExistent() {
        for(int i = 1; i < numberOfProjectsToCreate + 1; i++) {

            TaskEntity task1 = new TaskEntity();
            task1.setTitle("Task 1");
            task1.setDescription("This task aims to develop the front-end of the system.");
            task1.setInitialDate(LocalDateTime.now().minus(4, ChronoUnit.YEARS));
            task1.setFinalDate(LocalDateTime.now().plus(3, ChronoUnit.YEARS));
            task1.setStatus(TaskStatusENUM.PLANNED);
            task1.setProject(projectDao.findById(i));
            taskDao.persist(task1);

            UserTaskEntity userTask1 = new UserTaskEntity();
            userTask1.setTask(task1);
            userTask1.setUser(userDao.findById(1));
            userTask1.setType(UserInTaskTypeENUM.CREATOR);
            userTaskDao.persist(userTask1);

            UserTaskEntity userTask2 = new UserTaskEntity();
            userTask2.setTask(task1);
            userTask2.setUser(userDao.findById(2));
            userTask2.setType(UserInTaskTypeENUM.MEMBER);
            userTaskDao.persist(userTask2);

            TaskEntity task2 = new TaskEntity();
            task2.setTitle("Task 2");
            task2.setDescription("This task aims to develop the back-end of the system.");
            task2.setInitialDate(LocalDateTime.now().minus(3, ChronoUnit.YEARS));
            task2.setFinalDate(LocalDateTime.now().plus(2, ChronoUnit.YEARS));
            task2.setStatus(TaskStatusENUM.IN_PROGRESS);
            task2.setProject(projectDao.findById(i));
            List<TaskEntity> tasks = new ArrayList<>();
            tasks.add(task1);
            task2.setDependentTasks(tasks);
            taskDao.persist(task2);

            UserTaskEntity userTask3 = new UserTaskEntity();
            userTask3.setTask(task2);
            userTask3.setUser(userDao.findById(1));
            userTask3.setType(UserInTaskTypeENUM.CREATOR);
            userTaskDao.persist(userTask3);

            UserTaskEntity userTask4 = new UserTaskEntity();
            userTask4.setTask(task2);
            userTask4.setUser(userDao.findById(2));
            userTask4.setType(UserInTaskTypeENUM.MEMBER);
            userTaskDao.persist(userTask4);

            TaskEntity task3 = new TaskEntity();
            task3.setTitle("Task 3");
            task3.setDescription("This task aims to develop the database of the system.");
            task3.setInitialDate(LocalDateTime.now().minus(2, ChronoUnit.YEARS));
            task3.setFinalDate(LocalDateTime.now().plus(1, ChronoUnit.YEARS));
            task3.setStatus(TaskStatusENUM.DONE);
            task3.setProject(projectDao.findById(i));
            List<TaskEntity> tasks2 = new ArrayList<>();
            tasks2.add(task1);
            tasks2.add(task2);
            task3.setDependentTasks(tasks2);
            taskDao.persist(task3);

            UserTaskEntity userTask5 = new UserTaskEntity();
            userTask5.setTask(task3);
            userTask5.setUser(userDao.findById(1));
            userTask5.setType(UserInTaskTypeENUM.CREATOR);
            userTaskDao.persist(userTask5);

            UserTaskEntity userTask6 = new UserTaskEntity();
            userTask6.setTask(task3);
            userTask6.setUser(userDao.findById(2));
            userTask6.setType(UserInTaskTypeENUM.MEMBER);
            userTaskDao.persist(userTask6);
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
        if (userProjectDao.countUserProjects() < 20) {

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

        if (userProjectDao.findById(21) != null) {
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
        projectTableInfoDto.setMaxPageNumber(calculateMaximumPageTableProjects(projectDao.getNumberOfProjectsTableDtoInfo(lab, state, slotsAvailable, keyword)));

        return projectTableInfoDto;
    }

//    public ProjectTableInfoDto getMyProjectsPageInfo(String sessionId, int page){
//
//    }

    public ArrayList<StateProjectENUM> getAllStatus() {
        ArrayList<StateProjectENUM> status = new ArrayList<>();
        for (StateProjectENUM state : StateProjectENUM.values()) {
            status.add(state);
        }
        return status;
    }


    public int calculateMaximumPageTableProjects(int numberOfProjects) {
        return (int) Math.ceil((double) numberOfProjects / NUMBER_OF_PROJECTS_PER_PAGE);
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
        String text = "<p>User " + "<strong>" + sender.getFirstName() + " " + sender.getLastName() + "</strong>" + " has invited you to join the project <strong>" + project.getName() + "</strong>.</p>" +
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

    public String acceptInvite(int userId, String projectName) {
        ProjectEntity project = projectDao.findByName(projectName);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectIdAndType(userId, project.getId(), UserInProjectTypeENUM.PENDING_BY_INVITATION);
        userProject.setType(UserInProjectTypeENUM.MEMBER_BY_INVITATION);
        userProjectDao.merge(userProject);
        return "Invite accepted successfully";
    }

    public String rejectInvite(int userId, String projectName) {
        ProjectEntity project = projectDao.findByName(projectName);
        UserProjectEntity userProject = userProjectDao.findByUserIdAndProjectIdAndType(userId, project.getId(), UserInProjectTypeENUM.PENDING_BY_INVITATION);
        userProjectDao.remove(userProject);
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
        if (userProjectDao.isUserCreatorOrManager(userId, project.getId())) {
            if (project.getStatus() == StateProjectENUM.PLANNING || project.getStatus() == StateProjectENUM.READY) {
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

    //metodo que devolve o tipo de membro que o utilizador é num projecto, se o mesmo não for membro do projecto, verifica
    //se o utilizador é do type ADMIN, e assim devolve ADMIN, se não devolve GUEST

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

}
