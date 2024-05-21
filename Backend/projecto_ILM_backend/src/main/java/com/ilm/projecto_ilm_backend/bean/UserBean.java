package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.UserTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.dao.InterestDao;
import com.ilm.projecto_ilm_backend.dao.LabDao;
import com.ilm.projecto_ilm_backend.dao.SkillDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dto.user.RegisterUserDto;
import com.ilm.projecto_ilm_backend.entity.InterestEntity;
import com.ilm.projecto_ilm_backend.entity.LabEntity;
import com.ilm.projecto_ilm_backend.entity.SkillEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.service.UserService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.time.LocalDateTime;
import java.util.List;

/**
 * The UserBean class is responsible for managing UserEntity instances.
 * It is an application scoped bean, meaning there is a single instance for the entire application.
 */
@ApplicationScoped
public class UserBean {

    /**
     * The UserDao instance used for accessing the database.
     */
    @EJB
    UserDao userDao;

    /**
     * The LabDao instance used for accessing the database.
     */
    @EJB
    LabDao labDao;

    /**
     * The InterestDao instance used for accessing the database.
     */
    @EJB
    InterestDao interestDao;

    /**
     * The SkillDao instance used for accessing the database.
     */
    @EJB
    SkillDao skillDao;

    /**
     * Creates a default user with username "admin" if it does not exist.
     * The user is created with predefined values.
     */
    public void createDefaultUsersIfNotExistent() {
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
            user.setPassword(registerUserDto.getPassword());
            user.setMailConfirmed(false);
            user.setProfileCreated(false);
            user.setDeleted(false);
            user.setTutorial(false);
            user.setRegistrationDate(LocalDateTime.now());
            user.setType(UserTypeENUM.STANDARD_USER);
            user.setPhoto("https://www.pngkey.com/png/full/114-1149878_setting-user-avatar-in-specific-size-without-breaking.png");

            userDao.persist(user);
            return true;
        } catch (Exception e) {
            // Log the exception
            Logger logger = LogManager.getLogger(UserService.class);
            logger.error("Error persisting user: ", e);
            return false;
        }
    }
}
