package com.lefatechs.smarthome.Model.Point;

public class PointType {
    private String ID;
    private String Name;

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

    public PointType(String id, String name) {
        ID = id;
        Name = name;
    }

    public PointType() {
    }
}
