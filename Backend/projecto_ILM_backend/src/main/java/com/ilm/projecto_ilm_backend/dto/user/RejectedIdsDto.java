package com.ilm.projecto_ilm_backend.dto.user;

import java.util.List;

public class RejectedIdsDto {
    List<Integer> rejectedIds;

    public RejectedIdsDto() {
    }

    public List<Integer> getRejectedIds() {
        return rejectedIds;
    }

    public void setRejectedIds(List<Integer> rejectedIds) {
        this.rejectedIds = rejectedIds;
    }
}
