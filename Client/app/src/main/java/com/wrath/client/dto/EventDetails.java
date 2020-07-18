package com.wrath.client.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventDetails {
    private String _id;
    private String name;
    private String society_id;
    private Date start_date;
    private String description;
    private String creator_name;
    private List<String> attending;
    private Boolean food_choice;
    private List<Programme> programmes;
    private User user;
    private String time;

    public EventDetails() {
    }

    public EventDetails(String _id, String name, String society_id, Date start_date, String description, String creator_name, List<String> attending, Boolean food_choice, List<Programme> programmes, User user) {
        this._id = _id;
        this.name = name;
        this.society_id = society_id;
        this.start_date = start_date;
        this.description = description;
        this.creator_name = creator_name;
        this.attending = attending;
        this.food_choice = food_choice;
        this.programmes = programmes;
        this.user = user;
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

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator_name() {
        return creator_name;
    }

    public void setCreator_name(String creator_name) {
        this.creator_name = creator_name;
    }

    public List<String> getAttending() {
        return attending;
    }

    public void setAttending(List<String> attending) {
        this.attending = attending;
    }

    public Boolean getFood_choice() {
        return food_choice;
    }

    public void setFood_choice(Boolean food_choice) {
        this.food_choice = food_choice;
    }

    public List<Programme> getProgrammes() {
        return programmes;
    }

    public void setProgrammes(List<Programme> programmes) {
        this.programmes = programmes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
