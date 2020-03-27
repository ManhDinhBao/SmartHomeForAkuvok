package com.example.smarthome.Model.Room;

public class RoomType {

    private String ID;
    private String Name;

    public RoomType() {
    }

    public RoomType(String id, String name) {
        ID = id;
        Name = name;
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        ID = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
