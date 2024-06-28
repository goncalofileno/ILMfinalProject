import React, { useEffect } from "react";
import { Alert } from "react-bootstrap";
import "./NotificationBanner.css";

const NotificationBanner = ({ notification, onClick, onEnd }) => {
  useEffect(() => {
    const timer = setTimeout(onEnd, 3000); // Chama onEnd após 3 segundos

    return () => clearTimeout(timer);
  }, [notification, onEnd]);

  const getNotificationMessage = () => {
    const { type, projectName, userName, projectStatus, newUserType } = notification;
    switch (type) {
      case "APPLIANCE_REJECTED":
        return `Your application to <strong>${projectName}</strong> was rejected by <strong>${userName}</strong>.`;
      case "APPLIANCE_ACCEPTED":
        return `Your application to <strong>${projectName}</strong> was accepted by <strong>${userName}</strong>.`;
      case "APPLIANCE":
        return `<strong>${userName}</strong> applied to your project <strong>${projectName}</strong>.`;
      case "TASK":
        return `<strong>${userName}</strong> made changes to a task in the project <strong>${projectName}</strong> you are involved in.`;
      case "INVITE_REJECTED":
        return `<strong>${userName}</strong> rejected your invitation to join the project <strong>${projectName}</strong>.`;
      case "INVITE_ACCEPTED":
        return `<strong>${userName}</strong> accepted your invitation to join the project <strong>${projectName}</strong>.`;
      case "INVITE":
        return `<strong>${userName}</strong> invited you to join the project <strong>${projectName}</strong>.`;
      case "PROJECT":
        return `The project <strong>${projectName}</strong> changed its status to <strong>${projectStatus}</strong>.`;
      case "PROJECT_REJECTED":
        return `The project <strong>${projectName}</strong> was rejected by <strong>${userName}</strong>.`;
      case "REMOVED":
        return `You were removed from the project <strong>${projectName}</strong> by <strong>${userName}</strong>. Contact them for more information.`;
      case "PROJECT_MESSAGE":
        return `You have a new message in the project <strong>${projectName}</strong> chat from <strong>${userName}</strong>.`;
      case "USER_TYPE_CHANGED":
        return `Your user type was changed to <strong>${newUserType}</strong> by <strong>${userName}</strong> in the project <strong>${projectName}</strong>.`;
      default:
        return "You have a new notification.";
    }
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
      <span
        dangerouslySetInnerHTML={{ __html: getNotificationMessage() }}
      ></span>
    </Alert>
  );
};

export default NotificationBanner;
