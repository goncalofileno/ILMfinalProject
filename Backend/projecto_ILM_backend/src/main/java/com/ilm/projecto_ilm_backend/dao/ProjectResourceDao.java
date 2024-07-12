package com.ilm.projecto_ilm_backend.dao;



import com.ilm.projecto_ilm_backend.entity.ProjectResourceEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
public class ProjectResourceDao  extends AbstractDao<ProjectResourceEntity>{

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    EntityManager em;

    /**
     * Constructs a new ProjectDao instance.
     */
    public ProjectResourceDao() {
        super(ProjectResourceEntity.class);
    }

    /**
     * Merges the state of the given project into the current persistence context.
     *
     * @param project the project to be merged.
     */
    @Transactional
    public void merge(ProjectResourceEntity project) {
        em.merge(project);
    }

    /**
     * Removes the project from the database.
     *
     *the project to be removed.
     */
    public void removeById(int id) {
        try {
             em.createNamedQuery("ProjectResourceEntity.deleteById").setParameter("id", id).executeUpdate();
        }catch
        (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a project by its unique identifier.
     *
     * @param id The unique identifier of the project.
     * @return The found ProjectEntity or null if no entity is found with the provided id.
     */
    public ProjectResourceEntity findById(int id) {
        try {
           return em.createNamedQuery("ProjectResourceEntity.findById", ProjectResourceEntity.class).setParameter("id", id).getSingleResult();
        }catch
        (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves all projects from the database.
     *
     * @return A list of all projects in the database.
     */
    public List<ProjectResourceEntity> findResourcesById(int id) {
        try {
            return em.createNamedQuery("ProjectResourceEntity.getByProjectId", ProjectResourceEntity.class).setParameter("id", id).getResultList();
        }catch
        (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
