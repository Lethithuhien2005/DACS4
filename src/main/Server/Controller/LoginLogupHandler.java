package main.Server.Controller;

import main.Server.Controller.UserController;
import org.bson.Document;

public class LoginLogupHandler {
    private final UserController userController = new UserController();

    // Trả về Document response
    public Document handle(Document request) {
        String type = request.getString("type");

        if ("LOGIN".equals(type)) {
            return handleLogin(request);
        } else if ("LOGUP".equals(type)) {
            return handleLogup(request);
        } else {
            return new Document("type", "ERROR")
                    .append("message", "LoginLogupHandler cannot handle type: " + type);
        }
    }

    private Document handleLogin(Document request) {
        String email = request.getString("email");
        String password = request.getString("password");

        String result = userController.login(email, password);
        if ("SUCCESS".equals(result)) {
            // Lấy userIdHex từ UserController
            String userIdHex = userController.getUserProfile(email).getUserId().toHexString();
            return new Document("type", "LOGIN_OK")
                    .append("userIdHex", userIdHex);
        } else {
            return new Document("type", "LOGIN_FAIL")
                    .append("message", result);
        }

    }

    private Document handleLogup(Document request) {
        // TODO: xử lý logup
        return new Document("type", "LOGUP_RESPONSE").append("status", "OK");
    }
}
