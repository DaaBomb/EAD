package com.wrath.client.dto;

import java.util.Date;
import java.util.List;

public class Sport {
    private String _id;
    private User created_by;
    private Date date_created;
    private String sport;
    private String description;
    private int numberOfPlayers;
    private String time;
    private List<User> participants;

    public Sport() {
    }

    public Sport(String _id, User created_by, Date date_created, String sport, String description, int numberOfPlayers, String time, List<User> participants) {
        this._id = _id;
        this.created_by = created_by;
        this.date_created = date_created;
        this.sport = sport;
        this.description = description;
        this.numberOfPlayers = numberOfPlayers;
        this.time = time;
        this.participants = participants;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public User getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
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

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
}
