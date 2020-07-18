package com.wrath.client.dto;

import java.util.List;

public class Programme {
    private String name;
    private String description;
    private List<String> participants;

    public Programme(String name, String description, List<String> participants) {
        this.name = name;
        this.description = description;
        this.participants = participants;
    }

    public Programme(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Programme() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
