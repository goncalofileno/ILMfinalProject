import React from "react";
import "./NotificationModal.css";
import useNotificationStore from "../../stores/useNotificationStore";
import { useNavigate } from "react-router-dom";
import NotificationIcon from "../../resources/icons/other/notification.jpg";
import Cookies from "js-cookie";
import { markNotificationAsClicked } from "../../utilities/services";

export default function NotificationModal({ onClose, modalRef }) {
  const { notifications, loadMoreNotifications, hasMoreNotifications } =
    useNotificationStore();
  const navigate = useNavigate();

  const formatTime = (date) => {
    const now = new Date();
    const notificationDate = new Date(date);
    const diff = now - notificationDate;

    if (diff < 86400000) {
      // less than 24 hours
      const hours = Math.floor(diff / 3600000);
      const minutes = Math.floor((diff % 3600000) / 60000);
      if (hours > 0) {
        return `${hours} hours ago`;
      }
      return `${minutes} minutes ago`;
    }
    return notificationDate.toLocaleDateString("en-US", {
      day: "numeric",
      month: "short",
    });
  };

  const getNotificationMessage = (notification) => {
    const {
      type,
      projectName,
      userName,
      projectStatus,
      messageCount,
      newUserType,
    } = notification;
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
        if (messageCount && messageCount > 1) {
          return `You have ${messageCount} new messages in the project <strong>${projectName}</strong> chat.`;
        }
        return `You have a new message in the project <strong>${projectName}</strong> chat from <strong>${userName}</strong>.`;
      case "USER_TYPE_CHANGED":
        return `Your user type was changed to <strong>${newUserType}</strong> by <strong>${userName}</strong> in the project <strong>${projectName}</strong>.`;
      default:
        return "You have a new notification.";
    }
  };

  const handleNotificationClick = async (notification) => {
    const { type, projectSystemName, systemUserName, id, notificationIds } = notification;
    switch (type) {
      case "APPLIANCE_REJECTED":
      case "APPLIANCE_ACCEPTED":
      case "INVITE":
      case "PROJECT":
      case "PROJECT_REJECTED":
      case "USER_TYPE_CHANGED":
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
      case "PROJECT_MESSAGE":
        navigate(`/project/${projectSystemName}/chat`);
        // Marcar notificação como clicada
        const sessionId = Cookies.get("session-id");
        // Verifique se a notificação possui um array de IDs agrupados
        if (notificationIds) {
          await markNotificationAsClicked(sessionId, notificationIds);
        } else {
          await markNotificationAsClicked(sessionId, [id]);
        }
        break;
      default:
        break;
    }
  };

  const groupedNotifications = notifications.reduce((acc, notification) => {
    if (notification.type === "PROJECT_MESSAGE") {
      const existing = acc.find(
        (n) =>
          n.type === "PROJECT_MESSAGE" &&
          n.projectSystemName === notification.projectSystemName
      );
      if (existing) {
        existing.messageCount = (existing.messageCount || 1) + 1;
        existing.notificationIds.push(notification.id);
      } else {
        acc.push({
          ...notification,
          messageCount: 1,
          notificationIds: [notification.id],
        });
      }
    } else {
      acc.push(notification);
    }
    return acc;
  }, []);

  return (
    <div className="notification-modal" ref={modalRef}>
      <div className="notification-modal-content">
        {groupedNotifications.length === 0 ? (
          <div className="no-notifications">No notifications to show</div>
        ) : (
          groupedNotifications.map((notification) => (
            <div
              key={notification.id}
              className={`notification-item ${
                notification.readStatus ? "read" : "unread"
              }`}
              onClick={() => handleNotificationClick(notification)}
            >
              <div className="notification-item-header">
                <img
                  src={notification.userPhoto || NotificationIcon}
                  alt="User"
                />
                <span
                  dangerouslySetInnerHTML={{
                    __html: getNotificationMessage(notification),
                  }}
                ></span>
              </div>
              <span className="notification-date">
                {formatTime(notification.sendDate)}
              </span>
            </div>
          ))
        )}
      </div>
      {groupedNotifications.length > 0 && (
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
