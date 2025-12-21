package main.Server.Controller;

import main.Server.DAO.MeetingDAO;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import common.meeting.MeetingService;
import common.meeting.ChatMeeting;


public class MeetingServiceImplement extends UnicastRemoteObject implements MeetingService {

    private MeetingDAO meetingDAO;

    // meetingId -> list client callbacks
    private final Map<String, List<ClientCallback>> meetingClients = new ConcurrentHashMap<>();

    public MeetingServiceImplement() throws RemoteException {
        super();
        this.meetingDAO = new MeetingDAO();
    }

    @Override
    public synchronized void joinMeeting(
            String room_id,
            String userName,
            ClientCallback callback
    ) throws RemoteException {

        meetingClients
                .computeIfAbsent(room_id, k -> new ArrayList<>())
                .add(callback);

        // notify others
        for (ClientCallback cb : meetingClients.get(room_id)) {
            cb.onUserJoined(userName);
        }

        System.out.println(userName + " joined meeting " + room_id);
    }


    @Override
    public synchronized void leaveMeeting(
            String room_id,
            String userName,
            ClientCallback callback
    ) throws RemoteException {

        List<ClientCallback> clients = meetingClients.get(room_id);
        if (clients == null) return;

        // remove callback của client rời phòng
        clients.remove(callback);

        // notify những người còn lại
        for (ClientCallback cb : clients) {
            cb.onUserLeft(userName);
        }

        System.out.println(userName + " left meeting " + room_id);
    }



    @Override
    public synchronized void sendMessage( String room_id, ChatMeeting message) throws RemoteException {

        // 1️⃣ convert id
        //ObjectId conversationId = new ObjectId("507f1f77bcf86cd799439011"); // test
        ObjectId conversationId = new ObjectId(); // auto-generate // tạm thời
//        ObjectId senderId = new ObjectId(message.getSender());
//        ObjectId senderId = new ObjectId();

        String senderId = message.getSender(); // userIdHex từ Session

        // 2️⃣ LƯU DB QUA DAO
        meetingDAO.saveMessage(
                conversationId,
                senderId,
                message.getContent()
        );

        // 3️⃣ PUSH REALTIME
        List<ClientCallback> clients = meetingClients.get(room_id);
        if (clients == null) return;

        // SERVER PUSH MESSAGE VỀ CLIENT
//        for (ClientCallback cb : clients) {
//            cb.onNewMessage(message);
//        }
        Iterator<ClientCallback> it = clients.iterator();

        while (it.hasNext()) {
            ClientCallback cb = it.next();
            try {
                cb.onNewMessage(message);
            } catch (RemoteException e) {
                System.out.println("⚠️ Remove dead callback");
                it.remove(); // client đã disconnect
            }
        }


        System.out.println(
                "[SERVER] Message saved to DB | sender=" + senderId +
                        " | content=" + message.getContent()
        );

//        System.out.println(
//                "[" + room_id + "] "
//                        + message.getSender() + ": "
//                        + message.getContent()
//        );


//        for (ClientCallback cb : meetingClients.get(room_id)) {
//            cb.onNewMessage(message);
//        }
    }
}
