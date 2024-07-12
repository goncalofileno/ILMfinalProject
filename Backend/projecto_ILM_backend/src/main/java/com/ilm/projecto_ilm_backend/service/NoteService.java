package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.NoteBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.notes.NoteDto;
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
import java.util.Collections;

/**
 * Service class for handling note-related operations.
 */
@Path("/note")
public class NoteService {

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    NoteBean noteBean;

    @Inject
    UserBean userBean;

    private static final Logger logger = LogManager.getLogger(NoteService.class);

    /**
     * Creates a note for a specified project.
     *
     * @param sessionId         The session ID of the user.
     * @param noteDto           The note to be created.
     * @param systemProjectName The system name of the project.
     * @return A response indicating the result of the operation.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNote(@CookieParam("session-id") String sessionId,
                               NoteDto noteDto,
                               @QueryParam("systemProjectName") String systemProjectName) throws UnknownHostException {

        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("The client with IP address " + clientIP + " and name " + clientName + " requested to create a note for project " + systemProjectName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                noteBean.createNote(sessionId, noteDto, systemProjectName);
                logger.info("Note created successfully for project: " + systemProjectName);
                return Response.ok(Collections.singletonMap("message", "Note created successfully")).build();
            } catch (ProjectNotFoundException e) {
                logger.error("Project not found: " + systemProjectName, e);
                return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("error", "Project not found")).build();
            } catch (UserNotFoundException e) {
                logger.error("User not found for session id: " + sessionId, e);
                return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("error", "User not found")).build();
            } catch (UserNotInProjectException e) {
                logger.error("User not part of project: " + systemProjectName, e);
                return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("error", "User not part of project")).build();
            } catch (Exception e) {
                logger.error("Error while creating note for project: " + systemProjectName, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(Collections.singletonMap("error", "An error occurred while creating note")).build();
            }
        } else {
            logger.error("Invalid session id: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("error", "Invalid session id")).build();
        }
    }

    /**
     * Marks a note as done or undone.
     *
     * @param sessionId The session ID of the user.
     * @param id        The ID of the note.
     * @param done      The status to set (true for done, false for undone).
     * @return A response indicating the result of the operation.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @PUT
    @Path("/markAsDone")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response markAsDone(@CookieParam("session-id") String sessionId,
                               @QueryParam("id") int id,
                               @QueryParam("done") boolean done) throws UnknownHostException {

        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("The client with IP address " + clientIP + " and name " + clientName + " requested to mark note as done");

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                noteBean.markAsDone(sessionId, id, done);
                logger.info("Note marked as done successfully: " + id);
                return Response.ok().build();
            } catch (ProjectNotFoundException e) {
                logger.error("Project not found: " + id, e);
                return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
            } catch (UserNotFoundException e) {
                logger.error("User not found for session id: " + sessionId, e);
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            } catch (UserNotInProjectException e) {
                logger.error("User not part of project: " + id, e);
                return Response.status(Response.Status.FORBIDDEN).entity("User not part of project").build();
            } catch (Exception e) {
                logger.error("Error while marking note as done: " + id, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An error occurred while marking note as done").build();
            }
        } else {
            logger.error("Invalid session id: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid session id").build();
        }
    }
}
