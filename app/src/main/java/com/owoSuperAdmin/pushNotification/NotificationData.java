package com.owoSuperAdmin.pushNotification;

public class NotificationData {
    private String subject;
    private String content;
    private String imageUrl;

    public NotificationData() {
    }

    public NotificationData(String subject, String content, String imageUrl) {
        this.subject = subject;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
