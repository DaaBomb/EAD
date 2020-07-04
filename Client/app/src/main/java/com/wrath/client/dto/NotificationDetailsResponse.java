package com.wrath.client.dto;

import java.util.List;

public class NotificationDetailsResponse extends BaseResponse{
    private List<NotificationDetails> notificationDetails;

    public NotificationDetailsResponse(){
    }

    public NotificationDetailsResponse(List<NotificationDetails> notificationDetails) {
        this.notificationDetails = notificationDetails;
    }

    public List<NotificationDetails> getNotificationDetails() {
        return notificationDetails;
    }

    public void setNotificationDetails(List<NotificationDetails> notificationDetails) {
        this.notificationDetails = notificationDetails;
    }
}
