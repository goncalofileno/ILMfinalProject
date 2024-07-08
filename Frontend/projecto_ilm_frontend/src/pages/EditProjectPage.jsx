import "./ProjectCreationPage.css";
import AppNavbar from "../components/headers/AppNavbar";
import { Row, Col, Form, Image } from "react-bootstrap";
import InputForm from "../components/inputs/InputForm";
import InterestSelector from "../components/selectors/InterestSelector";
import { useState, useEffect } from "react";
import SkillSelector from "../components/selectors/SkillSelector";
import {
  getLabsWithSessionId,
  getProjectDetails,
  uploadProjectPhoto,
  checkProjectName,
  updateProject, // Import the update project function
} from "../utilities/services";
import { formatLab } from "../utilities/converters";
import defaultPhoto from "../resources/avatares/defaultProjectAvatar.jpg";
import StandardModal from "../components/modals/StandardModal";
import { useNavigate, useParams } from "react-router-dom";
import { Trans, t } from "@lingui/macro";
import { useMediaQuery } from "react-responsive";
import e from "cors";

export default function EditProjectPage() {
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
  const [maxMembers, setMaxMembers] = useState(0);
  const [isFormModified, setIsFormModified] = useState(false); // New state for form modification tracking
  const isTablet = useMediaQuery({ query: "(max-width: 991px)" });
  const isMobile = useMediaQuery({ query: "(max-width: 575px)" });
  const { systemProjectName } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    getLabsWithSessionId().then((response) => {
      return response
        .json()
        .then((data) => {
          setLabs(data);
        })
        .catch((error) => {
          console.log("Error while fetching labs: ", error);
        });
    });

    getProjectDetails(systemProjectName).then((data) => {
      if (data) {
        setProjectName(data.name);
        setDescription(data.description);
        setMotivation(data.motivation);
        setStartDate(formatDateForInput(data.startDate));
        setEndDate(formatDateForInput(data.endDate));
        setSelectedLab(data.lab);
        setSelectedInterests(data.keywords);
        setSelectedSkills(data.skills);
        setMaxMembers(data.maxMembers);
        if (data.photo) {
          setPreview(data.photo);
        }
      }
    });
  }, [systemProjectName]);

  const formatDateForInput = (dateString) => {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const handleInputChange = (setter) => (event) => {
    setter(event.target.value);
    setIsFormModified(true);
  };

  const handleClick = () => {
    if (
      projectName !== "" &&
      description !== "" &&
      motivation !== "" &&
      startDate !== "" &&
      endDate !== "" &&
      selectedInterests.length > 0
    ) {
      const today = new Date().toISOString().split("T")[0]; // Get today's date as YYYY-MM-DD string
      const startDateObj = new Date(startDate).toISOString().split("T")[0];
      const endDateObj = new Date(endDate).toISOString().split("T")[0];

      if (startDateObj < endDateObj) {
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

        updateProject(project, systemProjectName).then((response) => {
          if (response.success) {
            if (selectedFile !== null) {
              uploadProjectPhoto(selectedFile, projectName).then(() => {
                setModalMessage(t`Project updated successfully`);
                setModalType("success");
                setModalActive(true);

                setTimeout(() => {
                  navigate(`/project/${systemProjectName}/info`);
                }, 1200);
              });
            } else {
              setModalMessage(t`Project updated successfully`);
              setModalType("success");
              setModalActive(true);
              setTimeout(() => {
                navigate(`/project/${systemProjectName}/info`);
              }, 1200);
            }
          } else {
            setModalMessage(t`Error while updating project`);
            setModalType("danger");
            setModalActive(true);
          }
        });
      } else {
        setModalMessage(t`End date must be after start date`);
        setModalType("danger");
        setModalActive(true);
      }
    } else {
      setModalMessage(t`Please fill all the fields`);
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
      setIsFormModified(true);
    }
  };

  const handleOnBlur = () => {
    if (projectName !== "") {
      checkProjectName(projectName).then((response) => {
        if (response.status === 200) {
          setWarningType("success");
          setWarningTxt(t`Project name available`);
        } else if (response.status === 409) {
          setWarningType("incorrect");
          setWarningTxt(t`Project name already exists`);
        } else if (response.status === 400) {
          setWarningType("incorrect");
          if (projectName.length < 3)
            setWarningTxt(t`Name has to be at least 3 characters long`);
          else if (projectName.length > 35)
            setWarningTxt(t`Name has to be at most 35 characters long`);
          else setWarningTxt("Invalid project name");
        }
      });
    } else {
      setWarningType("");
      setWarningTxt("");
    }
  };

  const getNextDay = (date) => {
    if (!date) return "";
    const nextDay = new Date(date);
    nextDay.setDate(nextDay.getDate() + 1);
    return nextDay.toISOString().split("T")[0];
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
      <div
        className={!isTablet ? "ilm-pageb" : "ilm-page-mobile"}
        style={{ paddingTop: "15px" }}
      >
        <h1 className="page-title">
          <span className="app-slogan-1">
            <Trans>Edit</Trans>{" "}
          </span>
          <span className="app-slogan-2">
            <Trans>Project</Trans>
          </span>
        </h1>
        <Row className="project-creation-page">
          {isTablet && (
            <Row
              className="row-display"
              style={{ marginTop: "30px", marginBottom: "30px" }}
            >
              <Col xs={6} sm={5}>
                <InputForm
                  label="Project Name"
                  value={projectName}
                  setValue={setProjectName}
                  warningType={warningType}
                  warningTxt={warningTxt}
                  handleOnBlur={handleOnBlur}
                  onBlurActive={true}
                  onChange={handleInputChange(setProjectName)} // Use handleInputChange directly here
                ></InputForm>
              </Col>
              <Col xs={5} sm={2}>
                <div className="lab-drop-down-div">
                  <label htmlFor="lab-drop-down" className="custom-label">
                    <Trans>Laboratory</Trans>
                  </label>
                  <Form.Control
                    as="select"
                    className="custom-focus"
                    value={selectedLab}
                    onChange={handleInputChange(setSelectedLab)}
                  >
                    {labs.map((lab) => (
                      <option key={lab.local} value={lab.local}>
                        {formatLab(lab.local)}
                      </option>
                    ))}
                  </Form.Control>
                </div>
              </Col>
              <Col xs={6} sm={2}>
                <div className="lab-drop-down-div">
                  <label htmlFor="lab-drop-down" className="custom-label">
                    <Trans>Start Date</Trans>
                  </label>
                  <input
                    type="date"
                    className="date-input"
                    value={startDate}
                    onChange={handleInputChange(setStartDate)}
                  ></input>
                </div>
              </Col>

              <Col xs={5} sm={2}>
                <div className="lab-drop-down-div">
                  <label htmlFor="lab-drop-down" className="custom-label">
                    <Trans>End Date</Trans>
                  </label>
                  <input
                    type="date"
                    className="date-input"
                    value={endDate}
                    onChange={handleInputChange(setEndDate)}
                    min={getNextDay(startDate)} // Set minimum date for end date
                  ></input>
                </div>
              </Col>
            </Row>
          )}
          <Col lg={5} className="col-project-creation">
            <div className="div-half-col" style={{ height: "50%" }}>
              <label className="custom-label">
                <Trans>Project Image</Trans>
              </label>
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
            <div
              className="div-half-col "
              style={{
                marginBottom: isTablet && "30px",
                marginTop: isTablet && "30px",
              }}
            >
              <label htmlFor="motivation" className="custom-label">
                <Trans>Motivation</Trans>
              </label>
              <textarea
                name="motivation"
                id="motivation"
                className="text-area-project-creation"
                value={motivation}
                onChange={handleInputChange(setMotivation)}
              ></textarea>
            </div>
          </Col>
          <Col lg={7} className="col-project-creation">
            {!isTablet && (
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
                    onChange={handleInputChange(setProjectName)} // Use handleInputChange directly here
                  ></InputForm>
                </Col>
                <Col sm={2}>
                  <div className="lab-drop-down-div">
                    <label htmlFor="lab-drop-down" className="custom-label">
                      <Trans>Laboratory</Trans>
                    </label>
                    <Form.Control
                      as="select"
                      className="custom-focus"
                      value={selectedLab}
                      onChange={handleInputChange(setSelectedLab)}
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
                      <Trans>Start Date</Trans>
                    </label>
                    <input
                      type="date"
                      className="date-input"
                      value={startDate}
                      onChange={handleInputChange(setStartDate)}
                    ></input>
                  </div>
                </Col>
                <Col sm={2}>
                  <div className="lab-drop-down-div">
                    <label htmlFor="lab-drop-down" className="custom-label">
                      <Trans>End Date</Trans>
                    </label>
                    <input
                      type="date"
                      className="date-input"
                      value={endDate}
                      onChange={handleInputChange(setEndDate)}
                      min={getNextDay(startDate)} // Set minimum date for end date
                    ></input>
                  </div>
                </Col>
              </Row>
            )}
            <Row
              style={{
                width: "100%",
                height: "32%",
                marginBottom: isTablet && "30px",
              }}
            >
              <Col sm={9} style={{ height: "100%" }}>
                <div
                  className="display-column"
                  style={{ width: isTablet && "100%", height: "100%" }}
                >
                  <label htmlFor="description" className="custom-label">
                    <Trans>Description</Trans>
                  </label>
                  <textarea
                    name="description"
                    id="description"
                    className="text-area-project-creation"
                    value={description}
                    onChange={handleInputChange(setDescription)}
                  ></textarea>
                </div>
              </Col>
              <Col
                sm={2}
                id="column-quantity"
                style={{ marginTop: isMobile && "30px" }}
              >
                <div className="display-column">
                  <label
                    for="quantity"
                    style={{ fontSize: "18px", fontWeight: "500" }}
                  >
                    Max Members:
                  </label>
                  <input
                    type="number"
                    id="quantityInput"
                    className="date-input"
                    name="quantity"
                    min="1"
                    max="30"
                    value={maxMembers}
                    onChange={(e) => {
                      setMaxMembers(e.target.value);
                      setIsFormModified(true);
                    }}
                  />
                </div>
              </Col>
              <Col sm={1}></Col>
            </Row>
            <Row
              style={{
                display: "flex",
                flexDirection: "row",
                width: "100%",
                gap: !isTablet ? "6%" : "10%",
                marginBottom: isTablet && "30px",
              }}
            >
              <Col sm={6} style={{ width: "45%" }}>
                <InterestSelector
                  label={t`Keywords:`}
                  selectedInterests={selectedInterests}
                  setSelectedInterests={(interests) => {
                    setSelectedInterests(interests);
                    setIsFormModified(true);
                  }}
                ></InterestSelector>
              </Col>
              <Col sm={6} style={{ width: "45%" }}>
                <SkillSelector
                  selectedSkills={selectedSkills}
                  setSelectedSkills={(skills) => {
                    setSelectedSkills(skills);
                    setIsFormModified(true);
                  }}
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
                onClick={() => navigate(`/project/${systemProjectName}/info`)}
              >
                <Trans>Cancel</Trans>
              </button>
              <button
                className="submit-button"
                id="submit-creation-project-btn"
                onClick={handleClick}
                disabled={!isFormModified} // Enable button only if the form is modified
              >
                <Trans>Update Project</Trans>
              </button>
            </div>
          </Col>
          <Col sm={1}></Col>
        </Row>
      </div>
    </>
  );
}
