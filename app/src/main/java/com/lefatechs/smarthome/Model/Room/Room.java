package com.lefatechs.smarthome.Model.Room;

import com.lefatechs.smarthome.Model.Device.Device;

import java.util.ArrayList;

public class Room {
    private String ID;
    private String Name;
    private RoomType RType;
    private RoomPicture Picture;
    private String ApartmentId;
    private ArrayList<Device> Devices;
    public Room() {
    }

    public Room(String id, String name, RoomType type, RoomPicture picture, String apartmentId, ArrayList<Device> devices) {
        ID = id;
        Name = name;
        RType = type;
        Picture = picture;
        ApartmentId = apartmentId;
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

    public RoomType getRType() {
        return RType;
    }

    public void setRType(RoomType RType) {
        this.RType = RType;
    }

    public RoomPicture getPicture() {
        return Picture;
    }

    public void setPicture(RoomPicture picture) {
        Picture = picture;
    }

    public String getApartmentId() {
        return ApartmentId;
    }

    public void setApartmentId(String apartmentId) {
        ApartmentId = apartmentId;
    }

    public ArrayList<Device> getDevices() {
        return Devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        Devices = devices;
    }
}
