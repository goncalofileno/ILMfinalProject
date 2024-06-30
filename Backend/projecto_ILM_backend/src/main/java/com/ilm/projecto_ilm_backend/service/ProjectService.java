package com.ilm.projecto_ilm_backend.service;


import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.bean.ProjectBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.project.*;
import com.ilm.projecto_ilm_backend.dto.project.ProjectMembersPageDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectCreationDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectProfileDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectProfilePageDto;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsForInviteeException;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsToInviteException;
import com.ilm.projecto_ilm_backend.security.exceptions.UnauthorizedAccessException;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/project")
public class ProjectService {

    @Inject
    ProjectBean projectBean;
    @Inject
    DatabaseValidator databaseValidator;
    @Inject
    UserBean userBean;

    private static final Logger logger = LogManager.getLogger(ProjectService.class);

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProject(@CookieParam("session-id") String sessionId, ProjectCreationDto projectCreationInfoDto) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to create a project from a user with IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            if (projectBean.createProject(projectCreationInfoDto, sessionId)) {
                String projectSystemName = projectBean.projectSystemNameGenerator(projectCreationInfoDto.getName());
                return Response.status(Response.Status.OK).entity("{\"systemName\":\"" + projectSystemName + "\"}").build();
            } else return Response.status(Response.Status.BAD_REQUEST).build();
        } else return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @PATCH
    @Path("/photo/{projectName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProjectPhoto(@CookieParam("session-id") String sessionId, Map<String, String> request, @PathParam("projectName") String projectName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to create a project from a user with IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            if (projectBean.uploadProjectPicture(request, projectName)) {
                return Response.status(Response.Status.OK).build();
            } else return Response.status(Response.Status.BAD_REQUEST).build();
        } else return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/homeProjects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeProjects() throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to to receive all home projects a user from IP address: " + clientIP);

        try {
            System.out.println("projetos :   " + projectBean.getProjectsDtosHome());
            return Response.ok(projectBean.getProjectsDtosHome()).build();
        } catch (Exception e) {
            logger.error("An error occurred while retrieving home projects: " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving home projects").build();
        }
    }

    @GET
    @Path("/tableProjects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTableProjects(@CookieParam("session-id") String sessionId, @QueryParam("page") int page, @QueryParam("lab") String lab, @QueryParam("status") String status,
                                     @QueryParam("slotsAvailable") boolean slotsAvailable, @QueryParam("nameAsc") String nameAsc, @QueryParam("statusAsc") String statusAsc,
                                     @QueryParam("labAsc") String labAsc, @QueryParam("startDateAsc") String startDateAsc, @QueryParam("endDateAsc") String endDateAsc, @QueryParam("keyword") String keyword) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to to receive all table projects a user from IP address: " + clientIP);
        System.out.println("nameAsc " + nameAsc + " statusAsc " + statusAsc + " labAsc " + labAsc + " startDateAsc " + startDateAsc + " endDateAsc " + endDateAsc);


        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                return Response.ok(projectBean.getProjectTableInfo(sessionId, page, lab, status, slotsAvailable, nameAsc,
                        statusAsc, labAsc, startDateAsc, endDateAsc, keyword)).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving table projects: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving home projects").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    @GET
    @Path("/myprojects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyProjects(@CookieParam("session-id") String sessionId, @QueryParam("page") int page, @QueryParam("lab") String lab, @QueryParam("status") String status,
                                  @QueryParam("keyword") String keyword, @QueryParam("memberType") String memberType) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to to receive my projects table info from a user from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                return Response.ok(projectBean.getMyProjectsPageInfo(sessionId, page, lab, status, keyword, memberType)).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving my projects table: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving my projects").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }


    @GET
    @Path("/allStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTableProjects(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to to receive all project status a user from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                return Response.ok(projectBean.getAllStatus()).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving home projects: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving home projects").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    @GET
    @Path("/filters")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectsFilters(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to to receive projects filters a user from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                return Response.ok(projectBean.getProjectsFilters()).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving projects filters: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving projects filters").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }


    @GET
    @Path("/userOwnerProjectsToInvite")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProjects(@CookieParam("session-id") String sessionId, @QueryParam("inviteeUsername") String inviteeUsername) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to receive projects for user from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                List<ProjectProfileDto> projects = projectBean.getProjectsToInvite(userId, inviteeUsername);
                return Response.ok(projects).build();
            } catch (NoProjectsToInviteException | NoProjectsForInviteeException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", e.getMessage());
                return Response.status(Response.Status.OK).entity(errorResponse).build();
            } catch (IllegalArgumentException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", e.getMessage());
                return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving user projects: " + e.getMessage() + " from IP address: " + clientIP);
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "An error occurred while retrieving user projects");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
            }
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Unauthorized access");
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse).build();
        }
    }


    @POST
    @Path("/inviteUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response inviteUser(@CookieParam("session-id") String sessionId, @QueryParam("projectName") String projectName, @QueryParam("systemUsername") String systemUsername) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to invite user from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManagerByProjectName(userId, projectName)) {
                    String result = projectBean.inviteUserToProject(sessionId, systemUsername, projectName);
                    if ("User invited successfully".equals(result)) {
                        return Response.ok(result).build();
                    } else {
                        return Response.status(Response.Status.CONFLICT).entity(result).build();
                    }
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity("User does not have permission to invite others to this project").build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while inviting user to project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while inviting user to project").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    @POST
    @Path("/respondToInvite")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response respondToInvite(@CookieParam("session-id") String sessionId, @QueryParam("projectName") String projectName, @QueryParam("response") boolean response) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to respond to invite from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                String result;
                if (response) {
                    result = projectBean.acceptInvite(userId, projectName);
                    if ("Invite accepted successfully".equals(result)) {
                        return Response.ok(Collections.singletonMap("message", result)).build();
                    } else {
                        return Response.status(Response.Status.CONFLICT).entity(Collections.singletonMap("message", result)).build();
                    }
                } else {
                    result = projectBean.rejectInvite(userId, projectName);
                    if ("Invite rejected successfully".equals(result)) {
                        return Response.ok(Collections.singletonMap("message", result)).build();
                    } else {
                        return Response.status(Response.Status.CONFLICT).entity(Collections.singletonMap("message", result)).build();
                    }
                }
            } catch (Exception e) {
                logger.error("An error occurred while responding to invite: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while responding to invite")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    @GET
    @Path("/projectInfoPage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectInfoPage(@CookieParam("session-id") String sessionId, @QueryParam("projectName") String systemProjectName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to get project info page from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                ProjectProfilePageDto projectProfilePageDto = projectBean.getProjectInfoPage(userId, systemProjectName);
                return Response.ok(projectProfilePageDto).build();
            } catch (Exception e) {
                logger.error("An error occurred while getting project info page: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while getting project info page").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    @POST
    @Path("/approveOrRejectProject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveOrRejectProject(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("approve") boolean approve, @QueryParam("reason") String reason) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to approve or reject project from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                if (userBean.isUserAdmin(userId)) {
                    String result = projectBean.approveOrRejectProject(projectSystemName, approve, reason, userId, sessionId);
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to approve or reject projects.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while approving or rejecting project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while approving or rejecting project")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    @POST
    @Path("/joinProject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response joinProject(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to join project from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                String result = projectBean.joinProject(userId, projectSystemName);
                return Response.ok(Collections.singletonMap("message", result)).build();
            } catch (IllegalStateException e) {
                logger.warn("Attempt to join a canceled project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("message", e.getMessage())).build();
            } catch (Exception e) {
                logger.error("An error occurred while joining project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while joining project")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }


    @POST
    @Path("/cancelProject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelProject(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("reason") String reason) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to cancel project from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                if (userBean.isUserAdmin(userId)) {
                    String result = projectBean.cancelProject(userId, projectSystemName, reason, sessionId);
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    if (userBean.isUserCreatorOrManager(userId, projectSystemName)) {
                        String result = projectBean.cancelProject(userId, projectSystemName, reason, sessionId);
                        return Response.ok(Collections.singletonMap("message", result)).build();
                    } else {
                        return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to cancel projects.")).build();
                    }
                }
            } catch (Exception e) {
                logger.error("An error occurred while canceling project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while canceling project")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    @POST
    @Path("/markReasonAsRead")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response markReasonAsRead(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to mark reason as read from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManager(userId, projectSystemName)) {
                    String result = projectBean.markReasonAsRead(userId, projectSystemName);
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to mark reason as read for this project.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while marking reason as read: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while marking reason as read")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    @POST
    @Path("/changeProjectState")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeProjectState(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("newState") StateProjectENUM newState, @QueryParam("reason") String reason) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to change project state from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                String result = projectBean.changeProjectState(userId, projectSystemName, newState, reason);
                return Response.ok(Collections.singletonMap("message", result)).build();
            } catch (UnauthorizedAccessException e) {
                return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", e.getMessage())).build();
            } catch (Exception e) {
                logger.error("An error occurred while changing project state: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while changing project state")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    //Serviço que recebe um id de um user e um systemProjectName e remove esse user do projecto, só managers e creators podem remover users do projeto
    @POST
    @Path("/removeUserFromProject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUserFromProject(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("userId") int userId, String reason) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to remove user from project from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int currentUserId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManager(currentUserId, projectSystemName)) {
                    String result = projectBean.removeUserFromProject(projectSystemName, userId, currentUserId, reason, sessionId);
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to remove users from this project.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while removing user from project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while removing user from project")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    @POST
    @Path("/respondToApplication")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response respondToApplication(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("userId") int userId, @QueryParam("response") boolean response, String reason) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to respond to application from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int currentUserId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManager(currentUserId, projectSystemName)) {
                    String result;
                    if (response) {
                        result = projectBean.acceptApplication(projectSystemName, userId, currentUserId, sessionId);
                    } else {
                        result = projectBean.rejectApplication(projectSystemName, userId, currentUserId, reason, sessionId);
                    }
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to respond to applications for this project.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while responding to application: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while responding to application")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    @POST
    @Path("/members/{projectSystemName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMembers(@CookieParam("session-id") String sessionId, ProjectCreationMembersDto projectCreationMembersDto, @PathParam("projectSystemName") String projectSystemName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to create a project from a user with IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            if (projectBean.addInitialMembers(projectCreationMembersDto, projectSystemName, sessionId)) {
                return Response.status(Response.Status.OK).build();
            } else return Response.status(Response.Status.BAD_REQUEST).build();

        } else return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/getProjectMembersPage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectMembersPage(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to get project members page from IP address: " + clientIP);
        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManager(userId, projectSystemName)) {
                    return Response.ok(projectBean.getProjectMembersPage(userId, projectSystemName)).build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to view project members.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while getting project members page: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while getting project members page")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }


    @GET
    @Path("/checkName/{projectName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkProjectName(@CookieParam("session-id") String sessionId, @PathParam("projectName") String projectName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to check if the name of the project is valid from a user from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                if (projectName == null || projectName.isEmpty() || projectName.length() < 3 || projectName.length() > 35) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                if (!databaseValidator.checkProjectName(projectName)) {
                    return Response.ok().build();
                } else return Response.status(Response.Status.CONFLICT).build();

            } catch (Exception e) {
                logger.error("An error occurred while checking if the name of the project is valid : " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while checking if the name of the project is valid").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    @POST
    @Path("/changeUserInProjectType")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeUserInProjectType(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("userId") int userId, @QueryParam("newType") UserInProjectTypeENUM newType) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to change user in project type from IP address: " + clientIP);
        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int currentUserId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManager(currentUserId, projectSystemName)) {
                    String result = projectBean.changeUserInProjectType(projectSystemName, userId, newType, currentUserId);
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to change user in project type.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while changing user in project type: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while changing user in project type")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    @POST
    @Path("/removeInvitation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeInvitation(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("userId") int userId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to remove invitation from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int currentUserId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManager(currentUserId, projectSystemName)) {
                    String result = projectBean.removeInvitation(projectSystemName, userId, currentUserId, sessionId);
                    return Response.ok(Response.Status.OK).entity(Collections.singletonMap("message", result)).build();
                } else {
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to remove invitation.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while removing invitation: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while removing invitation")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

}
