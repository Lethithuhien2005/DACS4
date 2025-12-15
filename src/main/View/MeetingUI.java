package main.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MeetingUI extends StackPane {
    private HBox rootLayout;            // HBox chia 7:3
    private VBox videoContainer;   // vùng video
    private VBox chatContainer;         // vùng chat
    private List<VideoTile> tiles;
    private VideoCallPane videoCallPane;

    public MeetingUI() {
        rootLayout = new HBox();
        rootLayout.setSpacing(10);   // nếu bạn muốn khoảng giữa 2 panel
        rootLayout.setPadding(Insets.EMPTY); // xóa viền trắng

        // Video zone (7 phần)
        videoContainer = new VBox(10);
        videoContainer.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-radius: 15;");
        videoContainer.setPadding(new Insets(10));
        HBox.setHgrow(videoContainer, Priority.ALWAYS);  // toan bo videoContainer chiem toan bo phan ben trai theo chieu ngang

        videoCallPane = new VideoCallPane();
        tiles = new ArrayList<>();

        // ÉP videoCallPane fill 100% bên trong videoContainer
        videoCallPane.prefWidthProperty().bind(videoContainer.widthProperty());
        videoCallPane.prefHeightProperty().bind(videoContainer.heightProperty());
        videoCallPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // Test participants
        tiles.add(new VideoTile("Alice"));
        tiles.add(new VideoTile("Bob"));
        tiles.add(new VideoTile("Charlie"));
        tiles.add(new VideoTile("Charlie"));
        tiles.add(new VideoTile("Charlie"));

        videoCallPane.updateLayout(tiles);

        // Cac nut dieu khien
        HBox controlBar = new HBox(20);
        controlBar.setPadding(new Insets(10));
        controlBar.setAlignment(Pos.CENTER);
        controlBar.setStyle("-fx-background-color: #fff;");

        VBox micBox = styleControlBox("/images/mic_off.png", "Microphone");
        VBox cameraBox = styleControlBox("/images/camera_off.png", "Camera");
        VBox raiseBox = styleControlBox("/images/raise_off.png", "Raise");

        VBox reactBox = new VBox(5);
        reactBox.setAlignment(Pos.CENTER);
        reactBox.setPadding(new Insets(5));
        reactBox.setStyle("-fx-background-color: #fff;");
        ImageView reactImage = new ImageView(new Image(getClass().getResource("/images/like.png").toExternalForm()));
        reactImage.setFitHeight(28);
        reactImage.setFitWidth(28);
        Button reactBtn = new Button();
        reactBtn.setGraphic(reactImage);
        reactBtn.setStyle("-fx-background-color: #F6EBFF; -fx-border-radius: 15; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.2, 0, 1);");
        reactBtn.setPrefSize(50, 50);
        reactBtn.setMinSize(50, 50);
        reactBtn.setMaxSize(50, 50);
        Label reactLabel = new Label("Reaction");
        reactLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 13));
        reactLabel.setStyle("-fx-text-fill: #000");
        reactBox.getChildren().addAll(reactBtn, reactLabel);

        VBox leaveBox = new VBox(5);
        leaveBox.setAlignment(Pos.CENTER);
        leaveBox.setPadding(new Insets(5));
        leaveBox.setStyle("-fx-background-color: #fff;");
        ImageView leaveImage = new ImageView(new Image(getClass().getResource("/images/leave.png").toExternalForm()));
        leaveImage.setFitHeight(28);
        leaveImage.setFitWidth(28);
        Button leaveBtn = new Button();
        leaveBtn.setGraphic(leaveImage);
        leaveBtn.setStyle("-fx-background-color: #ef233c; -fx-border-radius: 15; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.2, 0, 1);");
        leaveBtn.setPrefSize(50, 50);
        leaveBtn.setMinSize(50, 50);
        leaveBtn.setMaxSize(50, 50);
        Label leaveLabel = new Label("Leave");
        leaveLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 13));
        leaveLabel.setStyle("-fx-text-fill: #000");
        leaveBox.getChildren().addAll(leaveBtn, leaveLabel);

        controlBar.getChildren().addAll(micBox, cameraBox, raiseBox, reactBox, leaveBox);
        controlBar.setAlignment(Pos.CENTER);


        videoContainer.getChildren().addAll(videoCallPane, controlBar);

        videoCallPane.prefWidthProperty().bind(rootLayout.widthProperty().multiply(0.7));
        chatContainer.prefWidthProperty().bind(rootLayout.widthProperty().multiply(0.3));

        // RIGHT PART (3 phan)
        VBox rightContainer = new VBox(15);

        // List of participants
        VBox listContainer = new VBox(5);

        HBox numberParticipants = new HBox();
        Label numberLabel = new Label("Participants");
        chatContainer = new VBox();
        chatContainer.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");



        rootLayout.getChildren().addAll(videoContainer, chatContainer);
        this.getChildren().add(rootLayout);
    }

    private VBox styleControlBox(String iconPath, String label) {
        Image iconOff = new Image(getClass().getResource(iconPath).toExternalForm());
        Image iconOn = new Image(getClass().getResource(iconPath.replace("_off.png", "_on.png")).toExternalForm());
        ImageView imageView = new ImageView(iconOff);
        imageView.setFitWidth(28);
        imageView.setFitHeight(28);

        Button buttonItem = new Button();
        buttonItem.setGraphic(imageView);
        buttonItem.setPrefSize(50, 50);
        buttonItem.setMinSize(50, 50);
        buttonItem.setMaxSize(50, 50);
        buttonItem.setStyle("-fx-background-color: #F6EBFF; -fx-border-radius: 15; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.2, 0, 1);");

        Label labelItem = new Label(label);
        labelItem.setFont(Font.font("Poppins", FontWeight.BOLD, 13));
        labelItem.setStyle("-fx-text-fill: #000;");

        // Style VBox
        VBox box = new VBox(5, buttonItem, labelItem);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(5));
        box.setStyle("-fx-background-color: #fff;");

        // ---- STATE ----
        final boolean[] isOn = {false};  // mặc định off

        // ---- Toggle style ----
        buttonItem.setOnMouseClicked(e -> {
            isOn[0] = !isOn[0];   // Đổi trạng thái

            if (isOn[0]) {
                // TURN ON
                imageView.setImage(iconOn);
                labelItem.setStyle("-fx-text-fill: #6A00F4;");
                buttonItem.setStyle(
                        "-fx-background-color: #E6D4FF; -fx-border-radius: 15; -fx-background-radius: 15;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.20), 8, 0.3, 0, 1);"
                );
            } else {
                // TURN OFF
                imageView.setImage(iconOff);
                labelItem.setStyle("-fx-text-fill: #000;");
                buttonItem.setStyle(
                        "-fx-background-color: #F6EBFF; -fx-border-radius: 15; -fx-background-radius: 15;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0.2, 0, 1);"
                );
            }
        });

        return box;
    }
}
