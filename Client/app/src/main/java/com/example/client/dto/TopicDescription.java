package com.example.client.dto;

public class TopicDescription {
    private String topic;
    private String description;
    private Boolean is_discussion;
    private  User user;

    public TopicDescription(String topic, String description, Boolean is_discussion, User user) {
        this.topic = topic;
        this.description = description;
        this.is_discussion = is_discussion;
        this.user = user;
    }

    public TopicDescription() {
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIs_discussion() {
        return is_discussion;
    }

    public void setIs_discussion(Boolean is_discussion) {
        this.is_discussion = is_discussion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
