package com.lefatechs.smarthome.Model.Message;

import com.lefatechs.smarthome.Model.User.User;

import java.util.Date;

public class MessageLog {
    private String MessageId;
    private User Receiver;
    private Date SendDate;
    private MessageStatus Status;

    public MessageLog() {
    }

    public MessageLog(String messageId, User receiver, Date sendDate, MessageStatus status) {
        MessageId = messageId;
        Receiver = receiver;
        SendDate = sendDate;
        Status = status;
    }

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }

    public User getReceiver() {
        return Receiver;
    }

    public void setReceiver(User receiver) {
        Receiver = receiver;
    }

    public Date getSendDate() {
        return SendDate;
    }

    public void setSendDate(Date sendDate) {
        SendDate = sendDate;
    }

    public MessageStatus getStatus() {
        return Status;
    }

    public void setStatus(MessageStatus status) {
        Status = status;
    }
}
