package com.lefatechs.smarthome.Model;

import java.util.ArrayList;

public class Building {
    private String Id;
    private String Name;
    private String Address;
    private ArrayList<Floor> Floors;

    public Building() {
    }

    public Building(String id, String name, String address, ArrayList<Floor> floors) {
        Id = id;
        Name = name;
        Address = address;
        Floors = floors;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public ArrayList<Floor> getFloors() {
        return Floors;
    }

    public void setFloors(ArrayList<Floor> floors) {
        Floors = floors;
    }
}
