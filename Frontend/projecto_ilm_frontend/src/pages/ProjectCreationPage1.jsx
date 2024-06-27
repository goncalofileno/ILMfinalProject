import "./ProjectCreationPage.css";
import AppNavbar from "../components/headers/AppNavbar";
import { Row, Col, Form, Image } from "react-bootstrap";
import InputForm from "../components/inputs/InputForm";

export default function ProjectCreationPage1() {
  return (
    <>
      <AppNavbar />
      <div className="ilm-pageb">
        <h1 className="page-title">
          <span className="app-slogan-1">Project </span>
          <span className="app-slogan-2">Creation</span>
        </h1>
        <Row className="project-creation-page">
          <Col sm={5} className="col-project-creation">
            <div className="div-half-col" style={{ height: "50%" }}>
              <label className="custom-label">Project Image</label>
              <div className="div-img-project">
                <Image
                  src="https://cdn.pixabay.com/photo/2016/03/29/08/48/project-1287781_1280.jpg"
                  className="project-creation-image"
                  fluid
                />
                <Form.Group controlId="formFileUpload">
                  <Form.Control
                    type="file"
                    className="custom-focus"
                    style={{
                      borderTopLeftRadius: "0",
                      borderTopRightRadius: "0",
                    }}
                  />
                </Form.Group>
              </div>
            </div>
            <div className="div-half-col ">
              <label htmlFor="motivation" className="custom-label">
                Motivation
              </label>
              <textarea
                name="motivation"
                id="motivation"
                className="text-area-project-creation"
              ></textarea>
            </div>
          </Col>
          <Col sm={7} className="col-project-creation">
            <Row className="row-display">
              <div style={{ width: "60%" }}>
                <InputForm label="Project Name"></InputForm>
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  width: "30%",
                }}
              >
                <div className="lab-drop-down-div">
                  <label htmlFor="lab-drop-down" className="custom-label">
                    Laboratory
                  </label>
                  <Form.Control as="select" className="custom-focus">
                    <option value="lab1">Lab 1</option>
                    <option value="lab2">Lab 2</option>
                    <option value="lab3">Lab 3</option>
                  </Form.Control>
                </div>
              </div>
            </Row>
            <Row style={{ width: "100%", marginTop: "3%", height: "38%" }}>
              <div className="display-column">
                <label htmlFor="description" className="custom-label">
                  Description
                </label>
                <textarea
                  name="description"
                  id="description"
                  className="text-area-project-creation"
                ></textarea>
              </div>
            </Row>
          </Col>
        </Row>
      </div>
    </>
  );
}
