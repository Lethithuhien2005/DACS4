package main.Server.Controller; // NHẬN REQUEST TỪ CLIENT + XỬ LÝ NGHIỆP VỤ

import main.Server.DAO.MeetingDAO;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class MeetingHandler {
    private MeetingDAO meetingDAO;

    public MeetingHandler() {
        meetingDAO = new MeetingDAO();
    }
    // Xu ly yeu cau CREATE MEETING
    public Document handleCreateMeeting(Document request) {
        String type = request.getString("type");
        if ("CREATE_MEETING".equals(type)) {
            String title = request.getString("title");
            String passcode = request.getString("passcode");
            String userID = request.getString("userID");
            if (userID == null) {
                return new Document("type", "CREATE_MEETING_FAIL")
                        .append("message", "User not authenticated");
            }
            if (title == null || passcode == null) {
                return new Document("type", "CREATE_MEETING_FAIL")
                        .append("message", "MeetingHandler cannot handle request: "+ type + " because of missing required fields");
            }

            // Tao ma phong hop random
            ObjectId hostId = new ObjectId(userID);
            String meeting_code = String.valueOf(100000 + new java.security.SecureRandom().nextInt(900000));
            ObjectId conversationId = meetingDAO.createMeeting(hostId, title, meeting_code, passcode);

            // Lay thoi gian tao phong hop
            Document roomDoc = meetingDAO.getRoomByConversationId(conversationId);
            Date timeCreateRoom = roomDoc.getDate("created_at");

            // Tra response cho client
            return new Document("type", "CREATE_MEETING_OK")
                    .append("conversationId", conversationId.toHexString())
                    .append("meeting_code", meeting_code)
                    .append("title", title)
                    .append("time_create_meeting", timeCreateRoom.getTime());
        }
        else {
            return new Document("type", "CREATE_MEETING_FAIL").append("message", "MeetingHandler cannot handle request: " + type);
        }
    }

    // Xu ly yeu cau JOIN MEETING
    public Document handleJoinMeeting(Document request) {
        String type = request.getString("type");
        if ("JOIN_MEETING".equals(type)) {
            String meetingId = request.getString("conservationId");
            String meeting_code = request.getString("meeting_code");
            String userIdStr = request.getString("userId");
            if (userIdStr == null) {
                return new Document("type", "JOIN_MEETING_FAIL")
                        .append("message", "User not authenticated");
            }
            if (meetingId == null) {
                return new Document("type", "JOIN_MEETING_FAIL")
                        .append("message", "MeetingHandler cannot handle request: "+ type + " because of missing required fields");
            }

            ObjectId conversationId = new ObjectId(meetingId);
            ObjectId userID = new ObjectId(userIdStr);

            // Tim phong hop
            Document room = meetingDAO.getRoomByConversationId(conversationId);
            if (room == null) {
                return new Document("type", "JOIN_MEETING_FAIL")
                        .append("message", "Meeting room not found");
            }

            // Kiem tra passcode cua phong hop (null hoac co passcode)
            if (!meeting_code.equals(room.getString("meeting_code"))) {
                return new Document("type", "JOIN_MEETING_FAIL")
                        .append("message", "Invalid meeting code");
            }

            // Kiem tra trang thai phong
            if (!"active".equals(room.getString("status"))) {
                return new Document("type", "JOIN_MEETING_FAIL")
                        .append("message", "Meeting is not active");
            }

            // Neu chua la member trong phong hop thi them vao
            if (!meetingDAO.isMember(conversationId, userID)) {
                meetingDAO.addMember(conversationId, userID);
            }

            // Lay danh sach nguoi dang tham gia cuoc hop


            return new Document("type", "JOIN_MEETING_OK")
                    .append("conversationId", conversationId)
                    .append("meeting_code", meeting_code)
                    .append("title", room.getString("title"));

        }
        else {
            return new Document("type", "JOIN_MEETING_FAIL").append("message", "MeetingHandler cannot handle request: " + type);
        }
    }

}
