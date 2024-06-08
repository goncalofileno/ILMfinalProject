import React, { useEffect, useState } from "react";
import { getSentMessages } from "../../utilities/services";
import { Table, Button, Modal, Form } from "react-bootstrap";
import Cookies from "js-cookie";

const SentMailTable = () => {
  const [mails, setMails] = useState([]);
  const [selectedMail, setSelectedMail] = useState(null);
  const sessionId = Cookies.get("session-id");

  useEffect(() => {
    const fetchMails = async () => {
      try {
        const response = await getSentMessages(sessionId);
        if (response.ok) {
          const data = await response.json();
          setMails(data);
        }
      } catch (error) {
        console.error("Error fetching sent mails:", error);
      }
    };

    fetchMails();
  }, [sessionId]);

  const handleDoubleClick = (mail) => {
    setSelectedMail(mail);
  };

  const handleCloseModal = () => {
    setSelectedMail(null);
  };

  return (
    <>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>#</th>
            <th>Subject</th>
            <th>Recipient</th>
            <th>Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {mails.map((mail, index) => (
            <tr key={mail.id} onDoubleClick={() => handleDoubleClick(mail)}>
              <td>{index + 1}</td>
              <td>{mail.subject}</td>
              <td>{mail.receiverName}</td>
              <td>{mail.date}</td>
              <td>
                <Button variant="danger">Delete</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {selectedMail && (
        <Modal show={true} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title>Mail Details</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group controlId="formContact">
                <Form.Label>To</Form.Label>
                <Form.Control
                  type="text"
                  readOnly
                  value={`${selectedMail.receiverName} <${selectedMail.receiverMail}>`}
                />
              </Form.Group>
              <Form.Group controlId="formSubject">
                <Form.Label>Subject</Form.Label>
                <Form.Control type="text" readOnly value={selectedMail.subject} />
              </Form.Group>
              <Form.Group controlId="formMessage">
                <Form.Label>Message</Form.Label>
                <Form.Control as="textarea" rows={8} readOnly value={selectedMail.text} />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
          </Modal.Footer>
        </Modal>
      )}
    </>
  );
};

export default SentMailTable;
