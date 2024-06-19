package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM;
import com.ilm.projecto_ilm_backend.entity.NotificationEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
public class NotificationDao extends AbstractDao<NotificationEntity> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    EntityManager em;

    public NotificationDao() {
        super(NotificationEntity.class);
    }

    @Transactional
    public void merge(NotificationEntity notification) {
        em.merge(notification);
    }

    @Transactional
    public void persist(NotificationEntity notification) {
        em.persist(notification);
    }

    @Transactional
    public void remove(NotificationEntity notification) {
        em.remove(notification);
    }

    @Transactional
    public NotificationEntity findById(int id) {
        return em.find(NotificationEntity.class, id);
    }

    @Transactional
    public List<NotificationEntity> findUnreadByUserId(int userId) {
        return em.createNamedQuery("NotificationEntity.findUnreadByUserId", NotificationEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Transactional
    public List<NotificationEntity> findReadByUserId(int userId, int page) {
        int offset = (page - 1) * 5;
        return em.createNamedQuery("NotificationEntity.findReadByUserId", NotificationEntity.class)
                .setParameter("userId", userId)
                .setFirstResult(offset)
                .setMaxResults(5)
                .getResultList();
    }

    @Transactional
    public void markNotificationsAsRead(List<NotificationEntity> notifications) {
        for (NotificationEntity notification : notifications) {
            notification.setReadStatus(true);
            em.merge(notification);
        }
    }

    @Transactional
    public int countUnreadByUserId(int userId) {
        Long count = em.createNamedQuery("NotificationEntity.countUnreadByUserId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
        return count.intValue();
    }

    @Transactional
    public int countAllByUserId(int userId) {
        Long count = em.createNamedQuery("NotificationEntity.countAllByUserId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
        return count.intValue();
    }

    @Transactional
    public String findSystemUsernameOfCreatorByReceptorAndType(int receptorId, NotificationTypeENUM type) {
        return em.createNamedQuery("NotificationEntity.findSystemUsernameOfCreatorByReceptorAndType", String.class)
                .setParameter("receptorId", receptorId)
                .setParameter("type", type)
                .getSingleResult();
    }
}
