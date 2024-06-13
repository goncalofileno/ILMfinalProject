import "./Tables.css";
import { InputGroup, Form, Button } from "react-bootstrap";
import TablePagination from "../paginations/TablePagination";

import { formatResourceType } from "../../utilities/converters";

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
}) {
  const NUMBER_OF_RESOURCES_PAGE = 15;

  const handleClick = (e) => {
    setCurrentPage(1);
    setNavigateTableResourcesTrigger((prev) => !prev);
  };

  const handleClean = (e) => {
    setKeyword("");
    setCurrentPage(1);
    setNavigateTableResourcesTrigger((prev) => !prev);
  };
  return (
    <>
      <div className="search-table-div">
        <InputGroup style={{ width: "50%" }}>
          <Form.Control
            type="text"
            placeholder="Search for name"
            className="custom-focus"
            id="search-table-projects"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <Button
            variant="primary"
            style={{
              backgroundColor: "#f39c12",
              borderColor: "#f39c12",
            }}
            onClick={handleClick}
          >
            Search
          </Button>
          <Button
            variant="secondary"
            style={{ marginLeft: "10px" }}
            onClick={handleClean}
          >
            Clear Search
          </Button>
        </InputGroup>
      </div>
      <table className="centered-table">
        <thead>
          <tr>
            <th onClick={sortByName}>Name</th>
            <th onClick={sortByType}>Type</th>
            <th onClick={sortByBrand}>Brand</th>
            <th onClick={sortBySupplier}>Supplier</th>
          </tr>
        </thead>
        <tbody>
          {" "}
          {resources.map((resource, index) => (
            <tr key={index}>
              <td style={{ fontWeight: "bold" }}>{resource.name}</td>
              <td>{formatResourceType(resource.type)}</td>
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
      </table>
      <div id="align-div-buttons">
        <div id="flex-row-table-projects">
          <div className="row-btns-table-projects-1">
            <button
              className="submit-button"
              id="btn-add-project-table-projects"
            >
              Add Resource
            </button>
          </div>
          <div className="tablePagination-div">
            <TablePagination
              totalPages={totalPages}
              currentPage={currentPage}
            />
          </div>
          <div className="row-btns-table-projects-2"></div>
        </div>
      </div>
    </>
  );
}
