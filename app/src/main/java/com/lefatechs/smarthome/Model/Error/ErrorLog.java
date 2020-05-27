package com.lefatechs.smarthome.Model.Error;

import com.lefatechs.smarthome.Model.User.User;

import java.util.Date;

public class ErrorLog {
    private String ErrorId;
    private ErrorStatus Status;
    private User Worker;
    private Date Time;

    public ErrorLog() {
    }

    public String getErrorId() {
        return ErrorId;
    }

    public void setErrorId(String errorId) {
        ErrorId = errorId;
    }

    public ErrorStatus getStatus() {
        return Status;
    }

    public void setStatus(ErrorStatus status) {
        Status = status;
    }

    public User getWorker() {
        return Worker;
    }

    public void setWorker(User worker) {
        Worker = worker;
    }

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
