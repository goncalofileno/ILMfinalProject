package com.ilm.projecto_ilm_backend.dao;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.entity.LabEntity;
import com.ilm.projecto_ilm_backend.entity.SkillEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Provides database operations for UserEntity instances.
 * This class extends AbstractDao to inherit common database operations and is marked as Stateless
 * to indicate that it does not maintain any conversational state with clients.
 */
@Stateless
public class UserDao extends AbstractDao<UserEntity> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext
    private EntityManager em;

    public UserDao() {
        super(UserEntity.class);
    }

    /**
     * Merges the state of the given user entity into the current persistence context.
     *
     * @param user The user entity to merge.
     */
    @Transactional
    public void merge(UserEntity user) {
        em.merge(user);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user.
     * @return The found UserEntity or null if no user with the given email exists.
     */
    public UserEntity findByEmail(String email) {
        try {
            return em.createNamedQuery("User.findByEmail", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a user by their unique identifier.
     *
     * @param id The unique identifier of the user.
     * @return The found UserEntity or null if no user with the given ID exists.
     */
    public UserEntity findById(int id) {
        try {
            return em.createNamedQuery("User.findById", UserEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user.
     * @return The found UserEntity or null if no user with the given username exists.
     */
    public UserEntity findByUsername(String username) {
        try {
            return em.createNamedQuery("User.findByUsername", UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Finds a user by their auxiliary token.
     *
     * @param auxiliarToken The auxiliary token associated with the user.
     * @return The found UserEntity or null if no user with the given token exists.
     */
    public UserEntity findByAuxiliarToken(String auxiliarToken) {
        try {
            return em.createNamedQuery("User.findByAuxiliarToken", UserEntity.class)
                    .setParameter("auxiliarToken", auxiliarToken)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Checks if the provided password matches the one stored for the user with the given email.
     *
     * @param email    The email of the user.
     * @param password The password to check.
     * @return true if the password matches, false otherwise.
     */
    public boolean checkPassFromEmail(String email, String password) {
        try {
            String pass = (String) em.createNamedQuery("User.checkPassFromEmail")
                    .setParameter("email", email)
                    .getSingleResult();
            return pass.equals(password);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a system username exists in the database.
     *
     * @param systemUsername The system username to check.
     * @return true if the system username exists, false otherwise.
     */
    public boolean checkSystemUsername(String systemUsername) {
        try {
            return em.createNamedQuery("User.checkSystemUsername")
                    .setParameter("systemUsername", systemUsername)
                    .getSingleResult() != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Finds a user by their system username.
     *
     * @param systemUsername The system username of the user.
     * @return The found UserEntity or null if no user with the given system username exists.
     */
    public UserEntity findBySystemUsername(String systemUsername) {
        try {
            return em.createNamedQuery("User.findBySystemUsername", UserEntity.class)
                    .setParameter("systemUsername", systemUsername)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves a user by their system username, including their projects, skills, and interests.
     *
     * @param systemUsername The system username of the user.
     * @return The UserEntity with initialized collections or null if no user is found.
     */
    public UserEntity getUserBySystemUsernameWithAssociations(String systemUsername) {
        UserEntity user = em.createQuery("SELECT u FROM UserEntity u WHERE u.systemUsername = :systemUsername", UserEntity.class)
                .setParameter("systemUsername", systemUsername)
                .getSingleResult();
        user.getUserProjects().size();
        user.getSkills().size();
        user.getInterests().size();
        return user;
    }

    /**
     * Retrieves the full name of a user by their system username.
     *
     * @param systemUsername The system username of the user.
     * @return The full name of the user or null if not found.
     */
    public String getFullNameBySystemUsername(String systemUsername) {
        try {
            return (String) em.createQuery("SELECT u.firstName || ' ' || u.lastName FROM UserEntity u WHERE u.systemUsername = :systemUsername")
                    .setParameter("systemUsername", systemUsername)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves a list of users for project creation, excluding certain users and applying filters.
     *
     * @param userId          The ID of the user performing the search.
     * @param rejectedUsersId A list of user IDs to exclude from the results.
     * @param usersPerPage    The number of users to return per page.
     * @param page            The page number of the results.
     * @param lab             The lab entity to filter by.
     * @param keyword         A keyword to filter the results.
     * @param skillNames      A list of skill names to filter the results.
     * @return A list of Object arrays containing user information.
     */
    public List<Object[]> getUserProjectCreationDto(int userId, List<Integer> rejectedUsersId, int usersPerPage, int page, LabEntity lab, String keyword, List<String> skillNames) {
        return em.createNamedQuery("User.getUserProjectCreationDto", Object[].class)
                .setParameter("id", userId)
                .setParameter("excludedIds", rejectedUsersId)
                .setParameter("lab", lab)
                .setParameter("keyword", keyword)
                .setParameter("skillNames", skillNames)
                .setFirstResult(usersPerPage * (page - 1))
                .setMaxResults(usersPerPage)
                .getResultList();
    }

    /**
     * Counts the number of users matching the criteria for project creation.
     *
     * @param userId          The ID of the user performing the search.
     * @param rejectedUsersId A list of user IDs to exclude from the count.
     * @param lab             The lab entity to filter by.
     * @param keyword         A keyword to filter the results.
     * @return The count of users matching the criteria.
     */
    public int getNumberUserProjectCreationDto(int userId, List<Integer> rejectedUsersId, LabEntity lab, String keyword) {
        return em.createNamedQuery("User.countUserProjectCreationDto", Long.class)
                .setParameter("id", userId)
                .setParameter("excludedIds", rejectedUsersId)
                .setParameter("lab", lab)
                .setParameter("keyword", keyword)
                .getSingleResult().intValue();
    }

    /**
     * Retrieves the skills of a user filtered by skill names.
     *
     * @param userId     The ID of the user.
     * @param skillNames A list of skill names to filter by.
     * @return A list of SkillEntity objects representing the user's skills.
     */
    public List<SkillEntity> getUserSkills(int userId, List<String> skillNames) {
        return em.createNamedQuery("User.getUserSkills", SkillEntity.class)
                .setParameter("id", userId)
                .setParameter("skillNames", skillNames)
                .getResultList();
    }

    /**
     * Counts the total number of users in the application.
     *
     * @return The total number of users.
     */
    public int getNumberOfUsersInApp() {
        return em.createNamedQuery("User.countUsersInApp", Long.class)
                .getSingleResult().intValue();
    }

    /**
     * Retrieves the number of users per lab.
     *
     * @return A list of Object arrays where each array contains the lab name and the count of users in that lab.
     */
    public List<Object[]> getUsersPerLab() {
        return em.createNamedQuery("User.countUsersPerLab", Object[].class)
                .getResultList();
    }

    /**
     * Finds all users except those with administration roles and the specified user.
     *
     * @param user The user to exclude from the results along with administration users.
     * @return A list of UserEntity objects.
     */
    public List<UserEntity> findAllUsersExceptAdministationAndUser(UserEntity user) {
        return em.createNamedQuery("User.findAllUsersExceptAdministationAndUser", UserEntity.class)
                .setParameter("user", user)
                .getResultList();
    }
}