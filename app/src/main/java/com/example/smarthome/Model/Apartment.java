package com.example.smarthome.Model;

import com.example.smarthome.Model.Device.Device;
import com.example.smarthome.Model.Room.Room;

import java.util.ArrayList;

public class Apartment {
    private String ID;
    private String Name;
    private String FloorId;
    private String Description;
    private ArrayList<Room> ListRooms;
    private ArrayList<Device> Scenes;

    public Apartment() {
    }

    public Apartment(String id, String name, String floorId, String description, ArrayList<Room> listRooms, ArrayList<Device> scenes) {
        ID = id;
        Name = name;
        FloorId = floorId;
        Description = description;
        ListRooms = listRooms;
        Scenes = scenes;
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

    public String getFloorId() {
        return FloorId;
    }

    public void setFloorId(String floorId) {
        FloorId = floorId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public ArrayList<Room> getListRooms() {
        return ListRooms;
    }

    public void setListRooms(ArrayList<Room> listRooms) {
        ListRooms = listRooms;
    }

    public ArrayList<Device> getScenes() {
        return Scenes;
    }

    public void setScenes(ArrayList<Device> scenes) {
        Scenes = scenes;
    }
}
