import Alert from "react-bootstrap/Alert";
import "./StandardModal.css";
import { useEffect } from "react";
import React from "react";

export default function StandardModal({
  modalType,
  message,
  setModalActive,
  modalActive,
}) {
  useEffect(() => {
    if (modalActive) {
      setTimeout(() => {
        setModalActive(false);
      }, 3000);
    }
  }, [modalActive]);

  return (
    <>
      {modalActive && (
        <Alert variant={modalType} className="standard-modal">
          {message}
        </Alert>
      )}
    </>
  );
}
