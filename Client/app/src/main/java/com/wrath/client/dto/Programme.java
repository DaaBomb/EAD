package com.wrath.client.dto;

import java.util.List;

public class Programme {
    private String name;
    private List<String> participants;

    public Programme(String name, List<String> participants) {
        this.name = name;
        this.participants = participants;
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
}
