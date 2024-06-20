package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.entity.ResourceSupplierEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.entity.UserProjectEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
public class ResourceSupplierDao extends AbstractDao<ResourceSupplierEntity>{

    /**
     * The EntityManager instance used to interact with the persistence context.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The EntityManager instance used to interact with the persistence context.
     */
    @PersistenceContext
    EntityManager em;

    public ResourceSupplierDao() {
        super(ResourceSupplierEntity.class);
    }

    @Transactional
    public boolean updateIsDeleted(int resourceId, int supplierId, boolean isDeleted) {
        try {
            Query query = em.createNamedQuery("ResourceSupplier.updateIsDeleted");
            query.setParameter("resourceId", resourceId);
            query.setParameter("supplierId", supplierId);
            query.setParameter("isDeleted", isDeleted);
            query.executeUpdate();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean doesRelationExist(int resourceId, int supplierId) {
        try {
            Query query = em.createNamedQuery("ResourceSupplier.doesRelationshipExist");
            query.setParameter("resourceId", resourceId);
            query.setParameter("supplierId", supplierId);
            return (boolean) query.getSingleResult();
        }catch (Exception e){
            return false;
        }
    }

    public boolean getIsDeletedByIds(int resourceId, int supplierId) {
        try {
            Query query = em.createNamedQuery("ResourceSupplier.getIsDeletedByIds");
            query.setParameter("resourceId", resourceId);
            query.setParameter("supplierId", supplierId);
            return (boolean) query.getSingleResult();
        }catch (Exception e){
            return false;
        }
    }
}

