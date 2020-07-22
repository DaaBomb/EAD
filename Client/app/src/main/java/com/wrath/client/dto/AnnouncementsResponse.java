package com.wrath.client.dto;

import java.util.List;

public class AnnouncementsResponse extends BaseResponse {
    private List<Announcement> announcements;

    public AnnouncementsResponse(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public AnnouncementsResponse(String msg, User user, List<Announcement> announcements) {
        super(msg, user);
        this.announcements = announcements;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }
}
