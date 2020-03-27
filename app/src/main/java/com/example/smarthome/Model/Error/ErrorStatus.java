package com.example.smarthome.Model.Error;

public class ErrorStatus {
    private String Id;
    private String Name;

    public ErrorStatus() {
    }

    public ErrorStatus(String id, String name) {
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
