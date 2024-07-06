import React from "react";
import { Modal, Button } from "react-bootstrap";
import { Trans, t } from "@lingui/macro";

const DeleteTaskModal = ({ show, handleClose, handleConfirmDelete }) => {
  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title><Trans>Confirm Delete</Trans></Modal.Title>
      </Modal.Header>
      <Modal.Body><Trans>Are you sure you want to delete this task?</Trans></Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
        <Trans>Cancel</Trans>
        </Button>
        <Button variant="danger" onClick={handleConfirmDelete}>
        <Trans>Delete</Trans>
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default DeleteTaskModal;
