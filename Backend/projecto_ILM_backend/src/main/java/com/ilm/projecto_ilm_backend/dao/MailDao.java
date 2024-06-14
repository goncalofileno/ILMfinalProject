package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.MailEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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

    public List<MailEntity> getMailsReceivedByUserId(int userId, int page, int pageSize, boolean unread) {
        int firstResult = (page - 1) * pageSize;
        String query = "SELECT m FROM MailEntity m WHERE m.receiver.id = :userId AND m.deleted = false";
        if (unread) {
            query += " AND m.seen = false";
        }
        query += " ORDER BY m.date DESC";
        return em.createQuery(query, MailEntity.class)
                .setParameter("userId", userId)
                .setFirstResult(firstResult)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public int getTotalMailsReceivedByUserId(int userId, boolean unread) {
        String query = "SELECT COUNT(m) FROM MailEntity m WHERE m.receiver.id = :userId AND m.deleted = false";
        if (unread) {
            query += " AND m.seen = false";
        }
        Long count = em.createQuery(query, Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
        return count.intValue();
    }

    public List<MailEntity> searchMails(int userId, String query, int page, int pageSize, boolean unread) {
        String searchQuery = "%" + query + "%";
        StringBuilder queryBuilder = new StringBuilder("SELECT m FROM MailEntity m WHERE m.receiver.id = :userId AND m.deleted = false AND (");
        queryBuilder.append("m.subject LIKE :query OR ")
                .append("m.text LIKE :query OR ")
                .append("m.sender.firstName LIKE :query OR ")
                .append("m.sender.lastName LIKE :query OR ")
                .append("m.sender.email LIKE :query)");

        if (unread) {
            queryBuilder.append(" AND m.seen = false");
        }

        queryBuilder.append(" ORDER BY m.date DESC");

        TypedQuery<MailEntity> q = em.createQuery(queryBuilder.toString(), MailEntity.class);
        q.setParameter("userId", userId);
        q.setParameter("query", searchQuery);
        q.setFirstResult((page - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }

    public int getTotalSearchResults(int userId, String query, boolean unread) {
        String searchQuery = "%" + query + "%";
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(m) FROM MailEntity m WHERE m.receiver.id = :userId AND m.deleted = false AND (");
        queryBuilder.append("m.subject LIKE :query OR ")
                .append("m.text LIKE :query OR ")
                .append("m.sender.firstName LIKE :query OR ")
                .append("m.sender.lastName LIKE :query OR ")
                .append("m.sender.email LIKE :query)");

        if (unread) {
            queryBuilder.append(" AND m.seen = false");
        }

        TypedQuery<Long> q = em.createQuery(queryBuilder.toString(), Long.class);
        q.setParameter("userId", userId);
        q.setParameter("query", searchQuery);

        return q.getSingleResult().intValue();
    }

    public List<MailEntity> searchSentMails(int userId, String query, int page, int pageSize) {
        String searchQuery = "%" + query + "%";
        TypedQuery<MailEntity> q = em.createQuery(
                "SELECT m FROM MailEntity m WHERE m.sender.id = :userId AND m.deleted = false AND " +
                        "(m.subject LIKE :query OR " +
                        "m.text LIKE :query OR " +
                        "m.receiver.firstName LIKE :query OR " +
                        "m.receiver.lastName LIKE :query OR " +
                        "m.receiver.email LIKE :query)", MailEntity.class);
        q.setParameter("userId", userId);
        q.setParameter("query", searchQuery);
        q.setFirstResult((page - 1) * pageSize);
        q.setMaxResults(pageSize);
        return q.getResultList();
    }

    public int getTotalSentSearchResults(int userId, String query) {
        String searchQuery = "%" + query + "%";
        TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(m) FROM MailEntity m WHERE m.sender.id = :userId AND m.deleted = false AND " +
                        "(m.subject LIKE :query OR " +
                        "m.text LIKE :query OR " +
                        "m.receiver.firstName LIKE :query OR " +
                        "m.receiver.lastName LIKE :query OR " +
                        "m.receiver.email LIKE :query)", Long.class);
        q.setParameter("userId", userId);
        q.setParameter("query", searchQuery);
        return q.getSingleResult().intValue();
    }

    public List<MailEntity> getMailsSentByUserId(int userId, int page, int pageSize) {
        TypedQuery<MailEntity> q = em.createQuery(
                "SELECT m FROM MailEntity m WHERE m.sender.id = :userId AND m.deleted = false ORDER BY m.date DESC",
                MailEntity.class);
        q.setParameter("userId", userId);
        q.setFirstResult((page - 1) * pageSize);
        q.setMaxResults(pageSize);
        return q.getResultList();
    }

    public int getTotalMailsSentByUserId(int userId) {
        TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(m) FROM MailEntity m WHERE m.sender.id = :userId AND m.deleted = false",
                Long.class);
        q.setParameter("userId", userId);
        return q.getSingleResult().intValue();
    }
}
