package com.wrath.client.dto;

import java.util.List;

public class EventRegisterRequest extends BaseResponse{
    private Boolean attending;
    private List<String> programmeslist;
    private Boolean food_choice;
    private EventDetails eventDetails;

    public EventRegisterRequest() {
    }

    public EventRegisterRequest(Boolean attending, List<String> programmeslist, Boolean food_choice, EventDetails eventDetails) {
        this.attending = attending;
        this.programmeslist = programmeslist;
        this.food_choice = food_choice;
        this.eventDetails = eventDetails;
    }

    public Boolean getAttending() {
        return attending;
    }

    public void setAttending(Boolean attending) {
        this.attending = attending;
    }

    public List<String> getProgrammeslist() {
        return programmeslist;
    }

    public void setProgrammeslist(List<String> programmeslist) {
        this.programmeslist = programmeslist;
    }

    public Boolean getFood_choice() {
        return food_choice;
    }

    public void setFood_choice(Boolean food_choice) {
        this.food_choice = food_choice;
    }

    public EventDetails getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(EventDetails eventDetails) {
        this.eventDetails = eventDetails;
    }
}
