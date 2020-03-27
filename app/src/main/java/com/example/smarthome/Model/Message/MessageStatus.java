package com.example.smarthome.Model.Message;

public class MessageStatus {
    private String Id;
    private String Name;

    public MessageStatus() {
    }

    public MessageStatus(String id, String name) {
        Id = id;
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
