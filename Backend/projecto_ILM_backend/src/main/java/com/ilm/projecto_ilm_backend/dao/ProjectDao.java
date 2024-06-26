package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dto.project.HomeProjectDto;
import com.ilm.projecto_ilm_backend.entity.LabEntity;
import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import com.ilm.projecto_ilm_backend.entity.SkillEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

    public List<Object[]> getProjectTableDtoInfo(int page, int projectsPerPage, LabEntity lab, StateProjectENUM status, boolean slotsAvailable, String nameAsc,
                                                 String  statusAsc,
                                                 String  labAsc,
                                                 String  startDateAsc,
                                                 String endDateAsc, String keyword) {
        String baseQueryString = em
                .createNamedQuery("Project.getProjectTableDtoInfo", Object[].class)
                .unwrap(org.hibernate.query.Query.class)
                .getQueryString();

        // Append the ORDER BY clause dynamically
        StringBuilder queryString = new StringBuilder(baseQueryString);

//        if(lab==null && status==null && !slotsAvailable && (nameAsc==null || nameAsc.equals("")) && (statusAsc==null || statusAsc.equals("")) && (labAsc==null || labAsc.equals("")) && (startDateAsc==null || startDateAsc.equals("")) && (endDateAsc==null || endDateAsc.equals("")) && (keyword==null || keyword.equals(""))){
//            queryString.append(", up.user.id HAVING (:slotsAvailable = FALSE OR p.maxMembers > COUNT(up)) ORDER BY CASE WHEN up.user.id = :userId THEN 0 ELSE 1 END, p.name");
//        }else {
//            queryString.append("HAVING (:slotsAvailable = FALSE OR p.maxMembers > COUNT(up))");
//        }
        if (nameAsc != null && !nameAsc.equals("")) {
            queryString.append(" ORDER BY p.name ").append(nameAsc.equals("true") ? "ASC" : "DESC");
        } else if (statusAsc != null && !statusAsc.equals("")) {
            queryString.append(" ORDER BY p.status ").append(statusAsc.equals("true") ? "ASC" : "DESC");
        } else if (labAsc != null && !labAsc.equals("")) {
            queryString.append(" ORDER BY p.lab ").append(labAsc.equals("true") ? "ASC" : "DESC");
        } else if (startDateAsc != null && !startDateAsc.equals("")) {
            queryString.append(" ORDER BY p.startDate ").append(startDateAsc.equals("true") ? "ASC" : "DESC");
        } else if (endDateAsc != null && !endDateAsc.equals("")) {
            queryString.append(" ORDER BY p.endDate ").append(endDateAsc.equals("true") ? "ASC" : "DESC");
        }

        // Create the query using the dynamically constructed query string
        TypedQuery<Object[]> query = em.createQuery(queryString.toString(), Object[].class);

        // Set parameters
        query.setParameter("lab", lab);
        query.setParameter("status", status);
        query.setParameter("slotsAvailable", slotsAvailable);
        query.setParameter("keyword", keyword);

//        if(lab==null && status==null && !slotsAvailable && (nameAsc==null || nameAsc.equals("")) && (statusAsc==null || statusAsc.equals("")) && (labAsc==null || labAsc.equals("")) && (startDateAsc==null || startDateAsc.equals("")) && (endDateAsc==null || endDateAsc.equals("")) && (keyword==null || keyword.equals(""))) {
//            query.setParameter("userId", userId);
//        }

        // Set pagination
        query.setFirstResult(projectsPerPage * (page - 1));
        query.setMaxResults(projectsPerPage);

        return query.getResultList();

    }




    public List<Object[]> getMyProjectsDtoInfo(int page, int projectsPerPage, LabEntity lab, StateProjectENUM status,
                                               String keyword, int userId, UserInProjectTypeENUM type) {

        String baseQueryString = em
                .createNamedQuery("Project.getMyProjectsInfo", Object[].class)
                .unwrap(org.hibernate.query.Query.class)
                .getQueryString();

        // Append the ORDER BY clause dynamically
        StringBuilder queryString = new StringBuilder(baseQueryString);

        if (type != null && !type.equals("")) {
            if(type==UserInProjectTypeENUM.MEMBER) queryString.append(" AND (up.type = 2 OR up.type = 3 OR up.type = 4) ");
            else queryString.append(" AND (up.type = :type) ");
        }

        queryString.append("GROUP BY p.id, p.name, p.lab, p.status, p.startDate, p.endDate, p.maxMembers,up.type " +
                "ORDER BY CASE " +
                "WHEN up.type = 0 THEN 0 " +  // CREATOR first
                "WHEN up.type = 1 THEN 1 " +  // MANAGER second
                "ELSE 2 END, " +
                "p.createdAt DESC");


        TypedQuery<Object[]> query = em.createQuery(queryString.toString(), Object[].class);
        // Set parameters
        query.setParameter("lab", lab);
        query.setParameter("status", status);

        query.setParameter("keyword", keyword);
        query.setParameter("userId", userId);
        if (type != null && !type.equals("") && type!=UserInProjectTypeENUM.MEMBER) {
            query.setParameter("type", type);
        }


        // Set pagination
        query.setFirstResult(projectsPerPage * (page - 1));
        query.setMaxResults(projectsPerPage);

        return query.getResultList();

    }

    public List<Object[]> findAllProjectsOrderedByUser(int page, int projectsPerPage, int userId) {
        TypedQuery<Object[]> query = em.createNamedQuery("Project.findAllProjectsOrderedByUser", Object[].class);
        query.setParameter("userId", userId);
        query.setFirstResult(projectsPerPage * (page - 1));
        query.setMaxResults(projectsPerPage);
        return query.getResultList();
    }

    public long countProjects() {
        try {
            return  em.createNamedQuery("Project.countProjects", Long.class)
                    .getSingleResult();

        } catch (Exception e) {
            return 0;
        }
    }

    public int getNumberOfProjectsTableDtoInfo(LabEntity lab, StateProjectENUM status, boolean slotsAvailable, String keyword) {
        try {


            return  em.createNamedQuery("Project.getNumberOfProjectsTableDtoInfo", Long.class).setParameter("lab",lab).setParameter("status",status).setParameter("slotsAvailable",slotsAvailable)
                    .setParameter("keyword",keyword).getSingleResult().intValue();

        } catch (Exception e) {
            return 0;
        }
    }

    public int getNumberOfMyProjectsDtoInfo(LabEntity lab, StateProjectENUM status, String keyword, int userId,UserInProjectTypeENUM type) {
        try {

            String baseQueryString = em
                    .createNamedQuery("Project.getNumberOfMyProjectsInfo", Long.class)
                    .unwrap(org.hibernate.query.Query.class)
                    .getQueryString();

            // Append the ORDER BY clause dynamically
            StringBuilder queryString = new StringBuilder(baseQueryString);

            if (type != null && !type.equals("")) {
                if(type==UserInProjectTypeENUM.MEMBER) queryString.append(" AND (up.type = 2 OR up.type = 3 OR up.type = 4) ");
                else queryString.append(" AND (up.type = :type) ");
            }

            TypedQuery<Long> query = em.createQuery(queryString.toString(), Long.class);
            // Set parameters
            query.setParameter("lab", lab);
            query.setParameter("status", status);

            query.setParameter("keyword", keyword);
            query.setParameter("userId", userId);
            if (type != null && !type.equals("") && type!=UserInProjectTypeENUM.MEMBER) {
                query.setParameter("type", type);
            }
            return  query.getSingleResult().intValue();

        } catch (Exception e) {
            return 0;
        }
    }


    public ProjectEntity findByName(String name) {
        return em.createQuery("SELECT p FROM ProjectEntity p WHERE p.name = :name", ProjectEntity.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    //retorna o nome do projeto procurando pelo systemName
    public String findNameBySystemName(String systemName) {
        return em.createQuery("SELECT p.name FROM ProjectEntity p WHERE p.systemName = :systemName", String.class)
                .setParameter("systemName", systemName)
                .getSingleResult();
    }

    public ProjectEntity findBySystemName(String systemName) {
        try {
            return em.createQuery("SELECT p FROM ProjectEntity p WHERE p.systemName = :systemName", ProjectEntity.class)
                    .setParameter("systemName", systemName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<String> getSkillsBySystemName(String projectSystemName) {
        TypedQuery<String> query = em.createNamedQuery("Project.getSkillsBySystemName", String.class);
        query.setParameter("projectSystemName", projectSystemName);
        return query.getResultList();
    }

    public boolean isSkillInProject(String projectSystemName, String skillName) {
        TypedQuery<Boolean> query = em.createNamedQuery("Project.isSkillInProject", Boolean.class);
        query.setParameter("projectSystemName", projectSystemName);
        query.setParameter("skillName", skillName);
        return query.getSingleResult();
    }

    public boolean doesProjectExists(String name) {
        try {
            return em.createNamedQuery("Project.doesProjectExist", Boolean.class).setParameter("name", name)
                    .getSingleResult();

        } catch (Exception e) {
            return false;
        }
    }
}