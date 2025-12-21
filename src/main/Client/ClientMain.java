//package main.Client;
//
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.scene.Scene;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//import main.Client.Network.TCP.SocketClient;
//import main.Client.View.LogIn;
//import main.util.DialogUtil;
//
//public class ClientMain extends Application {
//    private Stage primaryStage;
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//
//        connectTCP();
//
//        // 2. Mở Login
//        LogIn login = new LogIn();
//        try {
//            login.start(primaryStage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    private void connectTCP() {
//        new Thread(() -> {
//            try {
//                SocketClient.getInstance().connect("localhost", 5555);
//                System.out.println("TCP Connected!");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Platform.runLater(() -> {
//                    DialogUtil.showError(
//                            "Connect error",
//                            "Couldn't connect to server",
//                            "Connected to Server failed"
//                    );
//                });
//            }
//        }).start();
//    }
//
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//}

package main.Client;

//import common.meeting.MeetingService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
//import main.Client.Controller.MeetingService;
import main.Client.Network.TCP.SocketClient;
import main.Client.View.LogIn;
import main.util.DialogUtil;
import shared.ChatService;
import shared.MeetingService;
//import shared.MeetingService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientMain extends Application {

    // ⭐ GIỮ RMI SERVICE TOÀN CLIENT
    public static MeetingService meetingService;
    public static ChatService chatService;

//    @Override
//    public void start(Stage primaryStage) {

//    private MeetingService meetingService;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Connect TCP ngay khi app start (chạy trong thread riêng)
        connectTCP();
        connectRMI();

        // ===== 2. CONNECT RMI =====
        connectRMI();

        // ===== 3. OPEN LOGIN =====
        try {
            LogIn login = new LogIn();
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
                Platform.runLater(() ->
                        DialogUtil.showError(
                                "TCP Error",
                                "Cannot connect TCP server",
                                ex.getMessage()
                        )
                );
            }
        }).start();
    }

    // RMI CONNECT
//    private void connectRMI() {
//        try {
//            Registry registry = LocateRegistry.getRegistry("localhost", 2005);
//            meetingService = (MeetingService) registry.lookup("MeetingService");
//            System.out.println("RMI Connected!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            DialogUtil.showError(
//                    "RMI Error",
//                    "Cannot connect RMI server",
//                    e.getMessage()
//            );
//            System.exit(0);
//        }
//    }

    private void connectRMI() {
        new Thread(() -> {
            try {
                // lookup service
                Registry registry = LocateRegistry.getRegistry("localhost", 2005);
                meetingService = (MeetingService) registry.lookup("MeetingService");
                chatService    = (ChatService) registry.lookup("ChatService");

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

//    public static void main(String[] args) {
//        launch(args);
//    }

    public static void main(String[] args) {
        // BẮT BUỘC cho RMI callback
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");

        launch(args);
    }
}
