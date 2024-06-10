import React from "react";
import { Modal, Button } from "react-bootstrap";

const TimeoutModal = ({ show, handleClose }) => {
  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Session Timeout</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        Your session has expired due to inactivity. Please log in again.
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
