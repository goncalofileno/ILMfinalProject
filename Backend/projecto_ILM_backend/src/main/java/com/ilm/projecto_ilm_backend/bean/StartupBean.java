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
     * The LabBean instance used for managing LabEntity instances.
     */
    @Inject
    LabBean labBean;

    /**
     * The InterestBean instance used for managing InterestEntity instances.
     */
    @Inject
    InterestBean interestBean;

    /**
     * The SkillBean instance used for managing SkillEntity instances.
     */
    @Inject
    SkillBean skillBean;

    /**
     * The SupplierBean instance used for managing SupplierEntity instances.
     */
    @Inject
    SupplierBean supplierBean;

    @Inject
    ResourceBean resourceBean;

    /**
     * This method is called after the bean is constructed and dependency injection is complete.
     * It creates default users, labs, interests, skills, suppliers, resources if they do not exist.
     */
    @PostConstruct
    public void init() {
        labBean.createDefaultLabsIfNotExistent();
        skillBean.createDefaultSkillsIfNotExistent();
        interestBean.createDefaultInterestsIfNotExistent();
        supplierBean.createDefaultSuppliersIfNotExistent();
        resourceBean.createDefaultResourcesIfNotExistent();

        userBean.createDefaultUsersIfNotExistent();
    }
}
