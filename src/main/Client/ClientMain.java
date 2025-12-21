package main.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import main.Client.Network.TCP.SocketClient;
import main.Client.View.LogIn;
import main.util.DialogUtil;

public class ClientMain extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        this.primaryStage = primaryStage;

        // Connect TCP ngay khi app start (chạy trong thread riêng)
//        new Thread(() -> {
//            try {
//                SocketClient.getInstance().connect("localhost", 5555); // Thay doi IP
//                System.out.println("TCP Connected!");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                // Hien thi dialog thong bao
//                DialogUtil.showError("Connect error", "Couldn't connect to server", "Connected to Server failed");
//            }
//        }).start();

//        StackPane root = new StackPane();
//        Scene scene = new Scene(root, 900, 600);
//        primaryStage.setScene(scene);


        connectTCP();

        // 2. Mở Login
        LogIn login = new LogIn();
        try {
            login.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        showLogInPage(root);

        // Hien thi trang Login
//        showLogInPage();

//        primaryStage.setTitle("My App");
//        primaryStage.show();
    }

//    private void showLogInPage() {
//        LogIn loginPage = new LogIn();
//    }

//    private void showLogInPage(StackPane root) {
//        root.getChildren().clear();
//        root.getChildren().add(new LogIn());
//    }

    private void connectTCP() {
        new Thread(() -> {
            try {
                SocketClient.getInstance().connect("localhost", 5555);
                System.out.println("TCP Connected!");
            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() -> {
                    DialogUtil.showError(
                            "Connect error",
                            "Couldn't connect to server",
                            "Connected to Server failed"
                    );
                });
            }
        }).start();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
