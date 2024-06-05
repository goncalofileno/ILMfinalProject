package com.ilm.projecto_ilm_backend.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The SessionEntity class represents the "session" table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "session")
@NamedQuery(name = "Session.findBySessionId", query = "SELECT s FROM SessionEntity s WHERE s.sessionId = :sessionId")
@NamedQuery(name = "Session.findByUserId", query = "SELECT s FROM SessionEntity s WHERE s.user.id = :user_id")
@NamedQuery(name = "Session.findByUserAndUserAgent", query = "SELECT s FROM SessionEntity s WHERE s.user.id = :userId AND s.userAgent = :userAgent")
@NamedQuery(name = "Session.findByUser", query = "SELECT s FROM SessionEntity s WHERE s.user = :user")
public class SessionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique ID of the session. This is the primary key in the "session" table.
     */
    @Id
    @Column(name = "session_id", nullable = false, unique = true, updatable = false)
    private String sessionId;

    /**
     * The user associated with this session. This is a foreign key referencing the "user" table.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /**
     * The creation timestamp of the session.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * The expiration timestamp of the session.
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * The IP address from which the session was created.
     */
    @Column(name = "ip_address", nullable = true)
    private String ipAddress;

    /**
     * The user agent string from the client creating the session.
     */
    @Column(name = "user_agent", nullable = true)
    private String userAgent;

    /**
     * Default constructor.
     */
    public SessionEntity() {
    }

    /**
     * Returns the session ID of this session.
     *
     * @return the session ID of this session.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Sets the session ID of this session.
     *
     * @param sessionId the new session ID of this session.
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Returns the user associated with this session.
     *
     * @return the user associated with this session.
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Sets the user associated with this session.
     *
     * @param user the new user associated with this session.
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Returns the creation timestamp of this session.
     *
     * @return the creation timestamp of this session.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of this session.
     *
     * @param createdAt the new creation timestamp of this session.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the expiration timestamp of this session.
     *
     * @return the expiration timestamp of this session.
     */
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    /**
     * Sets the expiration timestamp of this session.
     *
     * @param expiresAt the new expiration timestamp of this session.
     */
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Returns the IP address from which this session was created.
     *
     * @return the IP address from which this session was created.
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the IP address from which this session was created.
     *
     * @param ipAddress the new IP address from which this session was created.
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Returns the user agent string from the client creating this session.
     *
     * @return the user agent string from the client creating this session.
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Sets the user agent string from the client creating this session.
     *
     * @param userAgent the new user agent string from the client creating this session.
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
