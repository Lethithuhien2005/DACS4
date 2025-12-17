package main.Client.DTO; // DỮ LIỆU HIỂN THỊ CHO UI

public class Message {
    private int messageId;
    private String content;
    private int senderId;
    private String senderName;
    private long timestamp;

    public Message() {}

    public Message(String content, int senderId, String senderName, long timestamp) {
        this.content = content;
        this.senderId = senderId;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
