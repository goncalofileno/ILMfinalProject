import "./Tables.css";
import { InputGroup, Form, Button } from "react-bootstrap";
import TablePagination from "../paginations/TablePagination";

import { formatResourceType } from "../../utilities/converters";

export default function ResourcesTable({ resources, totalPages, currentPage }) {
  const NUMBER_OF_RESOURCES_PAGE = 15;

  return (
    <>
      <div className="search-table-div">
        <InputGroup style={{ width: "50%" }}>
          <Form.Control
            type="text"
            placeholder="Search"
            className="custom-focus"
            id="search-table-projects"
          />
          <Button
            variant="primary"
            style={{
              backgroundColor: "#f39c12",
              borderColor: "#f39c12",
            }}
          >
            Search
          </Button>
          <Button variant="secondary" style={{ marginLeft: "10px" }}>
            Clear Search
          </Button>
        </InputGroup>
      </div>
      <table className="centered-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Brand</th>
            <th>Type</th>
            <th>Supplier</th>
          </tr>
        </thead>
        <tbody>
          {" "}
          {resources.map((resource, index) => (
            <tr key={index}>
              <td style={{ fontWeight: "bold" }}>{resource.name}</td>

              <td>{resource.brand}</td>
              <td>{formatResourceType(resource.type)}</td>
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
