package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.SkillBean;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * REST service for skill-related operations.
 */
@Path("/skill")
public class SkillService {

    /**
     * Injected SkillBean EJB
     */
    @Inject
    SkillBean skillBean;

    @Inject
    DatabaseValidator databaseValidator;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    /**
     * Retrieves all skills.
     *
     * @return HTTP response containing a list of all skills
     */
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSkills(@Context HttpHeaders headers) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to retrieve all skills from IP address: " + clientIP);

        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (databaseValidator.checkAuxiliarToken(authHeader)) {
            List<SkillDto> skills = skillBean.getAllSkills();
            return Response.ok(skills).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();

        }
    }
}
