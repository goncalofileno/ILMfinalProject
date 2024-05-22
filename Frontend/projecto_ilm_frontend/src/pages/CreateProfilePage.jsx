import React, { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Form,
  Button,
  Image,
  Alert,
} from "react-bootstrap";
import useAxios from "../axios/axiosConfig";
import InterestSelector from "../selectors/InterestSelector";
import SkillSelector from "../selectors/SkillSelector";
import DefaultAvatar from "../resources/avatares/Avatar padrão.jpg";
import LoginHeader from "../headers/LoginHeader";
import "./CreateProfilePage.css";

const CreateProfilePage = () => {
  const [username, setUsername] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [office, setOffice] = useState("");
  const [usernameValid, setUsernameValid] = useState(null); // null: not checked, true: available, false: not available
  const [loading, setLoading] = useState(false);
  const [labs, setLabs] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  const [preview, setPreview] = useState(null);
  const [fileUploadError, setFileUploadError] = useState(null);
  const [selectedInterests, setSelectedInterests] = useState([]);
  const [selectedSkills, setSelectedSkills] = useState([]);
  const [formErrors, setFormErrors] = useState({}); // Adicionei para gerenciar os erros de validação
  const axios = useAxios();

  useEffect(() => {
    const fetchLabs = async () => {
      try {
        const response = await axios.get("/lab/all");
        const formattedLabs = response.data.map((lab) => ({
          ...lab,
          local: formatLocalName(lab.local),
        }));
        setLabs(formattedLabs);
      } catch (error) {
        console.error("Error fetching labs", error);
      }
    };

    fetchLabs();
  }, [axios]);

  const formatLocalName = (name) => {
    return name
      .toLowerCase()
      .split(/[\s_]+/)
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
      .join(" ");
  };

  const checkUsernameAvailability = async () => {
    if (!username) {
      setUsernameValid(null); // Clear feedback if the username is empty
      return;
    }
    setLoading(true);
    try {
      const response = await axios.post(
        "/user/checkUsername",
        {}, // Enviando um corpo vazio
        {
          headers: {
            username: username,
          },
        }
      );
      if (response.status === 200) {
        setUsernameValid(true);
      } else if (response.status === 409) {
        setUsernameValid(false);
      }
    } catch (error) {
      console.error("Error checking username availability", error);
      setUsernameValid(false);
    } finally {
      setLoading(false);
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

  const validateForm = () => {
    const errors = {};
    if (!firstName) errors.firstName = "First Name is required.";
    if (!lastName) errors.lastName = "Last Name is required.";
    if (!office) errors.office = "Office is required.";
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("username", username);
      formData.append("firstName", firstName);
      formData.append("lastName", lastName);
      formData.append("office", office);
      if (selectedFile) {
        formData.append("file", selectedFile);
      }
      formData.append("interests", JSON.stringify(selectedInterests));
      formData.append("skills", JSON.stringify(selectedSkills));

      const response = await axios.post("/user/createProfile", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      const filePath = response.data.filePath;
      console.log("Profile created successfully:", filePath);
      // Handle further actions like redirecting to a different page
    } catch (error) {
      console.error("Error creating profile", error);
      setFileUploadError("Error creating profile");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Container>
        <Row className="justify-content-md-center mt-4">
          <Col md="10">
            <h2 className="text-center mb-4">Create Profile</h2>
            <Form onSubmit={handleSubmit}>
              <Row>
                <Col md={4} className="text-center">
                  {preview ? (
                    <Image src={preview} className="profile-image mb-3" fluid />
                  ) : (
                    <Image
                      src={DefaultAvatar}
                      className="profile-image mb-3"
                      fluid
                    />
                  )}
                  <Form.Group controlId="formFileUpload" className="mb-3">
                    <Form.Label>Upload a file:</Form.Label>
                    <Form.Control type="file" onChange={handleFileChange} />
                    {fileUploadError && (
                      <Alert variant="danger" className="mt-2">
                        {fileUploadError}
                      </Alert>
                    )}
                  </Form.Group>
                </Col>
                <Col md={8}>
                  <Form.Group
                    as={Row}
                    controlId="formFirstName"
                    className="mb-3"
                  >
                    <Form.Label column sm="4">
                      First Name <span className="text-danger">*</span>:
                    </Form.Label>
                    <Col sm="8">
                      <Form.Control
                        type="text"
                        placeholder="Enter First Name"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        isInvalid={!!formErrors.firstName}
                      />
                      <Form.Control.Feedback type="invalid">
                        {formErrors.firstName}
                      </Form.Control.Feedback>
                    </Col>
                  </Form.Group>

                  <Form.Group
                    as={Row}
                    controlId="formLastName"
                    className="mb-3"
                  >
                    <Form.Label column sm="4">
                      Last Name <span className="text-danger">*</span>:
                    </Form.Label>
                    <Col sm="8">
                      <Form.Control
                        type="text"
                        placeholder="Enter Last Name"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        isInvalid={!!formErrors.lastName}
                      />
                      <Form.Control.Feedback type="invalid">
                        {formErrors.lastName}
                      </Form.Control.Feedback>
                    </Col>
                  </Form.Group>

                  <Form.Group
                    as={Row}
                    controlId="formUsername"
                    className="mb-3"
                  >
                    <Form.Label column sm="4">
                      Username:
                    </Form.Label>
                    <Col sm="8">
                      <Form.Control
                        type="text"
                        placeholder="Enter Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        onBlur={checkUsernameAvailability}
                        isInvalid={usernameValid === false}
                        isValid={usernameValid === true}
                      />
                      <Form.Control.Feedback type="invalid">
                        Username is already taken.
                      </Form.Control.Feedback>
                      <Form.Control.Feedback type="valid">
                        Username is available.
                      </Form.Control.Feedback>
                      {loading && <div>Checking...</div>}
                    </Col>
                  </Form.Group>

                  <Form.Group as={Row} controlId="formOffice" className="mb-3">
                    <Form.Label column sm="4">
                      Office <span className="text-danger">*</span>:
                    </Form.Label>
                    <Col sm="8">
                      <Form.Control
                        as="select"
                        value={office}
                        onChange={(e) => setOffice(e.target.value)}
                        isInvalid={!!formErrors.office}
                      >
                        <option value="">Select Office</option>
                        {labs.map((lab, index) => (
                          <option key={index} value={lab.local}>
                            {lab.local}
                          </option>
                        ))}
                      </Form.Control>
                      <Form.Control.Feedback type="invalid">
                        {formErrors.office}
                      </Form.Control.Feedback>
                    </Col>
                  </Form.Group>

                  <Form.Group
                    as={Row}
                    controlId="formProfileToggle"
                    className="mb-3"
                  >
                    <Form.Label column sm="4">
                      Public Profile:
                    </Form.Label>
                    <Col sm="8" className="d-flex align-items-center">
                      <Form.Check type="switch" id="custom-switch" />
                    </Col>
                  </Form.Group>
                </Col>
              </Row>

              <Row className="mt-4">
                <Col md={6}>
                  <InterestSelector
                    selectedInterests={selectedInterests}
                    setSelectedInterests={setSelectedInterests}
                  />
                </Col>
                <Col md={6}>
                  <SkillSelector
                    selectedSkills={selectedSkills}
                    setSelectedSkills={setSelectedSkills}
                  />
                </Col>
              </Row>

              <Row className="mt-4 justify-content-end">
                <Col md="auto" className="button-group">
                  <Button variant="secondary" className="mr-3">
                    Cancel
                  </Button>
                  <Button variant="primary" type="submit">
                    Next
                  </Button>
                </Col>
              </Row>
            </Form>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default CreateProfilePage;
