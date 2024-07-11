package com.ilm.projecto_ilm_backend.test;

import static org.junit.jupiter.api.Assertions.*;
import com.ilm.projecto_ilm_backend.ENUMS.SkillTypeENUM;
import com.ilm.projecto_ilm_backend.bean.SkillBean;
import com.ilm.projecto_ilm_backend.dao.SkillDao;
import com.ilm.projecto_ilm_backend.dto.skill.SkillDto;
import com.ilm.projecto_ilm_backend.entity.SkillEntity;
import com.ilm.projecto_ilm_backend.mapper.SkillMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SkillBeanTest {
    @InjectMocks
    private SkillBean skillBean;

    @Mock
    private SkillDao skillDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    public void testGetAllSkills() {
        // Create a list of SkillEntity to return
        List<SkillEntity> skillEntities = new ArrayList<>();
        SkillEntity skillEntity1 = new SkillEntity();
        skillEntity1.setId(1);
        skillEntity1.setName("Artificial Intelligence");
        skillEntity1.setType(SkillTypeENUM.KNOWLEDGE);
        skillEntities.add(skillEntity1);

        SkillEntity skillEntity2 = new SkillEntity();
        skillEntity2.setId(2);
        skillEntity2.setName("Machine Learning");
        skillEntity2.setType(SkillTypeENUM.KNOWLEDGE);
        skillEntities.add(skillEntity2);

        // Mocking the behavior of skillDao.findAll to return the list of skill entities
        when(skillDao.findAll()).thenReturn(skillEntities);

        // Call the method to test
        List<SkillDto> skillDtos = skillBean.getAllSkills();

        // Verify the results
        assertEquals(2, skillDtos.size());
        assertEquals("Artificial Intelligence", skillDtos.get(0).getName());
        assertEquals("Machine Learning", skillDtos.get(1).getName());
    }
    @Test
    public void testGetAllSkills_Unsuccessful_EmptyList() {
        // Mocking the behavior of skillDao.findAll to return an empty list
        when(skillDao.findAll()).thenReturn(new ArrayList<>());

        // Call the method to test
        List<SkillDto> skillDtos = skillBean.getAllSkills();

        // Verify the results
        assertTrue(skillDtos.isEmpty());
    }
    @Test
    public void testGetAllSkills_DaoException() {
        // Mocking the behavior of skillDao.findAll to throw a RuntimeException
        when(skillDao.findAll()).thenThrow(new RuntimeException("Database error"));

        // Call the method to test and verify it handles the exception
        assertThrows(RuntimeException.class, () -> {
            skillBean.getAllSkills();
        });
    }
    @Test
    public void testGetAllSkills_Unsuccessful_Exception() {
        // Mocking the behavior of skillDao.findAll to throw an exception
        when(skillDao.findAll()).thenThrow(new RuntimeException("Database Error"));

        // Call the method to test and assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            skillBean.getAllSkills();
        });

        // Verify the exception message
        assertEquals("Database Error", exception.getMessage());
    }
}