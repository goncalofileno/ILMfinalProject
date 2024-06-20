package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.LogBean;
import com.ilm.projecto_ilm_backend.dto.logs.LogDto;
import com.ilm.projecto_ilm_backend.security.exceptions.ProjectNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotInProjectException;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Path("/log")
public class LogService {

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    LogBean logBean;

    private static final Logger logger = LogManager.getLogger(LogService.class);

    @GET
    @Path("/logs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectLogs(@CookieParam("session-id") String sessionId,
                                   @QueryParam("systemProjectName") String systemProjectName) throws UnknownHostException {

        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received request from IP: " + clientIP + " for logs of project: " + systemProjectName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                List<LogDto> logs = logBean.getLogsByProjectName(sessionId, systemProjectName);
                return Response.ok(logs).build();
            } catch (ProjectNotFoundException e) {
                logger.error("Project not found: " + systemProjectName, e);
                return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
            } catch (UserNotFoundException e) {
                logger.error("User not found for session id: " + sessionId, e);
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            } catch (UserNotInProjectException e) {
                logger.error("User not part of project: " + systemProjectName, e);
                return Response.status(Response.Status.FORBIDDEN).entity("User not part of project").build();
            } catch (Exception e) {
                logger.error("Error while fetching logs for project: " + systemProjectName, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An error occurred while fetching logs").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }


}
