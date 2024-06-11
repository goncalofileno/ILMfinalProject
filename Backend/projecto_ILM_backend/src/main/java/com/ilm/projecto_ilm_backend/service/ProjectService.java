package com.ilm.projecto_ilm_backend.service;


import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.bean.ProjectBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.project.ProjectProfileDto;
import com.ilm.projecto_ilm_backend.dto.user.RegisterUserDto;
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
import java.util.List;

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

        try{
            System.out.println("projetos :   "+projectBean.getProjectsDtosHome());
            return Response.ok(projectBean.getProjectsDtosHome()).build();
        } catch (Exception e) {
            logger.error("An error occurred while retrieving home projects: " + e.getMessage()+" from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving home projects").build();
        }

    }

    @GET
    @Path("/tableProjects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTableProjects(@CookieParam("session-id") String sessionId, @QueryParam("page") int page) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to to receive all table projects a user from IP address: " + clientIP);

        if(databaseValidator.checkSessionId(sessionId)) {
            try {
                return Response.ok(projectBean.getProjectsDtosTable(sessionId,page)).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving home projects: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving home projects").build();
            }
        }else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    @GET
    @Path("/userOwnerProjectsToInvite")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProjects(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to receive projects for user from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                List<ProjectProfileDto> projects = projectBean.getProjectsByUserRoleToInvite(userId, UserInProjectTypeENUM.CREATOR, UserInProjectTypeENUM.MANAGER);
                return Response.ok(projects).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving user projects: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving user projects").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
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
                if (projectBean.isUserCreatorOrManager(userId, projectName)) {
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
}
