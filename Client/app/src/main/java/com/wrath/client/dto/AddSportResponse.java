package com.wrath.client.dto;

public class AddSportResponse extends BaseResponse {
    private Sport sport;

    public AddSportResponse() {
    }

    public AddSportResponse(String msg, User user) {
        super(msg, user);
    }

    public AddSportResponse(Sport sport) {
        this.sport = sport;
    }

    public AddSportResponse(String msg, User user, Sport sport) {
        super(msg, user);
        this.sport = sport;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }
}
