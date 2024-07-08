import "./Asides.css";
import "./AsideProjectCreationPage2.css";
import { Form } from "react-bootstrap";
import { formatLab } from "../../utilities/converters";
import { useState, useEffect } from "react";
import { getLabsWithSessionId } from "../../utilities/services";
import userProfileIcon from "../../resources/avatares/Avatar padrão.jpg";
import { Trans, t } from "@lingui/macro";
import { useMediaQuery } from "react-responsive";

export default function AsideProjectCreationPage2({
  selectedLab,
  setSelectedLab,
  usersInProject,
  setUsersInProject,
  rejectedUsers,
  setRejectedUsers,
  setGetUsersTrigger,
  maxMembers,
  setMaxMembers,
  isVisible,
}) {
  const [labs, setLabs] = useState([]);
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });

  useEffect(() => {
    getLabsWithSessionId().then((response) => {
      return response.json().then((data) => {
        setLabs(data);
      });
    });
  }, []);

  const removeUser = (id) => {
    setUsersInProject(usersInProject.filter((user) => user.id !== id));
    setRejectedUsers(rejectedUsers.filter((userId) => userId !== id));
    setGetUsersTrigger((prev) => !prev);
  };
  return (
    <>
      {((isVisible && isMobile) || !isMobile) && (
        <div className={!isMobile && "aside-background"}>
          <div className={!isMobile ? "aside" : "aside-Mobile"}>
            <div className="div-control-form">
              <Form.Label className="custom-label" style={{ color: "white" }}>
                <Trans>Lab</Trans>
              </Form.Label>
              <Form.Control
                as="select"
                className="custom-focus"
                value={selectedLab}
                onChange={(e) => {
                  setSelectedLab(e.target.value);
                  setGetUsersTrigger((prev) => !prev);
                }}
              >
                <option value="">
                  <Trans>All Labs</Trans>
                </option>
                {labs.map((lab, index) => (
                  <option key={index} value={lab.local}>
                    {formatLab(lab.local)}
                  </option>
                ))}
              </Form.Control>
            </div>
            <div className="div-control-form">
              <Form.Label className="custom-label" style={{ color: "white" }}>
                <Trans>Max Members</Trans>
              </Form.Label>
              <Form.Control
                type="text"
                placeholder="4"
                style={{ borderRadius: "10px", cursor: "text" }}
                className="custom-focus"
                value={maxMembers}
                onChange={(e) => {
                  setMaxMembers(e.target.value);
                }}
              />
            </div>
            <div className="container-users-project">
              <div className="users-in-project-label">
                <Trans>Users in Project</Trans>:
              </div>
              <div className="users-in-project-container">
                <div className="users-in-project-div">
                  {usersInProject.map((user, index) => (
                    <div className="user-in-project">
                      <img
                        src={user.photo !== null ? user.photo : userProfileIcon}
                        alt="user-img"
                      />
                      <div>{user.name}</div>
                      <div className="cross-user-in-project">
                        <button onClick={() => removeUser(user.id)}>
                          <i class="fas fa-times"></i>
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
