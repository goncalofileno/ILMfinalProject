import "./Tables.css";
import { InputGroup, Form, Button } from "react-bootstrap";
import TablePagination from "../paginations/TablePagination";
import componentIcon from "../../resources/icons/other/application-control.png";
import { formatResourceType } from "../../utilities/converters";
import { Trans, t } from "@lingui/macro";

export default function ResourcesTable({
  resources,
  totalPages,
  currentPage,
  setCurrentPage,
  keyword,
  setKeyword,
  setNavigateTableResourcesTrigger,
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
}) {
  const NUMBER_OF_RESOURCES_PAGE = 8;

  const handleClick = (e) => {
    setCurrentPage(1);
    setNavigateTableResourcesTrigger((prev) => !prev);
  };

  const handleClean = (e) => {
    setKeyword("");
    setCurrentPage(1);
    setNavigateTableResourcesTrigger((prev) => !prev);
  };

  const handleRowClick = (id, supplier) => {
    setResourceId(id);
    setResourceSupplier(supplier);
    setIsModalActive(true);
  };
  return (
    <>
      <div className="search-table-div">
        <InputGroup className="mail-filters" style={{ width: "50%" }}>
          <Form.Control
            type="text"
            placeholder={t`Search mails`}
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            style={{ borderRadius: "10px", cursor: "text" }}
            className="custom-focus"
          />
          <Button variant="primary" onClick={handleClick} id="primary-btn-boot">
          <Trans>Search</Trans>
          </Button>
          <Button
            variant="secondary"
            onClick={handleClean}
            style={{ borderRadius: "10px" }}
          >
            <Trans>Clear Search</Trans>
          </Button>
        </InputGroup>
      </div>
      <table className="centered-table">
        <thead>
          <tr>
            <th onClick={sortByName} style={{ width: "25%" }}>
              {" "}
              <span style={{ marginRight: "10px" }}><Trans>Name</Trans></span>
              {nameAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                nameAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
            <th onClick={sortByType} style={{ width: "25%" }}>
              {" "}
              <span style={{ marginRight: "10px" }}><Trans>Type</Trans></span>
              {typeAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                typeAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
            <th onClick={sortByBrand} style={{ width: "25%" }}>
              {" "}
              <span style={{ marginRight: "10px" }}><Trans>Brand</Trans></span>
              {brandAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                brandAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
            <th onClick={sortBySupplier} style={{ width: "25%" }}>
              {" "}
              <span style={{ marginRight: "10px" }}><Trans>Supplier</Trans></span>
              {supplierAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                supplierAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
          </tr>
        </thead>
        {resources.length === 0 ? (
          <tr>
            <td colspan="4">
              <div className="no-results no-results-align">
              <Trans>No resources found matching your criteria.</Trans>
              </div>
            </td>
          </tr>
        ) : (
          <tbody>
            {" "}
            {resources.map((resource, index) => (
              <tr
                key={index}
                onClick={() => handleRowClick(resource.id, resource.supplier)}
              >
                <td style={{ fontWeight: "bold" }}>{resource.name}</td>
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
                <td>{resource.brand}</td>
                <td>{resource.supplier}</td>
              </tr>
            ))}
            {Array(NUMBER_OF_RESOURCES_PAGE - resources.length)
              .fill()
              .map((index) => (
                <tr key={index + resources.length}>
                  <td className="row-no-content"></td>
                  <td className="row-no-content"></td>
                  <td className="row-no-content"></td>
                  <td className="row-no-content"></td>
                </tr>
              ))}
          </tbody>
        )}
      </table>
      <div id="align-div-buttons">
        <div id="flex-row-table-projects">
          <div className="row-btns-table-projects-1">
            <button
              className="submit-button"
              id="btn-add-project-table-projects"
              onClick={() => setIsModalActive(true)}
            >
              <Trans>Add Resource</Trans>
            </button>
          </div>
          <div className="tablePagination-div">
            {resources.length > 0 && (
              <TablePagination
                totalPages={totalPages}
                currentPage={currentPage}
                setCurrentPage={setCurrentPage}
                setNavigateTableTrigger={setNavigateTableResourcesTrigger}
              />
            )}
          </div>
          <div className="row-btns-table-projects-2"></div>
        </div>
      </div>
    </>
  );
}
