import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import ProjectsTable from "../components/tables/ProjectsTable";
import AsideProjectsTable from "../components/asides/AsideProjectsTable";
import { getTableProjects } from "../utilities/services";
import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { useNavigate } from "react-router-dom";

const ProjectsPage = () => {
  const query = new URLSearchParams(useLocation().search);
  const navigate = useNavigate();
  const [totalPages, setTotalPages] = useState(null);
  const [currentPage, setCurrentPage] = useState(
    parseInt(query.get("currentPage")) || 1
  );
  const [projects, setProjects] = useState([]);
  const [selectedLab, setSelectedLab] = useState(
    query.get("selectedLab") || ""
  );
  const [selectedStatus, setSelectedStatus] = useState(
    query.get("selectedStatus") || ""
  );
  const [slotsAvailable, setSlotsAvailable] = useState(
    query.get("slotsAvailable") === "true"
  );
  const [nameAsc, setNameAsc] = useState(query.get("nameAsc") || "");
  const [statusAsc, setStatusAsc] = useState(query.get("statusAsc") || "");
  const [labAsc, setLabAsc] = useState(query.get("labAsc") || "");
  const [startDateAsc, setStartDateAsc] = useState(
    query.get("startDateAsc") || ""
  );
  const [endDateAsc, setEndDateAsc] = useState(query.get("endDateAsc") || "");
  const [keyword, setKeyword] = useState(query.get("search_query") || "");
  const [keywordButton, setKeywordButton] = useState(false);
  const [navigateTableProjectsTrigger, setNavigateTableProjectsTrigger] =
    useState(false);

  const sortByName = () => {
    if (statusAsc !== "") setStatusAsc("");
    if (labAsc !== "") setLabAsc("");
    if (startDateAsc !== "") setStartDateAsc("");
    if (endDateAsc !== "") setEndDateAsc("");
    if (nameAsc === "") setNameAsc(true);
    else setNameAsc(!nameAsc);
    setNavigateTableProjectsTrigger((prev) => !prev);
  };

  const sortByStatus = () => {
    if (nameAsc !== "") setNameAsc("");
    if (labAsc !== "") setLabAsc("");
    if (startDateAsc !== "") setStartDateAsc("");
    if (endDateAsc !== "") setEndDateAsc("");
    if (statusAsc === "") setStatusAsc(true);
    else setStatusAsc(!statusAsc);
    setNavigateTableProjectsTrigger((prev) => !prev);
  };

  const sortByLab = () => {
    if (nameAsc !== "") setNameAsc("");
    if (statusAsc !== "") setStatusAsc("");
    if (startDateAsc !== "") setStartDateAsc("");
    if (endDateAsc !== "") setEndDateAsc("");
    if (labAsc === "") setLabAsc(true);
    else setLabAsc(!labAsc);
    setNavigateTableProjectsTrigger((prev) => !prev);
  };

  const sortByStartDate = () => {
    if (nameAsc !== "") setNameAsc("");
    if (statusAsc !== "") setStatusAsc("");
    if (labAsc !== "") setLabAsc("");
    if (endDateAsc !== "") setEndDateAsc("");
    if (startDateAsc === "") setStartDateAsc(true);
    else setStartDateAsc(!startDateAsc);
    setNavigateTableProjectsTrigger((prev) => !prev);
  };

  const sortByEndDate = () => {
    if (nameAsc !== "") setNameAsc("");
    if (statusAsc !== "") setStatusAsc("");
    if (labAsc !== "") setLabAsc("");
    if (startDateAsc !== "") setStartDateAsc("");
    if (endDateAsc === "") setEndDateAsc(true);
    else setEndDateAsc(!endDateAsc);
    setNavigateTableProjectsTrigger((prev) => !prev);
  };

  useEffect(() => {
    getTableProjects(
      currentPage,
      selectedLab,
      selectedStatus,
      slotsAvailable,
      nameAsc,
      statusAsc,
      labAsc,
      startDateAsc,
      endDateAsc,
      keyword
    )
      .then((response) => response.json())
      .then((data) => {
        setProjects(data.tableProjects);
        setTotalPages(data.maxPageNumber);
      });

    const queryParamsObj = {};

    if (currentPage) queryParamsObj.currentPage = currentPage;
    if (selectedLab !== "") queryParamsObj.selectedLab = selectedLab;
    if (selectedStatus !== "") queryParamsObj.selectedStatus = selectedStatus;
    if (slotsAvailable) queryParamsObj.slotsAvailable = slotsAvailable;
    if (nameAsc !== "") queryParamsObj.nameAsc = nameAsc;
    if (statusAsc !== "") queryParamsObj.statusAsc = statusAsc;
    if (labAsc !== "") queryParamsObj.labAsc = labAsc;
    if (startDateAsc !== "") queryParamsObj.startDateAsc = startDateAsc;
    if (endDateAsc !== "") queryParamsObj.endDateAsc = endDateAsc;
    if (keyword) queryParamsObj.search_query = keyword;

    const queryParams = new URLSearchParams(queryParamsObj).toString();

    navigate(`/projects?${queryParams}`);
  }, [navigateTableProjectsTrigger]);

  return (
    <>
      <AppNavbar />
      <AsideProjectsTable
        selectedLab={selectedLab}
        setSelectedLab={setSelectedLab}
        selectedStatus={selectedStatus}
        setSelectedStatus={setSelectedStatus}
        slotsAvailable={slotsAvailable}
        setSlotsAvailable={setSlotsAvailable}
        navigateTableProjectsTrigger={navigateTableProjectsTrigger}
        setNavigateTableProjectsTrigger={setNavigateTableProjectsTrigger}
        setCurrentPage={setCurrentPage}
      />
      <div className="ilm-pageb-with-aside">
        <h1 className="page-title">
          <span className="app-slogan-1">All </span>
          <span className="app-slogan-2">Projects</span>
        </h1>
        <div className="table-margin-top">
          <ProjectsTable
            projects={projects}
            currentPage={currentPage}
            setCurrentPage={setCurrentPage}
            totalPages={totalPages}
            sortByName={sortByName}
            sortByStatus={sortByStatus}
            sortByLab={sortByLab}
            sortByStartDate={sortByStartDate}
            sortByEndDate={sortByEndDate}
            keyword={keyword}
            setKeyword={setKeyword}
            keywordButton={keywordButton}
            setKeywordButton={setKeywordButton}
            navigateTableProjectsTrigger={navigateTableProjectsTrigger}
            setNavigateTableProjectsTrigger={setNavigateTableProjectsTrigger}
          ></ProjectsTable>
        </div>
      </div>
    </>
  );
};

export default ProjectsPage;
