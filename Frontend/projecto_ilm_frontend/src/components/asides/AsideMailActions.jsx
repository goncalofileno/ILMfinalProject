import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import ComposeMailModal from "../modals/ComposeMailModal";
import ComposeIcon from "../../resources/icons/asides/mailAside/compose-icon.png";
import InboxIcon from "../../resources/icons/asides/mailAside/inbox-icon.png";
import SentIcon from "../../resources/icons/asides/mailAside/sent-icon.png";
import "./AsideMailActions.css";
import { Trans } from "@lingui/macro";
import { useMediaQuery } from "react-responsive";

const AsideMailActions = ({ isVisible }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [showComposeModal, setShowComposeModal] = useState(false);
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const handleCloseComposeModal = () => setShowComposeModal(false);
  const handleShowComposeModal = () => {
    setShowComposeModal(true);
    navigate(""); 
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
    <>
      {((isVisible && isMobile) || !isMobile) && (
        <div className={!isMobile && "aside-background"}>
          <div className={!isMobile ? "aside" : "aside-Mobile"}>
            <div
              className={`${getNavItemClass("compose")}`}
              onClick={handleShowComposeModal}
            >
              <div className="icon" style={getNavIconStyle("compose")}></div>
              <label>
                <Trans>Compose New Mail</Trans>
              </label>
            </div>
            <div
              className={getNavItemClass("/mail/inbox")}
              onClick={() => {
                setShowComposeModal(false);
                navigate("/mail/inbox");
              }}
            >
              <div
                className="icon"
                style={getNavIconStyle("/mail/inbox")}
              ></div>
              <label>
                <Trans>Inbox</Trans>
              </label>
            </div>
            <div
              className={getNavItemClass("/mail/sent")}
              onClick={() => {
                setShowComposeModal(false);
                navigate("/mail/sent");
              }}
            >
              <div className="icon" style={getNavIconStyle("/mail/sent")}></div>
              <label>
                <Trans>Sent Mails</Trans>
              </label>
            </div>
          </div>
          <ComposeMailModal
            show={showComposeModal}
            handleClose={handleCloseComposeModal}
          />
        </div>
      )}
    </>
  );
};

export default AsideMailActions;
