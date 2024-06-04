import React, { useState } from "react";
import "./AppNavbar.css";
import projectsIcon from "../../resources/icons/navbar/projects-icon.png";
import resourceIcon from "../../resources/icons/navbar/resource-icon.png";
import myProjectsIcon from "../../resources/icons/navbar/my-projects-icon.png";
import mailIcon from "../../resources/icons/navbar/mail-icon.png";
import bellIcon from "../../resources/icons/navbar/notification-icon.png";
import userProfileIcon from "../../resources/avatares/Avatar padrão.jpg";
import testePhoto from "../../resources/avatares/teste.jpeg";
import { useMediaQuery } from "react-responsive";
import { FlagIcon } from "react-flag-kit";

export default function AppNavbar() {
   const [dropdownOpen, setDropdownOpen] = useState(false);
   const isPhone = useMediaQuery({ maxWidth: 768 });

   const toggleDropdown = () => {
      setDropdownOpen(!dropdownOpen);
   };

   return (
      <div>
         <div className="app-navbar">
            <div className="logo"></div>
            <div className="nav-icons-wrapper">
               <div className="nav-icons">
                  <div className="nav-item">
                     <div className="icon" style={{ backgroundImage: `url(${projectsIcon})` }}></div>
                     <label>Projects</label>
                  </div>
                  <div className="nav-item">
                     <div className="icon" style={{ backgroundImage: `url(${resourceIcon})` }}></div>
                     <label>Resources</label>
                  </div>
                  <div className="nav-item">
                     <div className="icon" style={{ backgroundImage: `url(${myProjectsIcon})` }}></div>
                     <label>My Projects</label>
                  </div>
                  <div className="nav-item">
                     <div className="icon" style={{ backgroundImage: `url(${mailIcon})` }}></div>
                     <label>Mail</label>
                  </div>
               </div>
            </div>
            <div className="nav-right">
               <select className="language-dropdown">
                  <option className="option-flag" value="en">
                     <FlagIcon code="US" size={20} /> 🇺🇸
                  </option>
                  <option className="option-flag" value="pt">
                     <FlagIcon code="PT" size={20} /> 🇵🇹
                  </option>
                  <option className="option-flag" value="es">
                     <FlagIcon code="ES" size={20} /> 🇪🇸
                  </option>
               </select>
               <div className="icon bell" style={{ backgroundImage: `url(${bellIcon})` }}></div>
               <div className="user-profile" onClick={toggleDropdown}>
                  <div className="user-image" style={{ backgroundImage: `url(${testePhoto})` }}></div>
                  {dropdownOpen && (
                     <div className={`dropdown-content ${dropdownOpen ? "show" : ""}`}>
                        <a href="#edit-profile">Edit Profile</a>
                        <a href="#logout">Logout</a>
                     </div>
                  )}
               </div>
            </div>
         </div>
         {/* Bottom Navbar for mobile view */}
         {isPhone && (
            <div className="bottom-navbar">
               <div className="nav-icons">
                  <div className="nav-item">
                     <div className="icon" style={{ backgroundImage: `url(${projectsIcon})` }}></div>
                     <label>Projects</label>
                  </div>
                  <div className="nav-item">
                     <div className="icon" style={{ backgroundImage: `url(${resourceIcon})` }}></div>
                     <label>Resources</label>
                  </div>
                  <div className="nav-item">
                     <div className="icon" style={{ backgroundImage: `url(${myProjectsIcon})` }}></div>
                     <label>My Projects</label>
                  </div>
                  <div className="nav-item">
                     <div className="icon" style={{ backgroundImage: `url(${mailIcon})` }}></div>
                     <label>Mail</label>
                  </div>
               </div>
            </div>
         )}
      </div>
   );
}
