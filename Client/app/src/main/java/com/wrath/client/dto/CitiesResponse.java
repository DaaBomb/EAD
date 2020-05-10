package com.wrath.client.dto;

import java.util.List;

public class CitiesResponse extends BaseResponse {

    private List<String> cities;

    public CitiesResponse() {
    }

    public CitiesResponse(String msg, User user, List<String> cities) {
        super(msg, user);
        this.cities = cities;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
