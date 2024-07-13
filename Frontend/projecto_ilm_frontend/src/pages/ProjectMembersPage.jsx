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
  formatTypeUserInProject,
  formatTypeUserInProjectApplicationRequest,
  formatProjectState,
} from "../utilities/converters";
import { useMediaQuery } from "react-responsive";
import { Trans, t } from "@lingui/macro";
import { useNavigate } from "react-router-dom";

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
  const NUMBER_OF_MEMBERS_PAGE = 5;
  const NUMBER_OF_USERS_PAGE = 6;
  const isSmallMobile = useMediaQuery({ query: "(max-width: 575px)" });
  const isPhone = useMediaQuery({ query: "(max-width: 767px)" });
  const isTablet = useMediaQuery({ query: "(max-width: 991px)" });
  const sessionId = Cookies.get("session-id");
  const userSystemUsername = Cookies.get("user-systemUsername");
  const [currentLanguage, setCurrentLanguage] = useState(
    Cookies.get("user-language") || "ENGLISH"
  );
  const navigate = useNavigate();
  

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
  }, [systemProjectName, sessionId, currentLanguage]);

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
      setError(t`An error occurred while changing user role.`);
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
      setError(t`An error occurred while removing the user.`);
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
      setError(t`An error occurred while accepting the application.`);
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
      setError(t`An error occurred while rejecting the application.`);
    }
  };

  const fetchAllUsers = async (page = 1) => {
    try {
      const rejectedUsers = [
        ...projectData.projectMembers.map((member) => member.id),
        ...projectData.projectMembers.map((member) => member.id),
      ];

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

      setAllUsers(data.userProjectCreationDtos || []);
      setMaxPageNumber(data.maxPageNumber);
    } catch (fetchError) {
      setError(t`An error occurred while fetching users.`);
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
      alert(t`An error occurred while inviting the user.`);
    }
  };

  const handleRemoveInvitation = async (userId) => {
    try {
      const result = await removeInvitation(
        sessionId,
        systemProjectName,
        userId
      );
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
  }, [projectData, currentPage, selectedLab, navigateTableTrigger]);

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

  const isProjectInactive =
    projectData.projectState === "CANCELED" ||
    projectData.projectState === "READY";
  const isTeamFull = members.length >= projectData.maxMembers;

  const getMemberType = (memberType) => {
    return ["MEMBER", "MEMBER_BY_INVITATION", "MEMBER_BY_APPLIANCE"].includes(
      memberType
    )
      ? "MEMBER"
      : memberType;
  };

  return (
    <>
      <AppNavbar setCurrentLanguage={setCurrentLanguage} />

      <div className="ilm-pageb-noheight">
        <ProjectTabs
          typeOfUserSeingProject={projectData.userSeingProject}
          projectName={projectData.projectName}
        />
        <Container>
          <Row>
            <Col>
              {(projectData.projectState === "CANCELED" || projectData.projectState === "READY") && (
                <Alert variant="danger" className="standard-modal">
                  <Trans>
                    The project is {formatProjectState(projectData.projectState)} and no changes can be made.
                  </Trans>
                </Alert>
              )}
            </Col>
          </Row>
          <Row>
            {isTeamFull && (
              <Alert variant="warning">
                <Trans>
                  The team is full, no more members can be invited or accepted
                  until the team has an available slot.
                </Trans>
              </Alert>
            )}
            <Col>
              <div
                className="d-flex justify-content-between"
                style={{ flexDirection: isTablet && "column" }}
              >
                <Card
                  style={{
                    width: !isTablet ? "48%" : "100%",
                    border: "none",
                    backgroundColor: "transparent",
                  }}
                >
                  <Card.Header
                    style={{ backgroundColor: "transparent", border: "none" }}
                  >
                    <h5>
                      <Trans>
                        Members ({members.length}/{projectData.maxMembers})
                      </Trans>
                    </h5>
                  </Card.Header>
                  <Card.Body
                    style={{ border: "none", backgroundColor: "transparent" }}
                  >
                    <table
                      className="table-users-project-table"
                      style={{ height: "440px" }}
                    >
                      <thead id="table-users-project-head">
                        <tr>
                          <th colSpan={2} style={{ width: "40%" }}>
                            <Trans>Name</Trans>
                          </th>
                          <th style={{ width: "30%" }}>
                            <Trans>Type</Trans>
                          </th>
                          <th style={{ width: "30%" }}>
                            <Trans>Actions</Trans>
                          </th>
                        </tr>
                      </thead>
                      <tbody
                        id="body-table-members-project"
                        style={{
                          overflow:
                            members.length <= NUMBER_OF_MEMBERS_PAGE &&
                            "hidden",
                        }}
                      >
                        {members.map((member) => (
                          <tr key={member.id}>
                            <td
                              className="align-middle"
                              style={{ width: "15%" }}
                            >
                              <img
                                src={member.profilePicture}
                                alt={member.name}
                                width={50}
                                height={50}
                                className="rounded-circle"
                              />
                            </td>
                            <td
                              style={{ width: "25%" }}
                              className="align-middle"
                            >
                              {member.systemUsername === userSystemUsername
                                ? <strong>{t`You`}</strong>
                                : <strong>{member.name}</strong>}
                            </td>
                            <td
                              style={{ width: "30%" }}
                              className="align-middle"
                            >
                              {formatTypeUserInProject(member.type)}
                              {member.type !== "CREATOR" && (
                                <Form.Select
                                  value={
                                    selectedMemberType[member.systemUsername] ||
                                    getMemberType(member.type)
                                  }
                                  onChange={(e) =>
                                    handleRoleChange(member, e.target.value)
                                  }
                                  disabled={{ isProjectInactive }}
                                  className="mt-2"
                                >
                                  <option value="MANAGER">
                                    <Trans>Manager</Trans>
                                  </option>
                                  <option value="MEMBER">
                                    <Trans>Member</Trans>
                                  </option>
                                </Form.Select>
                              )}
                            </td>
                            <td
                              style={{ width: "30%" }}
                              className="align-middle"
                            >
                              {member.type !== "CREATOR" && (
                                <Button
                                  variant="danger"
                                  onClick={() => handleRemoveMember(member)}
                                  disabled={isProjectInactive}
                                >
                                  <Trans>Remove</Trans>
                                </Button>
                              )}
                            </td>
                          </tr>
                        ))}
                        {members.length < NUMBER_OF_MEMBERS_PAGE &&
                          Array(NUMBER_OF_MEMBERS_PAGE - members.length)
                            .fill()
                            .map((index) => (
                              <tr key={index + members.length}>
                                <td></td>
                                <td></td>
                                <td></td>
                              </tr>
                            ))}
                      </tbody>
                    </table>
                  </Card.Body>
                </Card>

                <Card
                  style={{
                    width: !isTablet ? "48%" : "100%",
                    border: "none",
                    backgroundColor: "transparent",
                  }}
                >
                  <Card.Header
                    style={{ backgroundColor: "transparent", border: "none" }}
                  >
                    <h5>
                      <Trans>Requests and Applications</Trans>
                    </h5>
                  </Card.Header>
                  <Card.Body
                    style={{ border: "none", backgroundColor: "transparent" }}
                  >
                    {requests.length === 0 ? (
                      <p>
                        <Trans>No requests or applications to show.</Trans>
                      </p>
                    ) : (
                      <table
                        className="table-users-project-table"
                        style={{ height: "440px" }}
                      >
                        <thead id="table-users-project-head">
                          <tr>
                            <th style={{ width: "40%" }} colSpan={2}>
                              <Trans>Name</Trans>
                            </th>
                            <th style={{ width: "30%" }}>
                              <Trans>Type</Trans>
                            </th>
                            <th style={{ width: "30%" }}>
                              <Trans>Actions</Trans>
                            </th>
                          </tr>
                        </thead>
                        <tbody id="body-table-members-project">
                          {requests.map((member) => (
                            <tr key={member.id}>
                              <td
                                className="align-middle"
                                style={{ width: "15%" }}
                              >
                                <img
                                  src={member.profilePicture}
                                  alt={member.name}
                                  width={50}
                                  height={50}
                                  className="rounded-circle"
                                />
                              </td>
                              <td
                                style={{ width: "25%" }}
                                className="align-middle"
                              >
                                {member.systemUsername === userSystemUsername
                                  ? <strong>{t`You`}</strong>
                                  : <strong>{member.name}</strong>}
                              </td>
                              <td
                                style={{ width: "28%" }}
                                className="align-middle"
                              >
                                {formatTypeUserInProjectApplicationRequest(
                                  member.type
                                )}
                              </td>
                              <td
                                className="align-middle"
                                style={{ width: "32%" }}
                              >
                                {member.type === "PENDING_BY_APPLIANCE" && (
                                  <>
                                    <Button
                                      variant="success"
                                      onClick={() =>
                                        handleAcceptRequest(member)
                                      }
                                      disabled={isTeamFull}
                                      className="me-2"
                                    >
                                      <Trans>Accept</Trans>
                                    </Button>
                                    <Button
                                      variant="danger"
                                      onClick={() =>
                                        handleRejectRequest(member)
                                      }
                                      disabled={isProjectInactive}
                                    >
                                      <Trans>Reject</Trans>
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
                                      disabled={isProjectInactive}
                                    >
                                      <Trans>Cancel</Trans>
                                    </Button>
                                  </>
                                )}
                              </td>
                            </tr>
                          ))}
                          {requests.length < NUMBER_OF_MEMBERS_PAGE &&
                            Array(NUMBER_OF_MEMBERS_PAGE - requests.length)
                              .fill()
                              .map((index) => (
                                <tr key={index + members.length}>
                                  <td></td>
                                  <td></td>
                                  <td></td>
                                </tr>
                              ))}
                        </tbody>
                      </table>
                    )}
                  </Card.Body>
                </Card>
              </div>
            </Col>
          </Row>

          <Row className="mt-1">
            <Col>
              <Card
                style={{
                  border: "none",
                  backgroundColor: "transparent",
                }}
              >
                <Card.Header
                  style={{
                    backgroundColor: "transparent",
                    border: "none",
                    marginBottom: "15px",
                  }}
                >
                  <h5>
                    <Trans>Available Users</Trans>
                  </h5>
                </Card.Header>
                <Card.Body
                  style={{ border: "none", backgroundColor: "transparent" }}
                >
                  <Row>
                    <Form.Group as={Col} className="mb-3">
                      <Row style={{ display: "flex", alignItems: "center" }}>
                        <Col sm={6}>
                          <Form.Label column sm={2}>
                            <h6
                              style={{
                                marginBottom: "0px",
                              }}
                            >
                              <Trans>Search</Trans>
                            </h6>
                          </Form.Label>
                          <Form.Control
                            type="text"
                            value={searchKeyword}
                            onChange={(e) => setSearchKeyword(e.target.value)}
                          />
                        </Col>
                        <Col
                          sm={2}
                          style={{ marginBottom: isSmallMobile && "10px" }}
                        >
                          <div
                            style={{
                              display: "flex",
                              flexDirection: !isSmallMobile ? "column" : "row",
                              gap: !isSmallMobile ? "5px" : "10%",
                              marginTop: isSmallMobile && "10px",
                            }}
                          >
                            <button
                              className="secondary-button"
                              style={{
                                padding: "5px",
                                width: !isSmallMobile ? "100px" : "45%",
                              }}
                              onClick={() => {
                                setSearchKeyword("");
                                setNavigateTableTrigger((prev) => !prev);
                              }}
                            >
                              <Trans>Clear</Trans>
                            </button>
                            <button
                              className="submit-button"
                              style={{
                                padding: "5px",
                                width: !isSmallMobile ? "100px" : "45%",
                              }}
                              onClick={() =>
                                setNavigateTableTrigger((prev) => !prev)
                              }
                            >
                              <Trans>Search</Trans>
                            </button>
                          </div>
                        </Col>
                        <Col sm={4}>
                          <Form.Group as={Col} className="mb-3">
                            <Form.Label
                              column
                              sm={2}
                              style={{ width: "100%", paddingTop: "0px" }}
                            >
                              <h6>
                                <Trans>Lab</Trans>
                              </h6>
                            </Form.Label>
                            <Col sm={10}>
                              <Form.Select
                                value={selectedLab}
                                onChange={(e) => setSelectedLab(e.target.value)}
                              >
                                <option value="">
                                  <Trans>All Labs</Trans>
                                </option>
                                {labs.map((lab) => (
                                  <option key={lab.local} value={lab.local}>
                                    {formatLab(lab.local)}
                                  </option>
                                ))}
                              </Form.Select>
                            </Col>
                          </Form.Group>
                        </Col>
                      </Row>
                    </Form.Group>
                  </Row>
                  <table
                    className="table-users-project-table"
                    style={{ height: "485px", marginBottom: "15px" }}
                  >
                    <thead id="table-users-project-head">
                      <tr>
                        <th
                          colSpan={2}
                          style={{ width: !isPhone ? "30%" : "40%" }}
                        >
                          <Trans>Name</Trans>
                        </th>
                        {!isPhone && <th style={{ width: "20%" }}>Lab</th>}
                        <th style={{ width: !isPhone ? "30%" : "35%" }}>
                          <Trans>Skills</Trans>
                        </th>
                        <th style={{ width: !isPhone ? "20%" : "25%" }}>
                          <Trans>Actions</Trans>
                        </th>
                      </tr>
                    </thead>
                    <tbody id="body-table-members-project">
                      {allUsers.length === 0 ? (
                        <tr style={{ height: "100%" }}>
                          <td colspan="7">
                            <div className="no-results no-results-align">
                              <Trans>
                                No users available to show based on the selected
                                criteria.
                              </Trans>
                            </div>
                          </td>
                        </tr>
                      ) : (
                        <>
                          {allUsers.map((user) => (
                            <tr
                              key={user.id}
                              onClick={() =>
                                navigate(`/profile/${user.systemUsername}`)
                              }
                              style={{ height: "16.67%" }}
                            >
                              <td
                                style={{ width: !isPhone ? "10%" : "15%" }}
                                className="align-middle"
                              >
                                <img
                                  src={user.photo}
                                  alt={user.name}
                                  width={50}
                                  height={50}
                                  className="rounded-circle"
                                />
                              </td>
                              <td
                                style={{ width: !isPhone ? "20%" : "25%" }}
                                className="align-middle"
                              >
                                {user.systemUsername === userSystemUsername
                                  ? <strong><Trans>You</Trans></strong>
                                  : <strong>{user.name}</strong>}
                              </td>
                              {!isPhone && (
                                <td
                                  style={{ width: "20%" }}
                                  className="align-middle"
                                >
                                  {formatLab(user.lab)}
                                </td>
                              )}

                              <td style={{ width: !isPhone ? "30%" : "35%" }}>
                                <div className="skills-td-users">
                                  {user.publicProfile ? (
                                    user.skills.map((skill) => (
                                      <div
                                        className={`skill-div-${skill.inProject}`}
                                        style={{
                                          marginTop: "0px",
                                          marginBottom: "6px",
                                          height: "50%",
                                        }}
                                      >
                                        <div className="skill-div-col1">
                                          {skill.name}
                                        </div>
                                      </div>
                                    ))
                                  ) : (
                                    <div className="profile-users-row">
                                      <Alert
                                        variant="danger"
                                        style={{
                                          padding: "5px 5px",
                                          transform: "translateY(5px)",
                                        }}
                                      >
                                        {!isSmallMobile ? (
                                          <span
                                            style={{
                                              height: "100%",
                                              display: "flex",
                                              alignItems: "center",
                                            }}
                                          >
                                            <Trans>
                                              This user has a private profile.
                                            </Trans>
                                          </span>
                                        ) : (
                                          <span
                                            style={{
                                              height: "100%",
                                              display: "flex",
                                              alignItems: "center",
                                            }}
                                          >
                                            <Trans>Private profile</Trans>
                                          </span>
                                        )}
                                      </Alert>
                                    </div>
                                  )}
                                </div>
                              </td>
                              <td
                                style={{ width: !isPhone ? "20%" : "25%" }}
                                className="align-middle"
                              >
                                <Button
                                  variant="primary"
                                  style={{
                                    backgroundColor: "rgb(30, 40, 82)",
                                    borderColor: "rgb(30, 40, 82)",
                                  }}
                                  onClick={(e) => {
                                    e.stopPropagation();
                                    handleInvite(user.systemUsername);
                                  }}
                                  disabled={isTeamFull}
                                >
                                  <Trans>Invite</Trans>
                                </Button>
                              </td>
                            </tr>
                          ))}
                          {allUsers.length < NUMBER_OF_USERS_PAGE &&
                            Array(NUMBER_OF_USERS_PAGE - allUsers.length)
                              .fill()
                              .map(
                                (
                                  _,
                                  index // Fixed the map function to use _, index
                                ) => (
                                  <tr
                                    key={`placeholder-${index}`}
                                    style={{ height: "16.66%" }}
                                  >
                                    {" "}
                                    {/* Ensured unique key */}
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                  </tr>
                                )
                              )}
                        </>
                      )}
                    </tbody>
                  </table>
                  {allUsers.length > 0 ? (
                    <TablePagination
                      totalPages={maxPageNumber}
                      currentPage={currentPage}
                      setCurrentPage={setCurrentPage}
                      setNavigateTableTrigger={setNavigateTableTrigger}
                    />
                  ) : (
                    <div style={{ height: "53.6px" }}></div>
                  )}
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>

        {/* Remove Member Modal */}
        <Modal show={showRemoveModal} onHide={() => setShowRemoveModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>
              <Trans>Remove Member</Trans>
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form.Group>
              <Form.Label>
                <Trans>Reason for Removal</Trans>
              </Form.Label>
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
              <Trans>Cancel</Trans>
            </Button>
            <Button variant="danger" onClick={handleConfirmRemove}>
              <Trans>Confirm Remove</Trans>
            </Button>
          </Modal.Footer>
        </Modal>

        {/* Reject Request Modal */}
        <Modal show={showRejectModal} onHide={() => setShowRejectModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>
              <Trans>Reject Application</Trans>
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form.Group>
              <Form.Label>
                <Trans>Reason for Rejection</Trans>
              </Form.Label>
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
              <Trans>Cancel</Trans>
            </Button>
            <Button variant="danger" onClick={handleConfirmReject}>
              <Trans>Confirm Reject</Trans>
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    </>
  );
};

export default ProjectMembersPage;
