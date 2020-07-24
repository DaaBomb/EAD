package com.wrath.client.dto;

import java.util.List;

public class updateProfileRequest {
    private String name;
    private String email;
    private List<String> interests;

    public updateProfileRequest(String name, String email, List<String> interests) {
        this.name = name;
        this.email = email;
        this.interests = interests;
    }

    public updateProfileRequest() {
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
