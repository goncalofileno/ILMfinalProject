import "./ProjectCreationPage.css";
import AppNavbar from "../components/headers/AppNavbar";
import { Row, Col, Form, Image } from "react-bootstrap";
import InputForm from "../components/inputs/InputForm";
import InterestSelector from "../components/selectors/InterestSelector";
import { useState, useEffect } from "react";
import SkillSelector from "../components/selectors/SkillSelector";
import {
  getLabsWithSessionId,
  createProject,
  uploadProjectPhoto,
} from "../utilities/services";
import { formatLab } from "../utilities/converters";
import defaultPhoto from "../resources/avatares/defaultProjectAvatar.jpg";
import StandardModal from "../components/modals/StandardModal";

export default function ProjectCreationPage1() {
  const [selectedInterests, setSelectedInterests] = useState([]);
  const [selectedSkills, setSelectedSkills] = useState([]);
  const [selectedLab, setSelectedLab] = useState("COIMBRA");
  const [motivation, setMotivation] = useState("");
  const [description, setDescription] = useState("");
  const [projectName, setProjectName] = useState("");
  const [labs, setLabs] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [preview, setPreview] = useState(null);

  useEffect(() => {
    getLabsWithSessionId().then((response) => {
      return response
        .json()
        .then((data) => {
          console.log("data : " + data);
          setLabs(data);
        })
        .catch((error) => {
          console.log("Error while fetching labs: ", error);
        });
    });
  }, []);

  const handleClick = () => {
    if (
      projectName !== "" &&
      description !== "" &&
      motivation !== "" &&
      startDate !== "" &&
      endDate !== "" &&
      selectedInterests.length > 0
    ) {
      if (selectedFile == null) {
        setSelectedFile(defaultPhoto);
      }
      const project = {
        name: projectName,
        lab: selectedLab,
        description: description,
        motivation: motivation,
        keywords: selectedInterests,
        skills: selectedSkills,
        startDate: startDate,
        endDate: endDate,
      };

      createProject(project).then((response) => {
        if (response.status === 200) {
          uploadProjectPhoto(selectedFile, projectName).then((response) => {
            if (response.status === 200) {
              console.log("Project created successfully");
            } else {
              console.log("Error while uploading project photo");
            }
          });
        } else {
          console.log("Error while creating project");
        }
      });
    } else {
      console.log("missing fields");
    }
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setSelectedFile(file);
    if (file) {
      const previewUrl = URL.createObjectURL(file);
      setPreview(previewUrl);
    }
  };

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
                {preview ? (
                  <Image
                    src={preview}
                    className="project-creation-image"
                    fluid
                  />
                ) : (
                  <Image
                    src={defaultPhoto}
                    className="project-creation-image"
                    fluid
                  />
                )}
                <Form.Group controlId="formFileUpload">
                  <Form.Control
                    type="file"
                    onChange={handleFileChange}
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
                value={motivation}
                onChange={(e) => setMotivation(e.target.value)}
              ></textarea>
            </div>
          </Col>
          <Col sm={7} className="col-project-creation">
            <Row className="row-display">
              <Col sm={5}>
                <InputForm
                  label="Project Name"
                  value={projectName}
                  setValue={setProjectName}
                ></InputForm>
              </Col>
              <Col sm={2}>
                <div className="lab-drop-down-div">
                  <label htmlFor="lab-drop-down" className="custom-label">
                    Laboratory
                  </label>
                  <Form.Control
                    as="select"
                    className="custom-focus"
                    value={selectedLab}
                    onChange={(e) => setSelectedLab(e.target.value)}
                  >
                    {labs.map((lab) => (
                      <option key={lab.local} value={lab.local}>
                        {formatLab(lab.local)}
                      </option>
                    ))}
                  </Form.Control>
                </div>
              </Col>
              <Col sm={2}>
                <div className="lab-drop-down-div">
                  <label htmlFor="lab-drop-down" className="custom-label">
                    Start Date
                  </label>
                  <input
                    type="date"
                    className="date-input"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                  ></input>
                </div>
              </Col>
              <Col sm={2}>
                <div className="lab-drop-down-div">
                  <label htmlFor="lab-drop-down" className="custom-label">
                    End Date
                  </label>
                  <input
                    type="date"
                    className="date-input"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                  ></input>
                </div>
              </Col>
            </Row>
            <Row style={{ width: "100%", height: "32%" }}>
              <div className="display-column">
                <label htmlFor="description" className="custom-label">
                  Description
                </label>
                <textarea
                  name="description"
                  id="description"
                  className="text-area-project-creation"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                ></textarea>
              </div>
            </Row>
            <Row
              style={{
                display: "flex",
                flexDirection: "row",
                width: "100%",
                gap: "6%",
              }}
            >
              <Col sm={6} style={{ width: "45%" }}>
                <InterestSelector
                  label="Keywords:"
                  selectedInterests={selectedInterests}
                  setSelectedInterests={setSelectedInterests}
                ></InterestSelector>
              </Col>
              <Col sm={6} style={{ width: "45%" }}>
                <SkillSelector
                  selectedSkills={selectedSkills}
                  setSelectedSkills={setSelectedSkills}
                ></SkillSelector>
              </Col>
            </Row>
          </Col>
        </Row>
        <Row style={{ marginRight: "0px" }}>
          <Col sm={7}></Col>
          <Col sm={4}>
            {projectName !== "" &&
              description !== "" &&
              motivation !== "" &&
              startDate !== "" &&
              endDate !== "" &&
              selectedInterests.length > 0 && (
                <button
                  className="submit-button"
                  id="submit-creation-project-btn"
                  onClick={handleClick}
                >
                  Create Project
                </button>
              )}
          </Col>
          <Col sm={1}></Col>
        </Row>
      </div>
    </>
  );
}
