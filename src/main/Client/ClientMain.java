package main.Client;

import javafx.application.Application;
import javafx.stage.Stage;
import main.Client.Network.TCP.SocketClient;
import main.Client.View.LogIn;
import main.util.DialogUtil;

public class ClientMain extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Connect TCP ngay khi app start (chạy trong thread riêng)
        new Thread(() -> {
            try {
                SocketClient.getInstance().connect("localhost", 5555); // Thay doi IP
                System.out.println("TCP Connected!");
            } catch (Exception ex) {
                ex.printStackTrace();
                // Hien thi dialog thong bao
                DialogUtil.showError("Connect error", "Couldn't connect to server", "Connected to Server failed");
            }
        }).start();

        // Hien thi trang Login
        showLogInPage();

        primaryStage.setTitle("My App");
        primaryStage.show();
    }

    private void showLogInPage() {
        LogIn loginPage = new LogIn();
    }


}
