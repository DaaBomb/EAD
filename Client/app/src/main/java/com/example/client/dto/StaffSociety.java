package com.example.client.dto;

public class StaffSociety {
    private String name;
    private String city;
    private String profession;
    private User user;

    public StaffSociety(String name, String city, String profession, User user) {
        this.name = name;
        this.city = city;
        this.profession = profession;
        this.user = user;
    }

    public StaffSociety() {
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
