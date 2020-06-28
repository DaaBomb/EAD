package com.wrath.client.dto;

public class PermissionRequest {
    private String _id;
    private Boolean confirmed;

    public PermissionRequest(String _id, Boolean confirmed) {
        this._id = _id;
        this.confirmed = confirmed;
    }

    public PermissionRequest() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}
