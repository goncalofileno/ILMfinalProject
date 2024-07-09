import AppNavbar from "../components/headers/AppNavbar";
import { useMediaQuery } from "react-responsive";
import { Trans } from "@lingui/react";
import { Row, Col, Card, Form, Alert, Button } from "react-bootstrap";
import {
  getUserProjectCreation,
  getLabsWithSessionId,
  inviteUserToProject,
} from "../utilities/services";
import { useState, useEffect } from "react";
import { formatLab } from "../utilities/converters";
import { Cookies } from "react-cookie";
import InviteProjectModal from "../components/modals/InviteProjectModal";
import TablePagination from "../components/paginations/TablePagination";

export default function UsersPage() {
  const isMobile = useMediaQuery({ query: "(max-width: 767px)" });
  const isSmallMobile = useMediaQuery({ query: "(max-width: 575px)" });
  const userSystemUsername = new Cookies().get("systemUsername");
  const [allUsers, setAllUsers] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedLab, setSelectedLab] = useState("");
  const [searchKeyword, setSearchKeyword] = useState("");
  const [systemUsername, setSystemUsername] = useState("");
  const [usersTrigger, setUsersTrigger] = useState(false);
  const [labs, setLabs] = useState([]);
  const isPhone = useMediaQuery({ query: "(max-width: 767px)" });
  const [showInviteModal, setShowInviteModal] = useState(false);
  const NUMBER_OF_USERS_PAGE = 6;
  const rejectedUsers = [];

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
  }, [usersTrigger]);

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

  return (
    <>
      <AppNavbar />
      <div
        className={isMobile ? "ilm-page-mobile" : "ilm-pageb"}
        style={{ padding: "15px 30px" }}
      >
        <h1 className="page-title">
          <span className="app-slogan-1">All </span>
          <span className="app-slogan-2">Users</span>
        </h1>
        <Row className="mt-1">
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
                        <Form.Label column sm={2}>
                          <h6
                            style={{
                              marginBottom: "0px",
                              padding: "0px",
                            }}
                          >
                            Search
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
                            Clear
                          </button>
                          <button
                            className="submit-button"
                            style={{
                              padding: "5px",
                              width: !isSmallMobile ? "100px" : "45%",
                            }}
                            onClick={() => setUsersTrigger((prev) => !prev)}
                          >
                            Search
                          </button>
                        </div>
                      </Col>
                      <Col sm={4}>
                        <Form.Group as={Col} className="mb-3">
                          <Form.Label column sm={2} style={{ width: "100%" }}>
                            <h6>Lab</h6>
                          </Form.Label>
                          <Col sm={10}>
                            <Form.Select
                              value={selectedLab}
                              onChange={(e) => {
                                setSelectedLab(e.target.value);
                                setUsersTrigger((prev) => !prev);
                              }}
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
                        Name
                      </th>
                      {!isPhone && <th style={{ width: "20%" }}>Lab</th>}
                      <th style={{ width: !isPhone ? "30%" : "35%" }}>
                        Skills
                      </th>
                      <th style={{ width: !isPhone ? "20%" : "25%" }}>
                        Actions
                      </th>
                    </tr>
                  </thead>
                  <tbody id="body-table-members-project">
                    {allUsers.length === 0 ? (
                      <tr style={{ height: "100%" }}>
                        <td colspan="7">
                          <div className="no-results no-results-align">
                            No users available to show based on the selected
                            criteria.
                          </div>
                        </td>
                      </tr>
                    ) : (
                      <>
                        {allUsers.map((user) => (
                          <tr
                            key={user.id}
                            onClick={() =>
                              window.open(
                                `/profile/${user.systemUsername}`,
                                "_blank"
                              )
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
                                ? "You"
                                : user.name}
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
                                      <span
                                        style={{
                                          height: "100%",
                                          display: "flex",
                                          alignItems: "center",
                                        }}
                                      >
                                        This user has a private profile
                                      </span>
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
                              >
                                Invite
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
    </>
  );
}
