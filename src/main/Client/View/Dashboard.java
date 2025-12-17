package main.Client.View;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Dashboard extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SidebarController sidebarController = new SidebarController();

        BorderPane root = new BorderPane();
        root.setLeft(sidebarController.getSidebar());
        root.setCenter(sidebarController.getContentPane());

        Scene scene = new Scene(root);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setTitle("HaloMeet");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
