package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MeetingService extends Remote {
        public void createMeeting(String hostId, String title, String passcode, MeetingClientCallback callback) throws RemoteException;
        public void joinMeeting(String userId, String meetCode, String passcode, MeetingClientCallback callback) throws RemoteException;
        public void leaveMeeting(String userId, String meetingId) throws RemoteException;
}
