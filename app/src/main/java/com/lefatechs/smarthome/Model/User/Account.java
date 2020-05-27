package com.lefatechs.smarthome.Model.User;

public class Account {
    private String Id;
    private String UserName;
    private String Password;

    public Account() {
    }

    public Account(String id, String userName, String password) {
        Id = id;
        UserName = userName;
        Password = password;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
