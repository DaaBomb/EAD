package com.wrath.client.dto;

public class AddSportRequest {
    private User user;
    private String sport;
    private String description;
    private int numberOfPlayers;
    private String time;

    public AddSportRequest() {
    }

    public AddSportRequest(User user, String sport, String description, int numberOfPlayers, String time) {
        this.user = user;
        this.sport = sport;
        this.description = description;
        this.numberOfPlayers = numberOfPlayers;
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
