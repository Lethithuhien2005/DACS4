package main.Server.Controller;

import main.Server.DAO.MeetingDAO;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//import common.meeting.MeetingService;
//import common.meeting.ChatMeeting;


import shared.ChatService;
import shared.DTO.Meeting_participantDTO;
import shared.DTO.RoomDTO;
import shared.MeetingClientCallback;
import main.Server.DAO.MeetingDAO;
import main.Server.DAO.UserDAO;
import org.bson.Document;
import org.bson.types.ObjectId;
import shared.MeetingService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


public class MeetingServiceImplement extends UnicastRemoteObject implements MeetingService {

    private MeetingDAO meetingDAO;
    private UserDAO userDAO;


    // meetingId -> list client callbacks
//    private final Map<String, List<ChatService.ClientCallback>> meetingClients = new ConcurrentHashMap<>();

    //    public MeetingServiceImplement() throws RemoteException {
//        super();
//        this.meetingDAO = new MeetingDAO();
//    }
    public MeetingServiceImplement() throws RemoteException {
        super();
        meetingDAO = new MeetingDAO();
        userDAO = new UserDAO();
    }

    //    @Override
//    public synchronized void joinMeeting(
//            String room_id,
//            String userName,
//            ClientCallback callback
//    ) throws RemoteException {
//
//        meetingClients
//                .computeIfAbsent(room_id, k -> new ArrayList<>())
//                .add(callback);
//
//        // notify others
//        for (ClientCallback cb : meetingClients.get(room_id)) {
//            cb.onUserJoined(userName);
//        }
//
//        System.out.println(userName + " joined meeting " + room_id);
//    }
    @Override
    public void createMeeting(String hostId, String title, String passcode, MeetingClientCallback callback) throws RemoteException {
        if (hostId == null) {
            callback.onCreateMeetingFail("User not loggin.");
        }
        if (title == null) {
            callback.onCreateMeetingFail("Meeting title is required");
            return;
        }

        ObjectId hostObjectId = new ObjectId(hostId);
        // Tao ma phong hop random
        String meetingCode = String.valueOf(100000 + new SecureRandom().nextInt(900000));
        ObjectId conversationId = meetingDAO.createMeeting(hostObjectId, title, meetingCode, passcode);

        // Lay phong hop thong qua conversationId
        Document roomDoc = meetingDAO.getRoomByConversationId(conversationId);

        // Tra ket qua cho client
        // CONVERT ObjectId → String
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setTitle(roomDoc.getString("title"));
        roomDTO.setMeeting_code(roomDoc.getString("meeting_code"));
        roomDTO.setPasscode(roomDoc.getString("passcode"));
        roomDTO.setStatus(roomDoc.getString("status"));
        roomDTO.setConservationId(conversationId.toHexString());
        roomDTO.setCreated_at(roomDoc.getLong("created_at"));

        callback.onCreateMeetingSuccess(roomDTO.getMeeting_code(), roomDTO.getPasscode(), roomDTO.getTitle(), roomDTO.getCreated_at());
    }

    @Override
    public void joinMeeting(String userId, String meetCode, String passcode, MeetingClientCallback callback) throws RemoteException {
        // Tim phong hop thong qua meetingCode
        Document room = meetingDAO.getRoomByMeetingCode(meetCode);
        if (room == null) {
            callback.onJoinMeetingFail("Meeting room not found");
        }

        // Kiem tra passcode
        String roomPasscode = room.getString("passcode");
        // Neu phong co passcode
        if (roomPasscode != null && !roomPasscode.isEmpty()) {
            if (!roomPasscode.equals(passcode)) { // client nhập không đúng
                callback.onJoinMeetingFail("Invalid passcode");
                return;
            }
        }

        // Kiểm tra trạng thái phòng
        if (!"active".equals(room.getString("status"))) {
            callback.onJoinMeetingFail("Meeting is not active");
            return;
        }

        ObjectId roomID = room.getObjectId("_id");
        ObjectId userID = new ObjectId(userId);

        // Neu chua la member trong phong hop thi them vao
        String role = "member";
        ObjectId hostId = room.getObjectId("created_by");
        if (hostId != null && hostId.equals(userID)) {
            role = "host";
        }
        if (meetingDAO.hasEverJoined(roomID, userID)) {
            meetingDAO.rejoin(roomID, userID);
        } else {
            meetingDAO.addParticipant(roomID, userID, role);
        }

        // Lấy danh sách participant dựa trên roomId
        List<Document> participants = meetingDAO.getActiveParticipants(roomID);
        List<Meeting_participantDTO> participantList = new ArrayList<>();
        for (Document p : participants) {
            ObjectId user_id = p.getObjectId("user_id");
            Document userDoc = userDAO.getUserById(user_id);
            Meeting_participantDTO dto = new Meeting_participantDTO(
                    user_id.toHexString(),
                    userDoc != null ? userDoc.getString("username") : "Unknown",
                    userDoc != null ? userDoc.getString("fullName") : "Unknown",
                    userDoc != null ? userDoc.getString("avatar") : "default_avatar",
                    p.getString("role"),
                    Boolean.TRUE.equals(p.getBoolean("is_muted")),
                    Boolean.TRUE.equals(p.getBoolean("is_camera_on"))
            );
            participantList.add(dto);
        }
        callback.onJoinMeetingSuccess(participantList);
    }

    @Override
    public synchronized void leaveMeeting(
            String room_id,
            String userName
    ) throws RemoteException {

//        List<ClientCallback> clients = meetingClients.get(room_id);
//        if (clients == null) return;
//
//        // remove callback của client rời phòng
//        clients.remove(callback);
//
//        // notify những người còn lại
//        for (ClientCallback cb : clients) {
//            cb.onUserLeft(userName);
//        }
//
//        System.out.println(userName + " left meeting " + room_id);
    }



}
//public class MeetingServiceImplement extends UnicastRemoteObject implements MeetingService {
//    private MeetingDAO meetingDAO;
//    private UserDAO userDAO;
//
//    public MeetingServiceImplement() throws RemoteException {
//        super();
//        meetingDAO = new MeetingDAO();
//        userDAO = new UserDAO();
//    }
//
//
//    @Override
//    public void createMeeting(String hostId, String title, String passcode, MeetingClientCallback callback) throws RemoteException {
//        if (hostId == null) {
//            callback.onCreateMeetingFail("User not loggin.");
//        }
//        if (title == null) {
//            callback.onCreateMeetingFail("Meeting title is required");
//            return;
//        }
//
//        ObjectId hostObjectId = new ObjectId(hostId);
//        // Tao ma phong hop random
//        String meetingCode = String.valueOf(100000 + new SecureRandom().nextInt(900000));
//        ObjectId conversationId = meetingDAO.createMeeting(hostObjectId, title, meetingCode, passcode);
//
//        // Lay phong hop thong qua conversationId
//        Document roomDoc = meetingDAO.getRoomByConversationId(conversationId);
//
//        // Tra ket qua cho client
//        // CONVERT ObjectId → String
//        RoomDTO roomDTO = new RoomDTO();
//        roomDTO.setTitle(roomDoc.getString("title"));
//        roomDTO.setMeeting_code(roomDoc.getString("meeting_code"));
//        roomDTO.setPasscode(roomDoc.getString("passcode"));
//        roomDTO.setStatus(roomDoc.getString("status"));
//        roomDTO.setConservationId(conversationId.toHexString());
//        roomDTO.setCreated_at(roomDoc.getLong("created_at"));
//
//        callback.onCreateMeetingSuccess(roomDTO.getMeeting_code(), roomDTO.getPasscode(), roomDTO.getTitle(), roomDTO.getCreated_at());
//    }
//
//    @Override
//    public void joinMeeting(String userId, String meetCode, String passcode, MeetingClientCallback callback) throws RemoteException {
//        // Tim phong hop thong qua meetingCode
//        Document room = meetingDAO.getRoomByMeetingCode(meetCode);
//        if (room == null) {
//            callback.onJoinMeetingFail("Meeting room not found");
//        }
//
//        // Kiem tra passcode
//        String roomPasscode = room.getString("passcode");
//        // Neu phong co passcode
//        if (roomPasscode != null && !roomPasscode.isEmpty()) {
//            if (!roomPasscode.equals(passcode)) { // client nhập không đúng
//                callback.onJoinMeetingFail("Invalid passcode");
//                return;
//            }
//        }
//
//        // Kiểm tra trạng thái phòng
//        if (!"active".equals(room.getString("status"))) {
//            callback.onJoinMeetingFail("Meeting is not active");
//            return;
//        }
//
//        ObjectId roomID = room.getObjectId("_id");
//        ObjectId userID = new ObjectId(userId);
//
//        // Neu chua la member trong phong hop thi them vao
//        String role = "member";
//        ObjectId hostId = room.getObjectId("created_by");
//        if (hostId != null && hostId.equals(userID)) {
//            role = "host";
//        }
//        if (meetingDAO.hasEverJoined(roomID, userID)) {
//            meetingDAO.rejoin(roomID, userID);
//        } else {
//            meetingDAO.addParticipant(roomID, userID, role);
//        }
//
//        // Lấy danh sách participant dựa trên roomId
//        List<Document> participants = meetingDAO.getActiveParticipants(roomID);
//        List<Meeting_participantDTO> participantList = new ArrayList<>();
//        for (Document p : participants) {
//            ObjectId user_id = p.getObjectId("user_id");
//            Document userDoc = userDAO.getUserById(user_id);
//            Meeting_participantDTO dto = new Meeting_participantDTO(
//                    user_id.toHexString(),
//                    userDoc != null ? userDoc.getString("username") : "Unknown",
//                    userDoc != null ? userDoc.getString("fullName") : "Unknown",
//                    userDoc != null ? userDoc.getString("avatar") : "default_avatar",
//                    p.getString("role"),
//                    Boolean.TRUE.equals(p.getBoolean("is_muted")),
//                    Boolean.TRUE.equals(p.getBoolean("is_camera_on"))
//            );
//            participantList.add(dto);
//        }
//        callback.onJoinMeetingSuccess(participantList);
//    }
//
//    @Override
//    public synchronized void leaveMeeting(String userId, String meetingId) throws RemoteException {
//
//    }
//}
