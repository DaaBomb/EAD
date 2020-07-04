package com.wrath.client.dto;

import java.util.Date;

public class NotificationDetails {
    private String visitor_name;
    private String block_visiting;
    private String flatnum_visiting;
    private String society_id;
    private Boolean confirmed;
    private String purpose;
    private String _id;
    private Boolean responded;
    private Date date_created;

    public NotificationDetails(String visitor_name, String block_visiting, String flatnum_visiting, String society_id, Boolean confirmed, String purpose, String _id, Boolean responded, Date date_created) {
        this.visitor_name = visitor_name;
        this.block_visiting = block_visiting;
        this.flatnum_visiting = flatnum_visiting;
        this.society_id = society_id;
        this.confirmed = confirmed;
        this.purpose = purpose;
        this._id = _id;
        this.responded = responded;
        this.date_created = date_created;
    }

    public NotificationDetails() {
    }

    public Boolean getResponded() {
        return responded;
    }

    public void setResponded(Boolean responded) {
        this.responded = responded;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }

    public String getBlock_visiting() {
        return block_visiting;
    }

    public void setBlock_visiting(String block_visiting) {
        this.block_visiting = block_visiting;
    }

    public String getFlatnum_visiting() {
        return flatnum_visiting;
    }

    public void setFlatnum_visiting(String flatnum_visiting) {
        this.flatnum_visiting = flatnum_visiting;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
