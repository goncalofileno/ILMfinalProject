package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.SupplierBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Service class for managing supplier-related operations.
 * This class handles HTTP requests related to suppliers, providing functionalities
 * such as retrieving all supplier names and finding supplier contact information by name.
 * It requires client authentication, verified through a session ID passed as a cookie.
 */
@Path("/supplier")
public class SupplierService {

    @Inject
    SupplierBean supplierBean;
    @Inject
    DatabaseValidator databaseValidator;
    @Inject
    UserBean userBean;

    private static final Logger logger = LogManager.getLogger(ProjectService.class);

    /**
     * Retrieves all supplier names from the database.
     * This endpoint is accessed via a GET request and produces a JSON response containing all supplier names.
     *
     * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
     * @return A {@link Response} object containing all supplier names.
     *         Returns HTTP Status 200 (OK) with the supplier names data if successful.
     *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
     *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the retrieval process.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/names")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNames(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to retrieve all suppliers names from IP address: " + clientIP + " by user: " + clientName);
        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                logger.info("All suppliers names retrieved successfully.");
                return Response.ok(supplierBean.getAllNames()).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving all suppliers names: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving all suppliers names").build();
            }
        } else {
            logger.error("Unauthorized access to suppliers names.");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    /**
     * Finds the contact information of a supplier by their name.
     * This endpoint is accessed via a GET request and produces a JSON response containing the supplier's contact information.
     *
     * @param sessionId The session ID of the user, obtained from a cookie, used for authentication.
     * @param supplierName The name of the supplier for which contact information is being requested.
     * @return A {@link Response} object containing the supplier's contact information.
     *         Returns HTTP Status 200 (OK) with the contact information if the supplier is found.
     *         Returns HTTP Status 404 (Not Found) if the supplier is not found.
     *         Returns HTTP Status 401 (Unauthorized) if the session ID is invalid or not provided.
     *         Returns HTTP Status 500 (Internal Server Error) if an error occurs during the search process.
     * @throws UnknownHostException If the IP address of the host could not be determined.
     */
    @GET
    @Path("/contact")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findSupplierContactByName(@CookieParam("session-id") String sessionId, @QueryParam("supplierName")String supplierName) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to verify if the name of a supplier exists from IP address: " + clientIP + " by user: " + clientName);
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
            logger.error("Unauthorized access to verify if the name of a supplier exists.");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }
}