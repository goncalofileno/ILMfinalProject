package com.ilm.projecto_ilm_backend.dto.mail;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.time.LocalDateTime;

@XmlRootElement
public class MailDto {

    private int id;
    private String subject;
    private String text;
    private LocalDateTime date;
    private String senderName;
    private String senderMail;
    private String receiverName;
    private String receiverMail;
    private boolean seen;
    private boolean deleted;

    public MailDto() {
    }

    public MailDto(int id, String subject, String text, LocalDateTime date, String senderName, String senderMail, String receiverName, String receiverMail, boolean seen, boolean deleted) {
        this.id = id;
        this.subject = subject;
        this.text = text;
        this.date = date;
        this.senderName = senderName;
        this.senderMail = senderMail;
        this.receiverName = receiverName;
        this.receiverMail = receiverMail;
        this.seen = seen;
        this.deleted = deleted;
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

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
