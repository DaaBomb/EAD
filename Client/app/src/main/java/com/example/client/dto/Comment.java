package com.example.client.dto;

import java.util.Date;

public class Comment {

    private String person_name;
    private Date date_created;
    private String _id;
    private String comment;

    public Comment() {
    }

    public Comment(String person_name, Date date_created, String _id, String comment) {
        this.person_name = person_name;
        this.date_created = date_created;
        this._id = _id;
        this.comment = comment;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
