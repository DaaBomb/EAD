package com.wrath.client.dto;

import java.util.List;

public class GetSportsResponse extends BaseResponse {
    private List<Sport> sports;

    public GetSportsResponse() {
    }

    public GetSportsResponse(List<Sport> sports) {
        this.sports = sports;
    }

    public List<Sport> getSports() {
        return sports;
    }

    public void setSports(List<Sport> sports) {
        this.sports = sports;
    }
}
