import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import ProjectsTable from "../components/tables/ProjectsTable";
import AsideProjectsTable from "../components/asides/AsideProjectsTable";
import { getTableProjects } from "../utilities/services";
import { useEffect, useState } from "react";

const ProjectsPage = () => {
  const [totalPages, setTotalPages] = useState(20);
  const [currentPage, setCurrentPage] = useState(1);
  const [projects, setProjects] = useState([]);
  const [selectedLab, setSelectedLab] = useState("");
  const [selectedStatus, setSelectedStatus] = useState("");
  const [slotsAvailable, setSlotsAvailable] = useState(false);
  const [nameAsc, setNameAsc] = useState("");
  const [statusAsc, setStatusAsc] = useState("");
  const [labAsc, setLabAsc] = useState("");
  const [startDateAsc, setStartDateAsc] = useState("");
  const [endDateAsc, setEndDateAsc] = useState("");
  const [keyword, setKeyword] = useState("");
  const [keywordButton, setKeywordButton] = useState(false);

  const sortByName = () => {
    if (statusAsc !== "") setStatusAsc("");
    if (labAsc !== "") setLabAsc("");
    if (startDateAsc !== "") setStartDateAsc("");
    if (endDateAsc !== "") setEndDateAsc("");
    if (nameAsc === "") setNameAsc(true);
    else setNameAsc(!nameAsc);
  };

  const sortByStatus = () => {
    if (nameAsc !== "") setNameAsc("");
    if (labAsc !== "") setLabAsc("");
    if (startDateAsc !== "") setStartDateAsc("");
    if (endDateAsc !== "") setEndDateAsc("");
    if (statusAsc === "") setStatusAsc(true);
    else setStatusAsc(!statusAsc);
  };

  const sortByLab = () => {
    if (nameAsc !== "") setNameAsc("");
    if (statusAsc !== "") setStatusAsc("");
    if (startDateAsc !== "") setStartDateAsc("");
    if (endDateAsc !== "") setEndDateAsc("");
    if (labAsc === "") setLabAsc(true);
    else setLabAsc(!labAsc);
  };

  const sortByStartDate = () => {
    if (nameAsc !== "") setNameAsc("");
    if (statusAsc !== "") setStatusAsc("");
    if (labAsc !== "") setLabAsc("");
    if (endDateAsc !== "") setEndDateAsc("");
    if (startDateAsc === "") setStartDateAsc(true);
    else setStartDateAsc(!startDateAsc);
  };

  const sortByEndDate = () => {
    if (nameAsc !== "") setNameAsc("");
    if (statusAsc !== "") setStatusAsc("");
    if (labAsc !== "") setLabAsc("");
    if (startDateAsc !== "") setStartDateAsc("");
    if (endDateAsc === "") setEndDateAsc(true);
    else setEndDateAsc(!endDateAsc);
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
  }, [
    currentPage,
    selectedLab,
    selectedStatus,
    slotsAvailable,
    nameAsc,
    statusAsc,
    labAsc,
    startDateAsc,
    endDateAsc,
    keywordButton,
  ]);

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
          ></ProjectsTable>
        </div>
      </div>
    </>
  );
};

export default ProjectsPage;
