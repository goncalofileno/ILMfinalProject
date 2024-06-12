package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;
import com.ilm.projecto_ilm_backend.ENUMS.StateProjectENUM;
import com.ilm.projecto_ilm_backend.dto.resource.ResourceTableDto;
import com.ilm.projecto_ilm_backend.dto.resource.ResourceTableInfoDto;
import com.ilm.projecto_ilm_backend.entity.ResourceEntity;
import com.ilm.projecto_ilm_backend.entity.SupplierEntity;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import com.ilm.projecto_ilm_backend.dao.ResourceDao;
import com.ilm.projecto_ilm_backend.dao.SupplierDao;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * The ResourceBean class is an application scoped bean that is responsible for managing ResourceEntity instances.
 */
@ApplicationScoped
public class ResourceBean {

    /**
     * The ResourceDao instance used for accessing the database.
     */
    @EJB
    private ResourceDao resourceDao;

    /**
     * The SupplierDao instance used for accessing the database.
     */
    @EJB
    private SupplierDao supplierDao;

    private static final int NUMBER_OF_RESOURCES_PER_PAGE=15;


    public void createDefaultResourcesIfNotExistent() {
        if (resourceDao.findById(1) == null) {
            ResourceEntity resource = new ResourceEntity();
            resource.setType(ResourceTypeENUM.COMPONENT);
            resource.setName("Logitech Mouse");
            resource.setDescription("A wireless mouse with 3 buttons and a scroll wheel.");
            resource.setObservation("The mouse is black and has a red light.");
            resource.setBrand("Logitech");
            resource.setSerialNumber("32423-324234-432423");
            SupplierEntity supplier = new SupplierEntity();
            supplier = supplierDao.findById(1);
            List<SupplierEntity> suppliers = new ArrayList<>();
            suppliers.add(supplier);
            resource.setSupplier(suppliers);
            resourceDao.persist(resource);
        }

        if (resourceDao.findById(2) == null) {
            ResourceEntity resource = new ResourceEntity();
            resource.setType(ResourceTypeENUM.COMPONENT);
            resource.setName("Dell Monitor");
            resource.setDescription("A 24-inch monitor with a resolution of 1920x1080.");
            resource.setObservation("The monitor has a VGA and HDMI port.");
            resource.setBrand("Dell");
            resource.setSerialNumber("4978-978978-789789");
            SupplierEntity supplier = new SupplierEntity();
            supplier = supplierDao.findById(2);
            List<SupplierEntity> suppliers = new ArrayList<>();
            suppliers.add(supplier);
            resource.setSupplier(suppliers);
            resourceDao.persist(resource);
        }

        if (resourceDao.findById(3) == null) {
            ResourceEntity resource = new ResourceEntity();
            resource.setType(ResourceTypeENUM.RESOURCE);
            resource.setName("Office License");
            resource.setDescription("A license for Microsoft Office 365.");
            resource.setObservation("The license is valid for 1 year.");
            resource.setBrand("Microsoft");
            resource.setSerialNumber("1234-1234-1234");
            SupplierEntity supplier = new SupplierEntity();
            supplier = supplierDao.findById(3);
            List<SupplierEntity> suppliers = new ArrayList<>();
            suppliers.add(supplier);
            resource.setSupplier(suppliers);
            resourceDao.persist(resource);
        }

        if (resourceDao.findById(4) == null) {
            ResourceEntity resource = new ResourceEntity();
            resource.setType(ResourceTypeENUM.RESOURCE);
            resource.setName("Windows License");
            resource.setDescription("A license for Windows 10.");
            resource.setObservation("The license is valid for 1 year.");
            resource.setBrand("Microsoft");
            resource.setSerialNumber("4321-4321-4321");
            SupplierEntity supplier = new SupplierEntity();
            supplier = supplierDao.findById(4);
            List<SupplierEntity> suppliers = new ArrayList<>();
            suppliers.add(supplier);
            resource.setSupplier(suppliers);

            resourceDao.persist(resource);
        }
    }

    public ResourceTableInfoDto getResourceDetails() {
        List <Object[]> resources = resourceDao.getResourceDetails();
        ArrayList<ResourceTableDto> resourceTableDtos = new ArrayList<>();
        ResourceTableInfoDto resourceTableInfoDto = new ResourceTableInfoDto();

        for (Object[] resource : resources) {
            ResourceTableDto resourceTableDto = new ResourceTableDto();
            resourceTableDto.setName((String) resource[0]);
            resourceTableDto.setBrand((String) resource[1]);
            resourceTableDto.setType(resource[2].toString());
            resourceTableDto.setSupplier(supplierDao.findNameById((int) resource[3]));
            resourceTableDtos.add(resourceTableDto);
        }

        resourceTableInfoDto.setTableResources(resourceTableDtos);
        resourceTableInfoDto.setMaxPageNumber(calculateMaximumPageTableProjects(resourceDao.getNumberOfResources()));
        return resourceTableInfoDto;
    }

    public int calculateMaximumPageTableProjects(int numberOfProjects){
        return (int) Math.ceil((double) numberOfProjects/NUMBER_OF_RESOURCES_PER_PAGE);
    }

    public ArrayList<ResourceTypeENUM> getAllTypes() {
        ArrayList<ResourceTypeENUM> types = new ArrayList<>();
        for (ResourceTypeENUM type : ResourceTypeENUM.values()) {
            types.add(type);
        }
        return types;
    }

    public List<String> getAllBrands() {
        return resourceDao.getAllBrands();
    }
}
