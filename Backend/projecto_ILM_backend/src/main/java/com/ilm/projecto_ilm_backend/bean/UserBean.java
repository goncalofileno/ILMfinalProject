package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.*;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.dto.project.ProjectProfileDto;
import com.ilm.projecto_ilm_backend.dto.resource.ResourcesProjectProfileDto;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import com.ilm.projecto_ilm_backend.dto.user.*;
import com.ilm.projecto_ilm_backend.entity.*;
import com.ilm.projecto_ilm_backend.security.exceptions.UnauthorizedException;
import com.ilm.projecto_ilm_backend.service.websockets.MailWebSocket;
import com.ilm.projecto_ilm_backend.utilities.EmailService;
import com.ilm.projecto_ilm_backend.service.UserService;
import com.ilm.projecto_ilm_backend.utilities.HashUtil;
import com.ilm.projecto_ilm_backend.utilities.imgsPath;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The UserBean class is responsible for managing UserEntity instances.
 * It is an application scoped bean, meaning there is a single instance for the entire application.
 */
@ApplicationScoped
public class UserBean {

    /**
     * The UserDao instance used for accessing the database.
     */
    @Inject
    UserDao userDao;

    /**
     * The LabDao instance used for accessing the database.
     */
    @Inject
    LabDao labDao;

    /**
     * The InterestDao instance used for accessing the database.
     */
    @Inject
    InterestDao interestDao;

    /**
     * The SkillDao instance used for accessing the database.
     */
    @Inject
    SkillDao skillDao;

    /**
     * The EmailService instance used for sending emails.
     */
    @Inject
    EmailService emailService;

    /**
     * The SessionDao instance used for accessing the database.
     */
    @Inject
    SessionDao sessionDao;

    @Inject
    ProjectDao projectDao;

    @Inject
    ProjectBean projectBean;

    @Inject
    UserProjectDao userProjectDao;

    @Inject
    NotificationBean notificationBean;


    private static final int NUMBER_OF_USERS_PER_PAGE = 6;
    /**
     * The logger used to log information, warning and error messages.
     */
    private static final Logger logger = LogManager.getLogger(UserService.class);

    /**
     * Creates a default user with username "admin" if it does not exist.
     * The user is created with predefined values.
     */
    public void createDefaultUsersIfNotExistent() throws MessagingException, UnsupportedEncodingException {
        if (userDao.findById(1) == null) {

            UserEntity user = new UserEntity();
            user.setUsername("administration");
            user.setSystemUsername("administration");
            user.setPassword(HashUtil.toSHA256("administration"));
            user.setEmail("admnistration@iml.com");
            user.setFirstName("Administration");
            user.setLastName("ILM");
            user.setType(UserTypeENUM.ADMIN);
            user.setRegistrationDate(LocalDateTime.now());
            user.setMailConfirmed(true);
            user.setProfileCreated(true);
            user.setPhoto("http://localhost:8080/images/users/4/profile_picture.png?t=1720364913202");
            user.setAvatarPhoto("http://localhost:8080/images/users/4/profile_picture.png?t=1720364913202");
            user.setThumbnailPhoto("http://localhost:8080/images/users/4/profile_picture.png?t=1720364913202");
            LabEntity lab = new LabEntity();
            lab = labDao.findbyLocal(WorkLocalENUM.COIMBRA);
            List<InterestEntity> interests = interestDao.findAll();
            user.setInterests(interests);
            List<SkillEntity> skills = skillDao.findAll();
            user.setSkills(skills);
            user.setLab(lab);
            user.setPublicProfile(false);
            user.setDeleted(false);
            user.setTutorial(false);
            user.setLanguage(LanguageENUM.ENGLISH);

            userDao.persist(user);


        }

        if (userDao.findById(2) == null) {

            UserEntity user = new UserEntity();
            user.setUsername("admin");
            user.setSystemUsername("admin");
            user.setPassword(HashUtil.toSHA256("admin"));
            user.setEmail("admin@admin.com");
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setType(UserTypeENUM.ADMIN);
            user.setRegistrationDate(LocalDateTime.now());
            user.setMailConfirmed(true);
            user.setProfileCreated(true);
            user.setPhoto("https://www.pngkey.com/png/full/114-1149878_setting-user-avatar-in-specific-size-without-breaking.png");
            user.setAvatarPhoto("https://www.pngkey.com/png/full/114-1149878_setting-user-avatar-in-specific-size-without-breaking.png");
            user.setThumbnailPhoto("https://www.pngkey.com/png/full/114-1149878_setting-user-avatar-in-specific-size-without-breaking.png");
            LabEntity lab = new LabEntity();
            lab = labDao.findbyLocal(WorkLocalENUM.COIMBRA);
            List<InterestEntity> interests = interestDao.findAll();
            user.setInterests(interests);
            List<SkillEntity> skills = skillDao.findAll();
            user.setSkills(skills);
            user.setLab(lab);
            user.setPublicProfile(false);
            user.setDeleted(false);
            user.setTutorial(false);
            user.setLanguage(LanguageENUM.ENGLISH);

            userDao.persist(user);


        }

        if (userDao.findById(3) == null) {

            UserEntity user = new UserEntity();
            user.setUsername("user");
            user.setSystemUsername("user");
            user.setPassword(HashUtil.toSHA256("user"));
            user.setEmail("user@user.com");
            user.setFirstName("User");
            user.setLastName("User");
            user.setType(UserTypeENUM.STANDARD_USER);
            user.setRegistrationDate(LocalDateTime.now());
            user.setMailConfirmed(true);
            user.setProfileCreated(true);
            user.setPhoto("https://e7.pngegg.com/pngimages/799/987/png-clipart-computer-icons-avatar-icon-design-avatar-heroes-computer-wallpaper-thumbnail.png");
            user.setAvatarPhoto("https://e7.pngegg.com/pngimages/799/987/png-clipart-computer-icons-avatar-icon-design-avatar-heroes-computer-wallpaper-thumbnail.png");
            user.setThumbnailPhoto("https://e7.pngegg.com/pngimages/799/987/png-clipart-computer-icons-avatar-icon-design-avatar-heroes-computer-wallpaper-thumbnail.png");
            LabEntity lab;
            lab = labDao.findbyLocal(WorkLocalENUM.TOMAR);
            List<InterestEntity> interests = interestDao.findAll();
            user.setInterests(interests);
            List<SkillEntity> skills = skillDao.findAll();
            user.setSkills(skills);
            user.setLab(lab);
            user.setPublicProfile(false);
            user.setDeleted(false);
            user.setTutorial(false);
            user.setLanguage(LanguageENUM.ENGLISH);

            userDao.persist(user);
        }

        if(userDao.findById(4) == null) {
            UserEntity user = new UserEntity();
            user.setUsername("guest");
            user.setPassword("guest");
            user.setEmail("guest@guest.com");
            user.setFirstName("Guest");
            user.setLastName("Guest");
            user.setType(UserTypeENUM.GUEST);
            user.setRegistrationDate(LocalDateTime.now());
            user.setMailConfirmed(false);
            user.setProfileCreated(false);
            user.setDeleted(false);
            user.setTutorial(false);
            user.setPublicProfile(false);
            user.setLanguage(LanguageENUM.ENGLISH);

            userDao.persist(user);
        }

        for (int i = 5; i < 19; i++) {
            if (userDao.findById(i) == null) {
                UserEntity user = new UserEntity();
                user.setUsername("user" + i);
                user.setSystemUsername("user" + i);
                user.setPassword(HashUtil.toSHA256("user"));
                user.setEmail("user" + i + "@user.com");
                user.setFirstName("User" + i);
                user.setLastName("User" + i);
                user.setType(UserTypeENUM.STANDARD_USER);
                user.setRegistrationDate(LocalDateTime.now());
                user.setMailConfirmed(true);
                user.setProfileCreated(true);
                user.setPhoto("https://e7.pngegg.com/pngimages/799/987/png-clipart-computer-icons-avatar-icon-design-avatar-heroes-computer-wallpaper-thumbnail.png");
                user.setAvatarPhoto("https://e7.pngegg.com/pngimages/799/987/png-clipart-computer-icons-avatar-icon-design-avatar-heroes-computer-wallpaper-thumbnail.png");
                user.setThumbnailPhoto("https://e7.pngegg.com/pngimages/799/987/png-clipart-computer-icons-avatar-icon-design-avatar-heroes-computer-wallpaper-thumbnail.png");
                LabEntity lab;
                lab = labDao.findbyLocal(WorkLocalENUM.TOMAR);
                List<InterestEntity> interests = interestDao.findAll();
                user.setInterests(interests);
                List<SkillEntity> skills = skillDao.findAll();
                user.setSkills(skills);
                user.setLab(lab);
                user.setPublicProfile(false);
                user.setDeleted(false);
                user.setTutorial(false);
                user.setLanguage(LanguageENUM.ENGLISH);

                userDao.persist(user);
            }
        }
    }

    /**
     * Registers a new user.
     *
     * @param registerUserDto the DTO containing user registration details
     * @return true if the user was successfully registered, false otherwise
     */
    public boolean registerUser(RegisterUserDto registerUserDto) {
        try {
            UserEntity user = new UserEntity();
            user.setEmail(registerUserDto.getMail());
            user.setPassword(HashUtil.toSHA256(registerUserDto.getPassword()));
            user.setMailConfirmed(false);
            user.setProfileCreated(false);
            user.setDeleted(false);
            user.setTutorial(false);
            user.setRegistrationDate(LocalDateTime.now());
            user.setType(UserTypeENUM.STANDARD_USER);
            user.setPhoto("https://www.pngkey.com/png/full/114-1149878_setting-user-avatar-in-specific-size-without-breaking.png");
            user.setAuxiliarToken(generateNewToken());
            user.setLanguage(LanguageENUM.ENGLISH);
            emailService.sendConfirmationEmail(user.getEmail(), user.getAuxiliarToken());
            userDao.persist(user);
            return true;
        } catch (Exception e) {
            // Log the exception
            Logger logger = LogManager.getLogger(UserService.class);
            logger.error("Error persisting user: ", e);
            return false;
        }
    }

    /**
     * Generates a new token for user confirmation.
     *
     * @return the generated token
     */
    private String generateNewToken() {
        SecureRandom secureRandom = new SecureRandom();
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    /**
     * Confirms the user's email.
     *
     * @param token
     * @return
     */
    public boolean confirmEmail(String token) {
        UserEntity user = userDao.findByAuxiliarToken(token);
        if (user != null) {
            user.setMailConfirmed(true);
            userDao.merge(user);
            return true;
        }
        return false;
    }

    /**
     * Creates a new user profile.
     *
     * @param userProfileDto the DTO containing user profile details
     * @param auxiliarToken  the token used to authenticate the user
     * @return true if the profile was successfully created, false otherwise
     */
    public boolean createProfile(UserProfileDto userProfileDto, String auxiliarToken) {
        UserEntity user = userDao.findByAuxiliarToken(auxiliarToken);
        if (user == null) {
            return false;
        }

        user.setFirstName(userProfileDto.getFirstName());
        user.setLastName(userProfileDto.getLastName());
        user.setUsername(userProfileDto.getUsername());
        user.setSystemUsername(generateUniqueSystemUsername(userProfileDto.getFirstName(), userProfileDto.getLastName()));
        logger.info("System username: " + user.getSystemUsername());
        user.setLab(labDao.findbyLocal(WorkLocalENUM.valueOf(userProfileDto.getLab().toUpperCase())));
        user.setBio(userProfileDto.getBio());
        user.setPublicProfile(userProfileDto.isPublicProfile());
        user.setPhoto("http://localhost:8080/images/users/default/profile_picture.png");
        user.setAvatarPhoto("http://localhost:8080/images/users/default/profile_picture.png");
        user.setThumbnailPhoto("http://localhost:8080/images/users/default/profile_picture.png");

        // Handle skills
        List<SkillEntity> skillEntities = userProfileDto.getSkills().stream()
                .map(skillDto -> skillDao.findByName(skillDto.getName())
                        .orElseGet(() -> {
                            SkillEntity newSkill = new SkillEntity();
                            newSkill.setName(skillDto.getName());
                            newSkill.setType(SkillTypeENUM.valueOf(skillDto.getType()));
                            skillDao.create(newSkill);
                            return newSkill;
                        }))
                .collect(Collectors.toList());
        user.setSkills(skillEntities);

        // Handle interests
        List<InterestEntity> interestEntities = userProfileDto.getInterests().stream()
                .map(interestDto -> interestDao.findByName(interestDto.getName())
                        .orElseGet(() -> {
                            InterestEntity newInterest = new InterestEntity();
                            newInterest.setName(interestDto.getName());
                            interestDao.create(newInterest);
                            return newInterest;
                        }))
                .collect(Collectors.toList());
        user.setInterests(interestEntities);

        user.setProfileCreated(true);


        userDao.merge(user);
        return true;
    }

    /**
     * Generates a unique system username based on the user's first and last name.
     *
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @return the generated system username
     */
    private String generateUniqueSystemUsername(String firstName, String lastName) {
        // Remove caracteres especiais e espaços, e converte para minúsculas
        String baseUsername = Normalizer.normalize(firstName + lastName, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "") // Remove diacríticos
                .replaceAll("[^a-zA-Z0-9]", "") // Remove outros caracteres não alfanuméricos
                .toLowerCase();
        String systemUsername = baseUsername;
        int suffix = 1;
        logger.info("Base username: " + baseUsername);

        while (userDao.checkSystemUsername(systemUsername)) {
            systemUsername = baseUsername + suffix;
            suffix++;
        }

        logger.info("Generated system username: " + systemUsername);
        return systemUsername;
    }

/**
 * Saves a user's profile picture from a Base64 encoded string.
 * This method decodes the Base64 string to an image, determines the image format,
 * and saves the original image along with resized versions for avatar and thumbnail use.
 * It updates the user entity with URLs to these images.
 *
 * @param auth The authentication token or session ID used to identify the user.
 * @param base64Image The Base64 encoded string of the image to be saved.
 * @return true if the image was successfully saved and the user entity updated, false otherwise.
 * @throws Exception If the user cannot be found or if an error occurs during image processing.
 */
public boolean saveUserProfilePicture(String auth, String base64Image) {
    try {
        // Decode the Base64 string back to an image
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // Extract user information from the token (assuming JWT or similar)
        UserEntity user = userDao.findByAuxiliarToken(auth);
        if (user == null) {
            user = sessionDao.findBySessionId(auth).getUser();
        }

        if (user == null) {
            throw new Exception("User not found");
        }

        // Save the original image to the user's specific directory
        String directoryPath = imgsPath.IMAGES_PATH + "/" + user.getId();
        logger.info("Directory path: " + directoryPath);
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Determine the image format
        ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
        BufferedImage originalImage = ImageIO.read(bis);
        String format = getImageFormat(originalImage);

        // Save the original image
        String originalFilePath = directoryPath + "/profile_picture." + format;
        ImageIO.write(originalImage, format, new File(originalFilePath));
        logger.info("Original file path: " + originalFilePath);

        // Save resized images
        BufferedImage avatarImage = resizeImage(originalImage, 100, 100); // Example size for avatar
        String avatarFilePath = directoryPath + "/profile_picture_avatar." + format;
        ImageIO.write(avatarImage, format, new File(avatarFilePath));

        BufferedImage thumbnailImage = resizeImage(originalImage, 300, 300); // Example size for thumbnail
        String thumbnailFilePath = directoryPath + "/profile_picture_thumbnail." + format;
        ImageIO.write(thumbnailImage, format, new File(thumbnailFilePath));

        // Construct the URLs
        long timestamp = System.currentTimeMillis();
        String baseUrl = "http://localhost:8080/images/users/" + user.getId();
        String originalUrl = baseUrl + "/profile_picture." + format + "?t=" + timestamp;
        logger.info("Original URL: " + originalUrl);
        String avatarUrl = baseUrl + "/profile_picture_avatar." + format + "?t=" + timestamp;
        logger.info("Avatar URL: " + avatarUrl);
        String thumbnailUrl = baseUrl + "/profile_picture_thumbnail." + format + "?t=" + timestamp;
        logger.info("Thumbnail URL: " + thumbnailUrl);

        // Update the user's photo URLs in the database
        user = userDao.findById(user.getId());
        user.setPhoto(originalUrl); // Set URL for original image
        user.setAvatarPhoto(avatarUrl); // Set URL for avatar
        user.setThumbnailPhoto(thumbnailUrl); // Set URL for thumbnail
        userDao.merge(user);

        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


    /**
     * Resizes an image to a specific width and height.
     *
     * @param originalImage the original image
     * @param width         the desired width
     * @param height        the desired height
     * @return the resized image
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    /**
     * Gets the format of an image.
     *
     * @param image the image to check
     * @return the format of the image (e.g., "jpg", "png")
     * @throws IOException if an error occurs during reading
     */
    private String getImageFormat(BufferedImage image) throws IOException {
        if (ImageIO.getImageWritersBySuffix("png").hasNext()) {
            return "png";
        } else if (ImageIO.getImageWritersBySuffix("jpg").hasNext()) {
            return "jpg";
        } else {
            throw new IOException("Unsupported image format");
        }
    }

    /**
     * Attempts to log in a user with the provided credentials.
     * If successful, it checks for an existing session. If one exists, it is refreshed and returned.
     * Otherwise, a new session is created and returned.
     *
     * @param registerUserDto The DTO containing the user's login credentials.
     * @param clientIpAddress The IP address of the client attempting to log in.
     * @param userAgent The user agent of the client attempting to log in.
     * @return The session ID if login is successful; null otherwise.
     */
    public String loginUser(RegisterUserDto registerUserDto, String clientIpAddress, String userAgent) {
        if (userDao.checkPassFromEmail(registerUserDto.getMail(), HashUtil.toSHA256(registerUserDto.getPassword()))) {
            UserEntity userEntity = userDao.findByEmail(registerUserDto.getMail());
            logger.info("User found: " + userEntity.getUsername());

            logger.info("Checking for existing session for user id: " + userEntity.getId());
            SessionEntity existingSession = sessionDao.findByUserId(userEntity.getId());

            if (existingSession != null) {
                logger.info("Session found: " + existingSession.getSessionId());
                existingSession.setExpiresAt(LocalDateTime.now().plusDays(1));
                sessionDao.merge(existingSession);
                return existingSession.getSessionId();
            } else {
                logger.info("Session not found");
                SessionEntity sessionEntity = new SessionEntity();
                sessionEntity.setSessionId(UUID.randomUUID().toString());
                sessionEntity.setUser(userEntity);
                sessionEntity.setCreatedAt(LocalDateTime.now());
                sessionEntity.setExpiresAt(LocalDateTime.now().plusDays(1));
                sessionEntity.setIpAddress(clientIpAddress);
                sessionEntity.setUserAgent(userAgent);

                sessionDao.persist(sessionEntity);

                return sessionEntity.getSessionId();
            }
        } else {
            return null;
        }
    }

    /**
     * Creates a new session for a user, or refreshes an existing one.
     * This method is used internally to manage user sessions upon successful login or session validation.
     *
     * @param userEntity The user entity for whom the session is being created or refreshed.
     * @param clientIpAddress The IP address of the client.
     * @param userAgent The user agent of the client.
     * @return The session ID of the newly created or refreshed session.
     */
    public String createSessionForUser(UserEntity userEntity, String clientIpAddress, String userAgent) {
        logger.info("Checking for existing session for user id: " + userEntity.getId());
        SessionEntity existingSession = sessionDao.findByUserId(userEntity.getId());

        if (existingSession != null) {
            logger.info("Session found: " + existingSession.getSessionId());
            existingSession.setExpiresAt(LocalDateTime.now().plusDays(1));
            sessionDao.merge(existingSession);
            return existingSession.getSessionId();
        } else {
            logger.info("Session not found");
            SessionEntity sessionEntity = new SessionEntity();
            sessionEntity.setSessionId(UUID.randomUUID().toString());
            sessionEntity.setUser(userEntity);
            sessionEntity.setCreatedAt(LocalDateTime.now());
            sessionEntity.setExpiresAt(LocalDateTime.now().plusDays(1));
            sessionEntity.setIpAddress(clientIpAddress);
            sessionEntity.setUserAgent(userAgent);

            sessionDao.persist(sessionEntity);
            return sessionEntity.getSessionId();
        }
    }

    /**
     * Sends a password reset link to the user's email if the user exists.
     * This method generates a new auxiliary token for the user, saves it, and sends an email with a reset link.
     *
     * @param email The email address of the user requesting a password reset.
     * @return true if the email was successfully sent, false otherwise.
     */
    public boolean sendForgetPassLink(String email) {
        UserEntity user = userDao.findByEmail(email);
        if (user != null) {
            user.setAuxiliarToken(generateNewToken());
            userDao.merge(user);
            try {
                emailService.sendResetPasswordEmail(user.getEmail(), user.getAuxiliarToken());
                return true;
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * Resets a user's password given a valid auxiliary token and a new password.
     * The method looks up the user by the auxiliary token, and if found, updates the user's password.
     *
     * @param token The auxiliary token associated with the user's password reset request.
     * @param newPassword The new password for the user.
     * @return true if the password was successfully reset, false otherwise.
     */
    public boolean resetPassword(String token, String newPassword) {
        UserEntity user = userDao.findByAuxiliarToken(token);
        if (user != null) {
            user.setPassword(HashUtil.toSHA256(newPassword));
            userDao.merge(user);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a user entity based on a session ID.
     * This method is commonly used to identify the user associated with a given session.
     *
     * @param sessionId The session ID to look up the user by.
     * @return The UserEntity associated with the session ID, or null if not found.
     */
    public UserEntity getUserBySessionId(String sessionId) {
        SessionEntity session = sessionDao.findBySessionId(sessionId);
        if (session != null) {
            return session.getUser();
        }
        return null;
    }

    /**
     * Retrieves a user entity based on their system username.
     * This method provides a way to fetch user details using the unique system username.
     *
     * @param systemUsername The system username of the user.
     * @return The UserEntity associated with the system username, or null if not found.
     */
    public UserEntity getUserBySystemUsername(String systemUsername) {
        return userDao.findBySystemUsername(systemUsername);
    }

    /**
     * Updates a user's profile information based on the provided UserProfileDto and session ID.
     * This method allows users to update their profile details, including first name, last name, lab, and bio.
     *
     * @param userProfileDto The DTO containing the new profile information.
     * @param sessionId The session ID of the user updating their profile.
     * @throws Exception If the session is invalid or required fields are missing.
     */
    public void updateUserProfile(UserProfileDto userProfileDto, String sessionId) throws Exception {
        UserEntity user = sessionDao.findBySessionId(sessionId).getUser();

        if (user == null) {
            throw new Exception("Unauthorized: Invalid session");
        }

        if (userProfileDto.getFirstName() == null || userProfileDto.getLastName() == null || userProfileDto.getLab() == null) {
            throw new Exception("First Name, Last Name, and Office are required fields");
        }

        user.setFirstName(userProfileDto.getFirstName());
        user.setLastName(userProfileDto.getLastName());
        user.setUsername(userProfileDto.getUsername());
        user.setLab(labDao.findbyLocal(userProfileDto.getLab().toUpperCase()));
        user.setBio(userProfileDto.getBio());
        user.setPublicProfile(userProfileDto.isPublicProfile());

        // Handle skills
        List<SkillEntity> skillEntities = userProfileDto.getSkills().stream()
                .map(skillDto -> skillDao.findByName(skillDto.getName())
                        .orElseGet(() -> {
                            SkillEntity newSkill = new SkillEntity();
                            newSkill.setName(skillDto.getName());
                            newSkill.setType(SkillTypeENUM.valueOf(skillDto.getType()));
                            skillDao.create(newSkill);
                            return newSkill;
                        }))
                .collect(Collectors.toList());
        user.setSkills(skillEntities);

        // Handle interests
        List<InterestEntity> interestEntities = userProfileDto.getInterests().stream()
                .map(interestDto -> interestDao.findByName(interestDto.getName())
                        .orElseGet(() -> {
                            InterestEntity newInterest = new InterestEntity();
                            newInterest.setName(interestDto.getName());
                            interestDao.create(newInterest);
                            return newInterest;
                        }))
                .collect(Collectors.toList());
        user.setInterests(interestEntities);

        userDao.merge(user);
    }

    /**
     * Validates a user's password against the stored hash.
     * This method is used for password verification during login and other authentication processes.
     *
     * @param user The user entity whose password is being validated.
     * @param password The password to validate.
     * @return true if the password is correct, false otherwise.
     */
    public boolean validatePassword(UserEntity user, String password) {
        return user.getPassword().equals(HashUtil.toSHA256(password));
    }

    /**
     * Updates a user's password to a new value.
     * This method securely updates the user's password with a new hashed value.
     *
     * @param user The user entity whose password is being updated.
     * @param newPassword The new password for the user.
     * @return true if the password was successfully updated, false otherwise.
     */
    public boolean updatePassword(UserEntity user, String newPassword) {
        user.setPassword(HashUtil.toSHA256(newPassword));
        userDao.merge(user);
        return true;
    }

    /**
     * Retrieves the profile information of a user for editing purposes, based on their system username and session ID.
     * This method ensures that only authorized users can access and edit their own profile information.
     *
     * @param systemUsername The system username of the user whose profile is being edited.
     * @param sessionId The session ID of the requesting user.
     * @return A UserProfileDto containing the editable profile information.
     * @throws NotFoundException If the user cannot be found.
     * @throws UnauthorizedException If the session ID is invalid or does not match the user.
     */
    public UserProfileDto getEditProfile(String systemUsername, String sessionId) {
        logger.info("Received a request to get the edit profile of a user with session ID: " + sessionId);
        UserEntity requestingUser = getUserBySessionId(sessionId);
        if (requestingUser != null) {
            UserEntity profileUser = userDao.getUserBySystemUsernameWithAssociations(systemUsername);
            if (profileUser != null) {
                UserProfileDto profileDto = new UserProfileDto();
                profileDto.setUsername(profileUser.getUsername());
                profileDto.setFirstName(profileUser.getFirstName());
                profileDto.setLastName(profileUser.getLastName());
                profileDto.setLab(profileUser.getLab().getLocal().toString());
                profileDto.setBio(profileUser.getBio());
                profileDto.setPublicProfile(profileUser.isPublicProfile());
                profileDto.setPhoto(profileUser.getPhoto());


                profileDto.setSkills(profileUser.getSkills().stream()
                        .map(skill -> {
                            SkillDto skillDto = new SkillDto();
                            skillDto.setId(skill.getId());
                            skillDto.setName(skill.getName());
                            skillDto.setType(skill.getType().toString());
                            return skillDto;
                        }).collect(Collectors.toList()));

                profileDto.setInterests(profileUser.getInterests().stream()
                        .map(interest -> {
                            InterestDto interestDto = new InterestDto();
                            interestDto.setId(interest.getId());
                            interestDto.setName(interest.getName());
                            return interestDto;
                        }).collect(Collectors.toList()));

                return profileDto;
            } else {
                throw new NotFoundException("User not found");
            }
        } else {
            throw new UnauthorizedException("Unauthorized: Invalid session");
        }
    }

    /**
     * Retrieves the public profile information of a user, based on their system username and session ID.
     * This method ensures that sensitive information is only available to authorized users or for public profiles.
     *
     * @param systemUsername The system username of the user whose profile is being requested.
     * @param sessionId The session ID of the requesting user.
     * @return A ShowProfileDto containing the user's public profile information.
     * @throws NotFoundException If the user cannot be found.
     * @throws UnauthorizedException If the session ID is invalid or does not match the user.
     */
    public ShowProfileDto getProfile(String systemUsername, String sessionId) {
        logger.info("Received a request to get the profile of a user with session ID: " + sessionId);
        UserEntity requestingUser = getUserBySessionId(sessionId);
        if (requestingUser != null) {
            UserEntity profileUser = userDao.getUserBySystemUsernameWithAssociations(systemUsername);
            if (profileUser != null) {
                ShowProfileDto profileDto = new ShowProfileDto();
                profileDto.setUsername(profileUser.getUsername());
                profileDto.setFirstName(profileUser.getFirstName());
                profileDto.setLastName(profileUser.getLastName());
                profileDto.setEmail(profileUser.getEmail());
                profileDto.setLocation(profileUser.getLab().getLocal().toString());
                profileDto.setProfileImage(profileUser.getPhoto());
                profileDto.setPublicProfile(profileUser.isPublicProfile());
                profileDto.setUserType(profileUser.getType());

                if (profileUser.isPublicProfile() || requestingUser.getId() == (profileUser.getId())) {
                    profileDto.setBio(profileUser.getBio());

                    profileDto.setProjects(profileUser.getUserProjects().stream()
                            .map(up -> {
                                ProjectProfileDto projectDto = new ProjectProfileDto();
                                ProjectEntity project = up.getProject();
                                projectDto.setName(project.getName());
                                projectDto.setSystemName(project.getSystemName());
                                projectDto.setTypeMember(up.getType().toString());
                                projectDto.setStatus(project.getStatus().toString());
                                projectDto.setCreatedDate(project.getCreatedAt());

                                return projectDto;
                            }).collect(Collectors.toList()));
                    
                    profileDto.setSkills(profileUser.getSkills().stream()
                            .map(skill -> {
                                SkillDto skillDto = new SkillDto();
                                skillDto.setId(skill.getId());
                                skillDto.setName(skill.getName());
                                skillDto.setType(skill.getType().toString());
                                return skillDto;
                            }).collect(Collectors.toList()));

                    profileDto.setInterests(profileUser.getInterests().stream()
                            .map(interest -> {
                                InterestDto interestDto = new InterestDto();
                                interestDto.setId(interest.getId());
                                interestDto.setName(interest.getName());
                                return interestDto;
                            }).collect(Collectors.toList()));
                }

                return profileDto;
            } else {
                throw new NotFoundException("User not found");
            }
        } else {
            throw new UnauthorizedException("Unauthorized: Invalid session");
        }
    }

    /**
     * Checks if a given user ID belongs to an admin user.
     * This method is used to verify if a user has administrative privileges.
     *
     * @param userId The ID of the user to check.
     * @return true if the user is an admin, false otherwise.
     */
    public boolean isUserAdmin(int userId) {
        UserEntity user = userDao.findById(userId);
        return user != null && user.getType() == UserTypeENUM.ADMIN;
    }

    /**
     * Determines if a user is either the creator or a manager of a given project.
     * This method is used to check a user's permissions within a specific project context.
     *
     * @param userId The ID of the user.
     * @param projectSystemName The system name of the project.
     * @return true if the user is a creator or manager of the project, false otherwise.
     */
    public boolean isUserCreatorOrManager(int userId, String projectSystemName) {
        UserEntity user = userDao.findById(userId);
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);

        if (user != null && project != null) {
            return project.getUserProjects().stream()
                    .anyMatch(up -> up.getUser().getId() == user.getId() && (up.getType() == UserInProjectTypeENUM.CREATOR || up.getType() == UserInProjectTypeENUM.MANAGER));
        }
        return false;
    }

    /**
     * Retrieves information for creating a user project, including potential project members.
     * This method supports the project creation process by suggesting users based on various criteria.
     *
     * @param sessionId The session ID of the user creating the project.
     * @param systemProjectName The system name of the project for which members are being suggested.
     * @param rejectedUsersDto A DTO containing IDs of users to exclude from the suggestions.
     * @param page The pagination page number.
     * @param labName The name of the lab to filter users by.
     * @param keyword A keyword to filter users by.
     * @return A UserProjectCreationInfoDto containing information about potential project members.
     */
    public UserProjectCreationInfoDto getUserProjectCreationInfoDto(String sessionId, String systemProjectName, RejectedIdsDto rejectedUsersDto, int page, String labName, String keyword) {
        int userId = sessionDao.findBySessionId(sessionId).getUser().getId();
        LabEntity lab;
        if (labName == null || labName.equals("")) lab = null;
        else lab = labDao.findbyLocal(WorkLocalENUM.valueOf(labName));
        if (keyword.equals("")) keyword = null;

        List<String> skillsInProject=new ArrayList<>();
        if(!systemProjectName.equals("")) {
            skillsInProject = projectDao.getSkillsBySystemName(systemProjectName);
        }
        List<Object[]> userInfo = userDao.getUserProjectCreationDto(userId, rejectedUsersDto.getRejectedIds(), NUMBER_OF_USERS_PER_PAGE, page, lab, keyword, skillsInProject);

        ArrayList<UserProjectCreationDto> userProjectCreationDtos = new ArrayList<>();
        UserProjectCreationInfoDto userProjectCreationInfoDto = new UserProjectCreationInfoDto();

        for (Object[] user : userInfo) {
            UserProjectCreationDto userProjectCreationDto = new UserProjectCreationDto();

            userProjectCreationDto.setLab((WorkLocalENUM) user[0]);
            userProjectCreationDto.setName((String) user[1] + " " + (String) user[2]);
            userProjectCreationDto.setPhoto((String) user[3]);
            userProjectCreationDto.setId((int) user[4]);
            userProjectCreationDto.setPublicProfile((boolean) user[7]);
            userProjectCreationDto.setEmail((String) user[8]);
            List<SkillEntity> skillsEntities = userDao.getUserSkills((int) user[4], skillsInProject);
            List<SkillDto> skills = new ArrayList<>();
            if(userProjectCreationDto.isPublicProfile()) {
                for (SkillEntity skill : skillsEntities) {
                    if (!skill.isDeleted()) {
                        SkillDto skillDto = new SkillDto();
                        skillDto.setId(skill.getId());
                        skillDto.setName(skill.getName());
                        skillDto.setType(skill.getType().toString());
                        skillDto.setInProject(projectDao.isSkillInProject(systemProjectName, skill.getName()));
                        skills.add(skillDto);
                    }
                }
            }
            userProjectCreationDto.setSkills(skills);
            userProjectCreationDto.setSystemUsername((String) user[5]);
            userProjectCreationDtos.add(userProjectCreationDto);
            int numberOfUsers = userDao.getNumberUserProjectCreationDto(userId, rejectedUsersDto.getRejectedIds(), lab, keyword);
            int maxPageNumber = calculateMaximumPageUsers(numberOfUsers, NUMBER_OF_USERS_PER_PAGE);

            userProjectCreationInfoDto.setUserProjectCreationDtos(userProjectCreationDtos);
            userProjectCreationInfoDto.setMaxPageNumber(maxPageNumber);
        }

        return userProjectCreationInfoDto;
    }

    /**
     * Calculates the maximum number of pages for user project creation suggestions.
     * This method aids in pagination by determining the total number of pages based on the number of users.
     *
     * @param numberOfProjects The total number of projects.
     * @param numberOfProjectPerPage The number of projects to display per page.
     * @return The maximum number of pages.
     */
    public int calculateMaximumPageUsers(int numberOfProjects, int numberOfProjectPerPage) {
        return (int) Math.ceil((double) numberOfProjects / numberOfProjectPerPage);
    }

    /**
     * Retrieves the role of a user within a specific project, based on session ID and project system name.
     * This method is used to determine a user's role within a project for access control and display purposes.
     *
     * @param sessionId The session ID of the user.
     * @param projectSystemName The system name of the project.
     * @return The UserInProjectTypeENUM representing the user's role in the project, or null if not found.
     */
    public UserInProjectTypeENUM getUserInProjectENUM(String sessionId, String projectSystemName) {
        UserEntity user = getUserBySessionId(sessionId);
        int projectId = projectDao.getIdBySystemName(projectSystemName);
        if (user != null) {
            return projectBean.getUserTypeInProject(user.getId(), projectId);
        }
        return null;
    }

    /**
     * Updates the preferred language of a user.
     * This method allows users to change their preferred language for the application interface.
     *
     * @param sessionId The session ID of the user requesting the language change.
     * @param language The new language preference.
     * @return true if the language was successfully updated, false otherwise.
     */
    public boolean updateLanguage(String sessionId, LanguageENUM language) {
        UserEntity user = getUserBySessionId(sessionId);

        if (language != LanguageENUM.ENGLISH && language != LanguageENUM.PORTUGUESE) {
            return false;
        }

        if (user != null) {
            user.setLanguage(language);
            userDao.merge(user);
            return true;
        }
        return false;
    }

    /**
     * Checks if a user is involved in any projects as a creator, manager, or member.
     * This method is used to determine if a user has any associated projects.
     *
     * @param sessionId The session ID of the user.
     * @return true if the user is involved in any projects, false otherwise.
     */
    public boolean userHasProjects(String sessionId) {
        UserEntity user = getUserBySessionId(sessionId);
        if (user != null) {
            return userProjectDao.userHasProjects(user.getId());
        }
        return false;
    }

    /**
     * Promotes a user to an admin role.
     * This method allows an existing admin to grant administrative privileges to another user.
     *
     * @param sessionId The session ID of the admin user making the request.
     * @param userToChangeSystemUsername The system username of the user being promoted to admin.
     * @return true if the user was successfully promoted to admin, false otherwise.
     */
    public boolean promoteUserToAdmin(String sessionId, String userToChangeSystemUsername){
        UserEntity user = getUserBySessionId(sessionId);
        UserEntity userToChange = userDao.findBySystemUsername(userToChangeSystemUsername);
        if(user != null && userToChange != null && user.getType() == UserTypeENUM.ADMIN && userToChange.getType() != UserTypeENUM.ADMIN){
            userToChange.setType(UserTypeENUM.ADMIN);
            userDao.merge(userToChange);

            notificationBean.createPromoteToAdminNotification(user.getSystemUsername(), userToChange);
            if (sessionDao.findByUserId(userToChange.getId()) != null) {
                String receiverSessionId = sessionDao.findByUserId(userToChange.getId()).getSessionId();
                MailWebSocket.notifyPromoteToAdmin(receiverSessionId);
            }

            return true;
        }
        return false;
    }

}