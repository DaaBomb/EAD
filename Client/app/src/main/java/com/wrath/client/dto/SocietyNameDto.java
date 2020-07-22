package com.wrath.client.dto;

public class SocietyNameDto extends BaseResponse{
    private Society society;

    public SocietyNameDto(Society society) {
        this.society = society;
    }

    public SocietyNameDto(String msg, User user, Society society) {
        super(msg, user);
        this.society = society;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}
