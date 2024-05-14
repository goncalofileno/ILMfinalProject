package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.UserTypeENUM;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;

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
     * Creates a default user with username "admin" if it does not exist.
     * The user is created with predefined values.
     */
    public void createDefaultUsersIfNotExistent() {
        if (userDao.findByUsername("admin") == null) {
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
            user.setDeleted(false);
            user.setTutorial(false);
            userDao.persist(user);
        }
    }
}
