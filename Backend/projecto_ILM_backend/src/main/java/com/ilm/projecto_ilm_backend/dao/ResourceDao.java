package com.ilm.projecto_ilm_backend.dao;

import jakarta.ejb.Stateless;
import com.ilm.projecto_ilm_backend.entity.ResourceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * The ResourceDao class provides database operations for ResourceEntity instances.
 * It extends the AbstractDao class to inherit common database operations.
 */
@Stateless
public class ResourceDao extends AbstractDao<ResourceEntity> {


    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ResourceDao instance.
     */
    public ResourceDao() {
        super(ResourceEntity.class);
    }

    /**
     * Merges the state of the given resource into the current persistence context.
     *
     * @param resource the resource to be merged.
     */
    @Transactional
    public void merge(ResourceEntity resource) {
        em.merge(resource);
    }

    /**
     * Finds a resource by the given id.
     *
     * @param id the id of the resource to be found.
     *           The id is unique for each resource.
     *           It is used to identify the resource.
     *           It is an integer.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Finds a resource by the given id.
     *
     * @param id the id of the resource to be found.
     *           The id is unique for each resource.
     *           It is used to identify the resource.
     *           It is an integer.
     */
    public ResourceEntity findById(int id) {
        try {
            return em.createNamedQuery("Resource.findById", ResourceEntity.class).setParameter("id", id)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }


}
