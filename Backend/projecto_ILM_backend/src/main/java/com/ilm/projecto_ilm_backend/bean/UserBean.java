package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.SkillTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.dao.InterestDao;
import com.ilm.projecto_ilm_backend.dao.LabDao;
import com.ilm.projecto_ilm_backend.dao.SkillDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dto.user.RegisterUserDto;
import com.ilm.projecto_ilm_backend.dto.user.UserProfileDto;
import com.ilm.projecto_ilm_backend.utilities.EmailService;
import com.ilm.projecto_ilm_backend.entity.InterestEntity;
import com.ilm.projecto_ilm_backend.entity.LabEntity;
import com.ilm.projecto_ilm_backend.entity.SkillEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.service.UserService;
import com.ilm.projecto_ilm_backend.utilities.HashUtil;
import com.ilm.projecto_ilm_backend.utilities.imgsPath;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
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

    @Inject
    EmailService emailService;

    /**
     * Creates a default user with username "admin" if it does not exist.
     * The user is created with predefined values.
     */
    public void createDefaultUsersIfNotExistent() throws MessagingException, UnsupportedEncodingException {
        if (userDao.findById(1) == null) {
            UserEntity user = new UserEntity();
            user.setUsername("admin");
            user.setPassword(HashUtil.toSHA256("admin"));
            user.setEmail("admin@admin.com");
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setType(UserTypeENUM.ADMIN);
            user.setRegistrationDate(LocalDateTime.now());
            user.setMailConfirmed(true);
            user.setProfileCreated(true);
            user.setPhoto("https://www.pngkey.com/png/full/114-1149878_setting-user-avatar-in-specific-size-without-breaking.png");
            LabEntity lab = new LabEntity();
            lab = labDao.findbyLocal(WorkLocalENUM.COIMBRA);
            List<InterestEntity> interests = interestDao.findAll();
            user.setInterests(interests);
            List<SkillEntity> skills = skillDao.findAll();
            user.setSkills(skills);
            user.setSkills(skills);
            user.setLab(lab);
            user.setPublicProfile(false);
            user.setDeleted(false);
            user.setTutorial(false);

            userDao.persist(user);
        }

        if (userDao.findById(2) == null) {
            UserEntity user = new UserEntity();
            user.setUsername("user");
            user.setPassword(HashUtil.toSHA256("user"));
            user.setEmail("user@user.com");
            user.setFirstName("User");
            user.setLastName("User");
            user.setType(UserTypeENUM.STANDARD_USER);
            user.setRegistrationDate(LocalDateTime.now());
            user.setMailConfirmed(true);
            user.setProfileCreated(true);
            user.setPhoto("https://www.pngkey.com/png/full/114-1149878_setting-user-avatar-in-specific-size-without-breaking.png");
            LabEntity lab = new LabEntity();
            lab = labDao.findbyLocal(WorkLocalENUM.PORTO);
            List<InterestEntity> interests = interestDao.findAll();
            user.setInterests(interests);
            List<SkillEntity> skills = skillDao.findAll();
            user.setSkills(skills);
            user.setLab(lab);
            user.setPublicProfile(false);
            user.setDeleted(false);
            user.setTutorial(false);

            userDao.persist(user);
        }

        if (userDao.findById(3) == null) {
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
     * @param auxiliarToken the token used to authenticate the user
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
        user.setLab(labDao.findbyLocal(WorkLocalENUM.valueOf(userProfileDto.getLab().toUpperCase())));
        user.setBio(userProfileDto.getBio());
        user.setPublicProfile(userProfileDto.publicProfile());

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
        return true;
    }


    /**
     * Saves a user's profile picture.
     *
     * @param authHeader the authorization header containing the user's token
     * @param base64Image the profile picture as a Base64 string
     * @return true if the profile picture was successfully saved, false otherwise
     */
    public boolean saveUserProfilePicture(String authHeader, String base64Image) {
        try {
            // Decode the Base64 string back to an image
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Extract user information from the token (assuming JWT or similar)
            UserEntity user = userDao.findByAuxiliarToken(authHeader);

            // Save the original image to the user's specific directory
            String directoryPath = imgsPath.IMAGES_PATH+ user.getId();
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFilePath = directoryPath + "/profile_picture.jpg";
            Files.write(Paths.get(originalFilePath), imageBytes);

            // Save resized images
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage originalImage = ImageIO.read(bis);

            BufferedImage avatarImage = resizeImage(originalImage, 100, 100); // Example size for avatar
            String avatarFilePath = directoryPath + "/profile_picture_avatar.jpg";
            ImageIO.write(avatarImage, "jpg", new File(avatarFilePath));

            BufferedImage thumbnailImage = resizeImage(originalImage, 300, 300); // Example size for thumbnail
            String thumbnailFilePath = directoryPath + "/profile_picture_thumbnail.jpg";
            ImageIO.write(thumbnailImage, "jpg", new File(thumbnailFilePath));

            // Update the user's photo paths in the database
            user = userDao.findById(user.getId());
            user.setPhoto(originalFilePath); // Set path for original image
            user.setAvatarPhoto(avatarFilePath); // Set path for avatar
            user.setThumbnailPhoto(thumbnailFilePath); // Set path for thumbnail
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
     * @param width the desired width
     * @param height the desired height
     * @return the resized image
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    /**
     * Logs in a user.
     *
     * @param registerUserDto the DTO containing user login details
     * @return the token of the logged in user, or null if login failed
     */
    public String loginUser(RegisterUserDto registerUserDto) {
        if (userDao.checkPassFromEmail(registerUserDto.getMail(), HashUtil.toSHA256(registerUserDto.getPassword()))) {
            UserEntity userEntity = userDao.findByEmail(registerUserDto.getMail());
            userEntity.setToken(generateNewToken());
            userDao.merge(userEntity);
            return userEntity.getToken();
        }
        else return null;
    }

   public boolean sendForgetPassLink(String email){
        UserEntity user = userDao.findByEmail(email);
        if(user != null){
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

    public boolean resetPassword(String token, String newPassword){
        UserEntity user = userDao.findByAuxiliarToken(token);
        if(user != null){
            user.setPassword(HashUtil.toSHA256(newPassword));
            userDao.merge(user);
            return true;
        }
        return false;
   }
}

