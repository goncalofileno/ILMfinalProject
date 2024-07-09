import AppNavbar from "../components/headers/AppNavbar";
import AsideProjectCreationPage2 from "../components/asides/AsideProjectCreationPage2";
import UserCard from "../components/cards/UserCard";
import { Row, Col, InputGroup, Form, Button } from "react-bootstrap";
import { getUserProjectCreation, addMembers } from "../utilities/services";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import StandardModal from "../components/modals/StandardModal";
import { useMediaQuery } from "react-responsive";
import { useNavigate } from "react-router-dom";
import { Trans, t } from "@lingui/macro";
import Cookies from "js-cookie";

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
  const [modalType, setModalType] = useState("warning");
  const [modalMessage, setModalMessage] = useState("");
  const [modalActive, setModalActive] = useState(false);
  const [isAsideVisible, setIsAsideVisible] = useState(false);
  const isMobileAuxiliar = useMediaQuery({ query: "(max-width: 330px)" });
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const isTablet = useMediaQuery({ query: "(max-width: 991px)" });
  const [currentLanguage, setCurrentLanguage] = useState(
    Cookies.get("user-language") || "ENGLISH"
  );
  const navigate = useNavigate();

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
    console.log("maxMembers", maxMembers);
    if (maxMembers < 31) {
      if (usersInProject.length <= maxMembers - 1) {
        let projectCreationMembersDto = {
          maxMembers: maxMembers,
          usersInProject: usersInProject.map((user) => user.id),
        };
        addMembers(systemProjectName, projectCreationMembersDto).then(
          (response) => {
            if (response.status === 200) {
              setModalType("success");
              setModalMessage(
                t`The members have been added to the project successfully`
              );
              setModalActive(true);
              setTimeout(() => {
                navigate(`/create-project/${systemProjectName}/resources`);
              }, 1500);
            }
          }
        );
      } else {
        setModalType("danger");
        setModalMessage(t`You have exceeded the maximum number of members`);
        setModalActive(true);
      }
    } else {
      setModalType("danger");
      setModalMessage(
        t`Please select a maximum number of members value smaller or equal to 30`
      );
      setModalActive(true);
    }
  };

  return (
    <>
      <AppNavbar
        setIsAsideVisible={setIsAsideVisible}
        pageWithAside={true}
        setCurrentLanguage={setCurrentLanguage}
      />
      <StandardModal
        modalType={modalType}
        message={modalMessage}
        modalActive={modalActive}
        setModalActive={setModalActive}
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
        isVisible={isAsideVisible}
      />
      <div
        className={isMobile ? "ilm-page-mobile" : "ilm-pageb-with-aside"}
        onClick={() => isAsideVisible && setIsAsideVisible((prev) => !prev)}
        style={{ height: isTablet && "unset" }}
      >
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
            placeholder={t`Search for user`}
            style={{
              borderRadius: "10px",
              cursor: "text",
              width: isMobile && "100%",
            }}
            className="custom-focus"
            value={keyword}
            onChange={(e) => {
              setKeyword(e.target.value);
            }}
          />
          <div className="flex-btn-row-mail-table">
            <Button
              variant="primary"
              id="primary-btn-boot"
              onClick={() => setGetUsersTrigger((prev) => !prev)}
            >
              <Trans>Search</Trans>
            </Button>
            <Button
              variant="secondary"
              style={{ borderRadius: "10px" }}
              onClick={handleClearSearch}
            >
              <Trans>Clear Search</Trans>
            </Button>
          </div>
        </InputGroup>
        <Row className="row-container">
          <Col
            className="height-100 arrow-cols"
            xs={1}
            sm={1}
            style={{ height: isTablet && "550px" }}
          >
            {" "}
            {currentPage > 1 && (
              <button className="btn-arrow" onClick={handlePreviousClick}>
                <i className="fas fa-chevron-left fa-3x"></i>
              </button>
            )}
          </Col>
          <Col className="height-100" xs={10} sm={10}>
            <Row
              className="height-45"
              style={{ marginBottom: isTablet && "10px" }}
            >
              {users !== null && !isTablet
                ? users.slice(0, 3).map((user) => {
                    return (
                      <Col className="height-100" lg={4}>
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
                          publicProfile={user.publicProfile}
                        ></UserCard>
                      </Col>
                    );
                  })
                : users.slice(0, 4).map((user) => {
                    return (
                      <Col
                        className="height-100"
                        sm={6}
                        lg={4}
                        style={{ marginBottom: isTablet && "30px" }}
                      >
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
              {users !== null && !isTablet
                ? users.slice(3, 6).map((user) => {
                    return (
                      <Col className="height-100" sm={6} lg={4}>
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
                  })
                : users.slice(4, 6).map((user) => {
                    return (
                      <Col
                        className="height-100"
                        sm={6}
                        lg={4}
                        style={{ marginBottom: isTablet && "30px" }}
                      >
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
                <Trans>Next step</Trans>
              </button>
            </Row>
          </Col>
          <Col
            className="height-100 arrow-cols"
            xs={1}
            sm={1}
            style={{
              height: isTablet && "550px",
            }}
          >
            {" "}
            {currentPage < totalPages && (
              <button className="btn-arrow" onClick={handleNextClick}>
                <i className="fas fa-chevron-right fa-3x"></i>
              </button>
            )}
          </Col>
        </Row>
      </div>
    </>
  );
}
