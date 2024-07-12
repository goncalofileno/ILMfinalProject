package com.ilm.projecto_ilm_backend.bean;


import com.ilm.projecto_ilm_backend.entity.SystemEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.ilm.projecto_ilm_backend.dao.SystemDao;


/**
 * Calculates the average number of users per project across all projects in the application.
 *
 * @return The average number of users per project as a double.
 */
@ApplicationScoped
public class SystemBean {

    @Inject
    SystemDao systemDao;

    /**
     * Ensures the existence of default system configurations. If certain configurations are not found,
     * they are created with default values. This method pre-populates the database with essential system
     * configurations such as timeout, maximum number of members, and default number of members.
     */
    public void createDefaultSystemIfNotExistent() {
        // Checks and sets the default value for the "timeout" configuration if not present
        if (systemDao.findConfigValueByName("timeout") == -1) {
            SystemEntity system = new SystemEntity();
            system.setName("timeout");
            system.setValue(24); // Default timeout value
            systemDao.persist(system);
        }
        // Checks and sets the default value for the "maxMaxMembers" configuration if not present
        if (systemDao.findConfigValueByName("maxMaxMembers") == -1) {
            SystemEntity system = new SystemEntity();
            system.setName("maxMaxMembers");
            system.setValue(30); // Default maximum number of members
            systemDao.persist(system);
        }
        // Checks and sets the default value for the "maxMembersDefault" configuration if not present
        if (systemDao.findConfigValueByName("maxMembersDefault") == -1) {
            SystemEntity system = new SystemEntity();
            system.setName("maxMembersDefault");
            system.setValue(4); // Default number of members
            systemDao.persist(system);
        }
    }
}
