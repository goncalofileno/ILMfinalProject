import React, { useState, useEffect, useCallback, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  getUserProfileImage,
  logoutUser,
  getUnreadNumber,
  updateLanguage,
} from "../../utilities/services";
import Cookies from "js-cookie";
import { useMediaQuery } from "react-responsive";
import useMailStore from "../../stores/useMailStore";
import useNotificationStore from "../../stores/useNotificationStore";
import NotificationModal from "../modals/NotificationModal";
import "./AppNavbar.css";
import projectsIcon from "../../resources/icons/navbar/projects-icon.png";
import resourceIcon from "../../resources/icons/navbar/resource-icon.png";
import myProjectsIcon from "../../resources/icons/navbar/my-projects-icon.png";
import usersIcon from "../../resources/icons/navbar/users-icon.png";
import mailIcon from "../../resources/icons/navbar/mail-icon.png";
import bellIcon from "../../resources/icons/navbar/notification-icon.png";
import userProfileIcon from "../../resources/avatares/Avatar padrão.jpg";
import portugalFlag from "../../resources/icons/navbar/portugal.png";
import ukFlag from "../../resources/icons/navbar/united-kingdom.png";
import { Trans, t } from "@lingui/macro";
import { useLanguage } from "../../I18nLoader";

export default function AppNavbar({
  setIsAsideVisible,
  pageWithAside,
  setCurrentLanguage,
}) {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [userImage, setUserImage] = useState(userProfileIcon);
  const [selectedLanguage, setSelectedLanguage] = useState("ENGLISH");
  const dropdownRef = useRef(null);
  const modalRef = useRef(null);
  const bellIconRef = useRef(null);
  const navigate = useNavigate();
  const location = useLocation();
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const { unreadCount, setUnreadCount } = useMailStore();
  const {
    unreadNotificationsCount,
    fetchUnreadNotificationsCount,
    resetUnreadNotificationsCount,
    fetchNotifications,
    clearNotifications,
  } = useNotificationStore();
  const { language, changeLanguage } = useLanguage();

  const toggleDropdown = () => {
    if (modalOpen) {
      setModalOpen(false);
    }
    setDropdownOpen((prev) => !prev);
  };

  const handleLogout = async () => {
    const response = await logoutUser();
    if (response.ok) {
      navigate("/");
    } else {
      navigate("/");
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

  const handleLanguageChange = async (event) => {
    const newLanguage = event.target.value;
    setSelectedLanguage(newLanguage);
    const sessionId = Cookies.get("session-id");
    if (sessionId) {
      const response = await updateLanguage(sessionId, newLanguage);
      if (response.success) {
        Cookies.set("user-language", newLanguage);
        setCurrentLanguage(newLanguage);
      } else {
        console.error("Failed to update language:", response.error);
      }
    }
    changeLanguage(newLanguage);
    if (setCurrentLanguage) setCurrentLanguage(newLanguage);
  };

  useEffect(() => {
    const userLanguage = Cookies.get("user-language") || "en";
    setSelectedLanguage(userLanguage);

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
          : path === "/users"
          ? usersIcon
          : ""
      })`,
    };
  };

  const handleNavigation = (path) => {
    navigate(path);
  };

  const handleBellClick = async () => {
    if (dropdownOpen) {
      setDropdownOpen(false);
    }
    if (modalOpen) {
      setModalOpen(false);
      clearNotifications();
    } else {
      setModalOpen(true);
      await fetchNotifications(1);
      resetUnreadNotificationsCount();
    }
  };

  const handleClickOutside = useCallback(
    (event) => {
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
    },
    [clearNotifications]
  );

  useEffect(() => {
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [handleClickOutside]);

  return (
    <div>
      <div className="app-navbar">
        {!isMobile || !pageWithAside ? (
          <div
            className="logo"
            onClick={() => handleNavigation("/projects")}
          ></div>
        ) : (
          pageWithAside && (
            <div className="flex-logo-hamb">
              <div
                className="logo"
                onClick={() => handleNavigation("/projects")}
              ></div>

              <div>
                <button
                  className="hamb-btn"
                  onClick={() => setIsAsideVisible((prev) => !prev)}
                >
                  <i class="fas fa-bars fa-2x"></i>
                </button>
              </div>
            </div>
          )
        )}
        <div className="nav-icons-wrapper">
          <div className="nav-icons">
            <div
              className={getNavItemClass("/projects")}
              onClick={() => handleNavigation("/projects")}
            >
              <div className="icon" style={getNavIconStyle("/projects")}></div>
              <label>
                <Trans>Projects</Trans>
              </label>
            </div>
            <div
              className={getNavItemClass("/resources")}
              onClick={() => handleNavigation("/resources")}
            >
              <div className="icon" style={getNavIconStyle("/resources")}></div>
              <label>
                <Trans>Resources</Trans>
              </label>
            </div>
            <div
              className={getNavItemClass("/myprojects")}
              onClick={() => handleNavigation("/myprojects")}
            >
              <div
                className="icon"
                style={getNavIconStyle("/myprojects")}
              ></div>
              <label>
                <Trans>My Projects</Trans>
              </label>
            </div>
            <div
              className={getNavItemClass("/mail/inbox")}
              onClick={() => handleNavigation("/mail/inbox")}
            >
              <div
                className="icon"
                style={getNavIconStyle("/mail/inbox")}
              ></div>
              <label>Email</label>
              {unreadCount > 0 && <span className="badge">{unreadCount}</span>}
            </div>
            <div
              className={getNavItemClass("/users")}
              onClick={() => handleNavigation("/users")}
            >
              <div className="icon" style={getNavIconStyle("/users")}></div>
              <label>
                <Trans>Users</Trans>
              </label>
            </div>
          </div>
        </div>
        <div className="nav-right">
          <select
            className="language-dropdown"
            value={selectedLanguage}
            onChange={handleLanguageChange}
          >
            <option className="option-flag" value="ENGLISH">
              🇬🇧
            </option>
            <option className="option-flag" value="PORTUGUESE">
              🇵🇹
            </option>
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
          <div
            className="user-profile"
            onClick={toggleDropdown}
            ref={dropdownRef}
          >
            <div
              className="user-image"
              style={{ backgroundImage: `url(${userImage})` }}
            ></div>
            {dropdownOpen && (
              <div className={`dropdown-content ${dropdownOpen ? "show" : ""}`}>
                <div onClick={handleMyProfile}>
                  👤 <Trans>My Profile</Trans>
                </div>
                <div onClick={handleLogout}>
                  ⛔️ <Trans>Logout</Trans>
                </div>
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
              <label>
                <Trans>Projects</Trans>
              </label>
            </div>
            <div
              className={getNavItemClass("/resources")}
              onClick={() => handleNavigation("/resources")}
            >
              <div className="icon" style={getNavIconStyle("/resources")}></div>
              <label>
                <Trans>Resources</Trans>
              </label>
            </div>
            <div
              className={getNavItemClass("/myprojects")}
              onClick={() => handleNavigation("/myprojects")}
              id="my-projects-nav-item"
            >
              <div
                className="icon"
                style={getNavIconStyle("/myprojects")}
              ></div>
              <label style={{ width: "80px", textAlign: "center" }}>
                <Trans>My Projects</Trans>
              </label>
            </div>
            <div
              className={getNavItemClass("/mail/inbox")}
              onClick={() => handleNavigation("/mail/inbox")}
            >
              <div
                className="icon mail-icon"
                style={getNavIconStyle("/mail/inbox")}
              ></div>
              <label>Email</label>
              {unreadCount > 0 && <span className="badge">{unreadCount}</span>}
            </div>
            <div
              className={getNavItemClass("/users")}
              onClick={() => handleNavigation("/users")}
              style={{ width: "60px" }}
            >
              <div className="icon" style={getNavIconStyle("/users")}></div>
              <label>
                <Trans>Users</Trans>
              </label>
            </div>
          </div>
        </div>
      )}
      {modalOpen && (
        <NotificationModal
          onClose={() => setModalOpen(false)}
          modalRef={modalRef}
        />
      )}
    </div>
  );
}
