package com.wrath.client.dto;

import java.util.Date;
import java.util.List;

public class Concierge {
    private String requirement;
    private String details;
    private User user;
    private Date date_needed;
    private String time_needed;
    private String latitude;
    private String longitude;
    private Boolean responded;
    private Boolean approved;
    private String blockname;
    private String flatnum;
    private String society_id;
    private String _id;
    private Boolean resident_responded;
    private Boolean done;
    private List<Comment> comments;

    public Concierge() {
    }

    public Concierge(String requirement, String details, User user, Date date_needed, String time_needed, String latitude, String longitude, Boolean responded, Boolean approved, String blockname, String flatnum, String society_id, String _id, Boolean resident_responded, Boolean done, List<Comment> comments) {
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
        this.resident_responded = resident_responded;
        this.done = done;
        this.comments = comments;
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

    public Boolean isResponded() {
        return responded;
    }

    public void setResponded(Boolean responded) {
        this.responded = responded;
    }

    public Boolean isApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
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

    public Boolean isResident_responded() {
        return resident_responded;
    }

    public void setResident_responded(Boolean resident_responded) {
        this.resident_responded = resident_responded;
    }

    public Boolean isDone(){
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
