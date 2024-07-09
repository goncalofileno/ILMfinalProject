import React, { useEffect, useState } from "react";
import {
  markMailAsSeen,
  searchMails,
  markMailAsDeleted,
  getReceivedMessages,
} from "../../utilities/services";
import Cookies from "js-cookie";
import { Modal, Button, Form, InputGroup, Pagination } from "react-bootstrap";
import { FaTrash } from "react-icons/fa";
import ComposeMailModal from "../modals/ComposeMailModal";
import "./MailTable.css";
import DOMPurify from "dompurify";
import { useNavigate, useLocation } from "react-router-dom";
import useMailStore from "../../stores/useMailStore";
import { Trans, t } from "@lingui/macro";
import { useMediaQuery } from "react-responsive";

const MailTable = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const {
    receivedMails,
    totalMails,
    fetchMailsInInbox,
    setReceivedMails,
    setTotalMails,
    decrementUnreadCount,
  } = useMailStore();
  const [loading, setLoading] = useState(true);
  const [selectedMail, setSelectedMail] = useState(null);
  const [searchInput, setSearchInput] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [unreadOnly, setUnreadOnly] = useState(false);
  const pageSize = 8;
  const sessionId = Cookies.get("session-id");
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [mailToDelete, setMailToDelete] = useState(null);
  const [hoveredMailId, setHoveredMailId] = useState(null);
  const [showComposeModal, setShowComposeModal] = useState(false);
  const [preFilledContact, setPreFilledContact] = useState("");
  const [preFilledSubject, setPreFilledSubject] = useState("");
  const [isSearching, setIsSearching] = useState(false);
  const isMobile = useMediaQuery({ query: "(max-width: 660px)" });
  const NUMBER_OF_MAILS_PAGE = 8;

  useEffect(() => {
    const query = new URLSearchParams(location.search);
    const page = query.get("page") || 1;
    const unread = query.get("unread") === "true";
    const search = query.get("search") || "";

    setCurrentPage(Number(page));
    setUnreadOnly(unread);
    setSearchInput(search);

    if (search) {
      handleSearch(page, unread, search);
    } else {
      fetchMails(page, unread);
    }
  }, [location.search]);

  const fetchMails = async (page, unread) => {
    setLoading(true);
    try {
      const result = await getReceivedMessages(
        sessionId,
        page,
        pageSize,
        unread
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

  const handleSearch = async (page, unread, search) => {
    setLoading(true);
    setIsSearching(true);
    try {
      const result = await searchMails(
        sessionId,
        search,
        page,
        pageSize,
        unread
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
    navigate("/mail/inbox");
  };

  const handleSingleClick = async (mail) => {
    if (!mail.seen) {
      await markMailAsSeen(sessionId, mail.id);
      fetchMails(currentPage, unreadOnly);
      decrementUnreadCount();
    }
    setSelectedMail(mail);
  };

  const handleDeleteClick = (mail, event) => {
    event.stopPropagation(); // Prevent event propagation
    setMailToDelete(mail);
    setShowDeleteModal(true);
  };

  const handleConfirmDelete = async () => {
    if (mailToDelete) {
      await markMailAsDeleted(sessionId, mailToDelete.id);
      fetchMails(currentPage, unreadOnly);
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

  const handleUnreadOnlyChange = (e) => {
    const unread = e.target.checked;
    setUnreadOnly(unread);
    setCurrentPage(1);
    updateURL({ unread, page: 1, search: searchInput });
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
    updateURL({ page, unread: unreadOnly, search: searchInput });
  };

  const updateURL = ({ page, unread, search }) => {
    const query = new URLSearchParams();

    if (page && page !== 1) query.set("page", page);
    if (unread) query.set("unread", unread);
    if (search) query.set("search", search);

    const queryString = query.toString();
    navigate(`/mail/inbox${queryString ? `?${queryString}` : ""}`);
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
    return (
      <div>
        <Trans>Loading...</Trans>
      </div>
    );
  }

  return (
    <div>
      <InputGroup className="mb-3 mail-filters">
        <Form.Check
          type="switch"
          id="unread-only-switch"
          label={t`Unread only`}
          checked={unreadOnly}
          onChange={handleUnreadOnlyChange}
          className="custom-switch2"
        />
        <Form.Control
          type="text"
          placeholder={t`Search mails`}
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
          style={{
            borderRadius: "10px",
            width: isMobile ? "100%" : "50%",
            cursor: "text",
          }}
          className="custom-focus"
        />
        <div className="flex-btn-row-mail-table">
          <Button
            variant="primary"
            onClick={() =>
              updateURL({ search: searchInput, page: 1, unread: unreadOnly })
            }
            id="primary-btn-boot"
          >
            <Trans>Search</Trans>
          </Button>
          <Button
            variant="secondary"
            onClick={handleClearSearch}
            style={{ borderRadius: "10px" }}
          >
            <Trans>Clear Search</Trans>
          </Button>
        </div>
      </InputGroup>

      {receivedMails.length === 0 ? (
        <div className="no-results">
          <Trans>No emails found matching your criteria.</Trans>
        </div>
      ) : (
        <>
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
                  <td className="mail-cell left-aligned-cell max-width-70">
                    <span className="mail-subject">{mail.subject}</span>
                    <span className="mail-preview">
                      {DOMPurify.sanitize(mail.text.slice(0, 70), {
                        ALLOWED_TAGS: [],
                      })}
                      ...
                    </span>
                  </td>
                  {!isMobile && (
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
                  )}
                </tr>
              ))}
              {Array(NUMBER_OF_MAILS_PAGE - receivedMails.length)
                .fill()
                .map((index) => (
                  <tr key={index + receivedMails.length}>
                    <td className="row-no-content"></td>
                    <td className="row-no-content"></td>
                    {!isMobile && <td className="row-no-content"></td>}
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
            <Modal.Title>
              <Trans>Mail Details</Trans>
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group controlId="formContact">
                <Form.Label>
                  <Trans>From</Trans>
                </Form.Label>
                <Form.Control
                  type="text"
                  readOnly
                  value={`${selectedMail.senderName} <${selectedMail.senderMail}>`}
                  className="custom-focus"
                />
              </Form.Group>
              <Form.Group controlId="formSubject">
                <Form.Label>
                  <Trans>Subject</Trans>
                </Form.Label>
                <Form.Control
                  type="text"
                  readOnly
                  value={selectedMail.subject}
                  className="custom-focus"
                />
              </Form.Group>
              <Form.Group controlId="formMessage">
                <Form.Label>
                  <Trans>Message</Trans>
                </Form.Label>
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
                  dangerouslySetInnerHTML={{
                    __html: DOMPurify.sanitize(selectedMail.text),
                  }}
                />
              </Form.Group>
              <Form.Group controlId="formDate">
                <Form.Label>
                  <Trans>Date</Trans>
                </Form.Label>
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
              <Trans>Close</Trans>
            </Button>
            <Button
              variant="primary"
              onClick={handleReplyClick}
              style={{ backgroundColor: "#f39c12", borderColor: "#f39c12" }}
            >
              <Trans>Reply</Trans>
            </Button>
          </Modal.Footer>
        </Modal>
      )}

      {showDeleteModal && (
        <Modal show={true} onHide={handleCancelDelete}>
          <Modal.Header closeButton>
            <Modal.Title>
              <Trans>Confirm Delete</Trans>
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Trans>Are you sure you want to delete this mail?</Trans>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCancelDelete}>
              <Trans>Cancel</Trans>
            </Button>
            <Button variant="danger" onClick={handleConfirmDelete}>
              <Trans>Delete</Trans>
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
