import React, { useEffect, useState } from "react";
import AppNavbar from "../components/headers/AppNavbar";
import { useParams, useNavigate } from "react-router-dom";
import {
  getUserProfile,
  respondToInvite, // Import the function
  getUnreadNotificationsCount,
  fetchNotifications
} from "../utilities/services";
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Alert,
  Tab,
  Nav,
} from "react-bootstrap";
import Cookies from "js-cookie";
import "./ProfilePage.css"; // Certifique-se de criar e incluir este arquivo CSS.
import ComposeMailModal from "../components/modals/ComposeMailModal";
import InviteProjectModal from "../components/modals/InviteProjectModal.jsx"; // Importe o componente InviteProjectModal

const UserProfile = () => {
  const { systemUsername } = useParams();
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState(null);
  const [isPrivate, setIsPrivate] = useState(false);
  const [showComposeModal, setShowComposeModal] = useState(false); // Estado para visibilidade do modal de email
  const [showInviteModal, setShowInviteModal] = useState(false); // Estado para visibilidade do modal de convite
  const [preFilledContact, setPreFilledContact] = useState(""); // Estado para contato pré-preenchido
  const [unreadNotificationsCount, setUnreadNotificationsCount] = useState(0);
  const [notifications, setNotifications] = useState([]);
  const navigate = useNavigate();

  const loggedInUsername = Cookies.get("user-systemUsername");

  useEffect(() => {
    fetchProfile();
    fetchUnreadNotificationsCount();
    fetchAllNotifications(1); // Fetch first page of notifications
  }, [systemUsername]);

  const fetchProfile = async () => {
    try {
      const profileData = await getUserProfile(systemUsername);
      if (profileData.status === 200) {
        setProfile(profileData.data);
        if (
          !profileData.data.publicProfile &&
          (!profileData.data.projects || profileData.data.projects.length === 0)
        ) {
          setIsPrivate(true);
        }
      } else if (profileData.status === 401) {
        setError("Unauthorized: Invalid session.");
      } else if (profileData.status === 404) {
        setError("User not found.");
      }
    } catch (err) {
      setError("An error occurred while fetching the profile.");
    }
  };

  const fetchUnreadNotificationsCount = async () => {
    try {
      const sessionId = Cookies.get("session-id");
      const count = await getUnreadNotificationsCount(sessionId);
      setUnreadNotificationsCount(count);
    } catch (err) {
      console.error("Error fetching unread notifications count:", err);
    }
  };

  const fetchAllNotifications = async (page) => {
    try {
      const sessionId = Cookies.get("session-id");
      const notificationsData = await fetchNotifications(sessionId, page);
      setNotifications(notificationsData);
    } catch (err) {
      console.error("Error fetching notifications:", err);
    }
  };

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  if (!profile) {
    return <div>Loading...</div>;
  }

  const handleEditProfile = () => {
    navigate("/editProfile");
  };

  const handleSendMessage = () => {
    const contact = `${profile.firstName} ${profile.lastName} <${profile.email}>`;
    setPreFilledContact(contact);
    setShowComposeModal(true);
  };

  const handleInvite = () => {
    setShowInviteModal(true);
  };

  const handleCloseComposeModal = () => {
    setShowComposeModal(false);
  };

  const handleCloseInviteModal = () => {
    setShowInviteModal(false);
  };

  const handleRespondToInvite = async (projectName, response) => {
    try {
      const sessionId = Cookies.get("session-id");
      const result = await respondToInvite(sessionId, projectName, response);
      if (result.status === 200) {
        fetchProfile(); // Atualize o perfil após a resposta
      } else {
        setError(result.data);
      }
    } catch (err) {
      setError("An error occurred while responding to the invite.");
    }
  };

  const nonPendingProjects = profile.projects
    ? profile.projects.filter(
        (project) =>
          project.typeMember !== "PENDING_BY_APPLIANCE" &&
          project.typeMember !== "PENDING_BY_INVITATION"
      )
    : [];

  const pendingProjects = profile.projects
    ? profile.projects.filter(
        (project) =>
          project.typeMember === "PENDING_BY_APPLIANCE" ||
          project.typeMember === "PENDING_BY_INVITATION"
      )
    : [];

    return (
      <>
        <AppNavbar />
        <div className="bckg-color-ilm-page ilm-pageb">
          <Container className="mt-4 outer-container">
            <Row className="justify-content-md-center">
              <Col md="12">
                <Card className="shadow-sm ilm-form">
                  <Card.Body>
                    <Row>
                      <Col md="3" className="text-center">
                        <div className="profile-avatar mb-3 user-info-center">
                          <img
                            src={
                              profile.profileImage ||
                              "https://via.placeholder.com/150"
                            }
                            alt="Profile"
                            className="img-fluid rounded-circle"
                          />
                        </div>
                      </Col>
                      <Col md="3">
                        <div className="user-info-center">
                          <Card.Title className="form-title">
                            {profile.firstName} {profile.lastName}
                          </Card.Title>
                          <Card.Subtitle className="mb-2 text-muted">
                            {profile.username}
                          </Card.Subtitle>
                          <Card.Text>
                            <strong>Location:</strong> {profile.location}
                          </Card.Text>
                          {!isPrivate && profile.bio && (
                            <Card.Text>
                              <strong>Bio:</strong> {profile.bio}
                            </Card.Text>
                          )}
                          {loggedInUsername === systemUsername ? (
                            <Button
                              variant="primary"
                              onClick={handleEditProfile}
                              className="submit-button"
                              style={{ padding: "10px 20px" }}
                            >
                              Edit Profile
                            </Button>
                          ) : (
                            <div className="button-group">
                              <Button
                                variant="primary"
                                className="mr-3 submit-button"
                                style={{ padding: "10px 20px" }}
                                onClick={handleSendMessage}
                              >
                                Send Email
                              </Button>
                              <Button
                                variant="secondary"
                                className="invite-button"
                                style={{ padding: "10px 20px" }}
                                onClick={handleInvite}
                              >
                                Invite to Project
                              </Button>
                            </div>
                          )}
                        </div>
                      </Col>
                      <Col md="6">
                        <Tab.Container defaultActiveKey="projects">
                          <Nav
                            variant="pills"
                            className="flex-row pills-container"
                          >
                            <Nav.Item>
                              <Nav.Link eventKey="projects">Projects</Nav.Link>
                            </Nav.Item>
                            {loggedInUsername === systemUsername && (
                              <Nav.Item>
                                <Nav.Link eventKey="applications">
                                  Applications
                                </Nav.Link>
                              </Nav.Item>
                            )}
                            <Nav.Item>
                              <Nav.Link eventKey="skills">Skills</Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                              <Nav.Link eventKey="interests">Interests</Nav.Link>
                            </Nav.Item>
                          </Nav>
                          <Tab.Content>
                            <Tab.Pane eventKey="projects">
                              <Card className="shadow-sm mb-4 card-container">
                                <Card.Body className="card-body">
                                  {nonPendingProjects.length > 0 ? (
                                    nonPendingProjects.map((project) => (
                                      <div
                                        key={project.name}
                                        className="ilm-form mb-3"
                                      >
                                        <p className="mb-1">
                                          <strong>Project Name:</strong>{" "}
                                          {project.name}
                                        </p>
                                        <p className="mb-1">
                                          <strong>Type Member:</strong>{" "}
                                          {project.typeMember}
                                        </p>
                                        <p className="mb-1">
                                          <strong>Status:</strong>{" "}
                                          {project.status}
                                        </p>
                                      </div>
                                    ))
                                  ) : (
                                    <p className="centered-message">
                                      No projects available.
                                    </p>
                                  )}
                                </Card.Body>
                              </Card>
                            </Tab.Pane>
                            {loggedInUsername === systemUsername && (
                              <Tab.Pane eventKey="applications">
                                <Card className="shadow-sm mb-4 card-container">
                                  <Card.Body className="card-body">
                                    {pendingProjects.length > 0 ? (
                                      pendingProjects.map((project) => (
                                        <div
                                          key={project.name}
                                          className="ilm-form mb-3 d-flex"
                                        >
                                          <div>
                                            <p className="mb-1">
                                              <strong>Project Name:</strong>{" "}
                                              {project.name}
                                            </p>
                                            <p className="mb-1">
                                              <strong>Status:</strong>{" "}
                                              {project.status}
                                            </p>
                                          </div>
                                          {project.typeMember ===
                                            "PENDING_BY_INVITATION" && (
                                            <div className="button-group-horizontal">
                                              <Button
                                                variant="success"
                                                onClick={() =>
                                                  handleRespondToInvite(
                                                    project.name,
                                                    true
                                                  )
                                                }
                                              >
                                                Accept
                                              </Button>
                                              <Button
                                                variant="danger"
                                                onClick={() =>
                                                  handleRespondToInvite(
                                                    project.name,
                                                    false
                                                  )
                                                }
                                              >
                                                Reject
                                              </Button>
                                            </div>
                                          )}
                                        </div>
                                      ))
                                    ) : (
                                      <p className="centered-message">
                                        No applications available.
                                      </p>
                                    )}
                                  </Card.Body>
                                </Card>
                              </Tab.Pane>
                            )}
                            <Tab.Pane eventKey="skills">
                              <Card className="shadow-sm mb-4 card-container">
                                <Card.Body className="card-body">
                                  {profile.skills && profile.skills.length > 0 ? (
                                    profile.skills.map((skill) => (
                                      <div
                                        key={skill.id}
                                        className="ilm-form mb-3"
                                      >
                                        <p className="mb-1">
                                          <strong>{skill.name}</strong>
                                        </p>
                                        <p
                                          className="text-muted mb-1"
                                          style={{ fontSize: "0.85em" }}
                                        >
                                          {skill.type}
                                        </p>
                                      </div>
                                    ))
                                  ) : (
                                    <p className="centered-message">
                                      No skills available.
                                    </p>
                                  )}
                                </Card.Body>
                              </Card>
                            </Tab.Pane>
                            <Tab.Pane eventKey="interests">
                              <Card className="shadow-sm mb-4 card-container">
                                <Card.Body className="card-body">
                                  {profile.interests &&
                                  profile.interests.length > 0 ? (
                                    profile.interests.map((interest) => (
                                      <div
                                        key={interest.id}
                                        className="ilm-form mb-3"
                                      >
                                        {interest.name}
                                      </div>
                                    ))
                                  ) : (
                                    <p className="centered-message">
                                      No interests available.
                                    </p>
                                  )}
                                </Card.Body>
                              </Card>
                            </Tab.Pane>
                          </Tab.Content>
                        </Tab.Container>
                      </Col>
                    </Row>
                  </Card.Body>
                </Card>
              </Col>
            </Row>
          </Container>
        </div>
        <ComposeMailModal
          show={showComposeModal}
          handleClose={handleCloseComposeModal}
          preFilledContact={preFilledContact}
        />
        <InviteProjectModal
          show={showInviteModal}
          handleClose={handleCloseInviteModal}
          systemUsername={systemUsername}
        />
      </>
    );
    
};

export default UserProfile;