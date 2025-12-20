package main.Client.Controller;

import javafx.application.Platform;
import main.Client.Network.TCP.SocketClient;
import main.Client.View.Home;
import main.util.DialogUtil;
import main.util.Session;
import org.bson.Document;

import java.util.Date;

public class MeetingController {
    private Home homeView;

    public MeetingController(Home homeView) {
        this.homeView = homeView;
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
                SocketClient socketClient = SocketClient.getInstance();
                if (!socketClient.isConnected()) {
                    Platform.runLater(() -> DialogUtil.showError("Connect to server", null, "TCP not connected!"));
                }

                // Gui yeu cau tao cuoc hop
                Document createMeetingRequest = new Document("type", "CREATE_MEETING").append("title", titleMeeting).append("passcode", passcode).append("userID", userID);
                socketClient.send(createMeetingRequest);

                // Cho ket qua tu server
                Document response = Document.parse(socketClient.getReader().readLine());
                String type = response.getString("type");

                // Neu tao cuoc hop thanh cong
                if ("CREATE_MEETING_OK".equals(type)) {
                    // Lay thong tin tu database cua server
                    String conversationId = response.getString("conversationId");
                    String meetingCode = response.getString("meeting_code");
                    String title = response.getString("title");
                    long timeCreateMeeting = response.getLong("time_create_meeting");

                    // Cap nhat giao dien danh sach Meeting today trong Home
                    Platform.runLater(() -> {
                        homeView.addMeetingToday(title, timeCreateMeeting);
                    });
                }
                else if ("CREATE_MEETING_FAIL".equals(type)) {
                    String message = response.getString("message");
                    Platform.runLater(() -> DialogUtil.showError("Create meeting error", null, message));
                }

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> DialogUtil.showError("Connect to server", null, "TCP not connected!"));
            }
        });
    }

    // Khi nguoi dung click vao button Join the meeting now hoac Join now
    public void onClickJoinTheMeeting() {
        String meetingID = homeView.idTextField.getText(); // conversationId
        String meet_code = homeView.passwordMeeting.getText();
        String userID = Session.getInstance().getUserIdHex();

        if (meetingID.isEmpty()) {
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
                SocketClient socketClient = SocketClient.getInstance();
                if (!socketClient.isConnected()) {
                    Platform.runLater(() -> DialogUtil.showError("Connect to server", null, "TCP not connected!"));
                }

                // Gui yeu cau tham gia cuoc hop
                Document joinMeetingRequest = new Document("type", "JOIN_MEETING").append("conversationId", meetingID).append("meeting_code", meet_code).append("userId", userID);
                socketClient.send(joinMeetingRequest);

                // Cho server tra ket qua (1 dong)
                Document response = Document.parse(socketClient.getReader().readLine());

                String type = response.getString("type");

                // Neu join thanh cong
                if ("JOIN_MEETING_OK".equals(type)) {
                    // Hien thi giao dien cuoc goi



                }
                else if ("JOIN_MEETING_FAIL".equals(type)) {
                    String message = response.getString("message");
                    Platform.runLater(()-> {
                        DialogUtil.showError("Join meeting error", null, message);
                    });
                }

            } catch (Exception e) {
                Platform.runLater(() -> {
                    DialogUtil.showError("ConnConnect to server", null, "TCP cannot connect to server");
                });
            }
        });

    }

}
