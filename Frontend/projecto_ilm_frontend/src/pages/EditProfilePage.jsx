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
  getLabsWithSessionId,
  checkUsername,
  updateUserProfile,
  uploadProfilePictureWithSession,
  getUserEditProfile,
  changeUserPassword,
  updatePassword,
} from "../utilities/services";
import { useNavigate } from "react-router-dom";
import InputForm from "../components/inputs/InputForm";
import AppNavbar from "../components/headers/AppNavbar";
import PasswordForm from "../components/inputs/PasswordForm";
import Cookies from "js-cookie";
import "../components/modals/Modals.css";
import "./ResetPasswordPage.css";
import { formatLab } from "../utilities/converters";
import { Trans, t } from "@lingui/macro";

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
  const [passwordSuccess, setPasswordSuccess] = useState("");
  const [conditionsMet, setConditionsMet] = useState({
    upper: false,
    lower: false,
    number: false,
    special: false,
    length: false,
  });
  const [warningTypePassword, setWarningTypePassword] = useState("");
  const [warningTxtPassword, setWarningTxtPassword] = useState("");
  const [warningTypeConfirmPassword, setWarningTypeConfirmPassword] =
    useState("");
  const [warningTxtConfirmPassword, setWarningTxtConfirmPassword] =
    useState("");
  const navigate = useNavigate();
  const systemUsername = Cookies.get("user-systemUsername");
  const [showTolltip, setShowTooltip] = useState(false);
  const [strength, setStrength] = useState(0);
  const [currentLanguage, setCurrentLanguage] = useState(
    Cookies.get("user-language") || "ENGLISH"
  );

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await getUserEditProfile(systemUsername);
        if (response.status === 200) {
          const profileData = await response.json();
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
    getLabsWithSessionId()
      .then((response) => response.json())
      .then((data) => {
        const formattedLabs = data.map((lab) => ({
          ...lab,
          local: formatLab(lab.local),
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
    if (!firstName) errors.firstName = t`First Name is required.`;
    if (!lastName) errors.lastName = t`Last Name is required.`;
    if (!office) errors.office = t`Office is required.`;
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

      const response = await updateUserProfile(userProfileDto);
      if (response.ok) {
        if (selectedFile) {
          const uploadResponse = await uploadProfilePictureWithSession(
            selectedFile
          );
          if (uploadResponse.ok) {
            navigate(`/profile/${systemUsername}`);
          } else {
            const uploadErrorData = await uploadResponse.json();
            console.error("Error uploading profile picture:", uploadErrorData);
            setFileUploadError(t`Error uploading profile picture`);
          }
        } else {
          navigate(`/profile/${systemUsername}`);
        }
      } else {
        const errorData = await response.json();
        console.error("Error updating profile:", errorData);
        setFileUploadError(t`Error updating profile`);
      }
    } catch (error) {
      console.error("Error updating profile", error);
      setFileUploadError(t`Error updating profile`);
    } finally {
      setLoading(false);
    }
  };

  // const updatePassword = (e) => {
  //   const value = e.target.value;
  //   setNewPassword(value);
  //   setStrength(calculateStrength(value));
  //   validatePassword(value);
  // };

  const calculateStrength = (password) => {
    let strength = 0;
    if (/\S/.test(password)) {
      strength++;
      if (password.length >= 6) {
        if (/[a-z]/.test(password)) strength++; // lowercase
        if (/[A-Z]/.test(password)) strength++; // uppercase
        if (/\d/.test(password)) strength++; // digits
      }
      if (strength === 4) {
        if (/\W/.test(password)) strength++; // special characters
      }
    }
    return strength;
  };

  const validatePassword = (value) => {
    const conditions = {
      upper: /[A-Z]/.test(value),
      lower: /[a-z]/.test(value),
      number: /\d/.test(value),
      special: /\W/.test(value),
      length: value.length >= 6,
    };
    setConditionsMet(conditions);
  };

  const handleOnBlurPassword = () => {
    setPasswordError("");
    setPasswordSuccess("");

    if (strength >= 4) {
      setWarningTypePassword("success");
      setWarningTxtPassword(t`Password is strong`);
    } else {
      setWarningTypePassword("incorrect");
      setWarningTxtPassword(t`Password must be strong`);
    }
  };

  const handleOnBlurConfirmPassword = () => {
    setPasswordError("");
    setPasswordSuccess("");

    if (newPassword === confirmNewPassword) {
      setWarningTypeConfirmPassword("success");
      setWarningTxtConfirmPassword(t`Passwords match`);
    } else {
      setWarningTypeConfirmPassword("incorrect");
      setWarningTxtConfirmPassword(t`Passwords do not match`);
    }
  };

  const handlePasswordChange = async () => {
    // Redefinir avisos
    setPasswordError("");
    setPasswordSuccess("");

    if (newPassword !== confirmNewPassword) {
      setPasswordError(t`New passwords do not match.`);
      return;
    }

    if (strength < 4) {
      setPasswordError(t`Password is not strong enough.`);
      return;
    }

    try {
      const response = await updatePassword(
        currentPassword,
        confirmNewPassword
      );
      if (response.ok) {
        setPasswordSuccess(t`Password updated successfully.`);
        setTimeout(() => {
          setShowModal(false);
          resetModalFields();
        }, 3000); // Fecha o modal após 3 segundos
      } else {
        const errorData = await response.json();
        setPasswordError(errorData.message || t`Error changing password.`);
      }
    } catch (error) {
      console.error("Error changing password:", error);
      setPasswordError(error.message || t`Error changing password.`);
    }
  };

  const resetModalFields = () => {
    setCurrentPassword("");
    setNewPassword("");
    setConfirmNewPassword("");
    setPasswordError("");
    setPasswordSuccess("");
    setStrength(0);
    setConditionsMet({
      upper: false,
      lower: false,
      number: false,
      special: false,
      length: false,
    });
    setWarningTypePassword("");
    setWarningTxtPassword("");
    setWarningTypeConfirmPassword("");
    setWarningTxtConfirmPassword("");
  };

  return (
    <>
      <AppNavbar setCurrentLanguage={setCurrentLanguage} />
      <Container className="ilm-form" style={{ marginTop: "80px" }}>
        <Row className="justify-content-md-center mt-4">
          <Col md="10">
            <h2 className="text-center mb-4">
              <Trans>Edit Profile</Trans>
            </h2>
            <Form onSubmit={handleSubmit}>
              <Row>
                <Col md={6}>
                  <Image src={preview} className="profile-image mb-3" fluid />
                  <Form.Group controlId="formFileUpload" className="mb-3">
                    <Form.Label className="custom-label">
                      <Trans>Photo</Trans>
                    </Form.Label>
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
                    <Form.Label className="custom-label">
                      <Trans>Bio</Trans>
                    </Form.Label>
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
                        <Trans>First Name</Trans>{" "}
                        <span className="text-danger">*</span>
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
                        <Trans>Last Name</Trans>{" "}
                        <span className="text-danger">*</span>
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
                    label={t`Username`}
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
                  {loading && (
                    <div>
                      <Trans>Checking...</Trans>
                    </div>
                  )}

                  <Form.Group controlId="formOffice" className="mb-3">
                    <Form.Label className="custom-label">
                      <Trans>Office</Trans>{" "}
                      <span className="text-danger">*</span>
                    </Form.Label>
                    <Form.Select
                      value={office}
                      onChange={(e) => setOffice(e.target.value)}
                      isInvalid={!!formErrors.office}
                      className="custom-focus"
                    >
                      <option value="">
                        <Trans>Select Office</Trans>
                      </option>
                      {labs.map((lab, index) => (
                        <option key={index} value={lab.local}>
                          {lab.local}
                        </option>
                      ))}
                    </Form.Select>
                    <Form.Control.Feedback type="invalid">
                      {formErrors.office}
                    </Form.Control.Feedback>
                  </Form.Group>
                  <Form.Group
                    controlId="formProfileToggle"
                    className="mb-3 custom-switch"
                  >
                    <Form.Label className="custom-label">
                      <Trans>Public Profile</Trans>
                    </Form.Label>
                    <Form.Check
                      type="switch"
                      checked={publicProfile}
                      onChange={(e) => setPublicProfile(e.target.checked)}
                    />
                  </Form.Group>
                  <Form.Group controlId="formChangePassword" className="mb-3">
                    <Button
                      variant="primary"
                      onClick={() => setShowModal(true)}
                      style={{
                        backgroundColor: "#f39c12",
                        borderColor: "#f39c12",
                      }}
                    >
                      <Trans>Change Password</Trans>
                    </Button>
                  </Form.Group>
                </Col>
              </Row>

              <Row className="mt-4">
                <Col md={6}>
                  <InterestSelector
                    label={t`Interests:`}
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
                <Col
                  md="auto"
                  className="button-group"
                  style={{ display: "flex", flexDirection: "row" }}
                >
                  <Button
                    variant="secondary"
                    onClick={handleCancel}
                    style={{ marginRight: "10px" }}
                  >
                    <Trans>Cancel</Trans>
                  </Button>
                  <Button
                    variant="primary"
                    type="submit"
                    style={{
                      backgroundColor: "#f39c12",
                      borderColor: "#f39c12",
                    }}
                  >
                    <Trans>Save</Trans>
                  </Button>
                </Col>
              </Row>
            </Form>
          </Col>
        </Row>
      </Container>

      {/* Modal for changing password */}
      <Modal
        show={showModal}
        onHide={() => {
          setShowModal(false);
          resetModalFields();
        }}
      >
        <Modal.Header closeButton>
          <Modal.Title>
            <Trans>Change Password</Trans>
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {passwordError && (
            <Alert variant="danger" className="mt-2">
              {passwordError}
            </Alert>
          )}
          {passwordSuccess && (
            <Alert variant="success" className="mt-2">
              {passwordSuccess}
            </Alert>
          )}
          <Form>
            <PasswordForm
              label={t`Current Password`}
              type="password"
              value={currentPassword}
              setValue={(e) => setCurrentPassword(e.target.value)}
              warningType=""
              warningTxt=""
              handleOnBlur={() => {}}
              showTolltip={false}
              setShowTooltip={() => {}}
              conditionsMet={{}}
              onBlurActive={false}
            />
            <PasswordForm
              label={t`New Password`}
              type="password"
              value={newPassword}
              setValue={(e) => {
                const value = e.target.value;
                setNewPassword(value);
                setStrength(calculateStrength(value));
                validatePassword(value);
              }}
              warningType={warningTypePassword}
              warningTxt={warningTxtPassword}
              handleOnBlur={handleOnBlurPassword}
              showTolltip={showTolltip}
              setShowTooltip={setShowTooltip}
              conditionsMet={conditionsMet}
              onBlurActive={true}
            />
            <div id="div-password-container">
              <div id="pass-strength">
                <div>
                  <Trans>Password Strength</Trans>
                </div>
                <meter max="5" value={strength}></meter>
              </div>
              <div id="pass-strength-string">
                <div></div>
                <div
                  style={{
                    color:
                      strength === 0
                        ? "black"
                        : strength === 1
                        ? "red"
                        : strength === 2
                        ? "orange"
                        : strength === 3
                        ? "yellow"
                        : strength === 4
                        ? "green"
                        : strength === 5 && "green",
                  }}
                >
                  {strength === 0 && t`None`}
                  {strength === 1 && t`Weak`}
                  {strength === 2 && t`Fair`}
                  {strength === 3 && t`Good`}
                  {strength === 4 && t`Strong`}
                  {strength === 5 && t`Very Strong`}
                </div>
              </div>
            </div>
            <InputForm
              label={t`Confirm New Password`}
              type="password"
              value={confirmNewPassword}
              setValue={setConfirmNewPassword}
              warningType={warningTypeConfirmPassword}
              warningTxt={warningTxtConfirmPassword}
              handleOnBlur={handleOnBlurConfirmPassword}
              onBlurActive={true}
            />
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="secondary"
            onClick={() => {
              setShowModal(false);
              resetModalFields();
            }}
          >
            <Trans>Cancel</Trans>
          </Button>
          <Button
            variant="primary"
            onClick={handlePasswordChange}
            style={{
              backgroundColor: "#f39c12",
              borderColor: "#f39c12",
            }}
          >
            <Trans>Change Password</Trans>
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default EditProfilePage;
