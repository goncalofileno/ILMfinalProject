import AppNavbar from "../components/headers/AppNavbar";
import "./ProjectCreationPage.css";
import { Row, Col, InputGroup, Form, Button } from "react-bootstrap";
import ResourcesProjectCreationTable from "../components/tables/ResourcesProjectCreationTable";
import YourResourcesProjectCreationTable from "../components/tables/YourResourcesProjectCreationTable";
import TablePagination from "../components/paginations/TablePagination";

export default function ProjectCreationPage3() {
  return (
    <div>
      <AppNavbar />
      <div className="ilm-pageb">
        <h1 className="page-title">
          <span className="app-slogan-1">Project </span>
          <span className="app-slogan-2">Resources</span>
        </h1>
        <Row className="row-container2">
          <Col sm={1}></Col>
          <Col sm={5}>
            <Row style={{ height: "100%" }}>
              <Row
                style={{ height: "17%", marginBottom: "0.5%" }}
                className="justify-align-content"
              >
                <Col sm={8}>
                  <InputGroup className="gap-10px">
                    <Form.Control
                      type="text"
                      placeholder="Search for resource"
                      style={{ borderRadius: "10px", cursor: "text" }}
                      className="custom-focus"
                    />
                    <Button variant="primary" id="primary-btn-boot">
                      <i class="fas fa-search"></i>
                    </Button>
                    <Button
                      variant="secondary"
                      style={{ borderRadius: "10px" }}
                    >
                      Clear
                    </Button>
                  </InputGroup>
                </Col>
                <Col sm={3}>
                  <Form.Control
                    as="select"
                    className="custom-focus"
                    style={{ marginBottom: "5px" }}
                  >
                    {" "}
                    <option value="">All Brands</option>
                  </Form.Control>
                  <Form.Control as="select" className="custom-focus">
                    {" "}
                    <option value="">All Suppliers</option>
                  </Form.Control>
                </Col>
                <Col s={1}></Col>
              </Row>
              <Row style={{ height: "83%" }}>
                <Col sm={11}>
                  <ResourcesProjectCreationTable></ResourcesProjectCreationTable>
                </Col>
                <Col sm={1}></Col>
              </Row>
            </Row>
          </Col>

          <Col sm={5}>
            <Row style={{ height: "100%" }}>
              <Row style={{ height: "17%", marginBottom: "0.5%" }}>
                <Col sm={1}></Col>
                <Col sm={11}>
                  <h4 className="h4-resources-project-creat">Your Resources</h4>
                </Col>
              </Row>
              <Row style={{ height: "83%" }}>
                <Col sm={1}></Col>
                <Col sm={11}>
                  <YourResourcesProjectCreationTable></YourResourcesProjectCreationTable>
                </Col>
              </Row>
            </Row>
          </Col>
          <Col sm={1}></Col>
        </Row>
        <Row className="last-row-resources-creation">
          <Col sm={1}></Col>
          <Col sm={3} className="table-resources-pagination">
            <TablePagination></TablePagination>
          </Col>
          <Col sm={3}></Col>
          <Col sm={4}>
            <button className="submit-button" style={{ width: "100%" }}>
              Finish Project Creation
            </button>
          </Col>
          <Col sm={1}></Col>
        </Row>
      </div>
    </div>
  );
}
