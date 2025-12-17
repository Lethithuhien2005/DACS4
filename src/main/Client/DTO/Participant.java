package main.Client.DTO; // DỮ LIỆU HIỂN THỊ CHO UI

public class Participant {
    private int userId;
    private String fullname;
    private String avatar;
    private String role;
    private boolean micOn;
    private boolean cameraOn;

    public Participant() {}

    public Participant(String fullname, String avatar, String role, boolean micOn, boolean cameraOn) {
        this.fullname = fullname;
        this.avatar = avatar;
        this.role = role; //admin - host - participant
        this.micOn = micOn;
        this.cameraOn = cameraOn;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isMicOn() {
        return micOn;
    }

    public void setMicOn(boolean micOn) {
        this.micOn = micOn;
    }

    public boolean isCameraOn() {
        return cameraOn;
    }

    public void setCameraOn(boolean cameraOn) {
        this.cameraOn = cameraOn;
    }
}
