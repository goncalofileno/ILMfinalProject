import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getUserProfile } from '../utilities/services'; // Importe a função do serviço
import { Container, Row, Col, Card, Button, Alert } from 'react-bootstrap';

const UserProfile = () => {
  const { systemUsername } = useParams(); // Obtém o systemUsername da URL
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState(null);
  const [isPrivate, setIsPrivate] = useState(false);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const profileData = await getUserProfile(systemUsername);
        if (profileData.status === 200) {
          setProfile(profileData.data);
          if (!profileData.data.publicProfile && (!profileData.data.projects || profileData.data.projects.length === 0)) {
            setIsPrivate(true);
          }
        } else if (profileData.status === 401) {
          setError("Unauthorized: Invalid session.");
        } else if (profileData.status === 404) {
          setError("User not found.");
        }
      } catch (err) {
        setError("An error occurred while fetching the profile.");
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

  return (
    <Container>
      <Row className="justify-content-md-center mt-4">
        <Col md="8">
          <Card>
            <Card.Body>
              <Row>
                <Col md="3" className="text-center">
                  <div className="profile-avatar">
                    <img
                      src={profile.profileImage || "https://via.placeholder.com/150"}
                      alt="Profile"
                      className="img-fluid rounded-circle"
                    />
                  </div>
                </Col>
                <Col md="9">
                  <Card.Title>{profile.firstName} {profile.lastName}</Card.Title>
                  <Card.Subtitle className="mb-2 text-muted">
                    {profile.username}
                  </Card.Subtitle>
                  <Card.Text>
                    <strong>Location:</strong> {profile.location}
                  </Card.Text>
                  {!isPrivate && profile.bio && (
                    <Card.Text>
                      <strong>Bio:</strong> {profile.bio}
                    </Card.Text>
                  )}
                  <Button variant="primary" className="mr-2">Send Message</Button>
                  <Button variant="secondary">Invite to Project</Button>
                </Col>
              </Row>
              {isPrivate ? (
                <Alert variant="warning" className="mt-4">This user profile is private.</Alert>
              ) : (
                <>
                  <Row className="mt-4">
                    <Col md="12">
                      <Card>
                        <Card.Body>
                          <Card.Title>Projects</Card.Title>
                          {profile.projects && profile.projects.map(project => (
                            <div key={project.name}>
                              <p><strong>Project Name:</strong> {project.name}</p>
                              <p><strong>Type Member:</strong> {project.typeMember}</p>
                              <p><strong>Status:</strong> {project.status}</p>
                            </div>
                          ))}
                        </Card.Body>
                      </Card>
                    </Col>
                  </Row>
                  <Row className="mt-4">
                    <Col md="6">
                      <Card>
                        <Card.Body>
                          <Card.Title>Skills</Card.Title>
                          {profile.skills && profile.skills.map(skill => (
                            <div key={skill.id}>
                              <p><strong>{skill.name}</strong></p>
                              <p className="text-muted" style={{ fontSize: '0.85em' }}>{skill.type}</p>
                            </div>
                          ))}
                        </Card.Body>
                      </Card>
                    </Col>
                    <Col md="6">
                      <Card>
                        <Card.Body>
                          <Card.Title>Interests</Card.Title>
                          {profile.interests && profile.interests.map(interest => (
                            <div key={interest.id}>{interest.name}</div>
                          ))}
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
  );
};

export default UserProfile;
