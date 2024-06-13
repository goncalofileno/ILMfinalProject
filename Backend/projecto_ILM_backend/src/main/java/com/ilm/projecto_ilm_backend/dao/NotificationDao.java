package com.ilm.projecto_ilm_backend.dao;

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
    public List<NotificationEntity> findByUserId(int userId) {
        return em.createNamedQuery("NotificationEntity.findByUserId", NotificationEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Transactional
    public void markNotificationAsRead(int userId, int notificationId) {
        em.createNamedQuery("NotificationEntity.markAsRead")
                .setParameter("userId", userId)
                .setParameter("notificationIds", List.of(notificationId))
                .executeUpdate();
    }
}
