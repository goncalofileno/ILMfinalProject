import React, { useState, useEffect, useRef } from "react";
import { Container, Row, Col, Card, Form, Button } from "react-bootstrap";
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
  const chatBodyRef = useRef(null);

  useEffect(() => {
    const fetchChatPage = async () => {
      const sessionId = Cookies.get("session-id");
      const response = await getChatPage(sessionId, systemProjectName);
      console.log("Chat Page Response:", response);
      if (response && !response.error) {
        setMessages(response.messages || []);
        setProjectMembers(response.projectMembers || []);
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
    return onlineMembers.some(member => member && member.systemUsername === systemUsername);
  };

  return (
    <>
      <AppNavbar />
      <ProjectChatWebSocket projectId={systemProjectName} />
      <div className="bckg-color-ilm-page ilm-pageb">
        <ProjectTabs />
        <Container>
          <Row>
            <Col>
              <h1>Project Chat</h1>
              <h5>{systemProjectName}</h5>
            </Col>
          </Row>
          <Row>
            <Col md={4}>
              <Card>
                <Card.Header>Members</Card.Header>
                <Card.Body>
                  <ul className="list-group list-group-flush">
                    {projectMembers.map((member) => (
                      <li
                        className="list-group-item"
                        key={member.systemUsername}
                        onClick={() => navigate(`/profile/${member.systemUsername}`)}
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
                              <strong>{member.name}</strong>
                              <div className="text-muted">{member.type}</div>
                            </div>
                          </Col>
                          <Col md="auto" className="d-flex align-items-center">
                            <div
                              className={`status-indicator ${
                                isOnline(member.systemUsername) ? "online" : "offline"
                              }`}
                            />
                            <span className={`status-label ${isOnline(member.systemUsername) ? "online-label" : "offline-label"}`}>
                              {isOnline(member.systemUsername) ? "Online" : "Offline"}
                            </span>
                          </Col>
                        </Row>
                      </li>
                    ))}
                  </ul>
                </Card.Body>
              </Card>
            </Col>
            <Col md={8}>
              <Card>
                <Card.Header>Chat</Card.Header>
                <Card.Body style={{ height: "400px", overflowY: "auto" }} ref={chatBodyRef}>
                  <ul className="list-group list-group-flush">
                    {messages.map((message, index) => (
                      <li className="list-group-item" key={index}>
                        <div
                          className={`message-container ${
                            message.systemUsername ===
                            Cookies.get("user-systemUsername")
                              ? "right"
                              : "left"
                          }`}
                        >
                          <img
                            src={message.photo}
                            alt={message.name}
                            className={`message-avatar ${
                              message.systemUsername ===
                              Cookies.get("user-systemUsername")
                                ? "right-avatar"
                                : "left-avatar"
                            }`}
                          />
                          <div
                            className={`message-content ${
                              message.systemUsername ===
                              Cookies.get("user-systemUsername")
                                ? "right"
                                : "left"
                            }`}
                          >
                            <strong>{message.name}</strong>: {message.message}
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
                      handleSendMessage();
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
