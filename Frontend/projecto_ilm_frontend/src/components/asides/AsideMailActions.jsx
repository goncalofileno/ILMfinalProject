import React, { useState } from "react";
import { Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import ComposeMailModal from "../modals/ComposeMailModal";

const AsideMailActions = () => {
  const navigate = useNavigate();
  const [showComposeModal, setShowComposeModal] = useState(false);

  const handleCloseComposeModal = () => setShowComposeModal(false);
  const handleShowComposeModal = () => setShowComposeModal(true);

  return (
    <div className="aside-background">
      <div className="aside">
        <h3 style={{ color: "white" }}>Mail Actions</h3>
        <Button
          variant="primary"
          className="mb-2"
          onClick={handleShowComposeModal}
          style={{ backgroundColor: "#f39c12", borderColor: "#f39c12" }}
        >
          Compose New Mail
        </Button>
        <Button
          variant="primary"
          className="mb-2"
          onClick={() => navigate("/mail/inbox")}
          style={{ backgroundColor: "#f39c12", borderColor: "#f39c12" }}
        >
          Inbox
        </Button>
        <Button
          variant="primary"
          className="mb-2"
          onClick={() => navigate("/mail/sent")}
          style={{ backgroundColor: "#f39c12", borderColor: "#f39c12" }}
        >
          Sent Mails
        </Button>
      </div>
      <ComposeMailModal show={showComposeModal} handleClose={handleCloseComposeModal} />
    </div>
  );
};

export default AsideMailActions;
