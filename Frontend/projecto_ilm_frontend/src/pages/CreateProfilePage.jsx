import React, { useState, useEffect } from "react";
import { Container, Row, Col, Form, Button, Image, Alert } from "react-bootstrap";
import InterestSelector from "../components/selectors/InterestSelector";
import SkillSelector from "../components/selectors/SkillSelector";
import DefaultAvatar from "../resources/avatares/Avatar padrão.jpg";
import "./CreateProfilePage.css";
import { getLabs, checkUsername, checkAuxiliarToken, createProfile, uploadProfilePicture } from "../utilities/services";
import { useNavigate, useParams } from "react-router-dom";
import InputForm from "../components/inputs/InputForm";
import LoginHeader from "../components/headers/LoginHeader";

const CreateProfilePage = () => {
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
   const [preview, setPreview] = useState(null);
   const [fileUploadError, setFileUploadError] = useState(null);
   const [selectedInterests, setSelectedInterests] = useState([]);
   const [selectedSkills, setSelectedSkills] = useState([]);
   const [formErrors, setFormErrors] = useState({});
   const [showConfirmationAlert, setShowConfirmationAlert] = useState(false);
   const [isFormValid, setIsFormValid] = useState(false); // Novo estado para validade do formulário
   const navigate = useNavigate();
   const { token } = useParams();

   useEffect(() => {
      if (token) {
         checkAuxiliarToken(token).then((response) => {
            if (response.status === 200) {
               setShowConfirmationAlert(true);
               getLabs(token)
                  .then((response) => response.json())
                  .then((data) => {
                     const formattedLabs = data.map((lab) => ({
                        ...lab,
                        local: formatLocalName(lab.local),
                     }));
                     setLabs(formattedLabs);
                  });
            } else {
               navigate("/");
            }
         });
      } else {
         getLabs(token)
            .then((response) => response.json())
            .then((data) => {
               const formattedLabs = data.map((lab) => ({
                  ...lab,
                  local: formatLocalName(lab.local),
               }));
               setLabs(formattedLabs);
            });
      }
   }, [token]);

   useEffect(() => {
      // Verifica a validade do formulário sempre que os campos são alterados
      setIsFormValid(firstName !== "" && lastName !== "" && office !== "");
   }, [firstName, lastName, office]);

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
         checkUsername(username, token).then((response) => {
            if (response.status === 200) {
               console.log("Username is available");
               setUsernameValid(true);
            } else if (response.status === 409) {
               console.log("Username is already taken");
               setUsernameValid(false);
            }
         });
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
      navigate("/");
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

         console.log("Creating profile...", userProfileDto);

         const response = await createProfile(userProfileDto, token);
         if (response.ok) {
            if (selectedFile) {
               const uploadResponse = await uploadProfilePicture(selectedFile, token);
               if (uploadResponse.ok) {
                  console.log("Profile created and picture uploaded successfully");
                  navigate("/home");
               } else {
                  const uploadErrorData = await uploadResponse.json();
                  console.error("Error uploading profile picture:", uploadErrorData);
                  setFileUploadError("Error uploading profile picture");
               }
            } else {
               console.log("Profile created successfully");
               navigate("/home");
            }
         } else {
            const errorData = await response.json();
            console.error("Error creating profile:", errorData);
            setFileUploadError("Error creating profile");
         }
      } catch (error) {
         console.error("Error creating profile", error);
         setFileUploadError("Error creating profile");
      } finally {
         setLoading(false);
      }
   };

   return (
      <>
         <LoginHeader />
         <Container className="ilm-form" style={{ marginTop: "80px" }}>
            {showConfirmationAlert && (
               <Alert variant="success" className="text-center">
                  Your account has been successfully confirmed!
               </Alert>
            )}
            <Row className="justify-content-md-center mt-4">
               <Col md="10">
                  <h2 className="text-center mb-4">Create Profile</h2>
                  <Form onSubmit={handleSubmit}>
                     <Row>
                        <Col md={6}>
                           {preview ? (
                              <Image src={preview} className="profile-image mb-3" fluid />
                           ) : (
                              <Image src={DefaultAvatar} className="profile-image mb-3" fluid />
                           )}
                           <Form.Group controlId="formFileUpload" className="mb-3">
                              <Form.Label className="custom-label">Photo</Form.Label>
                              <Form.Control type="file" onChange={handleFileChange} className="custom-focus" />
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
                                 usernameValid === false ? "incorrect" : usernameValid === true ? "success" : ""
                              }
                              warningTxt={
                                 usernameValid === false ? "Username is already taken." : "Username is available."
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
                              <Form.Control.Feedback type="invalid">{formErrors.office}</Form.Control.Feedback>
                           </Form.Group>
                           <Form.Group controlId="formProfileToggle" className="mb-3 custom-switch">
                              <Form.Label className="custom-label">Public Profile</Form.Label>
                              <Form.Check
                                 type="switch"
                                 checked={publicProfile}
                                 onChange={(e) => setPublicProfile(e.target.checked)}
                              />
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
                           <SkillSelector selectedSkills={selectedSkills} setSelectedSkills={setSelectedSkills} />
                        </Col>
                     </Row>

                     <Row className="mt-4 justify-content-end">
                        <Col md="auto" className="button-group">
                           {isFormValid && (
                              <Button
                                 variant="primary"
                                 type="submit"
                                 style={{
                                    backgroundColor: "rgb(192, 23, 34)",
                                    borderColor: "rgb(192, 23, 34)",
                                 }}
                              >
                                 Next
                              </Button>
                           )}
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
