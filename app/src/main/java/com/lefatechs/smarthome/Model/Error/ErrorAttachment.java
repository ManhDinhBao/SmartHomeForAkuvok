package com.lefatechs.smarthome.Model.Error;

public class ErrorAttachment {
    private String Id;
    private String Address;

    public ErrorAttachment() {
    }

    public ErrorAttachment(String id, String address) {
        Id = id;
        Address = address;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
