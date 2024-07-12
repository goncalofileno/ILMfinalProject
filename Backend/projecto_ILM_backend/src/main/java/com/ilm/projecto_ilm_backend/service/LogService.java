package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.LogBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.logs.LogDto;
import com.ilm.projecto_ilm_backend.dto.logs.LogsAndNotesPageDto;
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

/**
 * REST service for log-related operations.
 */
@Path("/log")
public class LogService {

    /**
     * Injected DatabaseValidator to validate session and auxiliary tokens.
     */
    @Inject
    DatabaseValidator databaseValidator;

    /**
     * Injected LogBean EJB.
     */
    @Inject
    LogBean logBean;

    /**
     * Injected UserBean to retrieve user information.
     */
    @Inject
    UserBean userBean;

    private static final Logger logger = LogManager.getLogger(LogService.class);

    /**
     * Retrieves logs for a specific project.
     *
     * @param sessionId the session ID of the user making the request
     * @param systemProjectName the name of the project for which logs are being requested
     * @return HTTP response containing a list of logs or an appropriate error status
     * @throws UnknownHostException if the client's IP address cannot be determined
     */
    @GET
    @Path("/logs")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectLogs(@CookieParam("session-id") String sessionId,
                                   @QueryParam("systemProjectName") String systemProjectName) throws UnknownHostException {

        // Get client IP address
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        // Get the full name of the user based on the session ID
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received request from IP: " + clientIP + " for logs of project: " + systemProjectName + " by user: " + clientName);

        // Validate the session ID
        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                // Retrieve logs for the specified project
                List<LogDto> logs = logBean.getLogsByProjectName(sessionId, systemProjectName);
                logger.info("Logs fetched successfully for project: " + systemProjectName + " by user: " + clientName);
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
            logger.error("Unauthorized access to fetch logs for project: " + systemProjectName);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    /**
     * Retrieves logs and notes for a specific project.
     *
     * @param sessionId the session ID of the user making the request
     * @param systemProjectName the name of the project for which logs and notes are being requested
     * @return HTTP response containing logs and notes or an appropriate error status
     * @throws UnknownHostException if the client's IP address cannot be determined
     */
    @GET
    @Path("/logsAndNotes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectLogsAndNotes(@CookieParam("session-id") String sessionId,
                                           @QueryParam("systemProjectName") String systemProjectName) throws UnknownHostException {

        // Get client IP address
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received request from IP: " + clientIP + " for logs and notes of project: " + systemProjectName + " by user: " + clientName);

        // Validate the session ID
        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                // Retrieve logs and notes for the specified project
                LogsAndNotesPageDto logsAndNotes = logBean.getLogsAndNotesByProjectName(sessionId, systemProjectName);
                return Response.ok(logsAndNotes).build();
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
                logger.error("Error while fetching logs and notes for project: " + systemProjectName, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An error occurred while fetching logs and notes").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }
}