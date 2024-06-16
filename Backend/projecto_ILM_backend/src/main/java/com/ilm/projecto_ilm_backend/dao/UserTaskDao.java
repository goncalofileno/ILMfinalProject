package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.UserTaskEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Stateless
public class UserTaskDao extends AbstractDao<UserTaskEntity> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager em;

    public UserTaskDao() {
        super(UserTaskEntity.class);
    }

    @Transactional
    public void persist(UserTaskEntity userTask) {
        em.persist(userTask);
    }

    @Transactional
    public void merge(UserTaskEntity userTask) {
        em.merge(userTask);
    }

    public UserTaskEntity findById(int id) {
        try {
            return em.createNamedQuery("UserTask.findById", UserTaskEntity.class).setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}