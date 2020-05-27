package com.lefatechs.smarthome.Model.Error;

public class ErrorType {
    private String Id;
    private String Name;

    public ErrorType() {
    }

    public ErrorType(String id, String name) {
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
