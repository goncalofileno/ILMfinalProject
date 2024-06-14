package com.ilm.projecto_ilm_backend.dto.notification;

import java.util.List;

public class NotificationResponseDto {
    private List<NotificationDto> notifications;
    private int totalNotifications;

    public NotificationResponseDto(List<NotificationDto> notifications, int totalNotifications) {
        this.notifications = notifications;
        this.totalNotifications = totalNotifications;
    }

    public List<NotificationDto> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDto> notifications) {
        this.notifications = notifications;
    }

    public int getTotalNotifications() {
        return totalNotifications;
    }

    public void setTotalNotifications(int totalNotifications) {
        this.totalNotifications = totalNotifications;
    }
}

