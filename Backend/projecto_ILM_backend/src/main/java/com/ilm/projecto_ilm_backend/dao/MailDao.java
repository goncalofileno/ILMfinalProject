package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.MailEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
public class MailDao extends AbstractDao<MailEntity> {

    private static final long serialVersionUID = 1L;

    public MailDao() {
        super(MailEntity.class);
    }

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void persist(MailEntity mail) {
        em.persist(mail);
    }

    public List<MailEntity> getMailsReceivedByUserId(int userId) {
        return em.createNamedQuery("Mail.getMailsReceivedByUserId", MailEntity.class).setParameter("userId", userId).getResultList();
    }

    public List<MailEntity> getMailsSentByUserId(int userId) {
        return em.createNamedQuery("Mail.getMailsSentByUserId", MailEntity.class).setParameter("userId", userId).getResultList();
    }

    public MailEntity findById(int id) {
       try {
           return em.createNamedQuery("Mail.findById", MailEntity.class).setParameter("id", id).getSingleResult();
       } catch (Exception e) {
           return null;
       }
    }

    public int getUnreadNumber(int userId) {
        return em.createNamedQuery("Mail.getUnseenMailsCount", Long.class).setParameter("userId", userId).getSingleResult().intValue();
    }



}
