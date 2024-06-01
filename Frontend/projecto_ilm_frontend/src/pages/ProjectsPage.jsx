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
            <h1>Projects Page</h1>
            <ProjectsTable></ProjectsTable>
         </div>
      </>
   );
};

export default ProjectsPage;
