import React, { useEffect, useState, useRef } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  ListGroup,
  Image,
  Button,
  Form,
  Modal,
  Alert,
} from "react-bootstrap";
import { useParams, Link, useNavigate } from "react-router-dom";
import AppNavbar from "../components/headers/AppNavbar";
import ProjectTabs from "../components/headers/ProjectTabs";
import NotesBackgroud from "../resources/backgrounds/note_paper.png";
import {
  getProjectLogsAndNotes,
  markAsDone,
  createNote,
  getTasksSuggestions,
} from "../utilities/services";
import Cookies from "js-cookie";
import MemberLogIcon from "../resources/icons/logs/member-log-icon.png";
import TaskLogIcon from "../resources/icons/logs/member-log-icon.png";
import ProjectLogIcon from "../resources/icons/logs/project-log-icon.png";
import ResourceLogIcon from "../resources/icons/logs/resource-log-icon.png";
import "./ProjectLogsPage.css";

const ProjectLogsPage = () => {
  const { systemProjectName } = useParams();
  const navigate = useNavigate();
  const [logsAndNotes, setLogsAndNotes] = useState(null);
  const [tasksSuggestions, setTasksSuggestions] = useState([]);
  const [error, setError] = useState(null);
  const [showCreateNoteModal, setShowCreateNoteModal] = useState(false);
  const [newNoteText, setNewNoteText] = useState("");
  const [filteredSuggestions, setFilteredSuggestions] = useState([]);
  const [selectedTask, setSelectedTask] = useState(null);
  const [activeSuggestionIndex, setActiveSuggestionIndex] = useState(0);
  const suggestionsRef = useRef(null);

  useEffect(() => {
    const fetchData = async () => {
      const sessionId = Cookies.get("session-id");
      const result = await getProjectLogsAndNotes(sessionId, systemProjectName);
      if (result.error) {
        setError(result.error);
      } else {
        setLogsAndNotes(result);
      }
      const taskSuggestionsResult = await getTasksSuggestions(
        sessionId,
        systemProjectName
      );
      if (taskSuggestionsResult.error) {
        setError(taskSuggestionsResult.error);
      } else {
        setTasksSuggestions(taskSuggestionsResult);
      }
    };
    fetchData();
  }, [systemProjectName]);

  const handleMarkAsDone = async (noteId, done) => {
    const sessionId = Cookies.get("session-id");
    await markAsDone(sessionId, noteId, done);
    const result = await getProjectLogsAndNotes(sessionId, systemProjectName);
    setLogsAndNotes(result);
  };

  const handleCreateNote = async () => {
    const sessionId = Cookies.get("session-id");
    const noteDto = {
      text: newNoteText,
      date: new Date().toISOString(),
      done: false,
      authorName: "Current User", // You may want to replace this with the actual user's name
      authorPhoto: "https://www.example.com/photo.png", // Replace with actual user's photo URL
      taskSystemName: selectedTask ? selectedTask.systemTitle : null,
    };
    await createNote(sessionId, noteDto, systemProjectName);
    const result = await getProjectLogsAndNotes(sessionId, systemProjectName);
    setLogsAndNotes(result);
    setShowCreateNoteModal(false);
    setNewNoteText("");
    setSelectedTask(null);
  };

  const handleNoteTextChange = (e) => {
    const text = e.target.value;
    setNewNoteText(text);
    if (text.includes("@")) {
      const query = text.split("@").pop();
      setFilteredSuggestions(
        tasksSuggestions.filter((task) =>
          task.title.toLowerCase().includes(query.toLowerCase())
        )
      );
      setActiveSuggestionIndex(0);
    } else {
      setFilteredSuggestions([]);
    }
  };

  const handleSuggestionClick = (suggestion) => {
    const newText = newNoteText.split("@")[0] + `@${suggestion.systemTitle} `;
    setNewNoteText(newText);
    setSelectedTask(suggestion);
    setFilteredSuggestions([]);
  };

  const handleKeyDown = (e) => {
    if (filteredSuggestions.length > 0) {
      if (e.key === "ArrowDown") {
        e.preventDefault();
        setActiveSuggestionIndex(
          (prevIndex) => (prevIndex + 1) % filteredSuggestions.length
        );
        suggestionsRef.current.scrollIntoView({
          behavior: "smooth",
          block: "nearest",
        });
      } else if (e.key === "ArrowUp") {
        e.preventDefault();
        setActiveSuggestionIndex(
          (prevIndex) =>
            (prevIndex - 1 + filteredSuggestions.length) %
            filteredSuggestions.length
        );
        suggestionsRef.current.scrollIntoView({
          behavior: "smooth",
          block: "nearest",
        });
      } else if (e.key === "Enter") {
        e.preventDefault();
        handleSuggestionClick(filteredSuggestions[activeSuggestionIndex]);
      }
    }
  };

  const renderLogMessage = (log) => {
    switch (log.type) {
      case "MEMBER_ADDED":
        return (
          <>
            The member <strong>{log.receiver}</strong> was added to the project by <strong>{log.authorName}</strong>.
          </>
        );
      case "MEMBER_REMOVED":
        return (
          <>
            The member <strong>{log.receiver}</strong> was removed from the
            project.
          </>
        );
      case "TASKS_CREATED":
        return (
          <>
            The task <strong>{log.taskTitle}</strong> was created in the project
            plan.
          </>
        );
      case "TASKS_COMPLETED":
        return (
          <>
            The task <strong>{log.taskTitle}</strong> was marked as completed.
          </>
        );
      case "TASKS_IN_PROGRESS":
        return (
          <>
            The task <strong>{log.taskTitle}</strong> was started.
          </>
        );
      case "TASKS_DELETED":
        return (
          <>
            The task <strong>{log.taskTitle}</strong> was removed from the
            project.
          </>
        );
      case "TASKS_UPDATED":
        return (
          <>
            The data of the task <strong>{log.taskTitle}</strong> was updated.
          </>
        );
      case "PROJECT_INFO_UPDATED":
        return <>The project data was updated.</>;
      case "PROJECT_STATUS_UPDATED":
        return (
          <>
            The user <strong>{log.authorName}</strong> project status changed from{" "}
            <strong>{log.projectOldState}</strong> to{" "}
            <strong>{log.projectNewState}</strong>.
          </>
        );
      case "RESOURCES_ADDED":
        return (
          <>
            The resource <strong>{log.resourceName}</strong> was added to the
            project.
          </>
        );
      default:
        return "Unknown log type.";
    }
  };

  const renderLogIcon = (logType) => {
    switch (logType) {
      case "MEMBER_ADDED":
      case "MEMBER_REMOVED":
        return MemberLogIcon;
      case "TASKS_CREATED":
      case "TASKS_COMPLETED":
      case "TASKS_IN_PROGRESS":
      case "TASKS_DELETED":
      case "TASKS_UPDATED":
        return TaskLogIcon;
      case "PROJECT_INFO_UPDATED":
      case "PROJECT_STATUS_UPDATED":
        return ProjectLogIcon;
      case "RESOURCES_ADDED":
        return ResourceLogIcon;
      default:
        return null;
    }
  };

  const renderNoteText = (note) => {
    const [before, task] = note.text.split("@");
    const taskTitle = task ? task.split(" ")[0] : "";
    const after = task ? task.slice(taskTitle.length) : "";

    return (
      <span
        style={{
          textDecoration: note.done ? "line-through" : "none",
        }}
      >
        {before}
        {taskTitle && (
          <>
            <Link to={`/project/${systemProjectName}/tasks/${taskTitle}`}>
              @{taskTitle}
            </Link>
            {after}
          </>
        )}
      </span>
    );
  };

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!logsAndNotes) {
    return <div>Loading...</div>;
  }

  // Ordenar logs por data, mais recentes primeiro
  const sortedLogs = logsAndNotes.logs.sort((a, b) => new Date(b.date) - new Date(a.date));
  // Ordenar notas por data, mais recentes primeiro
  const sortedNotes = logsAndNotes.notes.sort((a, b) => new Date(b.date) - new Date(a.date));

  return (
    <>
      <AppNavbar />
      <div className="bckg-color-ilm-page ilm-pageb">
        <ProjectTabs
          typeOfUserSeingProject={logsAndNotes.typeOfUserSeingPage}
        />
        <Container style={{ height: "92%" }}>
          <Row>
            <Col>
              <h1>{logsAndNotes.projectName}</h1>
              <h5>Status: {logsAndNotes.projectStatus}</h5>
              {["CANCELED", "READY"].includes(logsAndNotes.projectStatus) && (
                <Alert variant="danger">
                  The project is {logsAndNotes.projectStatus.toLowerCase()} and no notes can be added or changes made.
                </Alert>
              )}
            </Col>
          </Row>
          <Row style={{ height: "80%" }}>
            <Col md={6} style={{ height: "100%" }}>
              <Card style={{ height: "100%", overflowY: "auto" }}>
                <Card.Header>Logs</Card.Header>
                <Card.Body>
                  <ListGroup variant="flush">
                    {sortedLogs.map((log) => (
                      <ListGroup.Item
                        key={log.id}
                        className="d-flex align-items-start"
                      >
                        <Row>
                          <Col md="auto">
                            <Image
                              src={renderLogIcon(log.type)}
                              width={30}
                              height={30}
                              className="mr-3"
                            />
                          </Col>
                          <Col>
                            <div>
                              <div>{renderLogMessage(log)}</div>
                              <div className="text-muted">
                                {new Date(log.date).toLocaleString()}
                              </div>
                            </div>
                          </Col>
                        </Row>
                      </ListGroup.Item>
                    ))}
                  </ListGroup>
                </Card.Body>
              </Card>
            </Col>
            <Col md={6} style={{ height: "100%"}}>
              <Row>
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
                  <div>Notes<div>
                  <div>
                    <ListGroup variant="flush">
                      {sortedNotes.map((note) => (
                        <ListGroup.Item
                          key={note.id}
                          style={{ background: "none", border: "none" }}
                        >
                          <Row>
                            <Col md={10}>
                              <Form.Check
                                type="checkbox"
                                label={renderNoteText(note)}
                                checked={note.done}
                                onChange={() =>
                                  handleMarkAsDone(note.id, !note.done)
                                }
                                disabled={["CANCELED", "READY"].includes(logsAndNotes.projectStatus)}
                              />
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
              </Row>
              <Row className="mt-2">
                <Button
                  style={{
                    backgroundColor: "#f39c12",
                    borderColor: "#f39c12",
                    width: "20%",
                  }}
                  onClick={() => setShowCreateNoteModal(true)}
                  disabled={["CANCELED", "READY"].includes(logsAndNotes.projectStatus)}
                >
                  Add Note
                </Button>
              </Row>
            </Col>
          </Row>
        </Container>
      </div>

      <Modal
        show={showCreateNoteModal}
        onHide={() => setShowCreateNoteModal(false)}
      >
        <Modal.Header closeButton>
          <Modal.Title>Create Note</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group>
            <Form.Label>Note</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={newNoteText}
              onChange={handleNoteTextChange}
              onKeyDown={handleKeyDown}
              placeholder="Type your note here. Use @ to mention a task."
            />
            {filteredSuggestions.length > 0 && (
              <ListGroup className="mt-2" ref={suggestionsRef}>
                {filteredSuggestions.map((suggestion, index) => (
                  <ListGroup.Item
                    key={suggestion.systemTitle}
                    onClick={() => handleSuggestionClick(suggestion)}
                    action
                    active={index === activeSuggestionIndex}
                  >
                    {suggestion.title}
                  </ListGroup.Item>
                ))}
              </ListGroup>
            )}
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="secondary"
            onClick={() => setShowCreateNoteModal(false)}
          >
            Cancel
          </Button>
          <Button
            style={{
              backgroundColor: "#f39c12",
              borderColor: "#f39c12",
            }}
            onClick={handleCreateNote}
            disabled={["CANCELED", "READY"].includes(logsAndNotes.projectStatus)}
          >
            Create Note
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ProjectLogsPage;
