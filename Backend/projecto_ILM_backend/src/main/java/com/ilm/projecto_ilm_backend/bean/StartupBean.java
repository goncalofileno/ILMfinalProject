package com.ilm.projecto_ilm_backend.bean;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

/**
 * The StartupBean class is a singleton bean that is instantiated upon application startup.
 * It is responsible for initializing the application.
 */
@Singleton
@Startup
public class StartupBean {

    /**
     * The UserBean instance used for managing UserEntity instances.
     */
    @Inject
    UserBean userBean;

    /**
     * This method is called after the bean is constructed and dependency injection is complete.
     * It creates default users if they do not exist.
     */
    @PostConstruct
    public void init() {
        userBean.createDefaultUsersIfNotExistent();
    }
}
