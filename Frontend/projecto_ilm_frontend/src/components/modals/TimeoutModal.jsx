import React from "react";
import { Modal, Button } from "react-bootstrap";
import { Trans, t } from "@lingui/macro";

const TimeoutModal = ({ show, handleClose }) => {
  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title><Trans>Session Timeout</Trans></Modal.Title>
      </Modal.Header>
      <Modal.Body>
      <Trans>Your session has expired due to inactivity. Please log in again.</Trans>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="primary" onClick={handleClose}>
          OK
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default TimeoutModal;
