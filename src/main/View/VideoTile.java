package main.View;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class VideoTile extends StackPane {
    private ImageView videoView;
    private Label accountName;

    public VideoTile(String username) {
        videoView = new ImageView();
        videoView.setPreserveRatio(true);
        videoView.setSmooth(true);
        // QUAN TRỌNG: video sẽ co giãn theo tile
        videoView.fitWidthProperty().bind(this.widthProperty());
        videoView.fitHeightProperty().bind(this.heightProperty());

        accountName = new Label(username);
        accountName.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
        accountName.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-text-fill: white; -fx-padding: 5px;");

        StackPane.setAlignment(accountName, Pos.BOTTOM_CENTER);
        this.setMinSize(100, 70);     // an toàn
        this.setPrefSize(300, 200);   // gợi ý
//        this.setMaxSize(800, 600);
        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().addAll(videoView, accountName);
        this.setStyle("-fx-background-color: #000; -fx-border-color: #444; -fx-border-width: 1; -fx-background-radius: 15; -fx-border-radius: 15;");
    }

    public ImageView getVideoView() {
        return videoView;
    }

    public void updateFrame(Image frame) {
        // Sau này dùng để update frame từ stream
        videoView.setImage(frame);
    }
}
