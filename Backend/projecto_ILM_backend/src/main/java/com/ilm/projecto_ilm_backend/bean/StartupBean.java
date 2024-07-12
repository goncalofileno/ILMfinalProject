package com.ilm.projecto_ilm_backend.bean;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

import java.io.UnsupportedEncodingException;

/**
 * Initializes the application with default data upon startup. This bean is responsible for populating the database
 * with default entities such as labs, skills, interests, suppliers, resources, users, projects, tasks, system configurations,
 * mails, notifications, logs, notes, and messages. It ensures that the application has a basic dataset to work with
 * right after deployment.
 */
@Singleton
@Startup
public class StartupBean {

    @Inject
    UserBean userBean;

    @Inject
    LabBean labBean;

    @Inject
    InterestBean interestBean;

    @Inject
    SkillBean skillBean;

    @Inject
    SupplierBean supplierBean;

    @Inject
    ResourceBean resourceBean;

    @Inject
    ProjectBean projectBean;

    @Inject
    SystemBean systemBean;

    @Inject
    MailBean mailBean;

    @Inject
    NotificationBean notificationBean;

    @Inject
    LogBean logBean;

    @Inject
    NoteBean noteBean;

    @Inject
    TaskBean taskBean;

    @Inject
    MessageBean messageBean;

    /**
     * Called after the bean's construction and transaction start. This method triggers the creation of default entities
     * across various beans to ensure the application is pre-populated with essential data. It handles the initialization
     * of labs, skills, interests, suppliers, resources, users, projects, tasks, system configurations, mails,
     * notifications, logs, notes, and messages.
     *
     * @throws MessagingException If there is an issue with mail setup during initialization.
     * @throws UnsupportedEncodingException If encoding issues occur during initialization.
     */
    @PostConstruct
    @Transactional
    public void init() throws MessagingException, UnsupportedEncodingException {
        labBean.createDefaultLabsIfNotExistent();
        skillBean.createDefaultSkillsIfNotExistent();
        interestBean.createDefaultInterestsIfNotExistent();
        supplierBean.createDefaultSuppliersIfNotExistent();
        resourceBean.createDefaultResourcesIfNotExistent();
        userBean.createDefaultUsersIfNotExistent();
        projectBean.createDefaultProjectsIfNotExistent();
        projectBean.createDefaultUsersInProjectIfNotExistent();
        taskBean.createDefaultTasksIfNotExistent();
        systemBean.createDefaultSystemIfNotExistent();
        mailBean.createDefaultMailsIfNotExistent();
        notificationBean.createDefaultNotificationsIfNotExistent();
        logBean.createDefaultLogsIfNotExistent();
        noteBean.createDefaultNotesIfNotExistent();
        messageBean.createDefaultMessagesIfNotExistent();

    }
}
