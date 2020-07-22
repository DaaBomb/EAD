package com.wrath.client.dto;

public class Society {
    private String _id;
    private String name;

    public Society(String _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public Society() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
