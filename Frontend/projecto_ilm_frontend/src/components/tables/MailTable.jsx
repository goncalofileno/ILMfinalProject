import React, { useEffect, useState } from "react";
import {
  markMailAsSeen,
  searchMails,
  markMailAsDeleted,
} from "../../utilities/services";
import Cookies from "js-cookie";
import useMailStore from "../../stores/useMailStore";
import { Modal, Button, Form, InputGroup, Pagination } from "react-bootstrap";
import { FaTrash } from "react-icons/fa";
import ComposeMailModal from "../modals/ComposeMailModal";
import "./MailTable.css";
import DOMPurify from "dompurify";

const MailTable = () => {
  const [loading, setLoading] = useState(true);
  const [selectedMail, setSelectedMail] = useState(null);
  const [searchInput, setSearchInput] = useState("");
  const [totalMails, setTotalMails] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 8;
  const {
    receivedMails,
    fetchMailsInInbox,
    decrementUnreadCount,
    setReceivedMails,
  } = useMailStore();
  const sessionId = Cookies.get("session-id");
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [mailToDelete, setMailToDelete] = useState(null);
  const [hoveredMailId, setHoveredMailId] = useState(null);
  const [showComposeModal, setShowComposeModal] = useState(false);
  const [preFilledContact, setPreFilledContact] = useState("");
  const [preFilledSubject, setPreFilledSubject] = useState("");

  const handleDeleteClick = (mail, event) => {
    event.stopPropagation(); // Prevent event propagation
    setMailToDelete(mail);
    setShowDeleteModal(true);
  };

  const handleConfirmDelete = async () => {
    if (mailToDelete) {
      await markMailAsDeleted(sessionId, mailToDelete.id);
      fetchMailsInInbox();
      setShowDeleteModal(false);
      setMailToDelete(null);
      if (!mailToDelete.seen) {
        decrementUnreadCount();
      }
    }
  };

  const handleCancelDelete = () => {
    setShowDeleteModal(false);
    setMailToDelete(null);
  };

  useEffect(() => {
    setLoading(true);
    fetchMailsInInbox().finally(() => setLoading(false));
  }, [fetchMailsInInbox, currentPage, sessionId]);

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
    fetchMailsInInbox();
    setCurrentPage(1);
  };

  const handleSingleClick = async (mail) => {
    if (!mail.seen) {
      await markMailAsSeen(sessionId, mail.id);
      fetchMailsInInbox();
      decrementUnreadCount();
    }
    setSelectedMail(mail);
  };

  const handleCloseModal = () => {
    setSelectedMail(null);
  };

  const handleReplyClick = () => {
    setPreFilledContact(
      `${selectedMail.senderName} <${selectedMail.senderMail}>`
    );
    setPreFilledSubject(`Re: ${selectedMail.subject}`);
    setShowComposeModal(true);
    setSelectedMail(null);
  };

  const handleCloseComposeModal = () => {
    setShowComposeModal(false);
    setPreFilledContact("");
    setPreFilledSubject("");
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

  const renderPaginationItems = () => {
    const items = [];
    const maxPagesToShow = 5;

    if (totalPages <= maxPagesToShow) {
      for (let number = 1; number <= totalPages; number++) {
        items.push(
          <Pagination.Item
            key={number}
            active={number === currentPage}
            onClick={() => setCurrentPage(number)}
          >
            {number}
          </Pagination.Item>
        );
      }
    } else {
      let startPage, endPage;
      if (currentPage <= 3) {
        startPage = 1;
        endPage = maxPagesToShow - 1;
      } else if (currentPage + 1 >= totalPages) {
        startPage = totalPages - (maxPagesToShow - 2);
        endPage = totalPages - 1;
      } else {
        startPage = currentPage - 2;
        endPage = currentPage + 1;
      }

      for (let number = startPage; number <= endPage; number++) {
        items.push(
          <Pagination.Item
            key={number}
            active={number === currentPage}
            onClick={() => setCurrentPage(number)}
          >
            {number}
          </Pagination.Item>
        );
      }

      items.push(<Pagination.Ellipsis key="ellipsis" disabled />);

      items.push(
        <Pagination.Item
          key={totalPages}
          active={totalPages === currentPage}
          onClick={() => setCurrentPage(totalPages)}
        >
          {totalPages}
        </Pagination.Item>
      );
    }

    return items;
  };

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
            <th className="centered-cell max-width-150">Sender</th>
            <th className="left-aligned-cell">Subject</th>
            <th className="centered-cell max-width-100">Time</th>
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
              <td className="centered-cell max-width-150">
                <div className="sender-info">
                  <img
                    src={mail.senderPhoto}
                    alt={mail.senderName}
                    className="sender-photo"
                  />
                  <span>{mail.senderName}</span>
                </div>
              </td>
              <td className="left-aligned-cell">
                <strong>{mail.subject}</strong> - {mail.text.slice(0, 30)}...
              </td>
              <td className="centered-cell max-width-100">
                {hoveredMailId === mail.id ? (
                  <FaTrash
                    onClick={(event) => handleDeleteClick(mail, event)}
                  />
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
          {renderPaginationItems()}
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
                  className="custom-focus"
                />
              </Form.Group>
              <Form.Group controlId="formSubject">
                <Form.Label>Subject</Form.Label>
                <Form.Control
                  type="text"
                  readOnly
                  value={selectedMail.subject}
                  className="custom-focus"
                />
              </Form.Group>
              <Form.Group controlId="formMessage">
                <Form.Label>Message</Form.Label>
                <div
                  className="custom-focus"
                  style={{
                    padding: "10px",
                    border: "1px solid #ced4da",
                    borderRadius: ".25rem",
                    backgroundColor: "#e9ecef",
                    overflowY: "auto",
                    maxHeight: "400px",
                    whiteSpace: "pre-wrap", // Preserve whitespace and new lines
                  }}
                  dangerouslySetInnerHTML={{
                    __html: DOMPurify.sanitize(selectedMail.text),
                  }}
                />
              </Form.Group>
              <Form.Group controlId="formDate">
                <Form.Label>Date</Form.Label>
                <Form.Control
                  type="text"
                  readOnly
                  value={new Date(selectedMail.date).toLocaleString()}
                  className="custom-focus"
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
            <Button
              variant="primary"
              onClick={handleReplyClick}
              style={{ backgroundColor: "#f39c12", borderColor: "#f39c12" }}
            >
              Reply
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

      {showComposeModal && (
        <ComposeMailModal
          show={true}
          handleClose={handleCloseComposeModal}
          preFilledContact={preFilledContact}
          preFilledSubject={preFilledSubject}
        />
      )}
    </div>
  );
};

export default MailTable;
