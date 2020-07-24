package com.wrath.client.dto;

import java.util.List;

public class UpdateProfileRequest {
    private String _id;
    private String name;
    private String email;
    private List<String> interests;

    public UpdateProfileRequest(String _id, String name, String email, List<String> interests) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.interests = interests;
    }

    public UpdateProfileRequest() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
