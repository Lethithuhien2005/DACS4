package main.Client.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import main.Client.Controller.LoginController;
import main.Client.Controller.MeetingController;
import main.Client.DTO.Meeting;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Home extends StackPane {
    public PasswordField passwordRoom;
    public TextField idTextField;
    public TextField titleTextField;
    public PasswordField passwordMeeting;
    private StackPane contentPane;
    double fieldHeight = 40;
    private VBox noMeetingBox;
    private GridPane meetingTodayContainer;
    private Image noMeetingImage;

    private MeetingController meetingController = new MeetingController(this);

    public Home(StackPane contentPane) {
        this.contentPane = contentPane;

        HBox container = new HBox();
        VBox leftContainer = new VBox();
        VBox rightContainer = new VBox();
        // Grow with window
        HBox.setHgrow(leftContainer, Priority.ALWAYS);
        HBox.setHgrow(rightContainer, Priority.ALWAYS);

        VBox welcomeContainer = new VBox(10);

        HBox logoApp = new HBox(10);
        Image logo = new Image(getClass().getResource("/images/logo2.png").toExternalForm());
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(80);
        logoView.setFitWidth(120);

        Label welcome = new Label("Welcome to HaloMeet!");
        welcome.setFont(Font.font("Poppins", FontWeight.BOLD, 30));
        welcome.setPadding(new Insets(10, 0, 0, 20));

        logoApp.getChildren().addAll(logoView, welcome);

        Label textWelcome = new Label("Let’s meet, share, and grow together");
        textWelcome.setFont(Font.font("Poppins", FontWeight.NORMAL, 22));

        welcomeContainer.getChildren().addAll(logoApp, textWelcome);
        VBox.setMargin(textWelcome, new Insets(-30, 0, 0, 150 ));
        welcomeContainer.setPadding(new Insets(20, 20, 20, 15));

        VBox meetingBox = new VBox(5);

        Label label1 = new Label("New meeting");
        label1.setFont(Font.font("Poppins", FontWeight.BOLD, 20));

        // ToggleGroup
        HBox meetingGroup = new HBox(10);
        meetingGroup.setPadding(new Insets(5));
        meetingGroup.setStyle("-fx-background-color: #fff; -fx-background-radius: 15;");

        ToggleGroup group = new ToggleGroup();
        ToggleButton onlineMeeting = new ToggleButton("Online meeting");
        ToggleButton scheduleMeeting = new ToggleButton("Schedule a meeting");
        ToggleButton joinMeeting = new ToggleButton("Join a meeting");

        onlineMeeting.setToggleGroup(group);
        Image onlineIconDefault= new Image(getClass().getResource("/images/video.png").toExternalForm());
        Image onlineIconActive= new Image(getClass().getResource("/images/video_4.png").toExternalForm());
        scheduleMeeting.setToggleGroup(group);
        Image scheduleIconDefault = new Image(getClass().getResource("/images/calendar.png").toExternalForm());
        Image scheduleIconActive= new Image(getClass().getResource("/images/calendar_1.png").toExternalForm());
        joinMeeting.setToggleGroup(group);
        Image joinIconDefault = new Image(Objects.requireNonNull(getClass().getResource("/images/join.png")).toExternalForm());;
        Image joinIconActive= new Image(getClass().getResource("/images/join_1.png").toExternalForm());

        // Each toogleButton 1/3 meetingBox
        HBox.setHgrow(onlineMeeting, Priority.ALWAYS);
        onlineMeeting.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(scheduleMeeting, Priority.ALWAYS);
        scheduleMeeting.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(joinMeeting, Priority.ALWAYS);
        joinMeeting.setMaxWidth(Double.MAX_VALUE);


        String toggleStyle = """
                -fx-background-color: #fff;
                -fx-border-radius: 15;
                -fx-padding: 10 16;
                -fx-font-size: 16;
                -fx-font-weight: bold;
                -fx-graphic-text-gap: 6;
                -fx-alignment: CENTER;
                -fx-text-fill: #000;
                """;

        String toggleStyleSelected = """
                -fx-background-color: #F6EBFF;
                -fx-text-fill: #6A00F4;
                -fx-border-radius: 15;
                -fx-background-radius: 15;
                -fx-background-insets: 0;
                -fx-padding: 10 16;
                -fx-font-size: 16;
                -fx-font-weight: bold;
                -fx-graphic-text-gap: 6;
                -fx-alignment: CENTER;
                """;

        setupToggleButton(onlineMeeting, onlineIconDefault, onlineIconActive, toggleStyle, toggleStyleSelected);
        onlineMeeting.setSelected(true);
        onlineMeeting.setStyle(toggleStyleSelected);
        ((ImageView) onlineMeeting.getGraphic()).setImage(onlineIconActive);
        setupToggleButton(scheduleMeeting, scheduleIconDefault, scheduleIconActive, toggleStyle, toggleStyleSelected);
        setupToggleButton(joinMeeting, joinIconDefault, joinIconActive, toggleStyle, toggleStyleSelected);

        meetingGroup.getChildren().addAll(onlineMeeting, scheduleMeeting, joinMeeting);
        meetingBox.getChildren().addAll(label1, meetingGroup);
        VBox.setMargin(label1, new Insets(10, 0, 15, 30));
        VBox.setMargin(meetingBox, new Insets(0, 30, 0, 30));

        // Corresponding content to each toggleButton
        VBox contentMeeting = new VBox();

        // ToggleButton 1
        VBox onlineContent = new VBox(3);

        Label titleMeetingLabel = new Label("Give your meeting a title");
        titleMeetingLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
        titleMeetingLabel.setPadding(new Insets(10, 0, 8, 10));

        titleTextField = new TextField();
        titleTextField.setPromptText("What's this meeting about?");
        titleTextField.setFont(Font.font("Poppins", FontWeight.NORMAL, 13));
        titleTextField.setPrefHeight(fieldHeight);

        Label passwordLabel = new Label("Make it secure (Optional)");
        passwordLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
        passwordLabel.setPadding(new Insets(10, 0, 8, 10));

        passwordMeeting = new PasswordField();
        passwordMeeting.setFont(Font.font("Poppins", FontWeight.NORMAL, 13));
        passwordMeeting.setPromptText("Enter a password meeting");
        passwordMeeting.setPrefHeight(fieldHeight);

        String textFieldStyle = """
                -fx-text-fill: black;
                -fx-prompt-text-fill: gray;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-border-color: gray; 
                -fx-border-width: 1.5;
                """;
        String textFieldFocus = """
               -fx-text-fill: black;
               -fx-prompt-text-fill: gray;
               -fx-background-radius: 10;
               -fx-border-radius: 10;
               -fx-border-color: #9D59F5; 
               -fx-border-width: 2;
               """;

        titleTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                titleTextField.setStyle(textFieldFocus);
            } else {
                titleTextField.setStyle(textFieldStyle);
            }
        });

        passwordMeeting.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                passwordMeeting.setStyle(textFieldFocus);
            } else {
                passwordMeeting.setStyle(textFieldStyle);
            }
        });
        String BtnStyle = """
                -fx-background-color: #8900f2;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-border-color: transparent;
                -fx-cursor: hand;
                -fx-font-size: 15;
                -fx-padding: 10 20;     
                -fx-min-height: 40; 
                """;
        String BtnHover = """
                -fx-background-color: #6B44E8; 
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-border-color: transparent;
                -fx-font-size: 15;
                -fx-cursor: hand;
                -fx-padding: 10 20;  
                -fx-min-height: 40; 
                """;

        Button createMeetingButton = new Button("Create a new meeting");
        createMeetingButton.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
        VBox.setMargin(createMeetingButton, new Insets(12, 0, 0, 0));
        createMeetingButton.setStyle(BtnStyle);
        // Hover
        createMeetingButton.setOnMouseEntered(e -> {
            createMeetingButton.setStyle(BtnHover);
        });
        createMeetingButton.setOnMouseExited(e -> {
            createMeetingButton.setStyle(BtnStyle);
        });

        createMeetingButton.setOnAction(e -> meetingController.onClickCreatMeeting());


        VBox.setMargin(createMeetingButton, new Insets(12, 0, 0, 0));

        onlineContent.getChildren().addAll(titleMeetingLabel, titleTextField, passwordLabel, passwordMeeting, createMeetingButton);
        // set the width of onlineContent equals to meetingBox
        onlineContent.prefWidthProperty().bind(meetingBox.widthProperty());
        onlineContent.setPadding(new Insets(10, 10, 10, 10));
        onlineContent.setStyle("-fx-background-color: #fff; -fx-background-radius: 10;");

        // ToggleButton 3
        VBox joinContent = new VBox();

        Label joinLabel = new Label("Join a meeting with an ID");
        joinLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
        joinLabel.setPadding(new Insets(10, 0, 8, 10));
        idTextField = new TextField();
        idTextField.setFont(Font.font("Poppins", FontWeight.NORMAL, 13));
        idTextField.setPadding(new Insets(10, 0, 8, 10));
        idTextField.setPromptText("Type a meeting ID");
        idTextField.setPrefHeight(fieldHeight);

        Label passRoomIDLabel = new Label("Password meeting (Optional)");
        passRoomIDLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
        passRoomIDLabel.setPadding(new Insets(10, 0, 8, 10));
        passwordRoom = new PasswordField();
        passwordRoom.setFont(Font.font("Poppins", FontWeight.NORMAL, 13));
        passwordRoom.setPromptText("Type a meeting passcode");
        passwordRoom.setPrefHeight(fieldHeight);

        idTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                idTextField.setStyle(textFieldFocus);
            } else {
                idTextField.setStyle(textFieldStyle);
            }
        });

        passwordRoom.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (t1) {
                passwordRoom.setStyle(textFieldFocus);
            } else {
                passwordRoom.setStyle(textFieldStyle);
            }
        });

        Button joinButton = new Button("Join the meeting now");
        joinButton.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
        joinButton.setStyle(BtnStyle);
        // Hover
        joinButton.setOnMouseEntered(e -> {
            joinButton.setStyle(BtnHover);
        });
        joinButton.setOnMouseExited(e -> {
            joinButton.setStyle(BtnStyle);
        });

        joinButton.setOnAction(e -> meetingController.onClickJoinTheMeeting());

        VBox.setMargin(joinButton, new Insets(12, 0, 0, 0));

        createMeetingButton.setMaxWidth(Double.MAX_VALUE);
        joinButton.setMaxWidth(Double.MAX_VALUE);

        joinContent.getChildren().addAll(joinLabel, idTextField, passRoomIDLabel, passwordRoom, joinButton);
        // set the width of joinContent equals to meetingBox
        joinContent.prefWidthProperty().bind(meetingBox.widthProperty());
        joinContent.setPadding(new Insets(10, 10, 10, 10));
        joinContent.setStyle("-fx-background-color: #fff; -fx-background-radius: 10;");

        contentMeeting.getChildren().add(onlineContent); // default
        // Display corresponding content to toggleButton
        // Sau khi setupToggleButton cho tất cả toggle
        group.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == onlineMeeting) {
                contentMeeting.getChildren().setAll(onlineContent);
            } else if (newToggle == scheduleMeeting) {
                CalendarMeeting calendarMeeting = new CalendarMeeting(contentPane);
                contentPane.getChildren().setAll(calendarMeeting);
            } else if (newToggle == joinMeeting) {
                contentMeeting.getChildren().setAll(joinContent);
            }
        });

        // Keep style for toggleButton when click again
        onlineMeeting.setOnMouseClicked(e -> {
            if (!onlineMeeting.isSelected()) {
                onlineMeeting.setSelected(true);
            }
        });
        scheduleMeeting.setOnMouseClicked(e -> {
            if (!scheduleMeeting.isSelected()) {
                scheduleMeeting.setSelected(true);
            }
        });
        joinMeeting.setOnMouseClicked(e -> {
            if (!joinMeeting.isSelected()) {
                joinMeeting.setSelected(true);
            }
        });

        VBox.setMargin(contentMeeting, new Insets(15, 30, 0, 30));


        // Container chính cho các cuộc họp hôm nay
        VBox meetLinks = new VBox();

        Label label2 = new Label("Meetings today");
        label2.setFont(Font.font("Poppins", FontWeight.BOLD, 20));


        // Neu chua co meeting nao today
        noMeetingBox = createNoMeetingBox();



        // Neu co meeting hom nay
        // Box chứa danh sách meeting (ban đầu rỗng)
        meetingTodayContainer = new GridPane();
        meetingTodayContainer.setHgap(12);
        meetingTodayContainer.setVgap(12);
        meetingTodayContainer.setVisible(false);
        meetingTodayContainer.setManaged(false);  // Khong chiem cho

        // 2 cột bằng nhau
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        col1.setHgrow(Priority.ALWAYS);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        col2.setHgrow(Priority.ALWAYS);

        meetingTodayContainer.getColumnConstraints().addAll(col1, col2);

        meetLinks.getChildren().addAll(label2, noMeetingBox, meetingTodayContainer);

        VBox.setMargin(label2, new Insets(25, 0, 15, 30));
        VBox.setMargin(meetLinks, new Insets(0, 30, 30, 30));
        leftContainer.getChildren().addAll(welcomeContainer, meetingBox, contentMeeting, meetLinks);












//        VBox meetingToday1 = new VBox();
//
//        // Fontend
//        Label nameMeeting1 = new Label("Emerging Trends in Data & AI Discussion");
//        nameMeeting1.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
//
//        HBox timeCreateMeeting1 = new HBox(5);
//        ImageView timeImageView1 = new ImageView(new Image(getClass().getResource("/images/calendar_2.png").toExternalForm()));
//        timeImageView1.setFitWidth(20);
//        timeImageView1.setFitHeight(20);
//        Label time1 = new Label("02-12-2025 08:55 AM");
//        time1.setFont(Font.font("Poppins", FontWeight.BOLD, 13));
//        time1.setStyle("-fx-text-fill: gray");
//        timeCreateMeeting1.getChildren().addAll(timeImageView1, time1);
//
//        HBox meetingTodayBtnGroup1 = new HBox(5);
//        String btnMeetingTodayStyle = """
//                -fx-background-color: #F2E7FF;
//                -fx-border-raius: 10;
//                -fx-padding: 6 12;
//                -fx-text-fill: #000;
//                -fx-background-radius: 10;
//                -fx-font-size: 15;
//                -fx-font-weight: bold;
//                -fx-graphic-text-gap: 6;
//                -fx-alignment: CENTER;
//                -fx-min-height: 28;
//                """;
//
//        String btnMeetingTodayStyleHover = """
//                -fx-background-color: #D6B4FF;
//                -fx-border-raius: 10;
//                -fx-padding: 6 12;
//                -fx-text-fill: #000;
//                -fx-background-radius: 10;
//                -fx-font-size: 15;
//                -fx-font-weight: bold;
//                -fx-graphic-text-gap: 6;
//                -fx-alignment: CENTER;
//                -fx-min-height: 28;
//                """;
//
//        Button joinMeetingBtn1 = new Button("Join now");
//        joinMeetingBtn1.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
//        joinMeetingBtn1.setStyle(btnMeetingTodayStyle);
//        joinMeetingBtn1.setOnMouseEntered(e -> {
//            joinMeetingBtn1.setStyle(btnMeetingTodayStyleHover);
//        });
//        joinMeetingBtn1.setOnMouseExited(e -> {
//            joinMeetingBtn1.setStyle(btnMeetingTodayStyle);
//        });
//
//        Button shareMeetingBtn1 = new Button("Share meeting");
//        shareMeetingBtn1.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
//        shareMeetingBtn1.setStyle(btnMeetingTodayStyle);
//        shareMeetingBtn1.setOnMouseEntered(e -> {
//            shareMeetingBtn1.setStyle(btnMeetingTodayStyleHover);
//        });
//        shareMeetingBtn1.setOnMouseExited(e -> {
//            shareMeetingBtn1.setStyle(btnMeetingTodayStyle);
//        });
//
//        meetingTodayBtnGroup1.getChildren().addAll(joinMeetingBtn1, shareMeetingBtn1);
//
//        joinMeetingBtn1.prefWidthProperty().bind(meetingTodayBtnGroup1.widthProperty().multiply(0.5));
//        meetingToday1.setMaxWidth(Region.USE_PREF_SIZE);
//        shareMeetingBtn1.prefWidthProperty().bind(meetingTodayBtnGroup1.widthProperty().multiply(0.5));
//        meetingToday1.setMaxWidth(Region.USE_PREF_SIZE);
//
//        meetingToday1.getChildren().addAll(nameMeeting1, timeCreateMeeting1, meetingTodayBtnGroup1);
//        meetingToday1.setPadding(new Insets(15));
//        meetingToday1.setStyle("-fx-background-color: #fff;");
//        meetingToday1.setOnMouseEntered(e -> {
//            meetingToday1.setStyle("-fx-background-color: #fff; -fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #801AFB;");
//        });
//        meetingToday1.setOnMouseExited(e -> {
//            meetingToday1.setStyle("-fx-background-color: #fff; -fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: transparent;");
//        });
//        VBox.setMargin(nameMeeting1, new Insets(0, 0, 8, 0));
//        VBox.setMargin(timeCreateMeeting1, new Insets(2, 0, 15, 0));
//
//
//        VBox meetingToday2 = new VBox();
//        Label nameMeeting2 = new Label("Data Integration & Transformation planning");
//        nameMeeting2.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
//
//        HBox timeCreateMeeting2 = new HBox(5);
//        ImageView timeImageView2 = new ImageView(new Image(getClass().getResource("/images/calendar_2.png").toExternalForm()));
//        timeImageView2.setFitWidth(20);
//        timeImageView2.setFitHeight(20);
//        Label time2 = new Label("02-12-2025 02:25 PM");
//        time2.setFont(Font.font("Poppins", FontWeight.BOLD, 13));
//        time2.setStyle("-fx-text-fill: gray");
//        timeCreateMeeting2.getChildren().addAll(timeImageView2, time2);
//
//        HBox meetingTodayBtnGroup2 = new HBox(5);
//        Button joinMeetingBtn2 = new Button("Join now");
//        joinMeetingBtn2.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
//        joinMeetingBtn2.setStyle(btnMeetingTodayStyle);
//        joinMeetingBtn2.setOnMouseEntered(e -> {
//            joinMeetingBtn2.setStyle(btnMeetingTodayStyleHover);
//        });
//        joinMeetingBtn2.setOnMouseExited(e -> {
//            joinMeetingBtn2.setStyle(btnMeetingTodayStyle);
//        });
//
//        Button shareMeetingBtn2 = new Button("Share meeting");
//        shareMeetingBtn2.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
//        shareMeetingBtn2.setStyle(btnMeetingTodayStyle);
//        shareMeetingBtn2.setOnMouseEntered(e -> {
//            shareMeetingBtn2.setStyle(btnMeetingTodayStyleHover);
//        });
//        shareMeetingBtn2.setOnMouseExited(e -> {
//            shareMeetingBtn2.setStyle(btnMeetingTodayStyle);
//        });
//
//        meetingTodayBtnGroup2.getChildren().addAll(joinMeetingBtn2, shareMeetingBtn2);
//
//        joinMeetingBtn2.prefWidthProperty().bind(meetingTodayBtnGroup2.widthProperty().multiply(0.5));
//        meetingToday2.setMaxWidth(Region.USE_PREF_SIZE);
//        shareMeetingBtn2.prefWidthProperty().bind(meetingTodayBtnGroup2.widthProperty().multiply(0.5));
//        meetingToday2.setMaxWidth(Region.USE_PREF_SIZE);
//
//        meetingToday2.getChildren().addAll(nameMeeting2, timeCreateMeeting2, meetingTodayBtnGroup2);
//        meetingToday2.setPadding(new Insets(15));
//        meetingToday2.setStyle("-fx-background-color: #fff;");
//        meetingToday2.setOnMouseEntered(e -> {
//            meetingToday2.setStyle("-fx-background-color: #fff; -fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #801AFB;");
//        });
//        meetingToday2.setOnMouseExited(e -> {
//            meetingToday2.setStyle("-fx-background-color: #fff; -fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: transparent;");
//        });
//        VBox.setMargin(nameMeeting1, new Insets(0, 0, 8, 0));
//        VBox.setMargin(timeCreateMeeting1, new Insets(2, 0, 15, 0));
//        VBox.setMargin(nameMeeting2, new Insets(0, 0, 8, 0));
//        VBox.setMargin(timeCreateMeeting2, new Insets(2, 0, 15, 0));
//
//
//        HBox meetingTodayFontend = new HBox(5);
//        meetingTodayFontend.getChildren().addAll(meetingToday1, meetingToday2);
//        HBox.setHgrow(meetingToday1, Priority.ALWAYS);
//        HBox.setHgrow(meetingToday2, Priority.ALWAYS);
//
//        meetLinks.getChildren().addAll(label2, meetingTodayFontend);

//        meetingToday.prefWidthProperty().bind(meetingBox.widthProperty().multiply(0.5));
//        meetingToday.setMaxWidth(Region.USE_PREF_SIZE); // Giữ đúng width theo prefWidth



        // Right part

        // Account
        HBox accountBox = new HBox(10);
        ImageView avatar = new ImageView(new Image(getClass().getResource("/images/avatar.jpg").toExternalForm()));
        avatar.setFitHeight(50);
        avatar.setFitWidth(50);
        Rectangle clip = new Rectangle(50, 50);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        avatar.setClip(clip);

        VBox accountInfor = new VBox();
        Label nameAccount = new Label("Le Thi Thu Hien");
        nameAccount.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
        Label emailAccount = new Label("hienltt.23it@vku.udn.vn");
        emailAccount.setFont(Font.font("Poppins", FontWeight.NORMAL, 15));
        accountInfor.getChildren().addAll(nameAccount, emailAccount);

        ImageView more = new ImageView(new Image(getClass().getResource("/images/down.png").toExternalForm()));
        more.setFitWidth(16);
        more.setFitHeight(16);

        accountBox.getChildren().addAll(avatar, accountInfor, more);
        accountBox.setStyle("-fx-background-color: #fff; -fx-background-radius: 10; -fx-border-radius: 10;");
        accountBox.setPadding(new Insets(3));
        accountBox.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(accountBox, new Insets(15, 20, 0, 0));
        HBox.setMargin(accountInfor, new Insets(10, 0, 0, 0));
        HBox.setMargin(avatar, new Insets(4, 0, 0, 0));
        HBox.setMargin(more, new Insets(20, 8, 0, 60));

        // Tạo popup menu
        VBox popupMenu = new VBox();
        popupMenu.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 6; -fx-background-radius: 6;");
        popupMenu.setPadding(new Insets(7));
        popupMenu.setSpacing(10);
        popupMenu.setPrefWidth(accountBox.getWidth());
        popupMenu.prefWidthProperty().bind(accountBox.widthProperty());

        Label manage = new Label("Manage your account settings");
        manage.setFont(Font.font("Poppins", FontWeight.NORMAL, 12));
        manage.setStyle("-fx-text-fill: gray");

        popupMenu.getChildren().addAll(manage);

        // Tạo Popup
        Popup popup = new Popup();
        popup.getContent().add(popupMenu);
        popup.setAutoHide(true);


        // Bắt sự kiện click vào accountBox
        accountBox.setOnMouseClicked(e -> {
            if (!popup.isShowing()) {
                popup.show(
                        accountBox.getScene().getWindow(),
                        accountBox.localToScreen(0, accountBox.getHeight()).getX(),
                        accountBox.localToScreen(0, accountBox.getHeight()).getY()
                );
            } else {
                popup.hide();
            }
        });

        // Recent meetings
        VBox recentMeetinContainer = new VBox(10);
//        recentMeetinContainer.setStyle("-fx-background-color: #fff; -fx-background-radius: 10; -fx-border-radius:10");
//        recentMeetinContainer.setPadding(new Insets(15, 0, 0, 10));
        recentMeetinContainer.setPadding(new Insets(15)); // khoảng cách bên trong
        recentMeetinContainer.setStyle(
                "-fx-background-color: #fff;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;"
        );
        recentMeetinContainer.setMaxHeight(Double.MAX_VALUE); // full width

        Label label3 = new Label("Recent meetings");
        label3.setFont(Font.font("Poppins", FontWeight.BOLD, 20));

        List<Meeting> recentMeetings = Arrays.asList(
                new Meeting(
                        "Software Quality Assessment and Improvement",
                        "25-11-2025 02:25 PM",
                        "Nguyen Huu Quynh Anh",
                        "/images/avatar2.jpg"
                ),
                new Meeting(
                        "AI-Powered Automation in Enterprise Applications",
                        "27-11-2025 01:00 PM",
                        "Tran Bao Minh",
                        "/images/avatar3.jpg"
                ),
                new Meeting(
                        "Machine Learning for Real-time Decision Making",
                        "28-11-2025 09:30 AM",
                        "Le Thi Thu Ha",
                        "/images/avatar4.jpg"
                ),
                new Meeting(
                        "Company IT Strategy Review",
                        "29-11-2025 7:30 AM",
                        "Le Thi Thu Ha",
                        "/images/avatar4.jpg"
                )
        );

        VBox meetingListContainer = new VBox(15);
        meetingListContainer.setStyle("-fx-background-color: #fff");
        if (recentMeetings.isEmpty()) {
            VBox noRecentMeetingBox = new VBox(12);
            Image noRecentMeetingImage = new Image(getClass().getResource("/images/empty_box.png").toExternalForm());
            ImageView noRecentMeetingImageView = new ImageView(noRecentMeetingImage);
            noRecentMeetingImageView.setFitHeight(50);
            noRecentMeetingImageView.setFitWidth(50);

            Label noRecentMeetingLabel = new Label("No recent meetings to display");
            noRecentMeetingLabel.setFont(Font.font("Poppins", FontWeight.NORMAL, 13));
            noRecentMeetingLabel.setStyle("-fx-text-fill: gray");
            noRecentMeetingLabel.setWrapText(true);
            noRecentMeetingLabel.prefWidthProperty().bind(recentMeetinContainer.widthProperty());

            noRecentMeetingBox.getChildren().addAll(noRecentMeetingImageView, noRecentMeetingImageView);
            noRecentMeetingBox.setAlignment(Pos.CENTER);
            noRecentMeetingBox.setPadding(new Insets(30, 100, 30, 100));
            noRecentMeetingBox.setStyle("-fx-background-color: #fff; -fx-background-radius: 15;");

            meetingListContainer.getChildren().add(noRecentMeetingBox);
        }
        for (Meeting m : recentMeetings) {
            meetingListContainer.getChildren().add(createRecentMeetingItem(m));
        }

        ScrollPane scroll = new ScrollPane(meetingListContainer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #fff;");
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        recentMeetinContainer.getChildren().addAll(label3, scroll);
        scroll.prefHeightProperty().bind(rightContainer.heightProperty());

        // Neu khong co cuoc hop gan day
//        recentMeetinContainer.getChildren().addAll(label3, meetingListContainer);
//        meetingListContainer.prefHeightProperty().bind(rightContainer.heightProperty());

        rightContainer.getChildren().addAll(accountBox, recentMeetinContainer);
        VBox.setMargin(label3, new Insets(0, 0, 15, 0));
        VBox.setMargin(recentMeetinContainer, new Insets(20, 20, 0, 0));

        // Ratio 7:3
        leftContainer.prefWidthProperty().bind(container.widthProperty().multiply(0.7));
        rightContainer.prefWidthProperty().bind(container.widthProperty().multiply(0.3));
        rightContainer.prefHeightProperty().bind(leftContainer.heightProperty());
        container.getChildren().addAll(leftContainer, rightContainer);
        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        getChildren().add(scrollPane);
    }

    private void setupToggleButton(ToggleButton btn, Image defaultIcon, Image activeIcon,  String normalStyle, String hoverStyle) {
        ImageView icon = new ImageView(defaultIcon);
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        btn.setGraphic(icon);
        btn.setContentDisplay(ContentDisplay.LEFT);
        btn.setStyle(normalStyle);

        // Hover
        btn.setOnMouseEntered(e -> {
            btn.setStyle(hoverStyle);
            ((ImageView)btn.getGraphic()).setImage(activeIcon);

        });

        btn.setOnMouseExited(e -> {
            if (!btn.isSelected()) {
                btn.setStyle(normalStyle);
                ((ImageView)btn.getGraphic()).setImage(defaultIcon);
            }
        });

        // Selected
        btn.selectedProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                btn.setStyle(hoverStyle);
                ((ImageView)btn.getGraphic()).setImage(activeIcon);
            } else {
                btn.setStyle(normalStyle);
                ((ImageView)btn.getGraphic()).setImage(defaultIcon);
            }
        });
    }

    private VBox createNoMeetingBox() {
        VBox noMeetingBox = new VBox(12);
        noMeetingImage = new Image(getClass().getResource("/images/empty.png").toExternalForm());
        ImageView noMeetingImageView = new ImageView(noMeetingImage);
        noMeetingImageView.setFitHeight(50);
        noMeetingImageView.setFitWidth(50);

        Label noMeetingLabel = new Label("No meetings found. Create one to get started!");
        noMeetingLabel.setFont(Font.font("Poppins", FontWeight.NORMAL, 15));
        noMeetingLabel.setStyle("-fx-text-fill: gray");
        noMeetingLabel.setWrapText(true);


        noMeetingBox.getChildren().addAll(noMeetingImageView, noMeetingLabel);
        noMeetingBox.setAlignment(Pos.CENTER);
        noMeetingBox.setPadding(new Insets(30, 100, 30, 100));
        noMeetingBox.setStyle("-fx-background-color: #fff; -fx-background-radius: 15;");

        return noMeetingBox;
    }

    public void addMeetingToday(String title, long timestamp) {
        //  Lần đầu tiên có meeting
        if (meetingTodayContainer.getChildren().isEmpty()) {
            noMeetingBox.setVisible(false);
            noMeetingBox.setManaged(false);

            meetingTodayContainer.setVisible(true);
            meetingTodayContainer.setManaged(true);
        }

        VBox meetingBox = createMeetingBox(title, timestamp);
        meetingTodayContainer.getChildren().add(meetingBox);
    }

    // Tao UI cho 1 item meeting today
    private VBox createMeetingBox(String title, long timestamp) {
        VBox meetingBox = new VBox(10);
        meetingBox.setPadding(new Insets(15));
        meetingBox.setStyle("-fx-background-color: #fff; -fx-background-radius: 7;");

        Label name = new Label(title);
        name.setFont(Font.font("Poppins", FontWeight.BOLD, 15));

        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

        HBox timeBox = new HBox(5);
        ImageView icon = new ImageView(
                new Image(getClass().getResource("/images/calendar_2.png").toExternalForm())
        );
        icon.setFitWidth(20);
        icon.setFitHeight(20);

        Label timeLabel = new Label(sdf.format(date));
        timeLabel.setStyle("-fx-text-fill: gray;");
        timeBox.getChildren().addAll(icon, timeLabel);

        HBox btnBox = new HBox(5);
        Button join = new Button("Join now");
        Button share = new Button("Share meeting");

        String btnMeetingTodayStyle = """
                -fx-background-color: #F2E7FF;
                -fx-border-raius: 10;
                -fx-padding: 6 12;
                -fx-text-fill: #000;
                -fx-background-radius: 10;
                -fx-font-size: 15;
                -fx-font-weight: bold;
                -fx-graphic-text-gap: 6;
                -fx-alignment: CENTER;
                -fx-min-height: 28;
                """;

        String btnMeetingTodayStyleHover = """
            -fx-background-color: #D6B4FF;
            -fx-border-raius: 10;
            -fx-padding: 6 12;
            -fx-text-fill: #000;
            -fx-background-radius: 10;
            -fx-font-size: 15;
            -fx-font-weight: bold;
            -fx-graphic-text-gap: 6;
            -fx-alignment: CENTER;
            -fx-min-height: 28;
            """;

        join.setStyle(btnMeetingTodayStyle);
        share.setStyle(btnMeetingTodayStyle);

        join.setOnMouseEntered(e -> join.setStyle(btnMeetingTodayStyleHover));
        join.setOnMouseExited(e -> join.setStyle(btnMeetingTodayStyle));
        share.setOnMouseEntered(e -> share.setStyle(btnMeetingTodayStyleHover));
        share.setOnMouseExited(e -> share.setStyle(btnMeetingTodayStyle));

        join.prefWidthProperty().bind(btnBox.widthProperty().multiply(0.5));
        share.prefWidthProperty().bind(btnBox.widthProperty().multiply(0.5));
        btnBox.getChildren().addAll(join, share);

        meetingBox.getChildren().addAll(name, timeBox, btnBox);
        VBox.setMargin(name, new Insets(0, 0, 8, 0));
        VBox.setMargin(timeBox, new Insets(2, 0, 15, 0));

        meetingBox.setOnMouseEntered(e ->
                meetingBox.setStyle("-fx-background-color: #fff; -fx-border-color: #801AFB; -fx-border-radius: 7;")
        );
        meetingBox.setOnMouseExited(e ->
                meetingBox.setStyle("-fx-background-color: #fff; -fx-border-color: transparent; -fx-border-radius: 7;")
        );

        return meetingBox;
    }


    // Tao UI cho 1 item recent meeting
    private VBox createRecentMeetingItem(Meeting recentMeeting) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #ccc; -fx-border-width: 1; -fx-background-color: #fff;");

        // Title
        Label titleLabel = new Label(recentMeeting.getTitle());
        titleLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
        titleLabel.setWrapText(true);

        // Time
        HBox timeBox = new HBox(5);
        ImageView icon = new ImageView(new Image(getClass().getResource("/images/calendar_2.png").toExternalForm()));
        icon.setFitWidth(20);
        icon.setFitHeight(20);

        Label time = new Label(recentMeeting.getTime());
        time.setFont(Font.font("Poppins", FontWeight.BOLD, 13));
        time.setStyle("-fx-text-fill: gray");
        timeBox.getChildren().addAll(icon, time);

        // Host
        HBox hostBox = new HBox(10);
        ImageView avatar = new ImageView(new Image(getClass().getResource(recentMeeting.getAvatarPath()).toExternalForm()));
        avatar.setFitWidth(24);
        avatar.setFitHeight(24);
        avatar.setClip(new Circle(12, 12, 12));

        Label host = new Label(recentMeeting.getHostName());
        host.setFont(Font.font("Poppins", FontWeight.BOLD, 15));
        host.setStyle("-fx-text-fill: gray");
        hostBox.getChildren().addAll(avatar, host);

        // Add nodes
        box.getChildren().addAll(titleLabel, timeBox, hostBox);

        return box;
    }



}
