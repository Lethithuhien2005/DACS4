package shared;

import shared.DTO.Meeting_participantDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MeetingClientCallback extends Remote {
    // create meeting
    void onCreateMeetingSuccess(String meetingCode, String passcode, String title, long timeCreate) throws RemoteException;
    void onCreateMeetingFail(String message) throws RemoteException;

    void onJoinMeetingSuccess(List<Meeting_participantDTO> participantList) throws RemoteException;
    void onJoinMeetingFail(String reason) throws RemoteException;

    // update participants list
    public void onParticipantListUpdated(List<Meeting_participantDTO> participants) throws RemoteException;
}
