package com.lefatechs.smarthome.Model.Device;

import com.lefatechs.smarthome.Model.Point.Point;
import com.lefatechs.smarthome.Utils.Comm;

import java.util.ArrayList;

public class Device {
    private String ID;
    private String RoomId;
    private String Name;
    private DeviceType DType;
    private String Descr;
    private DeviceIcon Icon;
    private boolean Power;
    private int deviceViewType;
    private ArrayList<Point> Points;
    private String CurrentValue;

    public Device() {
    }

    public Device(String id, String roomId, String name, DeviceType type, String description, DeviceIcon icon, boolean power, ArrayList<Point> points) {
        ID = id;
        RoomId = roomId;
        Name = name;
        DType = type;
        Descr = description;
        Icon = icon;
        Power = power;
        Points = points;

        switch (type.getID()) {
            case "DTY0000001":
            case "DTY0000002":
                deviceViewType = 1;
                break;
            case "DTY0000010":
                deviceViewType = 2;
                break;
            default:
                deviceViewType = 0;
                break;
        }
        CurrentValue = "0";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public DeviceType getDType() {
        return DType;
    }

    public void setDType(DeviceType DType) {
        this.DType = DType;
    }

    public String getDescription() {
        return Descr;
    }

    public void setDescription(String description) {
        Descr = description;
    }

    public DeviceIcon getIcon() {
        return Icon;
    }

    public void setIcon(DeviceIcon icon) {
        Icon = icon;
    }

    public boolean isPower() {
        return Power;
    }

    public void setPower(boolean power) {
        Power = power;
    }

    public int getDeviceViewType() {
        return deviceViewType;
    }

    public void setDeviceViewType(int deviceViewType) {
        this.deviceViewType = deviceViewType;
    }

    public ArrayList<Point> getPoints() {
        return Points;
    }

    public void setPoints(ArrayList<Point> points) {
        Points = points;
    }

    public String getCurrentValue() {
        return CurrentValue;
    }

    public void setCurrentValue(String currentValue) {
        CurrentValue = currentValue;
    }

    public String GetPowerPublicTopic() {
        for (Point p : Points) {
            if (p.getAlias().matches("Power")) {
                return p.getPublishAddress();
            }
        }
        return null;
    }

    public void SetDeviceViewType() {
        switch (getDType().getID()) {
            case Comm.DEVICE_TYPE_LIGHT:
            case Comm.DEVICE_TYPE_AC:
                setDeviceViewType(1);
                break;
            case Comm.DEVICE_TYPE_CURTAIN:
                setDeviceViewType(2);
                break;
            default:
                setDeviceViewType(0);
                break;
        }
        setCurrentValue("0");
    }
}
