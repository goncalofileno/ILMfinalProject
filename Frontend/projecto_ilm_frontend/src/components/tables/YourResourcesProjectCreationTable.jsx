import "./Tables.css";
import { formatResourceType } from "../../utilities/converters";
import componentIcon from "../../resources/icons/other/application-control.png";
import Cookies from "js-cookie";
import { Trans, t } from "@lingui/macro";
import { useMediaQuery } from "react-responsive";

export default function YourResourcesProjectCreationTable({
  resources,
  setResources,
  setIsModalActive,
  setResourceSupplier,
  setResourceId,
  setResourcesTableTrigger,
  setSelectedResource,
  setSelectedResources,
}) {
  const isTablet = useMediaQuery({ query: "(max-width: 1000px)" });
  const NUMBER_OF_RESOURCES_PAGE = 8;

  const handleRowClick = (id, supplier, resource) => {
    setResourceId(id);
    setSelectedResource(resource);
    setResourceSupplier(supplier);
    setSelectedResources(resources);
    setIsModalActive(true);
  };

  const removeResource = (e, id) => {
    e.stopPropagation();
    setResources(
      resources.filter((resource) => resource.resourceSupplierId !== id)
    );

    setResourcesTableTrigger((prev) => !prev);
  };

  return (
    <table className="table-resources-proj-creation your-resources-table">
      <thead>
        <tr>
          <th style={{ width: !isTablet ? "25%" : "35%" }}><Trans>Name</Trans></th>
          {!isTablet && <th style={{ width: "26.5%" }}><Trans>Type</Trans></th>}
          <th style={{ width: !isTablet ? "21%" : "31%" }}><Trans>Brand</Trans></th>
          <th colSpan={2} style={{ width: !isTablet ? "27.5%" : "34%" }}>
            <Trans>Supplier</Trans>
          </th>
        </tr>
      </thead>

      <tbody
        id="body-table-your-resources"
        style={{ overflow: resources.length < 8 && "hidden" }}
      >
        {resources.map((resource, index) => (
          <tr
            key={index}
            onClick={() =>
              handleRowClick(resource.id, resource.supplier, resource)
            }
          >
            <td style={{ width: !isTablet ? "25%" : "35%" }}>
              {resource.name}
            </td>
            {!isTablet && (
              <td style={{ width: "26.5%" }}>
                {" "}
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
            <td style={{ width: !isTablet ? "21%" : "31%" }}>
              {resource.brand}
            </td>
            <td style={{ width: !isTablet ? "21.5%" : "28%" }}>
              {resource.supplier}
            </td>
            <td className="last-column-table-resources" style={{ width: "6%" }}>
              <button
                className="remove-button"
                onClick={(e) => removeResource(e, resource.resourceSupplierId)}
                style={{ visibility: isTablet && "visible" }}
              >
                <i class="fas fa-minus"></i>
              </button>
            </td>
          </tr>
        ))}
        {resources.length < NUMBER_OF_RESOURCES_PAGE &&
          Array(NUMBER_OF_RESOURCES_PAGE - resources.length)
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
