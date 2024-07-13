import AppNavbar from "../components/headers/AppNavbar";
import { useMediaQuery } from "react-responsive";
import { Row, Col, Card, Form, Alert, Button } from "react-bootstrap";
import {
  getUserProjectCreation,
  getLabsWithSessionId,
  inviteUserToProject,
} from "../utilities/services";
import { useState, useEffect } from "react";
import { formatLab } from "../utilities/converters";
import InviteProjectModal from "../components/modals/InviteProjectModal";
import ComposeMailModal from "../components/modals/ComposeMailModal"; // Import the ComposeMailModal
import TablePagination from "../components/paginations/TablePagination";
import MailIcon from "../resources/icons/other/email-icon.png";
import { Trans, t } from "@lingui/macro";
import Cookies from "js-cookie";
import React from "react";
import { useNavigate } from "react-router-dom";

export default function UsersPage() {
  const isMobile = useMediaQuery({ query: "(max-width: 767px)" });
  const isSmallMobile = useMediaQuery({ query: "(max-width: 575px)" });
  const userSystemUsername = Cookies.get("user-system-username");
  const [allUsers, setAllUsers] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedLab, setSelectedLab] = useState("");
  const [searchKeyword, setSearchKeyword] = useState("");
  const [systemUsername, setSystemUsername] = useState("");
  const [selectedUserContact, setSelectedUserContact] = useState(""); // State to hold the selected user contact
  const [usersTrigger, setUsersTrigger] = useState(false);
  const [labs, setLabs] = useState([]);
  const isPhone = useMediaQuery({ query: "(max-width: 767px)" });
  const [showInviteModal, setShowInviteModal] = useState(false);
  const [showMailModal, setShowMailModal] = useState(false); // State for the mail modal
  const NUMBER_OF_USERS_PAGE = 6;
  const [currentLanguage, setCurrentLanguage] = useState(
    Cookies.get("user-language") || "ENGLISH"
  );
  const rejectedUsers = [];
  const navigate = useNavigate();

  useEffect(() => {
    getUserProjectCreation(
      "",
      rejectedUsers,
      currentPage,
      selectedLab,
      searchKeyword
    ).then((result) => {
      if (result.status === 200) {
        result.json().then((data) => {
          setAllUsers(data.userProjectCreationDtos || []);
          setTotalPages(data.maxPageNumber);
        });
      }
    });
  }, [usersTrigger, currentLanguage]);

  useEffect(() => {
    getLabsWithSessionId().then((result) => {
      if (result.status === 200) {
        result.json().then((data) => {
          setLabs(data);
        });
      }
    });
  }, []);

  const handleInvite = (username) => {
    setSystemUsername(username);
    setShowInviteModal(true);
  };

  const handleCloseInviteModal = () => {
    setShowInviteModal(false);
  };

  const handleSendMail = (user) => {
    const contact = `${user.name} <${user.email}>`;
    setSelectedUserContact(contact);
    setShowMailModal(true);
  };

  const handleCloseMailModal = () => {
    setShowMailModal(false);
  };

  return (
    <>
      <AppNavbar setCurrentLanguage={setCurrentLanguage} />
      <div
        className={isMobile ? "ilm-page-mobile" : "ilm-pageb"}
        style={{ paddingTop: "15px" }}
      >
        <h1 className="page-title">
          <Trans>
            <span className="app-slogan-1">All </span>
            <span className="app-slogan-2">Users</span>
          </Trans>
        </h1>
        <Row className="mt-1" style={{ marginRight: "0px", marginLeft: "0px" }}>
          <Col>
            <Card
              style={{
                border: "none",
                backgroundColor: "transparent",
              }}
            >
              <Card.Body
                style={{ border: "none", backgroundColor: "transparent" }}
              >
                <Row>
                  <Form.Group as={Col} className="mb-3">
                    <Row
                      style={{
                        display: "flex",
                        alignItems: "center",
                      }}
                    >
                      <Col sm={6}>
                        <Form.Label column sm={2} style={{ width: "100%" }}>
                          <h6
                            style={{
                              marginBottom: "0px",
                              padding: "0px",
                            }}
                          >
                            <Trans>Search</Trans>
                          </h6>
                        </Form.Label>
                        <Form.Control
                          type="text"
                          value={searchKeyword}
                          onChange={(e) => {
                            setSearchKeyword(e.target.value);
                          }}
                        />
                      </Col>
                      <Col sm={2}>
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
                              setUsersTrigger((prev) => !prev);
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
                            onClick={() => setUsersTrigger((prev) => !prev)}
                          >
                            <Trans>Search</Trans>
                          </button>
                        </div>
                      </Col>
                      <Col sm={4}>
                        <Form.Group as={Col} className="mb-3">
                          <Form.Label column sm={2} style={{ width: "100%" }}>
                            <h6>
                              <Trans>Lab</Trans>
                            </h6>
                          </Form.Label>
                          <Col sm={10}>
                            <Form.Select
                              value={selectedLab}
                              onChange={(e) => {
                                setSelectedLab(e.target.value);
                                setUsersTrigger((prev) => !prev);
                              }}
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
                              {user.systemUsername === userSystemUsername ? (
                                <strong>{t`You`}</strong>
                              ) : (
                                <strong>{user.name}</strong>
                              )}
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
                                      key={skill.id}
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
                                  marginRight: "10px",
                                }}
                                onClick={(e) => {
                                  e.stopPropagation();
                                  handleInvite(user.systemUsername);
                                }}
                              >
                                <Trans>Invite</Trans>
                              </Button>
                              <img
                                src={MailIcon}
                                alt="Mail"
                                style={{
                                  cursor: "pointer",
                                  width: "50px",
                                  height: "50px",
                                  transition: "transform 0.2s",
                                }}
                                onClick={(e) => {
                                  e.stopPropagation();
                                  handleSendMail(user);
                                }}
                                onMouseDown={(e) => {
                                  e.target.style.transform = "scale(0.9)";
                                }}
                                onMouseUp={(e) => {
                                  e.target.style.transform = "scale(1)";
                                }}
                                onMouseLeave={(e) => {
                                  e.target.style.transform = "scale(1)";
                                }}
                              />
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
                    totalPages={totalPages}
                    currentPage={currentPage}
                    setCurrentPage={setCurrentPage}
                    setNavigateTableTrigger={setUsersTrigger}
                  />
                ) : (
                  <div style={{ height: "53.6px" }}></div>
                )}
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </div>
      <InviteProjectModal
        show={showInviteModal}
        handleClose={handleCloseInviteModal}
        systemUsername={systemUsername}
      />
      <ComposeMailModal
        show={showMailModal}
        handleClose={handleCloseMailModal}
        preFilledContact={selectedUserContact}
        preFilledSubject=""
      />
    </>
  );
}
