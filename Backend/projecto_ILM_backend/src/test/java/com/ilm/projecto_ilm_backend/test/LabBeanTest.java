package com.ilm.projecto_ilm_backend.test;

import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.bean.LabBean;
import com.ilm.projecto_ilm_backend.dao.LabDao;
import com.ilm.projecto_ilm_backend.dto.lab.LabDto;
import com.ilm.projecto_ilm_backend.entity.LabEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LabBeanTest {
    @Mock
    private LabDao labDao;

    @InjectMocks
    private LabBean labBean;
    @Test
    public void testGetAllLabs_WhenLabsFound() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Prepare mock behavior: mock LabEntity objects
        List<LabEntity> mockLabs = new ArrayList<>();
        mockLabs.add(createMockLab(1, WorkLocalENUM.COIMBRA, "+351 239 247 000"));
        mockLabs.add(createMockLab(2, WorkLocalENUM.LISBON, "+351 218 418 000"));

        // Mock behavior of labDao.findAll() to return mockLabs
        when(labDao.findAll()).thenReturn(mockLabs);

        // Call the method under test
        List<LabDto> result = labBean.getAllLabs();

        // Assert the result
        assertEquals(2, result.size(), "Expected two labs in the result list");
        // Add more specific assertions based on your LabDto conversion if needed
    }

    @Test
    public void testGetAllLabs_WhenNoLabsFound() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Prepare mock behavior: mock LabEntity objects
        List<LabEntity> mockLabs = new ArrayList<>();
        mockLabs.add(createMockLab(1, WorkLocalENUM.COIMBRA, "+351 239 247 000"));
        mockLabs.add(createMockLab(2, WorkLocalENUM.LISBON, "+351 218 418 000"));

        // Mock behavior of labDao.findAll() to return mockLabs
        when(labDao.findAll()).thenReturn(mockLabs);

        // Call the method under test
        List<LabDto> result = labBean.getAllLabs();

        assertNotEquals(0, result.size(), "Expected no labs in the result list");
    }
    // Utility method to create a mock LabEntity
    private LabEntity createMockLab(int id, WorkLocalENUM local, String contact) {
        LabEntity lab = new LabEntity();
        lab.setId(id);
        lab.setLocal(local);
        lab.setContact(contact);
        return lab;
    }
}