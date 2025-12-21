package main.Client.Controller;

import javafx.application.Platform;
import main.Client.DTO.Participant;
import main.Client.Network.TCP.SocketClient;
import main.Client.View.Home;
import main.Client.View.meeting.MeetingUI;
import main.Client.View.meeting.VideoTile;
import main.util.DialogUtil;
import main.util.Session;
import org.bson.Document;

import java.util.Date;
import java.util.List;

public class MeetingController {
    private Home homeView;
    private MeetingUI meetingUI;

    public MeetingController(Home homeView, MeetingUI meetingUI) {
        this.homeView = homeView;
        this.meetingUI = meetingUI;
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
                    String conversationId = response.getString("conversationId");
                    String meetingCode = response.getString("meeting_code");
                    String title = response.getString("title");
                    List<Document> participantList = (List<Document>) response.get("participantList");

                    // Hien thi giao dien cuoc goi
                    Platform.runLater(() -> {
                        List<VideoTile> tiles = meetingUI.getTiles();
                        tiles.clear(); // Xoa danh sach cu
                        meetingUI.getParticipantsList().clear();

                        List<Document> participants = (List<Document>) response.get("participantList");
                        for (Document p : participants) {
                            String username = p.getString("username");
                            String fullName = p.getString("fullName");
                            String avatar = p.getString("avatar");
                            String role = p.getString("role");
                            boolean isMicOn = !p.getBoolean("is_muted", false);
                            boolean isCameraOn = p.getBoolean("is_camera_on", true);

                            VideoTile tile = new VideoTile(username); // hiển thị tên
                            tile.setCameraOn(isCameraOn);
                            tile.setAvatar(avatar); // load ảnh avatar từ backend
                            tiles.add(tile);

                            // them nguoi dung vao danh sach ben UI
                            Participant participant = new Participant(fullName, avatar, role, isMicOn, isCameraOn);
                            meetingUI.getParticipantsList().add(participant);
                        }

                        meetingUI.getVideoCallPane().updateLayout(tiles); // cập nhật giao diện video
                    });
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
