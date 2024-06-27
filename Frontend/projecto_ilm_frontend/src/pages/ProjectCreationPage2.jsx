import AppNavbar from "../components/headers/AppNavbar";
import AsideProjectCreationPage2 from "../components/asides/AsideProjectCreationPage2";
import UserCard from "../components/cards/UserCard";
import { Row, Col, InputGroup, Form, Button } from "react-bootstrap";
import { getUserProjectCreation, addMembers } from "../utilities/services";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import StandardModal from "../components/modals/StandardModal";

export default function ProjectCreationPage2() {
  const [users, setUsers] = useState([]);
  const { systemProjectName } = useParams();
  const [selectedLab, setSelectedLab] = useState("");
  const [keyword, setKeyword] = useState("");
  const [usersInProject, setUsersInProject] = useState([]);
  const [rejectedUsers, setRejectedUsers] = useState([]);
  const [getUsersTrigger, setGetUsersTrigger] = useState(false);
  const [totalPages, setTotalPages] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [maxMembers, setMaxMembers] = useState(4);

  useEffect(() => {
    console.log("rejected users changed", rejectedUsers);
    getUserProjectCreation(
      systemProjectName,
      rejectedUsers,
      currentPage,
      selectedLab,
      keyword
    ).then((response) => {
      return response.json().then((data) => {
        setUsers(data.userProjectCreationDtos);
        setTotalPages(data.maxPageNumber);
      });
    });
  }, [getUsersTrigger]);

  const handlePreviousClick = () => {
    setCurrentPage(currentPage - 1);
    setGetUsersTrigger((prev) => !prev);
  };

  const handleNextClick = () => {
    setCurrentPage(currentPage + 1);
    setGetUsersTrigger((prev) => !prev);
  };

  const handleClearSearch = () => {
    setKeyword("");
    setGetUsersTrigger((prev) => !prev);
  };

  const handleSubmit = () => {
    let projectCreationMembersDto = {
      maxMembers: maxMembers,
      usersInProject: usersInProject.map((user) => user.id),
    };
    addMembers(systemProjectName, projectCreationMembersDto).then(
      (response) => {
        if (response.status === 200) {
          console.log("Members added successfully");
        }
      }
    );
  };

  return (
    <>
      <AppNavbar />
      <StandardModal
        modalType="success"
        message="alerta"
        modalActive={true}
      ></StandardModal>
      <AsideProjectCreationPage2
        selectedLab={selectedLab}
        setSelectedLab={setSelectedLab}
        usersInProject={usersInProject}
        setUsersInProject={setUsersInProject}
        rejectedUsers={rejectedUsers}
        setRejectedUsers={setRejectedUsers}
        setGetUsersTrigger={setGetUsersTrigger}
        maxMembers={maxMembers}
        setMaxMembers={setMaxMembers}
      />
      <div className="ilm-pageb-with-aside">
        <h1 className="page-title">
          <span className="app-slogan-1">Project </span>
          <span className="app-slogan-2">Members</span>
        </h1>
        <InputGroup
          className="mail-filters"
          style={{
            width: "50%",
            left: "50%",
            transform: "translateX(-50%)",
          }}
        >
          <Form.Control
            type="text"
            placeholder="Search for user"
            style={{ borderRadius: "10px", cursor: "text" }}
            className="custom-focus"
            value={keyword}
            onChange={(e) => {
              setKeyword(e.target.value);
            }}
          />
          <Button
            variant="primary"
            id="primary-btn-boot"
            onClick={() => setGetUsersTrigger((prev) => !prev)}
          >
            Search
          </Button>
          <Button
            variant="secondary"
            style={{ borderRadius: "10px" }}
            onClick={handleClearSearch}
          >
            Clear Search
          </Button>
        </InputGroup>
        <Row className="row-container">
          <Col className="height-100 arrow-cols" sm={1}>
            {" "}
            {currentPage > 1 && (
              <button className="btn-arrow" onClick={handlePreviousClick}>
                <i className="fas fa-chevron-left fa-3x"></i>
              </button>
            )}
          </Col>
          <Col className="height-100" sm={10}>
            <Row className="height-45">
              {users !== null &&
                users.slice(0, 3).map((user) => {
                  return (
                    <Col className="height-100" sm={4}>
                      <UserCard
                        name={user.name}
                        lab={user.lab}
                        img={user.photo}
                        id={user.id}
                        skills={user.skills}
                        systemUsername={user.systemUsername}
                        usersInProject={usersInProject}
                        setUsersInProject={setUsersInProject}
                        rejectedUsers={rejectedUsers}
                        setRejectedUsers={setRejectedUsers}
                        setGetUsersTrigger={setGetUsersTrigger}
                        maxMembers={maxMembers}
                        numberOfMembersInProject={usersInProject.length}
                      ></UserCard>
                    </Col>
                  );
                })}
            </Row>
            <Row style={{ height: "3%" }}></Row>
            <Row className="height-45">
              {users !== null &&
                users.slice(3, 6).map((user) => {
                  return (
                    <Col className="height-100" sm={4}>
                      <UserCard
                        name={user.name}
                        lab={user.lab}
                        img={user.photo}
                        id={user.id}
                        skills={user.skills}
                        systemUsername={user.systemUsername}
                        usersInProject={usersInProject}
                        setUsersInProject={setUsersInProject}
                        rejectedUsers={rejectedUsers}
                        setRejectedUsers={setRejectedUsers}
                        setGetUsersTrigger={setGetUsersTrigger}
                        maxMembers={maxMembers}
                        numberOfMembersInProject={usersInProject.length}
                      ></UserCard>
                    </Col>
                  );
                })}
            </Row>

            <Row className="last-row-submit">
              {" "}
              <button
                className="submit-button"
                style={{ width: "54%" }}
                onClick={handleSubmit}
              >
                Next page
              </button>
            </Row>
          </Col>
          <Col className="height-100 arrow-cols" sm={1}>
            {" "}
            {currentPage < totalPages && (
              <button className="btn-arrow" onClick={handleNextClick}>
                <i className="fas fa-chevron-right fa-3x "></i>
              </button>
            )}
          </Col>
        </Row>
      </div>
    </>
  );
}
