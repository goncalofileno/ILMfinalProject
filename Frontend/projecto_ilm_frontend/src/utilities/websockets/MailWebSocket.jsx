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
          fetchMailsInInbox(true);  // Ensure fetching mails
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

  const handleNotificationClick = () => {
    if (notification) {
      const { type, projectSystemName, systemUserName } = notification;
      switch (type) {
        case "APPLIANCE_REJECTED":
        case "APPLIANCE_ACCEPTED":
        case "INVITE":
        case "PROJECT":
        case "PROJECT_REJECTED":
        case "USER_TYPE_CHANGED":
        case "PROJECT_UPDATED":
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
          break;
        default:
          break;
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
