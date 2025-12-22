package main.Client.Controller;
// GỬI REQUEST ĐẾN SERVER
// NHẬN REPONSE TỪ CLIENT
// CẬP NHẬT UI

import javafx.application.Platform;
import main.Client.Network.TCP.SocketClient;
import main.Client.View.LogIn;
import main.util.Session;
import org.bson.Document;

public class LoginController {
    private LogIn logInView;

    public LoginController(LogIn view) {
        this.logInView = view;
    }

    // Khi nguoi dung click vao button SignIn
    public void onClickLogin() {
        String email = logInView.getEmail();
        String password = logInView.getPassword();

        if (email.isEmpty() || password.isEmpty()) {
            Platform.runLater(() -> {
                logInView.showError("Please fill in all fields.");
            });
            return;
        }

        // Chạy trên thread riêng để không block UI
        new Thread(() -> {
            try {
                SocketClient socketClient = SocketClient.getInstance();
                if (!socketClient.isConnected()) {
                    Platform.runLater(() -> logInView.showError("TCP chưa kết nối. Hãy đợi kết nối xong!"));
                }

                // Gui login request
                Document loginRequest = new org.bson.Document("type", "LOGIN").append("email", email).append("password", password);

                socketClient.send(loginRequest);

                // Cho server tra ket qua (Doc 1 dong)
                Document response = Document.parse(socketClient.getReader().readLine());

                String type = response.getString("type");

                // Neu login thanh cong
                if ("LOGIN_OK".equals(type)) {
//                    String userIdHex = response.getString("userIdHex"); // Lay userIdHex tu server de luu session
//                    Session.getInstance().setUser(email, userIdHex);

                    String userIdHex = response.getString("userIdHex");

                    // 1️⃣ GỬI REQUEST GET_PROFILE
                    Document profileRequest = new Document("type", "GET_PROFILE")
                            .append("email", email);

                    System.out.println("👉 SEND GET_PROFILE: " + profileRequest.toJson());

                    socketClient.send(profileRequest);

                    // 2️⃣ NHẬN RESPONSE
                    Document profileResponse =
                            Document.parse(socketClient.getReader().readLine());

                    if (!"GET_PROFILE_OK".equals(profileResponse.getString("type"))) {
                        Platform.runLater(() ->
                                logInView.showError("Cannot load user profile")
                        );
                        return;
                    }
//
//                    String emailRes  = response.getString("email");
//                    String fullName  = response.getString("fullName");

                    // ET SESSION TẠI ĐÂY (CHỈ 1 LẦN)
//                    Session.getInstance().setUser(
//                            emailRes,
//                            userIdHex,
//                            fullName
//                    );

                    // 3️⃣ SET SESSION ĐẦY ĐỦ
                    Session.getInstance().setUser(
                            profileResponse.getString("email"),
                            profileResponse.getString("userIdHex"),
                            profileResponse.getString("fullName")
                    );
                    System.out.println("Login Controller - SESSION FULLNAME = " + Session.getInstance().getFullName());
                    System.out.println("Login Controller - SESSION EMAIL = " + Session.getInstance().getEmail());

                    // Goi HELLO toi server chat
                    main.Client.Controller.ChatClient.getInstance().connectWithHello();

                    // Hien thi Dashboard
                    Platform.runLater(() -> {
                        logInView.openDashboard();
                    });
                }
                else if ("LOGIN_FAIL".equals(type)) {
                    String message = response.getString("message");
                    Platform.runLater(() -> logInView.showError(message));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> logInView.showError("TCP not connected!"));
            }
        }).start();
    }
}
