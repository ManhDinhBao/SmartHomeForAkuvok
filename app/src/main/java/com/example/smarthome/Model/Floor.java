package com.example.smarthome.Model;

import java.util.ArrayList;
import java.util.List;

public class Floor {
    private String Id;
    private String Name;
    private String BuildingId;
    private ArrayList<Apartment> Apartments;

    public Floor() {
    }

    public Floor(String id, String name, String buildingId, ArrayList<Apartment> apartments) {
        Id = id;
        Name = name;
        BuildingId = buildingId;
        Apartments = apartments;
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

    public String getBuildingId() {
        return BuildingId;
    }

    public void setBuildingId(String buildingId) {
        BuildingId = buildingId;
    }

    public ArrayList<Apartment> getApartments() {
        return Apartments;
    }

    public void setApartments(ArrayList<Apartment> apartments) {
        Apartments = apartments;
    }
}
