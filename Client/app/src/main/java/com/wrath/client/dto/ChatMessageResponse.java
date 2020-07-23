package com.wrath.client.dto;

import com.wrath.client.common.BaseNav;

import java.util.List;

public class ChatMessageResponse extends BaseResponse {
    private List<ChatMessage> chats;
    private ChatMessage chat;

    public ChatMessageResponse(List<ChatMessage> chats, ChatMessage chat) {
        this.chats = chats;
        this.chat = chat;
    }

    public ChatMessageResponse(String msg, User user, List<ChatMessage> chats, ChatMessage chat) {
        super(msg, user);
        this.chats = chats;
        this.chat = chat;
    }

    public List<ChatMessage> getChats() {
        return chats;
    }

    public void setChats(List<ChatMessage> chats) {
        this.chats = chats;
    }

    public ChatMessage getChat() {
        return chat;
    }

    public void setChat(ChatMessage chat) {
        this.chat = chat;
    }
}
