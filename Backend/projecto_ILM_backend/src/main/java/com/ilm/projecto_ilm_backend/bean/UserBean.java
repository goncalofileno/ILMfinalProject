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

    public boolean resetPassword(String token, String newPassword) {
        UserEntity user = userDao.findByAuxiliarToken(token);
        if (user != null) {
            user.setPassword(HashUtil.toSHA256(newPassword));
            userDao.merge(user);
            return true;
        }
        return false;
    }

    public UserEntity getUserBySessionId(String sessionId) {
        SessionEntity session = sessionDao.findBySessionId(sessionId);
        if (session != null) {
            return session.getUser();
        }
        return null;
    }

    public UserEntity getUserBySystemUsername(String systemUsername) {
        return userDao.findBySystemUsername(systemUsername);
    }

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

    public boolean validatePassword(UserEntity user, String password) {
        return user.getPassword().equals(HashUtil.toSHA256(password));
    }

    public boolean updatePassword(UserEntity user, String newPassword) {
        user.setPassword(HashUtil.toSHA256(newPassword));
        userDao.merge(user);
        return true;
    }

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

    public boolean isUserAdmin(int userId) {
        UserEntity user = userDao.findById(userId);
        return user != null && user.getType() == UserTypeENUM.ADMIN;
    }

    public boolean isUserCreatorOrManager(int userId, String projectSystemName) {
        UserEntity user = userDao.findById(userId);
        ProjectEntity project = projectDao.findBySystemName(projectSystemName);

        if (user != null && project != null) {
            return project.getUserProjects().stream()
                    .anyMatch(up -> up.getUser().getId() == user.getId() && (up.getType() == UserInProjectTypeENUM.CREATOR || up.getType() == UserInProjectTypeENUM.MANAGER));
        }
        return false;
    }

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
            System.out.println("max page: " + maxPageNumber);
            System.out.println("number of users: " + numberOfUsers);

            userProjectCreationInfoDto.setUserProjectCreationDtos(userProjectCreationDtos);
            userProjectCreationInfoDto.setMaxPageNumber(maxPageNumber);
        }

        return userProjectCreationInfoDto;
    }

    public int calculateMaximumPageUsers(int numberOfProjects, int numberOfProjectPerPage) {
        return (int) Math.ceil((double) numberOfProjects / numberOfProjectPerPage);
    }

    public UserInProjectTypeENUM getUserInProjectENUM(String sessionId, String projectSystemName) {
        UserEntity user = getUserBySessionId(sessionId);
        int projectId = projectDao.getIdBySystemName(projectSystemName);
        if (user != null) {
            return projectBean.getUserTypeInProject(user.getId(), projectId);
        }
        return null;
    }

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

    //checks if the user have projects where its creator, manager or member
    public boolean userHasProjects(String sessionId) {
        UserEntity user = getUserBySessionId(sessionId);
        if (user != null) {
            return userProjectDao.userHasProjects(user.getId());
        }
        return false;
    }

    public boolean promoteUserToAdmin(String sessionId, String userToChangeSystemUsername){
        UserEntity user = getUserBySessionId(sessionId);
        UserEntity userToChange = userDao.findBySystemUsername(userToChangeSystemUsername);
        if(user != null && userToChange != null && user.getType() == UserTypeENUM.ADMIN && userToChange.getType() != UserTypeENUM.ADMIN){
            userToChange.setType(UserTypeENUM.ADMIN);
            userDao.merge(userToChange);
            return true;
        }
        return false;
    }

}