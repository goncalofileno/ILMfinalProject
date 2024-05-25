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
import com.ilm.projecto_ilm_backend.emailService.EmailService;
import com.ilm.projecto_ilm_backend.entity.InterestEntity;
import com.ilm.projecto_ilm_backend.entity.LabEntity;
import com.ilm.projecto_ilm_backend.entity.SkillEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.service.UserService;
import com.ilm.projecto_ilm_backend.utilities.HashUtil;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
            user.setPassword("admin");
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
            user.setPassword("user");
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

            // Save the image to the user's specific directory
            String directoryPath = "/Users/goncalofileno/Servers/wildfly-31.0.1.Final/photos/" + user.getId();
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath = directoryPath + "/profile_picture.jpg";
            Files.write(Paths.get(filePath), imageBytes);

            // Update the user's photo path in the database
            user = userDao.findById(user.getId());
            user.setPhoto(filePath);
            userDao.merge(user);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Logs in a user.
     *
     * @param registerUserDto the DTO containing user login details
     * @return the token of the logged in user, or null if login failed
     */
    public String loginUser(RegisterUserDto registerUserDto) {
        if (userDao.checkPassFromEmail(registerUserDto.getMail(), registerUserDto.getPassword())) {
            UserEntity userEntity = userDao.findByEmail(registerUserDto.getMail());
            userEntity.setToken(generateNewToken());
            userDao.merge(userEntity);
            return userEntity.getToken();
        }
        else return null;
    }

}

