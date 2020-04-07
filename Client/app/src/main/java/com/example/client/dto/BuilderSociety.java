package com.example.client.dto;

public class BuilderSociety {
    private String name;
    private String address;
    private String city;
    private User user;

    public BuilderSociety(String name, String address, String city, User user) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.user = user;
    }

    public BuilderSociety() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
