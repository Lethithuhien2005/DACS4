package main.Controller;

import main.Model.DAO.UserDAO;
import main.Model.Entities.User;
import main.Model.PasswordUtils;

public class UserController {
    private UserDAO userDAO;

    public UserController() {
        userDAO = new UserDAO();
    }

    public String register(String username, String email, String password) {
        // Encrypt password before store at database
        String salt = password + "hienanh";
        String hashedPassword = PasswordUtils.hashPassword(salt);

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return "Please fill in all fields.";
        }
        if (userDAO.checkEmail(email)) {
            return "Email already exists!!!";
        }
        User user = new User(username, email, hashedPassword, "user");
        userDAO.addUser(user);
        return "SUCCESS";
    }

    public String login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            return "Please fill in all fields.";
        }
        // Get user from database base on email
        User user = userDAO.getUserByEmail(email);
        if (user ==  null) {
            return "Email does not exists!";
        }

        // Check password
        String salt = "hienanh";
        String hashedInput = PasswordUtils.hashPassword(password + salt);
        if (!hashedInput.equals(user.getPassword())) {
            return "Incorrect password!";
        }

        return "SUCCESS";
    }
    public User getUserProfile(String email) {
        return userDAO.getUserByEmail(email);
    }

    public boolean updateUserProfile(User user) {
        return userDAO.updateUser(user);
    }

    public void updateUserPassword(User user) {
        userDAO.updatePassword(user.getEmail(), user.getPassword());
    }

}
