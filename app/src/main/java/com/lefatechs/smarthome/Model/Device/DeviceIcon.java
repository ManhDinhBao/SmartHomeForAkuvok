package com.lefatechs.smarthome.Model.Device;

public class DeviceIcon {
    private String ID;
    private String IconOn;
    private String IconOff;

    public DeviceIcon() {
    }

    public DeviceIcon(String id, String iconOn, String iconOff) {
        ID = id;
        IconOn = iconOn;
        IconOff = iconOff;
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        ID = id;
    }

    public String getIconOn() {
        return IconOn;
    }

    public void setIconOn(String iconOn) {
        IconOn = iconOn;
    }

    public String getIconOff() {
        return IconOff;
    }

    public void setIconOff(String iconOff) {
        IconOff = iconOff;
    }
}
