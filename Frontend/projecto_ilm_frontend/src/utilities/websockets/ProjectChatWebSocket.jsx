import React, { useEffect, useRef, useState } from "react";
import Cookies from "js-cookie";
import useChatStore from "../../stores/useChatStore";

const ProjectChatWebSocket = ({ projectId }) => {
  const { addMessage, setOnlineMembers } = useChatStore();
  // const audioRef = useRef(new Audio("/notification_sound.wav"));
  const socketRef = useRef(null);
  const [audioEnabled, setAudioEnabled] = useState(false);

  // useEffect(() => {
  //   const enableAudio = () => {
  //     setAudioEnabled(true);
  //     document.removeEventListener("click", enableAudio);
  //   };

  //   document.addEventListener("click", enableAudio);

  //   return () => {
  //     document.removeEventListener("click", enableAudio);
  //   };
  // }, []);

  useEffect(() => {
    const systemUsername = Cookies.get("user-systemUsername");

    if (systemUsername) {

      const socket = new WebSocket(
        `ws://localhost:8080/projeto_ilm_final/ws/chat/${projectId}/${systemUsername}`
      );



      socketRef.current = socket;

      socket.onopen = () => {
        console.log("");
      };

      socket.onmessage = (event) => {
        try {
          const parsedData = JSON.parse(event.data);
          if (parsedData.type === "new_message") {
            addMessage(parsedData.message);
          } else if (parsedData.type === "online_members") {
            setOnlineMembers(parsedData.message.members);
          } else {
            console.warn("Unknown message type received:", parsedData.type);
          }
        } catch (e) {
          console.error("Error parsing WebSocket message:", e);
        }
      };

      socket.onclose = () => {
        console.log("");
      };

      socket.onerror = (error) => {
        console.error("WebSocket error:", error);
      };
    }

    return () => {
      if (socketRef.current) {
        socketRef.current.close();
      }
    };
  }, [projectId, addMessage, setOnlineMembers]);

  return null;
};

export default ProjectChatWebSocket;
