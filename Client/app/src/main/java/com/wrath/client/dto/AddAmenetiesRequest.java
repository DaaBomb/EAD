package com.wrath.client.dto;

import java.util.List;

public class AddAmenetiesRequest {
    private String society_id;
    private List<String> ameneties;

    public AddAmenetiesRequest(String society_id, List<String> ameneties) {
        this.society_id = society_id;
        this.ameneties = ameneties;
    }

    public AddAmenetiesRequest() {
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
    }

    public List<String> getAmeneties() {
        return ameneties;
    }

    public void setAmeneties(List<String> ameneties) {
        this.ameneties = ameneties;
    }
}
