package main.Client.Controller;

import javafx.application.Platform;
import main.Client.View.Home;
import main.Client.View.meeting.MeetingUI;
import main.util.DialogUtil;
import main.util.Session;
import shared.MeetingClientCallback;
import shared.MeetingService;

import java.rmi.RemoteException;

public class MeetingController {
    private Home homeView;
    private MeetingUI meetingUI;
    private MeetingService meetingService;
    private MeetingClientCallback callback;


    public MeetingController(Home homeView, MeetingUI meetingUI, MeetingService meetingService) {
        this.homeView = homeView;
        this.meetingUI = meetingUI;
        this.meetingService = meetingService;
        try {
            this.callback = new MeetingClientCallbackImplement(homeView, meetingUI);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // Khi nguoi dung click vao button Create a new meeting
    public void onClickCreatMeeting() {
        String titleMeeting = homeView.titleTextField.getText();
        String passcode = homeView.titleTextField.getText();
        String userID = Session.getInstance().getUserIdHex();

        if (titleMeeting.isEmpty()) {
            Platform.runLater(() -> {
                DialogUtil.showError("Error", null, "Please fill in the title meeting.");
            });
            return;
        }
        // Kiem tra userID tranh TH Server fail
        if (userID == null ) {
            Platform.runLater(() -> {
                DialogUtil.showError("Error", null, "User not logged in.");
            });
            return;
        }

        new Thread(() -> {
            try {
                meetingService.createMeeting(userID, titleMeeting, passcode, callback);
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> DialogUtil.showError("Connect to server", null, "TCP not connected!"));
            }
        });
    }

    // Khi nguoi dung click vao button Join the meeting now hoac Join now
    public void onClickJoinTheMeeting() {
        String meetingCode = homeView.idTextField.getText();
        String passcode = homeView.passwordMeeting.getText();
        String userID = Session.getInstance().getUserIdHex();

        if (meetingCode.isEmpty()) {
            Platform.runLater(() -> {
                DialogUtil.showError("Error", null, "Please fill in the meeting ID");
            });
            return;
        }

        // Kiem tra userID tranh TH Server fail
        if (userID == null ) {
            Platform.runLater(() -> {
                DialogUtil.showError("Error", null, "User not logged in.");
            });
            return;
        }

        new Thread(() -> {
            try {
                    meetingService.joinMeeting(userID, meetingCode, passcode, callback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Platform.runLater(() -> DialogUtil.showError("RMI Error", null, "Cannot connect to server!"));
                }
        }).start();

    }

}
