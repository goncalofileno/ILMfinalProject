package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.SupplierBean;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Path("/supplier")
public class SupplierService {

    @Inject
    SupplierBean supplierBean;
    @Inject
    DatabaseValidator databaseValidator;

    private static final Logger logger = LogManager.getLogger(ProjectService.class);

    @GET
    @Path("/names")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNames(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to receive all suppliers names from a user from IP address: " + clientIP);
        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                return Response.ok(supplierBean.getAllNames()).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving all suppliers names: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving all suppliers names").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    @GET
    @Path("/contact")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findSupplierContactByName(@CookieParam("session-id") String sessionId, @QueryParam("supplierName")String supplierName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to verify if the name of a supplier exist from a user from IP address: " + clientIP);
        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                String supplierContact=supplierBean.findSupplierContactByName(supplierName);
                if (supplierContact != null) return Response.ok(supplierContact).build();
                else return Response.status(Response.Status.NOT_FOUND).entity("Supplier not found").build();
            } catch (Exception e) {
                logger.error("An error occurred while verifying if the name of a supplier exists: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while verifying if the name of a supplier exist").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }


}
