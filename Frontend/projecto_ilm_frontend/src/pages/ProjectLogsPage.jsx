import React, { useEffect, useState } from "react";
import { Container, Row, Col, Card, ListGroup, Image } from "react-bootstrap";
import { useParams } from "react-router-dom";
import AppNavbar from "../components/headers/AppNavbar";
import ProjectTabs from "../components/headers/ProjectTabs";
import NotesBackgroud from "../resources/backgrounds/note_paper.png";
import { getProjectLogsAndNotes } from "../utilities/services";
import Cookies from "js-cookie";
import "./ProjectLogsPage.css";

const ProjectLogsPage = () => {
  const { systemProjectName } = useParams();
  const [logsAndNotes, setLogsAndNotes] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      const sessionId = Cookies.get("session-id");
      const result = await getProjectLogsAndNotes(sessionId, systemProjectName);
      if (result.error) {
        setError(result.error);
      } else {
        setLogsAndNotes(result);
      }
    };
    fetchData();
  }, [systemProjectName]);

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!logsAndNotes) {
    return <div>Loading...</div>;
  }

  const renderLogMessage = (log) => {
    switch (log.type) {
      case "MEMBER_ADDED":
        return `The member ${log.receiver} was added to the project.`;
      case "MEMBER_REMOVED":
        return `The member ${log.receiver} was removed from the project.`;
      case "TASKS_CREATED":
        return `The task ${log.taskTitle} was created in the project plan.`;
      case "TASKS_COMPLETED":
        return `The task ${log.taskTitle} was marked as completed.`;
      case "TASKS_IN_PROGRESS":
        return `The task ${log.taskTitle} was started.`;
      case "TASKS_DELETED":
        return `The task ${log.taskTitle} was removed from the project.`;
      case "TASKS_UPDATED":
        return `The data of the task ${log.taskTitle} was updated.`;
      case "PROJECT_INFO_UPDATED":
        return `The project data was updated.`;
      case "PROJECT_STATUS_UPDATED":
        return `The project status changed from ${log.projectOldState} to ${log.projectNewState}.`;
      case "RESOURCES_ADDED":
        return `The resource ${log.resourceName} was added to the project.`;
      default:
        return "Unknown log type.";
    }
  };

  return (
    <>
      <AppNavbar />
      <div className="bckg-color-ilm-page ilm-pageb">
        <ProjectTabs
          typeOfUserSeingProject={logsAndNotes.typeOfUserSeingPage}
        />
        <Container style={{ height: "100%" }}>
          <Row>
            <Col>
              <h1>{logsAndNotes.projectName}</h1>
            </Col>
          </Row>
          <Row style={{ height: "80%" }}>
            <Col md={6} style={{ height: "100%" }}>
              <Card style={{ height: "100%", overflowY: "auto" }}>
                <Card.Header>Logs</Card.Header>
                <Card.Body>
                  <ListGroup variant="flush">
                    {logsAndNotes.logs.map((log) => (
                      <ListGroup.Item key={log.id}>
                        <Row>
                          <Col md={2}>
                            <Image src={log.authorPhoto} roundedCircle />
                          </Col>
                          <Col md={10}>
                            <div>{renderLogMessage(log)}</div>
                            <div className="text-muted">
                              {new Date(log.date).toLocaleString()}
                            </div>
                          </Col>
                        </Row>
                      </ListGroup.Item>
                    ))}
                  </ListGroup>
                </Card.Body>
              </Card>
            </Col>
            <Col md={6} style={{ height: "100%" }}>
              <div
                style={{
                  backgroundImage: `url(${NotesBackgroud})`,
                  backgroundSize: "cover",
                  height: "100%",
                  paddingTop: "100px",
                  paddingLeft: "50px",
                  paddingRight: "50px",
                  paddingBottom: "20px",
                }}
              >
                <div>Notes</div>
                <div>
                  <ListGroup variant="flush">
                    {logsAndNotes.notes.map((note) => (
                      <ListGroup.Item
                        key={note.id}
                        style={{ background: "none", border: "none" }}
                      >
                        <Row>
                          <Col md={10}>
                            <div>{note.text}</div>
                            <div className="text-muted">
                              {new Date(note.date).toLocaleString()}
                            </div>
                          </Col>
                        </Row>
                      </ListGroup.Item>
                    ))}
                  </ListGroup>
                </div>
              </div>
            </Col>
          </Row>
        </Container>
      </div>
    </>
  );
};

export default ProjectLogsPage;
