import React, { useEffect, useState } from "react";
import {
  markMailAsSeen,
  searchMails,
  markMailAsDeleted,
  getReceivedMessages,
} from "../../utilities/services";
import Cookies from "js-cookie";
import useMailStore from "../../stores/useMailStore";
import { Modal, Button, Form, InputGroup, Pagination } from "react-bootstrap";
import { FaTrash } from "react-icons/fa";
import ComposeMailModal from "../modals/ComposeMailModal";
import "./MailTable.css";
import DOMPurify from "dompurify";
import TablePagination from "../paginations/TablePagination";
import TrashIcon from "../../resources/icons/other/trash-open-icon.png";

const MailTable = () => {
  const [loading, setLoading] = useState(true);
  const [selectedMail, setSelectedMail] = useState(null);
  const [searchInput, setSearchInput] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [unreadOnly, setUnreadOnly] = useState(false);
  const pageSize = 8;
  const {
    receivedMails,
    fetchMailsInInbox,
    decrementUnreadCount,
    setReceivedMails,
    totalMails,
    setTotalMails,
  } = useMailStore();
  const sessionId = Cookies.get("session-id");
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [mailToDelete, setMailToDelete] = useState(null);
  const [hoveredMailId, setHoveredMailId] = useState(null);
  const [showComposeModal, setShowComposeModal] = useState(false);
  const [preFilledContact, setPreFilledContact] = useState("");
  const [preFilledSubject, setPreFilledSubject] = useState("");
  const [isSearching, setIsSearching] = useState(false);
  const [trigger, setTrigger] = useState(false);

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

  const fetchMails = async () => {
    setLoading(true);
    try {
      const result = await getReceivedMessages(
        sessionId,
        currentPage,
        pageSize,
        unreadOnly
      );
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

  useEffect(() => {
    if (isSearching) {
      handleSearch();
    } else {
      fetchMails();
    }
  }, [currentPage, sessionId, unreadOnly]);

  const handleSearch = async () => {
    setLoading(true);
    setIsSearching(true);
    try {
      const result = await searchMails(
        sessionId,
        searchInput,
        currentPage,
        pageSize,
        unreadOnly
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
    setCurrentPage(1);
    setIsSearching(false);
    fetchMails();
  };

  const handleSingleClick = async (mail) => {
    if (!mail.seen) {
      await markMailAsSeen(sessionId, mail.id);
      fetchMails();
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
    const isSameDay =
      date.getDate() === today.getDate() &&
      date.getMonth() === today.getMonth() &&
      date.getFullYear() === today.getFullYear();
  
    const isSameYear = date.getFullYear() === today.getFullYear();
  
    if (isSameDay) {
      return date.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      });
    }
  
    if (isSameYear) {
      const month = date.toLocaleString("en-US", { month: "short" });
      return `${month} ${date.getDate()}`;
    }
  
    return date.toLocaleDateString("en-GB");
  };

  const totalPages = Math.ceil(totalMails / pageSize);

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <InputGroup className="mail-filters">
        <Form.Check
          type="switch"
          id="unread-only-switch"
          label="Unread only"
          checked={unreadOnly}
          onChange={(e) => {
            setUnreadOnly(e.target.checked);
            setCurrentPage(1);
          }}
          className="custom-switch2"
        />
        <Form.Control
          type="text"
          placeholder="Search mails"
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          style={{ borderRadius: "10px" }}
        />
        <Button
          variant="primary"
          onClick={handleSearch}
          style={{
            backgroundColor: "#f39c12",
            borderColor: "#f39c12",
            borderRadius: "10px",
          }}
        >
          Search
        </Button>
        <Button
          variant="secondary"
          onClick={handleClearSearch}
          style={{ borderRadius: "10px" }}
        >
          Clear Search
        </Button>
      </InputGroup>

      <table id="mail-table">
        <tbody>
          {receivedMails.map((mail) => (
            <tr
              key={mail.id}
              onClick={() => handleSingleClick(mail)}
              className={`mail-row ${!mail.seen ? "unread" : ""}`}
              onMouseEnter={() => setHoveredMailId(mail.id)}
              onMouseLeave={() => setHoveredMailId(null)}
            >
              <td className="mail-cell centered-cell max-width-40">
                <div className="sender-info">
                  <img
                    src={mail.senderPhoto}
                    alt={mail.senderName}
                    className="sender-photo"
                  />
                  <div className="sender-details">
                    <span className="sender-name">{mail.senderName}</span>
                    <span className="sender-email">{mail.senderMail}</span>
                  </div>
                </div>
              </td>
              <td className="mail-cell left-aligned-cell">
                <span className="mail-subject">{mail.subject}</span>
                <span className="mail-preview">
                  {mail.text.slice(0, 30)}...
                </span>
              </td>
              <td className="mail-cell centered-cell max-width-15">
                {hoveredMailId === mail.id ? (
                  <FaTrash
                    onClick={(event) => handleDeleteClick(mail, event)}
                    className="trash-icon"
                  />
                ) : (
                  formatDate(mail.date)
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <TablePagination
        totalPages={totalPages}
        currentPage={currentPage}
        setCurrentPage={setCurrentPage}
        setNavigateTableProjectsTrigger={setTrigger}
        className="pagination-container"
      />

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
