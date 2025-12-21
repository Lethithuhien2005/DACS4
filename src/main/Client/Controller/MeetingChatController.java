package main.Client.Controller;

import javafx.application.Platform;
import main.Client.ClientMain;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import main.util.Session;

import common.meeting.MeetingService;
import common.meeting.ChatMeeting;


public class MeetingChatController {

    private MeetingService meetingService;
    private String room_id ;
    private String userName;

    private MeetingService.ClientCallback callback;

    public interface UiListener {
        void onMessageReceived(ChatMeeting msg);
        void onSystemMessage(String text);
    }

    private UiListener uiListener;

    public MeetingChatController(String room_id, String userName) {
        this.room_id = room_id;
        this.userName = userName;
    }

    public void setUiListener(UiListener listener) {
        this.uiListener = listener;
    }

    /* ===== CONNECT RMI ===== */
    public void connect() throws Exception {
        meetingService = ClientMain.meetingService;

//        callback = new ClientCallbackImpl();
//        meetingService.joinMeeting(room_id, userName, callback);

        if (meetingService == null) {
            throw new IllegalStateException("RMI MeetingService not connected");
        }

        // callback client
//        MeetingService.ClientCallback callback =
//                new ClientCallbackImpl();
        callback = new ClientCallbackImpl();
        meetingService.joinMeeting(room_id, userName, callback);
    }

    /* ===== SEND MESSAGE ===== */
    public void sendMessage(String text) throws RemoteException {
        if (text == null || text.trim().isEmpty()) return;

        String senderId = Session.getInstance().getUserIdHex();

        ChatMeeting  msg =
                new ChatMeeting (
                        room_id,
                        senderId,
                        text
                );

        meetingService.sendMessage(room_id, msg);

        System.out.println(
                "[CLIENT] Message sent successfully by userId=" + senderId
                        + ", content=" + text
        );
    }

    /* ===== CALLBACK IMPLEMENT ===== */
    private class ClientCallbackImpl
            extends UnicastRemoteObject
            implements MeetingService.ClientCallback {

        protected ClientCallbackImpl() throws RemoteException {
            super();
        }

        @Override
        public void onNewMessage(ChatMeeting message) {
            Platform.runLater(() -> {
                if (uiListener != null) {
                    uiListener.onMessageReceived(message);
                }
            });
        }

        @Override
        public void onUserJoined(String name) {
            Platform.runLater(() -> {
                if (uiListener != null) {
                    uiListener.onSystemMessage(name + " joined the meeting");
                }
            });
        }

        @Override
        public void onUserLeft(String name) {
            Platform.runLater(() -> {
                if (uiListener != null) {
                    uiListener.onSystemMessage(name + " left the meeting");
                }
            });
        }
    }
}
