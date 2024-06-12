package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.ResourceBean;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
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
    public Response getResources(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to receive all resources from a user from IP address: " + clientIP);
        if(databaseValidator.checkSessionId(sessionId)) {
            try {
                return Response.ok(resourceBean.getResourceDetails()).build();
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
}
