import "./Tables.css";
import { formatStatus, formatLab } from "../../utilities/converters";
import TablePagination from "../paginations/TablePagination";
import { InputGroup, Form, Button } from "react-bootstrap";

export default function ProjectsTable({
  projects,
  currentPage,
  setCurrentPage,
  totalPages,
  sortByName,
  sortByStatus,
  sortByLab,
  sortByStartDate,
  sortByEndDate,
  keyword,
  setKeyword,
  keywordButton,
  setKeywordButton,
  navigateTableProjectsTrigger,
  setNavigateTableProjectsTrigger,
  nameAsc,
  statusAsc,
  labAsc,
  startDateAsc,
  endDateAsc,
}) {
  const NUMBER_OF_PROJECTS_PAGE = 15;

  function statusColor(status) {
    if (status === "PLANNING") {
      return "rgb(255, 255, 0)";
    } else if (status === "READY") {
      return "rgb(89, 89, 89)";
    } else if (status === "SUBMITTED") {
      return "rgb(134, 89, 45)";
    } else if (status === "APPROVED") {
      return "rgb(51, 153, 255)";
    } else if (status === "IN_PROGRESS") {
      return "blue";
    } else if (status === "CANCELED") {
      return "red";
    } else if (status === "FINISHED") {
      return "rgb(0, 179, 60)";
    } else {
      return "black";
    }
  }

  const handleClearSearch = () => {
    setKeyword("");
    setCurrentPage(1);
    setKeywordButton(!keywordButton);
  };
  return (
    <>
      <div className="search-table-div">
        <InputGroup style={{ width: "50%" }}>
          <Form.Control
            type="text"
            placeholder="Search for title or description"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
            className="custom-focus"
            id="search-table-projects"
          />
          <Button
            variant="primary"
            onClick={() => {
              setKeywordButton(!keywordButton);
              setCurrentPage(1);
              setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
            }}
            style={{
              backgroundColor: "#f39c12",
              borderColor: "#f39c12",
            }}
          >
            Search
          </Button>
          <Button
            variant="secondary"
            onClick={() => {
              handleClearSearch();
              setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
            }}
            style={{ marginLeft: "10px" }}
          >
            Clear Search
          </Button>
        </InputGroup>
      </div>
      <table className="centered-table">
        <thead>
          <tr>
            <th style={{ width: "25%" }} onClick={sortByName}>
              <span style={{ marginRight: "10px" }}>Project</span>
              {nameAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                nameAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
            <th style={{ width: "15%" }} onClick={sortByStatus}>
              <span style={{ marginRight: "10px" }}>Status</span>
              {statusAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                statusAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
            <th style={{ width: "15%" }} onClick={sortByLab}>
              <span style={{ marginRight: "10px" }}>Lab</span>
              {labAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                labAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
            <th style={{ width: "15%" }} onClick={sortByStartDate}>
              <span style={{ marginRight: "10px" }}>Start date</span>
              {startDateAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                startDateAsc === false && (
                  <i class="fas fa-arrow-down fa-xs"></i>
                )
              )}
            </th>
            <th style={{ width: "15%" }} onClick={sortByEndDate}>
              <span style={{ marginRight: "10px" }}>End date</span>
              {endDateAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                endDateAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
            <th style={{ width: "15%" }}>Members</th>
          </tr>
        </thead>
        <tbody>
          {projects.map((project, index) => (
            <tr key={index}>
              <td style={{ fontWeight: "bold" }}>{project.name}</td>

              <td style={{ color: statusColor(project.status) }}>
                {formatStatus(project.status)}
              </td>
              <td>{formatLab(project.lab)}</td>
              <td>{project.startDate}</td>
              <td>{project.finalDate}</td>
              <td>
                {project.numberOfMembers} / {project.maxMembers}{" "}
              </td>
            </tr>
          ))}
          {Array(NUMBER_OF_PROJECTS_PAGE - projects.length)
            .fill()
            .map((index) => (
              <tr key={index + projects.length}>
                <td className="row-no-content"></td>
                <td className="row-no-content"></td>
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
              Add Project
            </button>
          </div>
          <div className="tablePagination-div">
            <TablePagination
              totalPages={totalPages}
              currentPage={currentPage}
              setCurrentPage={setCurrentPage}
              setNavigateTableProjectsTrigger={setNavigateTableProjectsTrigger}
            />
          </div>
          <div className="row-btns-table-projects-2">
            <button
              className="submit-button"
              id="btn-projects-statistics-table-projects"
            >
              Projects Statistics
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
