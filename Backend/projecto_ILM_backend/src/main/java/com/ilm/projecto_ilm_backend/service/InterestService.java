package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import com.ilm.projecto_ilm_backend.bean.InterestBean;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ilm.projecto_ilm_backend.bean.UserBean;

import java.util.List;

/**
 * Service class for interests
 */
@Path("/interest")
public class InterestService {

    /*
        * Injects InterestBean class
     */
    @Inject
    InterestBean interestBean;

    /*
        * Injects DatabaseValidator class
     */
    @Inject
    DatabaseValidator databaseValidator;
    /*
        * Injects UserBean class
     */
    @Inject
    UserBean userBean;

    /*
        * Injects HttpHeaders class
     */
    private static final Logger logger = LogManager.getLogger(UserService.class);


    /**
     * Get all interests
     * @return Response with all interests
     * @throws UnknownHostException
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllInterests() throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();

        logger.info("Client with IP: " + clientIP + " requested all interests");

        List<InterestDto> interests = interestBean.getAllInterests();
        return Response.ok(interests).build();
    }
}
