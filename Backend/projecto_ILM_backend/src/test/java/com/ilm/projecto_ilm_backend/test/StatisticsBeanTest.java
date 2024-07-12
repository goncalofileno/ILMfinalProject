package com.ilm.projecto_ilm_backend.test;

import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.WorkLocalENUM;
import com.ilm.projecto_ilm_backend.bean.StatisticsBean;
import com.ilm.projecto_ilm_backend.dao.ProjectDao;
import com.ilm.projecto_ilm_backend.dao.ResourceSupplierDao;
import com.ilm.projecto_ilm_backend.dao.UserDao;
import com.ilm.projecto_ilm_backend.dao.UserProjectDao;
import com.ilm.projecto_ilm_backend.dto.statistics.StatisticsDto;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsBeanTest {
    @InjectMocks
    private StatisticsBean statisticsBean;

    @Mock
    private UserDao userDao;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private UserProjectDao userProjectDao;

    @Mock
    private ResourceSupplierDao resourceSupplierDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStatistics_Success() {
        // Mocking UserDao
        when(userDao.getNumberOfUsersInApp()).thenReturn(100);

        // Mocking ProjectDao
        List<Integer> projectIds = new ArrayList<>();
        projectIds.add(1);
        projectIds.add(2);
        when(projectDao.getProjectIds()).thenReturn(projectIds);
        when(projectDao.getProjectsExecutionDates()).thenReturn(Collections.singletonList(new Object[]{LocalDateTime.now().minusDays(5), LocalDateTime.now()}));

        // Mocking UserProjectDao
        when(userProjectDao.countUsersInProject(anyInt())).thenReturn(10);

        // Mocking ResourceSupplierDao
        when(resourceSupplierDao.countResourcesPerSupplier()).thenReturn(Collections.singletonList(new Object[]{"Supplier1", 50L}));

        // Mocking other methods
        when(userDao.getUsersPerLab()).thenReturn(Collections.singletonList(new Object[]{WorkLocalENUM.COIMBRA, 20L}));
        when(projectDao.getProjectsPerLab()).thenReturn(Collections.singletonList(new Object[]{WorkLocalENUM.COIMBRA, 10L}));
        when(projectDao.countProjectsByStatusAndLab()).thenReturn(Collections.singletonList(new Object[]{WorkLocalENUM.COIMBRA, StateProjectENUM.IN_PROGRESS, 5L}));

        // Call the method
        StatisticsDto statisticsDto = statisticsBean.getStatistics();

        // Verify the results
        assertNotNull(statisticsDto);
        assertEquals(100, statisticsDto.getTotalUsers());
        assertEquals(10.0, statisticsDto.getAverageUsersInProject());
        assertEquals(7200.0, statisticsDto.getAverageExecutionTimePerProject());
        assertEquals(1, statisticsDto.getSupplierWithMostResources().size());
        assertEquals("Supplier1", statisticsDto.getSupplierWithMostResources().get(0).getSupplier());
        assertEquals(50L, statisticsDto.getSupplierWithMostResources().get(0).getResources());
    }

    @Test
    public void testGetStatistics_Unsuccessful() {
        // Mocking UserDao to throw an exception
        when(userDao.getNumberOfUsersInApp()).thenThrow(new RuntimeException("Database error"));

        // Mocking other methods to ensure the correct enum types are returned
        when(userDao.getUsersPerLab()).thenReturn(Collections.singletonList(new Object[]{WorkLocalENUM.COIMBRA.toString(), 20L}));
        when(projectDao.getProjectsPerLab()).thenReturn(Collections.singletonList(new Object[]{WorkLocalENUM.COIMBRA.toString(), 10L}));
        when(projectDao.countProjectsByStatusAndLab()).thenReturn(Collections.singletonList(new Object[]{WorkLocalENUM.COIMBRA.toString(), StateProjectENUM.IN_PROGRESS, 5L}));

        // Call the method and assert that it throws an exception
        assertThrows(RuntimeException.class, () -> {
            statisticsBean.getStatistics();
        });
    }

}