package com.lefatechs.smarthome.Model.Point;

public class PointCharacter {
    private String ID;
    private String Data;

    public PointCharacter(String id, String map) {
        ID = id;
        Data = map;
    }

    public PointCharacter() {
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        ID = id;
    }

    public String getMap() {
        return Data;
    }

    public void setMap(String map) {
        Data = map;
    }
}
