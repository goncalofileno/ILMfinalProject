package com.ilm.projecto_ilm_backend.bean;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

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

    @PostConstruct
    public void init() throws MessagingException, UnsupportedEncodingException {
        labBean.createDefaultLabsIfNotExistent();
        skillBean.createDefaultSkillsIfNotExistent();
        interestBean.createDefaultInterestsIfNotExistent();
        supplierBean.createDefaultSuppliersIfNotExistent();
        resourceBean.createDefaultResourcesIfNotExistent();
        userBean.createDefaultUsersIfNotExistent();
        projectBean.createDefaultProjectsIfNotExistent();
        projectBean.createDefaultUsersInProjectIfNotExistent();
        projectBean.createDefaultTasksIfNotExistent();
        systemBean.createDefaultSystemIfNotExistent();
        mailBean.createDefaultMailsIfNotExistent();
        notificationBean.createDefaultNotificationsIfNotExistent();
        logBean.createDefaultLogsIfNotExistent();
        noteBean.createDefaultNotesIfNotExistent();
    }
}
