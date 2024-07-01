package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.TaskBean;
import com.ilm.projecto_ilm_backend.dto.task.TaskSuggestionDto;
import com.ilm.projecto_ilm_backend.dto.task.TasksPageDto;
import com.ilm.projecto_ilm_backend.security.exceptions.ProjectNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotInProjectException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;

import java.util.List;

@Path("/task")
public class TaskService {

    @Inject
    TaskBean taskBean;

    @Inject
    DatabaseValidator databaseValidator;

    private static final Logger logger = LogManager.getLogger(TaskService.class);

    @GET
    @Path("/tasksSuggestions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasksSuggestions(@CookieParam("session-id") String sessionId,
                                        @QueryParam("systemProjectName") String systemProjectName) {

        logger.info("Received request to get task suggestions for project: " + systemProjectName);

        if (databaseValidator.checkSessionId(sessionId)){
            try {
                List<TaskSuggestionDto> tasksSuggestions = taskBean.getTasksSuggestions(sessionId, systemProjectName);
                return Response.ok(tasksSuggestions).build();
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
                logger.error("Error while fetching tasks suggestions for project: " + systemProjectName, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An error occurred while fetching tasks suggestions").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid session id").build();
        }
    }

    //Serviço que recebe um systemProjectName e devolve uma lista de tarefas pertececentes a esse projeto, assegura que o utilizador está autenticado e que o projeto existe, e que o utilizador pertence ao projeto
    @GET
    @Path("/tasksPage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasks(@CookieParam("session-id") String sessionId,
                             @QueryParam("systemProjectName") String systemProjectName) {

        logger.info("Received request to get tasks for project: " + systemProjectName);

        if (databaseValidator.checkSessionId(sessionId)){
            try {
                TasksPageDto tasksPageDto = taskBean.getTasksPageDto(sessionId, systemProjectName);
                return Response.ok(tasksPageDto).build();
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
                logger.error("Error while fetching tasks suggestions for project: " + systemProjectName, e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An error occurred while fetching tasks suggestions").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid session id").build();
        }
    }


}
