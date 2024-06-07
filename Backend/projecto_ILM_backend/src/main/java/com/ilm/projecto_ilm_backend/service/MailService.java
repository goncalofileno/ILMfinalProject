package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.MailBean;
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

@Path("/message")
public class MailService {

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    MailBean mailBean;

    private static final Logger logger = LogManager.getLogger(MailService.class);

    @GET
    @Path("/received")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReceivedMessages(@CookieParam("session-id") String sessionId) {

        logger.info("Received a request to get received mails for session ID: " + sessionId);

        if(databaseValidator.checkSessionId(sessionId)) {
            return Response.ok(mailBean.getMailsReceivedBySessionId(sessionId)).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

}
