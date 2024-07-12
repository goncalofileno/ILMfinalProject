package com.ilm.projecto_ilm_backend.dto.mail;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for mail messages.
 * This class encapsulates the details of a mail message, including sender and receiver information,
 * message content, and status flags for seen and deletion states.
 */
@XmlRootElement
public class MailDto {

    private int id;
    private String subject;
    private String text;
    private LocalDateTime date;
    private String senderName;
    private String senderMail;
    private String senderPhoto;
    private String receiverName;
    private String receiverMail;
    private String receiverPhoto;
    private boolean seen;
    private boolean isDeletedBySender;
    private boolean isDeletedByReceiver;

    /**
     * Default constructor.
     */
    public MailDto() {
    }

    /**
     * Constructs a new MailDto with specified details.
     *
     * @param id The unique identifier of the mail.
     * @param subject The subject of the mail.
     * @param text The text content of the mail.
     * @param date The date and time the mail was sent.
     * @param senderName The name of the sender.
     * @param senderMail The email address of the sender.
     * @param senderPhoto The photo URL of the sender.
     * @param receiverName The name of the receiver.
     * @param receiverMail The email address of the receiver.
     * @param receiverPhoto The photo URL of the receiver.
     * @param seen Flag indicating if the mail has been seen by the receiver.
     * @param isDeletedBySender Flag indicating if the mail has been deleted by the sender.
     * @param isDeletedByReceiver Flag indicating if the mail has been deleted by the receiver.
     */
    public MailDto(int id, String subject, String text, LocalDateTime date, String senderName, String senderMail, String senderPhoto, String receiverName, String receiverMail, String receiverPhoto, boolean seen, boolean isDeletedBySender, boolean isDeletedByReceiver) {
        this.id = id;
        this.subject = subject;
        this.text = text;
        this.date = date;
        this.senderName = senderName;
        this.senderMail = senderMail;
        this.senderPhoto = senderPhoto;
        this.receiverName = receiverName;
        this.receiverMail = receiverMail;
        this.receiverPhoto = receiverPhoto;
        this.seen = seen;
        this.isDeletedBySender = isDeletedBySender;
        this.isDeletedByReceiver = isDeletedByReceiver;
    }

    /**
     * Constructs a new MailDto with essential details, excluding sender and receiver photos.
     *
     * @param subject The subject of the mail.
     * @param text The text content of the mail.
     * @param senderName The name of the sender.
     * @param senderMail The email address of the sender.
     * @param receiverName The name of the receiver.
     * @param receiverMail The email address of the receiver.
     */
    public MailDto(String subject, String text, String senderName, String senderMail, String receiverName, String receiverMail) {
        this.subject = subject;
        this.text = text;
        this.senderName = senderName;
        this.senderMail = senderMail;
        this.receiverName = receiverName;
        this.receiverMail = receiverMail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderMail() {
        return senderMail;
    }

    public void setSenderMail(String senderMail) {
        this.senderMail = senderMail;
    }

    public String getSenderPhoto() {
        return senderPhoto;
    }

    public void setSenderPhoto(String senderPhoto) {
        this.senderPhoto = senderPhoto;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMail() {
        return receiverMail;
    }

    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }

    public String getReceiverPhoto() {
        return receiverPhoto;
    }

    public void setReceiverPhoto(String receiverPhoto) {
        this.receiverPhoto = receiverPhoto;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isDeletedBySender() {
        return isDeletedBySender;
    }

    public void setDeletedBySender(boolean deletedBySender) {
        isDeletedBySender = deletedBySender;
    }

    public boolean isDeletedByReceiver() {
        return isDeletedByReceiver;
    }

    public void setDeletedByReceiver(boolean deletedByReceiver) {
        isDeletedByReceiver = deletedByReceiver;
    }
}
