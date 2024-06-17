import React from 'react';
import { Nav, NavItem, NavLink } from 'react-bootstrap';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import './ProjectTabs.css';

const ProjectTabs = ({ typeOfUserSeingProject }) => {
  const { systemProjectName } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  const tabs = [
    { name: 'Info', path: 'info' },
    { name: 'Plan', path: 'plan' },
    { name: 'Logs', path: 'logs' },
    { name: 'Resources', path: 'resources' },
    { name: 'Chat', path: 'chat' },
    { name: 'Members', path: 'members' }
  ];

  const restrictedUserTypes = ['PENDING_BY_APPLIANCE', 'PENDING_BY_INVITATION', 'EXMEMBER', 'GUEST'];

  const handleTabClick = (path) => {
    navigate(`/project/${systemProjectName}/${path}`);
  };

  const getActiveTab = () => {
    const currentPath = location.pathname.split('/').pop();
    return currentPath;
  };

  return (
    <Nav className="project-tabs">
      {tabs.map((tab) => (
        !restrictedUserTypes.includes(typeOfUserSeingProject) || tab.path === 'info' ? (
          <NavItem key={tab.path}>
            <NavLink
              href="#"
              className={getActiveTab() === tab.path ? 'active' : ''}
              onClick={() => handleTabClick(tab.path)}
            >
              {tab.name}
            </NavLink>
          </NavItem>
        ) : null
      ))}
    </Nav>
  );
};

export default ProjectTabs;
