package com.ilm.projecto_ilm_backend.dto.statistics;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;

import java.util.ArrayList;

public class ProjectsStatusNumberPerLab {
    private String lab;
    private ArrayList<StatusNumberDto> statusNumber;

    public ProjectsStatusNumberPerLab(String lab) {
        this.lab = lab;
        this.statusNumber = new ArrayList<>();
    }

    public String getLab() {
        return lab;
    }

    public void setLab(String lab) {
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
