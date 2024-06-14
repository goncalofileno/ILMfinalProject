import React from "react";
import "./NotificationModal.css";
import useNotificationStore from "../../stores/useNotificationStore";
import { useNavigate } from "react-router-dom";
import NotificationIcon from "../../resources/icons/other/notification.jpg";

export default function NotificationModal({ onClose, modalRef }) {
  const { notifications, loadMoreNotifications, hasMoreNotifications } = useNotificationStore();
  const navigate = useNavigate();

  const formatTime = (date) => {
    const now = new Date();
    const notificationDate = new Date(date);
    const diff = now - notificationDate;

    if (diff < 86400000) { // less than 24 hours
      const hours = Math.floor(diff / 3600000);
      const minutes = Math.floor((diff % 3600000) / 60000);
      if (hours > 0) {
        return `${hours} hours ago`;
      }
      return `${minutes} minutes ago`;
    }
    return notificationDate.toLocaleDateString('en-US', {
      day: 'numeric',
      month: 'short'
    });
  };

  const getNotificationMessage = (notification) => {
    const { type, projectName, userName, projectSystemName, systemUserName, projectStatus } = notification;
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
      case "REMOVED":
        return `You were removed from the project <strong>${projectName}</strong> by <strong>${userName}</strong>. Contact them for more information.`;
      default:
        return "You have a new notification.";
    }
  };

  const handleNotificationClick = (notification) => {
    const { type, projectSystemName, systemUserName } = notification;
    switch (type) {
      case "APPLIANCE_REJECTED":
      case "APPLIANCE_ACCEPTED":
      case "INVITE":
      case "PROJECT":
        navigate(`/project/${projectSystemName}`);
        break;
      case "APPLIANCE":
      case "INVITE_ACCEPTED":
        navigate(`/project/${projectSystemName}/members`);
        break;
      case "TASK":
        navigate(`/project/${projectSystemName}/tasks`);
        break;
      case "INVITE_REJECTED":
      case "REMOVED":
        navigate(`/profile/${systemUserName}`);
        break;
      default:
        break;
    }
  };

  return (
    <div className="notification-modal" ref={modalRef}>
      <div className="notification-modal-content">
        {notifications.length === 0 ? (
          <div className="no-notifications">No notifications to show</div>
        ) : (
          notifications.map((notification) => (
            <div
              key={notification.id}
              className={`notification-item ${
                notification.readStatus ? "read" : "unread"
              }`}
              onClick={() => handleNotificationClick(notification)}
            >
              <div className="notification-item-header">
                <img src={notification.userPhoto || NotificationIcon} alt="User" />
                <span dangerouslySetInnerHTML={{ __html: getNotificationMessage(notification) }}></span>
              </div>
              <span className="notification-date">
                {formatTime(notification.sendDate)}
              </span>
            </div>
          ))
        )}
      </div>
      {notifications.length > 0 && (
        <div className="notification-modal-footer">
          {hasMoreNotifications ? (
            <button onClick={loadMoreNotifications}>Load More</button>
          ) : (
            <p>No more notifications</p>
          )}
        </div>
      )}
    </div>
  );
}
