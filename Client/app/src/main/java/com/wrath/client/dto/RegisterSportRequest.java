package com.wrath.client.dto;

public class RegisterSportRequest {
    private String _id;
    private User user;

    public RegisterSportRequest(String _id, User user) {
        this._id = _id;
        this.user = user;
    }

    public RegisterSportRequest() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
