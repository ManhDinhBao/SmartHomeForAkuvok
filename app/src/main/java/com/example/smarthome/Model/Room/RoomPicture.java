package com.example.smarthome.Model.Room;

public class RoomPicture {
    private String ID;
    private String IconPicture;
    private String WallPicture;

    public RoomPicture() {
    }

    public RoomPicture(String id, String iconPicture, String wallPicture) {
        ID = id;
        IconPicture = iconPicture;
        WallPicture = wallPicture;
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        ID = id;
    }

    public String getIconPicture() {
        return IconPicture;
    }

    public void setIconPicture(String iconPicture) {
        IconPicture = iconPicture;
    }

    public String getWallPicture() {
        return WallPicture;
    }

    public void setWallPicture(String wallPicture) {
        WallPicture = wallPicture;
    }
}
