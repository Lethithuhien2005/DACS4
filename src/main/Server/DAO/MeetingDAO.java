package main.Server.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import main.util.MongoDBConnection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeetingDAO {
    private MongoCollection<Document> conversations;
    private MongoCollection<Document> rooms;
    private MongoCollection<Document> meeting_participants;

    public MeetingDAO() {
        conversations = MongoDBConnection.getDatabase().getCollection("conversations");
        rooms = MongoDBConnection.getDatabase().getCollection("rooms");
        meeting_participants = MongoDBConnection.getDatabase().getCollection("meeting_participants");
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

        return conservationId;
    }

    // Lay phong hop thong qua meetingCode
    public Document getRoomByMeetingCode(String meetingCode) {
        return rooms.find(new Document("meeting_code", meetingCode)).first();
    }

    // Lay phong hop thong qua conversationId
    public Document getRoomByConversationId(ObjectId conversationId) {
        return rooms.find(new Document("conversation_id", conversationId)).first();
    }


    // Kiem tra user da co trong bang member chua
    public boolean isMember(ObjectId conversationId, ObjectId userId) {
        Document query = new Document("conversation_id", conversationId).append("user_id", userId);
        return meeting_participants.find(query).first() != null;
    }

    // Them user vao bang meeting_participants
    public void addParticipant(ObjectId roomId, ObjectId userId, String role) {
        Document memberDoc = new Document("room_id", roomId)
                .append("user_id", userId)
                .append("role", role)
                .append("joined_at", new Date())
                .append("left_at", null)
                .append("status", "joined")
                .append("is_muted", false)
                .append("is_camera_on", true);
        meeting_participants.insertOne(memberDoc);
    }

    public List<Document> getActiveParticipants(ObjectId roomId) {
        List<Document> participantList = new ArrayList<>();
        meeting_participants.find(new Document("room_id", roomId)
                        .append("status", "joined")
        ).forEach(participantList::add);

        return participantList;
    }

    public boolean hasEverJoined(ObjectId roomId, ObjectId userId) {
        Document query = new Document("room_id", roomId)
                .append("user_id", userId);

        return meeting_participants.find(query).first() != null;
    }


    public void rejoin(ObjectId roomId, ObjectId userId) {
        meeting_participants.updateOne(
                new Document("room_id", roomId)
                        .append("user_id", userId),
                new Document("$set",
                        new Document("status", "JOINED")
                                .append("left_at", null)
                                .append("joined_at", new Date()))
        );
    }

    public void updateStatusParticipant(ObjectId roomId, ObjectId userId) {
        meeting_participants.updateOne(
                Filters.and(
                        Filters.eq("room_id", roomId),
                        Filters.eq("user_id", userId),
                        Filters.eq("status", "active") // chỉ update nếu đang active
                ),
                Updates.combine(
                        Updates.set("status", "left"),
                        Updates.set("left_at", System.currentTimeMillis())
                )
        );
    }

}
