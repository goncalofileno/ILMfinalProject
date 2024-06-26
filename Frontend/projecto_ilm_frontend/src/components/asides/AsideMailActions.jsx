import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import ComposeMailModal from "../modals/ComposeMailModal";
import ComposeIcon from "../../resources/icons/asides/mailAside/compose-icon.png";
import InboxIcon from "../../resources/icons/asides/mailAside/inbox-icon.png";
import SentIcon from "../../resources/icons/asides/mailAside/sent-icon.png";
import "./AsideMailActions.css";

const AsideMailActions = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [showComposeModal, setShowComposeModal] = useState(false);

  const handleCloseComposeModal = () => setShowComposeModal(false);
  const handleShowComposeModal = () => {
    setShowComposeModal(true);
    navigate(""); // Clear the path to ensure other items are deselected
  };

  const getNavItemClass = (path) => {
    if (path === "compose" && showComposeModal) {
      return "aside-nav-item active";
    }
    return location.pathname.startsWith(path) && !showComposeModal
      ? "aside-nav-item active"
      : "aside-nav-item";
  };

  const getNavIconStyle = (path) => {
    return {
      backgroundImage: `url(${
        path === "/mail/inbox"
          ? InboxIcon
          : path === "/mail/sent"
          ? SentIcon
          : path === "compose"
          ? ComposeIcon
          : ""
      })`,
    };
  };

  return (
    <div className="aside-background">
      <div className="aside">
        <div
          className={`${getNavItemClass("compose")}`}
          onClick={handleShowComposeModal}
        >
          <div className="icon" style={getNavIconStyle("compose")}></div>
          <label>Compose New Mail</label>
        </div>
        <div
          className={getNavItemClass("/mail/inbox")}
          onClick={() => {
            setShowComposeModal(false);
            navigate("/mail/inbox");
          }}
        >
          <div className="icon" style={getNavIconStyle("/mail/inbox")}></div>
          <label>Inbox</label>
        </div>
        <div
          className={getNavItemClass("/mail/sent")}
          onClick={() => {
            setShowComposeModal(false);
            navigate("/mail/sent");
          }}
        >
          <div className="icon" style={getNavIconStyle("/mail/sent")}></div>
          <label>Sent Mails</label>
        </div>
      </div>
      <ComposeMailModal show={showComposeModal} handleClose={handleCloseComposeModal} />
    </div>
  );
};

export default AsideMailActions;
