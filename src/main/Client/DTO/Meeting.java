package main.Client.DTO; // DỮ LIỆU HIỂN THỊ CHO UI

public class Meeting {
    private String title;
    private String time;
    private String hostName;
    private String avatarPath;

    public Meeting(String title, String time, String hostName, String avatarPath) {
        this.title = title;
        this.time = time;
        this.hostName = hostName;
        this.avatarPath = avatarPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
