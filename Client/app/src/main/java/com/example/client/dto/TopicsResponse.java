package com.example.client.dto;

import java.util.List;

public class TopicsResponse extends BaseResponse{
    private List<Topic> topics;

    public TopicsResponse() {
    }

    public TopicsResponse(String msg, User user, List<Topic> topics) {
        super(msg, user);
        this.topics = topics;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }
}
