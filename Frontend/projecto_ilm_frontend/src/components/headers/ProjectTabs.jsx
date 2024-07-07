import React from "react";
import { Nav, NavItem } from "react-bootstrap";
import { NavLink, useNavigate, useParams, useLocation } from "react-router-dom";
import "./ProjectTabs.css";
import { Trans, t } from "@lingui/macro";

const ProjectTabs = ({ typeOfUserSeingProject, projectName }) => {
  const { systemProjectName } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  const tabs = [
    { name: (t`Info`), path: "info" },
    { name: (t`Plan`), path: "plan" },
    { name: (t`Logs`), path: "logs" },
    { name: (t`Resources`), path: "resources" },
    { name: (t`Chat`), path: "chat" },
  ];

  // Add the 'Members' tab only if the user is 'CREATOR' or 'MANAGER'
  if (["CREATOR", "MANAGER"].includes(typeOfUserSeingProject)) {
    tabs.push({ name: "Members", path: "members" });
  }

  const restrictedUserTypes = [
    "PENDING_BY_APPLIANCE",
    "PENDING_BY_INVITATION",
    "EXMEMBER",
    "GUEST",
  ];

  const getActiveTab = () => {
    const currentPath = location.pathname.split("/").pop();
    return currentPath;
  };

  return (
    <>
      {!restrictedUserTypes.includes(typeOfUserSeingProject) ? (
        <Nav className="project-tabs">
          {tabs.map((tab) =>
            !restrictedUserTypes.includes(typeOfUserSeingProject) ||
            tab.path === "info" ? (
              <NavItem key={tab.path}>
                <NavLink
                  to={`/project/${systemProjectName}/${tab.path}`}
                  className={({ isActive }) => (isActive ? "nav-link active" : "nav-link")}
                >
                  {tab.name}
                </NavLink>
              </NavItem>
            ) : null
          )}
          <div className="project-name-tab-container">
            <div className="project-name-tab">{projectName}</div>
          </div>
        </Nav>
      ) : (
        <div style={{ height: "10px" }}></div>
      )}
    </>
  );
};

export default ProjectTabs;
