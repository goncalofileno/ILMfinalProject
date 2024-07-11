package com.ilm.projecto_ilm_backend.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.ENUMS.UserInProjectTypeENUM;
import com.ilm.projecto_ilm_backend.bean.ResourceBean;
import com.ilm.projecto_ilm_backend.bean.UserBean;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.resource.*;
import com.ilm.projecto_ilm_backend.dto.user.RejectedIdsDto;
import com.ilm.projecto_ilm_backend.entity.*;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class ResourceBeanTest {
    @Mock
    private ResourceDao resourceDao;

    @Mock
    private SupplierDao supplierDao;

    @Mock
    private ResourceSupplierDao resourceSupplierDao;

    @Mock
    private ProjectDao projectDao;

    @Mock
    private ProjectResourceDao projectResourceDao;

    @Mock
    private UserBean userBean;

    @InjectMocks
    private ResourceBean resourceBean;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testGetResourceDetails() {
        RejectedIdsDto rejectedIdsDto = new RejectedIdsDto();
        rejectedIdsDto.setRejectedIds(new ArrayList<>());

        when(resourceDao.getResourceDetails(anyInt(), anyInt(), anyString(), any(), any(), anyString(), anyString(), anyString(), anyString(), anyString(), any())).thenReturn(Collections.emptyList());
        when(resourceDao.getNumberOfResources(anyString(), any(), any(), anyString(), any())).thenReturn(0);

        ResourceTableInfoDto resourceTableInfoDto = resourceBean.getResourceDetails(1, "", "", "", "", "", "", "", "", rejectedIdsDto);

        assertNotNull(resourceTableInfoDto);
        assertEquals(0, resourceTableInfoDto.getMaxPageNumber());
        assertTrue(resourceTableInfoDto.getTableResources().isEmpty());
    }

    @Test
    public void testCalculateMaximumPageTableResources() {
        int maxPage = resourceBean.calculateMaximumPageTableResources(16);
        assertEquals(2, maxPage);
    }

    @Test
    public void testGetAllTypes() {
        List<ResourceTypeENUM> types = resourceBean.getAllTypes();
        assertNotNull(types);
        assertEquals(ResourceTypeENUM.values().length, types.size());
    }

    @Test
    public void testGetAllBrands() {
        when(resourceDao.getAllBrands()).thenReturn(Collections.singletonList("TestBrand"));
        List<String> brands = resourceBean.getAllBrands();
        assertNotNull(brands);
        assertEquals(1, brands.size());
    }

    @Test
    public void testCreateResource() {
        ResourceCreationDto resourceCreationDto = new ResourceCreationDto();
        resourceCreationDto.setName("TestResource");
        resourceCreationDto.setBrand("TestBrand");
        resourceCreationDto.setType("COMPONENT");
        resourceCreationDto.setSupplierName("TestSupplier");

        when(resourceDao.findResourceByDetails(anyString(), anyString(), any(), anyString())).thenReturn(null);
        when(supplierDao.findByName(anyString())).thenReturn(new SupplierEntity());

        boolean result = resourceBean.createResource(resourceCreationDto);
        assertTrue(result);
        verify(resourceDao, times(1)).persist(any(ResourceEntity.class));
        verify(resourceSupplierDao, times(1)).persist(any(ResourceSupplierEntity.class));
    }

    @Test
    public void testGetResourceFiltersDto() {
        when(supplierDao.getAllNames()).thenReturn(Collections.singletonList("TestSupplier"));
        when(resourceDao.getAllBrands()).thenReturn(Collections.singletonList("TestBrand"));
        when(resourceDao.getAllNames()).thenReturn(Collections.singletonList("TestResource"));

        ResourceFiltersDto filtersDto = resourceBean.getResourceFiltersDto(true, true);

        assertNotNull(filtersDto);
        assertEquals(1, filtersDto.getSuppliers().size());
        assertEquals(1, filtersDto.getBrands().size());
        assertEquals(1, filtersDto.getNames().size());
    }

    @Test
    public void testGetResourceById() {
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setName("TestResource");
        resourceEntity.setType(ResourceTypeENUM.COMPONENT);
        resourceEntity.setBrand("TestBrand");

        when(resourceDao.findById(anyInt())).thenReturn(resourceEntity);

        ResourceDto resourceDto = resourceBean.getResourceById(1);

        assertNotNull(resourceDto);
        assertEquals("TestResource", resourceDto.getName());
    }

    @Test
    public void testSetResourceDtoSupplier() {
        ResourceDto resourceDto = new ResourceDto();
        SupplierEntity supplier = new SupplierEntity();
        supplier.setName("TestSupplier");
        supplier.setContact("123456789");

        when(supplierDao.findByName(anyString())).thenReturn(supplier);

        resourceBean.setResourceDtoSupplier(resourceDto, "TestSupplier");

        assertEquals("TestSupplier", resourceDto.getSupplierName());
        assertEquals("123456789", resourceDto.getSupplierContact());
    }

    @Test
    public void testEditResource() {
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setName("TestResource");
        resourceDto.setBrand("TestBrand");
        resourceDto.setType(ResourceTypeENUM.COMPONENT);
        resourceDto.setSupplierName("TestSupplier");
        resourceDto.setOldSupplierName("OldSupplier");
        resourceDto.setId(1);

        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setName("TestSupplier");

        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setId(1);

        when(resourceDao.findById(anyInt())).thenReturn(resourceEntity);
        when(supplierDao.findByName(anyString())).thenReturn(supplierEntity);
        when(resourceDao.doesResourceExistWithId(anyString(), anyString(), any(), anyString(), anyInt())).thenReturn(false);

        boolean result = resourceBean.editResource(resourceDto);
        assertTrue(result);
        verify(resourceDao, times(1)).merge(any(ResourceEntity.class));
    }
    @Test
    public void testEditResource_Unsuccessful() {
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setName("TestResource");
        resourceDto.setBrand("TestBrand");
        resourceDto.setType(ResourceTypeENUM.COMPONENT);
        resourceDto.setSupplierName("TestSupplier");
        resourceDto.setOldSupplierName("OldSupplier");
        resourceDto.setId(1);

        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setName("TestSupplier");

        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setId(1);

        when(resourceDao.findById(anyInt())).thenReturn(resourceEntity);
        when(supplierDao.findByName(anyString())).thenReturn(supplierEntity);
        when(resourceDao.doesResourceExistWithId(anyString(), anyString(), any(), anyString(), anyInt())).thenReturn(false);
        when(resourceDao.doesResourceExist(anyString(), anyString(), any(), anyString())).thenReturn(true);

        boolean result = resourceBean.editResource(resourceDto);
        assertFalse(result);
        verify(resourceDao, never()).merge(any(ResourceEntity.class));
    }
    @Test
    public void testGetProjectResources() {
        ProjectResourceEntity projectResourceEntity = new ProjectResourceEntity();
        ResourceSupplierEntity resourceSupplierEntity = new ResourceSupplierEntity();
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setName("TestResource");
        resourceEntity.setBrand("TestBrand");
        resourceEntity.setType(ResourceTypeENUM.COMPONENT);
        resourceEntity.setId(1);
        resourceSupplierEntity.setResource(resourceEntity);
        resourceSupplierEntity.setSupplier(new SupplierEntity());
        projectResourceEntity.setResources(resourceSupplierEntity);

        when(projectDao.getIdBySystemName(anyString())).thenReturn(1);
        when(projectResourceDao.findResourcesById(anyInt())).thenReturn(Collections.singletonList(projectResourceEntity));
        when(resourceDao.findById(anyInt())).thenReturn(resourceEntity);

        List<ResourceTableDto> resources = resourceBean.getProjectResources("testProject");
        assertNotNull(resources);
        assertFalse(resources.isEmpty());
    }
    @Test
    public void testGetProjectResources_Unsuccessful() {
        // Simulate the projectDao returning a valid project ID but no resources being found
        when(projectDao.getIdBySystemName(anyString())).thenReturn(1);
        when(projectResourceDao.findResourcesById(1)).thenReturn(Collections.emptyList());

        List<ResourceTableDto> resources = resourceBean.getProjectResources("testProject");
        assertTrue(resources.isEmpty());
    }
    @Test
    public void testGetResourcesProjectProfile() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName("TestProject");
        projectEntity.setStatus(StateProjectENUM.IN_PROGRESS);

        ProjectResourceEntity projectResourceEntity = new ProjectResourceEntity();
        ResourceSupplierEntity resourceSupplierEntity = new ResourceSupplierEntity();
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setName("TestResource");
        resourceEntity.setBrand("TestBrand");
        resourceEntity.setType(ResourceTypeENUM.COMPONENT);
        resourceEntity.setId(1);
        resourceSupplierEntity.setResource(resourceEntity);
        resourceSupplierEntity.setSupplier(new SupplierEntity());
        projectResourceEntity.setResources(resourceSupplierEntity);

        when(userBean.getUserInProjectENUM(anyString(), anyString())).thenReturn(UserInProjectTypeENUM.MEMBER);
        when(projectDao.findBySystemName(anyString())).thenReturn(projectEntity);
        when(projectResourceDao.findResourcesById(anyInt())).thenReturn(Collections.singletonList(projectResourceEntity));
        when(resourceDao.findById(anyInt())).thenReturn(resourceEntity);

        ResourcesProjectProfileDto profileDto = resourceBean.getResourcesProjectProfile("sessionId", "testProject");

        assertNotNull(profileDto);
        assertEquals(UserInProjectTypeENUM.MEMBER, profileDto.getUserInProjectTypeENUM());
        assertEquals("TestProject", profileDto.getProjectName());
        assertEquals(StateProjectENUM.IN_PROGRESS.toString(), profileDto.getProjectStatus());
    }
    @Test
    public void testGetResourcesProjectProfile_Unsuccessful() {
        when(projectDao.findBySystemName(anyString())).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            resourceBean.getResourcesProjectProfile("sessionId", "testProject");
        });
    }
}