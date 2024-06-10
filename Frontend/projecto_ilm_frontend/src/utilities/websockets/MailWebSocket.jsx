import React, { useEffect, useState, useRef } from "react";
import Cookies from "js-cookie";
import useMailStore from "../../stores/useMailStore";
import { useLocation, useNavigate } from "react-router-dom";
import TimeoutModal from "../../components/modals/TimeoutModal";
import MailNotificationBanner from "../../components/banners/MailNotificationBanner";

const MailWebSocket = () => {
  const { incrementUnreadCount, fetchMailsInInbox } = useMailStore();
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
        if (event.data.startsWith("new_mail:")) {
          incrementUnreadCount();
          const senderName = event.data.split(":")[1];
          setNotification(`${senderName} sent you a new mail.`);
          if (audioEnabled) {
            audioRef.current.play();
          }
        } else if (event.data.startsWith("real_time_mail:") && isInboxPage) {
          incrementUnreadCount();
          fetchMailsInInbox();
          const senderName = event.data.split(":")[1];
          setNotification(`${senderName} sent you a new mail.`);
          if (audioEnabled) {
            audioRef.current.play();
          }
        } else if (event.data === "timeout") {
          setShowTimeoutModal(true);
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
  }, [incrementUnreadCount, fetchMailsInInbox, location.pathname, audioEnabled]);

  const handleTimeoutModalClose = () => {
    setShowTimeoutModal(false);
    Cookies.remove("session-id");
    Cookies.remove("user-systemUsername");
    Cookies.remove("user-userType");
    navigate("/"); // Redireciona para a página de login
  };

  const handleNotificationClick = () => {
    navigate("/mail/inbox");
    setNotification(null);
  };

  const handleNotificationEnd = () => {
    setNotification(null); // Reseta a notificação após a animação
  };

  return (
    <>
      <TimeoutModal show={showTimeoutModal} handleClose={handleTimeoutModalClose} />
      {notification && (
        <MailNotificationBanner
          message={notification}
          onClick={handleNotificationClick}
          onEnd={handleNotificationEnd} // Adiciona a função para resetar a notificação
        />
      )}
    </>
  );
};

export default MailWebSocket;
