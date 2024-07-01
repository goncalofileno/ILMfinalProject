package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.UserInTaskTypeENUM;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.entity.UserTaskEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

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

    public List<UserEntity> findUsersByTaskId(int taskId) {
        try {
            return em.createNamedQuery("UserTask.findUsersByTaskId", UserEntity.class).setParameter("taskId", taskId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public UserInTaskTypeENUM findUserTypeByTaskIdAndUserId(int taskId, int userId) {
        try {
            return em.createNamedQuery("UserTask.findUserTypeByTaskIdAndUserId", UserInTaskTypeENUM.class)
                    .setParameter("taskId", taskId).setParameter("userId", userId).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}