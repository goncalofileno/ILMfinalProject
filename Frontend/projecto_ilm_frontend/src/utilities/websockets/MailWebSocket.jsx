import React, { useEffect, useState, useRef } from "react";
import Cookies from "js-cookie";
import useChatStore from "../../stores/useChatStore";
import useMailStore from "../../stores/useMailStore";
import useNotificationStore from "../../stores/useNotificationStore";
import { useLocation, useNavigate } from "react-router-dom";
import TimeoutModal from "../../components/modals/TimeoutModal";
import NotificationBanner from "../../components/banners/NotificationBanner";

const MailWebSocket = () => {
  const { incrementUnreadCount, fetchMailsInInbox } = useMailStore();
  const { setOnlineMembers } = useChatStore();
  const { incrementUnreadNotificationsCount } = useNotificationStore();
  const location = useLocation();
  const [showTimeoutModal, setShowTimeoutModal] = useState(false);
  const [notification, setNotification] = useState(null);
  const navigate = useNavigate();
  const [audioEnabled, setAudioEnabled] = useState(false);
  const audioRef = useRef(new Audio("/notification_sound.wav"));

  useEffect(() => {
    const enableAudio = () => {
      setAudioEnabled(true);
      document.removeEventListener("click", enableAudio);
    };

    document.addEventListener("click", enableAudio);

    return () => {
      document.removeEventListener("click", enableAudio);
    };
  }, []);

  useEffect(() => {
    const sessionId = Cookies.get("session-id");
    let socket;

    const isInboxPage = location.pathname === "/mail/inbox";

    if (sessionId) {
      socket = new WebSocket(
        `ws://localhost:8080/projeto_ilm_final/ws/mail/${sessionId}/${
          isInboxPage ? "inbox" : ""
        }`
      );

      socket.onopen = () => {
        console.log("WebSocket connection opened");
      };

      socket.onmessage = (event) => {
        console.log("WebSocket message received:", event.data);
        if (event.data.startsWith("new_mail:") && isInboxPage) {
          console.log("Real-time mail received");
          incrementUnreadCount();
          fetchMailsInInbox(true); // Ensure fetching mails
          const senderName = event.data.split(":")[1];
          if (audioEnabled) {
            audioRef.current.play();
          }
        } else if (event.data === "timeout") {
          setShowTimeoutModal(true);
        } else {
          try {
            const parsedData = JSON.parse(event.data);
            if (parsedData.type === "online_members") {
              setOnlineMembers(parsedData.message.members);
            } else {
              setNotification(parsedData);
              incrementUnreadNotificationsCount();
              if (audioEnabled) {
                audioRef.current.play();
              }
            }
          } catch (e) {
            console.error("Error parsing WebSocket message:", e);
          }
        }
      };

      socket.onclose = () => {
        console.log("WebSocket connection closed");
      };

      socket.onerror = (error) => {
        console.error("WebSocket error:", error);
      };
    }

    return () => {
      if (socket) {
        socket.close();
      }
    };
  }, [
    incrementUnreadCount,
    fetchMailsInInbox,
    location.pathname,
    audioEnabled,
    setOnlineMembers,
    incrementUnreadNotificationsCount,
  ]);

  const handleTimeoutModalClose = () => {
    setShowTimeoutModal(false);
    Cookies.remove("session-id");
    Cookies.remove("user-systemUsername");
    Cookies.remove("user-userType");
    navigate("/"); // Redirect to login page
  };

  const notificationHandlers = {
    APPLIANCE_REJECTED: (projectSystemName) => navigate(`/project/${projectSystemName}`),
    APPLIANCE_ACCEPTED: (projectSystemName) => navigate(`/project/${projectSystemName}`),
    INVITE: (projectSystemName) => navigate(`/project/${projectSystemName}`),
    PROJECT: (projectSystemName) => navigate(`/project/${projectSystemName}`),
    PROJECT_REJECTED: (projectSystemName) => navigate(`/project/${projectSystemName}`),
    USER_TYPE_CHANGED: (projectSystemName) => navigate(`/project/${projectSystemName}`),
    PROJECT_UPDATED: (projectSystemName) => navigate(`/project/${projectSystemName}`),
    PROJECT_INSERTED: (projectSystemName) => navigate(`/project/${projectSystemName}`),
    APPLIANCE: (projectSystemName) => navigate(`/project/${projectSystemName}/members`),
    INVITE_ACCEPTED: (projectSystemName) => navigate(`/project/${projectSystemName}/members`),
    LEFT_PROJECT: (projectSystemName) => navigate(`/project/${projectSystemName}/members`),
    TASK: (projectSystemName) => navigate(`/project/${projectSystemName}/tasks`),
    TASK_ASSIGNED: (projectSystemName) => navigate(`/project/${projectSystemName}/tasks`),
    INVITE_REJECTED: (systemUserName) => navigate(`/profile/${systemUserName}`),
    REMOVED: (systemUserName) => navigate(`/profile/${systemUserName}`),
    PROJECT_MESSAGE: (projectSystemName) => navigate(`/project/${projectSystemName}/chat`),    
  };

  const handleNotificationClick = () => {
    if (notification) {
      const { type, projectSystemName, systemUserName } = notification;
      const handler = notificationHandlers[type];
      if (handler) {
        handler(projectSystemName || systemUserName);
      }
    }
    setNotification(null);
  };

  const handleNotificationEnd = () => {
    setNotification(null); // Reset notification after animation ends
  };

  return (
    <>
      <TimeoutModal
        show={showTimeoutModal}
        handleClose={handleTimeoutModalClose}
      />
      {notification && (
        <NotificationBanner
          notification={notification}
          onClick={handleNotificationClick}
          onEnd={handleNotificationEnd} // Reset notification after animation ends
        />
      )}
    </>
  );
};

export default MailWebSocket;
