package main.Server.Model; // ENTITIES ÁNH XẠ CSDL

public class Member {
    private int member_id;
    private int conversation_id;
    private String role;
    private boolean is_muted;
    private boolean is_camera_on;

    public Member() {}

    public Member(int member_id, int conversation_id, String role, boolean is_muted, boolean is_camera_on) {
        this.member_id = member_id;
        this.conversation_id = conversation_id;
        this.role = role;
        this.is_muted = is_muted;
        this.is_camera_on = is_camera_on;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isIs_muted() {
        return is_muted;
    }

    public void setIs_muted(boolean is_muted) {
        this.is_muted = is_muted;
    }

    public boolean isIs_camera_on() {
        return is_camera_on;
    }

    public void setIs_camera_on(boolean is_camera_on) {
        this.is_camera_on = is_camera_on;
    }
}
