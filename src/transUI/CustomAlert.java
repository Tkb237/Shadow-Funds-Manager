package transUI;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CustomAlert extends Alert {
    private static String currentStyle = "light.css";

    public CustomAlert(AlertType at, String style) {
        super(at);
        String pfad = "icons8-confirm-96.png";
        ;
        if (at == AlertType.CONFIRMATION) {
            pfad = "icons8-confirm-96.png";

        } else if (at == AlertType.INFORMATION) {
            pfad = "icons8-info-96.png";

        } else if (at == AlertType.WARNING) {
            pfad = "icons8-warning-96.png";

        } else {
            pfad = "icon.png";

        }
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getClassLoader().getResource(pfad).toExternalForm()));
        this.getDialogPane().getStylesheets().add(getClass().getResource(style).toExternalForm());
        currentStyle = style;
    }

    public static String getCurrentStyle() {
        return currentStyle;
    }
}
