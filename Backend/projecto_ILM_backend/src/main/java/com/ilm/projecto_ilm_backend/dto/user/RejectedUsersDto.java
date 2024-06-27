package com.ilm.projecto_ilm_backend.dto.user;

import java.util.List;

public class RejectedUsersDto {
    List<Long> rejectedUsersId;

    public RejectedUsersDto() {
    }

    public List<Long> getRejectedUsersId() {
        return rejectedUsersId;
    }

    public void setRejectedUsersId(List<Long> rejectedUsersId) {
        this.rejectedUsersId = rejectedUsersId;
    }
}
