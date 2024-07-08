import "./Tables.css";
import { formatResourceType } from "../../utilities/converters";
import componentIcon from "../../resources/icons/other/application-control.png";
import Cookies from "js-cookie";
import { useMediaQuery } from "react-responsive";

export default function ResourcesProjectCreationTable({
  resources,
  sortByName,
  sortByType,
  sortByBrand,
  sortBySupplier,
  nameAsc,
  typeAsc,
  brandAsc,
  supplierAsc,
  setIsModalActive,
  setResourceSupplier,
  setResourceId,
  yourResources,
  setYourResources,
  setResourcesTableTrigger,
  setSelectedResource,
}) {
  const isTablet = useMediaQuery({ query: "(max-width: 1000px)" });
  const NUMBER_OF_RESOURCES_PAGE = 8;

  const handleRowClick = (id, supplier) => {
    setResourceId(id);
    setSelectedResource(null);
    setResourceSupplier(supplier);
    setIsModalActive(true);
  };

  const handleAddResource = (
    name,
    type,
    brand,
    supplier,
    id,
    resourceSupplierId,
    event
  ) => {
    event.stopPropagation();
    const resource = {
      name: name,
      type: type,
      brand: brand,
      supplier: supplier,
      id: id,
      resourceSupplierId: resourceSupplierId,
    };

    setYourResources([...yourResources, resource]);

    setResourcesTableTrigger((prev) => !prev);
  };
  return (
    <table className="table-resources-proj-creation">
      <thead>
        <tr>
          <th style={{ width: "25%" }} onClick={sortByName}>
            <span style={{ marginRight: "10px" }}>Name</span>
            {nameAsc ? (
              <i class="fas fa-arrow-up fa-xs"></i>
            ) : (
              nameAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
            )}
          </th>
          {!isTablet && (
            <th style={{ width: "26.5%" }} onClick={sortByType}>
              <span style={{ marginRight: "10px" }}>Type</span>
              {typeAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                typeAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
          )}

          <th style={{ width: "21%" }} onClick={sortByBrand}>
            <span style={{ marginRight: "10px" }}>Brand</span>
            {brandAsc ? (
              <i class="fas fa-arrow-up fa-xs"></i>
            ) : (
              brandAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
            )}
          </th>
          <th colSpan={2} style={{ width: "27.5%" }} onClick={sortBySupplier}>
            {" "}
            <span style={{ marginRight: "10px" }}>Supplier</span>
            {supplierAsc ? (
              <i class="fas fa-arrow-up fa-xs"></i>
            ) : (
              supplierAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
            )}
          </th>
        </tr>
      </thead>
      <tbody>
        {resources.map((resource, index) => (
          <tr
            key={index}
            onClick={() => handleRowClick(resource.id, resource.supplier)}
          >
            <td>{resource.name}</td>
            {!isTablet && (
              <td>
                {formatResourceType(resource.type)}

                {resource.type === "COMPONENT" ? (
                  <i
                    style={{ marginLeft: "12px" }}
                    className="fas fa-cogs fa-lg"
                  ></i>
                ) : (
                  resource.type === "RESOURCE" && (
                    <img
                      style={{
                        marginLeft: "12px",
                        height: "22px",
                        width: "25px",
                      }}
                      src={componentIcon}
                    ></img>
                  )
                )}
              </td>
            )}
            <td>{resource.brand}</td>
            <td>{resource.supplier}</td>
            <td className="last-column-table-resources">
              <button
                className="add-button"
                onClick={(event) => {
                  console.log("resource", resource);
                  handleAddResource(
                    resource.name,
                    resource.type,
                    resource.brand,
                    resource.supplier,
                    resource.id,
                    resource.resourceSupplierId,
                    event
                  );
                }}
                style={{ visibility: isTablet && "visible" }}
              >
                <i class="fas fa-plus"></i>
              </button>
            </td>
          </tr>
        ))}
        {Array(NUMBER_OF_RESOURCES_PAGE - resources.length)
          .fill()
          .map((index) => (
            <tr key={index + resources.length}>
              <td></td>
              {!isTablet && <td></td>}
              <td></td>
              <td></td>
              <td></td>
            </tr>
          ))}
      </tbody>
    </table>
  );
}
