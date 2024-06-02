import React from "react";
import AppNavbar from "../components/headers/AppNavbar";
import ProjectsTable from "../components/tables/ProjectsTable";
import AsideProjectsTable from "../components/asides/AsideProjectsTable";

const ProjectsPage = () => {
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
               <ProjectsTable></ProjectsTable>
            </div>
         </div>
      </>
   );
};

export default ProjectsPage;
