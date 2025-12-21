package common.meeting;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MeetingService extends Remote {

    interface ClientCallback extends Remote {
        void onNewMessage(ChatMeeting message) throws RemoteException;
        void onUserJoined(String userName) throws RemoteException;
        void onUserLeft(String userName) throws RemoteException;
    }

    void joinMeeting(
            String roomId,
            String userName,
            ClientCallback callback
    ) throws RemoteException;

    void leaveMeeting(
            String roomId,
            String userName,
            ClientCallback callback
    ) throws RemoteException;

    void sendMessage(
            String roomId,
            ChatMeeting message
    ) throws RemoteException;
}
