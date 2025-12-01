package main.View;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MeetingUI extends StackPane {
    public MeetingUI() {
        Label label = new Label("Meeting Screen");
        this.getChildren().add(label);
    }
}
