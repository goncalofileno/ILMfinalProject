package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.MailBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.mail.ContactDto;
import com.ilm.projecto_ilm_backend.dto.mail.MailDto;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling mail-related operations.
 */
@Path("/mail")
public class MailService {

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    MailBean mailBean;

    @Inject
    UserBean userBean;

    private static final Logger logger = LogManager.getLogger(MailService.class);

    /**
     * Retrieves received messages for the user identified by the session ID.
     *
     * @param sessionId The session ID of the user.
     * @param page The page number to retrieve.
     * @param pageSize The number of messages per page.
     * @param unread If true, only unread messages are retrieved.
     * @return A response containing the received messages and the total number of messages.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @GET
    @Path("/received")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReceivedMessages(@CookieParam("session-id") String sessionId,
                                        @QueryParam("page") @DefaultValue("1") int page,
                                        @QueryParam("pageSize") @DefaultValue("10") int pageSize,
                                        @QueryParam("unread") @DefaultValue("false") boolean unread) throws UnknownHostException {

        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to get received mails for session ID: " + sessionId + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            List<MailDto> mails = mailBean.getMailsReceivedBySessionId(sessionId, page, pageSize, unread);
            int totalMails = mailBean.getTotalMailsReceivedBySessionId(sessionId, unread);
            Map<String, Object> response = new HashMap<>();
            response.put("mails", mails);
            response.put("totalMails", totalMails);
            logger.info("Received mails fetched successfully for session ID: " + sessionId);
            return Response.ok(response).build();
        } else {
            logger.error("Invalid session ID: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     * Retrieves sent messages for the user identified by the session ID.
     *
     * @param sessionId The session ID of the user.
     * @param page The page number to retrieve.
     * @param pageSize The number of messages per page.
     * @return A response containing the sent messages and the total number of messages.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @GET
    @Path("/sent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSentMessages(@CookieParam("session-id") String sessionId,
                                    @QueryParam("page") @DefaultValue("1") int page,
                                    @QueryParam("pageSize") @DefaultValue("10") int pageSize) throws UnknownHostException {

        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();

        logger.info("Received a request to get sent mails for session ID: " + sessionId + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            List<MailDto> mails = mailBean.getMailsSentBySessionId(sessionId, page, pageSize);
            int totalMails = mailBean.getTotalMailsSentBySessionId(sessionId);
            Map<String, Object> response = new HashMap<>();
            response.put("mails", mails);
            response.put("totalMails", totalMails);
            logger.info("Sent mails fetched successfully for session ID: " + sessionId);
            return Response.ok(response).build();
        } else {
            logger.error("Invalid session ID: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     * Marks a specific mail as seen for the user identified by the session ID.
     *
     * @param sessionId The session ID of the user.
     * @param mailId The ID of the mail to be marked as seen.
     * @return A response indicating the result of the operation.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @PUT
    @Path("/seen/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response markMailAsSeen(@CookieParam("session-id") String sessionId, @PathParam("id") int mailId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to mark mail as seen for mail ID: " + mailId + " and session ID: " + sessionId + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            boolean updated = mailBean.markMailAsSeen(sessionId, mailId);
            if (updated) {
                logger.info("Mail marked as seen successfully for mail ID: " + mailId + " and session ID: " + sessionId);
                return Response.ok().entity(Collections.singletonMap("message", "Mail marked as seen successfully")).build();
            } else {
                logger.error("Mail not found or not authorized for mail ID: " + mailId + " and session ID: " + sessionId);
                return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("message", "Mail not found or not authorized")).build();
            }
        } else {
            logger.error("Invalid session ID: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    /**
     * Marks a specific mail as deleted for the user identified by the session ID.
     *
     * @param sessionId The session ID of the user.
     * @param mailId The ID of the mail to be marked as deleted.
     * @return A response indicating the result of the operation.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @PUT
    @Path("/deleted/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response markMailAsDeleted(@CookieParam("session-id") String sessionId, @PathParam("id") int mailId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to mark mail as deleted for mail ID: " + mailId + " and session ID: " + sessionId + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            boolean updated = mailBean.markMailAsDeleted(sessionId, mailId);
            if (updated) {
                logger.info("Mail marked as deleted successfully for mail ID: " + mailId + " and session ID: " + sessionId);
                return Response.ok().entity(Collections.singletonMap("message", "Mail marked as deleted successfully")).build();
            } else {
                logger.error("Mail not found or not authorized for mail ID: " + mailId + " and session ID: " + sessionId);
                return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("message", "Mail not found or not authorized")).build();
            }
        } else {
            logger.error("Invalid session ID: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    /**
     * Sends a mail for the user identified by the session ID.
     *
     * @param sessionId The session ID of the user.
     * @param mailDto The details of the mail to be sent.
     * @return A response indicating the result of the operation.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMail(@CookieParam("session-id") String sessionId, MailDto mailDto) throws UnknownHostException {

        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to send mail for session ID: " + sessionId + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            UserEntity sender = userBean.getUserBySessionId(sessionId);
            boolean sent = mailBean.sendMail(sender, mailDto);
            if (sent) {
                logger.info("Mail sent successfully for session ID: " + sessionId);
                return Response.ok().entity(Collections.singletonMap("message", "Mail sent successfully")).build();
            } else {
                logger.error("Failed to send mail for session ID: " + sessionId);
                return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("message", "Failed to send mail")).build();
            }
        } else {
            logger.error("Invalid session ID: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    /**
     * Retrieves the contacts for the user identified by the session ID.
     *
     * @param sessionId The session ID of the user.
     * @return A response containing the list of contacts.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @GET
    @Path("/contacts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContacts(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to get contacts for session ID: " + sessionId + " from " + clientName + " with IP: " + clientIP);
        if (databaseValidator.checkSessionId(sessionId)) {

            List<ContactDto> contacts = mailBean.getContactsBySessionId(sessionId);
            logger.info("Contacts fetched successfully for session ID: " + sessionId);
            return Response.ok(contacts).build();
        } else {
            logger.error("Invalid session ID: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    /**
     * Retrieves the number of unread messages for the user identified by the session ID.
     *
     * @param sessionId The session ID of the user.
     * @return A response containing the number of unread messages.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @GET
    @Path("/unreadNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnreadNumber(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to get unread number for session ID: " + sessionId + " from " + clientName + " with IP: " + clientIP);
        if (databaseValidator.checkSessionId(sessionId)) {
            int unreadNumber = mailBean.getUnreadNumber(sessionId);
            logger.info("Unread number fetched successfully for session ID: " + sessionId);
            return Response.ok(Collections.singletonMap("unreadNumber", unreadNumber)).build();
        } else {
            logger.error("Invalid session ID: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    /**
     * Searches for received mails based on a query for the user identified by the session ID.
     *
     * @param sessionId The session ID of the user.
     * @param query The search query.
     * @param page The page number to retrieve.
     * @param pageSize The number of messages per page.
     * @param unread If true, only unread messages are retrieved.
     * @return A response containing the search results and the total number of results.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMails(@CookieParam("session-id") String sessionId,
                                @QueryParam("query") String query,
                                @QueryParam("page") @DefaultValue("1") int page,
                                @QueryParam("pageSize") @DefaultValue("10") int pageSize,
                                @QueryParam("unread") @DefaultValue("false") boolean unread) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to search mails for session ID: " + sessionId + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            List<MailDto> mails = mailBean.searchMailsBySessionId(sessionId, query, page, pageSize, unread);
            int totalMails = mailBean.getTotalSearchResultsBySessionId(sessionId, query, unread);
            Map<String, Object> response = new HashMap<>();
            response.put("mails", mails);
            response.put("totalMails", totalMails);
            logger.info("Mails fetched successfully for session ID: " + sessionId);
            return Response.ok(response).build();
        } else {
            logger.error("Invalid session ID: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("message", "Invalid session ID")).build();
        }
    }

    /**
     * Searches for sent mails based on a query for the user identified by the session ID.
     *
     * @param sessionId The session ID of the user.
     * @param query The search query.
     * @param page The page number to retrieve.
     * @param pageSize The number of messages per page.
     * @return A response containing the search results and the total number of results.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @GET
    @Path("/searchSent")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchSentMails(@CookieParam("session-id") String sessionId,
                                    @QueryParam("query") String query,
                                    @QueryParam("page") @DefaultValue("1") int page,
                                    @QueryParam("pageSize") @DefaultValue("10") int pageSize) throws UnknownHostException {

        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to search sent mails for session ID: " + sessionId + " from " + clientName + " with IP: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            List<MailDto> mails = mailBean.searchSentMailsBySessionId(sessionId, query, page, pageSize);
            int totalMails = mailBean.getTotalSentSearchResultsBySessionId(sessionId, query);
            Map<String, Object> response = new HashMap<>();
            response.put("mails", mails);
            response.put("totalMails", totalMails);
            logger.info("Sent mails fetched successfully for session ID: " + sessionId);
            return Response.ok(response).build();
        } else {
            logger.error("Invalid session ID: " + sessionId);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
