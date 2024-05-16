package com.ilm.projecto_ilm_backend.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import com.ilm.projecto_ilm_backend.entity.SupplierEntity;
import jakarta.transaction.Transactional;

/**
 * The SupplierDao class provides database operations for SupplierEntity instances.
 * It extends the AbstractDao class to inherit common database operations.
 */
@Stateless
public class SupplierDao extends AbstractDao<SupplierEntity> {


    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new SupplierDao instance.
     */
    public SupplierDao() {
        super(SupplierEntity.class);
    }

    /**
     * The EntityManager instance used for performing database operations.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Merges the state of the given supplier into the current persistence context.
     *
     * @param supplier the supplier to be merged.
     */
    @Transactional
    public void merge(SupplierEntity supplier) {
        em.merge(supplier);
    }

    /**
     * Finds a supplier by the given name.
     *
     * @param name the name of the supplier to be found.
     *             The name is unique for each supplier.
     *             It is used to identify the supplier.
     *             It is a string.
     */
    public SupplierEntity findByName(String name) {
        try {
            return em.createNamedQuery("Supplier.findByName", SupplierEntity.class).setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a supplier by the given id.
     *
     * @param id the id of the supplier to be found.
     *           The id is unique for each supplier.
     *           It is used to identify the supplier.
     *           It is an integer.
     */
    public SupplierEntity findById(int id) {
        try {
            return em.createNamedQuery("Supplier.findById", SupplierEntity.class).setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
