package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.NotificationBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.notification.NotificationResponseDto;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.dto.notification.NotificationDto;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Service class for handling notification-related operations.
 */
@Path("/notification")
public class NotificationService {

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    NotificationBean notificationBean;

    @Inject
    SessionDao sessionDao;

    @Inject
    UserBean userBean;

    private static final Logger logger = LogManager.getLogger(NotificationService.class);

    /**
     * Retrieves user notifications.
     *
     * @param sessionId The session ID of the user.
     * @param page      The page number to retrieve.
     * @return A response containing the user notifications and the total number of notifications.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @GET
    @Path("/userNotifications")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserNotifications(@CookieParam("session-id") String sessionId, @QueryParam("page") @DefaultValue("1") int page) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to get user notifications from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = sessionDao.findUserIdBySessionId(sessionId);
                List<NotificationDto> notifications = notificationBean.getUserNotifications(userId, page);
                int totalNotifications = notificationBean.getTotalUserNotifications(userId);
                NotificationResponseDto response = new NotificationResponseDto(notifications, totalNotifications);
                logger.info("User notifications retrieved successfully from IP address: " + clientIP);
                return Response.ok(response).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving user notifications: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving user notifications").build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    /**
     * Retrieves the unread notification count for the user.
     *
     * @param sessionId The session ID of the user.
     * @return A response containing the unread notification count.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @GET
    @Path("/unreadCount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnreadNotificationCount(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to get unread notification count from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = sessionDao.findUserIdBySessionId(sessionId);
                int unreadCount = notificationBean.getUnreadNotificationCount(userId);
                logger.info("Unread notification count retrieved successfully from IP address: " + clientIP + " with count: " + unreadCount);
                return Response.ok(unreadCount).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving unread notification count: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving unread notification count").build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    /**
     * Marks message notifications as clicked.
     *
     * @param sessionId       The session ID of the user.
     * @param notificationIds The list of notification IDs to be marked as clicked.
     * @return A response indicating the result of the operation.
     * @throws UnknownHostException If the client's IP address cannot be determined.
     */
    @PUT
    @Path("/markMessageNotificationClicked")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response markMessageNotificationClicked(@CookieParam("session-id") String sessionId, List<Integer> notificationIds) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String clientName = userBean.getUserBySessionId(sessionId).getFullName();
        logger.info("Received a request to mark message notification clicked from IP address: " + clientIP + " and name: " + clientName);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = sessionDao.findUserIdBySessionId(sessionId);
                logger.info("Marking message notification clicked from IP address: " + clientIP + " with userId: " + userId + " and notificationIds: " + notificationIds);
                notificationBean.markMessageNotificationClicked(userId, notificationIds);
                logger.info("Message notification clicked marked successfully from IP address: " + clientIP);
                return Response.ok().build();
            } catch (Exception e) {
                logger.error("An error occurred while marking message notification clicked: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while marking message notification clicked").build();
            }
        } else {
            logger.error("Unauthorized access from IP address: " + clientIP);
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }
}
