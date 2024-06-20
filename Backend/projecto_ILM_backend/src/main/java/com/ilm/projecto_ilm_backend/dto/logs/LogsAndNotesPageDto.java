package com.ilm.projecto_ilm_backend.dto.logs;

import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.dto.notes.NoteDto;

import java.util.List;

public class LogsAndNotesPageDto {

    List<LogDto> logs;
    List<NoteDto> notes;
    UserInProjectTypeENUM typeOfUserSeingPage;
    String projectName;

    public LogsAndNotesPageDto() {
    }

    public LogsAndNotesPageDto(List<LogDto> logs, List<NoteDto> notes, UserInProjectTypeENUM typeOfUserSeingPage, String projectName) {
        this.logs = logs;
        this.notes = notes;
        this.typeOfUserSeingPage = typeOfUserSeingPage;
        this.projectName = projectName;
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
}
