package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.dto.user.RejectedIdsDto;
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

    /**
     * Finds a resource by the given name.
     *
     * @param name the name of the resource to be found.
     *             The name is unique for each resource.
     *             It is used to identify the resource.
     *             It is a string.
     */
    public int findIdByName(String name) {
        try {
            return em.createNamedQuery("Resource.findIdByName", Long.class).setParameter("name", name)
                    .getSingleResult().intValue();

        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Finds a resource by the given name.
     *
     * @param name the name of the resource to be found.
     *             The name is unique for each resource.
     *             It is used to identify the resource.
     *             It is a string.
     */
    public ResourceEntity findByName(String name) {
        try {
            return em.createNamedQuery("Resource.findByName", ResourceEntity.class).setParameter("name", name)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves detailed information about resources, supporting pagination, sorting, and filtering.
     * This method dynamically constructs a query based on provided parameters to fetch resources.
     *
     * @param page The page number for pagination.
     * @param resourcesPerPage The number of resources to display per page.
     * @param brand The brand of the resources to filter by.
     * @param type The type of the resources to filter by.
     * @param supplierId The ID of the supplier to filter resources by.
     * @param name The name of the resources to filter by.
     * @param nameAsc Sort order for resource names, true for ascending, false for descending.
     * @param typeAsc Sort order for resource types, true for ascending, false for descending.
     * @param brandAsc Sort order for brands, true for ascending, false for descending.
     * @param supplierAsc Sort order for suppliers, true for ascending, false for descending.
     * @param rejectedIds DTO containing IDs of resources to exclude from the results.
     * @return A list of Object arrays containing resource details.
     */
    public List<Object[]> getResourceDetails(int page, int resourcesPerPage, String brand, ResourceTypeENUM type, Integer supplierId, String name,
                                             String nameAsc, String typeAsc,String brandAsc, String supplierAsc, RejectedIdsDto rejectedIds) {

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
            queryString.append(" ORDER BY rs.supplier.name ").append(supplierAsc.equals("true") ? "ASC" : "DESC");
        }

        TypedQuery<Object[]> query = em.createQuery(queryString.toString(), Object[].class);
        query.setParameter("brand", brand);
        query.setParameter("type", type);
        query.setParameter("supplierId", supplierId);
        query.setParameter("name", name);
        query.setParameter("rejectedIds", rejectedIds.getRejectedIds());

        query.setFirstResult(resourcesPerPage * (page - 1));
        query.setMaxResults(resourcesPerPage);
        return query.getResultList();
    }

    /**
     * Calculates the total number of resources that match the given filters.
     *
     * @param brand The brand of the resources to filter by.
     * @param type The type of the resources to filter by.
     * @param supplierId The ID of the supplier to filter resources by.
     * @param name The name of the resources to filter by.
     * @param rejectedIds DTO containing IDs of resources to exclude from the count.
     * @return The total number of resources matching the filters.
     */
    public int getNumberOfResources(String brand, ResourceTypeENUM type, Integer supplierId, String name, RejectedIdsDto rejectedIds) {
        try {
            return  em.createNamedQuery("Resource.getNumberOfResourcesDetails", Long.class).setParameter("brand",brand).setParameter("type",type).setParameter("supplierId",supplierId).setParameter("name",name).setParameter("rejectedIds",rejectedIds.getRejectedIds()).getSingleResult().intValue();

        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Retrieves all unique brands of resources.
     *
     * @return A list of all brands.
     */
    public List<String> getAllBrands() {
        return em.createNamedQuery("Resource.getAllBrands", String.class).getResultList();
    }

    /**
     * Retrieves all unique names of resources.
     *
     * @return A list of all resource names.
     */
    public List<String> getAllNames() {
        return em.createNamedQuery("Resource.getNames", String.class).getResultList();
    }

    /**
     * Finds a resource by its detailed attributes.
     *
     * @param name The name of the resource.
     * @param brand The brand of the resource.
     * @param type The type of the resource.
     * @param supplierName The name of the supplier associated with the resource.
     * @return The found ResourceEntity or null if no matching resource is found.
     */
    public ResourceEntity findResourceByDetails(String name, String brand, ResourceTypeENUM type, String supplierName) {
        try {
            return em.createNamedQuery("Resource.findResourceByDetails", ResourceEntity.class)
                    .setParameter("name", name)
                    .setParameter("brand", brand)
                    .setParameter("type", type)
                    .setParameter("supplierName", supplierName)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Checks if a resource is associated with a given supplier.
     *
     * @param resourceId The ID of the resource.
     * @param supplierId The ID of the supplier.
     * @return true if the resource is associated with the supplier, false otherwise.
     */
    public boolean resourceHasSupplier(int resourceId, int supplierId) {
        return em.createNamedQuery("Resource.hasSupplier", Boolean.class)
                .setParameter("resourceId", resourceId)
                .setParameter("supplierId", supplierId)
                .getSingleResult();
    }

    /**
     * Checks if a resource exists with the given attributes.
     *
     * @param name The name of the resource.
     * @param brand The brand of the resource.
     * @param type The type of the resource.
     * @param supplierName The name of the supplier associated with the resource.
     * @return true if a resource with the given attributes exists, false otherwise.
     */
    public boolean doesResourceExist(String name, String brand, ResourceTypeENUM type, String supplierName) {
        return em.createNamedQuery("Resource.doesResourceExist", Boolean.class)
                .setParameter("name", name)
                .setParameter("brand", brand)
                .setParameter("type", type)
                .setParameter("supplierName", supplierName)
                .getSingleResult();
    }
    /**
     * Checks if a resource exists with the given attributes and a specific ID.
     *
     * @param name The name of the resource.
     * @param brand The brand of the resource.
     * @param type The type of the resource.
     * @param supplierName The name of the supplier associated with the resource.
     * @param id The ID of the resource to exclude from the check.
     * @return true if a resource with the given attributes exists excluding the specified ID, false otherwise.
     */
    public boolean doesResourceExistWithId(String name, String brand, ResourceTypeENUM type, String supplierName, int id) {
        return em.createNamedQuery("Resource.doesResourceExistWithId", Boolean.class)
                .setParameter("name", name)
                .setParameter("brand", brand)
                .setParameter("type", type)
                .setParameter("supplierName", supplierName)
                .setParameter("id", id)
                .getSingleResult();
    }

    /**
     * Retrieves resources associated with a specific project ID.
     *
     * @param projectId The ID of the project.
     * @return A list of Object arrays containing details of resources associated with the project.
     */
    public List<Object[]> getResourcesFromProjectId(int projectId) {
        try {
            return  em.createNamedQuery("Resource.getResourcesDetailsFromProjectId", Object[].class).setParameter("projectId",projectId).getResultList()   ;

        } catch (Exception e) {
            return null;
        }
    }

}
