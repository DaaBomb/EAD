package com.example.client.dto;

public class UserResidence {
    private String name;
    private String city;
    private String blockname;
    private String flatnum;
    private User user;

    public UserResidence() {
    }

    public UserResidence(String name, String city, String blockname, String flatnum, User user) {
        this.name = name;
        this.city = city;
        this.blockname = blockname;
        this.flatnum = flatnum;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
