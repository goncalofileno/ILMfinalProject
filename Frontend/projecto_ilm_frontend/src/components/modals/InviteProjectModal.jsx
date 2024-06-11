import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, Alert } from 'react-bootstrap';
import Cookies from 'js-cookie';
import { getUserProjects, inviteUserToProject } from '../../utilities/services';

const InviteProjectModal = ({ show, handleClose, systemUsername }) => {
  const [projects, setProjects] = useState([]);
  const [selectedProject, setSelectedProject] = useState('');
  const [alertMessage, setAlertMessage] = useState('');
  const [alertVariant, setAlertVariant] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchProjects = async () => {
      const sessionId = Cookies.get('session-id');
      try {
        const response = await getUserProjects(sessionId);
        if (response && response.length > 0) {
          setProjects(response);
        } else {
          setAlertMessage('You do not have permissions to invite to any existing project.');
          setAlertVariant('danger');
        }
      } catch (error) {
        setAlertMessage('Failed to fetch projects. Please try again.');
        setAlertVariant('danger');
      }
    };

    if (show) {
      fetchProjects();
    }
  }, [show]);

  const handleInvite = async () => {
    const sessionId = Cookies.get('session-id');
    setLoading(true);
    setAlertMessage('');

    try {
      const response = await inviteUserToProject(sessionId, selectedProject, systemUsername);
      if (response.ok) {
        setAlertMessage('User invited successfully!');
        setAlertVariant('success');
        setTimeout(() => {
          handleCloseModal();
        }, 3000);
      } else if (response.status === 409) {
        const errorMessage = await response.text();
        setAlertMessage(errorMessage);
        setAlertVariant('danger');
      } else {
        setAlertMessage('Failed to invite user. Please try again.');
        setAlertVariant('danger');
      }
    } catch (error) {
      setAlertMessage('An error occurred. Please try again.');
      setAlertVariant('danger');
    } finally {
      setLoading(false);
    }
  };

  const handleCloseModal = () => {
    setAlertMessage('');
    setSelectedProject('');
    handleClose();
  };

  return (
    <Modal show={show} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title>Invite to Project</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {alertMessage && <Alert variant={alertVariant}>{alertMessage}</Alert>}
        {projects.length > 0 ? (
          <>
            <Form.Group className="mt-2"> {/* Adjust the top margin here */}
              <Form.Label>Select Project</Form.Label>
              <Form.Control
                as="select"
                value={selectedProject}
                onChange={(e) => setSelectedProject(e.target.value)}
                disabled={projects.length === 0}
              >
                <option value="">Select a project...</option>
                {projects.map((project) => (
                  <option key={project.name} value={project.name}>
                    {project.name}
                  </option>
                ))}
              </Form.Control>
            </Form.Group>
            <div className="d-flex justify-content-end mt-3">
              <Button
                variant="secondary"
                onClick={handleCloseModal}
                disabled={loading}
                style={{ marginRight: '10px' }}
              >
                Cancel
              </Button>
              <Button
                variant="primary"
                onClick={handleInvite}
                disabled={!selectedProject || loading}
                style={{ backgroundColor: '#f39c12', borderColor: '#f39c12' }}
              >
                {loading ? 'Inviting...' : 'Invite'}
              </Button>
            </div>
          </>
        ) : (
          <div className="d-flex justify-content-end mt-3">
            <Button variant="secondary" onClick={handleCloseModal}>
              Cancel
            </Button>
          </div>
        )}
      </Modal.Body>
    </Modal>
  );
};

export default InviteProjectModal;
