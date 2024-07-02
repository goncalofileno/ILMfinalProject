import React, { useState, useEffect, useRef } from "react";
import {
  Container,
  Row,
  Col,
  Card,
  Form,
  Button,
  Alert,
} from "react-bootstrap";
import { useParams, useNavigate } from "react-router-dom";
import AppNavbar from "../components/headers/AppNavbar";
import ProjectTabs from "../components/headers/ProjectTabs";
import useChatStore from "../stores/useChatStore";
import ProjectChatWebSocket from "../utilities/websockets/ProjectChatWebSocket";
import Cookies from "js-cookie";
import moment from "moment";
import { getChatPage, sendChatMessage } from "../utilities/services";
import "./ProjectChatPage.css";

const ProjectChatPage = () => {
  const { systemProjectName } = useParams();
  const navigate = useNavigate();
  const { messages, setMessages, addMessage, onlineMembers } = useChatStore();
  const [messageContent, setMessageContent] = useState("");
  const [projectMembers, setProjectMembers] = useState([]);
  const [projectState, setProjectState] = useState(null);
  const [projectName, setProjectName] = useState("");
  const [typeOfUserSeingTheProject, setTypeOfUserSeingTheProject] =
    useState("");
  const chatBodyRef = useRef(null);
  const userSystemUsername = Cookies.get("user-systemUsername");

  useEffect(() => {
    const fetchChatPage = async () => {
      const sessionId = Cookies.get("session-id");
      const response = await getChatPage(sessionId, systemProjectName);
      console.log("Chat Page Response:", response);
      if (response && !response.error) {
        setMessages(response.messages || []);
        setProjectMembers(response.projectMembers || []);
        setProjectState(response.stateProject || null);
        setProjectName(response.projectName || "");
        setTypeOfUserSeingTheProject(response.typeOfUserSeingTheProject || "");
      } else {
        console.error("Error fetching chat page:", response.error);
      }
    };

    fetchChatPage();
  }, [systemProjectName, setMessages]);

  useEffect(() => {
    if (chatBodyRef.current) {
      chatBodyRef.current.scrollTop = chatBodyRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSendMessage = async () => {
    if (messageContent.trim() !== "") {
      const sessionId = Cookies.get("session-id");
      sendChatMessage(sessionId, systemProjectName, messageContent);
      setMessageContent("");
    }
  };

  const formatDate = (date) => {
    const now = moment();
    const messageDate = moment(date);

    if (now.diff(messageDate, "days") < 1) {
      return messageDate.fromNow();
    } else {
      return messageDate.format("HH:mm DD MMM YYYY");
    }
  };

  const isOnline = (systemUsername) => {
    return onlineMembers.some(
      (member) => member && member.systemUsername === systemUsername
    );
  };

  return (
    <>
      <AppNavbar />
      <ProjectChatWebSocket projectId={systemProjectName} />
      <div className="ilm-pageb">
        <ProjectTabs
          typeOfUserSeingProject={typeOfUserSeingTheProject}
          projectName={projectName}
        />
        <Container style={{ height: "85%", marginTop: "20px" }}>
          {projectState === "CANCELED" && (
            <Row>
              <Col>
                <Alert variant="danger" className="standard-modal">
                  The project is canceled, and the chat is disabled.
                </Alert>
              </Col>
            </Row>
          )}

          <Row style={{ height: "100%" }}>
            <Col md={4} style={{ height: "100%" }}>
              <Card style={{ height: "100%" }}>
                <Card.Header>
                  <h5>Members</h5>
                </Card.Header>
                <Card.Body
                  style={{ height: "100%" }}
                  id="list-user-project-profile"
                >
                  <ul className="list-group list-group-flush">
                    {projectMembers.map((member) => (
                      <li
                        className="list-group-item"
                        key={member.systemUsername}
                        onClick={() =>
                          navigate(`/profile/${member.systemUsername}`)
                        }
                        style={{ cursor: "pointer" }}
                      >
                        <Row>
                          <Col md="auto">
                            <img
                              src={member.profilePicture}
                              alt={member.name}
                              width={30}
                              height={30}
                              className="mr-3 rounded-circle"
                            />
                          </Col>
                          <Col>
                            <div>
                              <strong>
                                {member.systemUsername === userSystemUsername
                                  ? "You"
                                  : member.name}
                              </strong>
                              <div className="text-muted">{member.type}</div>
                            </div>
                          </Col>
                          <Col md="auto" className="d-flex align-items-center">
                            <div
                              className={`status-indicator ${
                                isOnline(member.systemUsername)
                                  ? "online"
                                  : "offline"
                              }`}
                            />
                            <span
                              className={`status-label ${
                                isOnline(member.systemUsername)
                                  ? "online-label"
                                  : "offline-label"
                              }`}
                            >
                              {isOnline(member.systemUsername)
                                ? "Online"
                                : "Offline"}
                            </span>
                          </Col>
                        </Row>
                      </li>
                    ))}
                  </ul>
                </Card.Body>
              </Card>
            </Col>
            <Col md={8} style={{ height: "100%" }}>
              <Card style={{ height: "100%" }}>
                <Card.Header>
                  <h5>Chat</h5>
                </Card.Header>
                <Card.Body
                  style={{ height: "85%", overflowY: "auto" }}
                  ref={chatBodyRef}
                  id="chat-project-profile"
                >
                  <ul className="list-group list-group-flush">
                    {messages.map((message, index) => (
                      <li className="list-group-item" key={index}>
                        <div
                          className={`message-container ${
                            message.systemUsername === userSystemUsername
                              ? "right"
                              : "left"
                          }`}
                        >
                          <img
                            src={message.photo}
                            alt={message.name}
                            className={`message-avatar ${
                              message.systemUsername === userSystemUsername
                                ? "right-avatar"
                                : "left-avatar"
                            }`}
                          />
                          <div
                            className={`message-content ${
                              message.systemUsername === userSystemUsername
                                ? "right"
                                : "left"
                            }`}
                          >
                            <strong>
                              {message.systemUsername === userSystemUsername
                                ? "You"
                                : message.name}
                            </strong>
                            : {message.message}
                            <div className="text-muted message-date">
                              {formatDate(message.date)}
                            </div>
                          </div>
                        </div>
                      </li>
                    ))}
                  </ul>
                </Card.Body>
                <Card.Footer>
                  <Form
                    onSubmit={(e) => {
                      e.preventDefault();
                      if (projectState !== "CANCELED") {
                        handleSendMessage();
                      }
                    }}
                  >
                    <Form.Group>
                      <Form.Control
                        as="textarea"
                        rows={1}
                        value={messageContent}
                        onChange={(e) => setMessageContent(e.target.value)}
                        placeholder="Type a message..."
                        className="message-input"
                        disabled={projectState === "CANCELED"}
                      />
                    </Form.Group>
                    <Button
                      style={{
                        backgroundColor: "#f39c12",
                        borderColor: "#f39c12",
                        marginTop: "5px",
                        display: "flex-end",
                        float: "right",
                      }}
                      type="submit"
                      disabled={projectState === "CANCELED"}
                    >
                      Send
                    </Button>
                  </Form>
                </Card.Footer>
              </Card>
            </Col>
          </Row>
        </Container>
      </div>
    </>
  );
};

export default ProjectChatPage;
