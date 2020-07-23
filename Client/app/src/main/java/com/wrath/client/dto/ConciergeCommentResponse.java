package com.wrath.client.dto;

import com.wrath.client.common.BaseNav;

public class ConciergeCommentResponse extends BaseResponse {
    private Concierge concierge;

    public ConciergeCommentResponse() {
    }

    public ConciergeCommentResponse(Concierge concierge) {
        this.concierge = concierge;
    }

    public Concierge getConcierge() {
        return concierge;
    }

    public void setConcierge(Concierge concierge) {
        this.concierge = concierge;
    }
}
