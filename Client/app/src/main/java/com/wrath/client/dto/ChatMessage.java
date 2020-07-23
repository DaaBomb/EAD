package com.wrath.client.dto;

import java.util.Date;

public class ChatMessage {
    private User from;
    private User to;
    private String message;
    private Date date_created;

    public ChatMessage() {
    }

    public ChatMessage(User from, User to, String message, Date date_created) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date_created = date_created;
    }

    public ChatMessage(User from, User to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }
}
