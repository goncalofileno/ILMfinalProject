package com.ilm.projecto_ilm_backend.dto.statistics;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;

public class StatusNumberDto {
    private StateProjectENUM status;
    private long number;

    public StatusNumberDto(StateProjectENUM status, long number) {
        this.status = status;
        this.number = number;
    }

    public StateProjectENUM getStatus() {
        return status;
    }

    public void setStatus(StateProjectENUM status) {
        this.status = status;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}
