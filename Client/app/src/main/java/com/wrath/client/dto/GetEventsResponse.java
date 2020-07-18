package com.wrath.client.dto;

import java.util.List;

public class GetEventsResponse extends BaseResponse{
    private List<EventDetails> eventDetails;

    public GetEventsResponse() {
    }

    public GetEventsResponse(List<EventDetails> eventDetails) {
        this.eventDetails = eventDetails;
    }

    public List<EventDetails> getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(List<EventDetails> eventDetails) {
        this.eventDetails = eventDetails;
    }
}
