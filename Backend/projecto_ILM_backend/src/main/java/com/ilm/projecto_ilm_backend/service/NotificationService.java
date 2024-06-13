package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.NotificationBean;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.dto.notification.NotificationDto;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Path("/notification")
public class NotificationService {

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    NotificationBean notificationBean;

    @Inject
    SessionDao sessionDao;

    private static final Logger logger = LogManager.getLogger(NotificationService.class);

    @GET
    @Path("/userNotifications")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserNotifications(@CookieParam("session-id") String sessionId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to get user notifications from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = sessionDao.findUserIdBySessionId(sessionId);
                List<NotificationDto> notifications = notificationBean.getUserNotifications(userId);
                return Response.ok(notifications).build();
            } catch (Exception e) {
                logger.error("An error occurred while retrieving user notifications: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while retrieving user notifications").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }

    @POST
    @Path("/markAsRead")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response markNotificationAsRead(@CookieParam("session-id") String sessionId, Integer notificationId) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to mark notification as read from IP address: " + clientIP);

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                int userId = sessionDao.findUserIdBySessionId(sessionId);
                notificationBean.markNotificationAsRead(userId, notificationId);
                return Response.ok("Notification marked as read").build();
            } catch (Exception e) {
                logger.error("An error occurred while marking notification as read: " + e.getMessage() + " from IP address: " + clientIP);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred while marking notification as read").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized access").build();
        }
    }
}
