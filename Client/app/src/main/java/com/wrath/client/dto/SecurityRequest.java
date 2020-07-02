package com.wrath.client.dto;

public class SecurityRequest {
    private String visitor_name;
    private String blockname;
    private String flatnum;
    private String token;
    private String purpose;
    private User user;


    public SecurityRequest(String visitor_name, String blockname, String flatnum, String token, String purpose, User user) {
        this.visitor_name = visitor_name;
        this.blockname = blockname;
        this.flatnum = flatnum;
        this.token = token;
        this.purpose = purpose;
        this.user = user;
    }

    public SecurityRequest() {
    }

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }

    public String getBlockname() {
        return blockname;
    }

    public void setBlockname(String blockname) {
        this.blockname = blockname;
    }

    public String getFlatnum() {
        return flatnum;
    }

    public void setFlatnum(String flatnum) {
        this.flatnum = flatnum;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
