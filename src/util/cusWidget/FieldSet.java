package util.cusWidget;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class FieldSet extends StackPane {
    private Label title;

    public FieldSet(String titleString, Node content) {
        title = new Label(" " + titleString + " ");
        title.getStyleClass().add("bordered-titled-title");
        StackPane.setAlignment(title, Pos.TOP_CENTER);

        StackPane contentPane = new StackPane();
        content.getStyleClass().add("bordered-titled-content");
        contentPane.getChildren().add(content);

        getStyleClass().add("bordered-titled-border");
        getChildren().addAll(title, contentPane);
        getStylesheets().add(getClass().getResource("/style/field_set.css").toExternalForm());
        setId("id-text");

    }

    public void setCusStyle(boolean b) {
        if (b) {
            title.setStyle("-fx-background-color: #e0e0e0;");
        } else {
            title.setStyle("-fx-background-color: #1c1c24;");

        }
    }
}
