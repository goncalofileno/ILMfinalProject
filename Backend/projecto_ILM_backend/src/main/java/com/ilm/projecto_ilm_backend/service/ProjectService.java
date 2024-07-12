package com.ilm.projecto_ilm_backend.service;


import com.ilm.projecto_ilm_backend.ENUMS.LanguageENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.bean.ProjectBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.project.*;
import com.ilm.projecto_ilm_backend.dto.project.ProjectMembersPageDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectCreationDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectProfileDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectProfilePageDto;
import com.ilm.projecto_ilm_backend.dto.user.RejectedIdsDto;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsForInviteeException;
import com.ilm.projecto_ilm_backend.security.exceptions.NoProjectsToInviteException;
import com.ilm.projecto_ilm_backend.security.exceptions.UnauthorizedAccessException;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.ejb.EJBException;
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

/**
 * Service class for handling project-related operations.
 */
@Path("/project")
public class ProjectService {

    @Inject
    ProjectBean projectBean;
    @Inject
    DatabaseValidator databaseValidator;
    @Inject
    UserBean userBean;

    private static final Logger logger = LogManager.getLogger(ProjectService.class);


    /**
     * Handles the creation of a new project.
     * This endpoint accepts a JSON payload containing the project creation information
     * and uses a session ID from a cookie to authenticate the user making the request.
     *
     * @param sessionId              The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectCreationInfoDto The data transfer object containing all necessary information to create a project.
     * @return A {@link Response} object with the status code and body depending on the outcome.
     * If the project is created successfully, it returns HTTP Status 200 (OK) with the system name of the project.
     * If the project creation fails due to invalid input or server error, it returns HTTP Status 400 (Bad Request).
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized).
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createProject(@CookieParam("session-id") String sessionId, ProjectCreationDto projectCreationInfoDto) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to create a project from a user with IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            if (projectBean.createProject(projectCreationInfoDto, sessionId)) {
                String projectSystemName = projectBean.projectSystemNameGenerator(projectCreationInfoDto.getName());
                logger.info("Project created successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.status(Response.Status.OK).entity("{\"systemName\":\"" + projectSystemName + "\"}").build();
            } else return Response.status(Response.Status.BAD_REQUEST).build();
        } else return Response.status(Response.Status.UNAUTHORIZED).build();
    }


    /**
     * Updates the details of an existing project identified by its system name.
     * This method handles the PATCH request to update project information. It consumes and produces JSON.
     * Authentication is performed via a session ID passed as a cookie parameter.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectUpdateDto  The data transfer object containing the updated project information.
     * @param systemProjectName The system name of the project to be updated, obtained from the URL path.
     * @return A {@link Response} object containing the outcome of the update operation.
     * If the update is successful, it returns HTTP Status 200 (OK) with a message indicating success.
     * If the update fails due to invalid input or other reasons, it returns HTTP Status 400 (Bad Request) with an error message.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @PATCH
    @Path("/updateProject/{systemProjectName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProject(@CookieParam("session-id") String sessionId, ProjectCreationDto projectUpdateDto, @PathParam("systemProjectName") String systemProjectName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to update a project from a user with IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                boolean isUpdated = projectBean.updateProject(projectUpdateDto, sessionId, systemProjectName);
                if (isUpdated) {
                    logger.info("Project updated successfully from IP address: " + clientIP);
                    return Response.ok(Collections.singletonMap("message", "Project updated successfully")).build();
                } else {
                    logger.error("Error updating project from IP address: " + clientIP);
                    return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("message", "Error updating project")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while updating project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while updating project")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }


    /**
     * Retrieves the details of a specific project by its system name.
     * This endpoint is accessed via a GET request and produces a JSON response containing the project details.
     * Authentication is required to access this endpoint, which is done through a session ID passed as a cookie.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param systemProjectName The system name of the project for which details are being requested.
     * @return A {@link Response} object containing the project details if successful, or an error message if not.
     * If the session ID is valid and the project details are successfully retrieved, it returns HTTP Status 200 (OK).
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs while retrieving the project details, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/details/{systemProjectName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectDetails(@CookieParam("session-id") String sessionId, @PathParam("systemProjectName") String systemProjectName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to get project details from a user with IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                ProjectCreationDto projectDetails = projectBean.getProjectDetails(systemProjectName, sessionId);
                logger.info("Project details retrieved successfully from IP address: " + clientIP);
                return Response.ok(projectDetails).build();
            } catch (Exception e) {
                logger.error("An error occurred while getting project details: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while getting project details")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }


    /**
     * Updates the photo of a specific project identified by its name.
     * This endpoint is accessed via a PATCH request and consumes a JSON object containing the new photo information.
     * Authentication is performed via a session ID passed as a cookie parameter to ensure that the request is made by an authenticated user.
     *
     * @param sessionId   The session ID of the user, obtained from a cookie, used for authentication.
     * @param request     A map containing the new photo information. The exact structure of this map depends on the implementation of {@code uploadProjectPicture} method in {@code ProjectBean}.
     * @param projectName The name of the project whose photo is to be updated, obtained from the URL path.
     * @return A {@link Response} object indicating the outcome of the operation.
     * If the photo is updated successfully, it returns HTTP Status 200 (OK).
     * If the request fails due to bad input data, it returns HTTP Status 400 (Bad Request).
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized).
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @PATCH
    @Path("/photo/{projectName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProjectPhoto(@CookieParam("session-id") String sessionId, Map<String, String> request, @PathParam("projectName") String projectName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to update project photo from a user with IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            if (projectBean.uploadProjectPicture(request, projectName)) {
                logger.info("Project photo updated successfully from IP address: " + clientIP);
                return Response.status(Response.Status.OK).build();
            } else return Response.status(Response.Status.BAD_REQUEST).build();
        } else return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Fetches and returns a list of projects designated as "home projects".
     * This endpoint is accessed via a GET request and produces a response in JSON format.
     * It is designed to provide a quick overview or a featured selection of projects for the home page of an application.
     * <p>
     * The method logs the IP address of the requester and attempts to retrieve the home projects from the database.
     * If successful, it returns a 200 OK status with the projects data.
     * In case of any errors during the retrieval process, it logs the error and returns a 500 Internal Server Error status.
     *
     * @return A {@link Response} object containing either the list of home projects in JSON format or an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/homeProjects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHomeProjects() throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to to receive all home projects a user from IP address: " + clientIP);

        try {
            logger.info("Home projects retrieved successfully from IP address: " + clientIP);
            return Response.ok(projectBean.getProjectsDtosHome()).build();
        } catch (Exception e) {
            logger.error("An error occurred while retrieving home projects: " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving home projects").build();
        }
    }

    /**
     * Fetches and returns paginated project information for display in a table format.
     * This endpoint is accessed via a GET request and produces a JSON response containing the paginated project details.
     * It supports filtering and sorting based on various parameters such as lab, status, availability of slots, and more.
     * Authentication is required to access this endpoint, which is done through a session ID passed as a cookie.
     *
     * @param sessionId      The session ID of the user, obtained from a cookie, used for authentication.
     * @param page           The page number of the paginated response.
     * @param lab            The lab associated with the projects to filter by.
     * @param status         The status of the projects to filter by.
     * @param slotsAvailable A boolean indicating whether to filter projects by availability of slots.
     * @param nameAsc        The order of sorting by project name, ascending or not specified.
     * @param statusAsc      The order of sorting by project status, ascending or not specified.
     * @param labAsc         The order of sorting by lab, ascending or not specified.
     * @param startDateAsc   The order of sorting by project start date, ascending or not specified.
     * @param endDateAsc     The order of sorting by project end date, ascending or not specified.
     * @param keyword        A keyword to search within project names or descriptions.
     * @return A {@link Response} object containing the paginated list of projects in JSON format or an error message.
     * If the session ID is valid and the projects are successfully retrieved, it returns HTTP Status 200 (OK).
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs while retrieving the projects, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/tableProjects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTableProjects(@CookieParam("session-id") String sessionId, @QueryParam("page") int page, @QueryParam("lab") String lab, @QueryParam("status") String status,
                                     @QueryParam("slotsAvailable") boolean slotsAvailable, @QueryParam("nameAsc") String nameAsc, @QueryParam("statusAsc") String statusAsc,
                                     @QueryParam("labAsc") String labAsc, @QueryParam("startDateAsc") String startDateAsc, @QueryParam("endDateAsc") String endDateAsc, @QueryParam("keyword") String keyword) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to to receive table projects a user from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                logger.info("Table projects retrieved successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.ok(projectBean.getProjectTableInfo(sessionId, page, lab, status, slotsAvailable, nameAsc,
                        statusAsc, labAsc, startDateAsc, endDateAsc, keyword)).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving table projects: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving home projects").build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    /**
     * Retrieves a paginated list of projects associated with the current user, filtered by specified criteria.
     * This endpoint is accessed via a GET request and produces a JSON response containing the user's projects.
     * Authentication is required to access this endpoint, which is done through a session ID passed as a cookie.
     *
     * @param sessionId  The session ID of the user, obtained from a cookie, used for authentication.
     * @param page       The page number of the paginated response.
     * @param lab        The lab associated with the projects to filter by.
     * @param status     The status of the projects to filter by.
     * @param keyword    A keyword to search within project names or descriptions.
     * @param memberType The role of the user in the projects to filter by.
     * @return A {@link Response} object containing the paginated list of projects in JSON format or an error message.
     * If the session ID is valid and the projects are successfully retrieved, it returns HTTP Status 200 (OK).
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs while retrieving the projects, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/myprojects")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyProjects(@CookieParam("session-id") String sessionId, @QueryParam("page") int page, @QueryParam("lab") String lab, @QueryParam("status") String status,
                                  @QueryParam("keyword") String keyword, @QueryParam("memberType") String memberType) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to to receive my projects a user from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                logger.info("My projects retrieved successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.ok(projectBean.getMyProjectsPageInfo(sessionId, page, lab, status, keyword, memberType)).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving my projects table: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving my projects").build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }


    /**
     * Retrieves all possible statuses for projects.
     * This endpoint is accessed via a GET request and produces a JSON response containing all project statuses.
     * Authentication is required to access this endpoint, which is verified through a session ID passed as a cookie.
     *
     * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
     * @return A {@link Response} object containing all project statuses in JSON format if successful, or an error message if not.
     * If the session ID is valid and the statuses are successfully retrieved, it returns HTTP Status 200 (OK).
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs while retrieving the statuses, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/allStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTableProjects(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to to receive all status a user from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                logger.info("All status retrieved successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.ok(projectBean.getAllStatus()).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving home projects: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving home projects").build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    /**
     * Retrieves the available filters for projects.
     * This endpoint is accessed via a GET request and produces a JSON response containing the filters that can be applied when searching for projects.
     * Authentication is required to access this endpoint, which is verified through a session ID passed as a cookie.
     *
     * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
     * @return A {@link Response} object containing the filters for projects in JSON format if successful, or an error message if not.
     * If the session ID is valid and the filters are successfully retrieved, it returns HTTP Status 200 (OK).
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs while retrieving the filters, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/filters")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectsFilters(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to to receive projects filters a user from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                logger.info("Projects filters retrieved successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.ok(projectBean.getProjectsFilters()).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving projects filters: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving projects filters").build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }


    /**
     * Retrieves a list of projects owned by the current user that can be used to invite a specified user.
     * This endpoint is accessed via a GET request and produces a JSON response containing the projects suitable for invitation.
     * Authentication is required to access this endpoint, which is verified through a session ID passed as a cookie.
     *
     * @param sessionId       The session ID of the user, obtained from a cookie, used for authentication.
     * @param inviteeUsername The username of the invitee to check the projects against.
     * @return A {@link Response} object containing either a list of projects in JSON format or an error message.
     * If the session ID is valid and there are projects available for invitation, it returns HTTP Status 200 (OK) with the list of projects.
     * If there are no projects available for invitation or the invitee cannot be invited to any projects, it returns HTTP Status 200 (OK) with a specific error message.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/userOwnerProjectsToInvite")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProjects(@CookieParam("session-id") String sessionId, @QueryParam("inviteeUsername") String inviteeUsername) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to retrieve user projects to invite from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                List<ProjectProfileDto> projects = projectBean.getProjectsToInvite(userId, inviteeUsername);
                logger.info("User projects to invite retrieved successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.ok(projects).build();
            } catch (NoProjectsToInviteException | NoProjectsForInviteeException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", e.getMessage());
                logger.info("No projects to invite retrieved successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.status(Response.Status.OK).entity(errorResponse).build();
            } catch (IllegalArgumentException e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", e.getMessage());
                logger.error("An error occurred while retrieving user projects to invite: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
            } catch (EJBException e) {
                Throwable cause = e.getCause();
                if (cause instanceof NoProjectsToInviteException || cause instanceof NoProjectsForInviteeException) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", cause.getMessage());
                    logger.info("No projects to invite retrieved successfully from IP address: " + clientIP + " and name: " + clientName);
                    return Response.status(Response.Status.OK).entity(errorResponse).build();
                } else {
                    logger.error("An error occurred while retrieving user projects: " + e.getMessage() + " from IP address: " + clientIP);
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", "An error occurred while retrieving user projects");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
                }
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


    /**
     * Invites a user to join a project by their system username.
     * This endpoint is accessed via a POST request and consumes and produces JSON.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId      The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectName    The name of the project to which the user is being invited.
     * @param systemUsername The system username of the invitee.
     * @return A {@link Response} object indicating the outcome of the invitation process.
     * If the invitation is successful, it returns HTTP Status 200 (OK) with a success message.
     * If the user does not have permission to invite others to the project, it returns HTTP Status 403 (Forbidden) with an error message.
     * If an error occurs during the invitation process, it returns HTTP Status 409 (Conflict) with an error message if the issue is known,
     * or HTTP Status 500 (Internal Server Error) for any other errors.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/inviteUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response inviteUser(@CookieParam("session-id") String sessionId, @QueryParam("projectName") String projectName, @QueryParam("systemUsername") String systemUsername) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to invite user to project from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManagerByProjectName(userId, projectName)) {
                    String result = projectBean.inviteUserToProject(sessionId, systemUsername, projectName);
                    if ("User invited successfully".equals(result)) {
                        logger.info("User invited successfully to project from IP address: " + clientIP + " and name: " + clientName);
                        return Response.ok(result).build();
                    } else {
                        logger.error("An error occurred while inviting user to project: " + result + " from IP address: " + clientIP + " and name: " + clientName);
                        return Response.status(Response.Status.CONFLICT).entity(result).build();
                    }
                } else {
                    logger.error("User does not have permission to invite others to this project from IP address: " + clientIP + " and name: " + clientName);
                    return Response.status(Response.Status.FORBIDDEN).entity("User does not have permission to invite others to this project").build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while inviting user to project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while inviting user to project").build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    /**
     * Responds to an invitation to join a project.
     * This endpoint is accessed via a POST request and consumes and produces JSON.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId   The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectName The name of the project for which the invitation response is being sent.
     * @param response    The user's response to the invitation (true for accept, false for reject).
     * @return A {@link Response} object indicating the outcome of the response to the invitation.
     * If the invitation is accepted or rejected successfully, it returns HTTP Status 200 (OK) with a success message.
     * If an error occurs during the process (e.g., invalid project name, user not found), it returns HTTP Status 409 (Conflict) with an error message.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * For any other errors, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/respondToInvite")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response respondToInvite(@CookieParam("session-id") String sessionId, @QueryParam("projectName") String projectName, @QueryParam("response") boolean response) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to respond to invite from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                String result;
                if (response) {
                    result = projectBean.acceptInvite(userId, projectName);
                    if ("Invite accepted successfully".equals(result)) {
                        logger.info("Invite accepted successfully from IP address: " + clientIP + " and name: " + clientName);
                        return Response.ok(Collections.singletonMap("message", result)).build();
                    } else {
                        logger.error("An error occurred while responding to invite: " + result + " from IP address: " + clientIP + " and name: " + clientName);
                        return Response.status(Response.Status.CONFLICT).entity(Collections.singletonMap("message", result)).build();
                    }
                } else {
                    result = projectBean.rejectInvite(userId, projectName);
                    if ("Invite rejected successfully".equals(result)) {
                        logger.info("Invite rejected successfully from IP address: " + clientIP + " and name: " + clientName);
                        return Response.ok(Collections.singletonMap("message", result)).build();
                    } else {
                        logger.error("An error occurred while responding to invite: " + result + " from IP address: " + clientIP + " and name: " + clientName);
                        return Response.status(Response.Status.CONFLICT).entity(Collections.singletonMap("message", result)).build();
                    }
                }
            } catch (Exception e) {
                logger.error("An error occurred while responding to invite: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while responding to invite")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    /**
     * Fetches detailed information for a specific project identified by its name.
     * This endpoint is accessed via a GET request and produces a JSON response containing detailed information about the project.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param systemProjectName The system name of the project for which information is being requested.
     * @return A {@link Response} object containing the project information if successful, or an error message if not.
     * If the session ID is valid and the project information is successfully retrieved, it returns HTTP Status 200 (OK).
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs while retrieving the project information, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/projectInfoPage")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectInfoPage(@CookieParam("session-id") String sessionId, @QueryParam("projectName") String systemProjectName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to get project info page from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                ProjectProfilePageDto projectProfilePageDto = projectBean.getProjectInfoPage(userId, systemProjectName);
                logger.info("Project info page retrieved successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.ok(projectProfilePageDto).build();
            } catch (Exception e) {
                logger.error("An error occurred while getting project info page: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while getting project info page").build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }


    /**
     * Approves or rejects a project based on the provided parameters.
     * This endpoint is accessed via a POST request and consumes and produces JSON.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectSystemName The system name of the project to be approved or rejected.
     * @param approve           A boolean indicating whether the project is to be approved (true) or rejected (false).
     * @param reason            The reason for approval or rejection, provided by the user.
     * @return A {@link Response} object indicating the outcome of the operation.
     * If the operation is successful, it returns HTTP Status 200 (OK) with a success message.
     * If the user does not have permission to approve or reject the project, it returns HTTP Status 403 (Forbidden) with an error message.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs during the operation, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/approveOrRejectProject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveOrRejectProject(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("approve") boolean approve, @QueryParam("reason") String reason) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to approve or reject project from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                if (userBean.isUserAdmin(userId)) {
                    String result = projectBean.approveOrRejectProject(projectSystemName, approve, reason, userId, sessionId);
                    logger.info("Project approved or rejected successfully from IP address: " + clientIP + " and name: " + clientName);
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    logger.error("User does not have permission to approve or reject projects from IP address: " + clientIP + " and name: " + clientName);
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to approve or reject projects.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while approving or rejecting project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while approving or rejecting project")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }


    /**
     * Allows a user to join a project by specifying the project's system name.
     * This endpoint is accessed via a POST request and consumes and produces JSON.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectSystemName The system name of the project the user wishes to join.
     * @return A {@link Response} object indicating the outcome of the join request.
     * If the join request is successful, it returns HTTP Status 200 (OK) with a success message.
     * If the project specified does not exist or has been canceled, it returns HTTP Status 400 (Bad Request) with an error message.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * For any other errors, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/joinProject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response joinProject(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to join project from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                String result = projectBean.joinProject(userId, projectSystemName);
                logger.info("Project joined successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.ok(Collections.singletonMap("message", result)).build();
            } catch (IllegalStateException e) {
                logger.warn("Attempt to join a canceled project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("message", e.getMessage())).build();
            } catch (Exception e) {
                logger.error("An error occurred while joining project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while joining project")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    /**
     * Cancels a project based on the provided parameters.
     * This endpoint is accessed via a POST request and consumes and produces JSON.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectSystemName The system name of the project to be canceled.
     * @param reason            The reason for canceling the project, provided by the user.
     * @return A {@link Response} object indicating the outcome of the operation.
     * If the operation is successful, it returns HTTP Status 200 (OK) with a success message.
     * If the user does not have permission to cancel the project, it returns HTTP Status 403 (Forbidden) with an error message.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs during the operation, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/cancelProject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancelProject(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("reason") String reason) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to cancel project from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                if (userBean.isUserAdmin(userId)) {
                    String result = projectBean.cancelProject(userId, projectSystemName, reason, sessionId);
                    logger.info("Project canceled successfully from IP address: " + clientIP + " and name: " + clientName);
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    if (userBean.isUserCreatorOrManager(userId, projectSystemName)) {
                        String result = projectBean.cancelProject(userId, projectSystemName, reason, sessionId);
                        logger.info("Project canceled successfully from IP address: " + clientIP + " and name: " + clientName);
                        return Response.ok(Collections.singletonMap("message", result)).build();
                    } else {
                        logger.error("User does not have permission to cancel projects from IP address: " + clientIP + " and name: " + clientName);
                        return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to cancel projects.")).build();
                    }
                }
            } catch (Exception e) {
                logger.error("An error occurred while canceling project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while canceling project")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    /**
     * Marks a reason as read based on the provided parameters.
     * This endpoint is accessed via a POST request and consumes and produces JSON.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectSystemName The system name of the project to mark the reason as read.
     * @return A {@link Response} object indicating the outcome of the operation.
     * If the operation is successful, it returns HTTP Status 200 (OK) with a success message.
     * If the user does not have permission to mark the reason as read, it returns HTTP Status 403 (Forbidden) with an error message.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs during the operation, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/markReasonAsRead")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response markReasonAsRead(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to mark reason as read from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManager(userId, projectSystemName)) {
                    String result = projectBean.markReasonAsRead(userId, projectSystemName);
                    logger.info("Reason marked as read successfully from IP address: " + clientIP + " and name: " + clientName);
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    logger.error("User does not have permission to mark reason as read for this project from IP address: " + clientIP + " and name: " + clientName);
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to mark reason as read for this project.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while marking reason as read: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while marking reason as read")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    /**
     * Changes the state of a project based on the provided parameters.
     * This endpoint is accessed via a POST request and consumes and produces JSON.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectSystemName The system name of the project to change the state.
     * @param newState          The new state to which the project is to be changed.
     * @param reason            The reason for changing the project state, provided by the user.
     * @return A {@link Response} object indicating the outcome of the operation.
     * If the operation is successful, it returns HTTP Status 200 (OK) with a success message.
     * If the user does not have permission to change the project state, it returns HTTP Status 403 (Forbidden) with an error message.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs during the operation, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/changeProjectState")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeProjectState(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("newState") StateProjectENUM newState, @QueryParam("reason") String reason) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to change project state from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                String result = projectBean.changeProjectState(userId, projectSystemName, newState, reason);
                logger.info("Project state changed successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.ok(Collections.singletonMap("message", result)).build();
            } catch (UnauthorizedAccessException e) {
                logger.error("User does not have permission to change project state: " + e.getMessage() + " from IP address: " + clientIP + " and name: " + clientName);
                return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", e.getMessage())).build();
            } catch (Exception e) {
                logger.error("An error occurred while changing project state: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while changing project state")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    /**
     * Removes a user from a project based on the provided parameters.
     * This endpoint is accessed via a POST request and consumes and produces JSON.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectSystemName The system name of the project from which the user is to be removed.
     * @param userId            The ID of the user to be removed from the project.
     * @param reason            The reason for removing the user from the project, provided by the user.
     * @return A {@link Response} object indicating the outcome of the operation.
     * If the operation is successful, it returns HTTP Status 200 (OK) with a success message.
     * If the user does not have permission to remove the user from the project, it returns HTTP Status 403 (Forbidden) with an error message.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs during the operation, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/removeUserFromProject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUserFromProject(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("userId") int userId, String reason) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to remove user from project from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int currentUserId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManager(currentUserId, projectSystemName)) {
                    String result = projectBean.removeUserFromProject(projectSystemName, userId, currentUserId, reason, sessionId);
                    logger.info("User removed from project successfully from IP address: " + clientIP + " and name: " + clientName);
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    logger.error("User does not have permission to remove users from this project from IP address: " + clientIP + " and name: " + clientName);
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to remove users from this project.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while removing user from project: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while removing user from project")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    /**
     * Responds to an application to join a project based on the provided parameters.
     * This endpoint is accessed via a POST request and consumes and produces JSON.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectSystemName The system name of the project for which the application response is being sent.
     * @param userId            The ID of the user who applied to join the project.
     * @param response          The user's response to the application (true for accept, false for reject).
     * @param reason            The reason for rejecting the application, provided by the user.
     * @return A {@link Response} object indicating the outcome of the application response.
     * If the application is accepted or rejected successfully, it returns HTTP Status 200 (OK) with a success message.
     * If the user does not have permission to respond to applications for the project, it returns HTTP Status 403 (Forbidden) with an error message.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs during the operation, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/respondToApplication")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response respondToApplication(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("userId") int userId, @QueryParam("response") boolean response, String reason) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to respond to application from IP address: " + clientIP + " and name: " + clientName);

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
                    logger.info("Application responded successfully from IP address: " + clientIP + " and name: " + clientName);
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    logger.error("User does not have permission to respond to applications for this project from IP address: " + clientIP + " and name: " + clientName);
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to respond to applications for this project.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while responding to application: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while responding to application")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }

    /**
     * Adds members to a project based on the provided parameters.
     * This endpoint is accessed via a POST request and consumes and produces JSON.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId                   The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectCreationMembersDto  The DTO containing the project system name and the list of members to be added.
     * @param projectSystemName           The system name of the project to which members are to be added.
     * @return A {@link Response} object indicating the outcome of the operation.
     * If the operation is successful, it returns HTTP Status 200 (OK).
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If the project system name is invalid, it returns HTTP Status 400 (Bad Request) with an error message.
     * If an internal server error occurs during the operation, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/members/{projectSystemName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMembers(@CookieParam("session-id") String sessionId, ProjectCreationMembersDto projectCreationMembersDto, @PathParam("projectSystemName") String projectSystemName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to add members to project from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            if (projectBean.addInitialMembers(projectCreationMembersDto, projectSystemName, sessionId)) {
                logger.info("Members added to project successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.status(Response.Status.OK).build();
            } else return Response.status(Response.Status.BAD_REQUEST).build();

        } else return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    /**
     * Fetches the project members page for a specific project identified by its system name.
     * This endpoint is accessed via a GET request and produces a JSON response containing the project members page.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectSystemName The system name of the project for which the project members page is being requested.
     * @return A {@link Response} object containing the project members page if successful, or an error message if not.
     * If the session ID is valid and the project members page is successfully retrieved, it returns HTTP Status 200 (OK).
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If the user does not have permission to view the project members page, it returns HTTP Status 403 (Forbidden) with an error message.
     * If an internal server error occurs while retrieving the project members page, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/getProjectMembersPage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProjectMembersPage(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to get project members page from IP address: " + clientIP + " and name: " + clientName);
        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManager(userId, projectSystemName)) {
                    logger.info("Project members page retrieved successfully from IP address: " + clientIP + " and name: " + clientName);
                    return Response.ok(projectBean.getProjectMembersPage(userId, projectSystemName)).build();
                } else {
                    logger.error("User does not have permission to view project members from IP address: " + clientIP + " and name: " + clientName);
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to view project members.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while getting project members page: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while getting project members page")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }



   /**
 * Checks if the provided project name is valid and not already in use.
 * This endpoint is accessed via a GET request and produces a JSON response.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
 * @param projectName The name of the project to check for validity and uniqueness.
 * @return A {@link Response} object indicating the outcome of the check.
 *         If the project name is invalid (null, empty, too short, or too long), it returns HTTP Status 400 (Bad Request).
 *         If the project name is valid and not in use, it returns HTTP Status 200 (OK).
 *         If the project name is already in use, it returns HTTP Status 409 (Conflict).
 *         If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
 *         If an internal server error occurs, it returns HTTP Status 500 (Internal Server Error) with an error message.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@GET
@Path("/checkName/{projectName}")
@Produces(MediaType.APPLICATION_JSON)
public Response checkProjectName(@CookieParam("session-id") String sessionId, @PathParam("projectName") String projectName) throws UnknownHostException {
    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to check if the name of the project is valid from IP address: " + clientIP + " and name: " + clientName);

    if (databaseValidator.checkSessionId(sessionId)) {
        try {
            if (projectName == null || projectName.isEmpty() || projectName.length() < 3 || projectName.length() > 35) {
                logger.error("Invalid project name from IP address: " + clientIP + " and name: " + clientName);
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (!databaseValidator.checkProjectName(projectName)) {
                logger.info("Project name is valid from IP address: " + clientIP + " and name: " + clientName);
                return Response.ok().build();
            } else return Response.status(Response.Status.CONFLICT).build();

        } catch (Exception e) {
            logger.error("An error occurred while checking if the name of the project is valid : " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while checking if the name of the project is valid").build();
        }
    } else {
        logger.error("Unauthorized access from IP address: " + clientIP);
        return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
    }
}


    /**
     * Changes the type of a user in a project based on the provided parameters.
     * This endpoint is accessed via a POST request and consumes and produces JSON.
     * It requires authentication, verified through a session ID passed as a cookie.
     *
     * @param sessionId         The session ID of the user, obtained from a cookie, used for authentication.
     * @param projectSystemName The system name of the project in which the user type is to be changed.
     * @param userId            The ID of the user whose type is to be changed.
     * @param newType           The new type to which the user is to be changed.
     * @return A {@link Response} object indicating the outcome of the operation.
     * If the operation is successful, it returns HTTP Status 200 (OK) with a success message.
     * If the user does not have permission to change the user type in the project, it returns HTTP Status 403 (Forbidden) with an error message.
     * If the session ID is invalid or not provided, it returns HTTP Status 401 (Unauthorized) with an error message.
     * If an internal server error occurs during the operation, it returns HTTP Status 500 (Internal Server Error) with an error message.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @POST
    @Path("/changeUserInProjectType")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeUserInProjectType(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("userId") int userId, @QueryParam("newType") UserInProjectTypeENUM newType) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to change user in project type from IP address: " + clientIP + " and name: " + clientName);
        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int currentUserId = userBean.getUserBySessionId(sessionId).getId();
                if (projectBean.isUserCreatorOrManager(currentUserId, projectSystemName)) {
                    String result = projectBean.changeUserInProjectType(projectSystemName, userId, newType, currentUserId);
                    logger.info("User in project type changed successfully from IP address: " + clientIP + " and name: " + clientName);
                    return Response.ok(Collections.singletonMap("message", result)).build();
                } else {
                    logger.error("User does not have permission to change user in project type from IP address: " + clientIP + " and name: " + clientName);
                    return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to change user in project type.")).build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while changing user in project type: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while changing user in project type")).build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
        }
    }


/**
 * Updates the project's photo by adding new resources identified by their IDs.
 * This endpoint is accessed via a PATCH request and consumes JSON data.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId            The session ID of the user, obtained from a cookie, used for authentication.
 * @param projectSystemName    The system name of the project for which the photo is to be updated.
 * @param resourcesSuppliersIds A DTO containing the IDs of the resources to be added to the project.
 * @return A {@link Response} object indicating the outcome of the update operation.
 *         Returns HTTP Status 200 (OK) if the update is successful.
 *         Returns HTTP Status 400 (Bad Request) if the update fails due to invalid data.
 *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@PATCH
@Path("/{projectSystemName}")
@Consumes(MediaType.APPLICATION_JSON)
public Response updateProjectPhoto(@CookieParam("session-id") String sessionId, @PathParam("projectSystemName") String projectSystemName, RejectedIdsDto resourcesSuppliersIds) throws UnknownHostException {
    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to update project photo from IP address: " + clientIP + " and name: " + clientName);

    if (databaseValidator.checkSessionId(sessionId)) {
        if (projectBean.addResourcesToProject(projectSystemName, resourcesSuppliersIds, sessionId)) {
            logger.info("Project photo updated successfully from IP address: " + clientIP + " and name: " + clientName);
            return Response.status(Response.Status.OK).build();
        } else return Response.status(Response.Status.BAD_REQUEST).build();
    } else return Response.status(Response.Status.UNAUTHORIZED).build();
}


   /**
 * Removes an invitation for a user to join a project.
 * This endpoint is accessed via a POST request and consumes and produces JSON data.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
 * @param projectSystemName The system name of the project from which the invitation is to be removed.
 * @param userId The ID of the user whose invitation is to be removed.
 * @return A {@link Response} object indicating the outcome of the operation.
 *         Returns HTTP Status 200 (OK) with a success message if the invitation is successfully removed.
 *         Returns HTTP Status 403 (Forbidden) if the current user does not have permission to remove the invitation.
 *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
 *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the operation.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@POST
@Path("/removeInvitation")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response removeInvitation(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, @QueryParam("userId") int userId) throws UnknownHostException {
    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to remove invitation from IP address: " + clientIP + " and name: " + clientName);

    if (databaseValidator.checkSessionId(sessionId)) {
        try {
            int currentUserId = userBean.getUserBySessionId(sessionId).getId();
            if (projectBean.isUserCreatorOrManager(currentUserId, projectSystemName)) {
                String result = projectBean.removeInvitation(projectSystemName, userId, currentUserId, sessionId);
                logger.info("Invitation removed successfully from IP address: " + clientIP + " and name: " + clientName);
                return Response.ok(Response.Status.OK).entity(Collections.singletonMap("message", result)).build();
            } else {
                logger.error("User does not have permission to remove invitation from IP address: " + clientIP + " and name: " + clientName);
                return Response.status(Response.Status.FORBIDDEN).entity(Collections.singletonMap("message", "User does not have permission to remove invitation.")).build();
            }
        } catch (Exception e) {
            logger.error("An error occurred while removing invitation: " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while removing invitation")).build();
        }
    } else {
        logger.error("Unauthorized access from IP address: " + clientIP);
        return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
    }
}

   /**
 * Allows a user to leave a project.
 * This endpoint is accessed via a POST request and consumes and produces JSON data.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
 * @param projectSystemName The system name of the project from which the user wishes to leave.
 * @param reason The reason provided by the user for leaving the project.
 * @return A {@link Response} object indicating the outcome of the operation.
 *         Returns HTTP Status 200 (OK) with a success message if the operation is successful.
 *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
 *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the operation.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@POST
@Path("/leaveProject")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response leaveProject(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, String reason) throws UnknownHostException {
    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to leave project from IP address: " + clientIP + " and name: " + clientName);

    if (databaseValidator.checkSessionId(sessionId)) {
        try {
            int userId = userBean.getUserBySessionId(sessionId).getId();
            String result = projectBean.leaveProject(userId, projectSystemName, reason);
            logger.info("Project left successfully from IP address: " + clientIP + " and name: " + clientName);
            return Response.ok(Collections.singletonMap("message", result)).build();
        } catch (Exception e) {
            logger.error("An error occurred while leaving project: " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("message", "An error occurred while leaving project")).build();
        }
    } else {
        logger.error("Unauthorized access from IP address: " + clientIP);
        return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Unauthorized access")).build();
    }
}

}
