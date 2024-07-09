package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.NotificationTypeENUM;
import com.ilm.projecto_ilm_backend.entity.NotificationEntity;
import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
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
        Long countNonProjectMessages = em.createNamedQuery("NotificationEntity.countNonProjectMessageUnreadByUserId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();

        Long countDistinctProjectMessages = em.createNamedQuery("NotificationEntity.countDistinctProjectMessageUnreadByUserId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();

        return countNonProjectMessages.intValue() + countDistinctProjectMessages.intValue();
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

    @Transactional
    public void markMessageNotificationClicked(int userId, List<Integer> notificationIds) {
        for (Integer notificationId : notificationIds) {
            NotificationEntity notification = findById(notificationId);
            if (notification.getReceptor().getId() == userId) {
                notification.setMessageNotificationClicked(true);
                em.merge(notification);
            }
        }
    }

    @Transactional
    public void markAllNotificationsClicked(int userId, String projectSystemName) {
        List<NotificationEntity> notifications = em.createNamedQuery("NotificationEntity.findUnreadByUserIdAndProjectId", NotificationEntity.class)
                .setParameter("userId", userId)
                .setParameter("projectSystemName", projectSystemName)
                .getResultList();

        for (NotificationEntity notification : notifications) {
            notification.setMessageNotificationClicked(true);
            em.merge(notification);
        }
    }

    @Transactional
    public void removeByProjectIdAndReceptorAndType(String projectSystemName, int receptorId, NotificationTypeENUM type) {
        em.createNamedQuery("NotificationEntity.removeByProjectIdAndReceptorAndType")
                .setParameter("projectSystemName", projectSystemName)
                .setParameter("receptorId", receptorId)
                .setParameter("type", type)
                .executeUpdate();
    }

    public List<NotificationEntity> findByProjectSystemName(String projectSystemName) {
        return em.createNamedQuery("NotificationEntity.findByProjectSystemName", NotificationEntity.class)
                .setParameter("projectSystemName", projectSystemName)
                .getResultList();
    }

    public boolean findDoubleNotificationTask(String taskTitle, String projectSystemName, String systemUsername, UserEntity receptor, LocalDateTime date){
        try {
            em.createNamedQuery("NotificationEntity.findDoubleNotificationTask", NotificationEntity.class)
                    .setParameter("taskTitle", taskTitle)
                    .setParameter("projectSystemName", projectSystemName)
                    .setParameter("systemUsername", systemUsername)
                    .setParameter("receptor", receptor)
                    .setParameter("datePlus", date)
                    .setParameter("dateMinus", date.minusSeconds(1))
                    .getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
