package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.entity.MailEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.EntityManager;
import com.ilm.projecto_ilm_backend.entity.SupplierEntity;
import jakarta.transaction.Transactional;

import java.util.List;

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

    /**
     * Finds the user ID associated with a given session ID.
     *
     * @param sessionId The session ID for which the user ID is to be found.
     * @return The user ID associated with the given session ID, or -1 if no such session exists.
     */
    public String findNameById(int id) {
        try {
            return em.createNamedQuery("Supplier.findNameById", String.class).setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds the ID of a supplier based on the supplier's name.
     * This method queries the database for the ID of the supplier with the specified name.
     * If the supplier is found, their ID is returned. If not found, -1 is returned.
     *
     * @param name The name of the supplier.
     * @return The ID of the supplier if found, -1 otherwise.
     */
    public int findIdByName(String name) {
        try {
            return em.createNamedQuery("Supplier.findIdByName", int.class).setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Retrieves all supplier names from the database.
     * This method queries the database for the names of all suppliers and returns them as a list.
     *
     * @return A list of all supplier names.
     */
    public List<String> getAllNames() {
        return em.createNamedQuery("Supplier.findAllNames", String.class).getResultList();
    }

    /**
     * Finds the contact information of a supplier based on the supplier's name.
     * This method queries the database for the contact information of the supplier with the specified name.
     * If the supplier is found, their contact information is returned. If not found, null is returned.
     *
     * @param supplierName The name of the supplier whose contact information is being requested.
     * @return The contact information of the supplier if found, null otherwise.
     */
    public String findSupplierContactByName(String supplierName){
        try {
            return em.createNamedQuery("Supplier.findSupplierContactByName", String.class)
                    .setParameter("name", supplierName)
                    .getSingleResult();
        }
        catch (Exception e){
            return null;
        }
    }
}
