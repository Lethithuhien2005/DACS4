package main.Server.DAO;

import com.mongodb.client.MongoCollection;
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
    private MongoCollection<Document> messages;

    public MeetingDAO() {
        conversations = MongoDBConnection.getDatabase().getCollection("conversations");
        rooms = MongoDBConnection.getDatabase().getCollection("rooms");
        meeting_participants = MongoDBConnection.getDatabase().getCollection("meeting_participants");
        messages = MongoDBConnection.getDatabase().getCollection("messages");
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
    public void addParticipant(ObjectId conversationId, ObjectId userId, String role) {
        Document memberDoc = new Document("conversation_id", conversationId)
                .append("user_id", userId)
                .append("role", role)
                .append("joined_at", new Date())
                .append("left_at", null)
                .append("status", "joined")
                .append("is_muted", false)
                .append("is_camera_on", true);
        meeting_participants.insertOne(memberDoc);
    }

    public List<Document> getActiveParticipants(ObjectId conversationId) {
        List<Document> participantList = new ArrayList<>();
        meeting_participants.find(new Document("conversation_id", conversationId)
                        .append("status", "joined")
        ).forEach(participantList::add);

        return participantList;
    }

    public boolean hasEverJoined(ObjectId conversationId, ObjectId userId) {
        Document query = new Document("conversation_id", conversationId)
                .append("user_id", userId);

        return meeting_participants.find(query).first() != null;
    }


    public void rejoin(ObjectId conversationId, ObjectId userId) {
        meeting_participants.updateOne(
                new Document("conversation_id", conversationId)
                        .append("user_id", userId),
                new Document("$set",
                        new Document("status", "JOINED")
                                .append("left_at", null)
                                .append("joined_at", new Date()))
        );
    }

    public void saveMessage(
            ObjectId conversationId,
            String senderId,
            String content
    ) {
        Document doc = new Document()
                .append("conversation_id", conversationId)
                .append("sender_id", senderId)
                .append("content", content)
                .append("created_at", Date.from(Instant.now()));

        messages.insertOne(doc);
    }

}
