package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.dao.NoteDao;
import com.ilm.projecto_ilm_backend.dao.ProjectDao;
import com.ilm.projecto_ilm_backend.dao.TaskDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.entity.NoteEntity;
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



    public void createDefaultNotesIfNotExistent() {
        if(noteDao.findById(1) == null) {
            NoteEntity note = new NoteEntity();
            note.setDate(LocalDateTime.now());
            note.setText("Nota standard do project 1 NÂO FEITA");
            note.setUser(userDao.findById(1));
            note.setProject(projectDao.findById(1));
            note.setDone(false);
            note.setTask(null);
            noteDao.persist(note);
        }
        if(noteDao.findById(2) == null) {
            NoteEntity note = new NoteEntity();
            note.setDate(LocalDateTime.now());
            note.setText("Nota standard do project 1 FEITA");
            note.setUser(userDao.findById(1));
            note.setProject(projectDao.findById(1));
            note.setDone(true);
            note.setTask(null);
            noteDao.persist(note);
        }
        if(noteDao.findById(3) == null) {
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
}
