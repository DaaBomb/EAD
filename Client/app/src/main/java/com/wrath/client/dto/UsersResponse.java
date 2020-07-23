package com.wrath.client.dto;

import java.util.List;

public class UsersResponse extends BaseResponse {
    private List<User> users;

    public UsersResponse(List<User> users) {
        this.users = users;
    }

    public UsersResponse(String msg, User user, List<User> users) {
        super(msg, user);
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
