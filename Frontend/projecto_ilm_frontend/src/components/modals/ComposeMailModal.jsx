import React, { useState, useEffect } from "react";
import { Modal, Button, Form, Alert, ListGroup, InputGroup } from "react-bootstrap";
import { sendMail, getContacts } from "../../utilities/services";
import Cookies from "js-cookie";
import "./ComposeMailModal.css";
import { Trans, t } from "@lingui/macro";

const ComposeMailModal = ({ show, handleClose, preFilledContact, preFilledSubject }) => {
  const [contacts, setContacts] = useState([]);
  const [filteredContacts, setFilteredContacts] = useState([]);
  const [selectedContact, setSelectedContact] = useState(preFilledContact || "");
  const [subject, setSubject] = useState(preFilledSubject || "");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [selectedSuggestionIndex, setSelectedSuggestionIndex] = useState(-1);

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

  useEffect(() => {
    setSelectedContact(preFilledContact || "");
  }, [preFilledContact]);

  useEffect(() => {
    setSubject(preFilledSubject || "");
  }, [preFilledSubject]);

  const handleContactChange = (e) => {
    const input = e.target.value;
    setSelectedContact(input);
    setSelectedSuggestionIndex(-1);

    if (input) {
      const filtered = contacts.filter(
        (contact) =>
          contact.name.toLowerCase().includes(input.toLowerCase()) &&
          !selectedContact.includes(contact.email)
      );
      setFilteredContacts(filtered);
    } else {
      setFilteredContacts([]);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "ArrowDown") {
      setSelectedSuggestionIndex((prevIndex) =>
        Math.min(prevIndex + 1, filteredContacts.length - 1)
      );
    } else if (e.key === "ArrowUp") {
      setSelectedSuggestionIndex((prevIndex) => Math.max(prevIndex - 1, 0));
    } else if (e.key === "Enter") {
      e.preventDefault();
      if (
        selectedSuggestionIndex >= 0 &&
        selectedSuggestionIndex < filteredContacts.length
      ) {
        handleSelectContact(filteredContacts[selectedSuggestionIndex]);
      }
    }
  };

  const handleSelectContact = (contact) => {
    setSelectedContact(`${contact.name} <${contact.email}>`);
    setFilteredContacts([]);
  };

  const handleSendMail = async () => {
    if (!selectedContact || !subject || !message) {
      setSuccess("");
      setError(t`Please fill in all fields.`);
      return;
    }

    const contact = contacts.find(
      (contact) =>
        selectedContact.includes(contact.email) &&
        selectedContact.includes(contact.name)
    );

    if (!contact) {
      setSuccess("");
      setError(t`Contact does not exist.`);
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
        setError("");
        setSuccess(t`Mail sent successfully.`);
        setTimeout(() => {
          handleClose();
          resetFields();
        }, 2000);
      } else {
        setSuccess("");
        setError(t`Failed to send mail.`);
      }
    } catch (error) {
      console.error("Failed to send mail:", error);
      setSuccess("");
      setError(t`Failed to send mail.`);
    }
  };

  const resetFields = () => {
    setSelectedContact(preFilledContact || "");
    setSubject(preFilledSubject || "");
    setMessage("");
    setError("");
    setSuccess("");
    setFilteredContacts([]);
  };

  const handleModalClose = () => {
    handleClose();
    resetFields();
  };

  return (
    <Modal show={show} onHide={handleModalClose} size="lg">
      <Modal.Header closeButton>
        <Modal.Title><Trans>Compose New Mail</Trans></Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {error && <Alert variant="danger">{error}</Alert>}
        {success && <Alert variant="success">{success}</Alert>}
        <Form>
          <Form.Group controlId="formContact">
            <Form.Label><Trans>Contact</Trans></Form.Label>
            <InputGroup className="mb-3">
              <Form.Control
                type="text"
                placeholder={t`Enter contact name or email`}
                value={selectedContact}
                onChange={handleContactChange}
                onKeyDown={handleKeyDown}
                autoComplete="off"
                className="custom-focus"
              />
            </InputGroup>
            {filteredContacts.length > 0 && (
              <ListGroup className={`suggestions-list ${filteredContacts.length > 0 ? 'show' : ''}`}>
                {filteredContacts.map((contact, index) => (
                  <ListGroup.Item
                    key={contact.email}
                    action
                    onClick={() => handleSelectContact(contact)}
                    active={index === selectedSuggestionIndex}
                  >
                    {`${contact.name} <${contact.email}>`}
                  </ListGroup.Item>
                ))}
              </ListGroup>
            )}
          </Form.Group>
          <Form.Group controlId="formSubject">
            <Form.Label><Trans>Subject</Trans></Form.Label>
            <Form.Control
              type="text"
              placeholder={t`Enter subject`}
              value={subject}
              onChange={(e) => setSubject(e.target.value)}
              className="custom-focus"
            />
          </Form.Group>
          <Form.Group controlId="formMessage">
            <Form.Label><Trans>Message</Trans></Form.Label>
            <Form.Control
              as="textarea"
              rows={8}
              placeholder={t`Enter your message`}
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              className="custom-focus"
            />
          </Form.Group>
        </Form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleModalClose}>
        <Trans>Cancel</Trans>
        </Button>
        <Button
          variant="primary"
          onClick={handleSendMail}
          style={{ backgroundColor: "#f39c12", borderColor: "#f39c12" }}
        >
          <Trans>Send</Trans>
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ComposeMailModal;
