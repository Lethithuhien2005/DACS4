package main.Client.Controller;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import main.Client.View.SidebarController;
import main.Client.View.meeting.VideoTile;
import main.util.Session;
import shared.DTO.Meeting_participantDTO;
import main.Client.View.Home;
import main.Client.View.meeting.MeetingUI;
import main.util.DialogUtil;
import shared.MeetingClientCallback;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeetingClientCallbackImplement extends UnicastRemoteObject implements MeetingClientCallback {
    private Home homeView;
    private MeetingUI meetingUI;
    private SidebarController sidebarController;

    public MeetingClientCallbackImplement(Home homeView, MeetingUI meetingUI, SidebarController sidebarController) throws RemoteException {
        super();
        this.homeView = homeView;
        this.meetingUI = meetingUI;
        this.sidebarController = sidebarController;
    }

    @Override
    public void onCreateMeetingSuccess(String meetingCode, String passcode, String title, long timeCreate) throws RemoteException {
        Platform.runLater(() -> {
            homeView.addMeetingToday(title, meetingCode, passcode,timeCreate);
        });
    }

    @Override
    public void onCreateMeetingFail(String message) throws RemoteException {
        Platform.runLater(() -> {
            DialogUtil.showError("RMI callback", null, message);
        });
    }

    @Override
    public void onJoinMeetingSuccess(String roomId, List<Meeting_participantDTO> participantList) throws RemoteException {
        Platform.runLater(() -> {
            // Lay id phong de setMic, setCam, kickUser
            meetingUI.setRoomId(roomId);
            // Hien thi trang Meeting
            StackPane contentPane = meetingUI.getContentPane();
            contentPane.getChildren().setAll(meetingUI);
            // Sidebar hien thi item Meeting duoc chon
            sidebarController.selectMeetingItem();
            // clear UI cũ
            List<VideoTile> tiles = meetingUI.getTiles();
            tiles.clear();
            meetingUI.getParticipantsList().clear();

            for (Meeting_participantDTO p : participantList) {
                String userId = p.getUserId();
                String username = p.getUsername();
                String fullName = p.getFullName();
                String avatar = p.getAvatar();
                String role = p.getRole();
                boolean isMicOn = !p.isMuted();
                boolean isCameraOn = p.isCameraOn();

                // Video tile
                VideoTile tile = new VideoTile(userId, username);
                tile.setCameraOn(isCameraOn);
                tile.setAvatar(avatar);
                tiles.add(tile);

                meetingUI.getParticipantsList().add(p);

                // Set current user
                if (p.getUserId().equals(Session.getInstance().getUserIdHex())) {
                    meetingUI.setCurrentUser(p);
                }
            }

            // update layout video
            meetingUI.getVideoCallPane().updateLayout(tiles);
        });
    }

    @Override
    public void onJoinMeetingFail(String reason) throws RemoteException {
        Platform.runLater(() -> {
            DialogUtil.showError("RMI callback", null, reason);
        });
    }

    // Update giao dien danh sach nguoi tham gia cuoc hop khi co 1 client tham gia
    @Override
    public void onParticipantListUpdated(List<Meeting_participantDTO> updateList) throws RemoteException {
        Platform.runLater(() -> {
            // Cap nhat danh sach nguoi tham gia
            meetingUI.getParticipantsList().setAll(updateList);

            // Lưu trạng thái camera/mic hiện tại
            Map<String, Boolean> cameraStateMap = new HashMap<>();
            for (VideoTile tile : meetingUI.getTiles()) {
                cameraStateMap.put(tile.getUserId(), tile.isCameraOn());
            }


            // Cập nhật currentUser nếu có
            for (Meeting_participantDTO p : updateList) {
                if (p.getUserId().equals(Session.getInstance().getUserIdHex())) {
                    meetingUI.setCurrentUser(p);
                    break;
                }
            }

            // Cập nhật video tiles
            List<VideoTile> tiles = meetingUI.getTiles();
            tiles.clear();
            for (Meeting_participantDTO p : updateList) {
                VideoTile tile = new VideoTile(p.getUserId(), p.getUsername());
                tile.setCameraOn(p.isCameraOn());
                tile.setAvatar(p.getAvatar());

                // Nếu có trạng thái camera local, ưu tiên dùng
                if (cameraStateMap.containsKey(p.getUserId())) {
                    tile.setCameraOn(cameraStateMap.get(p.getUserId()));
                } else {
                    tile.setCameraOn(p.isCameraOn());
                }

                tiles.add(tile);
            }

            meetingUI.getVideoCallPane().updateLayout(tiles);
        });
    }

}