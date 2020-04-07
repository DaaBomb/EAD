package com.example.client.dto;

public class UserAddress {
    private String society_id;
    private String blockname;
    private String flatnum;

    public UserAddress() {
    }

    public UserAddress(String society_id, String blockname, String flatnum) {
        this.society_id = society_id;
        this.blockname = blockname;
        this.flatnum = flatnum;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
        this.society_id = society_id;
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
}
