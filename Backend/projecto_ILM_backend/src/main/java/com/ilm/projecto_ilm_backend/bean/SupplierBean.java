package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.dao.SupplierDao;
import com.ilm.projecto_ilm_backend.entity.SupplierEntity;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * The SupplierBean class is responsible for managing SupplierEntity instances.
 * It is an application scoped bean, meaning there is a single instance for the entire application.
 */
@ApplicationScoped
public class SupplierBean {

    /**
     * The SupplierDao instance used for accessing the database.
     */
    @Inject
    SupplierDao supplierDao;

    /**
     * Creates default suppliers if they do not exist.
     * The suppliers are created with predefined values.
     */
    public void createDefaultSuppliersIfNotExistent() {
        if (supplierDao.findById(1) == null) {
            SupplierEntity supplier = new SupplierEntity();
            supplier.setName("Worten");
            supplier.setContact("210155222");

            supplierDao.persist(supplier);
        }

        if (supplierDao.findById(2) == null) {
            SupplierEntity supplier = new SupplierEntity();
            supplier.setName("Fnac");
            supplier.setContact("210155333");

            supplierDao.persist(supplier);
        }

        if (supplierDao.findById(3) == null) {
            SupplierEntity supplier = new SupplierEntity();
            supplier.setName("MediaMarkt");
            supplier.setContact("210155444");

            supplierDao.persist(supplier);
        }

        if (supplierDao.findById(4) == null) {
            SupplierEntity supplier = new SupplierEntity();
            supplier.setName("Radio Popular");
            supplier.setContact("210155555");

            supplierDao.persist(supplier);
        }
    }
}
