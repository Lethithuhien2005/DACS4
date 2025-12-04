package main.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class SidebarController {
    private StackPane contentPane;
    private VBox sidebar;

    public SidebarController() {
        sidebar = new VBox(12);
        sidebar.setPrefWidth(80);
   //  sidebar.setStyle("-fx-background-color: #8900f2");
        sidebar.setStyle("-fx-background-color: #f2ebfb");
        contentPane = new StackPane();
        contentPane.setStyle("-fx-background-color: #f5f3f4");

        // MenuItem meeting
        VBox homeItem = createMenuItem("/images/home.png", "Home");
        VBox.setMargin(homeItem, new Insets(20, 0, 0, 0));
        homeItem.setOnMouseClicked(e -> setContent(new Home(contentPane)));

        // MenuItem meeting
        VBox meetingItem = createMenuItem("/images/video.png", "Meet");
        meetingItem.setOnMouseClicked(e -> setContent(new MeetingUI()));

        // MenuItem chatting
        VBox chattingItem = createMenuItem("/images/chat.png", "Chat");
        chattingItem.setOnMouseClicked(e -> setContent(new ChatPage(contentPane)));

        // MenuItem meeting
        VBox accountItem = createMenuItem("/images/profile.png", "Account");
        accountItem.setOnMouseClicked(e -> setContent(new Account()));

        // Spacer
        Pane spacer = new Pane();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        // MenuItem logout
        VBox logoutItem = createMenuItem("/images/logout.png", "Logout");
        logoutItem.setOnMouseClicked(e -> logout());

        sidebar.getChildren().addAll(homeItem, meetingItem, chattingItem, accountItem, spacer, logoutItem);
    }

    private VBox createMenuItem(String iconPath, String label) {
        Image icon = new Image(getClass().getResource(iconPath).toExternalForm());
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);

        Label labelItem = new Label(label);
        labelItem.setFont(Font.font("Poppins", FontWeight.NORMAL, 12));
//        labelItem.setStyle("-fx-text-fill: #fff;");
        labelItem.setStyle("-fx-text-fill: #000;");

        VBox vBox = new VBox(5, imageView, labelItem);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(12));

        // Hover effect

        // Change icon
        String hoverIconPath = iconPath.replace(".png", "_1.png");
//        String hoverIconPath = iconPath.replace("_2.png", "_3.png");
        Image hoverIcon = new Image(getClass().getResource(hoverIconPath).toExternalForm());

        vBox.setOnMouseEntered(e -> {
//            vBox.setStyle("-fx-background-color: #6A00F4;");
            vBox.setStyle("-fx-background-color: #fff; -fx-border-color: fff; -fx-background-radius: 10;");
            labelItem.setFont(Font.font("Poppins", FontWeight.BOLD, 12));
            labelItem.setStyle("-fx-text-fill: #872AFF;");
            imageView.setImage(hoverIcon);
        });
        vBox.setOnMouseExited(e -> {
            vBox.setStyle("-fx-background-color: transparent");
            labelItem.setFont(Font.font("Poppins", FontWeight.NORMAL, 12));
            labelItem.setStyle("-fx-text-fill: #000;");
            imageView.setImage(icon);
        });

        return vBox;
    }

    private void setContent(javafx.scene.Node newContent) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(newContent);
    }

    private void logout() {
        try {
            Stage currentStage = (Stage) sidebar.getScene().getWindow();
            currentStage.close();

            LogIn logIn = new LogIn();
            Stage logInStage = new Stage();
            logIn.start(logInStage);
        } catch (Exception e) {
            System.out.println("Logout failed!");
            e.printStackTrace();
        }
    }

    public VBox getSidebar() {
        return sidebar;
    }

    public StackPane getContentPane() {
        return contentPane;
    }
}
