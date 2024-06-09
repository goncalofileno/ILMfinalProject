package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.MailBean;
import com.ilm.projecto_ilm_backend.dto.mail.ContactDto;
import com.ilm.projecto_ilm_backend.dto.mail.MailDto;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/mail")
public class MailService {

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    MailBean mailBean;

    private static final Logger logger = LogManager.getLogger(MailService.class);

    @GET
    @Path("/received")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReceivedMessages(@CookieParam("session-id") String sessionId,
                                        @QueryParam("page") @DefaultValue("1") int page,
                                        @QueryParam("pageSize") @DefaultValue("10") int pageSize) {

        logger.info("Received a request to get received mails for session ID: " + sessionId);

        if (databaseValidator.checkSessionId(sessionId)) {
            List<MailDto> mails = mailBean.getMailsReceivedBySessionId(sessionId, page, pageSize);
            int totalMails = mailBean.getTotalMailsReceivedBySessionId(sessionId);
            Map<String, Object> response = new HashMap<>();
            response.put("mails", mails);
            response.put("totalMails", totalMails);
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
    @Path("/sent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSentMessages(@CookieParam("session-id") String sessionId,
                                    @QueryParam("page") @DefaultValue("1") int page,
                                    @QueryParam("pageSize") @DefaultValue("10") int pageSize) {

        logger.info("Received a request to get sent mails for session ID: " + sessionId);

        if (databaseValidator.checkSessionId(sessionId)) {
            List<MailDto> mails = mailBean.getMailsSentBySessionId(sessionId, page, pageSize);
            int totalMails = mailBean.getTotalMailsSentBySessionId(sessionId);
            Map<String, Object> response = new HashMap<>();
            response.put("mails", mails);
            response.put("totalMails", totalMails);
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


    @PUT
    @Path("/seen/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response markMailAsSeen(@CookieParam("session-id") String sessionId, @PathParam("id") int mailId) {
        logger.info("Received a request to mark mail as seen for mail ID: " + mailId + " and session ID: " + sessionId);

        if (databaseValidator.checkSessionId(sessionId)) {
            boolean updated = mailBean.markMailAsSeen(sessionId, mailId);
            if (updated) {
                return Response.ok().entity(Collections.singletonMap("message", "Mail marked as seen successfully")).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("message", "Mail not found or not authorized")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    @PUT
    @Path("/deleted/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response markMailAsDeleted(@CookieParam("session-id") String sessionId, @PathParam("id") int mailId) {
        logger.info("Received a request to mark mail as deleted for mail ID: " + mailId + " and session ID: " + sessionId);

        if (databaseValidator.checkSessionId(sessionId)) {
            boolean updated = mailBean.markMailAsDeleted(sessionId, mailId);
            if (updated) {
                return Response.ok().entity(Collections.singletonMap("message", "Mail marked as deleted successfully")).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("message", "Mail not found or not authorized")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMail(@CookieParam("session-id") String sessionId, MailDto mailDto) {

        logger.info("Received a request to send a mail from session ID: " + sessionId);

        logger.info("Mail DTO: " + mailDto.toString());

        if (databaseValidator.checkSessionId(sessionId)) {
            boolean sent = mailBean.sendMail(sessionId, mailDto);
            if (sent) {
                return Response.ok().entity(Collections.singletonMap("message", "Mail sent successfully")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("message", "Failed to send mail")).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    @GET
    @Path("/contacts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContacts(@CookieParam("session-id") String sessionId) {
        logger.info("Received a request to get contacts for session ID: " + sessionId);
        if (databaseValidator.checkSessionId(sessionId)) {
            List<ContactDto> contacts = mailBean.getContactsBySessionId(sessionId);
            return Response.ok(contacts).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    @GET
    @Path("/unreadNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnreadNumber(@CookieParam("session-id") String sessionId) {
        logger.info("Received a request to get the number of unread mails for session ID: " + sessionId);
        if (databaseValidator.checkSessionId(sessionId)) {
            int unreadNumber = mailBean.getUnreadNumber(sessionId);
            return Response.ok(Collections.singletonMap("unreadNumber", unreadNumber)).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMails(@CookieParam("session-id") String sessionId,
                                @QueryParam("query") String query,
                                @QueryParam("page") @DefaultValue("1") int page,
                                @QueryParam("pageSize") @DefaultValue("10") int pageSize) {
        logger.info("Received a request to search mails for session ID: " + sessionId);

        if (databaseValidator.checkSessionId(sessionId)) {
            List<MailDto> mails = mailBean.searchMailsBySessionId(sessionId, query, page, pageSize);
            int totalMails = mailBean.getTotalSearchResultsBySessionId(sessionId, query);
            Map<String, Object> response = new HashMap<>();
            response.put("mails", mails);
            response.put("totalMails", totalMails);
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    @GET
    @Path("/searchSent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchSentMails(@CookieParam("session-id") String sessionId,
                                    @QueryParam("query") String query,
                                    @QueryParam("page") @DefaultValue("1") int page,
                                    @QueryParam("pageSize") @DefaultValue("10") int pageSize) {

        logger.info("Received a request to search sent mails for session ID: " + sessionId);

        if (databaseValidator.checkSessionId(sessionId)) {
            List<MailDto> mails = mailBean.searchSentMailsBySessionId(sessionId, query, page, pageSize);
            int totalMails = mailBean.getTotalSentSearchResultsBySessionId(sessionId, query);
            Map<String, Object> response = new HashMap<>();
            response.put("mails", mails);
            response.put("totalMails", totalMails);
            return Response.ok(response).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


}
