import AppNavbar from "../components/headers/AppNavbar";
import AsideMyProjectsPage from "../components/asides/AsideMyProjectsPage";
import MyProjectCard from "../components/cards/MyProjectCard";
import "./MyProjectsPage.css";
import { Col, Row, Container } from "react-bootstrap";
export default function MyProjectsPage() {
  return (
    <>
      <AppNavbar />
      <AsideMyProjectsPage />
      <div className="ilm-pageb-with-aside">
        <h1 className="page-title">
          <span className="app-slogan-1">My </span>
          <span className="app-slogan-2">Projects</span>
        </h1>
        <Container fluid className="my-projects-container">
          <Row style={{ height: "100%" }}>
            <Col
              sm={1}
              className="align-center"
              style={{ justifyContent: "center" }}
            >
              {" "}
              <button className="btn-arrow">
                <i className="fas fa-chevron-left fa-3x ilm-arrow"></i>
              </button>
            </Col>
            <Col sm={10} style={{ height: "100%" }}>
              <Row style={{ height: "100%" }}>
                <Col style={{ height: "100%" }}>
                  <div style={{ height: "45%", marginBottom: "10%" }}>
                    <MyProjectCard></MyProjectCard>
                  </div>
                  <div style={{ height: "45%" }}>
                    <MyProjectCard></MyProjectCard>
                  </div>
                </Col>
                <Col>
                  <div style={{ height: "45%", marginBottom: "10%" }}>
                    <MyProjectCard></MyProjectCard>
                  </div>
                  <div style={{ height: "45%" }}>
                    <MyProjectCard></MyProjectCard>
                  </div>
                </Col>
                <Col>
                  <div style={{ height: "45%", marginBottom: "10%" }}>
                    <MyProjectCard></MyProjectCard>
                  </div>
                  <div style={{ height: "45%" }}>
                    <MyProjectCard></MyProjectCard>
                  </div>
                </Col>
              </Row>
            </Col>
            <Col
              sm={1}
              className="align-center"
              style={{ justifyContent: "center" }}
            >
              {" "}
              <button className="btn-arrow">
                <i className="fas fa-chevron-right fa-3x ilm-arrow"></i>
              </button>
            </Col>
          </Row>
        </Container>
      </div>
    </>
  );
}
