package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dto.user.RegisterUserDto;
import com.ilm.projecto_ilm_backend.dto.user.UserProfileDto;
import com.ilm.projecto_ilm_backend.validator.RegexValidator;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import com.ilm.projecto_ilm_backend.validator.UserProfileValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.InetAddress;
import java.net.UnknownHostException;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;

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
    public Response checkUsername(@HeaderParam("username") String username, @Context HttpHeaders headers) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to check if a username is already in the database from IP address: " + clientIP + " with username: " + username);

        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if(databaseValidator.checkAuxiliarToken(authHeader)){
            if (databaseValidator.checkUsername(username)) {
                return Response.status(Response.Status.CONFLICT).entity("Username already exists.").build();
            } else {
                return Response.status(Response.Status.OK).entity("Username available.").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }

    }


    /**
     * Checks if an email is already in the database.
     *
     * @param headers the email to be checked
     * @return HTTP response indicating the result of the email check
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    @GET
    @Path("/checkAuxiliarToken")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAuxiliarToken(@Context HttpHeaders headers) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to check if an auxiliar token exists in the database from IP address: " + clientIP);

        // Extrair o cabeçalho Authorization
        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if(databaseValidator.checkAuxiliarToken(authHeader)){
            logger.info("Auxiliar token exists.");
            return Response.status(Response.Status.OK).entity("Auxiliar token exists.").build();
        } else {
            logger.info("Auxiliar token does not exist.");
            return Response.status(Response.Status.NOT_FOUND).entity("Auxiliar token not found.").build();
        }
    }

    /**
     * Confirms the email of a user.
     *
     * @param auxiliarToken the auxiliar token of the user to be confirmed
     * @return HTTP response indicating the result of the email confirmation
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    @GET
    @Path("/confirmEmail/{auxiliarToken}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmEmail(@PathParam("auxiliarToken") String auxiliarToken) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to confirm the email of a user from IP address: " + clientIP);

        if(databaseValidator.checkAuxiliarToken(auxiliarToken)){
            if (userBean.confirmEmail(auxiliarToken)) {
                URI uri = URI.create("http://localhost:3000/create-profile/" + auxiliarToken);
                logger.info("Email confirmed.");
                return Response.seeOther(uri).build();
            } else {
                URI uri = URI.create("http://localhost:3000/");
                logger.error("Error confirming email.");
                return Response.seeOther(uri).build();
            }
        } else {
            logger.error("Auxiliar token not found.");
            return Response.status(Response.Status.NOT_FOUND).entity("Auxiliar token not found.").build();
        }
    }

    @POST
    @Path("/createProfile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProfile(@Context HttpHeaders headers, UserProfileDto userProfileDto) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to create a profile from IP address: " + clientIP);

        // Extract Authorization header
        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if(databaseValidator.checkAuxiliarToken(authHeader)){
            if (UserProfileValidator.validateUserProfileDto(userProfileDto)) {
                if (userBean.createProfile(userProfileDto, authHeader)) {
                    return Response.status(Response.Status.CREATED).entity("Profile created.").build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error creating profile.").build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid profile data.").build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }
    }

    @POST
    @Path("/uploadProfilePicture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadProfilePicture(){
        return Response.status(Response.Status.OK).build();
    }
}

