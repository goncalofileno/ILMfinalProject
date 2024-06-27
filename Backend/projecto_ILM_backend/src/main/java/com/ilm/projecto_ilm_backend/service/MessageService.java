package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.MessageBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.messages.MessageDto;
import com.ilm.projecto_ilm_backend.dto.messages.MessagesPageDto;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.security.exceptions.ProjectNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotInProjectException;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/message")
public class MessageService {

    @Inject
    MessageBean messageBean;

    @Inject
    DatabaseValidator databaseValidator;

    @Inject
    UserBean userBean;

    private static final Logger logger = LogManager.getLogger(MessageService.class);

    @GET
    @Path("/chatPage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getChatPage(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName) {
        logger.info("GET /message/chatPage");

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                UserEntity user = userBean.getUserBySessionId(sessionId);
                MessagesPageDto chatPage = messageBean.getChatPage(user, projectSystemName);
                return Response.ok(chatPage).build();
            } catch (ProjectNotFoundException | UserNotInProjectException e) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(e.getMessage())
                        .build();
            } catch (Exception e) {
                logger.error("Error fetching chat page", e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An unexpected error occurred.")
                        .build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid session.")
                    .build();
        }
    }

    //Serviço para enviar mensagem, recebe um sessionId, o systemName do projeto e a mensagemDto
    @POST
    @Path("/sendMessage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMessage(@CookieParam("session-id") String sessionId, @QueryParam("projectSystemName") String projectSystemName, MessageDto messageDto) {
        logger.info("POST /message/sendMessage");

        if (databaseValidator.checkSessionId(sessionId)) {
            try {
                UserEntity user = userBean.getUserBySessionId(sessionId);
                messageBean.sendMessage(user, projectSystemName, messageDto);
                return Response.ok().build();
            } catch (ProjectNotFoundException | UserNotInProjectException e) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(e.getMessage())
                        .build();
            } catch (Exception e) {
                logger.error("Error sending message", e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("An unexpected error occurred.")
                        .build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid session.")
                    .build();
        }
    }





}
