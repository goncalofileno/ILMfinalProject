package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.TaskBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.task.TaskSuggestionDto;
import com.ilm.projecto_ilm_backend.dto.task.TasksPageDto;
import com.ilm.projecto_ilm_backend.dto.task.UpdateTaskDto;
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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Path("/task")
public class TaskService {

    @Inject
    TaskBean taskBean;

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    UserBean userBean;

    private static final Logger logger = LogManager.getLogger(TaskService.class);

    /**
     * Retrieves a paginated list of tasks for a given project.
     * This endpoint is accessed via a GET request and produces a JSON response containing tasks.
     *
     * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
     * @param systemProjectName The name of the project for which tasks are being requested.
     * @return A {@link Response} object containing the paginated list of tasks.
     *         Returns HTTP Status 200 (OK) with the tasks data if successful.
     *         Returns HTTP Status 404 (Not Found) if the project is not found.
     *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
     *         Returns HTTP Status 403 (Forbidden) if the user is not part of the project.
     *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the retrieval process.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/tasksPage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasks(@CookieParam("session-id") String sessionId,
                             @QueryParam("systemProjectName") String systemProjectName) throws UnknownHostException{

        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received request to get tasks page for project: " + systemProjectName + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
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
            logger.error("Invalid session id: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid session id").build();
        }
    }

    /**
     * Retrieves task suggestions for a given project.
     * This endpoint is accessed via a GET request and produces a JSON response containing task suggestions.
     *
     * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
     * @param systemProjectName The name of the project for which task suggestions are being requested.
     * @return A {@link Response} object containing the task suggestions.
     *         Returns HTTP Status 200 (OK) with the task suggestions if successful.
     *         Returns HTTP Status 404 (Not Found) if the project is not found.
     *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
     *         Returns HTTP Status 403 (Forbidden) if the user is not part of the project.
     *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the retrieval process.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/tasksSuggestions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasksSuggestions(@CookieParam("session-id") String sessionId,
                                        @QueryParam("systemProjectName") String systemProjectName) throws UnknownHostException {

        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received request to get tasks suggestions for project: " + systemProjectName + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
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
            logger.error("Invalid session id: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid session id").build();
        }
    }

    /**
     * Updates a task based on the provided task details.
     * This endpoint is accessed via a PUT request and produces a JSON response.
     *
     * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
     * @param updateTaskDto The {@link UpdateTaskDto} object containing the updated task details.
     * @return A {@link Response} object indicating the outcome of the update operation.
     *         Returns HTTP Status 200 (OK) if the task is updated successfully.
     *         Returns HTTP Status 404 (Not Found) if the project or task is not found.
     *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
     *         Returns HTTP Status 403 (Forbidden) if the user is not part of the project.
     *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the update process.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @PUT
    @Path("/updateTask")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTask(@CookieParam("session-id") String sessionId, UpdateTaskDto updateTaskDto) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received request to update task: " + updateTaskDto.getId() + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                taskBean.updateTask(sessionId, updateTaskDto);
                return Response.ok("Task updated successfully").build();
            } catch (ProjectNotFoundException e) {
                logger.error("Project not found for task: " + updateTaskDto.getId(), e);
                return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
            } catch (UserNotFoundException e) {
                logger.error("User not found for session id: " + sessionId, e);
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            } catch (UserNotInProjectException e) {
                logger.error("User not part of project for task: " + updateTaskDto.getId(), e);
                return Response.status(Response.Status.FORBIDDEN).entity("User not part of project").build();
            } catch (Exception e) {
                logger.error("Error while updating task: " + updateTaskDto.getId(), e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An error occurred while updating the task").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid session id").build();
        }
    }

    /**
     * Adds a new task based on the provided task details.
     * This endpoint is accessed via a POST request and produces a JSON response.
     *
     * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
     * @param updateTaskDto The {@link UpdateTaskDto} object containing the new task details.
     * @return A {@link Response} object indicating the outcome of the add operation.
     *         Returns HTTP Status 200 (OK) if the task is added successfully.
     *         Returns HTTP Status 404 (Not Found) if the project is not found.
     *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
     *         Returns HTTP Status 403 (Forbidden) if the user is not part of the project.
     *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the add process.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/addTask")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTask(@CookieParam("session-id") String sessionId, UpdateTaskDto updateTaskDto) throws UnknownHostException{
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received request to add task: " + updateTaskDto.getTitle() + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                taskBean.addTask(sessionId, updateTaskDto);
                return Response.ok("Task added successfully").build();
            } catch (ProjectNotFoundException e) {
                logger.error("Project not found for task: " + updateTaskDto.getTitle(), e);
                return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
            } catch (UserNotFoundException e) {
                logger.error("User not found for session id: " + sessionId, e);
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            } catch (UserNotInProjectException e) {
                logger.error("User not part of project for task: " + updateTaskDto.getTitle(), e);
                return Response.status(Response.Status.FORBIDDEN).entity("User not part of project").build();
            } catch (Exception e) {
                logger.error("Error while adding new task: " + updateTaskDto.getTitle(), e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An error occurred while adding the task").build();
            }
        } else {
            logger.error("Invalid session id: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid session id").build();
        }
    }

    @POST
    @Path("/deleteTask")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTask(@CookieParam("session-id") String sessionId, UpdateTaskDto updateTaskDto) throws UnknownHostException{
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received request to delete task: " + updateTaskDto.getId() + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                taskBean.deleteTask(sessionId, updateTaskDto);
                return Response.ok("Task deleted successfully").build();
            } catch (ProjectNotFoundException e) {
                logger.error("Project not found for task: " + updateTaskDto.getId(), e);
                return Response.status(Response.Status.NOT_FOUND).entity("Project not found").build();
            } catch (UserNotFoundException e) {
                logger.error("User not found for session id: " + sessionId, e);
                return Response.status(Response.Status.UNAUTHORIZED).entity("User not found").build();
            } catch (UserNotInProjectException e) {
                logger.error("User not part of project for task: " + updateTaskDto.getId(), e);
                return Response.status(Response.Status.FORBIDDEN).entity("User not part of project").build();
            } catch (Exception e) {
                logger.error("Error while deleting task: " + updateTaskDto.getId(), e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An error occurred while deleting the task").build();
            }
        } else {
            logger.error("Invalid session id: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid session id").build();
        }
    }

}
