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

import java.util.List;

@Path("/interest")
public class InterestService {

    @Inject
    InterestBean interestBean;

    @Inject
    DatabaseValidator databaseValidator;

    private static final Logger logger = LogManager.getLogger(UserService.class);


    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllInterests(@Context HttpHeaders headers) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to retrieve all interests from IP address: " + clientIP);

        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (databaseValidator.checkAuxiliarToken(authHeader)) {
            List<InterestDto> interests = interestBean.getAllInterests();
            return Response.ok(interests).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();

        }
    }
}
