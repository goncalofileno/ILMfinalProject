import React, { useEffect, useState } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Table,
  Button,
  Form,
  Alert,
  Modal,
} from "react-bootstrap";
import { useParams } from "react-router-dom";
import AppNavbar from "../components/headers/AppNavbar";
import ProjectTabs from "../components/headers/ProjectTabs";
import {
  getProjectMembersPage,
  removeUserFromProject,
  changeUserInProjectType,
  respondToApplication,
  getUserProjectCreation,
  getLabsWithSessionId,
  inviteUserToProject,
  removeInvitation,
} from "../utilities/services";
import Cookies from "js-cookie";
import TablePagination from "../components/paginations/TablePagination";
import "./ProjectMembersPage.css";
import {
  formatLab,
  formatStatus,
  formatStatusDropDown,
  formatSkill,
  formatResourceType,
  formatTypeUserInProject,
} from "../utilities/converters";

const ProjectMembersPage = () => {
  const { systemProjectName } = useParams();
  const [projectData, setProjectData] = useState(null);
  const [error, setError] = useState(null);
  const [selectedMemberType, setSelectedMemberType] = useState({});
  const [showRemoveModal, setShowRemoveModal] = useState(false);
  const [userToRemove, setUserToRemove] = useState(null);
  const [removeReason, setRemoveReason] = useState("");
  const [showRejectModal, setShowRejectModal] = useState(false);
  const [userToReject, setUserToReject] = useState(null);
  const [rejectReason, setRejectReason] = useState("");
  const [allUsers, setAllUsers] = useState([]);
  const [labs, setLabs] = useState([]);
  const [selectedLab, setSelectedLab] = useState("");
  const [searchKeyword, setSearchKeyword] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [maxPageNumber, setMaxPageNumber] = useState(1);
  const [navigateTableTrigger, setNavigateTableTrigger] = useState(false);

  const sessionId = Cookies.get("session-id");
  const userSystemUsername = Cookies.get("user-systemUsername");

  useEffect(() => {
    const fetchProjectData = async () => {
      const result = await getProjectMembersPage(sessionId, systemProjectName);
      if (result.error) {
        setError(result.error);
      } else {
        setProjectData(result);
      }
    };

    const fetchLabs = async () => {
      const response = await getLabsWithSessionId();
      if (!response.ok) {
        console.error("Error fetching labs:", response.statusText);
        return;
      }
      const labsData = await response.json();
      setLabs(labsData);
    };

    fetchProjectData();
    fetchLabs();
  }, [systemProjectName, sessionId]);

  const handleRoleChange = async (member, newType) => {
    setSelectedMemberType((prev) => ({
      ...prev,
      [member.systemUsername]: newType,
    }));

    try {
      const result = await changeUserInProjectType(
        sessionId,
        systemProjectName,
        member.id,
        newType
      );
      if (result.error) {
        setError(result.error);
      } else {
        const updatedData = await getProjectMembersPage(
          sessionId,
          systemProjectName
        );
        setProjectData(updatedData);
      }
    } catch (error) {
      setError("An error occurred while changing user role.");
    }
  };

  const handleRemoveMember = (member) => {
    setUserToRemove(member);
    setShowRemoveModal(true);
  };

  const handleConfirmRemove = async () => {
    try {
      const result = await removeUserFromProject(
        sessionId,
        systemProjectName,
        userToRemove.id,
        removeReason
      );
      if (result.error) {
        setError(result.error);
      } else {
        const updatedData = await getProjectMembersPage(
          sessionId,
          systemProjectName
        );
        setProjectData(updatedData);
        setShowRemoveModal(false);
        setRemoveReason("");
        setUserToRemove(null);
      }
    } catch (error) {
      setError("An error occurred while removing the user.");
    }
  };

  const handleAcceptRequest = async (member) => {
    try {
      const result = await respondToApplication(
        sessionId,
        systemProjectName,
        member.id,
        true,
        ""
      );
      if (result.error) {
        setError(result.error);
      } else {
        const updatedData = await getProjectMembersPage(
          sessionId,
          systemProjectName
        );
        setProjectData(updatedData);
      }
    } catch (error) {
      setError("An error occurred while accepting the application.");
    }
  };

  const handleRejectRequest = (member) => {
    setUserToReject(member);
    setShowRejectModal(true);
  };

  const handleConfirmReject = async () => {
    try {
      const result = await respondToApplication(
        sessionId,
        systemProjectName,
        userToReject.id,
        false,
        rejectReason
      );
      if (result.error) {
        setError(result.error);
      } else {
        const updatedData = await getProjectMembersPage(
          sessionId,
          systemProjectName
        );
        setProjectData(updatedData);
        setShowRejectModal(false);
        setRejectReason("");
        setUserToReject(null);
      }
    } catch (error) {
      setError("An error occurred while rejecting the application.");
    }
  };

  const fetchAllUsers = async (page = 1) => {
    try {
      const rejectedUsers = [
        ...projectData.projectMembers.map((member) => member.id),
        ...projectData.projectMembers.map((member) => member.id),
      ];
      console.log("Fetching all users with rejectedUsers:", rejectedUsers);

      const result = await getUserProjectCreation(
        systemProjectName,
        rejectedUsers,
        page,
        selectedLab,
        searchKeyword
      );

      if (!result.ok) {
        throw new Error(`HTTP error! status: ${result.status}`);
      }

      const data = await result.json();
      console.log("Fetched users result:", data);

      setAllUsers(data.userProjectCreationDtos || []);
      setMaxPageNumber(data.maxPageNumber);
    } catch (fetchError) {
      setError("An error occurred while fetching users.");
      console.error("Fetching users error:", fetchError);
    }
  };

  const handleInvite = async (systemUsername) => {
    try {
      const result = await inviteUserToProject(
        sessionId,
        projectData.projectName,
        systemUsername
      );
      if (!result.ok) {
        throw new Error(`HTTP error! status: ${result.status}`);
      }
      const updatedData = await getProjectMembersPage(
        sessionId,
        systemProjectName
      );
      setProjectData(updatedData);
    } catch (error) {
      console.error("Error inviting user to project:", error);
      alert("An error occurred while inviting the user.");
    }
  };

  const handleRemoveInvitation = async (userId) => {
    try {
      const result = await removeInvitation(sessionId, systemProjectName, userId);
      // if (!result.ok) {
      //   throw new Error(`HTTP error! status: ${result.status}`);
      // }
      const updatedData = await getProjectMembersPage(
        sessionId,
        systemProjectName
      );
      setProjectData(updatedData);
    } catch (error) {
      console.error("Error removing invitation:", error);
    }
  };

  useEffect(() => {
    if (projectData) {
      fetchAllUsers(currentPage);
    }
  }, [projectData, currentPage, selectedLab, searchKeyword, navigateTableTrigger]);

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!projectData) {
    return <div>Loading...</div>;
  }

  const typeLabels = {
    CREATOR: "Creator",
    MANAGER: "Manager",
    MEMBER: "Member",
    MEMBER_BY_INVITATION: "Member by Invitation",
    MEMBER_BY_APPLIANCE: "Member by Application",
    PENDING_BY_APPLIANCE: "Application",
    PENDING_BY_INVITATION: "Pending Invitation",
  };

  const members = projectData.projectMembers.filter((member) =>
    [
      "CREATOR",
      "MANAGER",
      "MEMBER",
      "MEMBER_BY_INVITATION",
      "MEMBER_BY_APPLIANCE",
    ].includes(member.type)
  );

  const requests = projectData.projectMembers.filter((member) =>
    ["PENDING_BY_APPLIANCE", "PENDING_BY_INVITATION"].includes(member.type)
  );

  return (
    <>
      <AppNavbar />
      <div className="bckg-color-ilm-page ilm-pageb">
        <ProjectTabs typeOfUserSeingProject={projectData.userSeingProject} />
        <Container>
          <Row>
            <h2>{projectData.projectName}</h2>
          </Row>
          <Row>
            <Col>
              {projectData.projectState === "CANCELED" && (
                <Alert variant="danger">
                  The project is canceled and no changes can be made.
                </Alert>
              )}
            </Col>
          </Row>
          <Row>
            <Col>
              <div className="d-flex justify-content-between">
                <Card style={{ width: "48%" }}>
                  <Card.Header>Members</Card.Header>
                  <Card.Body>
                    <Table striped bordered hover className="custom-table">
                      <thead>
                        <tr>
                          <th>Photo</th>
                          <th>Name</th>
                          <th>Type</th>
                          <th>Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        {members.map((member) => (
                          <tr key={member.id}>
                            <td className="align-middle">
                              <img
                                src={member.profilePicture}
                                alt={member.name}
                                width={50}
                                height={50}
                                className="rounded-circle"
                              />
                            </td>
                            <td className="align-middle">
                              {member.systemUsername === userSystemUsername
                                ? "You"
                                : member.name}
                            </td>
                            <td className="align-middle">
                              {typeLabels[member.type]}
                              {member.type !== "CREATOR" && (
                                <Form.Select
                                  value={
                                    selectedMemberType[member.systemUsername] ||
                                    member.type
                                  }
                                  onChange={(e) =>
                                    handleRoleChange(member, e.target.value)
                                  }
                                  disabled={
                                    projectData.projectState === "CANCELED"
                                  }
                                  className="mt-2"
                                >
                                  <option value="MANAGER">Manager</option>
                                  <option value="MEMBER">Member</option>
                                </Form.Select>
                              )}
                            </td>
                            <td className="align-middle">
                              {member.type !== "CREATOR" && (
                                <Button
                                  variant="danger"
                                  onClick={() => handleRemoveMember(member)}
                                  disabled={
                                    projectData.projectState === "CANCELED"
                                  }
                                >
                                  Remove
                                </Button>
                              )}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </Table>
                  </Card.Body>
                </Card>

                <Card style={{ width: "48%" }}>
                  <Card.Header>Requests and Applications</Card.Header>
                  <Card.Body>
                    {requests.length === 0 ? (
                      <p>No requests or applications to show.</p>
                    ) : (
                      <Table striped bordered hover className="custom-table">
                        <thead>
                          <tr>
                            <th>Photo</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Actions</th>
                          </tr>
                        </thead>
                        <tbody>
                          {requests.map((member) => (
                            <tr key={member.id}>
                              <td className="align-middle">
                                <img
                                  src={member.profilePicture}
                                  alt={member.name}
                                  width={50}
                                  height={50}
                                  className="rounded-circle"
                                />
                              </td>
                              <td className="align-middle">
                                {member.systemUsername === userSystemUsername
                                  ? "You"
                                  : member.name}
                              </td>
                              <td className="align-middle">
                                {typeLabels[member.type]}
                              </td>
                              <td className="align-middle">
                                {member.type === "PENDING_BY_APPLIANCE" && (
                                  <>
                                    <Button
                                      variant="success"
                                      onClick={() =>
                                        handleAcceptRequest(member)
                                      }
                                      disabled={
                                        projectData.projectState === "CANCELED"
                                      }
                                      className="me-2"
                                    >
                                      Accept
                                    </Button>
                                    <Button
                                      variant="danger"
                                      onClick={() =>
                                        handleRejectRequest(member)
                                      }
                                      disabled={
                                        projectData.projectState === "CANCELED"
                                      }
                                    >
                                      Reject
                                    </Button>
                                  </>
                                )}
                                {member.type === "PENDING_BY_INVITATION" && (
                                  <>
                                    <Button
                                      variant="danger"
                                      onClick={() =>
                                        handleRemoveInvitation(member.id)
                                      }
                                      disabled={
                                        projectData.projectState === "CANCELED"
                                      }
                                    >
                                      Remove Invitation
                                    </Button>
                                  </>
                                )}
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </Table>
                    )}
                  </Card.Body>
                </Card>
              </div>
            </Col>
          </Row>

          <Row className="mt-4">
            <Col>
              <Card>
                <Card.Header>Available Users</Card.Header>
                <Card.Body>
                  <Form.Group as={Row} className="mb-3">
                    <Form.Label column sm={2}>
                      Search
                    </Form.Label>
                    <Col sm={10}>
                      <Form.Control
                        type="text"
                        value={searchKeyword}
                        onChange={(e) => setSearchKeyword(e.target.value)}
                      />
                    </Col>
                  </Form.Group>
                  <Form.Group as={Row} className="mb-3">
                    <Form.Label column sm={2}>
                      Lab
                    </Form.Label>
                    <Col sm={10}>
                      <Form.Select
                        value={selectedLab}
                        onChange={(e) => setSelectedLab(e.target.value)}
                      >
                        <option value="">All Labs</option>
                        {labs.map((lab) => (
                          <option key={lab.local} value={lab.local}>
                            {formatLab(lab.local)}
                          </option>
                        ))}
                      </Form.Select>
                    </Col>
                  </Form.Group>
                  {allUsers.length === 0 ? (
                    <p>No users available to show based on the selected criteria.</p>
                  ) : (
                    <>
                      <Table striped bordered hover className="custom-table">
                        <thead>
                          <tr>
                            <th>Photo</th>
                            <th>Name</th>
                            <th>Lab</th>
                            <th>Skills</th>
                            <th>Actions</th>
                          </tr>
                        </thead>
                        <tbody>
                          {allUsers.map((user) => (
                            <tr
                              key={user.id}
                              onClick={() =>
                                window.open(
                                  `/profile/${user.systemUsername}`,
                                  "_blank"
                                )
                              }
                            >
                              <td className="align-middle">
                                <img
                                  src={user.photo}
                                  alt={user.name}
                                  width={50}
                                  height={50}
                                  className="rounded-circle"
                                />
                              </td>
                              <td className="align-middle">
                                {user.systemUsername === userSystemUsername
                                  ? "You"
                                  : user.name}
                              </td>
                              <td className="align-middle">{formatLab(user.lab)}</td>
                              <td className="align-middle">
                                {user.skills.map((skill) => (
                                  <span
                                    key={skill.id}
                                    className={
                                      skill.inProject ? "text-success" : ""
                                    }
                                  >
                                    {formatSkill(skill.name)}
                                    <br />
                                  </span>
                                ))}
                              </td>
                              <td className="align-middle">
                                <Button
                                  variant="primary"
                                  onClick={(e) => {
                                    e.stopPropagation();
                                    handleInvite(user.systemUsername);
                                  }}
                                >
                                  Invite
                                </Button>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </Table>
                      <TablePagination
                        totalPages={maxPageNumber}
                        currentPage={currentPage}
                        setCurrentPage={setCurrentPage}
                        setNavigateTableTrigger={setNavigateTableTrigger}
                      />
                    </>
                  )}
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>

        {/* Remove Member Modal */}
        <Modal show={showRemoveModal} onHide={() => setShowRemoveModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Remove Member</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form.Group>
              <Form.Label>Reason for Removal</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                value={removeReason}
                onChange={(e) => setRemoveReason(e.target.value)}
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="secondary"
              onClick={() => setShowRemoveModal(false)}
            >
              Cancel
            </Button>
            <Button variant="danger" onClick={handleConfirmRemove}>
              Confirm Remove
            </Button>
          </Modal.Footer>
        </Modal>

        {/* Reject Request Modal */}
        <Modal show={showRejectModal} onHide={() => setShowRejectModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Reject Application</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form.Group>
              <Form.Label>Reason for Rejection</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                value={rejectReason}
                onChange={(e) => setRejectReason(e.target.value)}
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="secondary"
              onClick={() => setShowRejectModal(false)}
            >
              Cancel
            </Button>
            <Button variant="danger" onClick={handleConfirmReject}>
              Confirm Reject
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    </>
  );
};

export default ProjectMembersPage;
