package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.dao.InterestDao;
import com.ilm.projecto_ilm_backend.entity.InterestEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ejb.EJB;
import jakarta.transaction.Transactional;

/**
 * The InterestBean class is responsible for managing InterestEntity instances.
 * It is an application scoped bean, meaning there is a single instance for the entire application.
 */
@ApplicationScoped
public class InterestBean {

    /**
     * The InterestDao instance used for accessing the database.
     */
    @EJB
    InterestDao interestDao;

    /**
     * Creates default interests if they do not exist.
     * The interests are created with predefined values for name and deleted status.
     */
    public void createDefaultInterestsIfNotExistent() {
        if (interestDao.findById(1) == null) {
            InterestEntity interest = new InterestEntity();
            interest.setName("Health");
            interest.setDeleted(false);

            interestDao.persist(interest);
        }
        if (interestDao.findById(2) == null) {
            InterestEntity interest = new InterestEntity();
            interest.setName("Animals");
            interest.setDeleted(false);

            interestDao.persist(interest);
        }
        if (interestDao.findById(3) == null) {
            InterestEntity interest = new InterestEntity();
            interest.setName("Nature");
            interest.setDeleted(false);

            interestDao.persist(interest);
        }
        if (interestDao.findById(4) == null) {
            InterestEntity interest = new InterestEntity();
            interest.setName("Mathematics");
            interest.setDeleted(false);

            interestDao.persist(interest);
        }
        if (interestDao.findById(5) == null) {
            InterestEntity interest = new InterestEntity();
            interest.setName("Physics");
            interest.setDeleted(false);

            interestDao.persist(interest);
        }
        if (interestDao.findById(6) == null) {
            InterestEntity interest = new InterestEntity();
            interest.setName("Cybersecurity");
            interest.setDeleted(false);

            interestDao.persist(interest);
        }
        if (interestDao.findById(7) == null) {
            InterestEntity interest = new InterestEntity();
            interest.setName("Artificial Intelligence");
            interest.setDeleted(false);

            interestDao.persist(interest);
        }
    }
}
