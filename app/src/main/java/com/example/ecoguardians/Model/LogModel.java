package com.example.ecoguardians.Model;



public class LogModel {
    private String action;
    private long timestamp;

    public LogModel() { }

    public LogModel(String action, long timestamp) {
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
