package com.example.Demo_Caro.model;

public class Message {
    private String playerName;
    private String content;

    // Constructor, getter, setter
    public Message(String playerName, String content) {
        this.playerName = playerName;
        this.content = content;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
