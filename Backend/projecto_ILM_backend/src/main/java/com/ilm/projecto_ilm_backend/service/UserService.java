package com.ilm.projecto_ilm_backend.service;

import com.ilm.projecto_ilm_backend.ENUMS.LanguageENUM;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dao.SessionDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dto.user.*;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.security.exceptions.UnauthorizedException;
import com.ilm.projecto_ilm_backend.validator.RegexValidator;
import com.ilm.projecto_ilm_backend.validator.DatabaseValidator;
import com.ilm.projecto_ilm_backend.validator.UserProfileValidator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
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

    @Inject
    UserDao userDao;

    @Inject
    SessionDao sessionDao;

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

        if (databaseValidator.checkUsername(username)) {
            return Response.status(Response.Status.CONFLICT).entity("Username already exists.").build();
        } else {
            return Response.status(Response.Status.OK).entity("Username available.").build();
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


    /**
     * Endpoint to handle email validation.
     * This method receives an email as a header parameter, checks if the email exists in the database,
     * and if it does, returns a CONFLICT (409) status.
     * If the email does not exist in the database, it validates the email format,
     * and if it is valid, returns an OK (200) status, otherwise returns a BAD_REQUEST (400) status.
     *
     * @param email The email address to be validated.
     * @return A Response indicating the result of the operation.
     * - OK (200) if the email does not exist in the database and is valid.
     * - BAD_REQUEST (400) if the email does not exist in the database but is not valid.
     * - CONFLICT (409) if the email already exists in the database.
     * @throws UnknownHostException If the IP address of the local host could not be determined.
     */
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


    /**
     * Endpoint to handle profile creation.
     * This method receives a UserProfileDto object and an authorization token as header parameters.
     * It checks if the authorization token exists in the database and if the UserProfileDto object is valid.
     * If both checks pass, it creates a profile for the user associated with the authorization token.
     *
     * @param headers        The HttpHeaders object from which the authorization token is extracted.
     * @param userProfileDto The UserProfileDto object containing the user's profile data.
     * @return A Response indicating the result of the operation.
     * - CREATED (201) if the profile was created successfully.
     * - INTERNAL_SERVER_ERROR (500) if there was an error creating the profile.
     * - BAD_REQUEST (400) if the UserProfileDto object is not valid.
     * - UNAUTHORIZED (401) if the authorization token does not exist in the database.
     * @throws UnknownHostException If the IP address of the local host could not be determined.
     */
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
    public Response uploadProfilePicture(Map<String, String> request, @Context HttpHeaders headers, @CookieParam("session-id") String sessionId) {
        logger.info("Received a request to upload a profile picture.");
        String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && databaseValidator.checkAuxiliarToken(authHeader)) {
            logger.info("Authorized request to upload profile picture.");
            return handleUploadProfilePicture(request, authHeader);
        } else if (sessionId != null) {
            logger.info("Authorized request to upload profile picture with session id.");
            return handleUploadProfilePicture(request, sessionId);
        } else {
            logger.error("Unauthorized request to upload profile picture.");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
        }
    }

    private Response handleUploadProfilePicture(Map<String, String> request, String auth) {
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
            boolean isSaved = userBean.saveUserProfilePicture(auth, base64Image);
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
    }


    /**
     * Logs in a user.
     *
     * @param registerUserDto the DTO containing user login details
     * @return HTTP response indicating the result of the login process
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(RegisterUserDto registerUserDto) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        String userAgent = registerUserDto.getUserAgent();  // Certifique-se de que o DTO contém userAgent

        logger.info("Received a request to login from IP address: " + clientIP + " with email: " + registerUserDto.getMail() + " and user agent: " + userAgent);

        if (databaseValidator.checkEmail(registerUserDto.getMail())) {
            UserEntity userEntity = userDao.findByEmail(registerUserDto.getMail());
            if (userEntity != null && !userEntity.isProfileCreated()) {
                return Response.status(Response.Status.OK).entity(Collections.singletonMap("auxiliarToken", userEntity.getAuxiliarToken())).build();
            } else {
                String sessionId = userBean.loginUser(registerUserDto, clientIP, userAgent);
                if (sessionId != null) {
                    NewCookie sessionCookie = new NewCookie("session-id", sessionId, "/", null, null, NewCookie.DEFAULT_MAX_AGE, false, false);
                    NewCookie systemUsernameCookie = new NewCookie("user-systemUsername", userEntity.getSystemUsername(), "/", null, null, NewCookie.DEFAULT_MAX_AGE, false, false);
                    NewCookie userTypeCookie = new NewCookie("user-userType", String.valueOf(userEntity.getType()), "/", null, null, NewCookie.DEFAULT_MAX_AGE, false, false);
                    NewCookie languageCookie = new NewCookie("user-language", String.valueOf(userEntity.getLanguage()), "/", null, null, NewCookie.DEFAULT_MAX_AGE, false, false);

                    Map<String, Object> responseBody = new HashMap<>();
                    if(userBean.userHasProjects(sessionId)){
                        responseBody.put("hasProjects", true);
                    } else {
                        responseBody.put("hasProjects", false);
                    }

                    return Response.status(Response.Status.OK)
                            .cookie(sessionCookie)
                            .cookie(systemUsernameCookie)
                            .cookie(userTypeCookie)
                            .cookie(languageCookie)
                            .entity(responseBody)
                            .build();
                } else {
                    return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("error", "Unauthorized")).build();
                }
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("error", "Email not found")).build();
        }
    }

    @POST
    @Path("/loginWithToken")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginWithToken(LoginWithTokenDto loginWithTokenDto) throws UnknownHostException {
        String token = loginWithTokenDto.getToken();
        String userAgent = loginWithTokenDto.getUserAgent();
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        UserEntity userEntity = userDao.findByAuxiliarToken(token);

        if (userEntity != null) {
            logger.info("User found for token: " + token);
            String sessionId = userBean.createSessionForUser(userEntity, clientIP, userAgent);

            if (sessionId != null) {
                NewCookie sessionCookie = new NewCookie("session-id", sessionId, "/", null, null, NewCookie.DEFAULT_MAX_AGE, false, false);
                NewCookie systemUsernameCookie = new NewCookie("user-systemUsername", userEntity.getSystemUsername(), "/", null, null, NewCookie.DEFAULT_MAX_AGE, false, false);
                NewCookie userTypeCookie = new NewCookie("user-userType", String.valueOf(userEntity.getType()), "/", null, null, NewCookie.DEFAULT_MAX_AGE, false, false);

                return Response.status(Response.Status.OK)
                        .cookie(sessionCookie)
                        .cookie(systemUsernameCookie)
                        .cookie(userTypeCookie)
                        .entity(Collections.singletonMap("message", "Login successful"))
                        .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity(Collections.singletonMap("error", "Unauthorized")).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(Collections.singletonMap("error", "Token not found")).build();
        }
    }

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@CookieParam("session-id") Cookie sessionIdCookie) throws UnknownHostException {
        logger.info("Received a request to logout a user.");
        if (sessionIdCookie == null) {
            logger.error("Session ID cookie not found.");
            return Response.status(Response.Status.BAD_REQUEST).entity("Session ID cookie not found").build();
        }

        String sessionId = sessionIdCookie.getValue();
        boolean sessionRemoved = sessionDao.removeSessionById(sessionId);

        if (sessionRemoved) {
            logger.info("Session removed successfully.");
            NewCookie expiredSessionCookie = new NewCookie("session-id", null, "/", null, null, 0, false, true);
            NewCookie expiredSystemUsernameCookie = new NewCookie("user-systemUsername", null, "/", null, null, 0, false, true);
            NewCookie expiredUserTypeCookie = new NewCookie("user-userType", null, "/", null, null, 0, false, true);

            return Response.ok()
                    .cookie(expiredSessionCookie)
                    .cookie(expiredSystemUsernameCookie)
                    .cookie(expiredUserTypeCookie)
                    .entity(Collections.singletonMap("message", "Logout successful"))
                    .build();
        } else {
            logger.error("Error removing session.");
            return Response.status(Response.Status.NOT_FOUND).entity("Session not found").build();
        }
    }


    /**
     * Gets the profile image of a user.
     *
     * @param sessionId the session ID from the cookie
     * @return HTTP response containing the URL of the profile image
     */
    @GET
    @Path("/profileImage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfileImage(@CookieParam("session-id") String sessionId) {
        // Verifique se o sessionId é válido e corresponde a um usuário logado
        logger.info("Received a request to get the profile image of a user with session ID: " + sessionId);
        UserEntity user = userBean.getUserBySessionId(sessionId);
        if (user != null) {
            // Retorna a URL da imagem do perfil diretamente da entidade do usuário
            String imageUrl = user.getAvatarPhoto(); // ou qualquer campo que você esteja usando para armazenar a URL da foto
            return Response.status(Response.Status.OK).entity(Collections.singletonMap("imageUrl", imageUrl)).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }


    /**
     * Endpoint to handle password reset requests.
     * This method receives an email as a header parameter, checks if the email exists in the database,
     * and if it does, sends a password reset link to the email.
     *
     * @param email The email address of the user who wants to reset their password.
     * @return A Response indicating the result of the operation.
     * - OK (200) if the password reset link was sent successfully.
     * - INTERNAL_SERVER_ERROR (500) if there was an error sending the password reset link.
     * - NOT_FOUND (404) if the email does not exist in the database.
     * @throws UnknownHostException If the IP address of the local host could not be determined.
     */
    @POST
    @Path("/forgetPassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response forgetPassword(@HeaderParam("email") String email) throws UnknownHostException {
        String clientIP = InetAddress.getLocalHost().getHostAddress();
        logger.info("Received a request to send the forget password link from IP address: " + clientIP + " with email: " + email);
        if (databaseValidator.checkEmail(email)) {
            if (userBean.sendForgetPassLink(email)) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    /**
     * Endpoint to handle password reset checks.
     * This method receives an auxiliar token as a path parameter, checks if the token exists in the database,
     * and if it does, redirects to the password reset page with the token as a parameter.
     * If the token does not exist, it redirects to the home page.
     *
     * @param auxiliarToken The auxiliar token of the user who wants to reset their password.
     * @return A Response with a redirection to the appropriate page.
     * - If the token exists, it redirects to the password reset page with the token as a parameter.
     * - If the token does not exist, it redirects to the home page.
     * @throws UnknownHostException If the IP address of the local host could not be determined.
     */
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

    /**
     * Endpoint to handle password reset.
     * This method receives a new password and an auxiliar token as header parameters.
     * It checks if the auxiliar token exists in the database and if the new password is valid.
     * If both checks pass, it resets the password of the user associated with the auxiliar token.
     *
     * @param headers     The HttpHeaders object from which the auxiliar token is extracted.
     * @param newPassword The new password of the user.
     * @return A Response indicating the result of the operation.
     * - OK (200) if the password was reset successfully.
     * - INTERNAL_SERVER_ERROR (500) if there was an error resetting the password.
     * - BAD_REQUEST (400) if the new password is not valid.
     * - NOT_FOUND (404) if the auxiliar token does not exist in the database.
     * @throws UnknownHostException If the IP address of the local host could not be determined.
     */
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

    @GET
    @Path("/profile/{systemUsername}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getProfile(@CookieParam("session-id") String sessionId, @PathParam("systemUsername") String systemUsername) {
        System.out.println("SESSION ID: " + sessionId);
        System.out.println("SYSTEM USERNAME: " + systemUsername);
        try {
            ShowProfileDto profileDto = userBean.getProfile(systemUsername, sessionId);
            return Response.status(Response.Status.OK).entity(profileDto).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("message", e.getMessage())).build();
        } catch (UnauthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Collections.singletonMap("message", e.getMessage())).build();
        } catch (Exception e) {
            logger.error("Error fetching profile: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Collections.singletonMap("message", "Internal Server Error")).build();
        }
    }


    @GET
    @Path("/editProfile/{systemUsername}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEditProfile(@PathParam("systemUsername") String systemUsername, @CookieParam("session-id") String sessionId) {
        try {
            UserProfileDto profileDto = userBean.getEditProfile(systemUsername, sessionId);
            return Response.status(Response.Status.OK).entity(profileDto).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("message", e.getMessage())).build();
        } catch (UnauthorizedException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Collections.singletonMap("message", e.getMessage())).build();
        } catch (Exception e) {
            logger.error("Error fetching profile: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Collections.singletonMap("message", "Internal Server Error")).build();
        }
    }

    @POST
    @Path("/updateProfile")
    public Response updateProfile(UserProfileDto userProfileDto, @CookieParam("session-id") String sessionId) {
        try {
            logger.info("Received a request to update the profile of a user with session ID: " + sessionId);
            userBean.updateUserProfile(userProfileDto, sessionId);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            logger.error("Error updating profile: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("message", e.getMessage())).build();
        }
    }

    @POST
    @Path("/updatePassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePassword(
            @CookieParam("session-id") String sessionId,
            Map<String, String> passwords
    ) {
        String currentPassword = passwords.get("currentPassword");
        String newPassword = passwords.get("newPassword");

        logger.info("Received a request to update the password of a user with session ID: " + sessionId);

        UserEntity user = userBean.getUserBySessionId(sessionId);
        if (user == null) {
            // Session ID is not valid
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Collections.singletonMap("message", "Invalid session ID"))
                    .build();
        }

        if (!userBean.validatePassword(user, currentPassword)) {
            // Current password is incorrect
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(Collections.singletonMap("message", "Incorrect current password"))
                    .build();
        }

        // Update the password
        userBean.updatePassword(user, newPassword);
        return Response.status(Response.Status.OK)
                .entity(Collections.singletonMap("message", "Password updated successfully"))
                .build();
    }

    @POST
    @Path("/userProjectCreation")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUserProjectCreationInfo(@CookieParam("session-id") String sessionId, @QueryParam("systemProjectName") String systemProjectName, RejectedIdsDto rejectedUsersDto, @QueryParam("page") int page, @QueryParam("lab") String lab, @QueryParam("keyword") String keyword) {
        try {
            logger.info("Received a request to get the users information from a user with session ID: " + sessionId);

            if (databaseValidator.checkSessionId(sessionId)) {
                return Response.status(Response.Status.OK).entity(userBean.getUserProjectCreationInfoDto(sessionId, systemProjectName, rejectedUsersDto, page, lab, keyword)).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            logger.error("Error updating profile: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("message", e.getMessage())).build();
        }
    }

    @GET
    @Path("/validateSession")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateSession(@CookieParam("session-id") String sessionId) {
        if (databaseValidator.checkSessionId(sessionId)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    //Service that updates the user's to other language
    @PATCH
    @Path("/updateLanguage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLanguage(@CookieParam("session-id") String sessionId, @QueryParam("language") LanguageENUM language) {
        System.out.println("SESSION ID: " + sessionId);
        try {
            logger.info("Received a request to get the users information from a user with session ID: " + sessionId);
            if (databaseValidator.checkSessionId(sessionId)) {
                if (userBean.updateLanguage(sessionId, language)) {
                    NewCookie languageCookie = new NewCookie("user-language", String.valueOf(language), "/", null, null, NewCookie.DEFAULT_MAX_AGE, false, false);
                    return Response.status(Response.Status.OK).cookie(languageCookie).build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (
                Exception e) {
            logger.error("Error updating language: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("message", e.getMessage())).build();
        }
    }

    @PATCH
    @Path("/promoteToAdmin/{systemUsername}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response promoteUserToAdmin(@CookieParam("session-id") String sessionId,@PathParam("systemUsername") String systemUsername) {
        try {
            logger.info("Received a request to promote an user to admin from a user with session ID: " + sessionId);
            if (databaseValidator.checkSessionId(sessionId)) {
                if (userBean.promoteUserToAdmin(sessionId, systemUsername)) {
                    return Response.status(Response.Status.OK).build();
                } else {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
        } catch (
                Exception e) {
            logger.error("Error updating user type: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(Collections.singletonMap("message", e.getMessage())).build();
        }
    }

}

