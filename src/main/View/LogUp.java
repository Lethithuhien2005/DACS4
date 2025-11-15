package main.View;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static mongodb.PasswordUtils.hashPassword;


public class LogUp extends Application {
    double fieldWidth = 320;
    double fieldHeight = 50;

    @Override
    public void start(Stage stage) {
        // Left part
        Image logo = new Image(getClass().getResource("/images/logo2.jpg").toExternalForm());
        ImageView logo_imageView = new ImageView(logo);
        logo_imageView.setFitWidth(50);
        logo_imageView.setFitHeight(70);
        logo_imageView.setPreserveRatio(true);
        Label label3 = new Label("HaloMeet");
        label3.setFont(Font.font("Poppins", FontWeight.BOLD, 20));
        label3.setTextFill(Color.BLACK);
        label3.setPadding(new Insets(-5, 0, 0, 0));

        HBox app = new HBox(10);
        app.getChildren().addAll(logo_imageView, label3);
        app.setAlignment(Pos.TOP_CENTER);
        app.setPadding(new Insets(-20, 0, 30, 0));

        Label lable3 = new Label("CREATE AN ACCOUNT");
        lable3.setFont(Font.font("Poppins", FontWeight.BOLD, 25));
        lable3.setTextFill(Color.BLACK);
        lable3.setPadding(new Insets(20, 0, 10, 0));

        String textFieldStyle = """
                -fx-text-fill: black;
                -fx-prompt-text-fill: gray;
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                -fx-border-color: gray; 
                -fx-border-width: 1.5;
                """;
        String textFieldFocus = """
                -fx-text-fill: black;
                -fx-prompt-text-fill: gray;
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                -fx-border-color: #b333e9; 
                -fx-border-width: 2;
                """;

        Label name = new Label("Account name");
        name.setFont(Font.font("Poppins", FontWeight.BOLD, 16));
        name.setTextFill(Color.BLACK);

        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Enter your account name");
        nameTextField.setFont(Font.font("Poppins", FontWeight.NORMAL, 15));

        nameTextField.setStyle(textFieldStyle);
        nameTextField.setMaxWidth(fieldWidth);
        nameTextField.setPrefHeight(fieldHeight);
        // Change style when focusing
        nameTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                nameTextField.setStyle(textFieldFocus);
            } else {
                nameTextField.setStyle(textFieldStyle);
            }
        });
        nameTextField.setPrefWidth(fieldWidth);
        nameTextField.setMaxHeight(fieldHeight);

        Label email = new Label("Email");
        email.setFont(Font.font("Poppins", FontWeight.BOLD, 16));
        email.setTextFill(Color.BLACK);

        TextField emailTextField = new TextField();
        emailTextField.setPromptText("Enter your email");
        emailTextField.setFont(Font.font("Poppins", FontWeight.NORMAL, 15));

        emailTextField.setStyle(textFieldStyle);
        emailTextField.setMaxWidth(fieldWidth);
        emailTextField.setPrefHeight(fieldHeight);
        // Change style when focusing
        emailTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                emailTextField.setStyle(textFieldFocus);
            } else {
                emailTextField.setStyle(textFieldStyle);
            }
        });

        Label password = new Label("Password");
        password.setFont(Font.font("Poppins", FontWeight.BOLD, 16));
        password.setTextFill(Color.BLACK);
        password.setMaxWidth(400);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setFont(Font.font("Poppins", FontWeight.NORMAL, 15));

        passwordField.setStyle(textFieldStyle);
        passwordField.setMaxWidth(fieldWidth);
        passwordField.setPrefHeight(fieldHeight);
        // Change style when focusing
        passwordField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                passwordField.setStyle(textFieldFocus);
            } else {
                passwordField.setStyle(textFieldStyle);
            }
        });

        Button signInBtn = new Button("Sign up");
        signInBtn.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
        String signInBtnStyle = """
                -fx-background-color: #8900f2;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 30;
                -fx-border-radius: 30;
                -fx-border-color: transparent;
                -fx-cursor: hand;
                """;
        String signInBtnHover = """
                -fx-background-color: #6a00f4; 
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 30;
                -fx-border-radius: 30;
                -fx-border-color: transparent;
                -fx-cursor: hand;
                """;
        signInBtn.setStyle(signInBtnStyle);
        signInBtn.setOnMouseEntered(e -> {
            signInBtn.setStyle(signInBtnHover);
        });
        signInBtn.setOnMouseExited(e -> {
            signInBtn.setStyle(signInBtnStyle);
        });

        signInBtn.setMaxWidth(100);
        signInBtn.setPrefHeight(40);
        VBox.setMargin(signInBtn, new Insets(15, 0, 0, 0));

        // Set Action for Signin Btn
        signInBtn.setOnAction(e -> {
            // Lấy giá trị từ form
            String username = nameTextField.getText() != null ? nameTextField.getText() : "";
            String emailText = emailTextField.getText() != null ? emailTextField.getText() : "";
            String passwordText = passwordField.getText() != null ? passwordField.getText() : "";

            // Hash password
            String hashedPassword = hashPassword(passwordText);

            // Kết nối MongoDB và lưu user
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
                MongoDatabase db = mongoClient.getDatabase("halomeet");
                MongoCollection<Document> users = db.getCollection("users");

                Document user = new Document("username", username)
                        .append("fullname", "")
                        .append("email", emailText)
                        .append("password", hashedPassword)
                        .append("female", "")
                        .append("dateOfBirth", "");

                users.insertOne(user);
                System.out.println("User '" + username + "' đã được lưu vào MongoDB!");


                LogIn logInPage = new LogIn();
                Stage loginStage = new Stage();
                logInPage.start(loginStage);
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        // Keep the same size for email, password and button when window change size
        signInBtn.prefWidthProperty().bind(email.widthProperty());
        password.prefWidthProperty().bind(email.widthProperty());

        VBox nameBox = new VBox(2, name, nameTextField);
        VBox emailBox = new VBox(2, email, emailTextField);
        VBox passwordBox = new VBox(2, password, passwordField);

        VBox form = new VBox(8, nameBox, emailBox, passwordBox, signInBtn);
        form.setPadding(new Insets(0, 0, 0, 120));
        form.setAlignment(Pos.CENTER_LEFT);

        Label signupLabel = new Label("Have an account?");
        signupLabel.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.NORMAL, 15));
        Hyperlink sigInLink = new Hyperlink("Log in");
        sigInLink.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.NORMAL, 15));
        sigInLink.setTextFill(Color.web("#7b2cbf"));
        sigInLink.setPadding(new Insets(-1, 0, 0, 0));
        HBox signupBox = new HBox(5, signupLabel, sigInLink);
        signupBox.setPadding(new Insets(10, 0, 0, 125));

        sigInLink.setOnAction(e -> {
            LogIn logInPage = new LogIn();
            Stage stage1 = new Stage();
            try {
                logInPage.start(stage1);
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox leftPane = new VBox(5, app, lable3, form, signupBox);
        leftPane.setAlignment(Pos.CENTER);

        // Right part
        Label lable1 = new Label("Join HaloMeet Today!");
        lable1.setFont(Font.font("Poppins", FontWeight.BOLD, 36));
        lable1.setTextFill(Color.BLACK);
        lable1.setPadding(new Insets(20, 0, 0, 30));

        Label lable2 = new Label("Register to join online meeting rooms, chat, and easily share files");
        lable2.setFont(Font.font("Poppins", FontWeight.NORMAL, 20));
        lable2.setTextFill(Color.BLACK);
        lable2.setPadding(new Insets(0, 0, 20, 30));


        Image logup_image = new Image(getClass().getResource("/images/signup.png").toExternalForm());
        ImageView logup_imageView = new ImageView(logup_image);
        logup_imageView.setFitWidth(600);
        logup_imageView.setFitHeight(450);
        logup_imageView.setPreserveRatio(true);
        VBox.setMargin(logup_imageView, new Insets(0, 0, 0, 70));

        VBox rightPane = new VBox(10, lable1, lable2, logup_imageView);
        rightPane.setPadding(new Insets(20, 40, 30, 0));

        HBox root = new HBox(leftPane, rightPane);
        root.setSpacing(10);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));

        // When changing the window's size, each part is 50%
        root.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            leftPane.setPrefWidth(width * 0.45);
            rightPane.setPrefWidth(width * 0.55);
        });

        Scene scene = new Scene(root);
        stage.setTitle("Sign up");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
