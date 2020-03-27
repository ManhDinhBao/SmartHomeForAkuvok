package com.example.smarthome.Model.Room;

import com.example.smarthome.Model.Device.Device;

import java.util.ArrayList;

public class Room {
    private String ID;
    private String Name;
    private RoomType Type;
    private RoomPicture Picture;
    private String ApartmentID;
    private ArrayList<Device> Devices;
    public Room() {
    }

    public Room(String id, String name, RoomType type, RoomPicture picture, String apartmentId, ArrayList<Device> devices) {
        ID = id;
        Name = name;
        Type = type;
        Picture = picture;
        ApartmentID = apartmentId;
        Devices = devices;
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

    public RoomType getType() {
        return Type;
    }

    public void setType(RoomType type) {
        Type = type;
    }

    public RoomPicture getPicture() {
        return Picture;
    }

    public void setPicture(RoomPicture picture) {
        Picture = picture;
    }

    public String getApartmentID() {
        return ApartmentID;
    }

    public void setApartmentID(String apartmentID) {
        ApartmentID = apartmentID;
    }

    public ArrayList<Device> getDevices() {
        return Devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        Devices = devices;
    }
}
