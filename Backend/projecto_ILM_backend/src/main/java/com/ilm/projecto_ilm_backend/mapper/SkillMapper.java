package com.ilm.projecto_ilm_backend.mapper;

import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import com.ilm.projecto_ilm_backend.entity.SkillEntity;

/**
 * SkillMapper is a utility class that provides methods to convert between SkillEntity and SkillDto.
 */
public class SkillMapper {

    /**
     * Default constructor.
     */
    public SkillMapper() {
    }

    /**
     * Converts a SkillEntity object to a SkillDto object.
     *
     * @param skillEntity the SkillEntity object to be converted
     * @return the converted SkillDto object
     */
    public static SkillDto toDto(SkillEntity skillEntity) {
        SkillDto skillDto = new SkillDto();

        skillDto.setId(skillEntity.getId());
        skillDto.setName(skillEntity.getName());
        skillDto.setType(skillEntity.getType().toString());

        return skillDto;
    }
}
