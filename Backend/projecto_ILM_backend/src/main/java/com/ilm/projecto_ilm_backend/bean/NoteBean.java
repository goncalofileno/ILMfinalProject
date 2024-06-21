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
