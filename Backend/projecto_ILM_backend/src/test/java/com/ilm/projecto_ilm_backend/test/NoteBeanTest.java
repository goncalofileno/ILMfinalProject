package com.ilm.projecto_ilm_backend.test;

import com.ilm.projecto_ilm_backend.bean.NoteBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.notes.NoteDto;
import com.ilm.projecto_ilm_backend.entity.NoteEntity;
import com.ilm.projecto_ilm_backend.entity.UserEntity;
import com.ilm.projecto_ilm_backend.security.exceptions.ProjectNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotFoundException;
import com.ilm.projecto_ilm_backend.security.exceptions.UserNotInProjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import com.ilm.projecto_ilm_backend.entity.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class NoteBeanTest {

    @InjectMocks
    private NoteBean noteBean;

    @Mock
    private NoteDao noteDao;

    @Mock
    private UserDao userDao;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private TaskDao taskDao;

    @Mock
    private UserBean userBean;

    @Mock
    private UserProjectDao userProjectDao;

    // Set up common mocks and test data here
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testConvertToDto() {
        UserEntity user = new UserEntity();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoto("photo.jpg");

        NoteEntity note = new NoteEntity();
        note.setId(1);
        note.setText("Test note");
        note.setDate(LocalDateTime.now());
        note.setDone(false);
        note.setUser(user);

        NoteDto noteDto = noteBean.convertToDto(note);

        assertEquals(note.getId(), noteDto.getId());
        assertEquals(note.getText(), noteDto.getText());
        assertEquals(note.getDate(), noteDto.getDate());
        assertEquals(note.isDone(), noteDto.isDone());
        assertEquals(note.getUser().getFirstName() + " " + note.getUser().getLastName(), noteDto.getAuthorName());
        assertEquals(note.getUser().getPhoto(), noteDto.getAuthorPhoto());
    }

    @Test
    public void testCreateNote() throws Exception {
        String sessionId = "session123";
        String systemProjectName = "project1";

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");

        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName(systemProjectName);

        NoteDto noteDto = new NoteDto();
        noteDto.setText("New note");

        when(projectDao.findBySystemName(systemProjectName)).thenReturn(project);
        when(userBean.getUserBySessionId(sessionId)).thenReturn(user);
        when(userProjectDao.isUserInProject(project.getId(), user.getId())).thenReturn(true);

        noteBean.createNote(sessionId, noteDto, systemProjectName);

        verify(noteDao, times(1)).persist(any(NoteEntity.class));
    }
    @Test
    public void testMarkAsDone() throws Exception {
        String sessionId = "session123";
        int noteId = 1;
        boolean done = true;

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");

        ProjectEntity project = new ProjectEntity();
        project.setId(1);

        NoteEntity note = new NoteEntity();
        note.setId(noteId);
        note.setProject(project);
        note.setUser(user);

        when(noteDao.findById(noteId)).thenReturn(note);
        when(userBean.getUserBySessionId(sessionId)).thenReturn(user);
        when(userProjectDao.isUserInProject(project.getId(), user.getId())).thenReturn(true);

        noteBean.markAsDone(sessionId, noteId, done);

        verify(noteDao, times(1)).merge(any(NoteEntity.class));
        assertTrue(note.isDone());
    }

    @Test
    public void testCreateNoteProjectNotFound() {
        String sessionId = "session123";
        String systemProjectName = "project1";

        NoteDto noteDto = new NoteDto();
        noteDto.setText("New note");

        when(projectDao.findBySystemName(systemProjectName)).thenReturn(null);

        ProjectNotFoundException thrown = assertThrows(
                ProjectNotFoundException.class,
                () -> noteBean.createNote(sessionId, noteDto, systemProjectName)
        );

        assertEquals("Project not found: " + systemProjectName, thrown.getMessage());
    }

    @Test
    public void testCreateNoteUserNotFound() {
        String sessionId = "session123";
        String systemProjectName = "project1";

        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName(systemProjectName);

        NoteDto noteDto = new NoteDto();
        noteDto.setText("New note");

        when(projectDao.findBySystemName(systemProjectName)).thenReturn(project);
        when(userBean.getUserBySessionId(sessionId)).thenReturn(null);

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> noteBean.createNote(sessionId, noteDto, systemProjectName)
        );

        assertEquals("User not found for session id: " + sessionId, thrown.getMessage());
    }

    @Test
    public void testCreateNoteUserNotInProject() {
        String sessionId = "session123";
        String systemProjectName = "project1";

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");

        ProjectEntity project = new ProjectEntity();
        project.setId(1);
        project.setSystemName(systemProjectName);

        NoteDto noteDto = new NoteDto();
        noteDto.setText("New note");

        when(projectDao.findBySystemName(systemProjectName)).thenReturn(project);
        when(userBean.getUserBySessionId(sessionId)).thenReturn(user);
        when(userProjectDao.isUserInProject(project.getId(), user.getId())).thenReturn(false);

        UserNotInProjectException thrown = assertThrows(
                UserNotInProjectException.class,
                () -> noteBean.createNote(sessionId, noteDto, systemProjectName)
        );

        assertEquals("User not part of project", thrown.getMessage());
    }

    @Test
    public void testMarkAsDoneNoteNotFound() {
        String sessionId = "session123";
        int noteId = 1;
        boolean done = true;

        when(noteDao.findById(noteId)).thenReturn(null);

        ProjectNotFoundException thrown = assertThrows(
                ProjectNotFoundException.class,
                () -> noteBean.markAsDone(sessionId, noteId, done)
        );

        assertEquals("Note not found: " + noteId, thrown.getMessage());
    }

    @Test
    public void testMarkAsDoneUserNotFound() {
        String sessionId = "session123";
        int noteId = 1;
        boolean done = true;

        NoteEntity note = new NoteEntity();
        note.setId(noteId);

        when(noteDao.findById(noteId)).thenReturn(note);
        when(userBean.getUserBySessionId(sessionId)).thenReturn(null);

        UserNotFoundException thrown = assertThrows(
                UserNotFoundException.class,
                () -> noteBean.markAsDone(sessionId, noteId, done)
        );

        assertEquals("User not found for session id: " + sessionId, thrown.getMessage());
    }

    @Test
    public void testMarkAsDoneUserNotInProject() {
        String sessionId = "session123";
        int noteId = 1;
        boolean done = true;

        UserEntity user = new UserEntity();
        user.setId(1);
        user.setFirstName("John");
        user.setLastName("Doe");

        ProjectEntity project = new ProjectEntity();
        project.setId(1);

        NoteEntity note = new NoteEntity();
        note.setId(noteId);
        note.setProject(project);

        when(noteDao.findById(noteId)).thenReturn(note);
        when(userBean.getUserBySessionId(sessionId)).thenReturn(user);
        when(userProjectDao.isUserInProject(project.getId(), user.getId())).thenReturn(false);

        UserNotInProjectException thrown = assertThrows(
                UserNotInProjectException.class,
                () -> noteBean.markAsDone(sessionId, noteId, done)
        );

        assertEquals("User not part of project", thrown.getMessage());
    }
}