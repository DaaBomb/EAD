package com.wrath.client.dto;

public class AmenetiesResponse extends BaseResponse{
    private Society society;

    public AmenetiesResponse() {
    }

    public AmenetiesResponse(Society society) {
        this.society = society;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}
