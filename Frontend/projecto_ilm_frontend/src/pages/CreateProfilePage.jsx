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
  const axios = useAxios();

  useEffect(() => {
    const fetchLabs = async () => {
      try {
        const response = await axios.get("/lab/all");
        const formattedLabs = response.data.map(lab => ({
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
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
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

  const handleSubmit = async (e) => {
    e.preventDefault();
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
    <Container>
      <Row className="justify-content-md-center mt-4">
        <Col md="8">
          <h2 className="text-center mb-4">Create Profile</h2>
          <Form onSubmit={handleSubmit}>
            <Row>
              <Col md={3}>
                {preview ? (
                  <Image src={preview} roundedCircle className="mb-3" fluid />
                ) : (
                  <Image
                    src="path/to/your/default/avatar.png"
                    roundedCircle
                    className="mb-3"
                    fluid
                  />
                )}
              </Col>
              <Col md={9}>
                <Form.Group as={Row} controlId="formFirstName">
                  <Form.Label column sm="4">
                    First Name:
                  </Form.Label>
                  <Col sm="8">
                    <Form.Control
                      type="text"
                      placeholder="Enter First Name"
                      value={firstName}
                      onChange={(e) => setFirstName(e.target.value)}
                    />
                  </Col>
                </Form.Group>

                <Form.Group as={Row} controlId="formLastName">
                  <Form.Label column sm="4">
                    Last Name:
                  </Form.Label>
                  <Col sm="8">
                    <Form.Control
                      type="text"
                      placeholder="Enter Last Name"
                      value={lastName}
                      onChange={(e) => setLastName(e.target.value)}
                    />
                  </Col>
                </Form.Group>

                <Form.Group as={Row} controlId="formUsername">
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
                    />
                    {loading && <div>Checking...</div>}
                    {usernameValid === false && (
                      <Alert variant="danger" className="mt-2">
                        Username is already taken.
                      </Alert>
                    )}
                    {usernameValid === true && (
                      <Alert variant="success" className="mt-2">
                        Username is available.
                      </Alert>
                    )}
                  </Col>
                </Form.Group>

                <Form.Group as={Row} controlId="formOffice">
                  <Form.Label column sm="4">
                    Office:
                  </Form.Label>
                  <Col sm="8">
                    <Form.Control
                      as="select"
                      value={office}
                      onChange={(e) => setOffice(e.target.value)}
                    >
                      <option>ComboBox</option>
                      {labs.map((lab, index) => (
                        <option key={index} value={lab.local}>
                          {lab.local}
                        </option>
                      ))}
                    </Form.Control>
                  </Col>
                </Form.Group>

                <Form.Group as={Row} controlId="formProfileToggle">
                  <Form.Label column sm="4">
                    Public Profile:
                  </Form.Label>
                  <Col sm="8" className="d-flex align-items-center">
                    <Form.Check type="switch" id="custom-switch" />
                  </Col>
                </Form.Group>

                <Form.Group
                  as={Row}
                  controlId="formFileUpload"
                  className="mt-3"
                >
                  <Form.Label column sm="2">
                    Upload a file:
                  </Form.Label>
                  <Col sm="8">
                    <Form.Control type="file" onChange={handleFileChange} />
                    {fileUploadError && (
                      <Alert variant="danger" className="mt-2">
                        {fileUploadError}
                      </Alert>
                    )}
                  </Col>
                </Form.Group>

                <InterestSelector
                  selectedInterests={selectedInterests}
                  setSelectedInterests={setSelectedInterests}
                />
                <SkillSelector
              selectedSkills={selectedSkills}
              setSelectedSkills={setSelectedSkills}
            />
              </Col>
            </Row>

            <Row className="mt-4 justify-content-end">
              <Col md="auto">
                <Button variant="secondary" className="mr-2">
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
  );
};

export default CreateProfilePage;
