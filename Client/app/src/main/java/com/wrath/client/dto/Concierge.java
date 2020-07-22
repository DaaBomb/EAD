package com.wrath.client.dto;

import java.util.Date;

public class Concierge {
    private String requirement;
    private String details;
    private User user;
    private Date date_needed;
    private String time_needed;
    private String latitude;
    private String longitude;
    private boolean responded;
    private boolean approved;
    private String blockname;
    private String flatnum;
    private String society_id;
    private String _id;

    public Concierge() {
    }

    public Concierge(String requirement, String details, User user, Date date_needed, String time_needed, String latitude, String longitude, boolean responded, boolean approved, String blockname, String flatnum, String society_id, String _id) {
        this.requirement = requirement;
        this.details = details;
        this.user = user;
        this.date_needed = date_needed;
        this.time_needed = time_needed;
        this.latitude = latitude;
        this.longitude = longitude;
        this.responded = responded;
        this.approved = approved;
        this.blockname = blockname;
        this.flatnum = flatnum;
        this.society_id = society_id;
        this._id = _id;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate_needed() {
        return date_needed;
    }

    public void setDate_needed(Date date_needed) {
        this.date_needed = date_needed;
    }

    public String getTime_needed() {
        return time_needed;
    }

    public void setTime_needed(String time_needed) {
        this.time_needed = time_needed;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isResponded() {
        return responded;
    }

    public void setResponded(boolean responded) {
        this.responded = responded;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getBlockname() {
        return blockname;
    }

    public void setBlockname(String blockname) {
        this.blockname = blockname;
    }

    public String getFlatnum() {
        return flatnum;
    }

    public void setFlatnum(String flatnum) {
        this.flatnum = flatnum;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
