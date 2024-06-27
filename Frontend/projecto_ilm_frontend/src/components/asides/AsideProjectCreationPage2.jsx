import "./Asides.css";
import "./AsideProjectCreationPage2.css";
import { Form } from "react-bootstrap";
import { formatLab } from "../../utilities/converters";
import { useState, useEffect } from "react";
import { getLabsWithSessionId } from "../../utilities/services";
import userProfileIcon from "../../resources/avatares/Avatar padrão.jpg";

export default function AsideProjectCreationPage2({
  selectedLab,
  setSelectedLab,
  usersInProject,
  setUsersInProject,
  rejectedUsers,
  setRejectedUsers,
  setGetUsersTrigger,
}) {
  const [labs, setLabs] = useState([]);

  useEffect(() => {
    getLabsWithSessionId().then((response) => {
      return response.json().then((data) => {
        setLabs(data);
      });
    });
  }, []);

  const removeUser = (id) => {
    console.log("removing user", id);
    setUsersInProject(usersInProject.filter((user) => user.id !== id));
    setRejectedUsers(rejectedUsers.filter((userId) => userId !== id));
    setGetUsersTrigger((prev) => !prev);
  };
  return (
    <div className="aside-background">
      <div className="aside">
        <div className="div-control-form">
          <Form.Label className="custom-label" style={{ color: "white" }}>
            Lab
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
            <option value="">All Labs</option>
            {labs.map((lab, index) => (
              <option key={index} value={lab.local}>
                {formatLab(lab.local)}
              </option>
            ))}
          </Form.Control>
        </div>
        <div className="container-users-project">
          <div className="users-in-project-label">Users in Project:</div>
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
                    <button>
                      <i
                        class="fas fa-times"
                        onClick={() => removeUser(user.id)}
                      ></i>
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
