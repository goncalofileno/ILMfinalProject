package com.ilm.projecto_ilm_backend.test;

import com.ilm.projecto_ilm_backend.bean.InterestBean;
import com.ilm.projecto_ilm_backend.dao.InterestDao;
import com.ilm.projecto_ilm_backend.dto.interest.InterestDto;
import com.ilm.projecto_ilm_backend.entity.InterestEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class InterestBeanTest {

    // Mocking the InterestDao and InterestMapper
    @Mock
    private InterestDao interestDao;

    // Injecting the mocks into the InterestBean
    @InjectMocks
    private InterestBean interestBean;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllInterests() {
        // Mocking the data returned by interestDao.findAll()
        List<InterestEntity> mockEntities = new ArrayList<>();
        mockEntities.add(createMockInterestEntity(1, "Health"));
        mockEntities.add(createMockInterestEntity(2, "Animals"));

        when(interestDao.findAll()).thenReturn(mockEntities);

        // Testing getAllInterests()
        List<InterestDto> result = interestBean.getAllInterests();

        // Asserting the result
        assertEquals(2, result.size()); // Assert the size based on your mock data
        assertEquals("Health", result.get(0).getName()); // Example assertion for the first item
        assertEquals("Animals", result.get(1).getName()); // Example assertion for the second item
        // Add more assertions based on your mock data
    }

    // Helper method to create mock InterestEntity
    private InterestEntity createMockInterestEntity(int id, String name) {
        InterestEntity entity = new InterestEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setDeleted(false); // Assuming default behavior
        // Set other properties if needed
        return entity;
    }


    @Test
    public void testGetAllInterestsFail() {
        // Mocking the data returned by interestDao.findAll()
        List<InterestEntity> mockEntities = new ArrayList<>();
        mockEntities.add(createMockInterestEntity(1, "Health"));
        mockEntities.add(createMockInterestEntity(2, "Animals"));

        when(interestDao.findAll()).thenReturn(mockEntities);

        // Testing getAllInterests()
        List<InterestDto> result = interestBean.getAllInterests();

        // Asserting the result
        assertEquals(2, result.size()); // Assert the size based on your mock data
        assertNotEquals("Artificial Inteligence", result.get(0).getName()); // Example assertion for the first item
        assertNotEquals("Java", result.get(1).getName()); // Example assertion for the second item
        // Add more assertions based on your mock data
    }
}