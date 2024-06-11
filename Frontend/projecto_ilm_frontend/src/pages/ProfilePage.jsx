import React, { useEffect, useState } from 'react';
import AppNavbar from '../components/headers/AppNavbar';
import { useParams, useNavigate } from 'react-router-dom';
import { getUserProfile } from '../utilities/services';
import { Container, Row, Col, Card, Button, Alert } from 'react-bootstrap';
import Cookies from 'js-cookie';
import './ProfilePage.css'; // Make sure to create and include this CSS file.
import ComposeMailModal from '../components/modals/ComposeMailModal';
import InviteProjectModal from '../components/modals/InviteProjectModal.jsx'; // Import the InviteProjectModal component

const UserProfile = () => {
  const { systemUsername } = useParams();
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState(null);
  const [isPrivate, setIsPrivate] = useState(false);
  const [showComposeModal, setShowComposeModal] = useState(false); // State for mail modal visibility
  const [showInviteModal, setShowInviteModal] = useState(false); // State for invite modal visibility
  const [preFilledContact, setPreFilledContact] = useState(''); // State for pre-filled contact
  const navigate = useNavigate();

  const loggedInUsername = Cookies.get('user-systemUsername');

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const profileData = await getUserProfile(systemUsername);
        if (profileData.status === 200) {
          setProfile(profileData.data);
          if (
            !profileData.data.publicProfile &&
            (!profileData.data.projects || profileData.data.projects.length === 0)
          ) {
            setIsPrivate(true);
          }
        } else if (profileData.status === 401) {
          setError('Unauthorized: Invalid session.');
        } else if (profileData.status === 404) {
          setError('User not found.');
        }
      } catch (err) {
        setError('An error occurred while fetching the profile.');
      }
    };

    fetchProfile();
  }, [systemUsername]);

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  if (!profile) {
    return <div>Loading...</div>;
  }

  const handleEditProfile = () => {
    navigate('/editProfile');
  };

  const handleSendMessage = () => {
    const contact = `${profile.firstName} ${profile.lastName} <${profile.email}>`;
    setPreFilledContact(contact);
    setShowComposeModal(true);
  };

  const handleInvite = () => {
    setShowInviteModal(true);
  };

  const handleCloseComposeModal = () => {
    setShowComposeModal(false);
  };

  const handleCloseInviteModal = () => {
    setShowInviteModal(false);
  };

  const nonPendingProjects = profile.projects
    ? profile.projects.filter((project) => project.typeMember !== 'PENDING')
    : [];

  const pendingProjects = profile.projects
    ? profile.projects.filter((project) => project.typeMember === 'PENDING')
    : [];

  return (
    <>
      <AppNavbar />
      <div className="bckg-color-ilm-page ilm-pageb">
        <Container className="mt-4 outer-container">
          <Row className="justify-content-md-center">
            <Col md="8">
              <Card className="shadow-sm ilm-form">
                <Card.Body>
                  <Row>
                    <Col md="3" className="text-center">
                      <div className="profile-avatar mb-3">
                        <img
                          src={profile.profileImage || 'https://via.placeholder.com/150'}
                          alt="Profile"
                          className="img-fluid rounded-circle"
                        />
                      </div>
                    </Col>
                    <Col md="9">
                      <Card.Title className="form-title">
                        {profile.firstName} {profile.lastName}
                      </Card.Title>
                      <Card.Subtitle className="mb-2 text-muted">{profile.username}</Card.Subtitle>
                      <Card.Text>
                        <strong>Location:</strong> {profile.location}
                      </Card.Text>
                      {!isPrivate && profile.bio && (
                        <Card.Text>
                          <strong>Bio:</strong> {profile.bio}
                        </Card.Text>
                      )}
                      {loggedInUsername === systemUsername ? (
                        <Button
                          variant="primary"
                          onClick={handleEditProfile}
                          className="submit-button"
                          style={{ padding: '10px 20px' }}
                        >
                          Edit Profile
                        </Button>
                      ) : (
                        <div className="button-group">
                          <Button
                            variant="primary"
                            className="mr-3 submit-button"
                            style={{ padding: '10px 20px' }}
                            onClick={handleSendMessage}
                          >
                            Send Email
                          </Button>
                          <Button
                            variant="secondary"
                            className="invite-button"
                            style={{ padding: '10px 20px' }}
                            onClick={handleInvite}
                          >
                            Invite to Project
                          </Button>
                        </div>
                      )}
                    </Col>
                  </Row>
                  {isPrivate && loggedInUsername !== systemUsername ? (
                    <Alert variant="warning" className="mt-4">
                      This user profile is private.
                    </Alert>
                  ) : (
                    <>
                      <Row>
                        <Col md={loggedInUsername === systemUsername ? 6 : 12}>
                          <Row className="mt-4">
                            <Col md={12}>
                              <h3 className="page-title">Projects</h3>
                            </Col>
                          </Row>
                          <Card className="shadow-sm mb-4 card-container">
                            <Card.Body className="card-body">
                              {nonPendingProjects.length > 0 ? (
                                nonPendingProjects.map((project) => (
                                  <div key={project.name} className="ilm-form mb-3">
                                    <p className="mb-1">
                                      <strong>Project Name:</strong> {project.name}
                                    </p>
                                    <p className="mb-1">
                                      <strong>Type Member:</strong> {project.typeMember}
                                    </p>
                                    <p className="mb-1">
                                      <strong>Status:</strong> {project.status}
                                    </p>
                                  </div>
                                ))
                              ) : (
                                <p>No projects available.</p>
                              )}
                            </Card.Body>
                          </Card>
                        </Col>
                        {loggedInUsername === systemUsername && (
                          <Col md="6">
                            <Row className="mt-4">
                              <Col md={12}>
                                <h3 className="page-title">Applications</h3>
                              </Col>
                            </Row>
                            <Card className="shadow-sm mb-4 card-container">
                              <Card.Body className="card-body">
                                {pendingProjects.length > 0 ? (
                                  pendingProjects.map((project) => (
                                    <div key={project.name} className="ilm-form mb-3">
                                      <p className="mb-1">
                                        <strong>Project Name:</strong> {project.name}
                                      </p>
                                      <p className="mb-1">
                                        <strong>Status:</strong> {project.status}
                                      </p>
                                    </div>
                                  ))
                                ) : (
                                  <p>No applications available.</p>
                                )}
                              </Card.Body>
                            </Card>
                          </Col>
                        )}
                      </Row>

                      <Row>
                        <Col md="6">
                          <Row className="mt-4">
                            <Col md={12}>
                              <h3 className="page-title">Skills</h3>
                            </Col>
                          </Row>
                          <Card className="shadow-sm mb-4 card-container">
                            <Card.Body className="card-body">
                              {profile.skills && profile.skills.length > 0 ? (
                                profile.skills.map((skill) => (
                                  <div key={skill.id} className="ilm-form mb-3">
                                    <p className="mb-1">
                                      <strong>{skill.name}</strong>
                                    </p>
                                    <p className="text-muted mb-1" style={{ fontSize: '0.85em' }}>
                                      {skill.type}
                                    </p>
                                  </div>
                                ))
                              ) : (
                                <p>No skills available.</p>
                              )}
                            </Card.Body>
                          </Card>
                        </Col>
                        <Col md="6">
                          <Row className="mt-4">
                            <Col md={12}>
                              <h3 className="page-title">Interests</h3>
                            </Col>
                          </Row>
                          <Card className="shadow-sm mb-4 card-container">
                            <Card.Body className="card-body">
                              {profile.interests && profile.interests.length > 0 ? (
                                profile.interests.map((interest) => (
                                  <div key={interest.id} className="ilm-form mb-3">
                                    {interest.name}
                                  </div>
                                ))
                              ) : (
                                <p>No interests available.</p>
                              )}
                            </Card.Body>
                          </Card>
                        </Col>
                      </Row>
                    </>
                  )}
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </div>
      <ComposeMailModal show={showComposeModal} handleClose={handleCloseComposeModal} preFilledContact={preFilledContact} />
      <InviteProjectModal show={showInviteModal} handleClose={handleCloseInviteModal} systemUsername={systemUsername} />
    </>
  );
};

export default UserProfile;
