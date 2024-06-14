package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.entity.LabEntity;
import jakarta.ejb.Stateless;
import com.ilm.projecto_ilm_backend.entity.ResourceEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;

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

    public List<Object[]> getResourceDetails(int page, int resourcesPerPage, String brand, ResourceTypeENUM type, Integer supplierId, String name,
                                             String nameAsc, String typeAsc,String brandAsc, String supplierAsc){

        String baseQueryString = em
                .createNamedQuery("Resource.getResourcesDetails", Object[].class)
                .unwrap(org.hibernate.query.Query.class)
                .getQueryString();

        StringBuilder queryString = new StringBuilder(baseQueryString);

        if (nameAsc != null && !nameAsc.equals("")) {
            queryString.append(" ORDER BY r.name ").append(nameAsc.equals("true") ? "ASC" : "DESC");
        } else if (typeAsc != null && !typeAsc.equals("")) {
            queryString.append(" ORDER BY r.type ").append(typeAsc.equals("true") ? "ASC" : "DESC");
        } else if (brandAsc != null && !brandAsc.equals("")) {
            queryString.append(" ORDER BY r.brand ").append(brandAsc.equals("true") ? "ASC" : "DESC");
        } else if (supplierAsc != null && !supplierAsc.equals("")) {
            queryString.append(" ORDER BY s.name ").append(supplierAsc.equals("true") ? "ASC" : "DESC");
        }

        TypedQuery<Object[]> query = em.createQuery(queryString.toString(), Object[].class);
        query.setParameter("brand", brand);
        query.setParameter("type", type);
        query.setParameter("supplierId", supplierId);
        query.setParameter("name", name);

        query.setFirstResult(resourcesPerPage * (page - 1));
        query.setMaxResults(resourcesPerPage);
        return query.getResultList();
    }

    public int getNumberOfResources(String brand, ResourceTypeENUM type, Integer supplierId, String name) {
        try {
            return  em.createNamedQuery("Resource.getNumberOfResourcesDetails", Long.class).setParameter("brand",brand).setParameter("type",type).setParameter("supplierId",supplierId).setParameter("name",name).getSingleResult().intValue();

        } catch (Exception e) {
            return 0;
        }
    }

    public List<String> getAllBrands() {
        return em.createNamedQuery("Resource.getAllBrands", String.class).getResultList();
    }

}
