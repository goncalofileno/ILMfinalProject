package com.ilm.projecto_ilm_backend.service;


import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.bean.ProjectBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.project.ProjectProfileDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectProfilePageDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectTableInfoDto;
import com.ilm.projecto_ilm_backend.dto.user.RegisterUserDto;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsForInviteeException;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsToInviteException;
import com.ilm.projecto_ilm_backend.security.exceptions.UnauthorizedAccessException;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import com.ilm.projecto_ilm_backend.validator.RegexValidator;
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
                ProjectTableInfoDto projectTableInfoDto = projectBean.getProjectTableInfo(sessionId, page, lab, status, slotsAvailable, nameAsc,
                        statusAsc, labAsc, startDateAsc, endDateAsc, keyword);
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
                    if(userBean.isUserCreatorOrManager(userId, projectSystemName)){
                        String result = projectBean.cancelProject(userId, projectSystemName, reason, sessionId);
                        return Response.ok(Collections.singletonMap("message", result)).build();
                    }else{
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to cancel projects.")).build();}
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



}
