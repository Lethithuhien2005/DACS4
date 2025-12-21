package shared;

//import common.meeting.ChatMeeting;

import java.rmi.Remote;
import java.rmi.RemoteException;


import shared.MeetingClientCallback;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MeetingService extends Remote {
        public void createMeeting(String hostId, String title, String passcode, MeetingClientCallback callback) throws RemoteException;
        public void joinMeeting(String userId, String meetCode, String passcode, MeetingClientCallback callback) throws RemoteException;
//        public void leaveMeeting(String userId, String meetingId) throws RemoteException;
//        public void leaveMeeting(
//                    String roomId,
//                    String userName,
//                    ChatService.ClientCallback callback
//            ) throws RemoteException;
        void leaveMeeting(
                String userId,
                String meetingId
        ) throws RemoteException;
}

//package common.meeting;

//
//public interface MeetingService extends Remote {
//
//    interface ClientCallback extends Remote {
//        void onNewMessage(ChatMeeting message) throws RemoteException;
//        void onUserJoined(String userName) throws RemoteException;
//        void onUserLeft(String userName) throws RemoteException;
//    }
//
//    void createMeeting(String hostId, String title, String passcode, MeetingClientCallback callback) throws RemoteException;
//    void joinMeeting(String userId, String meetCode, String passcode, MeetingClientCallback callback) throws RemoteException;
//
////    void joinMeeting(
////            String roomId,
////            String userName,
////            common.meeting.MeetingService.ClientCallback callback
////    ) throws RemoteException;
//
//    void leaveMeeting(
//            String roomId,
//            String userName,
//            common.meeting.MeetingService.ClientCallback callback
//    ) throws RemoteException;
//
//    void sendMessage(
//            String roomId,
//            ChatMeeting message
//    ) throws RemoteException;
//}

