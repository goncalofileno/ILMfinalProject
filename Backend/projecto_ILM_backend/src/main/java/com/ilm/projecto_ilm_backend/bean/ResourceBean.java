package com.ilm.projecto_ilm_backend.bean;

import com.ilm.projecto_ilm_backend.ENUMS.ResourceTypeENUM;
import com.ilm.projecto_ilm_backend.dao.*;
import com.ilm.projecto_ilm_backend.dto.resource.*;
import com.ilm.projecto_ilm_backend.dto.user.RejectedIdsDto;
import com.ilm.projecto_ilm_backend.entity.*;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
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

    @Inject
    private ResourceSupplierDao resourceSupplierDao;

    @Inject
    private ProjectDao projectDao;

    @Inject
    private ProjectResourceDao projectResourceDao;

    @Inject
    UserBean userBean;

    private static final int NUMBER_OF_RESOURCES_PER_PAGE=8;

    /**
     * Creates default resources if they do not already exist in the database.
     */
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
            ResourceSupplierEntity resourceSupplierEntity = new ResourceSupplierEntity();
            resourceSupplierEntity.setResource(resource);
            resourceSupplierEntity.setSupplier(supplier);
            resourceSupplierEntity.setDeleted(false);
            resourceDao.persist(resource);
            resourceSupplierDao.persist(resourceSupplierEntity);

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
            ResourceSupplierEntity resourceSupplierEntity = new ResourceSupplierEntity();
            resourceSupplierEntity.setResource(resource);
            resourceSupplierEntity.setSupplier(supplier);
            resourceSupplierEntity.setDeleted(false);
            resourceDao.persist(resource);
            resourceSupplierDao.persist(resourceSupplierEntity);
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
            ResourceSupplierEntity resourceSupplierEntity = new ResourceSupplierEntity();
            resourceSupplierEntity.setResource(resource);
            resourceSupplierEntity.setSupplier(supplier);
            resourceSupplierEntity.setDeleted(false);
            resourceDao.persist(resource);
            resourceSupplierDao.persist(resourceSupplierEntity);

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
            SupplierEntity supplier2 = supplierDao.findById(2);
            ResourceSupplierEntity resourceSupplierEntity = new ResourceSupplierEntity();
            resourceSupplierEntity.setResource(resource);
            resourceSupplierEntity.setSupplier(supplier);
            resourceSupplierEntity.setDeleted(false);
            ResourceSupplierEntity resourceSupplierEntity2 = new ResourceSupplierEntity();
            resourceSupplierEntity2.setResource(resource);
            resourceSupplierEntity2.setSupplier(supplier2);
            resourceSupplierEntity2.setDeleted(false);
            resourceDao.persist(resource);
            resourceSupplierDao.persist(resourceSupplierEntity);
            resourceSupplierDao.persist(resourceSupplierEntity2);

        }

    }

    /**
     * Retrieves detailed information about resources for display in a paginated table format.
     *
     * @param page The page number of the resource table to retrieve.
     * @param brand The brand filter for the resources.
     * @param type The type filter for the resources.
     * @param supplierName The supplier name filter for the resources.
     * @param searchKeyword The keyword to search within the resources.
     * @param nameAsc The sorting order for resource names.
     * @param typeAsc The sorting order for resource types.
     * @param brandAsc The sorting order for resource brands.
     * @param supplierAsc The sorting order for resource suppliers.
     * @param rejectedIdsDto DTO containing IDs of resources to exclude from the result.
     * @return A DTO containing the details of resources for the specified page and filters.
     */
    public ResourceTableInfoDto getResourceDetails(int page, String brand, String type, String supplierName, String searchKeyword, String nameAsc, String typeAsc, String brandAsc, String supplierAsc, RejectedIdsDto rejectedIdsDto) {

        ResourceTypeENUM typeEnum = null;
        Integer supplierId=null;

        if(brand.equals("")){
            brand=null;
        }
        if(!type.equals("")&& type != null){
           typeEnum = ResourceTypeENUM.valueOf(type);
        }
        if(!supplierName.equals("") && supplierName != null){
            int potentialSupplierId=supplierDao.findIdByName(supplierName);
            if(potentialSupplierId!=-1) supplierId=Integer.valueOf(supplierDao.findIdByName(supplierName));
        }
        if(searchKeyword.equals("")){
            searchKeyword=null;
        }

        if(rejectedIdsDto == null){
            ArrayList<Integer> rejectedIds = new ArrayList<>();
            rejectedIdsDto = new RejectedIdsDto();
            rejectedIdsDto.setRejectedIds(rejectedIds);
        }

        List <Object[]> resources = resourceDao.getResourceDetails(page, NUMBER_OF_RESOURCES_PER_PAGE, brand, typeEnum, supplierId, searchKeyword,  nameAsc, typeAsc, brandAsc,  supplierAsc, rejectedIdsDto);
        ArrayList<ResourceTableDto> resourceTableDtos = new ArrayList<>();
        ResourceTableInfoDto resourceTableInfoDto = new ResourceTableInfoDto();

        for (Object[] resource : resources) {
            ResourceTableDto resourceTableDto = new ResourceTableDto();
            resourceTableDto.setName((String) resource[0]);
            resourceTableDto.setBrand((String) resource[1]);
            resourceTableDto.setType(resource[2].toString());
            resourceTableDto.setSupplier(supplierDao.findNameById((int) resource[3]));
            resourceTableDto.setId((int) resource[4]);
            resourceTableDto.setResourceSupplierId((int) resource[5]);
            resourceTableDtos.add(resourceTableDto);
        }

        int numberOfResources = resourceDao.getNumberOfResources(brand, typeEnum, supplierId, searchKeyword, rejectedIdsDto);
        resourceTableInfoDto.setTableResources(resourceTableDtos);
        resourceTableInfoDto.setMaxPageNumber(calculateMaximumPageTableResources(numberOfResources));

        return resourceTableInfoDto;
    }

    /**
     * Calculates the maximum number of pages for the resources table based on the total number of resources.
     *
     * @param numberOfResources The total number of resources.
     * @return The maximum number of pages.
     */
    public int calculateMaximumPageTableResources(int numberOfResources){
        return (int) Math.ceil((double) numberOfResources/NUMBER_OF_RESOURCES_PER_PAGE);
    }

    /**
     * Retrieves all resource types available in the system.
     *
     * @return A list of all resource types.
     */
    public ArrayList<ResourceTypeENUM> getAllTypes() {
        ArrayList<ResourceTypeENUM> types = new ArrayList<>();
        for (ResourceTypeENUM type : ResourceTypeENUM.values()) {
            types.add(type);
        }
        return types;
    }

    /**
     * Retrieves all brands associated with resources in the system.
     *
     * @return A list of all resource brands.
     */
    public List<String> getAllBrands() {
        return resourceDao.getAllBrands();
    }

    /**
     * Creates a new resource in the system based on the provided DTO.
     *
     * @param resourceCreationDto DTO containing the details of the resource to create.
     * @return true if the resource was successfully created, false otherwise.
     */
    public boolean createResource(ResourceCreationDto resourceCreationDto) {
            ResourceEntity resourceEntity;
            resourceEntity=resourceDao.findResourceByDetails(resourceCreationDto.getName(), resourceCreationDto.getBrand(), ResourceTypeENUM.valueOf(resourceCreationDto.getType()),resourceCreationDto.getSupplierName());
            SupplierEntity supplier = supplierDao.findByName(resourceCreationDto.getSupplierName());
            if(supplier==null){
                supplier = new SupplierEntity();
                String supplierValidateName=resourceCreationDto.getSupplierName().substring(0,1).toUpperCase() + resourceCreationDto.getSupplierName().substring(1);
                supplier.setName(supplierValidateName);
                supplier.setContact(resourceCreationDto.getSupplierContact());
                supplierDao.persist(supplier);
            }
            if(resourceEntity==null){
                resourceEntity = new ResourceEntity();
                resourceEntity.setName(resourceCreationDto.getName());
                resourceEntity.setDescription(resourceCreationDto.getDescription());
                resourceEntity.setObservation(resourceCreationDto.getObservations());
                resourceEntity.setBrand(resourceCreationDto.getBrand());
                resourceEntity.setSerialNumber(resourceCreationDto.getSerialNumber());
                resourceEntity.setType(ResourceTypeENUM.valueOf(resourceCreationDto.getType()));
                ResourceSupplierEntity resourceSupplierEntity = new ResourceSupplierEntity();
                resourceSupplierEntity.setResource(resourceEntity);
                resourceSupplierEntity.setSupplier(supplier);
                resourceSupplierEntity.setDeleted(false);
                resourceDao.persist(resourceEntity);
                resourceSupplierDao.persist(resourceSupplierEntity);
                return true;
            }
            else{
                if(resourceDao.resourceHasSupplier(resourceEntity.getId(), supplier.getId())){
                    if(resourceSupplierDao.getIsDeletedByIds(resourceEntity.getId(), supplier.getId())){
                        resourceSupplierDao.updateIsDeleted(resourceEntity.getId(), supplier.getId(), false);
                        return true;
                    }
                    else return false;

                }else {
                    ResourceSupplierEntity resourceSupplierEntity = new ResourceSupplierEntity();
                    resourceSupplierEntity.setResource(resourceEntity);
                    resourceSupplierEntity.setSupplier(supplier);
                    resourceSupplierEntity.setDeleted(false);
                    resourceSupplierDao.persist(resourceSupplierEntity);
                    resourceDao.merge(resourceEntity);
                    return true;
                }
        }
    }

    /**
     * Retrieves filters for resources, including types, suppliers, brands, and optionally names.
     *
     * @param withNames Include resource names in the filters if true.
     * @param withTypes Include resource types in the filters if true.
     * @return A DTO containing the filters for resources.
     */
    public ResourceFiltersDto getResourceFiltersDto(boolean withNames,boolean withTypes){
        ResourceFiltersDto resourceFiltersDto = new ResourceFiltersDto();

        if(withTypes) resourceFiltersDto.setTypes(getAllTypes());
        resourceFiltersDto.setSuppliers(supplierDao.getAllNames());
        resourceFiltersDto.setBrands(resourceDao.getAllBrands());
        if(withNames) resourceFiltersDto.setNames(resourceDao.getAllNames());
        return resourceFiltersDto;
    }

    /**
     * Retrieves the details of a resource by its ID.
     *
     * @param id The ID of the resource.
     * @return A DTO containing the details of the resource.
     */
    public ResourceDto getResourceById(int id){
        ResourceEntity resourceEntity=resourceDao.findById(id);
        ResourceDto resourceDto=new ResourceDto();
        resourceDto.setName(resourceEntity.getName());
        resourceDto.setType(resourceEntity.getType());
        resourceDto.setBrand(resourceEntity.getBrand());
        resourceDto.setSerialNumber(resourceEntity.getBrand());
        resourceDto.setDescription(resourceEntity.getDescription());
        resourceDto.setObservation(resourceEntity.getObservation());

        return resourceDto;
    }

    /**
     * Sets the supplier details in a resource DTO.
     *
     * @param resourceDto The resource DTO to update with supplier details.
     * @param supplierName The name of the supplier.
     */
    public void setResourceDtoSupplier(ResourceDto resourceDto, String supplierName){
        SupplierEntity supplier =supplierDao.findByName(supplierName);
        resourceDto.setSupplierName(supplier.getName());
        resourceDto.setSupplierContact(supplier.getContact());
    }

    /**
     * Edits an existing resource in the system based on the provided DTO.
     *
     * @param resourceDto DTO containing the updated details of the resource.
     * @return true if the resource was successfully updated, false otherwise.
     */
    public boolean editResource(ResourceDto resourceDto){
        SupplierEntity supplier = supplierDao.findByName(resourceDto.getSupplierName());
        ResourceEntity resourceEntity = resourceDao.findById(resourceDto.getId());
        if(resourceEntity!=null) {
            if(resourceDao.doesResourceExistWithId(resourceDto.getName(), resourceDto.getBrand(), resourceDto.getType(), resourceDto.getSupplierName(), resourceDto.getId())){
                resourceEntity.setSerialNumber(resourceDto.getSerialNumber());
                resourceEntity.setDescription(resourceDto.getDescription());
                resourceEntity.setObservation(resourceDto.getObservation());
                resourceDao.merge(resourceEntity);
                updateSupplier(resourceDto, supplier);
                return true;
            }
            if(resourceDao.doesResourceExist(resourceDto.getName(), resourceDto.getBrand(), resourceDto.getType(), resourceDto.getSupplierName())){
                return false;
            }
            else {
                resourceEntity.setName(resourceDto.getName());
                resourceEntity.setType(resourceDto.getType());
                resourceEntity.setBrand(resourceDto.getBrand());
                resourceEntity.setSerialNumber(resourceDto.getSerialNumber());
                resourceEntity.setDescription(resourceDto.getDescription());
                resourceEntity.setObservation(resourceDto.getObservation());
                resourceDao.merge(resourceEntity);
                updateSupplier(resourceDto, supplier);
                return true;
            }
        }
        else return false;
    }

    /**
     * Updates the supplier for a resource.
     *
     * @param resourceDto The DTO containing the resource and new supplier details.
     * @param supplier The entity of the new supplier.
     */
    public void updateSupplier(ResourceDto resourceDto, SupplierEntity supplier ){
        if(!resourceDto.getOldSupplierName().equals(resourceDto.getSupplierName())){
            resourceSupplierDao.updateIsDeleted(resourceDto.getId(), supplierDao.findIdByName(resourceDto.getOldSupplierName()), true);

            if(supplier==null){
                supplier = new SupplierEntity();
                supplier.setName(resourceDto.getSupplierName());
                supplier.setContact(resourceDto.getSupplierContact());
                supplierDao.persist(supplier);
            }
            if(resourceSupplierDao.doesRelationExist(resourceDto.getId(),supplier.getId())){
                resourceSupplierDao.updateIsDeleted(resourceDto.getId(), supplier.getId(), false);
            }else {
                ResourceSupplierEntity resourceSupplierEntity = new ResourceSupplierEntity();
                resourceSupplierEntity.setResource(resourceDao.findById(resourceDto.getId()));
                resourceSupplierEntity.setSupplier(supplier);
                resourceSupplierEntity.setDeleted(false);
                resourceSupplierDao.persist(resourceSupplierEntity);
            }
        }
    }

    /**
     * Retrieves a list of resources associated with a specific project.
     *
     * @param projectSystemName The system name of the project.
     * @return A list of DTOs containing the details of resources associated with the project.
     */
    public List<ResourceTableDto> getProjectResources(String projectSystemName) {
        try {
            int projectId = projectDao.getIdBySystemName(projectSystemName);

            List<ProjectResourceEntity> projectResources = projectResourceDao.findResourcesById(projectId);

            List<ResourceTableDto> resourceTableDtos = new ArrayList<>();
            for (ProjectResourceEntity projectResource : projectResources) {
                ResourceTableDto resourceTableDto = new ResourceTableDto();
                ResourceEntity resource=resourceDao.findById(projectResource.getResources().getResource().getId());
                resourceTableDto.setName(resource.getName());
                resourceTableDto.setBrand(resource.getBrand());
                resourceTableDto.setType(resource.getType().toString());
                resourceTableDto.setSupplier(projectResource.getResources().getSupplier().getName());
                resourceTableDto.setId(resource.getId());
                resourceTableDto.setResourceSupplierId(projectResource.getResources().getId());
                resourceTableDtos.add(resourceTableDto);
            }
            return resourceTableDtos;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves resources and user role information for a project's profile page.
     *
     * @param sessionId The session ID of the user requesting the information.
     * @param projectSystemName The system name of the project.
     * @return A DTO containing resources and user role information for the project.
     */
    public ResourcesProjectProfileDto getResourcesProjectProfile(String sessionId, String projectSystemName){
        ResourcesProjectProfileDto resourcesProjectProfileDto=new ResourcesProjectProfileDto();
        resourcesProjectProfileDto.setResources(getProjectResources(projectSystemName));
        resourcesProjectProfileDto.setUserInProjectTypeENUM(userBean.getUserInProjectENUM(sessionId,projectSystemName));
        ProjectEntity project=projectDao.findBySystemName(projectSystemName);
        resourcesProjectProfileDto.setProjectName(project.getName());
        resourcesProjectProfileDto.setProjectStatus(project.getStatus().toString());
        return resourcesProjectProfileDto;
    }

}
