package com.example.client.dto;

public class BaseResponse {
    private String msg;
    private User user;

    public BaseResponse() {
    }

    public BaseResponse(String msg, User user) {
        this.msg = msg;
        this.user = user;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
