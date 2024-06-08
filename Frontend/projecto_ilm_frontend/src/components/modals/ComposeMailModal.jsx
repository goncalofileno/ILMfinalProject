import React, { useState, useEffect } from "react";
import { Modal, Button, Form, Alert } from "react-bootstrap";
import { sendMail, getContacts } from "../../utilities/services";
import Cookies from "js-cookie";
import "./ComposeMailModal.css"; // Certifique-se de criar este arquivo CSS

const ComposeMailModal = ({ show, handleClose }) => {
  const [contacts, setContacts] = useState([]);
  const [filteredContacts, setFilteredContacts] = useState([]);
  const [selectedContact, setSelectedContact] = useState("");
  const [subject, setSubject] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [activeSuggestion, setActiveSuggestion] = useState(0);
  const [showSuggestions, setShowSuggestions] = useState(false);

  useEffect(() => {
    const fetchContacts = async () => {
      try {
        const response = await getContacts();
        if (response.ok) {
          const contacts = await response.json();
          setContacts(contacts);
        }
      } catch (error) {
        console.error("Failed to fetch contacts:", error);
      }
    };

    fetchContacts();
  }, []);

  const handleContactChange = (e) => {
    const input = e.target.value;
    setSelectedContact(input);

    if (input === "") {
      setFilteredContacts([]);
      setShowSuggestions(false);
      return;
    }

    const filtered = contacts.filter((contact) =>
      contact.name.toLowerCase().includes(input.toLowerCase())
    );
    setFilteredContacts(filtered);
    setShowSuggestions(true);
  };

  const handleSelectContact = (contact) => {
    setSelectedContact(`${contact.name} <${contact.email}>`);
    setFilteredContacts([]);
    setShowSuggestions(false);
  };

  const handleKeyDown = (e) => {
    if (e.key === "ArrowDown") {
      setActiveSuggestion((prev) => (prev + 1) % filteredContacts.length);
    } else if (e.key === "ArrowUp") {
      setActiveSuggestion((prev) =>
        prev === 0 ? filteredContacts.length - 1 : prev - 1
      );
    } else if (e.key === "Enter") {
      e.preventDefault();
      if (filteredContacts.length > 0) {
        handleSelectContact(filteredContacts[activeSuggestion]);
      }
    }
  };

  const handleSendMail = async () => {
    if (!selectedContact || !subject || !message) {
      setError("Please fill in all fields.");
      return;
    }

    const contact = contacts.find(
      (contact) =>
        selectedContact.includes(contact.email) &&
        selectedContact.includes(contact.name)
    );

    if (!contact) {
      setError("Contact does not exist.");
      return;
    }

    try {
      const sessionId = Cookies.get("session-id");
      const response = await sendMail(sessionId, {
        receiverMail: contact.email,
        subject,
        text: message,
      });

      if (response.ok) {
        setSuccess("Mail sent successfully.");
        setTimeout(() => {
          handleClose();
          resetFields();
        }, 2000); // Close the modal after 2 seconds
      } else {
        setError("Failed to send mail.");
      }
    } catch (error) {
      console.error("Failed to send mail:", error);
      setError("Failed to send mail.");
    }
  };

  const resetFields = () => {
    setSelectedContact("");
    setSubject("");
    setMessage("");
    setError("");
    setSuccess("");
    setFilteredContacts([]);
    setShowSuggestions(false);
    setActiveSuggestion(0);
  };

  const handleModalClose = () => {
    handleClose();
    resetFields();
  };

  return (
    <Modal show={show} onHide={handleModalClose} size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Compose New Mail</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {error && <Alert variant="danger">{error}</Alert>}
        {success && <Alert variant="success">{success}</Alert>}
        <Form>
          <Form.Group controlId="formContact">
            <Form.Label>Contact</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter contact name or email"
              value={selectedContact}
              onChange={handleContactChange}
              onKeyDown={handleKeyDown}
              autoComplete="off"
            />
            {showSuggestions && filteredContacts.length > 0 && (
              <div className="contact-suggestions">
                {filteredContacts.map((contact, index) => (
                  <div
                    key={contact.email}
                    className={`contact-suggestion ${
                      index === activeSuggestion ? "active" : ""
                    }`}
                    onClick={() => handleSelectContact(contact)}
                  >
                    {contact.name}
                  </div>
                ))}
              </div>
            )}
          </Form.Group>
          <Form.Group controlId="formSubject">
            <Form.Label>Subject</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter subject"
              value={subject}
              onChange={(e) => setSubject(e.target.value)}
            />
          </Form.Group>
          <Form.Group controlId="formMessage">
            <Form.Label>Message</Form.Label>
            <Form.Control
              as="textarea"
              rows={8}
              placeholder="Enter your message"
              value={message}
              onChange={(e) => setMessage(e.target.value)}
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleModalClose}>
          Cancel
        </Button>
        <Button
          variant="primary"
          onClick={handleSendMail}
          style={{ backgroundColor: "#f39c12", borderColor: "#f39c12" }}
        >
          Send
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ComposeMailModal;
