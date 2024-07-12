package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.notes.NoteDto;
import com.ilm.projecto_ilm_backend.entity.NoteEntity;
import com.ilm.projecto_ilm_backend.entity.ProjectEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.security.exceptions.ProjectNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotInProjectException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

/**
 * The NoteBean class is responsible for managing NoteEntity instances.
 * It is an application scoped bean, meaning there is a single instance for the entire application.
 */
@ApplicationScoped
public class NoteBean {

    @Inject
    private NoteDao noteDao;

    @Inject
    private UserDao userDao;

    @Inject
    private ProjectDao projectDao;

    @Inject
    private TaskDao taskDao;

    @Inject
    private UserBean userBean;

    @Inject
    private UserProjectDao userProjectDao;


    /**
     * Creates default notes for demonstration or initial setup purposes.
     * This method checks if there are any notes present in the database and,
     * if not, creates a predefined set of notes for a project.
     */
    public void createDefaultNotesIfNotExistent() {
        if (noteDao.findById(1) == null) {
            NoteEntity note = new NoteEntity();
            note.setDate(LocalDateTime.now());
            note.setText("Nota standard do project 1 NÂO FEITA");
            note.setUser(userDao.findById(1));
            note.setProject(projectDao.findById(1));
            note.setDone(false);
            note.setTask(null);
            noteDao.persist(note);
        }
        if (noteDao.findById(2) == null) {
            NoteEntity note = new NoteEntity();
            note.setDate(LocalDateTime.now());
            note.setText("Nota standard do project 1 FEITA");
            note.setUser(userDao.findById(1));
            note.setProject(projectDao.findById(1));
            note.setDone(true);
            note.setTask(null);
            noteDao.persist(note);
        }
        if (noteDao.findById(3) == null) {
            NoteEntity note = new NoteEntity();
            note.setDate(LocalDateTime.now());
            note.setText("Nota standard do project 1 NÂO FEITA c/ task");
            note.setUser(userDao.findById(1));
            note.setProject(projectDao.findById(1));
            note.setDone(false);
            note.setTask(taskDao.findById(1));
            noteDao.persist(note);
        }
    }

    /**
     * Converts a NoteEntity instance to a NoteDto instance.
     *
     * @param note the NoteEntity instance to convert
     * @return the converted NoteDto instance
     */
    public NoteDto convertToDto(NoteEntity note) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(note.getId());
        noteDto.setText(note.getText());
        noteDto.setDate(note.getDate());
        noteDto.setDone(note.isDone());
        noteDto.setAuthorName(note.getUser().getFirstName() + " " + note.getUser().getLastName());
        noteDto.setAuthorPhoto(note.getUser().getPhoto());
        if (note.getTask() != null) {
            noteDto.setTaskSystemName(note.getTask().getSystemTitle());
        }

        return noteDto;
    }

    /**
     * Creates a new note for a project.
     * This method checks if the project exists and if the user is a member of the project.
     * If both conditions are met, it creates a new note for the project.
     *
     * @param sessionId          the session id of the user
     * @param noteDto            the NoteDto instance containing the note data
     * @param systemProjectName  the system name of the project
     * @throws Exception Throws {@link ProjectNotFoundException} if the project is not found,
     *                   or {@link UserNotInProjectException} if the user is not a member of the project.
     */
    public void createNote(String sessionId, NoteDto noteDto, String systemProjectName) throws Exception {

        ProjectEntity project = projectDao.findBySystemName(systemProjectName);
        UserEntity user = userBean.getUserBySessionId(sessionId);

        if (project == null) {
            throw new ProjectNotFoundException("Project not found: " + systemProjectName);
        }

        if (user == null) {
            throw new UserNotFoundException("User not found for session id: " + sessionId);
        }

        if (!userProjectDao.isUserInProject(project.getId(), user.getId())) {
            throw new UserNotInProjectException("User not part of project");
        }

        NoteEntity note = new NoteEntity();
        note.setDate(LocalDateTime.now());
        note.setText(noteDto.getText());
        note.setUser(userBean.getUserBySessionId(sessionId));
        note.setProject(projectDao.findBySystemName(systemProjectName));
        note.setDone(false);
        if (noteDto.getTaskSystemName() != null) {
            note.setTask(taskDao.findTaskBySystemTitle(noteDto.getTaskSystemName()));
        }
        noteDao.persist(note);
    }

    /**
     * Marks a note as done or not done.
     * This method checks if the note exists and if the user is a member of the project.
     * If both conditions are met, it marks the note as done or not done.
     *
     * @param sessionId the session id of the user
     * @param id        the id of the note
     * @param done      the done status to set
     * @throws Exception Throws {@link ProjectNotFoundException} if the note is not found,
     *                   or {@link UserNotInProjectException} if the user is not a member of the project.
     */
    public void markAsDone(String sessionId, int id, boolean done) throws Exception {

        NoteEntity note = noteDao.findById(id);
        UserEntity user = userBean.getUserBySessionId(sessionId);

        if (note == null) {
            throw new ProjectNotFoundException("Note not found: " + id);
        }

        if (user == null) {
            throw new UserNotFoundException("User not found for session id: " + sessionId);
        }

        if (!userProjectDao.isUserInProject(note.getProject().getId(), user.getId())) {
            throw new UserNotInProjectException("User not part of project");
        }

        note.setDone(done);
        noteDao.merge(note);
    }
}
