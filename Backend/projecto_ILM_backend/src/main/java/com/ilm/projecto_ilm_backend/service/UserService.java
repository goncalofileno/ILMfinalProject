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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;


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
        if (databaseValidator.checkAuxiliarToken(authHeader)) {
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
        if (databaseValidator.checkAuxiliarToken(authHeader)) {
            logger.info("Auxiliar token exists.");
            return Response.status(Response.Status.OK).entity("Auxiliar token exists.").build();
        } else {
            logger.info("Auxiliar token does not exist.");
            return Response.status(Response.Status.NOT_FOUND).entity("Auxiliar token not found.").build();
        }
    }


    @GET
    @Path("/checkEmail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkEmail(@HeaderParam("email") String email) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to check if an email is already in the database and if it is valid, from IP address: " + clientIP);
        // Check if email already exists in the database
        if (!databaseValidator.checkEmail(email)) {
            if (RegexValidator.validateEmail(email)) return Response.status(Response.Status.OK).build();
            else return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
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

        if (databaseValidator.checkAuxiliarToken(auxiliarToken)) {
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
        if (databaseValidator.checkAuxiliarToken(authHeader)) {
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

    /**
     * Uploads a profile picture for a user.
     *
     * @param request the request containing the image file
     * @return HTTP response indicating the result of the profile picture upload
     */
    @POST
    @Path("/uploadProfilePicture")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadProfilePicture(Map<String, String> request, @Context HttpHeaders headers) {
        logger.info("Received a request to upload a profile picture.");
        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (databaseValidator.checkAuxiliarToken(authHeader)) {
            try {
                String base64Image = request.get("file");
                if (base64Image == null || base64Image.isEmpty()) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("File is missing").build();
                }
                // Remove "data:image/png;base64," or similar prefix from the string
                if (base64Image.contains(",")) {
                    base64Image = base64Image.split(",")[1];
                }
                // Delegate to UserBean to handle the file saving and updating the user's photo path
                boolean isSaved = userBean.saveUserProfilePicture(authHeader, base64Image);
                if (isSaved) {
                    logger.info("Profile picture uploaded successfully.");
                    return Response.ok("Profile picture uploaded successfully.").build();
                } else {
                    logger.error("Failed to save profile picture");
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to save profile picture").build();
                }
            } catch (Exception e) {
                logger.error("Error during file upload: " + e.getMessage(), e);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error during file upload: " + e.getMessage()).build();
            }
        } else {
            logger.error("Unauthorized request to upload profile picture.");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }
    }


    /**
     * Logs in a user.
     *
     * @param registerUserDto the DTO containing user login details
     * @return HTTP response containing the token of the logged in user
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(RegisterUserDto registerUserDto) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to login from IP address: " + clientIP + " with email: " + registerUserDto.getMail());
        if (databaseValidator.checkEmail(registerUserDto.getMail())) {
            String token = userBean.loginUser(registerUserDto);
            if (token != null) {
                return Response.status(Response.Status.OK).entity(token).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }

    @POST
    @Path("/forgetPassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response forgetPassword(@HeaderParam("email")String email) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
       logger.info("Received a request to send the forget password link from IP address: " + clientIP + " with email: " + email);
        if (databaseValidator.checkEmail(email)) {
            if(userBean.sendForgetPassLink(email)){
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @GET
    @Path("/resetPasswordCheck/{auxiliarToken}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPasswordCheck(@PathParam("auxiliarToken") String auxiliarToken) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to reset password of a user from IP address: " + clientIP);

        if (databaseValidator.checkAuxiliarToken(auxiliarToken)) {
            URI uri = URI.create("http://localhost:3000/reset-password/" + auxiliarToken);
            logger.info("Auxiliar token to reset password was confirmed.");
            return Response.seeOther(uri).build();


        } else {
            logger.error("Auxiliar token not found.");
            URI uri = URI.create("http://localhost:3000/");
            return Response.seeOther(uri).build();
        }
    }

    @POST
    @Path("/resetPassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(@Context HttpHeaders headers, @HeaderParam("newPassword") String newPassword) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String auxiliarToken = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        logger.info("Received a request to reset the password from IP address: " + clientIP);
        if (databaseValidator.checkAuxiliarToken(auxiliarToken)) {
            if (RegexValidator.validatePassword(newPassword)) {
                if (userBean.resetPassword(auxiliarToken, newPassword)) {
                    return Response.status(Response.Status.OK).build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}

