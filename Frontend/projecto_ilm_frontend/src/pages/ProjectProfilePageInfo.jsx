import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
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
import { getProjectInfoPage } from "../utilities/services";
import ProjectTabs from "../components/headers/ProjectTabs";
import ProjectMembersTable from "../components/tables/ProjectMembersTable";
import ProgressBar from "../components/bars/ProgressBar";
import AppNavbar from "../components/headers/AppNavbar";
import "./ProjectProfilePageInfo.css";

const ProjectProfilePageInfo = () => {
  const { systemProjectName } = useParams();
  const [projectInfo, setProjectInfo] = useState(null);
  const [error, setError] = useState(null);
  const [selectedState, setSelectedState] = useState("");
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [showRejectModal, setShowRejectModal] = useState(false);
  const [reason, setReason] = useState("");
  const [showReasonModal, setShowReasonModal] = useState(false);

  useEffect(() => {
    const fetchProjectInfo = async () => {
      const data = await getProjectInfoPage(systemProjectName);
      if (data.error) {
        setError(data.error);
      } else {
        setProjectInfo(data);
        setSelectedState(data.state);
        console.log(data);

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
  }, [systemProjectName]);

  const handleStateChange = (event) => {
    setSelectedState(event.target.value);
  };

  const handleCancelProject = () => {
    // Logic to handle project cancellation with reason
    console.log("Project Cancelled with reason:", reason);
    setShowCancelModal(false);
    setReason("");
  };

  const handleRejectProject = () => {
    // Logic to handle project rejection with reason
    console.log("Project Rejected with reason:", reason);
    setShowRejectModal(false);
    setReason("");
  };

  const handleMarkAsRead = () => {
    // Logic to mark the reason as read
    console.log("Reason marked as read");
    setShowReasonModal(false);
  };

  const handleRejectInvite = (projectTitle) => {
    // Logic to handle rejection of invitation
  };

  const handleRespondToInvite = (projectTitle, accept) => {
    // Logic to handle response to invitation
  };

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
    if (isAdmin) {
      if (projectInfo.state === "READY") {
        return (
          <div>
            <div style={{ marginBottom: "10px" }}>
              <span>
                <strong>This project is ready for approval:</strong>
              </span>
            </div>
            <div className="admin-buttons">
              <Button variant="success" className="mr-2">
                Approve Project
              </Button>
              <Button
                variant="warning"
                className="mr-2"
                onClick={() => setShowRejectModal(true)}
              >
                Reject Project
              </Button>
              <Button variant="danger" onClick={() => setShowCancelModal(true)}>
                Cancel Project
              </Button>
            </div>
          </div>
        );
      } else {
        return (
          <div className="admin-buttons">
            <Button variant="danger" onClick={() => setShowCancelModal(true)}>
              Cancel Project
            </Button>
          </div>
        );
      }
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
              <strong>You have been invited to join this project.</strong>
            </p>
          </div>
          <div className="button-group-horizontal">
            <Button
              variant="danger"
              onClick={() => handleRejectInvite(projectInfo.title)}
            >
              Reject
            </Button>
            <Button
              variant="success"
              onClick={() => handleRespondToInvite(projectInfo.title, true)}
            >
              Accept
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
              <div style={{textAlign:"center"}}>
                <strong>
                  You have applied to this project. You will be notified when
                  one of the managers responds to your application.
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
              <strong>You are a member of this project.</strong>
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
      <AppNavbar />
      <div className="bckg-color-ilm-page ilm-pageb">
        <Container className="mt-4">
          <ProjectTabs
            typeOfUserSeingProject={projectInfo.typeOfUserSeingProject}
          />
          <Row className="justify-content-md-center">
            <Col md="12">
              <Card className="shadow-sm">
                <div
                  className="project-cover-photo"
                  style={{
                    backgroundImage: `url(${projectInfo.photo})`,
                    backgroundSize: "cover",
                    backgroundPosition: "center",
                    height: "200px", // Adjust as necessary
                    marginBottom: "20px",
                  }}
                ></div>
                <Row>
                  <Col md="6">
                    <Card.Body>
                      <Row style={{ marginBottom: "40px" }}>
                        <Col md="6">
                          <Card.Title>{projectInfo.title}</Card.Title>
                          <Card.Subtitle className="mb-2 text-muted">
                            {projectInfo.state}
                          </Card.Subtitle>
                        </Col>
                        <Col md="6" className="text-right">
                          {renderAdminButtons()}
                        </Col>
                      </Row>
                      <Card.Text>
                        <strong>Description:</strong> {projectInfo.description}
                      </Card.Text>
                      <Card.Text>
                        <strong>Start Date:</strong>{" "}
                        {new Date(projectInfo.startDate).toLocaleDateString()}
                      </Card.Text>
                      <Card.Text>
                        <strong>End Date:</strong>{" "}
                        {new Date(projectInfo.endDate).toLocaleDateString()}
                      </Card.Text>
                      <Card.Text>
                        <strong>Lab:</strong> {projectInfo.lab}
                      </Card.Text>
                      <Card.Text>
                        <strong>Creator:</strong>{" "}
                        <Link
                          to={`/profile/${projectInfo.creator.systemUsername}`}
                        >
                          <Image
                            src={projectInfo.creator.profilePicture}
                            roundedCircle
                            className="creator-profile-picture"
                          />
                          {projectInfo.creator.name}
                        </Link>
                      </Card.Text>
                      {isCreatorOrManager && (
                        <>
                          <Card.Text>
                            <strong>States to Change:</strong>
                          </Card.Text>
                          <Form.Select
                            value={selectedState}
                            onChange={handleStateChange}
                          >
                            {projectInfo.statesToChange.map((state, index) => (
                              <option key={index} value={state}>
                                {state}
                              </option>
                            ))}
                          </Form.Select>
                        </>
                      )}
                      <Card.Text>
                        <strong>Skills:</strong>
                        <Row>
                          {projectInfo.skills.length > 0 ? (
                            projectInfo.skills.map((skill) => (
                              <Col md="auto" key={skill.id}>
                                <div className="skill-interest-card mb-3">
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
                                No skills available.
                              </p>
                            </Col>
                          )}
                        </Row>
                      </Card.Text>
                      <Card.Text>
                        <strong>Keywords:</strong>
                        <Row>
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
                                No keywords available.
                              </p>
                            </Col>
                          )}
                        </Row>
                      </Card.Text>
                    </Card.Body>
                  </Col>
                  <Col md="6">
                    <Row>
                      <Col md="12">{renderUserStatusCard()}</Col>
                    </Row>
                    <Row>
                      <Card.Body>
                        <div className="label-applyButton">
                          <div>
                            <strong>Members</strong>{" "}
                            {projectInfo.members.length}/
                            {projectInfo.maxMembers}:
                          </div>
                          {hasVacancies &&
                            ![
                              "PENDING_BY_INVITATION",
                              "PENDING_BY_APPLIANCE",
                              "MEMBER",
                              "MEMBER_BY_APPLIANCE",
                              "MEMBER_BY_INVITATION",
                            ].includes(projectInfo.typeOfUserSeingProject) && (
                              <Button variant="primary">Join Project</Button>
                            )}
                        </div>
                        <ProjectMembersTable members={projectInfo.members} />
                      </Card.Body>
                    </Row>
                  </Col>
                </Row>
                <Row>
                  <Col md="12">
                    <Card.Body>
                      <Card.Text>
                        <strong>Progress:</strong>
                      </Card.Text>
                      <ProgressBar
                        percentage={projectInfo.progress}
                        status={projectInfo.state}
                      />
                    </Card.Body>
                  </Col>
                </Row>
                <Row>
                  <Col md="12">
                    <Card.Body>
                      <Card.Text>
                        <strong>Type of User Seeing Project:</strong>{" "}
                        {projectInfo.typeOfUserSeingProject}
                      </Card.Text>
                    </Card.Body>
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
          <Modal.Title>Cancel Project</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="cancelReason">
              <Form.Label>Reason</Form.Label>
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
            Cancel
          </Button>
          <Button variant="danger" onClick={handleCancelProject}>
            Confirm Cancel
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Reject Project Modal */}
      <Modal show={showRejectModal} onHide={() => setShowRejectModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Reject Project</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="rejectReason">
              <Form.Label>Reason</Form.Label>
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
            Cancel
          </Button>
          <Button variant="warning" onClick={handleRejectProject}>
            Confirm Reject
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Reason Modal */}
      <Modal show={showReasonModal} onHide={() => setShowReasonModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>
            {projectInfo.state === "PLANNING"
              ? "Reason of Reject"
              : "Reason of Cancellation"}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>{reason}</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowReasonModal(false)}>
            Close
          </Button>
          {["MANAGER", "CREATOR"].includes(
            projectInfo.typeOfUserSeingProject
          ) && (
            <Button variant="primary" onClick={handleMarkAsRead}>
              Mark as Read
            </Button>
          )}
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ProjectProfilePageInfo;
