package com.lefatechs.smarthome.Model.Message;

import java.util.Date;
import java.util.ArrayList;

public class Message {
    private String Id;
    private String Title;
    private String Detail;
    private Date SendDate;
    private boolean IsEmergency;
    private ArrayList<String> AttachAddress;

    public Message() {
    }

    public Message(String id, String title, String detail, Date sendDate, boolean isEmergency, ArrayList<String> attachAddress) {
        Id = id;
        Title = title;
        Detail = detail;
        SendDate = sendDate;
        IsEmergency = isEmergency;
        AttachAddress = attachAddress;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public boolean isEmergency() {
        return IsEmergency;
    }

    public void setEmergency(boolean emergency) {
        IsEmergency = emergency;
    }

    public Date getSendDate() {
        return SendDate;
    }

    public void setSendDate(Date sendDate) {
        SendDate = sendDate;
    }

    public ArrayList<String> getAttachAddress() {
        return AttachAddress;
    }

    public void setAttachAddress(ArrayList<String> attachAddress) {
        AttachAddress = attachAddress;
    }
}
