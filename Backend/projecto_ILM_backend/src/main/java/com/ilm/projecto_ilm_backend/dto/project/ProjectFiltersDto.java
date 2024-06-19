package com.ilm.projecto_ilm_backend.dto.project;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;

import java.util.ArrayList;
import java.util.List;

public class ProjectFiltersDto {
    private ArrayList<WorkLocalENUM> labs;
    private ArrayList<StateProjectENUM> states;
    private ArrayList<UserInProjectTypeENUM> userTypesInProject;

    public ProjectFiltersDto() {
    }

    public ArrayList<WorkLocalENUM> getLabs() {
        return labs;
    }

    public void setLabs(ArrayList<WorkLocalENUM> labs) {
        this.labs = labs;
    }

    public ArrayList<StateProjectENUM> getStates() {
        return states;
    }

    public void setStates(ArrayList<StateProjectENUM> states) {
        this.states = states;
    }

    public ArrayList<UserInProjectTypeENUM> getUserTypesInProject() {
        return userTypesInProject;
    }

    public void setUserTypesInProject(ArrayList<UserInProjectTypeENUM> userTypesInProject) {
        this.userTypesInProject = userTypesInProject;
    }
}
