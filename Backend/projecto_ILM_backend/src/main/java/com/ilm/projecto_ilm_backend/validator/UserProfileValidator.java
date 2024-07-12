package com.ilm.projecto_ilm_backend.validator;

import com.ilm.projecto_ilm_backend.dto.user.UserProfileDto;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * UserProfileValidator is a class that provides a method to validate UserProfileDto objects.
 * It is annotated with @ApplicationScoped, meaning that it is a singleton and the same instance is used throughout the application.
 */
@ApplicationScoped
public class UserProfileValidator {

    /**
     * This method validates a UserProfileDto object.
     * It checks if the firstName, lastName, and lab fields are not null or empty.
     * It also checks if the lab field value is one of the possible values in the WorkLocalENUM.
     *
     * @param userProfileDto the UserProfileDto object to be validated
     * @return true if the UserProfileDto object is valid, false otherwise
     */
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
