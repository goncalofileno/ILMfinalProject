import React from "react";
import { useLanguage } from "../../I18nLoader"; // Importe o hook useLanguage
import "./Headers.css";

export default function LoginHeader() {
  const { language, changeLanguage } = useLanguage();

  const handleLanguageChange = (event) => {
    const selectedLanguage = event.target.value;
    changeLanguage(selectedLanguage);
  };

  return (
    <div className="ilm-headers">
      <div className="app-navbar">
        <div className="logo"> </div>
        <div className="nav-right">
          <select
            className="language-dropdown"
            onChange={handleLanguageChange}
            value={language} // Use the state value
          >
            <option className="option-flag" value="ENGLISH">
              🇬🇧
            </option>
            <option className="option-flag" value="PORTUGUESE">
              🇵🇹
            </option>
          </select>
        </div>
      </div>
    </div>
  );
}
