import "./Tables.css";
import { formatStatus, formatLab } from "../../utilities/converters";
import TablePagination from "../paginations/TablePagination";
import { InputGroup, Form, Button } from "react-bootstrap";
import Cookies from "js-cookie";
import { useNavigate } from "react-router-dom";
import { useCurrentPng } from "recharts-to-png";
import { useMediaQuery } from "react-responsive";
import { Trans, t } from "@lingui/macro";
import React from "react";

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
  const NUMBER_OF_PROJECTS_PAGE = 8;
  const userType = Cookies.get("user-userType");
  const language = Cookies.get("user-language");
  const isTablet = useMediaQuery({ query: "(max-width: 1024px)" });
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const navigate = useNavigate();
  const defaultPhoto =
    "https://cdn.pixabay.com/photo/2016/03/29/08/48/project-1287781_1280.jpg";

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
        <InputGroup
          className={isMobile && "projects-filters-input"}
          style={{ width: isMobile ? "100%" : "50%" }}
        >
          {isMobile && (
            <div className="flex-row-button-filters">
              <button
                className="submit-button"
                id="btn-add-project-table-projects"
                onClick={() => navigate("/create-project/info")}
              >
                <Trans>Add Project</Trans>
              </button>

              {userType === "ADMIN" && (
                <button
                  className="submit-button"
                  id="btn-projects-statistics-table-projects"
                  onClick={() => navigate("/statistics")}
                >
                  <Trans>Projects Statistics</Trans>
                </button>
              )}
            </div>
          )}
          <div className="projects-filters">
            <Form.Control
              type="text"
              placeholder={t`Search for project name`}
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              style={{ borderRadius: "10px", cursor: "text" }}
              className="custom-focus"
            />
            <Button
              variant="primary"
              onClick={() => {
                setKeywordButton(!keywordButton);
                setCurrentPage(1);
                setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
              }}
              id="primary-btn-boot"
            >
              <Trans>Search</Trans>
            </Button>
            <Button
              variant="secondary"
              onClick={() => {
                handleClearSearch();
                setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
              }}
              style={{ borderRadius: "10px", width: "200px" }}
            >
              <Trans>Clear</Trans>{" "}
              {!isTablet && language === "ENGLISH" && "Search"}
            </Button>
          </div>
        </InputGroup>
      </div>
      <table
        className="centered-table"
        style={{ fontSize: isMobile && "12px" }}
      >
        <thead>
          <tr>
            <th colSpan="2" style={{ width: "25%" }} onClick={sortByName}>
              <span style={{ marginRight: "10px" }}>
                <Trans>Project</Trans>
              </span>
              {nameAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                nameAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
            <th style={{ width: "15%" }} onClick={sortByStatus}>
              <span style={{ marginRight: "10px" }}>
                <Trans>Status</Trans>
              </span>
              {statusAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                statusAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
            <th style={{ width: "15%" }} onClick={sortByLab}>
              <span style={{ marginRight: "10px" }}>
                <Trans>Lab</Trans>
              </span>
              {labAsc ? (
                <i class="fas fa-arrow-up fa-xs"></i>
              ) : (
                labAsc === false && <i class="fas fa-arrow-down fa-xs"></i>
              )}
            </th>
            {!isTablet && (
              <>
                <th style={{ width: "15%" }} onClick={sortByStartDate}>
                  <span style={{ marginRight: "10px" }}>
                    <Trans>Start date</Trans>
                  </span>
                  {startDateAsc ? (
                    <i class="fas fa-arrow-up fa-xs"></i>
                  ) : (
                    startDateAsc === false && (
                      <i class="fas fa-arrow-down fa-xs"></i>
                    )
                  )}
                </th>
                <th style={{ width: "15%" }} onClick={sortByEndDate}>
                  <span style={{ marginRight: "10px" }}>
                    <Trans>End date</Trans>
                  </span>
                  {endDateAsc ? (
                    <i class="fas fa-arrow-up fa-xs"></i>
                  ) : (
                    endDateAsc === false && (
                      <i class="fas fa-arrow-down fa-xs"></i>
                    )
                  )}
                </th>
                <th style={{ width: "15%" }}>
                  <Trans>Members</Trans>
                </th>{" "}
              </>
            )}
          </tr>
        </thead>

        {projects.length === 0 ? (
          <tr>
            <td colspan="7">
              <div className="no-results no-results-align">
                <Trans>No projects found matching your criteria.</Trans>
              </div>
            </td>
          </tr>
        ) : (
          <tbody id="table-projects-body">
            {projects.map(
              (project, index) => (
                (
                  <tr
                    onClick={() =>
                      navigate(`/project/${project.systemProjectName}/info`)
                    }
                    key={index}
                    className={project.member && "is-member-project"}
                  >
                    <td
                      style={{
                        width: "40px",
                        textAlign: "end",
                        paddingRight: "15px",
                      }}
                    >
                      <div
                        className="project-img-table"
                        style={{
                          backgroundImage:
                            project.photo == null
                              ? `url(${defaultPhoto})`
                              : `url(${project.photo})`,
                          backgroundSize: "cover",
                          backgroundPosition: "center",
                        }}
                      ></div>
                    </td>
                    <td
                      style={{
                        fontWeight: "bold",
                        paddingLeft: "0px",
                        textAlign: "start",
                      }}
                    >
                      {project.name}
                    </td>

                    <td style={{ color: statusColor(project.status) }}>
                      {formatStatus(project.status)}
                    </td>
                    <td>{formatLab(project.lab)}</td>
                    {!isTablet && (
                      <>
                        <td>{project.startDate}</td>
                        <td>{project.finalDate}</td>

                        <td>
                          {project.numberOfMembers} / {project.maxMembers}{" "}
                        </td>
                      </>
                    )}
                  </tr>
                )
              )
            )}
            {Array(NUMBER_OF_PROJECTS_PAGE - projects.length)
              .fill()
              .map((index) => (
                <tr key={index + projects.length}>
                  <td></td>
                  <td className="row-no-content"></td>
                  <td className="row-no-content"></td>
                  <td className="row-no-content"></td>
                  {!isTablet && (
                    <>
                      <td className="row-no-content"></td>
                      <td className="row-no-content"></td>
                      <td className="row-no-content"></td>
                    </>
                  )}
                </tr>
              ))}
          </tbody>
        )}
      </table>
      <div id="align-div-buttons">
        <div id="flex-row-table-projects">
          <div className="row-btns-table-projects-1">
            {!isMobile && (
              <button
                className="submit-button"
                id="btn-add-project-table-projects"
                onClick={() => navigate("/create-project/info")}
              >
                <Trans>Add Project</Trans>
              </button>
            )}
          </div>

          <div className="tablePagination-div">
            {projects.length > 0 && (
              <TablePagination
                totalPages={totalPages}
                currentPage={currentPage}
                setCurrentPage={setCurrentPage}
                setNavigateTableTrigger={setNavigateTableProjectsTrigger}
              />
            )}
          </div>

          <div className="row-btns-table-projects-2">
            {!isMobile && (
              <>
                {userType === "ADMIN" && (
                  <button
                    className="submit-button"
                    id="btn-projects-statistics-table-projects"
                    onClick={() => navigate("/statistics")}
                  >
                    <Trans>Projects Statistics</Trans>
                  </button>
                )}
              </>
            )}
          </div>
        </div>
      </div>
    </>
  );
}
