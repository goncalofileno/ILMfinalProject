import "./MyProjectCard.css";
import "./UserCard.css";
import userProfileIcon from "../../resources/avatares/Avatar padrão.jpg";
import { Trans, t } from "@lingui/macro";
import { Alert } from "react-bootstrap";
import React from "react";

export default function UserCard({
  name,
  lab,
  skills,
  img,
  id,
  systemUsername,
  usersInProject,
  setUsersInProject,
  rejectedUsers,
  setRejectedUsers,
  setGetUsersTrigger,
  maxMembers,
  numberOfMembersInProject,
  publicProfile,
}) {
  const openNewWindow = () => {
    const url = `https://localhost:3000/profile/${systemUsername}`; // The URL you want to navigate to
    const newWindow = window.open(url, "_blank", "noopener,noreferrer");

    if (newWindow) newWindow.opener = null;
  };

  const handleAddUser = () => {
    const user = {
      name: name,
      photo: img,
      id: id,
    };
    setUsersInProject([...usersInProject, user]);
    setRejectedUsers([...rejectedUsers, user.id]);
    setGetUsersTrigger((prev) => !prev);
  };

  const handleRejectUser = () => {
    const user = {
      name: name,
      photo: img,
      id: id,
    };
    setRejectedUsers([...rejectedUsers, user.id]);
    setGetUsersTrigger((prev) => !prev);
  };

  return (
    <div style={{ height: "100%", display: "flex", justifyContent: "center" }}>
      <div className="my-project-card user-card" style={{ height: "100%" }}>
        <div
          className="my-projects-banner user-card-banner"
          onClick={openNewWindow}
        >
          <div className="div-image-user-card">
            <img
              src={img !== null ? img : userProfileIcon}
              alt=""
              className="user-card-img"
            />
          </div>
          <div className="div-user-card-name">
            <div className="banner-user-name">{name}</div>
            <div className="banner-user-lab">{lab}</div>
          </div>
        </div>
        <div className="user-card-body" onClick={openNewWindow}>
          {publicProfile ? (
            skills.map((skill) => (
              <div className={`skill-div-${skill.inProject}`}>
                <div className="skill-div-col1">{skill.name}</div>
                <div className="skill-div-col2">{skill.type}</div>
              </div>
            ))
          ) : (
            <div
              style={{
                height: "100%",
                width: "100%",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <Alert
                variant="danger"
                style={{
                  padding: "10px 5px",
                }}
              >
                <span
                  style={{
                    height: "100%",
                    display: "flex",
                    alignItems: "center",
                    fontSize: "16px",
                  }}
                >
                  This user has a private profile
                </span>
              </Alert>
            </div>
          )}
        </div>
        <div className="user-card-footer">
          <button
            className="submit-button user-card-button"
            onClick={handleAddUser}
            disabled={numberOfMembersInProject >= maxMembers - 1 ? true : false}
          >
            <Trans>Add</Trans>
          </button>
          <button className="secondary-button" onClick={handleRejectUser}>
            <Trans>Reject</Trans>
          </button>
        </div>
      </div>
    </div>
  );
}
