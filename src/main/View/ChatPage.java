package main.View;
import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class ChatPage extends StackPane {
    private StackPane contentPane;

    public ChatPage(StackPane contentPane) {
        this.contentPane = contentPane;
        // ============================================ LEFT PANEL =========================================
        HBox searchBar = createSearchBar();

        // ================== PEOPLE ==================
        Label peopleLabel = new Label("People");
        peopleLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 20));
        peopleLabel.setPadding(new Insets(0, 0, 7, 10));


        ListView<HBox> peopleList = new ListView<>();
        peopleList.getItems().addAll(
                userGroupItem("./images/img.png","F4", "Just finished", "Are you okay?", "read"),
                userGroupItem("./images/logo.png","VKU04", "1 mins ago", "What are u doing?", "unread"),
                userGroupItem("./images/unread.png","LTMangHuhu", "15 mins ago", "I think it's a good chance", "read"),
                userGroupItem("./images/img.png","Quynh Anh Nguyen", "1 hr ago", "Are you okay?", "read"),
                userGroupItem("./images/img.png","Thu Hien", "1 hr ago", "Helloooo?", "read"),
                userGroupItem("./images/img.png","Minh Anh", "1 hr ago", "REp me pls", "read"),
                userGroupItem("./images/img.png","Cam Anh", "2 hrs ago", "I love you", "unread"),
                userGroupItem("./images/img.png","Nhat Uyen", "4 hrs ago", "I miss you", "unread"),
                userGroupItem("./images/img.png","Quynh Nhu", "15 hrs ago", "Heyyyy", "unread"),
                userGroupItem("./images/img.png","Anh Thu", "1 day ago", "OMG", "unread")
        );
        peopleList.setPrefWidth(200);

//        double rowHeight = 34 + 10*2;
        double rowHeight = 34 + 10 + 1;

        // Tối đa hiển thị 6 items
        int maxVisibleRows = 6;
        int numItems = peopleList.getItems().size();
        int visibleRows = Math.min(numItems, maxVisibleRows);
        // Set fixed cell size để ListView hiển thị đúng số dòng
        peopleList.setFixedCellSize(rowHeight);
        peopleList.setPrefHeight(visibleRows * peopleList.getFixedCellSize());
        peopleList.setMaxHeight(visibleRows * peopleList.getFixedCellSize());
        peopleList.setMinHeight(visibleRows * peopleList.getFixedCellSize());

        // Xóa background và viền của ListView
        peopleList.setStyle("""
            -fx-background-color: transparent; 
            -fx-background-insets: 0; 
            -fx-padding: 0;
            -fx-border-width: 0; 
        """);

        // CellFactory để padding đẹp, background trong suốt
        peopleList.setCellFactory(lv -> new ListCell<HBox>() {
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    //setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(item);

                    setPrefWidth(0);
                    setMaxWidth(Double.MAX_VALUE);
                    // Padding trong cell để tạo khoảng cách
                    setPadding(new Insets(10, 10, 5, 10)); // top, right, bottom, left


                    // ================== Xử lý chọn item ==================
                    selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            setBackground(new Background(new BackgroundFill(
                                    Color.web("#e6e6fa"), new CornerRadii(10), Insets.EMPTY)));
                        } else {
//                            setBackground(new Background(new BackgroundFill(
//                                    Color.TRANSPARENT, new CornerRadii(10),Insets.EMPTY)));
                            setBackground(new Background(new BackgroundFill(
                                    Color.web("#F8F7FF"), new CornerRadii(10), Insets.EMPTY))); // màu mặc định
                        }
                    });

                    // Khởi tạo màu nền mặc định khi chưa chọn
                    if (!isSelected()) {
                        setBackground(new Background(new BackgroundFill(
                                Color.web("#F8F7FF"), new CornerRadii(10), Insets.EMPTY)));
                    }

                    // Giữ chữ màu đen cho tất cả label bên trong HBox
                    item.getChildren().forEach(node -> {
                        if (node instanceof VBox) {
                            ((VBox) node).getChildren().forEach(inner -> {
                                if (inner instanceof Label) {
                                    ((Label) inner).setTextFill(Color.BLACK);
                                }
                            });
                        } else if (node instanceof Label) {
                            ((Label) node).setTextFill(Color.BLACK);
                        }
                    });

                    // TẮT highlight mặc định khi chọn
                    pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), false);
                    focusedProperty().addListener((obs, oldVal, newVal) -> {
                        pseudoClassStateChanged(PseudoClass.getPseudoClass("focused"), false);
                    });
                }
            }
        });

        // Thêm style trực tiếp vào ListView
        peopleList.setStyle("""
            -fx-background-color: #F8F7FF; 
            -fx-background-insets: 0; 
            -fx-padding: 0;
            -fx-border-width: 0;
        """);

        // Thêm pseudo-class style cho ScrollBar
        String scrollStyle = """
            .list-view .scroll-bar:vertical {
                -fx-background-color: transparent;  /* nền scrollbar */
                -fx-pref-width: 8px;               /* độ rộng scrollbar */
            }
        
            .list-view .scroll-bar:vertical .thumb {
                 -fx-background-color: rgba(138,43,226,0.7);
                -fx-background-insets: 2;
                -fx-background-radius: 4;          /* bo tròn thumb */
            }
        
            .list-view .scroll-bar:vertical .track {
                -fx-background-color: transparent;  /* nền track */
            }
        
            .list-view .scroll-bar:horizontal {
                -fx-background-color: transparent;
                -fx-pref-height: 6px;
            }
        
            .list-view .scroll-bar:horizontal .thumb {
                -fx-background-color: rgba(138,43,226,0.7);
                -fx-background-radius: 3;
            }
        
            .list-view .scroll-bar:horizontal .track {
                -fx-background-color: transparent;
            }
        """;

        // Add stylesheet trực tiếp từ chuỗi
        peopleList.getStylesheets().add("data:text/css," + scrollStyle.replace("\n", ""));


        // VBox bo tròn, padding và margin giống groupsBox
        VBox peopleBox = new VBox(0, peopleLabel, peopleList);
        peopleBox.setPadding(new Insets(10,5,0,5)); // (top, right, bottom, left)
        peopleBox.setPrefWidth(350);
        peopleBox.setPrefHeight(333);
        // cho list chiếm hết không gian còn lại
        VBox.setVgrow(peopleList, Priority.ALWAYS);

        // Xóa fixed cell size, để ListView tự co giãn
        peopleList.setFixedCellSize(-1);
        peopleList.setPrefHeight(Region.USE_COMPUTED_SIZE);
        peopleList.setMaxHeight(Double.MAX_VALUE);
        peopleList.setMinHeight(0);

        peopleBox.setStyle("""
            -fx-background-color: #F8F7FF; 
            -fx-background-radius: 15; 
            -fx-border-radius: 15; 
            /*-fx-border-color: #8b008b;
            -fx-border-width: 2;*/
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0.5, 0, 2);
        """);

        // ================== GROUP ==================
        Label groupsLabel = new Label("Groups");
        groupsLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 20));
        groupsLabel.setPadding(new Insets(0, 0, 7, 10));

        ListView<HBox> groupsList = new ListView<>();
        //groupsList.getItems().addAll("Friends Forever", "Halo Meet", "Pretty Girls");
        groupsList.getItems().addAll(
                userGroupItem("./images/img.png","F4", "Just finished", "Are you okay?", "read"),
                userGroupItem("./images/logo.png","VKU04", "1 mins ago", "What are u doing?", "unread"),
                userGroupItem("./images/unread.png","LTMangHuhu", "2 hrs ago", "I think it's a good chance", "read")
        );

        //double rowHeightGroup = 34 + 10*2; // HBox cao 34 + padding top/bottom 10px
        double rowHeightGroup = 34 + 10 + 1; // content + padding top + padding bottom + separator

        groupsList.setFixedCellSize(rowHeightGroup);
        int maxVisibleRowsGroup = 3; // tối đa 3 item hiển thị
        int numItemsGroup = groupsList.getItems().size();
        int visibleRowsGroup = Math.min(numItemsGroup, maxVisibleRowsGroup);
        // set prefHeight/minHeight/maxHeight để ListView hiển thị đúng
        groupsList.setPrefHeight(visibleRowsGroup * groupsList.getFixedCellSize());
        groupsList.setMinHeight(visibleRowsGroup * groupsList.getFixedCellSize());
        groupsList.setMaxHeight(visibleRowsGroup * groupsList.getFixedCellSize());


        groupsList.setStyle("""
            -fx-background-color: transparent;
            -fx-background-insets: 0;
            -fx-padding: 0;
            -fx-border-width: 0;
        """);

        groupsList.setCellFactory(lv -> new ListCell<HBox>() {
            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(item);

                    setPrefWidth(0);
                    setMaxWidth(Double.MAX_VALUE);

                    // Padding trong cell để tạo khoảng cách
                    setPadding(new Insets(10, 10, 5, 10)); // top, right, bottom, left
                    setBackground(new Background(new BackgroundFill(
                            Color.web("#E6E6FA"), new CornerRadii(10), Insets.EMPTY)));

                    // ================== Xử lý chọn item ==================
                    selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            setBackground(new Background(new BackgroundFill(
                                    Color.web("#e6e6fa"), new CornerRadii(10), Insets.EMPTY)));
                        } else {
//                            setBackground(new Background(new BackgroundFill(
//                                    Color.TRANSPARENT, new CornerRadii(10),Insets.EMPTY)));
                            setBackground(new Background(new BackgroundFill(
                                    Color.web("#F8F7FF"), new CornerRadii(10), Insets.EMPTY))); // màu mặc định
                        }
                    });

                    // Khởi tạo màu nền mặc định khi chưa chọn
                    if (!isSelected()) {
                        setBackground(new Background(new BackgroundFill(
                                Color.web("#F8F7FF"), new CornerRadii(10), Insets.EMPTY)));
                    }


                    // Giữ chữ màu đen cho tất cả label bên trong HBox
                    item.getChildren().forEach(node -> {
                        if (node instanceof VBox) {
                            ((VBox) node).getChildren().forEach(inner -> {
                                if (inner instanceof Label) {
                                    ((Label) inner).setTextFill(Color.BLACK);
                                }
                            });
                        } else if (node instanceof Label) {
                            ((Label) node).setTextFill(Color.BLACK);
                        }
                    });

                    // TẮT highlight mặc định khi chọn
                    pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), false);
                    focusedProperty().addListener((obs, oldVal, newVal) -> {
                        pseudoClassStateChanged(PseudoClass.getPseudoClass("focused"), false);
                    });

                }
            }
        });

        VBox groupsBox = new VBox(0, groupsLabel, groupsList);
        groupsList.setPrefWidth(Double.MAX_VALUE);
        groupsList.setMinWidth(groupsBox.getPrefWidth());

        groupsBox.setPadding(new Insets(10,5,0,5)); // (top, right, bottom, left)
//        VBox.setMargin(groupsBox, new Insets(20, 0, 0, 20));
        groupsBox.setPrefWidth(350);
        groupsBox.setPrefHeight(200);
        // cho list chiếm hết không gian còn lại
        VBox.setVgrow(groupsList, Priority.ALWAYS);

        // Xóa fixed cell size, để ListView tự co giãn
        groupsList.setFixedCellSize(-1);
        groupsList.setPrefHeight(Region.USE_COMPUTED_SIZE);
        groupsList.setMaxHeight(Double.MAX_VALUE);
        groupsList.setMinHeight(0);

        // Thêm style trực tiếp vào ListView
        groupsList.setStyle("""
            -fx-background-color: #F8F7FF; 
            -fx-background-insets: 0; 
            -fx-padding: 0;
            -fx-border-width: 0;
        """);

        // Add stylesheet trực tiếp từ chuỗi
        groupsList.getStylesheets().add("data:text/css," + scrollStyle.replace("\n", ""));


        groupsBox.setStyle("""
            -fx-background-color: #F8F7FF; 
            -fx-background-radius: 15; 
            -fx-border-radius: 15; 
            /*-fx-border-color: #8b008b;
            -fx-border-width: 2;*/
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0.5, 0, 2);
        """);

        VBox leftPane = new VBox(15,searchBar,groupsBox, peopleBox);
        leftPane.setPadding(new Insets(0,0,0,0)); // (top, right, bottom, left)
        //VBox.setMargin(leftPane, new Insets(20, 0, 0, 20));


        // ============================================ Center Pane: Chat Area =========================================
        // ================= HEADER =================
        chatHeaderAvatar = new ImageView();
        chatHeaderAvatar.setFitWidth(50);
        chatHeaderAvatar.setFitHeight(50);
        //chatHeaderAvatar.setClip(new Circle(15, 15, 15)); // avatar tròn
        // Clip để avatar tròn
        Circle clip = new Circle(25, 25, 25); // bán kính = 25
        chatHeaderAvatar.setClip(clip);

        chatHeaderName = new Label("Select a friend to chat");
        chatHeaderName.setFont(Font.font("Poppins", FontWeight.BOLD, 18));
        chatHeaderName.setTextFill(Color.BLACK);

        chatHeader = new HBox(10, chatHeaderAvatar, chatHeaderName);
        //chatHeader.setPadding(new Insets(10,15,10,15));
        chatHeader.setAlignment(Pos.CENTER_LEFT);
        chatHeader.setPadding(Insets.EMPTY); // bỏ padding ở HBox chính

        //iconMessBox = new HBox(10);
        ImageView iconCall = new ImageView(new Image("./images/chatPage/phone.png", false));
        iconCall.setFitWidth(25);
        iconCall.setFitHeight(25);

        ImageView iconVideo = new ImageView(new Image("./images/chatPage/video-icon.png", false));
        iconVideo.setFitWidth(30);
        iconVideo.setFitHeight(30);

        ImageView iconShowMore = new ImageView(new Image("./images/chatPage/showMore.png", false));
        iconShowMore.setFitWidth(26);
        iconShowMore.setFitHeight(26);

        // ===== BUTTONS WRAPPING ICONS =====
        Button callBtn = createIconButton(iconCall);
        Button videoBtn = createIconButton(iconVideo);
        Button moreBtn = createIconButton(iconShowMore);

        //iconMessBox = new HBox(22, iconCall, iconVideo, iconShowMore);
        iconMessBox = new HBox(22, callBtn, videoBtn, moreBtn);
        iconMessBox.setAlignment(Pos.CENTER_RIGHT);

        // Nếu muốn đưa iconBox vào header chatHeaderBox
        HBox headerContent = new HBox(chatHeader, iconMessBox);
        HBox.setHgrow(chatHeader, Priority.ALWAYS); // header giãn hết khoảng trống còn lại
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(17, 0, 15, 0));

        // Line
        Region line = new Region();
        line.setPrefHeight(1);
        line.setStyle("-fx-background-color: lightgray;");
        line.prefWidthProperty().bind(headerContent.widthProperty());


        // ================= VBox chứa headerContent + line =================
        VBox chatHeaderBox = new VBox(headerContent, line);
        chatHeaderBox.setPadding(new Insets(0,15,0,15));
        chatHeaderBox.setSpacing(0);
        //chatHeaderBox.setPadding(Insets.EMPTY);



        // Chat VBox (nơi chứa tin nhắn)
        chatVBox = new VBox(10);
        chatVBox.setPadding(new Insets(10));
        // ScrollPane cho chat
        scrollPane = new ScrollPane(chatVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Input Field + Send Button
        TextField inputField = new TextField();
        inputField.setPromptText("Type your message here...");
        inputField.setPrefHeight(40);
//        inputField.setStyle("""
//            -fx-background-radius: 20;
//            -fx-border-radius: 20;
//            -fx-border-color: #8b008b;
//            -fx-border-width: 1;
//            -fx-padding: 0 15 0 15;
//        """);
        // Style mặc định
        String normalStyle = """
            -fx-background-color: white;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
       
            -fx-padding: 0 15 0 15;
            -fx-focus-color: transparent;      /* loại bỏ viền xanh khi focus */
            -fx-faint-focus-color: transparent; /* loại bỏ ánh sáng focus mờ */
        """;

        // Style hover + scale
        String hoverStyle = """
            -fx-background-color: white;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-border-width: 1;
            -fx-padding: 0 15 0 15;
            -fx-focus-color: transparent;
            -fx-faint-focus-color: transparent;
             -fx-effect: dropshadow(gaussian, rgba(255,255,255,0.7), 10, 0.5, 0, 0);
        """;

        inputField.setStyle(normalStyle);

        inputField.setOnMouseEntered(e -> {
            inputField.setStyle(hoverStyle);
            inputField.setScaleX(0.99); // phóng to x nhẹ
            inputField.setScaleY(0.99); // phóng to y nhẹ
        });

        inputField.setOnMouseExited(e -> {
            inputField.setStyle(normalStyle);
            inputField.setScaleX(1);
            inputField.setScaleY(1);
        });

        ImageView sendIcon = new ImageView(new Image("./images/chatPage/send.png", false));
        sendIcon.setFitWidth(28);
        sendIcon.setFitHeight(28);

        Button sendBtn = new Button();
        sendBtn.setGraphic(sendIcon);
        sendBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        sendBtn.setOnMouseEntered(e -> {
            sendIcon.setScaleX(0.9);
            sendIcon.setScaleY(0.9);
            sendIcon.setOpacity(0.7);
        });

        sendBtn.setOnMouseExited(e -> {
            sendIcon.setScaleX(1);
            sendIcon.setScaleY(1);
            sendIcon.setOpacity(1);
        });

        sendBtn.setOnAction(e -> {
            String text = inputField.getText().trim();
            if(!text.isEmpty()){
                addMessage(text, true); // true = của mình
                inputField.clear();
            }
        });

        HBox inputBox = new HBox(10, inputField, sendBtn);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);
        // Cho inputField giãn ra hết
        HBox.setHgrow(inputField, Priority.ALWAYS);


        // Chat Area tổng (dùng VBox để xếp Header + ScrollPane + Input)
        VBox chatArea = new VBox(chatHeaderBox, scrollPane, inputBox);
        chatArea.setPrefWidth(400);
        chatArea.setMinHeight(Region.USE_COMPUTED_SIZE);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        HBox.setMargin(chatArea, new Insets(0, 0, 0, 20));
        chatArea.setStyle("""
            -fx-background-color: #F8F7FF;
            -fx-background-radius: 15;
            -fx-border-radius: 15;
        """);

        peopleList.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                isGroup = false;
                updateChatHeader(newSel);
                updateShowMorePanel(newSel); // 🔑 cập nhật panel
                chatVBox.getChildren().clear();
            }
        });

        groupsList.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                isGroup=true;
                updateChatHeader(newSel);
                updateShowMorePanel(newSel); // 🔑 cập nhật panel
                chatVBox.getChildren().clear();
            }
        });


        HBox mainChatContainer = new HBox(chatArea); // ban đầu chỉ add chatArea
        HBox.setHgrow(chatArea, Priority.ALWAYS);
        HBox.setHgrow(mainChatContainer, Priority.ALWAYS);
        mainChatContainer.setStyle("""
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0.5, 0, 2);
                
                """);

        //mainChatContainer.getChildren().add(chatArea);

        // ===================================== SHOW MORE PANEL ================================
        VBox showMorePanel = new VBox();
        showMorePanel.setPrefWidth(280);
        showMorePanel.setMinWidth(280);
        showMorePanel.setStyle("""
            -fx-background-color: #ffffff;
            /*-fx-background-radius: 15;
            -fx-border-radius: 15;*/
            /*-fx-border-color: #8b008b;
            -fx-border-width: 2;*/
            -fx-background-radius: 0 15 15 0;  
            -fx-border-radius: 0 15 15 0;
        """);

        // ====== SCROLLABLE SHOW MORE PANEL ======
        ScrollPane scrollMorePane = new ScrollPane(showMorePanel);
        scrollMorePane.setFitToWidth(true);

        //HBox.setMargin(scrollMorePane, new Insets(0, 0, 0, 20));
        scrollMorePane.setStyle("""
            -fx-background-color: white;
            /*-fx-border-color: #8b008b;
            -fx-border-width: 2;*/
            /*-fx-background-radius: 15;
            -fx-border-radius: 15;*/
            -fx-padding: 0;
            -fx-background-radius: 0 15 15 0;  
            -fx-border-radius: 0 15 15 0;
        """);
        showMorePanel.setStyle("""
            -fx-background-color: transparent;
        """);


        scrollMorePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollMorePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollMorePane.setFitToHeight(true);
        VBox.setVgrow(scrollMorePane, Priority.ALWAYS);


        scrollMorePane.setVisible(false);
        scrollMorePane.setManaged(false);

        // Tạo các node
        showMoreAvatar = new ImageView();
        showMoreAvatar.setFitWidth(100);
        showMoreAvatar.setFitHeight(100);
        VBox.setMargin(showMoreAvatar, new Insets(50, 0, 0, 0));

        // Bo tròn avatar
        Circle clipAvtShowMore = new Circle(50, 50, 50); // bán kính = 50 -> tròn đúng 100x100
        showMoreAvatar.setClip(clipAvtShowMore);

        showMoreName = new Label("Friend Name");
        showMoreName.setFont(Font.font("Poppins", FontWeight.BOLD, 16));
        VBox.setMargin(showMoreName, new Insets(0, 0, -5, 0));
        showMoreStatus = new Label("Active now");
        showMoreStatus.setFont(Font.font(12));
        showMoreStatus.setTextFill(Color.GRAY);


        ImageView viewProfIcon = new ImageView(new Image("./images/chatPage/profile.png", false));
        viewProfIcon.setFitWidth(27);
        viewProfIcon.setFitHeight(27);

        ImageView notifIcon = new ImageView(new Image("./images/chatPage/notif.png", false));
        notifIcon.setFitWidth(26);
        notifIcon.setFitHeight(26);
        ImageView searchChatIcon = new ImageView(new Image("./images/chatPage/search-chat.png", false));
        searchChatIcon.setFitWidth(26);
        searchChatIcon.setFitHeight(26);


        // ===== BUTTONS WRAPPING ICONS =====
        Button viewProf = createIconButton(viewProfIcon);
        viewProf.setPadding(new Insets(10)); // tạo khoảng cách xung quanh icon
        viewProf.setStyle("""
            -fx-background-color: #f0f0f0;
            -fx-background-radius: 50; 
            -fx-cursor: hand;
        """);
        Button notif = createIconButton(notifIcon);
        notif.setPadding(new Insets(10));
        notif.setStyle("""
            -fx-background-color: #f0f0f0;
            -fx-background-radius: 50; 
            -fx-cursor: hand;
        """);
        Button searchChat = createIconButton(searchChatIcon);
        searchChat.setPadding(new Insets(10));
        searchChat.setStyle("""
            -fx-background-color: #f0f0f0;
            -fx-background-radius: 50; 
            -fx-cursor: hand;
        """);

        iconChatInforBox = new HBox(18,viewProf, notif, searchChat);
        iconChatInforBox.setAlignment(Pos.CENTER);
        iconChatInforBox.setPadding(new Insets(22, 0,0,0));

        // VBox chứa tất cả

        // ======= Custom Expandable Section =======
        HBox sectionHeader = new HBox();
        Label headerLabel = new Label("Chat information");
        headerLabel.setFont(Font.font("Poppins", FontWeight.SEMI_BOLD, 14));
        ImageView arrowIcon = new ImageView(new Image("./images/chatPage/arrow_down.png"));
        arrowIcon.setFitWidth(16);
        arrowIcon.setFitHeight(16);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        sectionHeader.getChildren().addAll(headerLabel, spacer, arrowIcon);
        sectionHeader.setPadding(new Insets(12));
        sectionHeader.setStyle("-fx-background-color: #f4f4f4; -fx-cursor: hand; "
                + "-fx-background-radius: 8;");


        sectionContent.setSpacing(8);
        sectionContent.setPadding(new Insets(10, 12, 12, 12));
        sectionContent.setVisible(false);
        sectionContent.setManaged(false);

        sectionHeader.setOnMouseClicked(e -> {
            boolean isVisible = sectionContent.isVisible();
            sectionContent.setVisible(!isVisible);
            sectionContent.setManaged(!isVisible);

            arrowIcon.setRotate(isVisible ? 0 : -90); // rotated icon animation
        });

        // Wrap everything into a VBox
        VBox customSection = new VBox(sectionHeader, sectionContent);
        customSection.setSpacing(5);


        HBox sectionHeader1 = new HBox();
        Label headerLabel1 = new Label("Customize chat");
        headerLabel1.setFont(Font.font("Poppins", FontWeight.SEMI_BOLD, 14));
        ImageView arrowIcon1 = new ImageView(new Image("./images/chatPage/arrow_down.png"));
        arrowIcon1.setFitWidth(16);
        arrowIcon1.setFitHeight(16);

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        sectionHeader1.getChildren().addAll(headerLabel1, spacer1, arrowIcon1);
        sectionHeader1.setPadding(new Insets(12));
        sectionHeader1.setStyle("-fx-background-color: #f4f4f4; -fx-cursor: hand; "
                + "-fx-background-radius: 8;");

        VBox sectionContent1 = new VBox(
                new Label("Edit nickname")
        );
        sectionContent1.setSpacing(8);
        sectionContent1.setPadding(new Insets(10, 12, 12, 12));
        sectionContent1.setVisible(false);
        sectionContent1.setManaged(false);

        // Toggle show/hide when clicking header
        sectionHeader1.setOnMouseClicked(e -> {
            boolean isVisible = sectionContent1.isVisible();
            sectionContent1.setVisible(!isVisible);
            sectionContent1.setManaged(!isVisible);

            arrowIcon.setRotate(isVisible ? 0 : -90); // rotated icon animation
        });

        // Wrap everything into a VBox
        VBox customSection1 = new VBox(sectionHeader1, sectionContent1);
        customSection1.setSpacing(5);


        HBox sectionHeader2 = new HBox();
        Label headerLabel2 = new Label("Privacy and support");
        headerLabel2.setFont(Font.font("Poppins", FontWeight.SEMI_BOLD, 14));
        ImageView arrowIcon2 = new ImageView(new Image("./images/chatPage/arrow_down.png"));
        arrowIcon2.setFitWidth(16);
        arrowIcon2.setFitHeight(16);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        sectionHeader2.getChildren().addAll(headerLabel2, spacer2, arrowIcon2);
        sectionHeader2.setPadding(new Insets(12));
        sectionHeader2.setStyle("-fx-background-color: #f4f4f4; -fx-cursor: hand; "
                + "-fx-background-radius: 8;");

        VBox sectionContent2 = new VBox(
                new Label("Delete chat"),
                new Label("Block"),
                new Label("Turn off notifications")
        );
        sectionContent2.setSpacing(8);
        sectionContent2.setPadding(new Insets(10, 12, 12, 12));
        sectionContent2.setVisible(false);
        sectionContent2.setManaged(false);

        // Toggle show/hide when clicking header
        sectionHeader2.setOnMouseClicked(e -> {
            boolean isVisible = sectionContent2.isVisible();
            sectionContent2.setVisible(!isVisible);
            sectionContent2.setManaged(!isVisible);

            arrowIcon2.setRotate(isVisible ? 0 : -90); // rotated icon animation
        });

        // Wrap everything into a VBox
        VBox customSection2 = new VBox(sectionHeader2, sectionContent2);
        customSection2.setSpacing(5);


        HBox sectionHeader3 = new HBox();
        Label headerLabel3 = new Label("Media files and links");
        headerLabel3.setFont(Font.font("Poppins", FontWeight.SEMI_BOLD, 14));
        ImageView arrowIcon3 = new ImageView(new Image("./images/chatPage/arrow_down.png"));
        arrowIcon3.setFitWidth(16);
        arrowIcon3.setFitHeight(16);

        Region spacer3 = new Region();
        HBox.setHgrow(spacer3, Priority.ALWAYS);

        sectionHeader3.getChildren().addAll(headerLabel3, spacer3, arrowIcon3);
        sectionHeader3.setPadding(new Insets(12));
        sectionHeader3.setStyle("-fx-background-color: #f4f4f4; -fx-cursor: hand; "
                + "-fx-background-radius: 8;");



        GridPane mediaGrid = new GridPane();
        mediaGrid.setHgap(8);
        mediaGrid.setVgap(8);
        mediaGrid.setPadding(new Insets(10, 12, 12, 12));
        mediaGrid.setVisible(false);
        mediaGrid.setManaged(false);

        String[] imagePaths = {
                "./images/img.png",
                "./images/img.png",
                "./images/img.png",
                "./images/img.png",
                "./images/img.png"
        };

        int col = 0, row = 0;
        for (String path : imagePaths) {
            ImageView img = new ImageView(new Image(path, false));
            img.setFitWidth(70);
            img.setFitHeight(70);
            img.setPreserveRatio(true);
            img.setStyle("-fx-background-radius: 10; -fx-cursor: hand;");

            // Hover effect
            img.setOnMouseEntered(e -> img.setOpacity(0.8));
            img.setOnMouseExited(e -> img.setOpacity(1.0));

            mediaGrid.add(img, col, row);

            col++;
            if (col == 3) { // mỗi hàng 3 ảnh
                col = 0;
                row++;
            }
        }

        // Toggle show/hide when clicking header
        sectionHeader3.setOnMouseClicked(e -> {
            boolean isVisible = mediaGrid.isVisible();
            mediaGrid.setVisible(!isVisible);
            mediaGrid.setManaged(!isVisible);
            arrowIcon3.setRotate(isVisible ? 0 : -90);
        });

        // Wrap everything into a VBox
        VBox customSection3 = new VBox(sectionHeader3, mediaGrid);
        customSection3.setSpacing(5);

        VBox rightPanel = new VBox(
                customSection,
                customSection1,
                customSection2,
                customSection3
        );
        rightPanel.setSpacing(10);
        rightPanel.setPadding(new Insets(30, 0, 0, 0));


        showMorePanel.getChildren().addAll(showMoreAvatar, showMoreName, showMoreStatus, iconChatInforBox, rightPanel);
        showMorePanel.setSpacing(10);
        showMorePanel.setPadding(new Insets(20));
        showMorePanel.setAlignment(Pos.TOP_CENTER);
//        HBox.setMargin(showMorePanel, new Insets(0, 0, 0, 20));


        moreBtn.setOnAction(e -> {
            if (!mainChatContainer.getChildren().contains(scrollMorePane)) {
                mainChatContainer.getChildren().add(scrollMorePane);
                scrollMorePane.setVisible(true);
                scrollMorePane.setManaged(true);

                // Khi show panel, chỉ bo góc trái chatArea
                chatArea.setStyle("""
                    -fx-background-color: #F8F7FF;
                    -fx-background-radius: 15 0 0 15;  
                    -fx-border-radius: 15 0 0 15;
                """);
            } else {
                mainChatContainer.getChildren().remove(scrollMorePane);
                scrollMorePane.setVisible(false);
                scrollMorePane.setManaged(false);

                chatArea.setStyle("""
                    -fx-background-color: #F8F7FF;
                    -fx-background-radius: 15;
                    -fx-border-radius: 15;
                """);
            }
        });


        // ================================= ROOT ==================================
        //HBox root = new HBox(leftPane, chatArea);
        HBox root = new HBox(leftPane, mainChatContainer);

        String scrollMoreStyle = """
            .scroll-pane {
                -fx-background-color: transparent;
            }
        
            .scroll-pane .viewport {
                -fx-background-color: transparent;
            }
        
            .scroll-pane .scroll-bar:vertical {
                -fx-background-color: transparent;
                -fx-pref-width: 8px;
            }
        
            .scroll-pane .scroll-bar:vertical .thumb {
                -fx-background-color: rgba(138,43,226,0.7);  /* thiên tím hơn */
                -fx-background-insets: 2;
                -fx-background-radius: 4;
            }
        
            .scroll-pane .scroll-bar:vertical .track {
                -fx-background-color: transparent;
            }
        
            .scroll-pane .scroll-bar:horizontal {
                -fx-background-color: transparent;
                -fx-pref-height: 6px;
            }
        
            .scroll-pane .scroll-bar:horizontal .thumb {
                -fx-background-color: rgba(138,43,226,0.7);
                -fx-background-radius: 3;
            }
        
            .scroll-pane .scroll-bar:horizontal .track {
                -fx-background-color: transparent;
            }
        """;

        // Gắn style trực tiếp cho scene hoặc root
        root.getStylesheets().add("data:text/css," + scrollMoreStyle.replace("\n", ""));


        String styleBorderScroll = """
            .scroll-rounded .viewport {
                -fx-background-color: transparent;
            }
        """;
        scrollMorePane.getStyleClass().add("scroll-rounded");
        root.getStylesheets().add("data:text/css," + styleBorderScroll.replace("\n", ""));
        root.setStyle("-fx-background-color: #ffffff;");

        root.setPrefSize(800, 600);
        HBox.setHgrow(chatArea, Priority.ALWAYS);
        root.setPadding(new Insets(20));

      this.getChildren().add(root);
    }

    HBox createSearchBar() {
        // Icon kính lúp
        ImageView icon = new ImageView(new Image("images/chatPage/search-icon.png"));
        icon.setFitWidth(16);
        icon.setFitHeight(16);

        // TextField với placeholder
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Search");
        txtSearch.setStyle("""
        -fx-background-color: transparent;
        -fx-border-width: 0;
        -fx-font-size: 14px;
    """);

        // Container cho search bar
        HBox box = new HBox(10);
        box.setPadding(new Insets(8, 12, 8, 12));
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(icon, txtSearch);

        // Style: bo góc + màu nền
        box.setStyle("""
        -fx-background-color: #ffffff;
        -fx-background-radius: 30;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0.5, 0, 2);
    """);

        // Hover effect
        box.setOnMouseEntered(e -> {
            box.setScaleX(0.99);
            box.setScaleY(0.99);
            box.setStyle("""
            -fx-background-color: #E6E6FA;
            -fx-background-radius: 30;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0.5, 0, 2);
        """);
        });

        box.setOnMouseExited(e -> {
            box.setScaleX(1); // quay về bình thường
            box.setScaleY(1);
            box.setStyle("""
            -fx-background-color: #ffffff;
            -fx-background-radius: 30;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0.5, 0, 2);
        """);
        });

        // Khi focus vào textfield → highlight
        txtSearch.focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (isFocused) {
                box.setStyle("""
                -fx-background-color: #ffffff;
                -fx-background-radius: 30;
                -fx-border-radius: 30;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0.5, 0, 2);
            """);
            } else {
                box.setStyle("""
                -fx-background-color: #ffffff;
                -fx-background-radius: 30;
                -fx-border-radius: 30;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 10, 0.5, 0, 2);
            """);
            }
        });

        return box;
    }

    HBox userGroupItem(String avatarPath, String name, String timeMsg, String textMsg, String isRead) {

        // Avatar hình tròn
        Image img = new Image(avatarPath);
        Circle avatar = new Circle(18); // bán kính 25px
        avatar.setFill(new ImagePattern(img));

        // Name + Time
        Label lblName = new Label(name);
        lblName.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
        Label lblTime = new Label(timeMsg);
        lblTime.setStyle("-fx-font-size: 8px; -fx-text-fill: #777;");
        Region spacerNameTime = new Region();                    // khoảng đẩy
        HBox.setHgrow(spacerNameTime, Priority.ALWAYS);         // cho phép spacer giãn hết mức
        HBox hBoxNameTime = new HBox();
        hBoxNameTime.getChildren().addAll(lblName, spacerNameTime, lblTime);

        // TextMsg + Isread
        Label lblTextMsg = new Label(textMsg);
        // Nếu unread thì in đậm, còn read thì bình thường
        if ("unread".equals(isRead)) {
            lblTextMsg.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
        } else {
            lblTextMsg.setStyle("-fx-font-size: 10px; -fx-font-weight: normal;");
        }
        String iconPath = isRead.equals("read")
                ? "./images/tick.png"
                : "./images/unread.png";
        ImageView iconIsread = new ImageView(new Image(iconPath));
        iconIsread.setFitWidth(12);
        iconIsread.setFitHeight(12);
        Region spacerTxtMsg = new Region();
        HBox.setHgrow(spacerTxtMsg, Priority.ALWAYS);
        HBox hBoxTxtMsgIsread = new HBox(lblTextMsg, spacerTxtMsg, iconIsread);
        hBoxTxtMsgIsread.setAlignment(Pos.CENTER_RIGHT);

        Region spacerVertical = new Region();
        VBox.setVgrow(spacerVertical, Priority.ALWAYS);
        VBox.setVgrow(spacerVertical, Priority.ALWAYS); // cho spacer giãn hết mức dọc
        VBox vBoxMsglog= new VBox(hBoxNameTime,spacerVertical, hBoxTxtMsgIsread);

        vBoxMsglog.setMaxWidth(Double.MAX_VALUE);      // Cho phép VBox giãn

        // Container
        HBox boxMsgDialog = new HBox(5, avatar, vBoxMsglog); // khoảng cách 5px
        //boxMsgDialog.setAlignment(Pos.CENTER_LEFT);
        boxMsgDialog.setMaxWidth(Double.MAX_VALUE);        // cho HBox giãn full
        HBox.setHgrow(vBoxMsglog, Priority.ALWAYS);
        //HBox.setHgrow(boxMsgDialog, Priority.ALWAYS);

        return boxMsgDialog;
    }

    private VBox chatVBox; // đưa ra ngoài để các method khác truy cập được
    private ScrollPane scrollPane;

    // Header Chat: thay vì chỉ là Label
    private HBox chatHeader; // đưa ra ngoài để listener truy cập
    private ImageView chatHeaderAvatar;
    private Label chatHeaderName;
    private HBox iconMessBox;
    private HBox iconChatInforBox;

    private String getItemName(HBox item) {
        if (item == null) return "";

        for (Node node : item.getChildren()) {
            if (node instanceof VBox) {
                VBox vbox = (VBox) node;
                // Lấy HBox đầu tiên trong VBox
                if (!vbox.getChildren().isEmpty() && vbox.getChildren().get(0) instanceof HBox) {
                    HBox hBoxNameTime = (HBox) vbox.getChildren().get(0);
                    for (Node inner : hBoxNameTime.getChildren()) {
                        if (inner instanceof Label) {
                            return ((Label) inner).getText();
                        }
                    }
                }
            }
        }
        return "";
    }
    private void updateChatHeader(HBox item) {
        if (item == null) return;

        // Lấy tên
        String name = getItemName(item);
        chatHeaderName.setText(name);

        // Lấy avatar: HBox con đầu tiên chứa VBox con avatar?
        for (Node node : item.getChildren()) {
            if (node instanceof Circle) {
                Circle avatarCircle = (Circle) node;
                ImagePattern pattern = (ImagePattern) avatarCircle.getFill();
                chatHeaderAvatar.setImage(pattern.getImage());
                break;
            } else if (node instanceof ImageView) {
                chatHeaderAvatar.setImage(((ImageView) node).getImage());
                break;
            }
        }
    }
    VBox sectionContent = new VBox();

    // ====== GLOBAL VARIABLES ======
    private ImageView showMoreAvatar;
    private Label showMoreName;
    private Label showMoreStatus;
    private boolean isGroup=false;
    Label numMembersLabel = new Label("Number of members: 15");
    Label pinnedMsgLabel = new Label("View pinned messages");

    private void updateShowMorePanel(HBox item) {
        if (item == null) return;

        sectionContent.getChildren().clear(); // reset nội dung cũ
        if (isGroup) {
            sectionContent.getChildren().add(numMembersLabel);
        }

        sectionContent.getChildren().add(pinnedMsgLabel);
        // Lấy avatar
        for (Node node : item.getChildren()) {
            if (node instanceof ImageView) {
                showMoreAvatar.setImage(((ImageView) node).getImage());
                break;
            } else if (node instanceof Circle) {
                Circle avatarCircle = (Circle) node;
                ImagePattern pattern = (ImagePattern) avatarCircle.getFill();
                showMoreAvatar.setImage(pattern.getImage());
                break;
            }
        }

        // Lấy tên
        String name = getItemName(item); // bạn đã có hàm này
        showMoreName.setText(name);

        // Optional: trạng thái
        showMoreStatus.setText("Active now"); // hoặc lấy từ dữ liệu thực tế

    }

    private Button createIconButton(ImageView icon) {
        Button btn = new Button();
        btn.setGraphic(icon);
        btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        btn.setOnMouseEntered(e -> {
            icon.setScaleX(0.9);
            icon.setScaleY(0.9);
            icon.setOpacity(0.7);
        });

        btn.setOnMouseExited(e -> {
            icon.setScaleX(1);
            icon.setScaleY(1);
            icon.setOpacity(1);
        });

        return btn;
    }

    // ================== Phương thức thêm tin nhắn ==================
    private void addMessage(String text, boolean isMine) {
        Label msgLabel = new Label(text);
        msgLabel.setWrapText(true);
        msgLabel.setPadding(new Insets(8));
        msgLabel.setStyle("-fx-background-radius: 10; -fx-font-size: 14px;");

        if(isMine){
            msgLabel.setStyle(msgLabel.getStyle() + "-fx-background-color: #d8bfd8; -fx-text-fill: white;");
        } else {
            msgLabel.setStyle(msgLabel.getStyle() + "-fx-background-color: #f0f0f0; -fx-text-fill: black;");
        }

        HBox msgHBox = new HBox(msgLabel);
        msgHBox.setMaxWidth(Double.MAX_VALUE);
        if(isMine){
            msgHBox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            msgHBox.setAlignment(Pos.CENTER_LEFT);
        }

        chatVBox.getChildren().add(msgHBox);
        scrollPane.layout();
        scrollPane.setVvalue(1.0); // cuộn xuống cuối
    }
}
