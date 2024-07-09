import React, { useState, useEffect } from "react";
import { Modal, Button, Form, Alert } from "react-bootstrap";
import Cookies from "js-cookie";
import { getUserProjects, inviteUserToProject } from "../../utilities/services";
import { Trans, t } from "@lingui/macro";

const InviteProjectModal = ({ show, handleClose, systemUsername }) => {
  const [projects, setProjects] = useState([]);
  const [selectedProject, setSelectedProject] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertVariant, setAlertVariant] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchProjects = async () => {
      const sessionId = Cookies.get("session-id");
      try {
        const response = await getUserProjects(sessionId, systemUsername);
        if (response.success) {
          setProjects(response.data);
        } else {
          setAlertMessage(response.message);
          setAlertVariant("danger");
        }
      } catch (error) {
        setAlertMessage(t`Failed to fetch projects. Please try again.`);
        setAlertVariant("danger");
      }
    };

    if (show) {
      setProjects([]);
      setSelectedProject("");
      fetchProjects();
    }
  }, [show, systemUsername]);

  const handleInvite = async () => {
    const sessionId = Cookies.get("session-id");
    setLoading(true);
    setAlertMessage("");

    try {
      const response = await inviteUserToProject(
        sessionId,
        selectedProject,
        systemUsername
      );
      if (response.ok) {
        setAlertMessage(t`User invited successfully!`);
        setAlertVariant("success");
        setTimeout(() => {
          handleCloseModal();
        }, 3000);
      } else if (response.status === 409) {
        const errorMessage = await response.text();
        setAlertMessage(errorMessage);
        setAlertVariant("danger");
      } else {
        setAlertMessage(t`Failed to invite user. Please try again.`);
        setAlertVariant("danger");
      }
    } catch (error) {
      setAlertMessage(t`An error occurred. Please try again.`);
      setAlertVariant("danger");
    } finally {
      setLoading(false);
    }
  };

  const handleCloseModal = () => {
    setAlertMessage("");
    setSelectedProject("");
    handleClose();
  };

  return (
    <Modal show={show} onHide={handleCloseModal}>
      <Modal.Header closeButton>
        <Modal.Title>
          <Trans>Invite to Project</Trans>
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {alertMessage && <Alert variant={alertVariant}>{alertMessage}</Alert>}
        {projects.length > 0 ? (
          <>
            <Form.Group className="mt-2">
              <Form.Label>
                <Trans>Select Project</Trans>
              </Form.Label>
              <Form.Control
                as="select"
                value={selectedProject}
                onChange={(e) => setSelectedProject(e.target.value)}
              >
                <option value="">
                  <Trans>Select a project</Trans>...
                </option>
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
                style={{ marginRight: "10px" }}
              >
                <Trans>Cancel</Trans>
              </Button>
              <Button
                variant="primary"
                onClick={handleInvite}
                disabled={!selectedProject || loading}
                style={{ backgroundColor: "#f39c12", borderColor: "#f39c12" }}
              >
                {loading ? t`Inviting...` : t`Invite`}
              </Button>
            </div>
          </>
        ) : (
          <div className="d-flex justify-content-end mt-3">
            <Button variant="secondary" onClick={handleCloseModal}>
              <Trans>Cancel</Trans>
            </Button>
          </div>
        )}
      </Modal.Body>
    </Modal>
  );
};

export default InviteProjectModal;
