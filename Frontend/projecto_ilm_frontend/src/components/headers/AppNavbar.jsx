import React, { useState, useEffect, useCallback, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  getUserProfileImage,
  logoutUser,
  getUnreadNumber,
} from "../../utilities/services";
import Cookies from "js-cookie"; // Importando a biblioteca de cookies
import { useMediaQuery } from "react-responsive";
import useMailStore from "../../stores/useMailStore"; // Importando a store zustand
import useNotificationStore from "../../stores/useNotificationStore"; // Importando a nova store
import NotificationModal from "../modals/NotificationModal"; // Importando o novo componente
import "./AppNavbar.css";
import projectsIcon from "../../resources/icons/navbar/projects-icon.png";
import resourceIcon from "../../resources/icons/navbar/resource-icon.png";
import myProjectsIcon from "../../resources/icons/navbar/my-projects-icon.png";
import mailIcon from "../../resources/icons/navbar/mail-icon.png";
import bellIcon from "../../resources/icons/navbar/notification-icon.png";
import userProfileIcon from "../../resources/avatares/Avatar padrão.jpg";

export default function AppNavbar() {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [userImage, setUserImage] = useState(userProfileIcon);
  const dropdownRef = useRef(null);
  const modalRef = useRef(null);
  const bellIconRef = useRef(null);
  const navigate = useNavigate();
  const location = useLocation(); // Obter a localização atual
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const { unreadCount, setUnreadCount } = useMailStore();
  const {
    unreadNotificationsCount,
    fetchUnreadNotificationsCount,
    resetUnreadNotificationsCount,
    fetchNotifications,
    clearNotifications,
  } = useNotificationStore();

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

    async function fetchUnreadCount() {
      const sessionId = Cookies.get("session-id");
      if (sessionId) {
        const result = await getUnreadNumber(sessionId);
        setUnreadCount(result.unreadNumber);
      }
    }

    fetchUserImage();
    fetchUnreadCount();
    fetchUnreadNotificationsCount();
  }, [setUnreadCount, fetchUnreadNotificationsCount]);

  const getNavItemClass = (path) => {
    if (path === "/mail/inbox" || path === "/mail/sent") {
      return location.pathname.startsWith("/mail")
        ? "nav-item active"
        : "nav-item";
    }
    return location.pathname.startsWith(path) ? "nav-item active" : "nav-item";
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
          : path === "/mail/inbox" || path === "/mail/sent"
          ? mailIcon
          : ""
      })`,
    };
  };

  const handleNavigation = (path) => {
    navigate(path);
  };

  const handleBellClick = async () => {
    if (modalOpen) {
      setModalOpen(false);
      clearNotifications();
    } else {
      setModalOpen(true);
      await fetchNotifications(1);
      resetUnreadNotificationsCount();
    }
  };

  const handleClickOutside = useCallback((event) => {
    if (
      (dropdownRef.current && dropdownRef.current.contains(event.target)) ||
      (modalRef.current && modalRef.current.contains(event.target)) ||
      (bellIconRef.current && bellIconRef.current.contains(event.target))
    ) {
      return;
    }
    setDropdownOpen(false);
    setModalOpen(false);
    clearNotifications();
  }, [clearNotifications]);

  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [handleClickOutside]);

  return (
    <div>
      <div className="app-navbar">
        <div className="logo" onClick={() => handleNavigation("/projects")}></div>
        <div className="nav-icons-wrapper">
          <div className="nav-icons">
            <div
              className={getNavItemClass("/projects")}
              onClick={() => handleNavigation("/projects")}
            >
              <div className="icon" style={getNavIconStyle("/projects")}></div>
              <label>Projects</label>
            </div>
            <div
              className={getNavItemClass("/resources")}
              onClick={() => handleNavigation("/resources")}
            >
              <div className="icon" style={getNavIconStyle("/resources")}></div>
              <label>Resources</label>
            </div>
            <div
              className={getNavItemClass("/myprojects")}
              onClick={() => handleNavigation("/myprojects")}
            >
              <div className="icon" style={getNavIconStyle("/myprojects")}></div>
              <label>My Projects</label>
            </div>
            <div
              className={getNavItemClass("/mail/inbox")}
              onClick={() => handleNavigation("/mail/inbox")}
            >
              <div className="icon" style={getNavIconStyle("/mail/inbox")}></div>
              <label>Mail</label>
              {unreadCount > 0 && <span className="badge">{unreadCount}</span>}
            </div>
          </div>
        </div>
        <div className="nav-right">
          <select className="language-dropdown">
            <option className="option-flag" value="en">🇺🇸</option>
            <option className="option-flag" value="pt">🇵🇹</option>
            <option className="option-flag" value="es">🇪🇸</option>
          </select>
          <div
            className="icon"
            style={{ backgroundImage: `url(${bellIcon})` }}
            onClick={handleBellClick}
            ref={bellIconRef}
          >
            {unreadNotificationsCount > 0 && (
              <span id="notification-badge" className="badge">
                {unreadNotificationsCount}
              </span>
            )}
          </div>
          <div className="user-profile" onClick={toggleDropdown} ref={dropdownRef}>
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
            <div
              className={getNavItemClass("/projects")}
              onClick={() => handleNavigation("/projects")}
            >
              <div className="icon" style={getNavIconStyle("/projects")}></div>
              <label>Projects</label>
            </div>
            <div
              className={getNavItemClass("/resources")}
              onClick={() => handleNavigation("/resources")}
            >
              <div className="icon" style={getNavIconStyle("/resources")}></div>
              <label>Resources</label>
            </div>
            <div
              className={getNavItemClass("/myprojects")}
              onClick={() => handleNavigation("/myprojects")}
            >
              <div className="icon" style={getNavIconStyle("/myprojects")}></div>
              <label>My Projects</label>
            </div>
            <div
              className={getNavItemClass("/mail/inbox")}
              onClick={() => handleNavigation("/mail/inbox")}
            >
              <div className="icon mail-icon" style={getNavIconStyle("/mail/inbox")}></div>
              <label>Mail</label>
              {unreadCount > 0 && (
                <span className="badge">
                  {unreadCount}
                </span>
              )}
            </div>
          </div>
        </div>
      )}
      {modalOpen && <NotificationModal onClose={() => setModalOpen(false)} modalRef={modalRef} />}
    </div>
  );
}
