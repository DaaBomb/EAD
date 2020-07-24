package com.wrath.client.dto;

import java.util.List;

public class Society {
    private String _id;
    private String name;
    private List<String> ameneties;

    public Society() {
    }

    public Society(String _id, String name, List<String> ameneties) {
        this._id = _id;
        this.name = name;
        this.ameneties = ameneties;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAmeneties() {
        return ameneties;
    }

    public void setAmeneties(List<String> ameneties) {
        this.ameneties = ameneties;
    }
}
