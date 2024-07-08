import AppNavbar from "../components/headers/AppNavbar";
import AsideMyProjectsPage from "../components/asides/AsideMyProjectsPage";
import MyProjectCard from "../components/cards/MyProjectCard";
import "./MyProjectsPage.css";
import { Col, Row, Container, Form, Button, InputGroup } from "react-bootstrap";
import { useEffect, useState } from "react";
import { getMyProjectsTable } from "../utilities/services";
import { useLocation, useNavigate } from "react-router-dom";
import { formatLab } from "../utilities/converters";
import { useMediaQuery } from "react-responsive";

export default function MyProjectsPage() {
  const query = new URLSearchParams(useLocation().search);
  const isTablet = useMediaQuery({ query: "(max-width: 1199px)" });
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const isSmallMobile = useMediaQuery({ query: "(max-width: 576px)" });
  const isMobileAuxiliar = useMediaQuery({ query: "(max-width: 740px)" });
  const navigate = useNavigate();
  const [projects, setProjects] = useState([]);
  const [totalPages, setTotalPages] = useState(0);
  const [isAsideVisible, setIsAsideVisible] = useState(false);
  const [currentPage, setCurrentPage] = useState(
    parseInt(query.get("currentPage")) || 1
  );
  const [selectedLab, setSelectedLab] = useState(
    query.get("selectedLab") || ""
  );
  const [selectedStatus, setSelectedStatus] = useState(
    query.get("selectedStatus") || ""
  );
  const [selectedTypeMember, setSelectedTypeMember] = useState(
    query.get("selectedTypeMember") || ""
  );
  const [keyword, setKeyword] = useState(query.get("search_query") || "");
  const [navigateTableProjectsTrigger, setNavigateTableProjectsTrigger] =
    useState(false);

  useEffect(() => {
    getMyProjectsTable(
      currentPage,
      selectedLab,
      selectedStatus,
      keyword,
      selectedTypeMember
    )
      .then((response) => response.json())
      .then((data) => {
        setProjects(data.tableProjects);
        setTotalPages(data.maxPageNumber);
        console.log(data.tableProjects);
      });

    const queryParamsObj = {};

    if (currentPage) queryParamsObj.currentPage = currentPage;
    if (selectedLab !== "") queryParamsObj.selectedLab = selectedLab;
    if (selectedStatus !== "") queryParamsObj.selectedStatus = selectedStatus;
    if (selectedTypeMember !== "")
      queryParamsObj.selectedTypeMember = selectedTypeMember;
    if (keyword) queryParamsObj.search_query = keyword;

    const queryParams = new URLSearchParams(queryParamsObj).toString();

    navigate(`/myprojects?${queryParams}`);
  }, [navigateTableProjectsTrigger]);

  const handlePreviousClick = () => {
    setCurrentPage(currentPage - 1);
    setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
  };

  const handleNextClick = () => {
    setCurrentPage(currentPage + 1);
    setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
  };
  return (
    <>
      <AppNavbar setIsAsideVisible={setIsAsideVisible} pageWithAside={true} />
      <AsideMyProjectsPage
        selectedLab={selectedLab}
        setSelectedLab={setSelectedLab}
        selectedStatus={selectedStatus}
        setSelectedStatus={setSelectedStatus}
        selectedTypeMember={selectedTypeMember}
        setSelectedTypeMember={setSelectedTypeMember}
        navigateTableProjectsTrigger={navigateTableProjectsTrigger}
        setNavigateTableProjectsTrigger={setNavigateTableProjectsTrigger}
        setCurrentPage={setCurrentPage}
        setKeyword={setKeyword}
        isVisible={isAsideVisible}
      />

      {console.log(projects)}
      <div
        className={
          isMobile
            ? "ilm-page-mobile-my-projects"
            : isTablet
            ? "ilm-page-tablet-with-aside"
            : "ilm-pageb-with-aside"
        }
      >
        <h1 className="page-title" style={{ marginBottom: "0px" }}>
          <span className="app-slogan-1">My </span>
          <span className="app-slogan-2">Projects</span>
        </h1>
        <InputGroup
          className="mail-filters"
          style={{
            width: isMobile ? "90%" : "50%",
            left: "50%",
            transform: "translateX(-50%)",
            marginTop: isMobile && "10px",
          }}
        >
          <Form.Control
            type="text"
            placeholder="Search for project name"
            style={{
              borderRadius: "10px",
              cursor: "text",
              marginBottom: "1%",
              width: isMobileAuxiliar && "70%",
            }}
            className="custom-focus"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <div className="flex-btn-row-mail-table">
            <Button
              variant="primary"
              onClick={() => {
                setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
              }}
              id="primary-btn-boot"
            >
              Search
            </Button>
            <Button
              variant="secondary"
              onClick={() => {
                setKeyword("");
                setNavigateTableProjectsTrigger(!navigateTableProjectsTrigger);
              }}
              style={{ borderRadius: "10px" }}
            >
              Clear Search
            </Button>
          </div>
        </InputGroup>
        <Container fluid className="my-projects-container">
          <Row style={{ height: "100%" }}>
            <Col
              xs={1}
              sm={1}
              className="align-center"
              style={{ justifyContent: "center" }}
            >
              {currentPage > 1 && (
                <button className="btn-arrow" onClick={handlePreviousClick}>
                  <i className="fas fa-chevron-left fa-3x"></i>
                </button>
              )}
            </Col>

            <Col xs={10} sm={10} style={{ height: "100%" }}>
              <Row style={{ height: "100%" }}>
                <Col sm={6} xl={4}>
                  {projects.slice(0, 4).map((project, index) => {
                    return index === 0 ? (
                      <div
                        style={{
                          height: !isSmallMobile ? "45%" : "300px",
                          marginBottom: "5%",
                        }}
                      >
                        <MyProjectCard
                          name={project.name}
                          lab={formatLab(project.lab)}
                          members={project.numberOfMembers}
                          maxMembers={project.maxMembers}
                          startDate={project.startDate}
                          endDate={project.finalDate}
                          progress={project.percentageDone}
                          status={project.status}
                          typeMember={project.userInProjectType}
                          systemProjectName={project.systemProjectName}
                          photo={project.photo}
                        ></MyProjectCard>
                      </div>
                    ) : (
                      index === 3 && (
                        <div
                          style={{ height: !isSmallMobile ? "45%" : "300px" }}
                        >
                          <MyProjectCard
                            name={project.name}
                            lab={formatLab(project.lab)}
                            members={project.numberOfMembers}
                            maxMembers={project.maxMembers}
                            startDate={project.startDate}
                            endDate={project.finalDate}
                            progress={project.percentageDone}
                            status={project.status}
                            typeMember={project.userInProjectType}
                            systemProjectName={project.systemProjectName}
                            photo={project.photo}
                          ></MyProjectCard>
                        </div>
                      )
                    );
                  })}
                  {isTablet &&
                    !isSmallMobile &&
                    projects.slice(2, 6).map((project, index) => {
                      return (
                        index === 0 && (
                          <div
                            style={{
                              height: !isSmallMobile ? "45%" : "300px",
                              marginBottom: "5%",
                            }}
                          >
                            <MyProjectCard
                              name={project.name}
                              lab={formatLab(project.lab)}
                              members={project.numberOfMembers}
                              maxMembers={project.maxMembers}
                              startDate={project.startDate}
                              endDate={project.finalDate}
                              progress={project.percentageDone}
                              status={project.status}
                              typeMember={project.userInProjectType}
                              systemProjectName={project.systemProjectName}
                              photo={project.photo}
                            ></MyProjectCard>
                          </div>
                        )
                      );
                    })}
                </Col>
                <Col sm={6} xl={4}>
                  {projects.slice(1, 5).map((project, index) => {
                    return index === 0 ? (
                      <div
                        style={{
                          height: !isSmallMobile ? "45%" : "300px",
                          marginBottom: "5%",
                        }}
                      >
                        <MyProjectCard
                          name={project.name}
                          lab={formatLab(project.lab)}
                          members={project.numberOfMembers}
                          maxMembers={project.maxMembers}
                          startDate={project.startDate}
                          endDate={project.finalDate}
                          progress={project.percentageDone}
                          status={project.status}
                          typeMember={project.userInProjectType}
                          systemProjectName={project.systemProjectName}
                          photo={project.photo}
                        ></MyProjectCard>
                      </div>
                    ) : (
                      index === 3 && (
                        <div
                          style={{ height: !isSmallMobile ? "45%" : "300px" }}
                        >
                          <MyProjectCard
                            name={project.name}
                            lab={formatLab(project.lab)}
                            members={project.numberOfMembers}
                            maxMembers={project.maxMembers}
                            startDate={project.startDate}
                            endDate={project.finalDate}
                            progress={project.percentageDone}
                            status={project.status}
                            typeMember={project.userInProjectType}
                            systemProjectName={project.systemProjectName}
                            photo={project.photo}
                          ></MyProjectCard>
                        </div>
                      )
                    );
                  })}
                  {isTablet &&
                    !isSmallMobile &&
                    projects.slice(2, 6).map((project, index) => {
                      return (
                        index === 3 && (
                          <div
                            style={{
                              height: !isSmallMobile ? "45%" : "300px",
                              marginBottom: "5%",
                            }}
                          >
                            <MyProjectCard
                              name={project.name}
                              lab={formatLab(project.lab)}
                              members={project.numberOfMembers}
                              maxMembers={project.maxMembers}
                              startDate={project.startDate}
                              endDate={project.finalDate}
                              progress={project.percentageDone}
                              status={project.status}
                              typeMember={project.userInProjectType}
                              systemProjectName={project.systemProjectName}
                              photo={project.photo}
                            ></MyProjectCard>
                          </div>
                        )
                      );
                    })}
                </Col>
                {(!isTablet || isSmallMobile) && (
                  <Col xl={4}>
                    {projects.slice(2, 6).map((project, index) => {
                      return index === 0 ? (
                        <div
                          style={{
                            height: !isSmallMobile ? "45%" : "300px",
                            marginBottom: "5%",
                          }}
                        >
                          <MyProjectCard
                            name={project.name}
                            lab={formatLab(project.lab)}
                            members={project.numberOfMembers}
                            maxMembers={project.maxMembers}
                            startDate={project.startDate}
                            endDate={project.finalDate}
                            progress={project.percentageDone}
                            status={project.status}
                            typeMember={project.userInProjectType}
                            systemProjectName={project.systemProjectName}
                            photo={project.photo}
                          ></MyProjectCard>
                        </div>
                      ) : (
                        index === 3 && (
                          <div
                            style={{
                              height: !isSmallMobile ? "45%" : "300px",
                            }}
                          >
                            <MyProjectCard
                              name={project.name}
                              lab={formatLab(project.lab)}
                              members={project.numberOfMembers}
                              maxMembers={project.maxMembers}
                              startDate={project.startDate}
                              endDate={project.finalDate}
                              progress={project.percentageDone}
                              status={project.status}
                              typeMember={project.userInProjectType}
                              systemProjectName={project.systemProjectName}
                              photo={project.photo}
                            ></MyProjectCard>
                          </div>
                        )
                      );
                    })}
                  </Col>
                )}
              </Row>
            </Col>
            <Col
              xs={1}
              sm={1}
              className="align-center"
              style={{ justifyContent: "center" }}
            >
              {" "}
              {currentPage < totalPages && (
                <button className="btn-arrow" onClick={handleNextClick}>
                  <i className="fas fa-chevron-right fa-3x "></i>
                </button>
              )}
            </Col>
          </Row>
        </Container>
      </div>
    </>
  );
}
