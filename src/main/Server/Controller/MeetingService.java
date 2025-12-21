//package main.Server.Controller;
//
//import java.rmi.Remote;
//import java.rmi.RemoteException;
//import java.io.Serializable;
//
//public interface MeetingService extends Remote {
//
//    /* ===== CALLBACK ===== */
//    interface ClientCallback extends Remote {
//        void onNewMessage(ChatMessage message) throws RemoteException;
//        void onUserJoined(String userName) throws RemoteException;
//        void onUserLeft(String userName) throws RemoteException;
//    }
//
//    /* ===== DTO ===== */
//    class ChatMessage implements Serializable {
//        private String room_id;
//        private String sender;
//        private String content;
//        private long timestamp;
//
//        public ChatMessage(String room_id, String sender, String content) {
//            this.room_id = room_id;
//            this.sender = sender;
//            this.content = content;
//            this.timestamp = System.currentTimeMillis();
//        }
//
//        public String getRoom_id() { return room_id; }
//        public String getSender() { return sender; }
//        public String getContent() { return content; }
//        public long getTimestamp() { return timestamp; }
//    }
//
//    /* ===== METHODS ===== */
//    void joinMeeting(
//            String room_id,
//            String userName,
//            ClientCallback callback
//    ) throws RemoteException;
//
//    void leaveMeeting(
//            String room_id,
//            String userName
//    ) throws RemoteException;
//
//    void sendMessage(
//            String room_id,
//            ChatMessage message
//    ) throws RemoteException;
//}
