package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.ResourceBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.resource.ResourceCreationDto;
import com.ilm.projecto_ilm_backend.dto.resource.ResourceDto;
import com.ilm.projecto_ilm_backend.dto.resource.ResourceTableDto;
import com.ilm.projecto_ilm_backend.dto.resource.ResourcesProjectProfileDto;
import com.ilm.projecto_ilm_backend.dto.user.RejectedIdsDto;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling resource-related operations.
 */
@Path("/resource")
public class ResourceService {
    @Inject
    private ResourceBean resourceBean;

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    UserBean userBean;

    private static final Logger logger = LogManager.getLogger(ProjectService.class);




 /**
 * Retrieves a paginated list of resources filtered by various criteria.
 * This endpoint is accessed via a POST request and produces a JSON response.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
 * @param page The page number for pagination.
 * @param brand The brand name to filter resources by.
 * @param type The type of resource to filter by.
 * @param supplierName The name of the supplier to filter resources by.
 * @param searchKeyword A keyword for searching resources.
 * @param nameAsc Sort order for resource names ("asc" for ascending, any other value for no sorting).
 * @param typeAsc Sort order for resource types ("asc" for ascending, any other value for no sorting).
 * @param brandAsc Sort order for brands ("asc" for ascending, any other value for no sorting).
 * @param supplierAsc Sort order for suppliers ("asc" for ascending, any other value for no sorting).
 * @param rejectedIdsDto A DTO containing IDs of resources to be excluded from the result.
 * @return A {@link Response} object containing the filtered list of resources.
 *         Returns HTTP Status 200 (OK) with the list of resources if successful.
 *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
 *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the retrieval process.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@POST
@Path("/getResources")
@Produces(MediaType.APPLICATION_JSON)
public Response getResources(@CookieParam("session-id") String sessionId, @QueryParam("page") int page, @QueryParam("brand") String brand,
                             @QueryParam("type") String type, @QueryParam("supplier")String supplierName, @QueryParam("keyword")String searchKeyword,
                             @QueryParam("nameAsc")String nameAsc, @QueryParam("typeAsc")String typeAsc, @QueryParam("brandAsc")String brandAsc,
                             @QueryParam("supplierAsc")String supplierAsc, RejectedIdsDto rejectedIdsDto) throws UnknownHostException {

    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to receive all resources from a user from IP address: " + clientIP + " and name: " + clientName);

    if(databaseValidator.checkSessionId(sessionId)) {
        try {
            logger.info("Received a request to receive all resources from a user from IP address: " + clientIP + " and name: " + clientName);
            return Response.ok(resourceBean.getResourceDetails(page,brand,type,supplierName,searchKeyword, nameAsc,typeAsc,brandAsc,supplierAsc, rejectedIdsDto)).build();
        } catch (Exception e) {
            logger.error("An error occurred while retrieving home projects: " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving all resources").build();
        }
    }else {
        logger.error("Unauthorized access from IP address: " + clientIP);
        return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
    }
}


    /**
 * Retrieves a specific resource by its ID and optionally enriches it with supplier information.
 * This endpoint is accessed via a GET request and produces a JSON response.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
 * @param id The unique identifier of the resource to retrieve.
 * @param supplierName The name of the supplier to enrich the resource information with. If "null", no enrichment occurs.
 * @return A {@link Response} object containing the requested resource.
 *         Returns HTTP Status 200 (OK) with the resource data if successful.
 *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
 *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the retrieval process.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@GET
@Path("/{id}/{supplier}")
@Produces(MediaType.APPLICATION_JSON)
public Response getResource(@CookieParam("session-id") String sessionId,@PathParam("id") int id,@PathParam("supplier")String supplierName) throws UnknownHostException {
    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to get the resource with id "+id+" from a user from IP address: " + clientIP + " and name: " + clientName);
    if(databaseValidator.checkSessionId(sessionId)) {
        try {
            ResourceDto resourceDto=resourceBean.getResourceById(id);
            if(!supplierName.equals("null")) {
                resourceBean.setResourceDtoSupplier(resourceDto, supplierName);
            }
            logger.info("Resource with id "+id+" retrieved successfully from IP address: " + clientIP);
            return Response.ok(resourceDto).build();
        } catch (Exception e) {
            logger.error("An error occurred while retrieving the resource with id "+id+" : " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving the resource with id "+id).build();
        }
    }else {
        logger.error("Unauthorized access from IP address: " + clientIP);
        return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
    }
}

    /**
 * Edits an existing resource based on the provided {@link ResourceDto} details.
 * This endpoint is accessed via a PATCH request and consumes JSON data.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
 * @param resourceDto The {@link ResourceDto} containing the updated details of the resource.
 * @return A {@link Response} object indicating the outcome of the edit operation.
 *         Returns HTTP Status 200 (OK) if the resource is edited successfully.
 *         Returns HTTP Status 400 (Bad Request) if the resource already exists or cannot be edited due to validation constraints.
 *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
 *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the editing process.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@PATCH
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
public Response getResource(@CookieParam("session-id") String sessionId,ResourceDto resourceDto) throws UnknownHostException {
    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to edit the resource "+resourceDto.getName()+" from a user from IP address: " + clientIP + " and name: " + clientName);
    if(databaseValidator.checkSessionId(sessionId)) {
        try {
            if(resourceBean.editResource(resourceDto)){
                logger.info("Resource "+resourceDto.getName()+" edited successfully from IP address: " + clientIP);
                return Response.ok().build();
            }else {
                logger.error("This resource already exist");
                return Response.status(Response.Status.BAD_REQUEST).entity("This resource already exist").build();
            }
        } catch (Exception e) {
            logger.error("An error occurred while editing the resource "+resourceDto.getName()+": " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while editing the resource " +resourceDto.getName()).build();
        }
    }else {
        logger.error("Unauthorized access from IP address: " + clientIP);
        return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
    }
}

/**
 * Retrieves the resources associated with a specific project.
 * This endpoint is accessed via a GET request and produces a JSON response.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
 * @param projectSystemName The system name of the project for which resources are being requested.
 * @return A {@link Response} object containing the resources of the specified project.
 *         Returns HTTP Status 200 (OK) with the resources data if successful.
 *         Returns HTTP Status 400 (Bad Request) if the specified project does not exist.
 *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
 *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the retrieval process.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@GET
@Path("/project/{projectSystemName}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response getProjectResources(@CookieParam("session-id") String sessionId,@PathParam("projectSystemName")String projectSystemName) throws UnknownHostException {
    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to get the resources of the project "+projectSystemName+" from a user from IP address: " + clientIP + " and name: " + clientName);
    if(databaseValidator.checkSessionId(sessionId)) {
//            List<ResourceTableDto> resourceTableDtos=resourceBean.getProjectResources(projectSystemName);
        ResourcesProjectProfileDto resourcesProjectProfileDto=resourceBean.getResourcesProjectProfile(sessionId,projectSystemName);
        if(resourcesProjectProfileDto!=null) {
            logger.info("Resources of the project "+projectSystemName+" retrieved successfully from IP address: " + clientIP);
            return Response.ok(resourcesProjectProfileDto).build();
        }else {
            logger.error("This project does not exist");
            return Response.status(Response.Status.BAD_REQUEST).entity("This project does not exist").build();
        }
    }else {
        logger.error("Unauthorized access from IP address: " + clientIP);
        return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
    }
}

    /**
 * Retrieves all available resource types.
 * This endpoint is accessed via a GET request and produces a JSON response containing all resource types.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
 * @return A {@link Response} object containing all resource types.
 *         Returns HTTP Status 200 (OK) with the resource types data if successful.
 *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
 *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the retrieval process.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@GET
@Path("/types")
@Produces(MediaType.APPLICATION_JSON)
public Response getAllTypes(@CookieParam("session-id") String sessionId) throws UnknownHostException {
    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to receive all resources types from a user from IP address: " + clientIP + " and name: " + clientName);
    if(databaseValidator.checkSessionId(sessionId)) {
        try {
            logger.info("Received a request to receive all resources types from a user from IP address: " + clientIP + " and name: " + clientName);
            return Response.ok(resourceBean.getAllTypes()).build();
        } catch (Exception e) {
            logger.error("An error occurred while retrieving all resources types: " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving all resources types").build();
        }
    }else {
        logger.error("Unauthorized access from IP address: " + clientIP);
        return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
    }
}

/**
 * Retrieves all available brands from the database.
 * This endpoint is accessed via a GET request and produces a JSON response containing all brands.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
 * @return A {@link Response} object containing all brands.
 *         Returns HTTP Status 200 (OK) with the brands data if successful.
 *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
 *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the retrieval process.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@GET
@Path("/brands")
@Produces(MediaType.APPLICATION_JSON)
public Response getAllBrands(@CookieParam("session-id") String sessionId) throws UnknownHostException {
    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to receive all resources brands from a user from IP address: " + clientIP + " and name: " + clientName);
    if(databaseValidator.checkSessionId(sessionId)) {
        try {
            logger.info("Received a request to receive all resources brands from a user from IP address: " + clientIP + " and name: " + clientName);
            return Response.ok(resourceBean.getAllBrands()).build();
        } catch (Exception e) {
            logger.error("An error occurred while retrieving all resources brands: " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving all resources brands").build();
        }
    }else {
        logger.error("Unauthorized access from IP address: " + clientIP);
        return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
    }
}

    /**
 * Creates a new resource in the system.
 * This endpoint is accessed via a POST request and produces a JSON response.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
 * @param resourceCreationDto The {@link ResourceCreationDto} object containing the details of the resource to be created.
 * @return A {@link Response} object indicating the outcome of the creation operation.
 *         Returns HTTP Status 200 (OK) if the resource is created successfully.
 *         Returns HTTP Status 400 (Bad Request) if the resource already exists or cannot be created due to validation constraints.
 *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
 *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the creation process.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@POST
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public Response createResource(@CookieParam("session-id") String sessionId, ResourceCreationDto resourceCreationDto) throws UnknownHostException {
    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to create a new resource named "+resourceCreationDto.getName()+" from a user from IP address: " + clientIP + " and name: " + clientName);
    if(databaseValidator.checkSessionId(sessionId)) {
        try {
            if(resourceBean.createResource(resourceCreationDto)){
                logger.info("A new resource named "+resourceCreationDto.getName()+" from a user from IP address: " + clientIP);
                return Response.ok().build();
            }else {
                logger.error("This resource already exists");
                return Response.status(Response.Status.BAD_REQUEST).entity("This resource already exists").build();
            }
        } catch (Exception e) {
            logger.error("An error occurred while creating a new resource: " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while creating a new resource:").build();
        }
    }else {
        logger.error("Unauthorized access from IP address: " + clientIP);
        return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
    }
}

   /**
 * Retrieves available resource filters based on the specified criteria.
 * This endpoint is accessed via a GET request and produces a JSON response containing the filters.
 * It requires authentication, verified through a session ID passed as a cookie.
 *
 * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
 * @param withNames A boolean indicating if the response should include resource names.
 * @param withTypes A boolean indicating if the response should include resource types.
 * @return A {@link Response} object containing the filters for resources.
 *         Returns HTTP Status 200 (OK) with the filters data if successful.
 *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
 *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the retrieval process.
 * @throws UnknownHostException If the IP address of the host could not be determined.
 */
@GET
@Path("/filters")
@Produces(MediaType.APPLICATION_JSON)
public Response getResourcesFilters(@CookieParam("session-id") String sessionId, @QueryParam("withNames")boolean withNames, @QueryParam("withTypes")boolean withTypes) throws UnknownHostException {
    String clientIP = InetAddress.getLocalHost().getHostAddress();
    String clientName = userBean.getUserBySessionId(sessionId).getFullName();
    logger.info("Received a request to receive the resource filters from a user from IP address: " + clientIP + " and name: " + clientName);
    if(databaseValidator.checkSessionId(sessionId)) {
        try {
            logger.info("Received a request to receive the resource filters from a user from IP address: " + clientIP + " and name: " + clientName);
            return Response.ok(resourceBean.getResourceFiltersDto(withNames,withTypes)).build();
        } catch (Exception e) {
            logger.error("An error occurred while retrieving resources filters: " + e.getMessage() + " from IP address: " + clientIP);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving all resources brands").build();
        }
    }else {
        logger.error("Unauthorized access from IP address: " + clientIP);
        return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
    }
}

//    @PATCH
//    @Path("/")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response editResource(@CookieParam("session-id") String sessionId, ResourceDto resourceDto) throws UnknownHostException {
//        String clientIP = InetAddress.getLocalHost().getHostAddress();
//        logger.info("Received a request to receive the resource filters from a user from IP address: " + clientIP);
//        if(databaseValidator.checkSessionId(sessionId)) {
//            try {
//                return Response.ok(resourceBean.getResourceFiltersDto(withNames)).build();
//            } catch (Exception e) {
//                logger.error("An error occurred while retrieving resources filters: " + e.getMessage() + " from IP address: " + clientIP);
//                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving all resources brands").build();
//            }
//        }else {
//            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
//        }
//    }


}
