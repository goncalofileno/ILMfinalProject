package com.ilm.projecto_ilm_backend.validator;

import com.ilm.projecto_ilm_backend.dto.user.UserProfileDto;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserProfileValidator {

    //Verifica que no userprofileDto os campos firstname, lastname e office não estão vazios. verifca se o office é um dos valores possiveis no ENUM WorkLocalENUM
    public static boolean validateUserProfileDto(UserProfileDto userProfileDto) {
        if (userProfileDto.getFirstName() == null || userProfileDto.getFirstName().isEmpty()) {
            return false;
        }
        if (userProfileDto.getLastName() == null || userProfileDto.getLastName().isEmpty()) {
            return false;
        }
        if (userProfileDto.getLab() == null || userProfileDto.getLab().isEmpty()) {
            return false;
        }
        if (!WorkLocalENUM.contains(userProfileDto.getLab().toUpperCase())) {
            return false;
        }
        return true;
    }


}
