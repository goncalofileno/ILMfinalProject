import React, { useEffect } from "react";
import { Alert } from "react-bootstrap";
import Cookies from "js-cookie";
import "./NotificationBanner.css";

const NotificationBanner = ({ notification, onClick, onEnd }) => {
  useEffect(() => {
    const timer = setTimeout(onEnd, 3000); // Chama onEnd após 3 segundos

    return () => clearTimeout(timer);
  }, [notification, onEnd]);

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
      `O projeto <strong>${projectName}</strong> mudou o seu estado para <strong>${projectStatus}</strong>.`,
    PROJECT_REJECTED: ({ projectName, userName }) =>
      `O projeto <strong>${projectName}</strong> foi rejeitado por <strong>${userName}</strong>.`,
    PROJECT_INSERTED: ({ projectName, userName }) =>
      `Foi adicionado ao projeto <strong>${projectName}</strong> por <strong>${userName}</strong>.`,
    REMOVED: ({ projectName, userName }) =>
      `Foi removido do projeto <strong>${projectName}</strong> por <strong>${userName}</strong>. Contacte-os para mais informações.`,
    PROJECT_MESSAGE: ({ projectName, userName }) =>
      `Tem uma nova mensagem no chat do projeto <strong>${projectName}</strong> de <strong>${userName}</strong>.`,
    USER_TYPE_CHANGED: ({ projectName, userName, newUserType }) =>
      `O seu tipo de utilizador foi alterado para <strong>${newUserType}</strong> por <strong>${userName}</strong> no projeto <strong>${projectName}</strong>.`,
    PROJECT_UPDATED: ({ projectName, userName }) =>
      `O projeto <strong>${projectName}</strong> foi atualizado por <strong>${userName}</strong>.`,
    LEFT_PROJECT: ({ projectName, userName }) =>
      `O utilizador <strong>${userName}</strong> saiu do projeto <strong>${projectName}</strong>.`,
    PROMOTE_TO_ADMIN: ({ userName }) =>
      `Foi promovido a administrador por <strong>${userName}</strong>.`,
  };

  const getNotificationMessage = () => {
    const { type } = notification;
    const userLanguage = Cookies.get("user-language") || "ENGLISH";
    const messages =
      userLanguage === "PORTUGUESE" ? notificationMessagesPt : notificationMessagesEn;
    const messageFunc = messages[type];
    return messageFunc
      ? messageFunc(notification)
      : userLanguage === "PORTUGUESE"
      ? "Você tem uma nova notificação."
      : "You have a new notification.";
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
