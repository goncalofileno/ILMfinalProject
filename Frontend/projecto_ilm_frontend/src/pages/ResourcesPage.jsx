import AppNavbar from "../components/headers/AppNavbar";
import AsideResourcesPage from "../components/asides/AsideResourcesPage";
import ResourcesTable from "../components/tables/ResourcesTable";
import { useEffect, useState } from "react";
import { getAllResources } from "../utilities/services";

export default function ResourcesPage() {
  const [resources, setResources] = useState([]);
  const [totalPages, setTotalPages] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  useEffect(() => {
    getAllResources()
      .then((response) => response.json())
      .then((data) => {
        setResources(data.tableResources);
        setTotalPages(data.maxPageNumber);
      });
  }, []);
  return (
    <>
      <AppNavbar />
      <AsideResourcesPage />
      <div className="ilm-pageb-with-aside">
        <h1 className="page-title">
          <span className="app-slogan-1">All </span>
          <span className="app-slogan-2">Resources</span>
        </h1>
        <div className="table-margin-top">
          <ResourcesTable
            resources={resources}
            totalPages={totalPages}
            currentPage={currentPage}
          />
        </div>
      </div>
    </>
  );
}
