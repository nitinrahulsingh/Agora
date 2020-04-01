package com.intelegain.agora.model;

public class NotificationItem {

    public String title;
    public String message;
    public String status;

    public NotificationItem() {
    }

    public NotificationItem(String title, String message, String status) {
        this.title = title;
        this.message = message;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}