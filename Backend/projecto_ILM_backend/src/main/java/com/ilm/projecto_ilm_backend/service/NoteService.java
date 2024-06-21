package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.NoteBean;
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

@Path("/note")
public class NoteService {

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    NoteBean noteBean;

    private static final Logger logger = LogManager.getLogger(NoteService.class);

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNote(@CookieParam("session-id") String sessionId,
                               NoteDto noteDto,
                               @QueryParam("systemProjectName") String systemProjectName) throws UnknownHostException {

        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received request from IP: " + clientIP + " to create note for project: " + systemProjectName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                noteBean.createNote(sessionId, noteDto, systemProjectName);
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
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("error", "Invalid session id")).build();
        }
    }



    @PUT
    @Path("/markAsDone")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response markAsDone(@CookieParam("session-id") String SessionId,
                               @QueryParam("id") int id,
                               @QueryParam("done") boolean done) throws UnknownHostException {

        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received request from IP: " + clientIP + " to mark note as done");

        if (databaseValidator.checkSessionId(SessionId)) {
            try {
                noteBean.markAsDone(SessionId, id, done);
                return Response.ok().build();
            } catch (ProjectNotFoundException e) {
                logger.error("Project not found: " + id, e);
                return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
            } catch (UserNotFoundException e) {
                logger.error("User not found for session id: " + SessionId, e);
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
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid session id").build();
        }
    }




}
