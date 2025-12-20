package main.Server.DAO;

import com.mongodb.client.MongoCollection;
import main.util.MongoDBConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Date;

public class MeetingDAO {
    private MongoCollection<Document> conversations;
    private MongoCollection<Document> rooms;
    private MongoCollection<Document> member;

    public MeetingDAO() {
        conversations = MongoDBConnection.getDatabase().getCollection("conversations");
        rooms = MongoDBConnection.getDatabase().getCollection("rooms");
        member = MongoDBConnection.getDatabase().getCollection("member");
    }

    // Add a meeting to database
    public ObjectId createMeeting(ObjectId hostId, String title, String meeting_code, String passcode) {
        // Tao conversation
        Document conversationDoc = new Document("type", "meeting")
                .append("created_by", hostId)
                .append("created_at", new Date());
        conversations.insertOne(conversationDoc);
        ObjectId conservationId = conversationDoc.getObjectId("_id"); // Lay id tu database

        // Tao phong hop
        Document roomDoc = new Document("title", title)
                .append("meeting_code", meeting_code)
                .append("passcode", passcode)
                .append("status", "active")
                .append("created_at", new Date())
                .append("conversation_id", conservationId);
        rooms.insertOne(roomDoc);

        // Them HOST vao member
        Document memberDoc = new Document("conversation_id", conservationId)
                .append("user_id", hostId)
                .append("role", "host")
                .append("is_muted", false)
                .append("is_camera_on", true);
        member.insertOne(memberDoc);

        // Tra ve conversationID de phuc vu viec chat trong cuoc hop, hien thi danh sach nguoi tham gia ma khong can phai truy van conversationId tu bang rooms
        return conservationId;
    }

    // Lay phong hop thong qua conversationId
    public Document getRoomByConversationId(ObjectId conversationId) {
        return rooms.find(new Document("conversation_id", conversationId)).first();
    }

    // Kiem tra user da co trong bang member chua
    public boolean isMember(ObjectId conversationId, ObjectId userId) {
        Document query = new Document("conversationId", conversationId).append("user_id", userId);
        return member.find(query).first() != null;
    }

    // Them user vao bang member
    public void addMember(ObjectId conversationId, ObjectId userId) {
        Document memberDoc = new Document("conversationId", conversationId)
                .append("user_id", userId)
                .append("role", "member")
                .append("is_muted", false)
                .append("is_camera_on", true);
        member.insertOne(memberDoc);
    }
}
