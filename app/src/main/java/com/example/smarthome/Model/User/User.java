package com.example.smarthome.Model.User;

public class User {
    private String Id;
    private String ApartmentId;
    private Account Account;
    private String RoleId;
    private String Name;
    private String Address;
    private String PhoneNo;
    private String Description;

    public User() {
    }

    public User(String id, String apartmentId, com.example.smarthome.Model.User.Account account, String roleId, String name, String address, String phoneNo, String description) {
        Id = id;
        ApartmentId = apartmentId;
        Account = account;
        RoleId = roleId;
        Name = name;
        Address = address;
        PhoneNo = phoneNo;
        Description = description;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getApartmentId() {
        return ApartmentId;
    }

    public void setApartmentId(String apartmentId) {
        ApartmentId = apartmentId;
    }

    public com.example.smarthome.Model.User.Account getAccount() {
        return Account;
    }

    public void setAccount(com.example.smarthome.Model.User.Account account) {
        Account = account;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
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

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
