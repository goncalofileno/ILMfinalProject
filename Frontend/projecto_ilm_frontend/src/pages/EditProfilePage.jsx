import React, { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Form,
  Button,
  Image,
  Alert,
  Modal,
} from "react-bootstrap";
import InterestSelector from "../components/selectors/InterestSelector";
import SkillSelector from "../components/selectors/SkillSelector";
import DefaultAvatar from "../resources/avatares/Avatar padrão.jpg";
import {
  getLabs,
  checkUsername,
  updateUserProfile,
  uploadProfilePicture,
  getUserEditProfile,
  changeUserPassword,
} from "../utilities/services";
import { useNavigate } from "react-router-dom";
import InputForm from "../components/inputs/InputForm";
import AppNavbar from "../components/headers/AppNavbar";
import Cookies from "js-cookie";

const EditProfilePage = () => {
  const [username, setUsername] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [office, setOffice] = useState("");
  const [bio, setBio] = useState("");
  const [publicProfile, setPublicProfile] = useState(false);
  const [usernameValid, setUsernameValid] = useState(false);
  const [loading, setLoading] = useState(false);
  const [labs, setLabs] = useState([]);
  const [selectedFile, setSelectedFile] = useState(null);
  const [preview, setPreview] = useState(DefaultAvatar);
  const [fileUploadError, setFileUploadError] = useState(null);
  const [selectedInterests, setSelectedInterests] = useState([]);
  const [selectedSkills, setSelectedSkills] = useState([]);
  const [formErrors, setFormErrors] = useState({});
  const [showModal, setShowModal] = useState(false);
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmNewPassword, setConfirmNewPassword] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const navigate = useNavigate();
  const systemUsername = Cookies.get("user-systemUsername");

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await getUserEditProfile(systemUsername);
        if (response.status === 200) {
          const profileData = await response.json();
          console.log("User profile data:", profileData);
          setUsername(profileData.username);
          setFirstName(profileData.firstName);
          setLastName(profileData.lastName);
          setOffice(formatLocalName(profileData.lab));
          setBio(profileData.bio);
          setPublicProfile(profileData.publicProfile);
          setSelectedInterests(profileData.interests);
          setSelectedSkills(profileData.skills);
          setPreview(profileData.photo || DefaultAvatar);
        } else {
          console.error("Error fetching user profile:", response);
        }
      } catch (error) {
        console.error("Error fetching user profile:", error);
      }
    };

    fetchProfile();
    getLabs()
      .then((response) => response.json())
      .then((data) => {
        const formattedLabs = data.map((lab) => ({
          ...lab,
          local: formatLocalName(lab.local),
        }));
        setLabs(formattedLabs);
      });
  }, [systemUsername]);

  const formatLocalName = (name) => {
    return name
      .toLowerCase()
      .split(/[\s_]+/)
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
      .join(" ");
  };

  const formatLabName = (lab) => {
    return lab.toUpperCase().replace(/ /g, "_");
  };

  const checkUsernameAvailability = async () => {
    if (!username) {
      setUsernameValid(null);
      return;
    }
    setLoading(true);
    try {
      const response = await checkUsername(username);
      if (response.status === 200) {
        console.log("Username is available");
        setUsernameValid(true);
      } else if (response.status === 409) {
        console.log("Username is already taken");
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

  const handleCancel = () => {
    navigate(`/profile/${systemUsername}`);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;

    setLoading(true);

    const formattedOffice = formatLabName(office);

    try {
      const userProfileDto = {
        firstName,
        lastName,
        username,
        lab: formattedOffice,
        publicProfile,
        bio,
        skills: selectedSkills,
        interests: selectedInterests,
      };

      console.log("Updating profile...", userProfileDto);

      const response = await updateUserProfile(userProfileDto);
      if (response.ok) {
        if (selectedFile) {
          const uploadResponse = await uploadProfilePicture(selectedFile);
          if (uploadResponse.ok) {
            console.log("Profile updated and picture uploaded successfully");
            navigate(`/profile/${systemUsername}`);
          } else {
            const uploadErrorData = await uploadResponse.json();
            console.error("Error uploading profile picture:", uploadErrorData);
            setFileUploadError("Error uploading profile picture");
          }
        } else {
          console.log("Profile updated successfully");
          navigate(`/profile/${systemUsername}`);
        }
      } else {
        const errorData = await response.json();
        console.error("Error updating profile:", errorData);
        setFileUploadError("Error updating profile");
      }
    } catch (error) {
      console.error("Error updating profile", error);
      setFileUploadError("Error updating profile");
    } finally {
      setLoading(false);
    }
  };

  const handlePasswordChange = async () => {
    if (newPassword !== confirmNewPassword) {
      setPasswordError("New passwords do not match.");
      return;
    }

    try {
      const response = await changeUserPassword(currentPassword, newPassword);
      if (response.ok) {
        setPasswordError("");
        setShowModal(false);
      } else {
        const errorData = await response.json();
        setPasswordError(errorData.message || "Error changing password.");
      }
    } catch (error) {
      console.error("Error changing password", error);
      setPasswordError("Error changing password.");
    }
  };

  return (
    <>
      <AppNavbar />
      <Container className="ilm-form" style={{ marginTop: "80px" }}>
        <Row className="justify-content-md-center mt-4">
          <Col md="10">
            <h2 className="text-center mb-4">Edit Profile</h2>
            <Form onSubmit={handleSubmit}>
              <Row>
                <Col md={6}>
                  <Image src={preview} className="profile-image mb-3" fluid />
                  <Form.Group controlId="formFileUpload" className="mb-3">
                    <Form.Label className="custom-label">Photo</Form.Label>
                    <Form.Control
                      type="file"
                      onChange={handleFileChange}
                      className="custom-focus"
                    />
                    {fileUploadError && (
                      <Alert variant="danger" className="mt-2">
                        {fileUploadError}
                      </Alert>
                    )}
                  </Form.Group>

                  <Form.Group controlId="formBio" className="mb-3">
                    <Form.Label className="custom-label">Bio</Form.Label>
                    <Form.Control
                      as="textarea"
                      rows={3}
                      style={{ resize: "none" }}
                      placeholder="Enter Bio"
                      value={bio}
                      onChange={(e) => setBio(e.target.value)}
                      className="custom-focus"
                    />
                  </Form.Group>
                </Col>
                <Col md={6}>
                  <InputForm
                    label={
                      <>
                        First Name <span className="text-danger">*</span>
                      </>
                    }
                    type="text"
                    value={firstName}
                    setValue={setFirstName}
                    warningType={formErrors.firstName ? "incorrect" : ""}
                    warningTxt={formErrors.firstName}
                    handleOnBlur={() => {}}
                    onBlurActive={false}
                  />

                  <InputForm
                    label={
                      <>
                        Last Name <span className="text-danger">*</span>
                      </>
                    }
                    type="text"
                    value={lastName}
                    setValue={setLastName}
                    warningType={formErrors.lastName ? "incorrect" : ""}
                    warningTxt={formErrors.lastName}
                    handleOnBlur={() => {}}
                    onBlurActive={false}
                  />

                  <InputForm
                    label="Username"
                    type="text"
                    value={username}
                    setValue={setUsername}
                    warningType={
                      usernameValid === false
                        ? "incorrect"
                        : usernameValid === true
                        ? "success"
                        : ""
                    }
                    warningTxt={
                      usernameValid === false
                        ? "Username is already taken."
                        : "Username is available."
                    }
                    handleOnBlur={checkUsernameAvailability}
                    onBlurActive={true}
                  />
                  {loading && <div>Checking...</div>}

                  <Form.Group controlId="formOffice" className="mb-3">
                    <Form.Label className="custom-label">
                      Office <span className="text-danger">*</span>
                    </Form.Label>
                    <Form.Control
                      as="select"
                      value={office}
                      onChange={(e) => setOffice(e.target.value)}
                      isInvalid={!!formErrors.office}
                      className="custom-focus"
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
                  </Form.Group>
                  <Form.Group
                    controlId="formProfileToggle"
                    className="mb-3 custom-switch"
                  >
                    <Form.Label className="custom-label">
                      Public Profile
                    </Form.Label>
                    <Form.Check
                      type="switch"
                      checked={publicProfile}
                      onChange={(e) => setPublicProfile(e.target.checked)}
                    />
                  </Form.Group>
                  <Form.Group controlId="formChangePassword" className="mb-3">
                    <Button
                      variant="link"
                      onClick={() => setShowModal(true)}
                      style={{ padding: "0" }}
                    >
                      Change Password
                    </Button>
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
                  <Button
                    variant="secondary"
                    onClick={handleCancel}
                    style={{ marginRight: "10px" }}
                  >
                    Cancel
                  </Button>
                  <Button
                    variant="primary"
                    type="submit"
                    style={{
                      backgroundColor: "#f39c12",
                      borderColor: "#f39c12",
                    }}
                  >
                    Save
                  </Button>
                </Col>
              </Row>
            </Form>
          </Col>
        </Row>
      </Container>

      {/* Modal for changing password */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Change Password</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {passwordError && (
            <Alert variant="danger" className="mt-2">
              {passwordError}
            </Alert>
          )}
          <Form>
            <Form.Group controlId="formCurrentPassword" className="mb-3">
              <Form.Label className="custom-label">Current Password</Form.Label>
              <Form.Control
                type="password"
                value={currentPassword}
                onChange={(e) => setCurrentPassword(e.target.value)}
                className="custom-focus"
              />
            </Form.Group>
            <Form.Group controlId="formNewPassword" className="mb-3">
              <Form.Label className="custom-label">New Password</Form.Label>
              <Form.Control
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                className="custom-focus"
              />
            </Form.Group>
            <Form.Group controlId="formConfirmNewPassword" className="mb-3">
              <Form.Label className="custom-label">
                Confirm New Password
              </Form.Label>
              <Form.Control
                type="password"
                value={confirmNewPassword}
                onChange={(e) => setConfirmNewPassword(e.target.value)}
                className="custom-focus"
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handlePasswordChange}>
            Change Password
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default EditProfilePage;
