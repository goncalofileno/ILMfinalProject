import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import ProjectsTable from "../components/tables/ProjectsTable";
import AsideProjectsTable from "../components/asides/AsideProjectsTable";
import { getTableProjects } from "../utilities/services";
import { useEffect, useState } from "react";

const ProjectsPage = () => {
  const [projects, setProjects] = useState([]);
  const [page, setPage] = useState(1);
  useEffect(() => {
    getTableProjects(page)
      .then((response) => response.json())
      .then((data) => {
        setProjects(data);
      });
  }, []);

  return (
    <>
      <AppNavbar />
      <AsideProjectsTable />
      <div className="ilm-pageb-with-aside">
        <h1 className="page-title">
          <span className="app-slogan-1">All </span>
          <span className="app-slogan-2">Projects</span>
        </h1>
        <div className="table-margin-top">
          <ProjectsTable projects={projects}></ProjectsTable>
        </div>
      </div>
    </>
  );
};

export default ProjectsPage;
