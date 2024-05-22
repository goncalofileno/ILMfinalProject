package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.SkillTypeENUM;
import com.ilm.projecto_ilm_backend.dao.SkillDao;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import com.ilm.projecto_ilm_backend.entity.SkillEntity;
import com.ilm.projecto_ilm_backend.mapper.SkillMapper;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

/**
 * The SkillBean class is an application scoped bean that manages SkillEntity instances.
 * It provides methods to interact with the database.
 */
@ApplicationScoped
public class SkillBean {

    @EJB
    SkillDao skillDao;

    public void createDefaultSkillsIfNotExistent(){
        if(skillDao.findById(1) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.KNOWLEDGE);
            skill.setName("Artificial Intelligence");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

        if(skillDao.findById(2) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.KNOWLEDGE);
            skill.setName("Machine Learning");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

        if(skillDao.findById(3) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.KNOWLEDGE);
            skill.setName("Deep Learning");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

        if(skillDao.findById(4) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.SOFTWARE);
            skill.setName("Python");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

        if(skillDao.findById(5) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.SOFTWARE);
            skill.setName("Java");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

        if(skillDao.findById(6) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.SOFTWARE);
            skill.setName("C++");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

        if(skillDao.findById(7) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.HARDWARE);
            skill.setName("Arduino");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

        if(skillDao.findById(8) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.HARDWARE);
            skill.setName("Raspberry Pi");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

        if(skillDao.findById(9) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.HARDWARE);
            skill.setName("Sensors");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

        if(skillDao.findById(10) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.TOOLS);
            skill.setName("Git");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

        if(skillDao.findById(11) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.TOOLS);
            skill.setName("Docker");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

        if(skillDao.findById(12) == null){
            SkillEntity skill = new SkillEntity();
            skill.setType(SkillTypeENUM.TOOLS);
            skill.setName("Jenkins");
            skill.setDeleted(false);

            skillDao.persist(skill);
        }

    }

    public List<SkillDto> getAllSkills() {
        List<SkillEntity> skills = skillDao.findAll();
        List<SkillDto> skillDtos = new ArrayList<>();
        for (SkillEntity skill : skills) {
            skillDtos.add(SkillMapper.toDto(skill));
        }
        return skillDtos;
    }
}
