package com.ilm.projecto_ilm_backend.dto.user;

import java.util.List;

public class UserProjectCreationInfoDto {
    private List<UserProjectCreationDto> userProjectCreationDtos;
    private int maxPageNumber;

    public UserProjectCreationInfoDto() {
    }

    public List<UserProjectCreationDto> getUserProjectCreationDtos() {
        return userProjectCreationDtos;
    }

    public void setUserProjectCreationDtos(List<UserProjectCreationDto> userProjectCreationDtos) {
        this.userProjectCreationDtos = userProjectCreationDtos;
    }

    public int getMaxPageNumber() {
        return maxPageNumber;
    }

    public void setMaxPageNumber(int maxPageNumber) {
        this.maxPageNumber = maxPageNumber;
    }
}
