package com.lefatechs.smarthome.Model.Error;

import com.lefatechs.smarthome.Model.User.User;

import java.util.Date;

public class Error {
    private String Id;
    private User Sender;
    private ErrorType Type;
    private ErrorStatus Status;
    private ErrorAttachment Attachment;
    private User Worker;
    private String Subject;
    private String Detail;
    private Date SendTime;

    public Error() {
    }

    public Error(String id, User sender, ErrorType type, ErrorStatus status, ErrorAttachment attachment, User worker, String subject, String detail, Date sendTime) {
        Id = id;
        Sender = sender;
        Type = type;
        Status = status;
        Attachment = attachment;
        Worker = worker;
        Subject = subject;
        Detail = detail;
        SendTime = sendTime;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public User getSender() {
        return Sender;
    }

    public void setSender(User sender) {
        Sender = sender;
    }

    public ErrorType getType() {
        return Type;
    }

    public void setType(ErrorType type) {
        Type = type;
    }

    public ErrorStatus getStatus() {
        return Status;
    }

    public void setStatus(ErrorStatus status) {
        Status = status;
    }

    public ErrorAttachment getAttachment() {
        return Attachment;
    }

    public void setAttachment(ErrorAttachment attachment) {
        Attachment = attachment;
    }

    public User getWorker() {
        return Worker;
    }

    public void setWorker(User worker) {
        Worker = worker;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public Date getSendTime() {
        return SendTime;
    }

    public void setSendTime(Date sendTime) {
        SendTime = sendTime;
    }
}
