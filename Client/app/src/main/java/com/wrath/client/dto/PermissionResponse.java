package com.wrath.client.dto;

public class PermissionResponse {
    private String msg;
    private NotificationDetails notificationDetails;

    public PermissionResponse(String msg, NotificationDetails notificationDetails) {
        this.msg = msg;
        this.notificationDetails = notificationDetails;
    }

    public PermissionResponse() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public NotificationDetails getNotificationDetails() {
        return notificationDetails;
    }

    public void setNotificationDetails(NotificationDetails notificationDetails) {
        this.notificationDetails = notificationDetails;
    }
}
