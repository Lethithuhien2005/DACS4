package main.Client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import main.Client.Network.TCP.SocketClient;
import main.Client.View.LogIn;
import main.util.DialogUtil;
import shared.MeetingService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain extends Application {
    private MeetingService meetingService;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Connect TCP ngay khi app start (chạy trong thread riêng)
        connectTCP();
        connectRMI();

        // 2. Mở Login
        LogIn login = new LogIn();
        try {
            login.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }

   }

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

    private void connectRMI() {
        new Thread(() -> {
            try {
                // lookup service
                Registry registry = LocateRegistry.getRegistry("localhost", 2005);
                meetingService = (MeetingService) registry.lookup("MeetingService");
                System.out.println("RMI Connected!");
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> DialogUtil.showError(
                        "RMI Connect error",
                        "Couldn't connect to RMI server",
                        "RMI connection failed"
                ));
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
