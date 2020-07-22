package com.wrath.client.util;

import com.wrath.client.dto.User;

public class RulesUtil {
    public static boolean isManager(User user) {
        return user.getProfession() != null && user.getProfession().equalsIgnoreCase("manager");
    }

    public static boolean isSecurity(User user) {
        return user.getProfession() != null && user.getProfession().equalsIgnoreCase("security");
    }

    public static boolean isBuilder(User user) {
        return user.getProfession() != null && user.getProfession().equalsIgnoreCase("builder");
    }

    public static boolean isResident(User user) {
        return user.getProfession() == null;
    }
}
