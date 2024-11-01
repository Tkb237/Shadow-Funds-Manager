package util.cusWidget;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import transUI.TransaktionUi;

import java.util.Timer;
import java.util.TimerTask;

public class CustomAlert extends Alert {

    private static String style = "cusAlertLight.css";

    public CustomAlert(AlertType at) {
        super(at);
        String pfad;
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
        stage.getIcons().add(new Image(getClass().getResource("/icons/"+pfad).toExternalForm()));
        this.getDialogPane().getStylesheets().add(getClass().getResource(TransaktionUi.SRC+style).toExternalForm());
    }

    public void  showTemp()
    {
        initModality(Modality.NONE);
        show();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                Platform.runLater(CustomAlert.this::close);
                timer.cancel();
            }
        }, 1000);

    }

    public static void setStyle(String style) {
        CustomAlert.style = style;
    }
}
