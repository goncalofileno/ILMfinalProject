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
  checkProjectName,
} from "../utilities/services";
import { formatLab } from "../utilities/converters";
import defaultPhoto from "../resources/avatares/defaultProjectAvatar.jpg";
import StandardModal from "../components/modals/StandardModal";
import { useNavigate } from "react-router-dom";

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
  const [modalActive, setModalActive] = useState(false);
  const [modalType, setModalType] = useState("success");
  const [modalMessage, setModalMessage] = useState("");
  const [warningType, setWarningType] = useState("");
  const [warningTxt, setWarningTxt] = useState("");

  const navigate = useNavigate();

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
      if (startDate < endDate) {
        if (startDate > new Date().toISOString().split("T")[0]) {
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
              response.json().then((data) => {
                if (selectedFile !== null) {
                  uploadProjectPhoto(selectedFile, projectName).then(() => {
                    setModalMessage("Project created successfully");
                    setModalType("success");
                    setModalActive(true);

                    setTimeout(() => {
                      navigate(`/create-project/${data.systemName}/members`);
                    }, 1200);
                  });
                } else {
                  setModalMessage("Project created successfully");
                  setModalType("success");
                  setModalActive(true);
                  setTimeout(() => {
                    navigate(`/create-project/${data.systemName}/members`);
                  }, 1200);
                }
              });
            } else {
              setModalMessage("Error while creating project");
              setModalType("danger");
              setModalActive(true);
            }
          });
        } else {
          setModalMessage("Start date must be after today's date");
          setModalType("danger");
          setModalActive(true);
        }
      } else {
        setModalMessage("End date must be after start date");
        setModalType("danger");
        setModalActive(true);
      }
    } else {
      setModalMessage("Please fill all the fields");
      setModalType("danger");
      setModalActive(true);
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

  const handleOnBlur = () => {
    if (projectName !== "") {
      checkProjectName(projectName).then((response) => {
        if (response.status === 200) {
          setWarningType("success");
          setWarningTxt("Project name available");
        } else if (response.status === 409) {
          setWarningType("incorrect");
          setWarningTxt("Project name already exists");
        } else if (response.status === 400) {
          setWarningType("incorrect");
          if (projectName.length < 3)
            setWarningTxt("Name has to be at least 3 characters long");
          else if (projectName.length > 35)
            setWarningTxt("Name has to be at most 35 characters long");
          else setWarningTxt("Invalid project name");
        }
      });
    } else {
      setWarningType("");
      setWarningTxt("");
    }
  };
  return (
    <>
      <AppNavbar />
      <StandardModal
        modalType={modalType}
        message={modalMessage}
        modalActive={modalActive}
        setModalActive={setModalActive}
      ></StandardModal>
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
                  warningType={warningType}
                  warningTxt={warningTxt}
                  handleOnBlur={handleOnBlur}
                  onBlurActive={true}
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
          <Col sm={5}></Col>
          <Col sm={6}>
            <div style={{ display: "flex", flexDirection: "row", gap: "7%" }}>
              <button
                className="secondary-button"
                style={{ width: "20%" }}
                onClick={() => navigate("/projects")}
              >
                Cancel
              </button>
              <button
                className="submit-button"
                id="submit-creation-project-btn"
                onClick={handleClick}
                disabled={
                  projectName !== "" &&
                  description !== "" &&
                  motivation !== "" &&
                  startDate !== "" &&
                  endDate !== "" &&
                  selectedInterests.length > 0 &&
                  warningType === "success"
                    ? false
                    : true
                }
              >
                Create Project
              </button>
            </div>
          </Col>
          <Col sm={1}></Col>
        </Row>
      </div>
    </>
  );
}
