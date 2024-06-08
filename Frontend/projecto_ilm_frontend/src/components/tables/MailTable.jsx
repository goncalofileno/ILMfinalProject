import React, { useEffect, useState } from "react";
import {
  getReceivedMessages,
  markMailAsSeen,
  searchMails,
  markMailAsDeleted,
} from "../../utilities/services";
import Cookies from "js-cookie";
import useMailStore from "../../stores/useMailStore"; // Importar a store zustand
import { Modal, Button, Form, InputGroup, Pagination } from "react-bootstrap";
import "./MailTable.css";
import { FaTrash } from "react-icons/fa";

const MailTable = () => {
  const [receivedMails, setReceivedMails] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedMail, setSelectedMail] = useState(null);
  const [searchInput, setSearchInput] = useState("");
  const [totalMails, setTotalMails] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 10;
  const { unreadCount, decrementUnreadCount } = useMailStore();
  const sessionId = Cookies.get("session-id");
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [mailToDelete, setMailToDelete] = useState(null);
  const [hoveredMailId, setHoveredMailId] = useState(null);

  const handleDeleteClick = (e, mail) => {
    e.stopPropagation(); // Adicionar para evitar a propagação do evento de clique
    setMailToDelete(mail);
    setShowDeleteModal(true);
  };

  const handleConfirmDelete = async () => {
    if (mailToDelete) {
      await markMailAsDeleted(sessionId, mailToDelete.id);
      setReceivedMails((prevMails) =>
        prevMails.filter((m) => m.id !== mailToDelete.id)
      );
      setShowDeleteModal(false);
      setMailToDelete(null);
    }
  };

  const handleCancelDelete = () => {
    setShowDeleteModal(false);
    setMailToDelete(null);
  };

  useEffect(() => {
    fetchMails(currentPage);
  }, [sessionId, currentPage]);

  const fetchMails = async (page) => {
    setLoading(true);
    try {
      const result = await getReceivedMessages(sessionId, page, pageSize);
      const { mails, totalMails } = result;
      mails.sort((a, b) => new Date(b.date) - new Date(a.date));
      setReceivedMails(mails);
      setTotalMails(totalMails);
    } catch (error) {
      console.error("Error fetching mails:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    setLoading(true);
    try {
      const result = await searchMails(
        sessionId,
        searchInput,
        currentPage,
        pageSize
      );
      const { mails, totalMails } = result;
      mails.sort((a, b) => new Date(b.date) - new Date(a.date));
      setReceivedMails(mails);
      setTotalMails(totalMails);
    } catch (error) {
      console.error("Error searching mails:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleClearSearch = () => {
    setSearchInput("");
    fetchMails(1);
    setCurrentPage(1);
  };

  const handleSingleClick = async (mail) => {
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

  const totalPages = Math.ceil(totalMails / pageSize);

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <InputGroup className="mb-3">
        <Form.Control
          type="text"
          placeholder="Search mails"
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
        />
        <Button
          variant="primary"
          onClick={handleSearch}
          style={{
            backgroundColor: "#f39c12",
            borderColor: "#f39c12",
          }}
        >
          Search
        </Button>
        <Button
          variant="secondary"
          onClick={handleClearSearch}
          style={{ marginLeft: "10px" }}
        >
          Clear Search
        </Button>
      </InputGroup>

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
              onClick={() => handleSingleClick(mail)}
              className={!mail.seen ? "unread" : ""}
              onMouseEnter={() => setHoveredMailId(mail.id)}
              onMouseLeave={() => setHoveredMailId(null)}
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
              <td>
                {hoveredMailId === mail.id ? (
                  <FaTrash onClick={(e) => handleDeleteClick(e, mail)} />
                ) : (
                  formatDate(mail.date)
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="pagination-container">
        <Pagination className="pagination">
          <Pagination.First
            onClick={() => setCurrentPage(1)}
            disabled={currentPage === 1}
          />
          <Pagination.Prev
            onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
            disabled={currentPage === 1}
          />
          {Array.from({ length: totalPages }, (_, index) => (
            <Pagination.Item
              key={index + 1}
              active={index + 1 === currentPage}
              onClick={() => setCurrentPage(index + 1)}
            >
              {index + 1}
            </Pagination.Item>
          ))}
          <Pagination.Next
            onClick={() =>
              setCurrentPage((prev) => Math.min(prev + 1, totalPages))
            }
            disabled={currentPage === totalPages}
          />
          <Pagination.Last
            onClick={() => setCurrentPage(totalPages)}
            disabled={currentPage === totalPages}
          />
        </Pagination>
      </div>

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

      {showDeleteModal && (
        <Modal show={true} onHide={handleCancelDelete}>
          <Modal.Header closeButton>
            <Modal.Title>Confirm Delete</Modal.Title>
          </Modal.Header>
          <Modal.Body>Are you sure you want to delete this mail?</Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCancelDelete}>
              Cancel
            </Button>
            <Button variant="danger" onClick={handleConfirmDelete}>
              Delete
            </Button>
          </Modal.Footer>
        </Modal>
      )}
    </div>
  );
};

export default MailTable;
