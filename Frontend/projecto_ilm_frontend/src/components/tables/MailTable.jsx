import React, { useEffect, useState } from "react";
import { getReceivedMessages, markMailAsSeen } from "../../utilities/services"; // Importar markMailAsSeen
import Cookies from "js-cookie";
import useMailStore from "../../stores/useMailStore"; // Importar a store zustand
import { Modal, Button, Form } from "react-bootstrap";
import "./MailTable.css";

const MailTable = () => {
  const [receivedMails, setReceivedMails] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedMail, setSelectedMail] = useState(null);
  const { unreadCount, decrementUnreadCount } = useMailStore();
  const sessionId = Cookies.get("session-id");

  useEffect(() => {
    const fetchMails = async () => {
      try {
        const receivedResponse = await getReceivedMessages(sessionId);

        if (receivedResponse.ok) {
          const receivedData = await receivedResponse.json();
          // Ordenar os e-mails pela data mais recente primeiro
          receivedData.sort((a, b) => new Date(b.date) - new Date(a.date));
          setReceivedMails(receivedData);
        }
      } catch (error) {
        console.error("Error fetching mails:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchMails();
  }, [sessionId]);

  const handleDoubleClick = async (mail) => {
    if (!mail.seen) {
      await markMailAsSeen(sessionId, mail.id);
      setReceivedMails((prevMails) =>
        prevMails.map((m) => (m.id === mail.id ? { ...m, seen: true } : m))
      );
      decrementUnreadCount();
    }
    setSelectedMail(mail);
  };

  const handleCloseModal = () => {
    setSelectedMail(null);
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    const today = new Date();
    if (
      date.getDate() === today.getDate() &&
      date.getMonth() === today.getMonth() &&
      date.getFullYear() === today.getFullYear()
    ) {
      return date.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      });
    }
    return `${date.getDate()}/${date.getMonth() + 1}`;
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <table className="mail-table">
        <thead>
          <tr>
            <th>Sender</th>
            <th>Subject</th>
            <th>Time</th>
          </tr>
        </thead>
        <tbody>
          {receivedMails.map((mail) => (
            <tr
              key={mail.id}
              onDoubleClick={() => handleDoubleClick(mail)}
              className={!mail.seen ? "unread" : ""}
            >
              <td>
                <div className="sender-info">
                  <img
                    src={mail.senderPhoto}
                    alt={mail.senderName}
                    className="sender-photo"
                  />
                  <span>{mail.senderName}</span>
                </div>
              </td>
              <td>
                <strong>{mail.subject}</strong> - {mail.text.slice(0, 30)}...
              </td>
              <td>{formatDate(mail.date)}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {selectedMail && (
        <Modal show={true} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title>Mail Details</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group controlId="formContact">
                <Form.Label>From</Form.Label>
                <Form.Control
                  type="text"
                  readOnly
                  value={`${selectedMail.senderName} <${selectedMail.senderMail}>`}
                />
              </Form.Group>
              <Form.Group controlId="formSubject">
                <Form.Label>Subject</Form.Label>
                <Form.Control
                  type="text"
                  readOnly
                  value={selectedMail.subject}
                />
              </Form.Group>
              <Form.Group controlId="formMessage">
                <Form.Label>Message</Form.Label>
                <Form.Control
                  as="textarea"
                  rows={8}
                  readOnly
                  value={selectedMail.text}
                />
              </Form.Group>
              <Form.Group controlId="formDate">
                <Form.Label>Date</Form.Label>
                <Form.Control
                  type="text"
                  readOnly
                  value={new Date(selectedMail.date).toLocaleString()}
                />
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
    </div>
  );
};

export default MailTable;
