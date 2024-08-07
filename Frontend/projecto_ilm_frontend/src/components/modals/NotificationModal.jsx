import React from "react";
import "./NotificationModal.css";
import useNotificationStore from "../../stores/useNotificationStore";
import { useNavigate } from "react-router-dom";
import NotificationIcon from "../../resources/icons/other/notification.jpg";
import Cookies from "js-cookie";
import { markNotificationAsClicked } from "../../utilities/services";
import { Trans, t } from "@lingui/macro";
import { formatProjectState } from "../../utilities/converters";

export default function NotificationModal({ onClose, modalRef }) {
  const { notifications, loadMoreNotifications, hasMoreNotifications } =
    useNotificationStore();
  const navigate = useNavigate();
  const loggedInUsername = Cookies.get("user-systemUsername");

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

  const notificationMessagesEn = {
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
      `The project <strong>${projectName}</strong> changed its status to <strong>${formatProjectState(projectStatus)}</strong>.`,
    PROJECT_REJECTED: ({ projectName, userName }) =>
      `The project <strong>${projectName}</strong> was rejected by <strong>${userName}</strong>.`,
    PROJECT_INSERTED: ({ projectName, userName }) =>
      `You were added to the project <strong>${projectName}</strong> by <strong>${userName}</strong>.`,
    REMOVED: ({ projectName, userName }) =>
      `You were removed from the project <strong>${projectName}</strong> by <strong>${userName}</strong>. Contact them for more information.`,
    PROJECT_MESSAGE: ({ projectName, userName, messageCount }) =>
      messageCount && messageCount > 1
        ? `You have ${messageCount} new messages in the project <strong>${projectName}</strong> chat.`
        : `You have a new message in the project <strong>${projectName}</strong> chat from <strong>${userName}</strong>.`,
    USER_TYPE_CHANGED: ({ projectName, userName, newUserType }) =>
      `Your user type was changed to <strong>${newUserType}</strong> by <strong>${userName}</strong> in the project <strong>${projectName}</strong>.`,
    PROJECT_UPDATED: ({ projectName, userName }) =>
      `The project details <strong>${projectName}</strong> was updated by <strong>${userName}</strong>.`,
    LEFT_PROJECT: ({ projectName, userName }) =>
      `The user <strong>${userName}</strong> left the project <strong>${projectName}</strong>.`,
    PROMOTE_TO_ADMIN: ({ userName }) =>
      `You were promoted to admin by <strong>${userName}</strong>.`,
  };

  const notificationMessagesPt = {
    APPLIANCE_REJECTED: ({ projectName, userName }) =>
      `A sua candidatura ao projeto <strong>${projectName}</strong> foi rejeitada por <strong>${userName}</strong>.`,
    APPLIANCE_ACCEPTED: ({ projectName, userName }) =>
      `A sua candidatura ao projeto <strong>${projectName}</strong> foi aceite por <strong>${userName}</strong>.`,
    APPLIANCE: ({ projectName, userName }) =>
      `<strong>${userName}</strong> candidatou-se ao seu projeto <strong>${projectName}</strong>.`,
    TASK: ({ projectName, userName }) =>
      `<strong>${userName}</strong> fez alterações numa tarefa no projeto <strong>${projectName}</strong> em que você está envolvido.`,
    TASK_ASSIGNED: ({ projectName, userName }) =>
      `<strong>${userName}</strong> atribuiu-lhe uma tarefa no projeto <strong>${projectName}</strong>.`,
    INVITE_REJECTED: ({ projectName, userName }) =>
      `<strong>${userName}</strong> rejeitou o seu convite para se juntar ao projeto <strong>${projectName}</strong>.`,
    INVITE_ACCEPTED: ({ projectName, userName }) =>
      `<strong>${userName}</strong> aceitou o seu convite para se juntar ao projeto <strong>${projectName}</strong>.`,
    INVITE: ({ projectName, userName }) =>
      `<strong>${userName}</strong> convidou-o para se juntar ao projeto <strong>${projectName}</strong>.`,
    PROJECT: ({ projectName, projectStatus }) =>
      `O projeto <strong>${projectName}</strong> mudou o seu estado para <strong>${formatProjectState(projectStatus)}</strong>.`,
    PROJECT_REJECTED: ({ projectName, userName }) =>
      `O projeto <strong>${projectName}</strong> foi rejeitado por <strong>${userName}</strong>.`,
    PROJECT_INSERTED: ({ projectName, userName }) =>
      `Foi adicionado ao projeto <strong>${projectName}</strong> por <strong>${userName}</strong>.`,
    REMOVED: ({ projectName, userName }) =>
      `Foi removido do projeto <strong>${projectName}</strong> por <strong>${userName}</strong>. Contacte-os para mais informações.`,
    PROJECT_MESSAGE: ({ projectName, userName, messageCount }) =>
      messageCount && messageCount > 1
        ? `Você tem ${messageCount} novas mensagens no chat do projeto <strong>${projectName}</strong>.`
        : `Você tem uma nova mensagem no chat do projeto <strong>${projectName}</strong> de <strong>${userName}</strong>.`,
    USER_TYPE_CHANGED: ({ projectName, userName, newUserType }) =>
      `O seu tipo de utilizador foi alterado para <strong>${newUserType}</strong> por <strong>${userName}</strong> no projeto <strong>${projectName}</strong>.`,
    PROJECT_UPDATED: ({ projectName, userName }) =>
      `Os detalhes do projeto <strong>${projectName}</strong> foram atualizados por <strong>${userName}</strong>.`,
    LEFT_PROJECT: ({ projectName, userName }) =>
      `O utilizador <strong>${userName}</strong> saiu do projeto <strong>${projectName}</strong>.`,
    PROMOTE_TO_ADMIN: ({ userName }) =>
      `Foi promovido a administrador por <strong>${userName}</strong>.`,
  };

  const getNotificationMessage = (notification) => {
    const userLanguage = Cookies.get("user-language") || "ENGLISH";
    const messages =
      userLanguage === "PORTUGUESE" ? notificationMessagesPt : notificationMessagesEn;
    const messageFunc = messages[notification.type];
    return messageFunc
      ? messageFunc(notification)
      : userLanguage === "PORTUGUESE"
      ? "Você tem uma nova notificação."
      : "You have a new notification.";
  };

  const notificationNavigation = {
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
    TASK: (projectSystemName) => navigate(`/project/${projectSystemName}/plan`),
    TASK_ASSIGNED: (projectSystemName) => navigate(`/project/${projectSystemName}/plan`),
    INVITE_REJECTED: (systemUserName) => navigate(`/profile/${systemUserName}`),
    REMOVED: (systemUserName) => navigate(`/profile/${systemUserName}`),
    PROJECT_MESSAGE: async (projectSystemName, id, notificationIds, sessionId) => {
      navigate(`/project/${projectSystemName}/chat`);
      if (notificationIds) {
        await markNotificationAsClicked(sessionId, notificationIds);
      } else {
        await markNotificationAsClicked(sessionId, [id]);
      }
    },
    PROMOTE_TO_ADMIN: (systemUserName) => navigate(`/profile/${loggedInUsername}`),
  };

  const handleNotificationClick = async (notification) => {
    const { type, projectSystemName, systemUserName, id, notificationIds } = notification;
    const sessionId = Cookies.get("session-id");

    const navigateFunc = notificationNavigation[type];
    if (navigateFunc) {
      if (type === "PROJECT_MESSAGE") {
        await navigateFunc(projectSystemName, id, notificationIds, sessionId);
      } else if (type === "INVITE_REJECTED" || type === "REMOVED") {
        navigateFunc(systemUserName);
      } else {
        navigateFunc(projectSystemName);
      }
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
          <div className="no-notifications"><Trans>No notifications to show</Trans></div>
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
            <button onClick={loadMoreNotifications}><Trans>Load More</Trans></button>
          ) : (
            <p><Trans>No more notifications</Trans></p>
          )}
        </div>
      )}
    </div>
  );
}
