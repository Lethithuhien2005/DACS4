package shared.DTO;

import java.io.Serializable;

public class RoomDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roomId;
    private String title;
    private String meeting_code;
    private String passcode;
    private String status;
    private String conservationId;
    private long created_at;

    public RoomDTO() {}

    public RoomDTO(String roomId, String title, String meeting_code, String passcode, String status, String conservationId, long created_at) {
        this.roomId = roomId;
        this.title = title;
        this.meeting_code = meeting_code;
        this.passcode = passcode;
        this.status = status;
        this.conservationId = conservationId;
        this.created_at = created_at;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMeeting_code() {
        return meeting_code;
    }

    public void setMeeting_code(String meeting_code) {
        this.meeting_code = meeting_code;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConservationId() {
        return conservationId;
    }

    public void setConservationId(String conservationId) {
        this.conservationId = conservationId;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}