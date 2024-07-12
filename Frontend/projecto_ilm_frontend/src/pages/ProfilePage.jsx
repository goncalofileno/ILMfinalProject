import React, { useEffect, useState } from "react";
import AppNavbar from "../components/headers/AppNavbar";
import { useParams, useNavigate } from "react-router-dom";
import {
  getUserProfile,
  respondToInvite,
  getUnreadNotificationsCount,
  fetchNotifications,
  promoteUserToAdmin,
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
  Modal,
  Form,
} from "react-bootstrap";
import Cookies from "js-cookie";
import { CSSTransition } from "react-transition-group";
import "./ProfilePage.css";
import "./AlertAnimation.css";
import ComposeMailModal from "../components/modals/ComposeMailModal";
import InviteProjectModal from "../components/modals/InviteProjectModal.jsx";
import { Trans, t } from "@lingui/macro";
import {
  formatProjectState,
  formatTypeUserInProject,
} from "../utilities/converters.js";
import { useMediaQuery } from "react-responsive";

const UserProfile = () => {
  const { systemUsername, section } = useParams();
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState(null);
  const [isPrivate, setIsPrivate] = useState(false);
  const [showComposeModal, setShowComposeModal] = useState(false);
  const [showInviteModal, setShowInviteModal] = useState(false);
  const [preFilledContact, setPreFilledContact] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const [unreadNotificationsCount, setUnreadNotificationsCount] = useState(0);
  const [notifications, setNotifications] = useState([]);
  const [activeKey, setActiveKey] = useState(section || "projects");
  const [showConfirmModal, setShowConfirmModal] = useState(false);
  const [pendingProjectToReject, setPendingProjectToReject] = useState(null);
  const [projectFilter, setProjectFilter] = useState("");
  const [sortOrder, setSortOrder] = useState("asc");
  const isMobile = useMediaQuery({ query: "(max-width: 767px)" });
  const isTablet = useMediaQuery({ query: "(max-width: 991px)" });
  const navigate = useNavigate();
  const atualUserType = Cookies.get("user-userType");
  const [currentLanguage, setCurrentLanguage] = useState(
    Cookies.get("user-language") || "ENGLISH"
  );
  const loggedInUsername = Cookies.get("user-systemUsername");
  const [changeUserModal, setChangeUserModal] = useState(false);

  const fetchProfile = async () => {
    try {
      const profileData = await getUserProfile(systemUsername);
      if (profileData.status === 200) {
        setProfile(profileData.data);
        if (
          loggedInUsername !== systemUsername &&
          !profileData.data.publicProfile &&
          (!profileData.data.projects || profileData.data.projects.length === 0)
        ) {
          setIsPrivate(true);
        } else {
          setIsPrivate(false);
        }
      } else if (profileData.status === 401) {
        setError(t`Unauthorized: Invalid session.`);
      } else if (profileData.status === 404) {
        setError(t`User not found.`);
      }
    } catch (err) {
      setError(t`An error occurred while fetching the profile.`);
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

  const handleSendMessage = () => {
    const contact = `${profile.firstName} ${profile.lastName} <${profile.email}>`;
    setPreFilledContact(contact);
    setShowComposeModal(true);
    navigate(`/profile/${systemUsername}/message`);
  };

  useEffect(() => {
    fetchProfile();
    fetchUnreadNotificationsCount();
    fetchAllNotifications(1);
  }, [systemUsername]);

  useEffect(() => {
    if (profile) {
      setActiveKey(section || "projects");
      if (section === "message") {
        handleSendMessage();
      }
    }
  }, [profile, section]);

  useEffect(() => {
    if (successMessage) {
      setShowAlert(true);
      setTimeout(() => setShowAlert(false), 3000);
    }
  }, [successMessage]);

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  if (!profile) {
    return (
      <div>
        <Trans>Loading...</Trans>
      </div>
    );
  }

  const handleEditProfile = () => {
    navigate("/editProfile");
  };

  const handleInvite = () => {
    setShowInviteModal(true);
  };

  const handleCloseComposeModal = () => {
    setShowComposeModal(false);
    navigate(`/profile/${systemUsername}/projects`);
  };

  const handleCloseInviteModal = () => {
    setShowInviteModal(false);
  };

  const handleRespondToInvite = async (projectName, response) => {
    try {
      const sessionId = Cookies.get("session-id");
      const result = await respondToInvite(sessionId, projectName, response);
      if (!result.error) {
        if (response) {
          setSuccessMessage(t`You now belong to the project: ${projectName}.`);
          fetchProfile();
          navigate(`/profile/${systemUsername}/projects`);
        } else {
          setSuccessMessage(
            t`The invite for the project: ${projectName} was successfully rejected.`
          );
          fetchProfile();
          setPendingProjectToReject(null);
        }
      } else {
        setError(result.error);
      }
    } catch (err) {
      setError(t`An error occurred while responding to the invite.`);
    }
  };

  const handleRejectInvite = (projectName) => {
    setPendingProjectToReject(projectName);
    setShowConfirmModal(true);
  };

  const handleConfirmReject = () => {
    handleRespondToInvite(pendingProjectToReject, false);
    setShowConfirmModal(false);
  };

  const handleCloseConfirmModal = () => {
    setShowConfirmModal(false);
  };

  const handleProjectClick = (systemName) => {
    navigate(`/project/${systemName}`);
  };

  const handleConfirmPromotion = async () => {
    promoteUserToAdmin(systemUsername).then((response) => {
    setChangeUserModal(false);
    window.location.reload();
    });
  };

  const nonPendingProjects = profile.projects
    ? profile.projects.filter(
        (project) =>
          project.typeMember !== "PENDING_BY_APPLIANCE" &&
          project.typeMember !== "PENDING_BY_INVITATION"
      )
    : [];

  const filteredProjects = nonPendingProjects.filter(
    (project) =>
      !projectFilter || project.status.toLowerCase() === projectFilter
  );

  const sortedProjects = filteredProjects.sort((a, b) => {
    if (sortOrder === "asc") {
      return new Date(a.createdDate) - new Date(b.createdDate);
    } else {
      return new Date(b.createdDate) - new Date(a.createdDate);
    }
  });

  const pendingProjects = profile.projects
    ? profile.projects.filter(
        (project) =>
          project.typeMember === "PENDING_BY_APPLIANCE" ||
          project.typeMember === "PENDING_BY_INVITATION"
      )
    : [];

  const clearFilters = () => {
    setProjectFilter("");
    setSortOrder("asc");
  };

  const formatTypeUserInProjectCard = (type) => {
    if(type === "PENDING_BY_APPLIANCE"){
      return t`Appliance Pending`;
    }
    if(type === "PENDING_BY_INVITATION"){
      return t`Invitation`;
    }
  };

  return (
    <>
      <AppNavbar setCurrentLanguage={setCurrentLanguage} />
      <div
        className={
          !isMobile ? "bckg-color-ilm-page ilm-pageb" : "ilm-pageb-noheight"
        }
        style={{ paddingTop: "5px" }}
      >
        <Container className="mt-4" style={{ height: !isMobile && "94%" }}>
          <CSSTransition
            in={showAlert}
            timeout={300}
            classNames="alert"
            unmountOnExit
          >
            <Alert
              id="profile-alert"
              variant="success"
              className="slide-down alert-center"
            >
              {successMessage}
            </Alert>
          </CSSTransition>
          <Row
            className="justify-content-md-center"
            style={{ height: !isMobile && "100%" }}
          >
            <Col md="12" style={{ height: "100%" }}>
              <Card
                className="shadow-sm ilm-form"
                style={{ height: "100%", padding: isMobile && "35px 0px" }}
              >
                <Card.Body style={{ height: "100%" }}>
                  <Row style={{ height: "100%" }}>
                    <Col
                      md={6}
                      className="text-center align-divs-center"
                      style={{ flexDirection: "column" }}
                    >
                      <div
                        className="profile-avatar mb-3 user-info-center"
                        style={{ justifyContent: "flex-end" }}
                      >
                        <img
                          src={
                            profile.profileImage ||
                            "https://via.placeholder.com/150"
                          }
                          alt="Profile"
                          className="img-fluid rounded-circle"
                        />
                      </div>

                      <div
                        className="user-info-center"
                        style={{ justifyContent: "center", height: "unset" }}
                      >
                        <Card.Title className="form-title">
                          {profile.firstName} {profile.lastName}{" "}
                          <span
                            className={
                              profile.userType === "STANDARD_USER"
                                ? "user-type-user"
                                : profile.userType === "ADMIN" &&
                                  "user-type-admin"
                            }
                          >
                            {profile.userType === "STANDARD_USER"
                              ? t`User`
                              : profile.userType === "ADMIN" && t`Admin`}
                          </span>
                        </Card.Title>
                        <Card.Subtitle className="mb-2 text-muted">
                          {profile.username}
                        </Card.Subtitle>
                        <Card.Text>
                          <strong>
                            <Trans>Location</Trans>:
                          </strong>{" "}
                          {profile.location}
                        </Card.Text>
                        {!isPrivate && profile.bio && (
                          <Card.Text>
                            <strong>
                              <Trans>Bio</Trans>:
                            </strong>{" "}
                            {profile.bio}
                          </Card.Text>
                        )}
                        {loggedInUsername === systemUsername ? (
                          <Button
                            variant="primary"
                            onClick={handleEditProfile}
                            className="submit-button"
                            style={{ padding: "10px 20px" }}
                          >
                            <Trans>Edit Profile</Trans>
                          </Button>
                        ) : (
                          <div className="button-group">
                            <div>
                              <Button
                                variant="primary"
                                className="mr-3 submit-button"
                                style={{ padding: "10px 20px" }}
                                onClick={handleSendMessage}
                              >
                                <Trans>Send Email</Trans>
                              </Button>
                              <Button
                                variant="secondary"
                                className="invite-button"
                                style={{ padding: "10px 20px" }}
                                onClick={handleInvite}
                              >
                                <Trans>Invite to Project</Trans>
                              </Button>
                            </div>
                            {atualUserType === "ADMIN" &&
                              profile.userType === "STANDARD_USER" && (
                                <Button
                                  variant="primary"
                                  className="secondary-button"
                                  style={{ padding: "10px 20px" }}
                                  onClick={() => setChangeUserModal(true)}
                                >
                                  <Trans>Promote to Admin</Trans>
                                </Button>
                              )}
                          </div>
                        )}
                      </div>
                    </Col>
                    <Col
                      md="6"
                      style={{ height: "100%", marginTop: isMobile && "30px" }}
                    >
                      {isPrivate ? (
                        <div style={{ marginTop: "50%" }}>
                          <Alert variant="info" style={{ textAlign: "center" }}>
                            <Trans>This profile is private.</Trans>
                          </Alert>
                        </div>
                      ) : (
                        <Tab.Container
                          activeKey={activeKey}
                          onSelect={(key) =>
                            navigate(`/profile/${systemUsername}/${key}`)
                          }
                        >
                          <Nav
                            variant="pills"
                            className="flex-row pills-container"
                          >
                            <Nav.Item>
                              <Nav.Link eventKey="projects">
                                <Trans>Projects</Trans>
                              </Nav.Link>
                            </Nav.Item>
                            {loggedInUsername === systemUsername && (
                              <Nav.Item>
                                <Nav.Link eventKey="applications">
                                  <Trans>Applications</Trans>
                                </Nav.Link>
                              </Nav.Item>
                            )}
                            <Nav.Item>
                              <Nav.Link eventKey="skills">Skills</Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                              <Nav.Link eventKey="interests">
                                <Trans>Interests</Trans>
                              </Nav.Link>
                            </Nav.Item>
                          </Nav>
                          <Tab.Content id="container-cards">
                            <Tab.Pane
                              eventKey="projects"
                              style={{ height: "100%" }}
                            >
                              <Form>
                                <Row>
                                  <Col md={5}>
                                    <Form.Group className="mb-3">
                                      <Form.Label>
                                        <Trans>Status:</Trans>
                                      </Form.Label>
                                      <Form.Control
                                        as="select"
                                        value={projectFilter}
                                        onChange={(e) =>
                                          setProjectFilter(e.target.value)
                                        }
                                      >
                                        <option value="">
                                          <Trans>All</Trans>
                                        </option>
                                        <option value="planning">
                                          <Trans>Planning</Trans>
                                        </option>
                                        <option value="ready">
                                          <Trans>Ready</Trans>
                                        </option>
                                        <option value="approved">
                                          <Trans>Approved</Trans>
                                        </option>
                                        <option value="in_progress">
                                          <Trans>In Progress</Trans>
                                        </option>
                                        <option value="canceled">
                                          <Trans>Canceled</Trans>
                                        </option>
                                        <option value="finished">
                                          <Trans>Finished</Trans>
                                        </option>
                                      </Form.Control>
                                    </Form.Group>
                                  </Col>
                                  <Col md={5}>
                                    <Form.Group className="mb-3">
                                      <Form.Label>
                                        <Trans>Date:</Trans>
                                      </Form.Label>
                                      <Form.Control
                                        as="select"
                                        value={sortOrder}
                                        onChange={(e) =>
                                          setSortOrder(e.target.value)
                                        }
                                      >
                                        <option value="asc">
                                          <Trans>Ascending</Trans>
                                        </option>
                                        <option value="desc">
                                          <Trans>Descending</Trans>
                                        </option>
                                      </Form.Control>
                                    </Form.Group>
                                  </Col>
                                  <Col md={2}>
                                    <div style={{display:"flex", alignItems:"center", height:"100%"}}>
                                      <Button
                                        variant="secondary"
                                        onClick={clearFilters}
                                      >
                                        <Trans>Clear</Trans>
                                      </Button>
                                    </div>
                                  </Col>
                                </Row>
                              </Form>
                              <Card
                                className="card-container"
                                style={{
                                  maxHeight: !isMobile && isTablet && "50vh",
                                }}
                              >
                                <Card.Body
                                  className="card-body"
                                  style={{
                                    padding: isMobile && "0px",
                                    fontSize: isMobile && "14px",
                                    borderRadius: isMobile && "0px",
                                    marginRight: isMobile && "0px",
                                    marginLeft: isMobile && "0px",
                                  }}
                                >
                                  {sortedProjects.length > 0 ? (
                                    sortedProjects.map((project) => (
                                      <div
                                        key={project.name}
                                        id="application-request-card"
                                        className="mb-3 clickable"
                                        onClick={() =>
                                          handleProjectClick(project.systemName)
                                        }
                                      >
                                        <p
                                          id="column-div-project"
                                          className="mb-1"
                                        >
                                          <strong>
                                            <Trans>Project </Trans>:
                                          </strong>
                                          {project.name}
                                        </p>
                                        <p
                                          id="column-div-project"
                                          className="mb-1"
                                        >
                                          <strong>
                                            <Trans>Type </Trans>{" "}
                                            {!isMobile &&
                                              currentLanguage === "ENGLISH" &&
                                              "Member"}
                                            :
                                          </strong>{" "}
                                          {formatTypeUserInProject(
                                            project.typeMember
                                          )}
                                        </p>
                                        <p
                                          id="column-div-project"
                                          className="mb-1"
                                        >
                                          <strong>
                                            <Trans>Status</Trans>:
                                          </strong>{" "}
                                          {formatProjectState(project.status)}
                                        </p>
                                      </div>
                                    ))
                                  ) : (
                                    <p className="centered-message">
                                      <Trans>No projects available.</Trans>
                                    </p>
                                  )}
                                </Card.Body>
                              </Card>
                            </Tab.Pane>
                            {loggedInUsername === systemUsername && (
                              <Tab.Pane
                                eventKey="applications"
                                style={{ height: "100%" }}
                              >
                                <Card className="card-container">
                                  <Card.Body className="card-body">
                                    {pendingProjects.length > 0 ? (
                                      pendingProjects.map((project) => (
                                        <div
                                          key={project.name}
                                          id="application-request-card"
                                          className="mb-3 d-flex justify-content-between align-items-center"
                                        >
                                          <div>
                                            <p className="mb-1">
                                              <strong>
                                                <Trans>Project:</Trans>
                                              </strong>{" "}
                                              {project.name}
                                            </p>
                                            <p className="mb-1">
                                              <strong>
                                                <Trans>Status:</Trans>
                                              </strong>{" "}
                                              {formatProjectState(project.status)}
                                            </p>
                                            <p className="mb-1">
                                              <strong>
                                                <Trans>Type of Application:</Trans>
                                                {" "}
                                                </strong>
                                              {formatTypeUserInProjectCard(project.typeMember)}
                                            </p>
                                          </div>
                                          {project.typeMember ===
                                            "PENDING_BY_INVITATION" && (
                                            <div className="button-group-horizontal">
                                              <Button
                                                variant="danger"
                                                onClick={() =>
                                                  handleRejectInvite(
                                                    project.name
                                                  )
                                                }
                                              >
                                                <Trans>Reject</Trans>
                                              </Button>
                                              <Button
                                                variant="success"
                                                onClick={() =>
                                                  handleRespondToInvite(
                                                    project.name,
                                                    true
                                                  )
                                                }
                                              >
                                                <Trans>Accept</Trans>
                                              </Button>
                                            </div>
                                          )}
                                        </div>
                                      ))
                                    ) : (
                                      <p className="centered-message">
                                        <Trans>
                                          No applications available.
                                        </Trans>
                                      </p>
                                    )}
                                  </Card.Body>
                                </Card>
                              </Tab.Pane>
                            )}
                            <Tab.Pane
                              eventKey="skills"
                              style={{ height: "100%" }}
                            >
                              <Card className="card-container">
                                <Card.Body className="card-body">
                                  <Row style={{ height: "100%" }}>
                                    {profile.skills &&
                                    profile.skills.length > 0 ? (
                                      profile.skills.map((skill) => (
                                        <Col md="auto" key={skill.id}>
                                          <div
                                            id="skill-interest-card"
                                            className="mb-3"
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
                                        </Col>
                                      ))
                                    ) : (
                                      <Col>
                                        <p className="centered-message">
                                          <Trans>No skills available.</Trans>
                                        </p>
                                      </Col>
                                    )}
                                  </Row>
                                </Card.Body>
                              </Card>
                            </Tab.Pane>
                            <Tab.Pane
                              eventKey="interests"
                              style={{ height: "100%" }}
                            >
                              <Card className="card-container">
                                <Card.Body className="card-body">
                                  <Row>
                                    {profile.interests &&
                                    profile.interests.length > 0 ? (
                                      profile.interests.map((interest) => (
                                        <Col md="auto" key={interest.id}>
                                          <div
                                            id="skill-interest-card"
                                            className="mb-3"
                                          >
                                            {interest.name}
                                          </div>
                                        </Col>
                                      ))
                                    ) : (
                                      <Col>
                                        <p className="centered-message">
                                          <Trans>No interests available.</Trans>
                                        </p>
                                      </Col>
                                    )}
                                  </Row>
                                </Card.Body>
                              </Card>
                            </Tab.Pane>
                          </Tab.Content>
                        </Tab.Container>
                      )}
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
      <Modal show={showConfirmModal} onHide={handleCloseConfirmModal}>
        <Modal.Header closeButton>
          <Modal.Title>
            <Trans>Confirm Rejection</Trans>
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Trans>Are you sure you want to reject this invite?</Trans>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseConfirmModal}>
            <Trans>Cancel</Trans>
          </Button>
          <Button variant="danger" onClick={handleConfirmReject}>
            <Trans>Reject</Trans>
          </Button>
        </Modal.Footer>
      </Modal>
      {changeUserModal && (
        <Modal show={true}>
          <Modal.Header closeButton>
            <Modal.Title>
              <Trans>Confirm User Promotion to Admin</Trans>
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Trans>Are you sure you want to promote this user to Admin?</Trans>
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="secondary"
              onClick={() => setChangeUserModal(false)}
            >
              <Trans>Cancel</Trans>
            </Button>
            <Button variant="danger" onClick={handleConfirmPromotion}>
              <Trans>Confirm</Trans>
            </Button>
          </Modal.Footer>
        </Modal>
      )}
    </>
  );
};

export default UserProfile;
