package com.ilm.projecto_ilm_backend.dto.logs;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dto.notes.NoteDto;

import java.util.List;

/**
 * Data Transfer Object for aggregating logs and notes for a specific project page.
 * This class encapsulates logs and notes along with additional information about the project
 * and the type of user viewing the page.
 */
public class LogsAndNotesPageDto {

    /**
     * A list of LogDto objects representing the logs associated with the project.
     */
    List<LogDto> logs;

    /**
     * A list of NoteDto objects representing the notes associated with the project.
     */
    List<NoteDto> notes;

    /**
     * The type of user viewing the page, determining the level of access or interaction
     * they have with the logs and notes.
     */
    UserInProjectTypeENUM typeOfUserSeingPage;

    /**
     * The name of the project to which the logs and notes are associated.
     */
    String projectName;

    /**
     * The current status of the project.
     */
    StateProjectENUM projectStatus;

    /**
     * Default constructor.
     */
    public LogsAndNotesPageDto() {
    }

    /**
     * Constructs a new LogsAndNotesPageDto with specified logs, notes, user type, project name, and project status.
     *
     * @param logs A list of LogDto objects.
     * @param notes A list of NoteDto objects.
     * @param typeOfUserSeingPage The type of user viewing the page.
     * @param projectName The name of the project.
     * @param projectStatus The current status of the project.
     */
    public LogsAndNotesPageDto(List<LogDto> logs, List<NoteDto> notes, UserInProjectTypeENUM typeOfUserSeingPage, String projectName, StateProjectENUM projectStatus) {
        this.logs = logs;
        this.notes = notes;
        this.typeOfUserSeingPage = typeOfUserSeingPage;
        this.projectName = projectName;
        this.projectStatus = projectStatus;
    }

    public List<LogDto> getLogs() {
        return logs;
    }

    public void setLogs(List<LogDto> logs) {
        this.logs = logs;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteDto> notes) {
        this.notes = notes;
    }

    public UserInProjectTypeENUM getTypeOfUserSeingPage() {
        return typeOfUserSeingPage;
    }

    public void setTypeOfUserSeingPage(UserInProjectTypeENUM typeOfUserSeingPage) {
        this.typeOfUserSeingPage = typeOfUserSeingPage;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public StateProjectENUM getProjectStatus() {
        return projectStatus;
    }

    public void setProjectState(StateProjectENUM projectStatus) {
        this.projectStatus = projectStatus;
    }
}
