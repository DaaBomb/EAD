package com.example.client.dto;

public class CommentUser {
    private String topic_id;
    private String comment;
    private User user;

    public CommentUser(String topic_id, String comment, User user) {
        this.topic_id = topic_id;
        this.comment = comment;
        this.user = user;
    }

    public CommentUser() {
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
