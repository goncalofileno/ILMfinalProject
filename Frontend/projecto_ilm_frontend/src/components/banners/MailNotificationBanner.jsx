import React, { useEffect } from "react";
import { Alert } from "react-bootstrap";
import "./MailNotificationBanner.css";

const MailNotificationBanner = ({ message, onClick, onEnd }) => {
  useEffect(() => {
    const timer = setTimeout(onEnd, 3000);

    return () => clearTimeout(timer);
  }, [message, onEnd]);

  const getFormattedMessage = () => {
    const parts = message.split(" ");
    const senderName = parts.slice(0, -5).join(" ");
    const restOfMessage = parts.slice(-5).join(" ");
    return (
      <>
        <span className="sender-name">{senderName}</span> {restOfMessage}
      </>
    );
  };

  return (
    <Alert
      variant="info"
      className="alert-slide-in"
      style={{
        position: "fixed",
        top: "10%",
        left: "50%",
        transform: "translateX(-50%)",
        width: "50%",
        zIndex: 10000000,
        cursor: "pointer",
        textAlign: "center",
      }}
      onClick={onClick}
    >
      {getFormattedMessage()}
    </Alert>
  );
};

export default MailNotificationBanner;
