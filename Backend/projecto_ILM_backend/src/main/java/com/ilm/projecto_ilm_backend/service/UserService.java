package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.user.RegisterUserDto;
import com.ilm.projecto_ilm_backend.validator.RegexValidator;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.mapping.AuxiliaryDatabaseObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * REST service for user-related operations.
 */
@Path("/user")
public class UserService {

    @Inject
    UserBean userBean;

    @Inject
    DatabaseValidator databaseValidator;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    /**
     * Registers a new user.
     *
     * @param registerUserDto the DTO containing user registration details
     * @return HTTP response indicating the result of the registration process
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(RegisterUserDto registerUserDto) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to register a user from IP address: " + clientIP + " with email: " + registerUserDto.getMail());
        // Validate email format
        if (RegexValidator.validateEmail(registerUserDto.getMail())) {
            // Validate password format
            if (RegexValidator.validatePassword(registerUserDto.getPassword())) {
                // Check if email already exists in the database
                if (!databaseValidator.checkEmail(registerUserDto.getMail())) {
                    // Register user
                    if (userBean.registerUser(registerUserDto)) {
                        return Response.status(Response.Status.CREATED).build();
                    } else {
                        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                    }
                } else {
                    return Response.status(Response.Status.CONFLICT).build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Checks if a username is already in the database.
     *
     * @param username the username to be checked
     * @return HTTP response indicating the result of the username check
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    @POST
    @Path("/checkUsername")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkUsername(@HeaderParam("username") String username) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to check if a username is already in the database from IP address: " + clientIP + " with username: " + username);
        // Check if username already exists in the database
        if (databaseValidator.checkUsername(username)) {
            return Response.status(Response.Status.CONFLICT).build();
        } else {
            return Response.status(Response.Status.OK).build();
        }
    }

    @GET
    @Path("/checkAuxiliarToken")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAuxiliarToken(@HeaderParam("auxiliarToken") String auxiliarToken) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to check if an auxiliar token exist in the database from IP address: " + clientIP);
        // Check if username already exists in the database
        if (databaseValidator.checkAuxiliarToken(auxiliarToken)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @GET
    @Path("/checkEmail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkEmail(@HeaderParam("email") String email) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to check if an email is already in the database and if it is valid, from IP address: " + clientIP);
        // Check if username already exists in the database
        if (!databaseValidator.checkEmail(email)) {
            if(RegexValidator.validateEmail(email)) return Response.status(Response.Status.OK).build();
            else return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
}
