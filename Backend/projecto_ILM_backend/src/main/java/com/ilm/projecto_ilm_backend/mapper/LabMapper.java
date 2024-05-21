package com.ilm.projecto_ilm_backend.mapper;

import com.ilm.projecto_ilm_backend.dto.lab.LabDto;
import com.ilm.projecto_ilm_backend.entity.LabEntity;

/**
 * LabMapper is a utility class that provides methods to convert between LabEntity and LabDto.
 */
public class LabMapper {

    /**
     * Default constructor.
     */
    public LabMapper() {
    }

    /**
     * Converts a LabEntity object to a LabDto object.
     *
     * @param labEntity the LabEntity object to be converted
     * @return the converted LabDto object
     */
    public static LabDto toDto(LabEntity labEntity) {
        LabDto labDto = new LabDto();

        labDto.setLocal(labEntity.getLocal().toString());
        labDto.setContact(labEntity.getContact());

        return labDto;
    }
}