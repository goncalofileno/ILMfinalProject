import Alert from "react-bootstrap/Alert";
import "./StandardModal.css";
import { useEffect } from "react";

export default function StandardModal({
  modalType,
  message,
  setModalActive,
  modalActive,
}) {
  console.log(modalType);
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
