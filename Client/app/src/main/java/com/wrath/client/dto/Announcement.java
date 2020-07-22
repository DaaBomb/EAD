package com.wrath.client.dto;

import java.util.Date;

public class Announcement {
    private String _id;
    private String announcement;
    private String society_id;
    private Date date_created;

    public Announcement() {
    }

    public Announcement(String _id, String announcement, String society_id, Date date_created) {
        this._id = _id;
        this.announcement = announcement;
        this.society_id = society_id;
        this.date_created = date_created;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
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
}
