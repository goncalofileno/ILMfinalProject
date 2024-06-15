package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.ResourceBean;
import com.ilm.projecto_ilm_backend.dto.resource.ResourceCreationDto;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Path("/resource")
public class ResourceService {
    @Inject
    private ResourceBean resourceBean;
    @Inject
    DatabaseValidator databaseValidator;

    private static final Logger logger = LogManager.getLogger(ProjectService.class);



    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResources(@CookieParam("session-id") String sessionId, @QueryParam("page") int page, @QueryParam("brand") String brand,
                                 @QueryParam("type") String type, @QueryParam("supplier")String supplierName,@QueryParam("keyword")String searchKeyword,
                                 @QueryParam("nameAsc")String nameAsc, @QueryParam("typeAsc")String typeAsc, @QueryParam("brandAsc")String brandAsc,
                                 @QueryParam("supplierAsc")String supplierAsc) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to receive all resources from a user from IP address: " + clientIP);
        if(databaseValidator.checkSessionId(sessionId)) {
            try {
                return Response.ok(resourceBean.getResourceDetails(page,brand,type,supplierName,searchKeyword, nameAsc,typeAsc,brandAsc,supplierAsc)).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving home projects: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving all resources").build();
            }
        }else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTypes(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to receive all resources types from a user from IP address: " + clientIP);
        if(databaseValidator.checkSessionId(sessionId)) {
            try {
                return Response.ok(resourceBean.getAllTypes()).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving all resources types: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving all resources types").build();
            }
        }else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }
    @GET
    @Path("/brands")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBrands(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to receive all resources brands from a user from IP address: " + clientIP);
        if(databaseValidator.checkSessionId(sessionId)) {
            try {
                return Response.ok(resourceBean.getAllBrands()).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving all resources brands: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving all resources brands").build();
            }
        }else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createResource(@CookieParam("session-id") String sessionId, ResourceCreationDto resourceCreationDto) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to create a resource from a user from IP address: " + clientIP);
        if(databaseValidator.checkSessionId(sessionId)) {
            try {
                if(resourceBean.createResource(resourceCreationDto)){
                    logger.info("A new resource named "+resourceCreationDto.getName()+" from a user from IP address: " + clientIP);
                    return Response.ok().build();
                }else {
                    return Response.status(Response.Status.BAD_REQUEST).entity("This resource already exists").build();
                }
            } catch (Exception e) {
                logger.error("An error occurred while creating a new resource: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while creating a new resource:").build();
            }
        }else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }
}
