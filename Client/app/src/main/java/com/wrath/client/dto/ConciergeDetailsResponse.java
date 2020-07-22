package com.wrath.client.dto;

import java.util.List;

public class ConciergeDetailsResponse extends BaseResponse {
    private List<Concierge> concierge;

    public ConciergeDetailsResponse(List<Concierge> concierge) {
        this.concierge = concierge;
    }

    public ConciergeDetailsResponse(String msg, User user, List<Concierge> concierge) {
        super(msg, user);
        this.concierge = concierge;
    }

    public List<Concierge> getConcierge() {
        return concierge;
    }

    public void setConcierge(List<Concierge> concierge) {
        this.concierge = concierge;
    }
}
