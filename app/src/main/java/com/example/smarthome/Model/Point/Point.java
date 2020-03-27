package com.example.smarthome.Model.Point;

import android.content.Intent;

public class Point {
    private String ID;
    private String Name;
    private PointType Type;
    private String DeviceID;
    private PointCharacter Character;
    private String Descr;
    private String Alias;
    private String PublishAddress;
    private String SubcribeAddress;
    private Integer CurrentValue;

    public Point() {
    }

    public Point(String id, String name, PointType type, String deviceId, PointCharacter character, String description, String alias, String publishAddress, String subcribeAddress) {
        ID = id;
        Name = name;
        Type = type;
        DeviceID = deviceId;
        Character = character;
        Descr = description;
        Alias = alias;
        PublishAddress = publishAddress;
        SubcribeAddress = subcribeAddress;
        CurrentValue=0;
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

    public PointType getType() {
        return Type;
    }

    public void setType(PointType type) {
        Type = type;
    }

    public String getDeviceId() {
        return DeviceID;
    }

    public void setDeviceId(String deviceId) {
        DeviceID = deviceId;
    }

    public PointCharacter getCharacter() {
        return Character;
    }

    public void setCharacter(PointCharacter character) {
        Character = character;
    }

    public String getDescription() {
        return Descr;
    }

    public void setDescription(String description) {
        Descr = description;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getPublishAddress() {
        return PublishAddress;
    }

    public void setPublishAddress(String publishAddress) {
        PublishAddress = publishAddress;
    }

    public String getSubcribeAddress() {
        return SubcribeAddress;
    }

    public void setSubcribeAddress(String subcribeAddress) {
        SubcribeAddress = subcribeAddress;
    }

    public Integer getValue() {
        return CurrentValue;
    }

    public void setValue(Integer value) {
        CurrentValue = value;
    }
}
