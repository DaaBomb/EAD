package com.example.client.dto;

import java.util.Date;
import java.util.List;

public class Topic {

    private boolean is_discussion;
    private String topic;
    private String description;
    private String creator_name;
    private String _id;
    private String society_id;
    private Date date_created;
    private List<Comment> comments;

    public Topic() {
    }

    public Topic(boolean is_discussion, String topic, String description, String creator_name, String _id, String society_id, Date date_created, List<Comment> comments) {
        this.is_discussion = is_discussion;
        this.topic = topic;
        this.description = description;
        this.creator_name = creator_name;
        this._id = _id;
        this.society_id = society_id;
        this.date_created = date_created;
        this.comments = comments;
    }

    public boolean isIs_discussion() {
        return is_discussion;
    }

    public void setIs_discussion(boolean is_discussion) {
        this.is_discussion = is_discussion;
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

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
