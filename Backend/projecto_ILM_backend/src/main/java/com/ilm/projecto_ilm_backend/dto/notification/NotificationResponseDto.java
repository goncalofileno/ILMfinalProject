package com.ilm.projecto_ilm_backend.dto.notification;

import java.util.List;

/**
 * Data Transfer Object for notification responses.
 * This class encapsulates a list of notifications and the total number of notifications,
 * providing a structured response format for notification queries.
 */
public class NotificationResponseDto {
    /**
     * A list of NotificationDto objects representing individual notifications.
     */
    private List<NotificationDto> notifications;

    /**
     * The total number of notifications, useful for pagination or displaying counts.
     */
    private int totalNotifications;

    /**
     * Constructs a new NotificationResponseDto with specified notifications and total notification count.
     *
     * @param notifications A list of NotificationDto objects.
     * @param totalNotifications The total number of notifications.
     */
    public NotificationResponseDto(List<NotificationDto> notifications, int totalNotifications) {
        this.notifications = notifications;
        this.totalNotifications = totalNotifications;
    }

    /**
     * Returns the list of notifications.
     *
     * @return A list of NotificationDto objects.
     */
    public List<NotificationDto> getNotifications() {
        return notifications;
    }

    /**
     * Sets the list of notifications.
     *
     * @param notifications A list of NotificationDto objects to be set.
     */
    public void setNotifications(List<NotificationDto> notifications) {
        this.notifications = notifications;
    }

    /**
     * Returns the total number of notifications.
     *
     * @return The total number of notifications.
     */
    public int getTotalNotifications() {
        return totalNotifications;
    }

    /**
     * Sets the total number of notifications.
     *
     * @param totalNotifications The total number of notifications to be set.
     */
    public void setTotalNotifications(int totalNotifications) {
        this.totalNotifications = totalNotifications;
    }
}