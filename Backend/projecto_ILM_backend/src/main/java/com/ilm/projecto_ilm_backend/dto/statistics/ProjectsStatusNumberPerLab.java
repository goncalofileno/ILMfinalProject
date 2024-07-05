package com.ilm.projecto_ilm_backend.dto.statistics;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;

import java.util.ArrayList;

public class ProjectsStatusNumberPerLab {
    private WorkLocalENUM lab;
    private ArrayList<StatusNumberDto> statusNumber;

    public ProjectsStatusNumberPerLab(WorkLocalENUM lab) {
        this.lab = lab;
        this.statusNumber = new ArrayList<>();
    }

    public WorkLocalENUM getLab() {
        return lab;
    }

    public void setLab(WorkLocalENUM lab) {
        this.lab = lab;
    }

     public ArrayList<StatusNumberDto> getStatusNumber() {
          return statusNumber;
     }

     public void setStatusNumber(ArrayList<StatusNumberDto> statusNumber) {
            this.statusNumber = statusNumber;
    }

    public void addStatusNumber(StatusNumberDto statusNumberDto) {
        statusNumber.add(statusNumberDto);
    }
    public void setStatusNumber(long numberOfProjects, StateProjectENUM projectState) {
        for (StatusNumberDto statusNumberDto : statusNumber) {
            if (statusNumberDto.getStatus().equals(projectState)) {
                statusNumberDto.setNumber(numberOfProjects);
                return;
            }
        }
    }
}
