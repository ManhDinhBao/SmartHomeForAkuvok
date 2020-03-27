package com.example.smarthome.Model.Device;

public class DeviceType {
    private String ID;
    private String Name;

    public DeviceType() {
    }

    public DeviceType(String id, String name) {
        ID = id;
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
