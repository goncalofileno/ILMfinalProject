import React, { useEffect, useState } from "react";
import "./NotificationModal.css";
import useNotificationStore from "../../stores/useNotificationStore";
import userProfileIcon from "../../resources/avatares/Avatar padrão.jpg";

export default function NotificationModal({ onClose }) {
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const { notifications, fetchNotifications } = useNotificationStore();

  useEffect(() => {
    const loadNotifications = async () => {
      setLoading(true);
      await fetchNotifications(page);
      setLoading(false);
    };

    loadNotifications();
  }, [page, fetchNotifications]);

  const loadMore = () => {
    setPage((prevPage) => prevPage + 1);
  };

  const handleClose = () => {
    setPage(1);
    onClose();
  };

  return (
    <div className="notification-modal">
      <div className="notification-modal-header">
        <h4>Notifications</h4>
        <button onClick={handleClose}>&times;</button>
      </div>
      <div className="notification-modal-content">
        {notifications.map((notification) => (
          <div
            key={notification.id}
            className={`notification-item ${
              notification.readStatus ? "read" : "unread"
            }`}
          >
            <div className="notification-item-header">
              <img src={notification.userPhoto || userProfileIcon} alt="User" />
              <span>{notification.userName}</span>
            </div>
            <p>{notification.projectName}</p>
            <span className="notification-date">
              {new Date(notification.sendDate).toLocaleString()}
            </span>
          </div>
        ))}
      </div>
      <div className="notification-modal-footer">
        {loading ? (
          <p>Loading...</p>
        ) : (
          <button onClick={loadMore}>Load More</button>
        )}
      </div>
    </div>
  );
}
