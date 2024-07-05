import React, { useEffect } from "react";
import { Alert } from "react-bootstrap";
import "./NotificationBanner.css";

const NotificationBanner = ({ notification, onClick, onEnd }) => {
  useEffect(() => {
    const timer = setTimeout(onEnd, 3000); // Chama onEnd após 3 segundos

    return () => clearTimeout(timer);
  }, [notification, onEnd]);

  const notificationMessages = {
    APPLIANCE_REJECTED: ({ projectName, userName }) =>
      `Your application to <strong>${projectName}</strong> was rejected by <strong>${userName}</strong>.`,
    APPLIANCE_ACCEPTED: ({ projectName, userName }) =>
      `Your application to <strong>${projectName}</strong> was accepted by <strong>${userName}</strong>.`,
    APPLIANCE: ({ projectName, userName }) =>
      `<strong>${userName}</strong> applied to your project <strong>${projectName}</strong>.`,
    TASK: ({ projectName, userName }) =>
      `<strong>${userName}</strong> made changes to a task in the project <strong>${projectName}</strong> you are involved in.`,
    TASK_ASSIGNED: ({ projectName, userName }) =>
      `<strong>${userName}</strong> assigned you to a task in the project <strong>${projectName}</strong>.`,
    INVITE_REJECTED: ({ projectName, userName }) =>
      `<strong>${userName}</strong> rejected your invitation to join the project <strong>${projectName}</strong>.`,
    INVITE_ACCEPTED: ({ projectName, userName }) =>
      `<strong>${userName}</strong> accepted your invitation to join the project <strong>${projectName}</strong>.`,
    INVITE: ({ projectName, userName }) =>
      `<strong>${userName}</strong> invited you to join the project <strong>${projectName}</strong>.`,
    PROJECT: ({ projectName, projectStatus }) =>
      `The project <strong>${projectName}</strong> changed its status to <strong>${projectStatus}</strong>.`,
    PROJECT_REJECTED: ({ projectName, userName }) =>
      `The project <strong>${projectName}</strong> was rejected by <strong>${userName}</strong>.`,
    PROJECT_INSERTED: ({ projectName, userName }) =>
      `You were added to the project <strong>${projectName}</strong> by <strong>${userName}</strong>.`,
    REMOVED: ({ projectName, userName }) =>
      `You were removed from the project <strong>${projectName}</strong> by <strong>${userName}</strong>. Contact them for more information.`,
    PROJECT_MESSAGE: ({ projectName, userName }) =>
      `You have a new message in the project <strong>${projectName}</strong> chat from <strong>${userName}</strong>.`,
    USER_TYPE_CHANGED: ({ projectName, userName, newUserType }) =>
      `Your user type was changed to <strong>${newUserType}</strong> by <strong>${userName}</strong> in the project <strong>${projectName}</strong>.`,
    PROJECT_UPDATED: ({ projectName, userName }) =>
      `The project <strong>${projectName}</strong> was updated by <strong>${userName}</strong>.`,
    LEFT_PROJECT: ({ projectName, userName }) =>
      `The user <strong>${userName}</strong> left the project <strong>${projectName}</strong>.`,
  };

  const getNotificationMessage = () => {
    const { type } = notification;
    const messageFunc = notificationMessages[type];
    return messageFunc ? messageFunc(notification) : "You have a new notification.";
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
