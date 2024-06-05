package com.ilm.projecto_ilm_backend.service;

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

    @Inject
    DatabaseValidator databaseValidator;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    /**
     * Retrieves all labs.
     *
     * @return HTTP response containing a list of all labs
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLabs() throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to retrieve all labs from IP address: " + clientIP);

        List<LabDto> labs = labBean.getAllLabs();
        return Response.ok(labs).build();

    }

}
