package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.dto.project.HomeProjectDto;
import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The ProjectDao class provides database operations for ProjectEntity instances.
 * It extends the AbstractDao class to inherit common database operations.
 */
@Stateless
public class ProjectDao extends AbstractDao<ProjectEntity>{

    /**
     * The EntityManager instance used for performing database operations.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ProjectDao instance.
     */
    @PersistenceContext
    EntityManager em;

    /**
     * Constructs a new ProjectDao instance.
     */
    public ProjectDao() {
        super(ProjectEntity.class);
    }

    /**
     * Merges the state of the given project into the current persistence context.
     *
     * @param project the project to be merged.
     */
    @Transactional
    public void merge(ProjectEntity project) {
        em.merge(project);
    }

    /**
     * Finds a project by the given id.
     *
     * @param id the id of the project to be found.
     *           The id is unique for each project.
     *           It is used to identify the project.
     *           It is an integer.
     */
    public ProjectEntity findById(int id) {
        try {
            return em.createNamedQuery("Project.findById", ProjectEntity.class).setParameter("id", id)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<HomeProjectDto> findAllNamesAndDescriptionsHome() {
        TypedQuery<Object[]> query = em.createNamedQuery("Project.findNameAndDescriptionHome", Object[].class);
        List<Object[]> results = query.getResultList();

        ArrayList<HomeProjectDto> projects = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            projects.add(new HomeProjectDto((String) results.get(i)[0], (String) results.get(i)[1]));
        }
        return projects;
    }

    public List<Object[]> getProjectTableDtoInfo() {
        return em.createNamedQuery("Project.getProjectTableDtoInfo", Object[].class).getResultList();
    }
    public long countProjects() {
        try {
            return  em.createNamedQuery("Project.countProjects", Long.class)
                    .getSingleResult();

        } catch (Exception e) {
            return 0;
        }
    }
}