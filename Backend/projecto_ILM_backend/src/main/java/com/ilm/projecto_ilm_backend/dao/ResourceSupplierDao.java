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

/**
 * Retrieves resources associated with a specific project ID.
 *
 * @param projectId The ID of the project.
 * @return A list of Object arrays containing details of resources associated with the project.
 */
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

    /**
     * Default constructor initializing with ResourceSupplierEntity class type.
     */
    public ResourceSupplierDao() {
        super(ResourceSupplierEntity.class);
    }

    /**
     * Updates the deletion status of a resource-supplier relationship.
     *
     * @param resourceId The ID of the resource.
     * @param supplierId The ID of the supplier.
     * @param isDeleted The deletion status to be set.
     * @return true if the operation was successful, false otherwise.
     */
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

    /**
     * Checks if a relationship between a resource and a supplier exists.
     *
     * @param resourceId The ID of the resource.
     * @param supplierId The ID of the supplier.
     * @return true if the relationship exists, false otherwise.
     */
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

    /**
     * Retrieves the deletion status of a resource-supplier relationship by their IDs.
     *
     * @param resourceId The ID of the resource.
     * @param supplierId The ID of the supplier.
     * @return true if the relationship is marked as deleted, false otherwise.
     */
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

    /**
     * Counts the number of resources associated with each supplier.
     *
     * @return A list of Object arrays, each containing a supplier ID and the count of associated resources.
     */
    public List<Object[]> countResourcesPerSupplier() {
        try {
          return   em.createNamedQuery("ResourceSupplier.countResourcesPerSupplier", Object[].class).getResultList();

        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception appropriately
            return null;
        }
    }
}

