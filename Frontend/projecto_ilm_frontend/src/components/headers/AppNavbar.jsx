import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { getUserProfileImage, logoutUser } from "../../utilities/services";
import Cookies from "js-cookie"; // Importando a biblioteca de cookies
import { useMediaQuery } from "react-responsive";
import "./AppNavbar.css";
import projectsIcon from "../../resources/icons/navbar/projects-icon.png";
import resourceIcon from "../../resources/icons/navbar/resource-icon.png";
import myProjectsIcon from "../../resources/icons/navbar/my-projects-icon.png";
import mailIcon from "../../resources/icons/navbar/mail-icon.png";
import bellIcon from "../../resources/icons/navbar/notification-icon.png";
import userProfileIcon from "../../resources/avatares/Avatar padrão.jpg";

export default function AppNavbar() {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [userImage, setUserImage] = useState(userProfileIcon);
  const navigate = useNavigate();
  const location = useLocation(); // Obter a localização atual
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });

  const toggleDropdown = () => {
    setDropdownOpen(!dropdownOpen);
  };

  const handleLogout = async () => {
    const response = await logoutUser();
    if (response.ok) {
      navigate("/"); // Redireciona para a página de login
    } else {
      console.error("Failed to logout");
    }
  };

  const handleMyProfile = () => {
    const systemUsername = Cookies.get("user-systemUsername");
    if (systemUsername) {
      navigate(`/profile/${systemUsername}`);
    } else {
      console.error("System username not found in cookies");
    }
  };

  useEffect(() => {
    async function fetchUserImage() {
      const imageUrl = await getUserProfileImage();
      if (imageUrl) {
        setUserImage(imageUrl);
      }
    }

    fetchUserImage();
  }, []);

  const getNavItemClass = (path) => {
    return location.pathname === path ? "nav-item active" : "nav-item";
  };

  const getNavIconStyle = (path) => {
    return {
      backgroundImage: `url(${
        path === "/projects"
          ? projectsIcon
          : path === "/resources"
          ? resourceIcon
          : path === "/myprojects"
          ? myProjectsIcon
          : path === "/mail"
          ? mailIcon
          : ""
      })`,
    };
  };

  const handleNavigation = (path) => {
    navigate(path);
  };

  return (
    <div>
      <div className="app-navbar">
        <div className="logo"></div>
        <div className="nav-icons-wrapper">
          <div className="nav-icons">
            <div className={getNavItemClass("/projects")} onClick={() => handleNavigation("/projects")}>
              <div className="icon" style={getNavIconStyle("/projects")}></div>
              <label>Projects</label>
            </div>
            <div className={getNavItemClass("/resources")} onClick={() => handleNavigation("/resources")}>
              <div className="icon" style={getNavIconStyle("/resources")}></div>
              <label>Resources</label>
            </div>
            <div className={getNavItemClass("/myprojects")} onClick={() => handleNavigation("/myprojects")}>
              <div className="icon" style={getNavIconStyle("/myprojects")}></div>
              <label>My Projects</label>
            </div>
            <div className={getNavItemClass("/mail")} onClick={() => handleNavigation("/mail")}>
              <div className="icon" style={getNavIconStyle("/mail")}></div>
              <label>Mail</label>
            </div>
          </div>
        </div>
        <div className="nav-right">
          <select className="language-dropdown">
            <option className="option-flag" value="en">
              🇺🇸 
            </option>
            <option className="option-flag" value="pt">
              🇵🇹 
            </option>
            <option className="option-flag" value="es">
              🇪🇸 
            </option>
          </select>
          <div
            className="icon bell"
            style={{ backgroundImage: `url(${bellIcon})` }}
          ></div>
          <div className="user-profile" onClick={toggleDropdown}>
            <div
              className="user-image"
              style={{ backgroundImage: `url(${userImage})` }}
            ></div>
            {dropdownOpen && (
              <div className={`dropdown-content ${dropdownOpen ? "show" : ""}`}>
                <div onClick={handleMyProfile}>👤 My Profile</div>
                <div onClick={handleLogout}>⛔️ Logout</div>
              </div>
            )}
          </div>
        </div>
      </div>
      {/* Bottom Navbar for mobile view */}
      {isMobile && (
        <div className="bottom-navbar">
          <div className="nav-icons">
            <div className={getNavItemClass("/projects")} onClick={() => handleNavigation("/projects")}>
              <div className="icon" style={getNavIconStyle("/projects")}></div>
              <label>Projects</label>
            </div>
            <div className={getNavItemClass("/resources")} onClick={() => handleNavigation("/resources")}>
              <div className="icon" style={getNavIconStyle("/resources")}></div>
              <label>Resources</label>
            </div>
            <div className={getNavItemClass("/myprojects")} onClick={() => handleNavigation("/myprojects")}>
              <div className="icon" style={getNavIconStyle("/myprojects")}></div>
              <label>My Projects</label>
            </div>
            <div className={getNavItemClass("/mail")} onClick={() => handleNavigation("/mail")}>
              <div className="icon" style={getNavIconStyle("/mail")}></div>
              <label>Mail</label>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
