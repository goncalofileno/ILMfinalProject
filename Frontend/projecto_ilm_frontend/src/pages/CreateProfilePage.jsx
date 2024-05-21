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

const CreateProfilePage = () => {
  const [username, setUsername] = useState("");
  const [usernameValid, setUsernameValid] = useState(null); // null: not checked, true: available, false: not available
  const [loading, setLoading] = useState(false);
  const [labs, setLabs] = useState([]);
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

  return (
    <Container>
      <Row className="justify-content-md-center mt-4">
        <Col md="8">
          <h2 className="text-center mb-4">Create Profile</h2>
          <Form>
            <Row>
              <Col md={3}>
                <Image
                  src="path/to/your/avatar.png"
                  roundedCircle
                  className="mb-3"
                />
              </Col>
              <Col md={9}>
                <Form.Group as={Row} controlId="formFirstName">
                  <Form.Label column sm="4">
                    First Name:
                  </Form.Label>
                  <Col sm="8">
                    <Form.Control type="text" placeholder="Enter First Name" />
                  </Col>
                </Form.Group>

                <Form.Group as={Row} controlId="formLastName">
                  <Form.Label column sm="4">
                    Last Name:
                  </Form.Label>
                  <Col sm="8">
                    <Form.Control type="text" placeholder="Enter Last Name" />
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
                    <Form.Control as="select">
                      <option>Select a Office</option>
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
              </Col>
            </Row>

            <Form.Group as={Row} controlId="formFileUpload" className="mt-3">
              <Form.Label column sm="2">
                Upload a file:
              </Form.Label>
              <Col sm="8">
                <Form.Control type="file" />
              </Col>
            </Form.Group>

            <Row className="mt-4">
              <Col md={6}>
                <Form.Group controlId="formInterests">
                  <Form.Label>Interests</Form.Label>
                  <Form.Control type="text" placeholder="interests" />
                </Form.Group>
              </Col>
              <Col md={6}>
                <Form.Group controlId="formSkills">
                  <Form.Label>Skills</Form.Label>
                  <Form.Control type="text" placeholder="skills" />
                </Form.Group>
              </Col>
            </Row>

            <Row className="mt-4 justify-content-end">
              <Col md="auto">
                <Button variant="secondary" className="mr-2">
                  Cancel
                </Button>
                <Button variant="primary">Next</Button>
              </Col>
            </Row>
          </Form>
        </Col>
      </Row>
    </Container>
  );
};

export default CreateProfilePage;
