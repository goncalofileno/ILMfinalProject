import React, { useEffect, useState } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import {
  Alert,
  Card,
  Col,
  Container,
  Row,
  Button,
  Form,
  Image,
  Modal,
} from "react-bootstrap";
import {
  getProjectInfoPage,
  approveOrRejectProject,
  joinProject,
  cancelProject,
  markReasonAsRead,
  respondToInvite,
  changeProjectState,
  leaveProject,
} from "../utilities/services";
import ProjectTabs from "../components/headers/ProjectTabs";
import ProjectMembersTable from "../components/tables/ProjectMembersTable";
import ProgressBar from "../components/bars/ProgressBar";
import AppNavbar from "../components/headers/AppNavbar";
import {
  formatLab,
  formatStatus,
  formatStatusDropDown,
  formatSkill,
} from "../utilities/converters";
import "./ProjectProfilePageInfo.css";
import Cookies from "js-cookie";
import { Trans, t } from "@lingui/macro";
import {
  formatProjectState,
  formatTypeUserInProject,
} from "../utilities/converters";
import { useMediaQuery } from "react-responsive";

const ProjectProfilePageInfo = () => {
  const { systemProjectName } = useParams();
  const navigate = useNavigate();
  const [projectInfo, setProjectInfo] = useState(null);
  const [error, setError] = useState(null);
  const [selectedState, setSelectedState] = useState("");
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [showRejectModal, setShowRejectModal] = useState(false);
  const [reason, setReason] = useState("");
  const [showReasonModal, setShowReasonModal] = useState(false);
  const [showLeaveModal, setShowLeaveModal] = useState(false);
  const [leaveReason, setLeaveReason] = useState("");
  const sessionId = Cookies.get("session-id");
  const userSystemUsername = Cookies.get("user-systemUsername");
  const [currentLanguage, setCurrentLanguage] = useState(
    Cookies.get("user-language") || "ENGLISH"
  );

  useEffect(() => {
    const fetchProjectInfo = async () => {
      const data = await getProjectInfoPage(systemProjectName);
      if (data.error) {
        setError(data.error);
      } else {
        setProjectInfo(data);
        setSelectedState(data.state);

        if (
          (data.state === "PLANNING" || data.state === "CANCELED") &&
          [
            "MEMBER",
            "MEMBER_BY_APPLIANCE",
            "MEMBER_BY_INVITATION",
            "MANAGER",
            "CREATOR",
          ].includes(data.typeOfUserSeingProject) &&
          data.reason
        ) {
          setReason(data.reason);
          setShowReasonModal(true);
        }
      }
    };
    fetchProjectInfo();
  }, [systemProjectName, currentLanguage]);

  const handleStateChange = (event) => {
    const newState = event.target.value;
    if (newState === "CANCELED") {
      setSelectedState(newState);
      setShowCancelModal(true);
    } else {
      setSelectedState(newState);
      updateProjectState(newState);
    }
  };

  const updateProjectState = async (newState) => {
    try {
      const result = await changeProjectState(
        sessionId,
        systemProjectName,
        newState,
        reason
      );
      if (result.error) {
        setError(result.error);
      } else {
        const data = await getProjectInfoPage(systemProjectName);
        setProjectInfo(data);
      }
    } catch (error) {
      console.error("Error updating project state:", error);
      setError(t`An error occurred while updating the project state.`);
    }
  };

  const handleCancelProject = async () => {
    try {
      const result = await cancelProject(sessionId, systemProjectName, reason);
      if (result.error) {
        setError(result.error);
      } else {
        setShowCancelModal(false);
        setReason("");
        const data = await getProjectInfoPage(systemProjectName);
        setProjectInfo(data);
      }
    } catch (error) {
      console.error("Error canceling project:", error);
      setError(t`An error occurred while canceling the project.`);
    }
  };

  const handleRejectProject = async () => {
    try {
      const result = await approveOrRejectProject(
        sessionId,
        systemProjectName,
        false,
        reason
      );
      if (result.error) {
        setError(result.error);
      } else {
        setShowRejectModal(false);
        setReason("");
        const data = await getProjectInfoPage(systemProjectName);
        setProjectInfo(data);
      }
    } catch (error) {
      console.error("Error rejecting project:", error);
      setError(t`An error occurred while rejecting the project.`);
    }
  };

  const handleApproveProject = async () => {
    try {
      const result = await approveOrRejectProject(
        sessionId,
        systemProjectName,
        true,
        ""
      );
      if (result.error) {
        setError(result.error);
      } else {
        const data = await getProjectInfoPage(systemProjectName);
        setProjectInfo(data);
      }
    } catch (error) {
      console.error("Error approving project:", error);
      setError(t`An error occurred while approving the project.`);
    }
  };

  const handleMarkAsRead = async () => {
    try {
      const result = await markReasonAsRead(sessionId, systemProjectName);
      if (result.error) {
        setError(result.error);
      } else {
        setShowReasonModal(false);
        const data = await getProjectInfoPage(systemProjectName);
        setProjectInfo(data);
      }
    } catch (error) {
      console.error("Error marking reason as read:", error);
      setError(t`An error occurred while marking the reason as read.`);
    }
  };

  const handleRejectInvite = async (projectTitle) => {
    try {
      const result = await respondToInvite(sessionId, projectTitle, false);
      if (result.error) {
        setError(result.error);
      } else {
        const data = await getProjectInfoPage(systemProjectName);
        setProjectInfo(data);
      }
    } catch (error) {
      console.error("Error rejecting invite:", error);
      setError(t`An error occurred while rejecting the invite.`);
    }
  };

  const handleRespondToInvite = async (projectTitle, accept) => {
    try {
      const result = await respondToInvite(sessionId, projectTitle, accept);
      if (result.error) {
        setError(result.error);
      } else {
        const data = await getProjectInfoPage(systemProjectName);
        setProjectInfo(data);
      }
    } catch (error) {
      console.error("Error responding to invite:", error);
      setError(t`An error occurred while responding to the invite.`);
    }
  };

  const handleJoinProject = async () => {
    try {
      const result = await joinProject(sessionId, systemProjectName);
      if (result.error) {
        setError(result.error);
      } else {
        const data = await getProjectInfoPage(systemProjectName);
        setProjectInfo(data);
      }
    } catch (error) {
      console.error("Error joining project:", error);
      setError(t`An error occurred while joining the project.`);
    }
  };

  const handleLeaveProject = async () => {
    try {
      const result = await leaveProject(
        sessionId,
        systemProjectName,
        leaveReason
      );
      if (result.error) {
        setError(result.error);
        console.error("Error leaving project:", result.error);
      } else {
        setShowLeaveModal(false);
        setLeaveReason("");
        const data = await getProjectInfoPage(systemProjectName);
        setProjectInfo(data);
      }
    } catch (error) {
      console.error("Error leaving project:", error);
      setError(t`An error occurred while leaving the project.`);
    }
  };

  const showLeaveProjectModal = () => setShowLeaveModal(true);

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  if (!projectInfo) {
    return <div>Loading...</div>;
  }

  const hasVacancies = projectInfo.members.length < projectInfo.maxMembers;

  const isCreatorOrManager =
    projectInfo.typeOfUserSeingProject === "CREATOR" ||
    projectInfo.typeOfUserSeingProject === "MANAGER";

  const isAdmin = projectInfo.typeOfUserSeingProject === "ADMIN";

  const renderAdminButtons = () => {
    if (projectInfo.state !== "CANCELED") {
      return (
        <div>
          {isAdmin && projectInfo.state === "READY" && (
            <div style={{ marginBottom: "10px" }}>
              <span>
                <strong>
                  <Trans>This project is ready for approval</Trans>:
                </strong>
              </span>
            </div>
          )}
          <div className="admin-buttons">
            {isAdmin && projectInfo.state === "READY" && (
              <Row style={{ marginBottom: "10px" }}>
                <Col md="6">
                  <Button
                    variant="success"
                    className="mr-2"
                    onClick={handleApproveProject}
                  >
                    <Trans>Approve Project</Trans>
                  </Button>
                </Col>
                <Col md="6">
                  <Button
                    variant="warning"
                    className="mr-2"
                    onClick={() => setShowRejectModal(true)}
                  >
                    <Trans>Reject Project</Trans>
                  </Button>
                </Col>
              </Row>
            )}
            {(isAdmin || isCreatorOrManager) && (
              <>
                <Row>
                  <Col md="6">
                    <Button
                      style={{
                        backgroundColor: "rgb(30, 40, 82)",
                        borderColor: "rgb(30, 40, 82)",
                        marginRight: "10px",
                        marginBottom: "10px",
                      }}
                      onClick={() =>
                        navigate("/editProject/" + systemProjectName)
                      }
                      disabled={["CANCELED", "READY"].includes(
                        projectInfo.state
                      )}
                    >
                      <Trans>Edit Project</Trans>
                    </Button>
                  </Col>
                  <Col md="6">
                    <Button
                      variant="danger"
                      onClick={() => setShowCancelModal(true)}
                    >
                      <Trans>Cancel Project</Trans>
                    </Button>
                  </Col>
                </Row>
                <Row>
                  {["CANCELED", "READY"].includes(projectInfo.state) && (
                    <Alert variant="danger" className="mt-3">
                      <Trans>
                        The project is {projectInfo.state.toLowerCase()} and
                        cannot be edited.
                      </Trans>
                    </Alert>
                  )}
                </Row>
              </>
            )}
          </div>
        </div>
      );
    }
    return null;
  };

  const renderUserStatusCard = () => {
    const { typeOfUserSeingProject } = projectInfo;
    if (typeOfUserSeingProject === "PENDING_BY_INVITATION") {
      return (
        <div
          id="application-request-card"
          className="mb-3 d-flex justify-content-between align-items-center"
        >
          <div>
            <p className="mb-1">
              <strong>
                <Trans>You have been invited to join this project.</Trans>
              </strong>
            </p>
          </div>
          <div className="button-group-horizontal">
            <Button
              variant="danger"
              onClick={() => handleRejectInvite(projectInfo.title)}
            >
              <Trans>Reject</Trans>
            </Button>
            <Button
              variant="success"
              onClick={() => handleRespondToInvite(projectInfo.title, true)}
            >
              <Trans>Accept</Trans>
            </Button>
          </div>
        </div>
      );
    } else if (typeOfUserSeingProject === "PENDING_BY_APPLIANCE") {
      return (
        <div
          id="application-request-card"
          className="mb-3 d-flex justify-content-between align-items-center"
        >
          <div>
            <p className="mb-1">
              <div style={{ textAlign: "center" }}>
                <strong>
                  <Trans>
                    You have applied to this project. You will be notified when
                    one of the managers responds to your application.
                  </Trans>
                </strong>
              </div>
            </p>
          </div>
        </div>
      );
    } else if (
      typeOfUserSeingProject === "MEMBER" ||
      typeOfUserSeingProject === "MEMBER_BY_APPLIANCE" ||
      typeOfUserSeingProject === "MEMBER_BY_INVITATION" ||
      typeOfUserSeingProject === "MANAGER" ||
      typeOfUserSeingProject === "CREATOR"
    ) {
      return (
        <div
          id="application-request-card"
          className="mb-3 d-flex justify-content-between align-items-center"
        >
          <div>
            <p className="mb-1">
              <div>
                <strong>
                  <Trans>You are a member of this project.</Trans>
                </strong>
              </div>
            </p>
          </div>
        </div>
      );
    }

    return null;
  };

  return (
    <>
      <AppNavbar setCurrentLanguage={setCurrentLanguage} />
      <div className="ilm-pageb-noheight">
        <ProjectTabs
          typeOfUserSeingProject={projectInfo.typeOfUserSeingProject}
          projectName={projectInfo.title}
        />
        <Container className="mt-4" style={{ height: "100%" }}>
          <Row className="justify-content-md-center" style={{ height: "100%" }}>
            <Col md="12" style={{ height: "100%" }}>
              <Card
                className="shadow-sm"
                style={{ height: "100%", paddingBottom: "15px" }}
              >
                <div
                  className="project-cover-photo"
                  style={{
                    backgroundImage:
                      projectInfo.photo == null
                        ? "url(https://cdn.pixabay.com/photo/2016/03/29/08/48/project-1287781_1280.jpg)"
                        : `url(${projectInfo.photo})`,
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    height: "200px", // Adjust as necessary
                    marginBottom: "20px",
                  }}
                ></div>
                <Row style={{ paddingRight: "10px", paddingLeft: "10px" }}>
                  <Col md="12">
                    <Card.Body>
                      <Card.Text>
                        <strong>
                          <Trans>Progress</Trans>:
                        </strong>
                      </Card.Text>
                      <ProgressBar
                        percentage={projectInfo.progress}
                        status={projectInfo.state}
                      />
                    </Card.Body>
                  </Col>
                </Row>
                <Row
                  style={{
                    height: "100%",
                    padding: "15px",
                  }}
                >
                  <Col md="6">
                    <Card.Body>
                      <Row style={{ marginBottom: "40px" }}>
                        <Col md="6">
                          <Card.Title style={{ fontSize: "2em" }}>
                            {projectInfo.title}
                          </Card.Title>
                          <Card.Subtitle
                            className="mb-2 text-muted"
                            style={{ fontSize: "1.5em" }}
                          >
                            {formatStatus(projectInfo.state)}
                          </Card.Subtitle>
                        </Col>
                        <Col md="6" className="text-right">
                          {renderAdminButtons()}
                        </Col>
                      </Row>
                      <Card.Text>
                        <strong>
                          <Trans>Description</Trans>:
                        </strong>{" "}
                        {projectInfo.description}
                      </Card.Text>
                      <Card.Text>
                        <strong>
                          <Trans>Start Date</Trans>:
                        </strong>{" "}
                        {new Date(projectInfo.startDate).toLocaleDateString()}
                      </Card.Text>
                      <Card.Text>
                        <strong>
                          <Trans>End Date</Trans>:
                        </strong>{" "}
                        {new Date(projectInfo.endDate).toLocaleDateString()}
                      </Card.Text>
                      <Card.Text>
                        <strong>
                          <Trans>Lab</Trans>:
                        </strong>{" "}
                        {formatLab(projectInfo.lab)}
                      </Card.Text>
                      <Card.Text>
                        <strong>
                          <Trans>Creator</Trans>:
                        </strong>{" "}
                        <Link
                          to={`/profile/${projectInfo.creator.systemUsername}`}
                        >
                          <Image
                            src={projectInfo.creator.profilePicture}
                            roundedCircle
                            className="creator-profile-picture"
                          />
                          {projectInfo.creator.systemUsername ===
                          userSystemUsername
                            ? t`You`
                            : projectInfo.creator.name}
                        </Link>
                      </Card.Text>
                      {(isCreatorOrManager || isAdmin) && (
                        <>
                          {projectInfo.statesToChange.length > 0 && (
                            <div className="states-to-change">
                              <Card.Text>
                                <strong><Trans>States to Change</Trans>:</strong>
                              </Card.Text>
                              <Form.Select
                                value={selectedState}
                                onChange={handleStateChange}
                              >
                                {projectInfo.statesToChange.map(
                                  (state, index) => (
                                    <option key={index} value={state}>
                                      {formatProjectState(state)}
                                    </option>
                                  )
                                )}
                              </Form.Select>
                            </div>
                          )}
                        </>
                      )}
                    </Card.Body>
                  </Col>
                  <Col md="6" style={{ height: "100%" }}>
                    <Row>
                      <Col md="12">{renderUserStatusCard()}</Col>
                    </Row>
                    <Row style={{ height: "100%" }}>
                      <Card.Body
                        style={{ height: "100%" }}
                        className="card-body-text"
                      >
                        <div className="label-applyButton">
                          <div>
                            <strong>
                              <Trans>Members</Trans>
                            </strong>{" "}
                            {projectInfo.members.length}/
                            {projectInfo.maxMembers}:
                          </div>
                          {hasVacancies &&
                            projectInfo.state !== "CANCELED" &&
                            ![
                              "PENDING_BY_INVITATION",
                              "PENDING_BY_APPLIANCE",
                              "MEMBER",
                              "MEMBER_BY_APPLIANCE",
                              "MEMBER_BY_INVITATION",
                              "CREATOR",
                              "MANAGER",
                            ].includes(projectInfo.typeOfUserSeingProject) && (
                              <Button
                                variant="primary"
                                onClick={handleJoinProject}
                                style={{
                                  backgroundColor: "#f39c12",
                                  borderColor: "#f39c12",
                                }}
                              >
                                <Trans>Join Project</Trans>
                              </Button>
                            )}
                          {[
                            "MEMBER",
                            "MEMBER_BY_APPLIANCE",
                            "MEMBER_BY_INVITATION",
                            "MANAGER",
                          ].includes(projectInfo.typeOfUserSeingProject) && (
                            <Button
                              variant="danger"
                              onClick={showLeaveProjectModal}
                              style={{
                                backgroundColor: "#dc3545",
                                borderColor: "#dc3545",
                              }}
                            >
                              <Trans>Leave Project</Trans>
                            </Button>
                          )}
                        </div>
                        <ProjectMembersTable members={projectInfo.members} />
                      </Card.Body>
                    </Row>
                  </Col>
                </Row>
                <Row
                  style={{
                    paddingLeft: "30px",
                    paddingRight: "20px",
                  }}
                >
                  <Col>
                    <Card.Text>
                      <strong>Skills:</strong>
                      <Row style={{ marginTop: "10px" }}>
                        {projectInfo.skills.length > 0 ? (
                          projectInfo.skills.map((skill) => (
                            <Col md="auto" key={skill.id}>
                              <div className="skill-interest-card mb-3">
                                <p className="mb-1">
                                  <strong>{formatSkill(skill.name)}</strong>
                                </p>
                                <p
                                  className="text-muted mb-1"
                                  style={{ fontSize: "0.85em" }}
                                >
                                  {formatSkill(skill.type)}
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
                    </Card.Text>
                  </Col>{" "}
                  <Col>
                    <Card.Text>
                      <strong>Keywords:</strong>
                      <Row style={{ marginTop: "10px" }}>
                        {projectInfo.keywords.length > 0 ? (
                          projectInfo.keywords.map((keyword, index) => (
                            <Col md="auto" key={index}>
                              <div className="skill-interest-card mb-3">
                                {keyword}
                              </div>
                            </Col>
                          ))
                        ) : (
                          <Col>
                            <p className="centered-message">
                              <Trans>No keywords available.</Trans>
                            </p>
                          </Col>
                        )}
                      </Row>
                    </Card.Text>
                  </Col>
                </Row>
              </Card>
            </Col>
          </Row>
        </Container>
      </div>

      {/* Cancel Project Modal */}
      <Modal show={showCancelModal} onHide={() => setShowCancelModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>
            <Trans>Cancel Project</Trans>
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="cancelReason">
              <Form.Label>
                <Trans>Reason</Trans>
              </Form.Label>
              <Form.Control
                as="textarea"
                rows={5}
                value={reason}
                onChange={(e) => setReason(e.target.value)}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowCancelModal(false)}>
            <Trans>Cancel</Trans>
          </Button>
          <Button variant="danger" onClick={handleCancelProject}>
            <Trans>Confirm Cancel</Trans>
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Reject Project Modal */}
      <Modal show={showRejectModal} onHide={() => setShowRejectModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>
            <Trans>Reject Project</Trans>
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="rejectReason">
              <Form.Label>
                <Trans>Reason</Trans>
              </Form.Label>
              <Form.Control
                as="textarea"
                rows={5}
                value={reason}
                onChange={(e) => setReason(e.target.value)}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowRejectModal(false)}>
            <Trans>Cancel</Trans>
          </Button>
          <Button variant="warning" onClick={handleRejectProject}>
            <Trans>Confirm Reject</Trans>
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Leave Project Modal */}
      <Modal show={showLeaveModal} onHide={() => setShowLeaveModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>
            <Trans>Reason to leave the Project</Trans>
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="leaveReason">
              <Form.Control
                as="textarea"
                rows={5}
                value={leaveReason}
                onChange={(e) => setLeaveReason(e.target.value)}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowLeaveModal(false)}>
            <Trans>Cancel</Trans>
          </Button>
          <Button variant="danger" onClick={handleLeaveProject}>
            <Trans>Confirm Leave</Trans>
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Reason Modal */}
      <Modal show={showReasonModal} onHide={() => setShowReasonModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>
            {projectInfo.state === "PLANNING"
              ? t`Reason of Reject`
              : t`Reason of Cancellation`}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>{reason}</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowReasonModal(false)}>
            <Trans>Close</Trans>
          </Button>
          {["MANAGER", "CREATOR"].includes(
            projectInfo.typeOfUserSeingProject
          ) && (
            <Button
              variant="primary"
              onClick={handleMarkAsRead}
              style={{ backgroundColor: "#f39c12", borderColor: "#f39c12" }}
            >
              <Trans>Mark as Read</Trans>
            </Button>
          )}
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ProjectProfilePageInfo;
