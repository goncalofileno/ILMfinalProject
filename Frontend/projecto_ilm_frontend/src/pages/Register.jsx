import "./App.css";
import { useNavigate } from "react-router-dom";
import RegisterForm from "../components/forms/RegisterForm";
import LoginHeader from "../components/headers/LoginHeader";
import { useEffect, useRef, useState } from "react";
import LoginProjectsCards from "../components/cards/LoginProjectsCards";
import { Row, Col, Container, Alert } from "react-bootstrap";
import { useMediaQuery } from "react-responsive";
import ForgetPassModal from "../components/modals/ForgetPassModal";
import alertStore from "../stores/alertStore";
import { getHomeProjects } from "../utilities/services";
import { Trans, t } from "@lingui/macro";

function App() {
  const navigate = useNavigate();
  const contentRef = useRef(null);
  const loginFormRef = useRef(null);
  const isComputer = useMediaQuery({ minWidth: 1200 });
  const isTablet = useMediaQuery({ minWidth: 992, maxWidth: 1200 });
  const isSmallTablet = useMediaQuery({ minWidth: 768, maxWidth: 992 });
  const isPhone = useMediaQuery({ maxWidth: 768 });
  const [homeProjects, setHomeProjects] = useState([]);
  const [visibleProjects, setVisibleProjects] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [page, setPage] = useState(null);
  const [maxPage, setMaxPage] = useState(null);
  const [showAlert, setShowAlert] = useState(false);
  const [isModalActive, setIsModalActive] = useState(false);
  const { visibility, type, message, setVisibility } = alertStore();
  const [registerMessage, setRegisterMessage] = useState("");
  const [registerMessageType, setRegisterMessageType] = useState("");
  const headerHeight = 110;

  useEffect(() => {
    getHomeProjects()
      .then((response) => response.json())
      .then((data) => {
        setHomeProjects(data);
        setVisibleProjects(data);
        setMaxPage(Math.ceil(data.length / 8) - 1);
        setPage(0);
      });
  }, []);

  useEffect(() => {
    if (searchTerm === "") {
      setVisibleProjects(homeProjects);
      setMaxPage(Math.ceil(homeProjects.length / 8) - 1);
      setPage(0);
    } else {
      const filteredProjects = homeProjects.filter(
        (project) =>
          project.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
          project.description.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setVisibleProjects(filteredProjects);
      const potentialMaxPage = Math.ceil(filteredProjects.length / 8) - 1;
      if (potentialMaxPage >= 0) {
        setMaxPage(potentialMaxPage);
      } else setMaxPage(0);
      setPage(0);
    }
  }, [searchTerm]);

  const scrollToContent = () => {
    contentRef.current.scrollIntoView({ behavior: "smooth", block: "start" });
  };

  const scrollToLoginForm = () => {
    loginFormRef.current.scrollIntoView({ behavior: "smooth", block: "start" });
    setTimeout(() => {
      window.scrollBy(0, -headerHeight);
    }, 500);
  };

  const scrollToLogin = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth", // Smooth scroll effect
    });
  };

  useEffect(() => {
    if (visibility === "visible") {
      setTimeout(() => {
        setVisibility("hidden");
      }, 3000);
    }
  }, [visibility]);

  useEffect(() => {
    if (showAlert === true) {
      setTimeout(() => {
        setShowAlert(false);
      }, 3000);
    }
  }, [showAlert]);

  const handlePrevious = () => {
    setPage(page - 1);
  };

  const handleNext = () => {
    setPage(page + 1);
  };

  return (
    <>
      <LoginHeader />
      <ForgetPassModal
        isModalActive={isModalActive}
        setIsModalActive={setIsModalActive}
      ></ForgetPassModal>
      <Alert
        variant={type}
        className="alerts-message"
        style={{ visibility: visibility, zIndex: "1005" }}
      >
        {message}
      </Alert>
      <div className="page-content">
        <div
          className={
            isComputer
              ? "ilm-page"
              : isTablet
              ? "ilm-pageb"
              : "ilm-page-noheight"
          }
        >
          <Container fluid className="outer-container">
            <Row className="outer-row">
              <Col xl={8} lg={6} sm={12}>
                <Row>
                  <Col xl={6} lg={12} md={6} sm={6}>
                    <h1
                      className="app-slogan app-slogan-1"
                      style={{
                        textAlign: isSmallTablet && "end",
                        marginTop: isPhone && "10px",
                        marginBottom: isPhone && "0px",
                      }}
                    >
                      <Trans>Where Ideas</Trans>
                    </h1>
                  </Col>
                  <Col xl={6} lg={12} md={6} sm={6}>
                    <h1
                      className="app-slogan app-slogan-2"
                      style={{
                        textAlign: (isTablet || isPhone) && "center",
                        marginTop: isPhone && "10px",
                        marginBottom: isPhone && "0px",
                      }}
                    >
                      <Trans>Take Flight</Trans>
                    </h1>
                  </Col>
                </Row>
                <Row>
                  {isComputer && <Col lg={6}></Col>}

                  <Col>
                    {" "}
                    <div className="ilm-description">
                      <Trans>
                        Welcome to our Innovation Lab Management app, where
                        creativity meets collaboration and ideas turn into
                        reality. With our app, you can manage and track your
                        projects seamlessly, from brainstorming to
                        implementation, ensuring every idea has the potential to
                        flourish. Join us in driving progress and inspiring
                        change, one innovative project at a time.
                      </Trans>
                    </div>
                    {!isSmallTablet && !isPhone ? (
                      <div className="div-project-button">
                        <button
                          className="submit-button"
                          id="projects-button"
                          onClick={scrollToContent}
                        >
                          <Trans>Some of our Projects</Trans>
                        </button>
                      </div>
                    ) : (
                      <div className="div-see-login-button">
                        <button
                          className="submit-button"
                          id="loginForm-button"
                          onClick={scrollToLoginForm}
                        >
                          Login
                        </button>
                      </div>
                    )}
                  </Col>
                </Row>
              </Col>
              <Col
                xl={4}
                lg={6}
                className="align-items-center"
                style={{ marginTop: (isPhone || isSmallTablet) && "30px" }}
                ref={loginFormRef}
              >
                <Alert
                  variant={registerMessageType}
                  id="alert-message-register"
                  style={{ visibility: showAlert ? "visible" : "hidden" }}
                >
                  {registerMessage}
                </Alert>
                <RegisterForm
                  setShowAlert={setShowAlert}
                  setRegisterMessage={setRegisterMessage}
                  setRegisterMessageType={setRegisterMessageType}
                />
              </Col>
              {isPhone && (
                <Col>
                  <div className="div-project-button">
                    <button
                      className="submit-button"
                      id="projects-button"
                      onClick={scrollToContent}
                    >
                      <Trans>Some of our projects</Trans>
                    </button>
                  </div>
                </Col>
              )}
            </Row>
          </Container>
        </div>
        <div
          className="ilm-page2"
          ref={contentRef}
          style={{ paddingTop: "60px" }}
        >
          <Container fluid className="outer-container">
            <Row>
              <Col xs={1} sm={1}></Col>
              <Col xs={10} sm={3}>
                <h1 className="ilm-general-subTitle">
                  <Trans>Projects</Trans>
                </h1>
              </Col>
              {(isSmallTablet || isPhone) && <Col xs={1} sm={1}></Col>}

              <Col
                xs={12}
                sm={7}
                lg={8}
                className="align-center"
                style={{
                  justifyContent: isPhone && "center",
                  marginTop: "30px",
                }}
              >
                <input
                  type="text"
                  style={{ width: !isPhone ? "50%" : "74%" }}
                  className="ilm-search"
                  placeholder={t`Search for title or description`}
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </Col>
            </Row>
            <Row className="row-margin-top-20 last-row-padd-bott">
              <Col
                sm={1}
                xs={1}
                className="align-center"
                style={{ justifyContent: "center" }}
              >
                {page > 0 && (
                  <button className="btn-arrow" onClick={handlePrevious}>
                    <i class="fas fa-chevron-left fa-3x"></i>
                  </button>
                )}
              </Col>
              <Col sm={10} xs={10}>
                <Row>
                  <Col
                    lg={3}
                    md={6}
                    className={
                      !isComputer && !isTablet && "col-margin-bottom-16"
                    }
                  >
                    <div className="flex-col-gap-16">
                      {visibleProjects
                        .slice(0 + 8 * page, 2 + 8 * page)
                        .map((component, index) => {
                          return index % 2 === 0 ? (
                            <LoginProjectsCards
                              key={component.index}
                              cardBkgColor="card-bkg-color1"
                              title={component.name}
                              description={component.description}
                              scrollToRef={scrollToLogin}
                            />
                          ) : (
                            <LoginProjectsCards
                              key={component.index}
                              cardBkgColor="card-bkg-color1"
                              title={component.name}
                              description={component.description}
                              scrollToRef={scrollToLogin}
                            />
                          );
                        })}
                    </div>
                  </Col>

                  <Col
                    lg={3}
                    md={6}
                    className={
                      !isComputer && !isTablet && "col-margin-bottom-16"
                    }
                  >
                    <div className="flex-col-gap-16">
                      {visibleProjects
                        .slice(2 + 8 * page, 4 + 8 * page)
                        .map((component, index) => {
                          return index % 2 === 0 ? (
                            <LoginProjectsCards
                              key={component.index}
                              cardBkgColor="card-bkg-color2"
                              title={component.name}
                              description={component.description}
                              scrollToRef={scrollToLogin}
                            />
                          ) : (
                            <LoginProjectsCards
                              key={component.index}
                              cardBkgColor="card-bkg-color2"
                              title={component.name}
                              description={component.description}
                              scrollToRef={scrollToLogin}
                            />
                          );
                        })}
                    </div>
                  </Col>
                  <Col
                    lg={3}
                    md={6}
                    className={
                      !isComputer && !isTablet && "col-margin-bottom-16"
                    }
                  >
                    <div className="flex-col-gap-16">
                      {visibleProjects
                        .slice(4 + 8 * page, 6 + 8 * page)
                        .map((component, index) => {
                          return index % 2 === 0 ? (
                            <LoginProjectsCards
                              key={component.index}
                              cardBkgColor="card-bkg-color3"
                              title={component.name}
                              description={component.description}
                              scrollToRef={scrollToLogin}
                            />
                          ) : (
                            <LoginProjectsCards
                              key={component.index}
                              cardBkgColor="card-bkg-color3"
                              title={component.name}
                              description={component.description}
                              scrollToRef={scrollToLogin}
                            />
                          );
                        })}
                    </div>
                  </Col>
                  <Col
                    lg={3}
                    md={6}
                    className={
                      !isComputer && !isTablet && "col-margin-bottom-16"
                    }
                  >
                    <div className="flex-col-gap-16">
                      {visibleProjects
                        .slice(6 + 8 * page, 8 + 8 * page)
                        .map((component, index) => {
                          return index % 2 === 0 ? (
                            <LoginProjectsCards
                              key={component.index}
                              cardBkgColor="card-bkg-color4"
                              title={component.name}
                              description={component.description}
                              scrollToRef={scrollToLogin}
                            />
                          ) : (
                            <LoginProjectsCards
                              key={component.index}
                              cardBkgColor="card-bkg-color4"
                              title={component.name}
                              description={component.description}
                              scrollToRef={scrollToLogin}
                            />
                          );
                        })}
                    </div>
                  </Col>
                </Row>
              </Col>

              <Col
                sm={1}
                xs={1}
                className="align-center"
                style={{ justifyContent: "center" }}
              >
                {page != maxPage && page != null && (
                  <button className="btn-arrow" onClick={handleNext}>
                    {" "}
                    <i class="fas fa-chevron-right fa-3x "></i>
                  </button>
                )}
              </Col>
            </Row>
          </Container>
        </div>
      </div>
    </>
  );
}

export default App;
