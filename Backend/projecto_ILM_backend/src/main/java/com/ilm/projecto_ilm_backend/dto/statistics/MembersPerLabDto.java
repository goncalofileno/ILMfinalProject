package com.ilm.projecto_ilm_backend.dto.statistics;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;

public class MembersPerLabDto {
    private WorkLocalENUM workLocal;
    private long members;

    public MembersPerLabDto() {
    }

    public WorkLocalENUM getWorkLocal() {
        return workLocal;
    }

    public void setWorkLocal(WorkLocalENUM workLocal) {
        this.workLocal = workLocal;
    }

    public long getMembers() {
        return members;
    }

    public void setMembers(long members) {
        this.members = members;
    }
}
