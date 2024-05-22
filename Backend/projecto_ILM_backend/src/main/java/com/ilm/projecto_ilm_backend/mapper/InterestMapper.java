package com.ilm.projecto_ilm_backend.mapper;

import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.entity.InterestEntity;

/**
 * InterestMapper is a utility class that provides methods to convert between InterestEntity and InterestDto.
 */
public class InterestMapper {

    /**
     * Default constructor.
     */
    public InterestMapper() {
    }

    /**
     * Converts an InterestEntity object to an InterestDto object.
     *
     * @param interestEntity the InterestEntity object to be converted
     * @return the converted InterestDto object
     */
    public static InterestDto toDto(InterestEntity interestEntity) {
        InterestDto interestDto = new InterestDto();

        interestDto.setId(interestEntity.getId());
        interestDto.setName(interestEntity.getName());

        return interestDto;
    }
}
