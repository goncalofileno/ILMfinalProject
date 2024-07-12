package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.dto.lab.LabDto;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.ilm.projecto_ilm_backend.bean.LabBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * REST service for lab-related operations.
 */
@Path("/lab")
public class LabService {

    /**
     * Injected LabBean EJB
     */
    @Inject
    LabBean labBean;

    /**
     * Injected DatabaseValidator to validate session and auxiliary tokens.
     */
    @Inject
    DatabaseValidator databaseValidator;

    /**
     * Injected UserBean to retrieve user information.
     */
    @Inject
    UserBean userBean;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    /**
     * Retrieves all labs.
     *
     * @param sessionId the session ID of the user making the request
     * @return HTTP response containing a list of all labs or an unauthorized status if the session is invalid
     * @throws UnknownHostException if the client's IP address cannot be determined
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLabs(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        // Get client IP address
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        // Get the full name of the user based on the session ID
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to retrieve all labs from IP address: " + clientIP + " by user: " + clientName);

        // Validate the session ID
        if (databaseValidator.checkSessionId(sessionId)) {
            // Retrieve all labs
            List<LabDto> labs = labBean.getAllLabs();
            logger.info("Labs retrieved successfully");
            return Response.ok(labs).build();
        } else {
            logger.error("Unauthorized access to retrieve all labs");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     * Retrieves all labs using an auxiliary token.
     *
     * @param headers HTTP headers containing the authorization token
     * @return HTTP response containing a list of all labs or an unauthorized status if the auxiliary token is invalid
     * @throws UnknownHostException if the client's IP address cannot be determined
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLabsWithAuxToken(@Context HttpHeaders headers) throws UnknownHostException {
        // Get client IP address
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to retrieve all labs from IP address: " + clientIP);

        // Get the auxiliary token from the headers
        String auxiliarToken = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        // Validate the auxiliary token
        if (databaseValidator.checkAuxiliarToken(auxiliarToken)) {
            // Retrieve all labs
            List<LabDto> labs = labBean.getAllLabs();
            logger.info("Labs retrieved successfully");
            return Response.ok(labs).build();
        } else {
            logger.error("Unauthorized access to retrieve all labs");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}