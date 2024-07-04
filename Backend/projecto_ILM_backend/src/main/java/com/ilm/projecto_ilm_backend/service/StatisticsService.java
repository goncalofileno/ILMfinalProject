package com.ilm.projecto_ilm_backend.service;


import com.ilm.projecto_ilm_backend.bean.StatisticsBean;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import com.ilm.projecto_ilm_backend.dto.statistics.ProjectsStatusNumberPerLab;
import com.ilm.projecto_ilm_backend.dto.statistics.StatisticsDto;
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
import java.util.List;

@Path("/statistics")
public class StatisticsService {

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    StatisticsBean statisticsBean;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSkills(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to retrieve the statistics from IP address: " + clientIP);
        if(databaseValidator.checkSessionId(sessionId)) {
            return Response.ok(statisticsBean.getStatistics()).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }
}
