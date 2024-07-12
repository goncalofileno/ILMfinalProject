package com.ilm.projecto_ilm_backend.test;

import com.ilm.projecto_ilm_backend.bean.SupplierBean;
import com.ilm.projecto_ilm_backend.dao.SupplierDao;
import com.ilm.projecto_ilm_backend.entity.SupplierEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class SupplierBeanTest {
    @InjectMocks
    private SupplierBean supplierBean;

    @Mock
    private SupplierDao supplierDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetAllNames() {
        // Given
        List<String> names = Arrays.asList("Worten", "Fnac", "MediaMarkt", "Radio Popular");
        when(supplierDao.getAllNames()).thenReturn(names);

        // When
        List<String> result = supplierBean.getAllNames();

        // Then
        assertEquals(4, result.size());
        assertTrue(result.contains("Worten"));
        assertTrue(result.contains("Fnac"));
        assertTrue(result.contains("MediaMarkt"));
        assertTrue(result.contains("Radio Popular"));
    }

    @Test
    public void testFindSupplierContactByName() {
        // Given
        when(supplierDao.findSupplierContactByName("Worten")).thenReturn("210155222");

        // When
        String contact = supplierBean.findSupplierContactByName("Worten");

        // Then
        assertEquals("210155222", contact);
    }

    @Test
    public void testGetAllNames_Unsuccessful() {
        // Given
        List<String> names = Arrays.asList("SomeOtherName1", "SomeOtherName2");
        when(supplierDao.getAllNames()).thenReturn(names);

        // When
        List<String> result = supplierBean.getAllNames();

        // Then
        assertFalse(result.contains("Worten")); // Ensure that "Worten" is not in the result
        assertFalse(result.contains("Fnac")); // Ensure that "Fnac" is not in the result
        assertEquals(2, result.size()); // Ensure the size does not match the expected default suppliers
    }

    @Test
    public void testFindSupplierContactByName_Unsuccessful() {
        // Given
        when(supplierDao.findSupplierContactByName("Worten")).thenReturn(null); // Simulate no contact found

        // When
        String contact = supplierBean.findSupplierContactByName("Worten");

        // Then
        assertNull(contact); // Ensure the contact is null
    }
}