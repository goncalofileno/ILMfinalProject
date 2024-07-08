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
import TaskLogIcon from "../resources/icons/logs/task-log-icon.png";
import ProjectLogIcon from "../resources/icons/logs/project-log-icon.png";
import ResourceLogIcon from "../resources/icons/logs/resource-log-icon.png";
import "./ProjectLogsPage.css";
import { formatStatusDropDown } from "../utilities/converters";
import StandardModal from "../components/modals/StandardModal";
import { Trans, t } from "@lingui/macro";
import { useMediaQuery } from "react-responsive";

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
  const [currentLanguage, setCurrentLanguage] = useState(
    Cookies.get("user-language") || "ENGLISH"
  );
  const isMobile = useMediaQuery({ query: "(max-width: 767px)" });
  const isTablet = useMediaQuery({ query: "(max-width: 1024px)" });

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
  }, [systemProjectName, currentLanguage]);

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
      authorName: "Current User",
      authorPhoto: "https://www.example.com/photo.png",
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

  const logMessagesEn = {
    MEMBER_ADDED: (log) => (
      <>
        The member <strong>{log.receiver}</strong> was added to the project by{" "}
        <strong>{log.authorName}</strong>.
      </>
    ),
    MEMBER_REMOVED: (log) => (
      <>
        The member <strong>{log.receiver}</strong> was removed from the project.
      </>
    ),
    TASKS_CREATED: (log) => (
      <>
        The task <strong>{log.taskTitle}</strong> was created in the project
        plan.
      </>
    ),
    TASKS_COMPLETED: (log) => (
      <>
        The task <strong>{log.taskTitle}</strong> was marked as completed.
      </>
    ),
    TASKS_IN_PROGRESS: (log) => (
      <>
        The task <strong>{log.taskTitle}</strong> was started.
      </>
    ),
    TASKS_DELETED: (log) => (
      <>
        The task <strong>{log.taskTitle}</strong> was removed from the project.
      </>
    ),
    TASKS_UPDATED: (log) => (
      <>
        The data of the task <strong>{log.taskTitle}</strong> was updated.
      </>
    ),
    PROJECT_INFO_UPDATED: () => <>The project data was updated.</>,
    PROJECT_STATUS_UPDATED: (log) => (
      <>
        The user <strong>{log.authorName}</strong> project status changed from{" "}
        <strong>{log.projectOldState}</strong> to{" "}
        <strong>{log.projectNewState}</strong>.
      </>
    ),
    RESOURCES_UPDATED: (log) => (
      <>
        The resources in the project were updated by{" "}
        <strong>{log.authorName}</strong>.
      </>
    ),
    MEMBER_TYPE_CHANGED: (log) => (
      <>
        The user <strong>{log.receiver}</strong> user type was changed from{" "}
        <strong>{log.memberOldType}</strong> to{" "}
        <strong>{log.memberNewType}</strong> by{" "}
        <strong>{log.authorName}</strong>.
      </>
    ),
    MEMBER_LEFT: (log) => (
      <>
        The user <strong>{log.authorName}</strong> left the project.
      </>
    ),
    default: () => "Unknown log type.",
  };

  const logMessagesPt = {
    MEMBER_ADDED: (log) => (
      <>
        O membro <strong>{log.receiver}</strong> foi adicionado ao projeto por{" "}
        <strong>{log.authorName}</strong>.
      </>
    ),
    MEMBER_REMOVED: (log) => (
      <>
        O membro <strong>{log.receiver}</strong> foi removido do projeto.
      </>
    ),
    TASKS_CREATED: (log) => (
      <>
        A tarefa <strong>{log.taskTitle}</strong> foi criada no plano do
        projeto.
      </>
    ),
    TASKS_COMPLETED: (log) => (
      <>
        A tarefa <strong>{log.taskTitle}</strong> foi marcada como concluída.
      </>
    ),
    TASKS_IN_PROGRESS: (log) => (
      <>
        A tarefa <strong>{log.taskTitle}</strong> foi iniciada.
      </>
    ),
    TASKS_DELETED: (log) => (
      <>
        A tarefa <strong>{log.taskTitle}</strong> foi removida do projeto.
      </>
    ),
    TASKS_UPDATED: (log) => (
      <>
        Os dados da tarefa <strong>{log.taskTitle}</strong> foram atualizados.
      </>
    ),
    PROJECT_INFO_UPDATED: () => <>Os dados do projeto foram atualizados.</>,
    PROJECT_STATUS_UPDATED: (log) => (
      <>
        O estado do projeto do utilizador <strong>{log.authorName}</strong>{" "}
        mudou de <strong>{log.projectOldState}</strong> para{" "}
        <strong>{log.projectNewState}</strong>.
      </>
    ),
    RESOURCES_UPDATED: (log) => (
      <>
        Os recursos no projeto foram atualizados por{" "}
        <strong>{log.authorName}</strong>.
      </>
    ),
    MEMBER_TYPE_CHANGED: (log) => (
      <>
        O tipo de utilizador do membro <strong>{log.receiver}</strong> foi
        alterado de <strong>{log.memberOldType}</strong> para{" "}
        <strong>{log.memberNewType}</strong> por{" "}
        <strong>{log.authorName}</strong>.
      </>
    ),
    MEMBER_LEFT: (log) => (
      <>
        O utilizador <strong>{log.authorName}</strong> deixou o projeto.
      </>
    ),
    default: () => "Tipo de log desconhecido.",
  };

  const renderLogMessage = (log) => {
    const userLanguage = Cookies.get("user-language") || "ENGLISH";
    const messageFunc =
      userLanguage === "PORTUGUESE"
        ? logMessagesPt[log.type]
        : logMessagesEn[log.type];
    return messageFunc
      ? messageFunc(log)
      : userLanguage === "PORTUGUESE"
      ? logMessagesPt.default()
      : logMessagesEn.default();
  };

  const logIcons = {
    MEMBER_ADDED: MemberLogIcon,
    MEMBER_REMOVED: MemberLogIcon,
    MEMBER_TYPE_CHANGED: MemberLogIcon,
    MEMBER_LEFT: MemberLogIcon,
    TASKS_CREATED: TaskLogIcon,
    TASKS_COMPLETED: TaskLogIcon,
    TASKS_IN_PROGRESS: TaskLogIcon,
    TASKS_DELETED: TaskLogIcon,
    TASKS_UPDATED: TaskLogIcon,
    PROJECT_INFO_UPDATED: ProjectLogIcon,
    PROJECT_STATUS_UPDATED: ProjectLogIcon,
    RESOURCES_UPDATED: ResourceLogIcon,
  };

  const renderLogIcon = (logType) => logIcons[logType] || null;

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
            <Link to={`/project/${systemProjectName}/plan/${taskTitle}`}>
              @{taskTitle}
            </Link>
            {after}
          </>
        )}
      </span>
    );
  };

  if (error) {
    return (
      <div>
        <Trans>Error</Trans>: {error}
      </div>
    );
  }

  if (!logsAndNotes) {
    return (
      <div>
        <Trans>Loading...</Trans>
      </div>
    );
  }

  // Ordenar logs por data, mais recentes primeiro
  const sortedLogs = logsAndNotes.logs.sort(
    (a, b) => new Date(b.date) - new Date(a.date)
  );
  // Ordenar notas por data, mais recentes primeiro
  const sortedNotes = logsAndNotes.notes.sort(
    (a, b) => new Date(b.date) - new Date(a.date)
  );

  return (
    <>
      <AppNavbar setCurrentLanguage={setCurrentLanguage} />
      <div
        className={
          !isMobile ? "bckg-color-ilm-page ilm-pageb" : "ilm-page-mobile"
        }
      >
        <ProjectTabs
          typeOfUserSeingProject={logsAndNotes.typeOfUserSeingPage}
          projectName={logsAndNotes.projectName}
        />
        <Container
          id="container-project-logs-page"
          style={{
            height: !isMobile ? "89%" : "140vh",
            marginTop: "1%",
            paddingRight: "0",
          }}
        >
          <Row>
            <Col>
              <h5>
                Status: {formatStatusDropDown(logsAndNotes.projectStatus)}
              </h5>
              {["CANCELED", "READY"].includes(logsAndNotes.projectStatus) && (
                <Alert variant="danger" className="standard-modal">
                  <Trans>
                    The project is {logsAndNotes.projectStatus.toLowerCase()}{" "}
                    and no notes can be added or changes made.
                  </Trans>
                </Alert>
              )}
            </Col>
          </Row>
          <Row style={{ height: !isMobile ? "80%" : "400px" }}>
            <Col
              sm={12}
              md={6}
              style={{ height: "100%", marginBottom: isMobile && "40px" }}
            >
              <Card style={{ height: "100%" }}>
                <Card.Header>
                  <h4>
                    <Trans>Logs</Trans>
                  </h4>
                </Card.Header>
                <Card.Body className="logs-list">
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
            <Col sm={12} md={6} style={{ height: "100%" }}>
              <Row style={{ height: "100%" }}>
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
                  <Row style={{ height: "100%" }}>
                    <Col xs={9} sm={9} style={{ height: "100%" }}>
                      <h4>
                        <Trans>Notes</Trans>
                      </h4>
                      <div
                        style={{
                          height: "95%",
                        }}
                      >
                        <Row className="note-list">
                          <Col
                            md={10}
                            style={{ height: "100%", width: "100%" }}
                          >
                            <ListGroup variant="flush">
                              {sortedNotes.map((note) => (
                                <ListGroup.Item
                                  key={note.id}
                                  style={{ background: "none", border: "none" }}
                                >
                                  <Form.Check
                                    type="checkbox"
                                    label={renderNoteText(note)}
                                    checked={note.done}
                                    onChange={() =>
                                      handleMarkAsDone(note.id, !note.done)
                                    }
                                    disabled={["CANCELED", "READY"].includes(
                                      logsAndNotes.projectStatus
                                    )}
                                  />
                                  <div className="text-muted">
                                    {new Date(note.date).toLocaleString()}
                                  </div>
                                </ListGroup.Item>
                              ))}
                            </ListGroup>
                          </Col>
                        </Row>
                      </div>
                    </Col>
                    <Col
                      xs={3}
                      sm={3}
                      style={{
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "end",
                        height: "100%",
                      }}
                    >
                      {" "}
                      <div id="add-note-button-div">
                        <Button
                          style={{
                            backgroundColor: "#f39c12",
                            borderColor: "#f39c12",
                            width: "100%",
                          }}
                          onClick={() => setShowCreateNoteModal(true)}
                          disabled={["CANCELED", "READY"].includes(
                            logsAndNotes.projectStatus
                          )}
                        >
                          Add {!isTablet && "Note"}
                        </Button>
                      </div>
                    </Col>
                  </Row>
                </div>
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
          <Modal.Title>
            <Trans>Create Note</Trans>
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group>
            <Form.Label>
              <Trans>Note</Trans>
            </Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={newNoteText}
              onChange={handleNoteTextChange}
              onKeyDown={handleKeyDown}
              placeholder={t`Type your note here. Use @ to mention a task.`}
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
            <Trans>Cancel</Trans>
          </Button>
          <Button
            style={{
              backgroundColor: "#f39c12",
              borderColor: "#f39c12",
            }}
            onClick={handleCreateNote}
            disabled={["CANCELED", "READY"].includes(
              logsAndNotes.projectStatus
            )}
          >
            <Trans>Create Note</Trans>
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ProjectLogsPage;
