import React, { useEffect, useState } from "react";
import {
  getSentMessages,
  searchSentMails,
  markMailAsDeleted,
} from "../../utilities/services";
import {
  Table,
  Button,
  Modal,
  Form,
  InputGroup,
  Pagination,
} from "react-bootstrap";
import Cookies from "js-cookie";
import { FaTrash } from "react-icons/fa";
import { useNavigate, useLocation } from "react-router-dom";
import "./SentMailTable.css";

const SentMailTable = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [mails, setMails] = useState([]);
  const [selectedMail, setSelectedMail] = useState(null);
  const [searchInput, setSearchInput] = useState("");
  const [totalMails, setTotalMails] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [loading, setLoading] = useState(true);
  const [isSearching, setIsSearching] = useState(false);
  const pageSize = 8;
  const sessionId = Cookies.get("session-id");
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [mailToDelete, setMailToDelete] = useState(null);
  const [hoveredMailId, setHoveredMailId] = useState(null);

  useEffect(() => {
    const query = new URLSearchParams(location.search);
    const page = query.get("page") || 1;
    const search = query.get("search") || "";

    setCurrentPage(Number(page));
    setSearchInput(search);

    if (search) {
      handleSearch(page, search);
    } else {
      fetchMails(page);
    }
  }, [location.search]);

  const fetchMails = async (page) => {
    setLoading(true);
    try {
      const result = await getSentMessages(sessionId, page, pageSize);
      const { mails, totalMails } = result;
      mails.sort((a, b) => new Date(b.date) - new Date(a.date));
      setMails(mails);
      setTotalMails(totalMails);
    } catch (error) {
      console.error("Error fetching mails:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async (page, search) => {
    setLoading(true);
    setIsSearching(true);
    try {
      const result = await searchSentMails(sessionId, search, page, pageSize);
      const { mails, totalMails } = result;
      mails.sort((a, b) => new Date(b.date) - new Date(a.date));
      setMails(mails);
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
    navigate("/mail/sent");
  };

  const handleSingleClick = (mail) => {
    setSelectedMail(mail);
  };

  const handleDeleteClick = (mail, event) => {
    event.stopPropagation(); // Impede a propagação do evento
    setMailToDelete(mail);
    setShowDeleteModal(true);
  };

  const handleConfirmDelete = async () => {
    if (mailToDelete) {
      await markMailAsDeleted(sessionId, mailToDelete.id);
      setMails((prevMails) =>
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

  const handleCloseModal = () => {
    setSelectedMail(null);
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
    updateURL({ page, search: searchInput });
  };

  const updateURL = ({ page, search }) => {
    const query = new URLSearchParams();

    if (page && page !== 1) query.set("page", page);
    if (search) query.set("search", search);

    const queryString = query.toString();
    navigate(`/mail/sent${queryString ? `?${queryString}` : ""}`);
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

  const renderPaginationItems = () => {
    const items = [];
    const maxPagesToShow = 5;

    if (totalPages <= maxPagesToShow) {
      for (let number = 1; number <= totalPages; number++) {
        items.push(
          <Pagination.Item
            key={number}
            active={number === currentPage}
            onClick={() => handlePageChange(number)}
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
            onClick={() => handlePageChange(number)}
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
          onClick={() => handlePageChange(totalPages)}
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
      <InputGroup className="mb-3 mail-filters">
        <Form.Control
          type="text"
          placeholder="Search mails"
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          style={{ borderRadius: "10px" }}
          className="custom-focus"
        />
        <Button
          variant="primary"
          onClick={() => updateURL({ search: searchInput, page: 1 })}
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

      {mails.length === 0 ? (
        <div className="no-results">
          No emails found matching your criteria.
        </div>
      ) : (
        <>
          <table id="sent-mail-table">
            <tbody>
              {mails.map((mail) => (
                <tr
                  key={mail.id}
                  onClick={() => handleSingleClick(mail)}
                  onMouseEnter={() => setHoveredMailId(mail.id)}
                  onMouseLeave={() => setHoveredMailId(null)}
                >
                  <td className="mail-cell centered-cell max-width-40">
                    <div className="sender-info">
                      <img
                        src={mail.receiverPhoto}
                        alt={mail.receiverName}
                        className="sender-photo"
                      />
                      <div className="sender-details">
                        <span className="receiver-name">
                          {mail.receiverName}
                        </span>
                        <span className="receiver-email">
                          {mail.receiverMail}
                        </span>
                      </div>
                    </div>
                  </td>
                  <td className="mail-cell left-aligned-cell max-width-70">
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
          <div className="pagination-container">
            <Pagination className="pagination">
              <Pagination.First
                onClick={() => handlePageChange(1)}
                disabled={currentPage === 1}
              />
              <Pagination.Prev
                onClick={() => handlePageChange(Math.max(currentPage - 1, 1))}
                disabled={currentPage === 1}
              />
              {renderPaginationItems()}
              <Pagination.Next
                onClick={() =>
                  handlePageChange(Math.min(currentPage + 1, totalPages))
                }
                disabled={currentPage === totalPages}
              />
              <Pagination.Last
                onClick={() => handlePageChange(totalPages)}
                disabled={currentPage === totalPages}
              />
            </Pagination>
          </div>
        </>
      )}

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
                    whiteSpace: "pre-wrap",
                  }}
                >
                  {selectedMail.text}
                </div>
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

export default SentMailTable;
