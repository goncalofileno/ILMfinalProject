package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.MessageEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
public class MessageDao extends AbstractDao <MessageEntity>{

    private static final long serialVersionUID = 1L;

    public MessageDao() {
        super(MessageEntity.class);
    }

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void persist(MessageEntity message) {
        em.persist(message);
    }

    public MessageEntity findById(int id) {
        try {
            return em.createNamedQuery("Message.findById", MessageEntity.class).setParameter("id", id).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<MessageEntity> findByProjectId(int projectId) {
        try {
            return em.createNamedQuery("Message.findByProjectId", MessageEntity.class).setParameter("projectId", projectId).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
